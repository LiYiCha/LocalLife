package com.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.utils.Result;
import com.product.config.RabbitMQProducer;
import com.product.dto.ProductStockDTO;
import com.product.pojo.ProductSkus;
import com.product.service.ProductSkusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品SKU表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Api(tags = "商品SKU控制器")
@RestController
@RequestMapping("/api/product/productSkus")
public class ProductSkusController {

    @Autowired
    private ProductSkusService productSkusService;
    @Resource
    private RabbitMQProducer rabbitMQProducer;
    /**
     * 批量校验库存
     *
     * @param stockList
     * @return
     */
    @ApiOperation(value = "批量校验库存")
    @PostMapping("/checkStock")
    public Result checkStock(@RequestBody List<ProductStockDTO> stockList) {
        return productSkusService.checkStock(stockList);
    }

    /**
     * 批量扣减库存
     *
     * @param stockList
     * @return
     */
    @ApiOperation(value = "批量扣减库存")
    @PostMapping("/deductStock")
    public Result deductStock(@RequestBody List<ProductStockDTO> stockList) {
        return productSkusService.deductStock(stockList);
    }

    /**
     * 批量恢复库存
     *
     * @param stockList
     * @return
     */
    @ApiOperation(value = "批量恢复库存")
    @PostMapping("/restoreStock")
    public Result restoreStock(@RequestBody List<ProductStockDTO> stockList) {
        return productSkusService.restoreStock(stockList);
    }
    /**
     * 根据商品ID获取SKU列表
     *
     * @param productId
     * @return
     */
    @ApiOperation(value = "根据商品ID获取SKU列表")
    @GetMapping("/product")
    public Result getSkusByProduct(@RequestParam("productId") Integer productId) {
        QueryWrapper<ProductSkus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        List<ProductSkus> skus = productSkusService.list(queryWrapper);
        return Result.success(skus);
    }

    /**
     * 创建SKU
     *
     * @param sku
     * @return
     */
    @ApiOperation(value = "创建SKU")
    @PostMapping("add")
    public Result createSku(@RequestBody ProductSkus sku) {
        boolean success = productSkusService.save(sku);
        if (success){
            String message = "{\"type\": \"product\",\"productId\": "+sku.getProductId()+",\"action\": \"create\"}";
            rabbitMQProducer.sendProductSyncEvent(message);
        }
        return success ? Result.success() : Result.error("创建SKU失败");
    }

    /**
     * 更新SKU
     *
     * @param sku
     * @return
     */
    @ApiOperation(value = "更新SKU")
    @PostMapping("/update")
    public Result updateSku(@RequestBody ProductSkus sku) {
        boolean success = productSkusService.updateById(sku);
        if (success){
            String message = "{\"type\": \"product\",\"productId\": "+sku.getProductId()+",\"action\": \"update\"}";
            rabbitMQProducer.sendProductSyncEvent(message);
        }
        return success ? Result.success() : Result.error("更新SKU失败");
    }

    /**
     * 删除SKU
     *
     * @param skuId
     * @return
     */
    @ApiOperation(value = "删除SKU")
    @PostMapping("/del")
    public Result deleteSku(@RequestParam("skuId") Integer skuId) {
        // 先查出该 SKU 对应的 productId
        ProductSkus skus = productSkusService.getById(skuId);
        if (skus == null) {
            return Result.error("SKU不存在");
        }
        Integer productId = skus.getProductId();
        //  删除SKU
        boolean success = productSkusService.removeById(skuId);
        // 发送消息到消息队列
        if (success){
            String message = "{\"type\": \"product\",\"productId\": "+productId+",\"action\": \"delete\"}";
            rabbitMQProducer.sendProductSyncEvent(message);
        }
        return success ? Result.success() : Result.error("删除SKU失败");
    }
}
