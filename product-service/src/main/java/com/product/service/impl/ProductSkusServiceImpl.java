package com.product.service.impl;

import com.core.utils.RedisUtil;
import com.core.utils.Result;
import com.product.dto.ProductStockDTO;
import com.product.pojo.ProductSkus;
import com.product.mapper.ProductSkusMapper;
import com.product.service.ProductSkusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductSkusServiceImpl extends ServiceImpl<ProductSkusMapper, ProductSkus> implements ProductSkusService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ProductSkusMapper productSkusMapper;

    // 缓存配置
    private static final String STOCK_CACHE_PREFIX = "product:stock:";
    private final Cache<Integer, Integer> localStockCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.SECONDS) // 本地缓存5秒
            .maximumSize(1000) // 最多缓存1000个商品
            .build();

    @Override
    public Result checkStock(List<ProductStockDTO> stockList) {
        List<String> insufficientStockMessages = new ArrayList<>();

        // 1. 批量查询本地缓存
        Map<Integer, Integer> localCacheMap = localStockCache.getAllPresent(
                stockList.stream().map(ProductStockDTO::getProductId).collect(Collectors.toSet())
        );

        // 2. 批量查询Redis缓存
        List<String> redisKeys = stockList.stream()
                .map(dto -> STOCK_CACHE_PREFIX + dto.getProductId())
                .collect(Collectors.toList());
        Map<String, Integer> redisCacheMap = redisUtil.multiGet(redisKeys);

        // 3. 处理缓存命中
        stockList.forEach(dto -> {
            Integer stock = localCacheMap.get(dto.getProductId());
            if (stock == null) {
                stock = redisCacheMap.get(STOCK_CACHE_PREFIX + dto.getProductId());
            }
            if (stock != null && stock < dto.getQuantity()) {
                insufficientStockMessages.add("商品ID：" + dto.getProductId() + " 库存不足");
            }
        });

        // 4. 处理缓存未命中
        if (!insufficientStockMessages.isEmpty()) {
            return Result.error(String.join("；", insufficientStockMessages));
        } else {
            return Result.success();
        }
    }

    @Override
    @GlobalTransactional(name = "deduct-stock-tx", rollbackFor = Exception.class)
    public Result deductStock(List<ProductStockDTO> stockList) {
        try {
            // 1. 按商品ID分组统计总数量
            Map<Integer, Integer> productQuantityMap = stockList.stream()
                    .collect(Collectors.groupingBy(ProductStockDTO::getProductId,
                            Collectors.summingInt(ProductStockDTO::getQuantity)));

            // 2. 遍历每个商品进行库存扣减
            for (Map.Entry<Integer, Integer> entry : productQuantityMap.entrySet()) {
                Integer productId = entry.getKey();
                Integer totalDeduct = entry.getValue();

                // 3. 查询该商品的所有SKU（带悲观锁）
                List<ProductSkus> skus = productSkusMapper.selectByProductIdForUpdate(productId);

                // 4. 按库存优先级排序（如价格低的优先）
                skus.sort(Comparator.comparing(ProductSkus::getPrice));

                // 5. 循环扣减库存
                for (ProductSkus sku : skus) {
                    if (totalDeduct <= 0) break;

                    int deduct = Math.min(totalDeduct, sku.getStockQuantity());
                    if (deduct > 0) {
                        int updated = productSkusMapper.deductStock(sku.getSkuId(), deduct);
                        if (updated > 0) {
                            totalDeduct -= deduct;
                            // 6. 更新缓存
                            String cacheKey = STOCK_CACHE_PREFIX + productId;
                            redisUtil.decrement(cacheKey, deduct);
                            localStockCache.invalidate(productId);
                        }
                    }
                }

                // 7. 如果剩余需扣减数量大于0，则库存不足
                if (totalDeduct > 0) {
                    return Result.error("商品ID:" + productId + " 库存不足");
                }
            }
            return Result.success();
        } catch (Exception e) {
            log.error("库存扣减失败", e);
            return Result.error(e.getMessage());
        }
    }

    @Override
    @GlobalTransactional(name = "restore-stock-tx", rollbackFor = Exception.class)
    public Result restoreStock(List<ProductStockDTO> stockList) {
        try {
            // 1. 按商品ID分组统计总数量
            Map<Integer, Integer> productQuantityMap = stockList.stream()
                    .collect(Collectors.groupingBy(ProductStockDTO::getProductId,
                            Collectors.summingInt(ProductStockDTO::getQuantity)));

            // 2. 遍历每个商品进行库存恢复
            for (Map.Entry<Integer, Integer> entry : productQuantityMap.entrySet()) {
                Integer productId = entry.getKey();
                Integer totalRestore = entry.getValue();

                // 3. 查询该商品的所有SKU（带悲观锁）
                List<ProductSkus> skus = productSkusMapper.selectByProductIdForUpdate(productId);

                // 4. 循环恢复库存
                for (ProductSkus sku : skus) {
                    if (totalRestore <= 0) break;

                    int restore = Math.min(totalRestore, Integer.MAX_VALUE);
                    if (restore > 0) {
                        int updated = productSkusMapper.restoreStock(sku.getSkuId(), restore);
                        if (updated > 0) {
                            totalRestore -= restore;
                            // 5. 更新缓存
                            String cacheKey = STOCK_CACHE_PREFIX + productId;
                            redisUtil.increment(cacheKey, restore);
                            localStockCache.invalidate(productId);
                        }
                    }
                }

                // 6. 如果剩余需恢复数量大于0，则库存恢复失败
                if (totalRestore > 0) {
                    return Result.error("商品ID:" + productId + " 库存恢复失败");
                }
            }
            return Result.success();
        } catch (Exception e) {
            log.error("库存恢复失败", e);
            return Result.error(e.getMessage());
        }
    }
}
