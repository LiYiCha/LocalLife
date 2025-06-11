package com.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.utils.Result;
import com.dataresource.pojo.ProductEs;
import com.product.config.RabbitMQProducer;
import com.product.dto.ProductDetailDTO;
import com.product.mapper.ProductImagesMapper;
import com.product.mapper.ProductReviewsMapper;
import com.product.mapper.ProductSkusMapper;
import com.product.mapper.ProductsMapper;
import com.product.pojo.Products;
import com.product.service.ProductsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Slf4j
@Service
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products> implements ProductsService {

    @Resource
    private ProductsMapper productsMapper;

    @Resource
    private ProductImagesMapper productImagesMapper;

    @Resource
    private ProductSkusMapper productSkusMapper;

    @Resource
    private ProductReviewsMapper productReviewsMapper;

    @Resource
    private RabbitMQProducer rabbitMQProducer;

    /**
     * 增加商品
     * @param product
     * @return
     */
    @Override
    public boolean addProducts(Products product) {
        int insert = productsMapper.insert(product);
        if (insert > 0) {
            String message = "{\"type\": \"product\",\"productId\": "+product.getProductId()+",\"action\": \"create\"}";
            rabbitMQProducer.sendProductSyncEvent(message);
        }
        return insert  > 0;
    }

    /**
     * 更新商品
     * @param product
     * @return
     */
    @Override
    public boolean updateByProductId(Products product) {
        int i = productsMapper.updateById(product);
        if (i > 0) {
            String message = "{\"type\": \"product\",\"productId\": "+product.getProductId()+",\"action\": \"update\"}";
            rabbitMQProducer.sendProductSyncEvent(message);
        }
        return i > 0;
    }

    /**
     * 根据商户id获取商品列表
     * @param merchantId
     * @param pageRequest
     * @return
     */
    @Override
    public Page<ProductDetailDTO> getProductsByMerchantId(Integer merchantId, Page<ProductDetailDTO> pageRequest) {
        return productsMapper.getByMerchant(merchantId, pageRequest);
    }

    /**
     * 批量更新库存
     * @param stockList
     * @return
     */
    @Override
    public boolean addSealedCount(List<Map<String, Integer>> stockList) {
        try {
            int updated = productsMapper.addSalesBatch(stockList);
            return updated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 搜索商品
     * @param keyword
     * @param merchantId
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result searchProducts(String keyword, Integer merchantId, Integer page, Integer size) {
        try {
            // 创建分页请求
            Page<ProductDetailDTO> pageRequest = new Page<>(page, size);

            // 1. 模糊搜索商品
            Page<ProductDetailDTO> productsPage = productsMapper.searchByKeyword(keyword, merchantId, pageRequest);

            // 2. 获取搜索结果列表
            List<ProductDetailDTO> searchResults = productsPage.getRecords();

            // 3. 如果搜索结果不足，补充推荐商品
            if (searchResults.size() < size) {
                Integer remaining = size - searchResults.size();
                Page<ProductDetailDTO> recommendPage = new Page<>(1, remaining); // 新页面，获取剩余数量的商品
                Page<ProductDetailDTO> recommendedProducts = productsMapper.getHootProducts(recommendPage);

                // 合并结果
                searchResults.addAll(recommendedProducts.getRecords());
            }

            // 4. 构造新的 Page 对象，保留原始分页信息
            Page<ProductDetailDTO> resultPage = new Page<>();
            resultPage.setCurrent(page);
            resultPage.setSize(size);
            resultPage.setTotal(productsPage.getTotal()); // 可选：是否使用搜索结果总数？
            resultPage.setPages((int) Math.ceil((double) productsPage.getTotal() / size));
            resultPage.setRecords(searchResults);

            // 5. 返回分页结果
            return Result.success(resultPage);
        } catch (Exception e) {
            log.error("搜索商品失败", e);
            return Result.error("搜索商品失败");
        }
    }



    /**
     * 根据商品id获取具体详细信息
     * @param productId
     * @return
     */
    @Override
    public Result getProductsInfo(int productId) {
        try {
            // 1. 查询商品详细信息
            List<ProductDetailDTO> productDetail = productsMapper.getProductDetailById(productId);
            if (productDetail == null) {
                return Result.error("商品不存在");
            }

            // 2. 返回结果
            return Result.success(productDetail);
        } catch (Exception e) {
            return Result.error("获取商品信息失败");
        }
    }

    /**
     * 分页查询商品
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<ProductDetailDTO> getProductsPage(Integer page, Integer size) {
        // 1. 创建分页对象
        Page<ProductDetailDTO> pageRequest = new Page<>(page, size);

        // 2. 调用 Mapper 方法查询商品
        Page<ProductDetailDTO> hotProducts = productsMapper.getProductsPage(pageRequest);

        // 3. 返回结果
        return hotProducts;
    }


    /**
     * 删除商品及其相关数据
     * @param productId
     * @return
     */
    @Override
    @Transactional
    public Result deleteProduct(int productId) {
        try {
            // 1. 删除商品评价
            productReviewsMapper.deleteByProductId(productId);

            // 2. 删除商品SKU
            productSkusMapper.deleteByProductId(productId);

            // 3. 删除商品图片
            productImagesMapper.deleteByProductId(productId);

            // 4. 删除商品
            int success = productsMapper.deleteById(productId);
            if (success == 0) {
                return Result.error("删除商品失败");
            }
            String message = "{\"type\": \"product\",\"productId\": "+productId+",\"action\": \"delete\"}";
            rabbitMQProducer.sendProductSyncEvent(message);
            // 5. 返回结果
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error("删除商品及其相关数据失败");
        }
    }

    /**
     * 批量删除商品及其相关数据
     * @param productIds
     * @return
     */
    @Override
    @Transactional
    public Result batchDeleteProducts(List<Integer> productIds) {
        try {
            for (int productId : productIds) {
                // 1. 删除商品评价
                productReviewsMapper.deleteByProductId(productId);

                // 2. 删除商品SKU
                productSkusMapper.deleteByProductId(productId);

                // 3. 删除商品图片
                productImagesMapper.deleteByProductId(productId);

                // 4. 删除商品
                int success = productsMapper.deleteById(productId);
                if (success == 0) {
                    return Result.error("删除商品ID " + productId + " 失败");
                }
                // 5. 发送消息到消息队列
                String message = "{\"type\": \"product\",\"productId\": "+productId+",\"action\": \"delete\"}";
                rabbitMQProducer.sendProductSyncEvent(message);
            }

            // 5. 返回结果
            return Result.success();
        } catch (Exception e) {
            return Result.error("批量删除商品及其相关数据失败");
        }
    }

    /**
     * 获取热销商品
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<ProductDetailDTO> getHootProducts(Integer page, Integer size) {
        // 1. 创建分页对象
        Page<ProductDetailDTO> pageRequest = new Page<>(page, size);
        // 2. 调用 Mapper 方法查询热销商品
        Page<ProductDetailDTO> hotProducts = productsMapper.getHootProducts(pageRequest);
        // 3. 返回结果
        return hotProducts;
    }

    /**
     * 获取商品分类树
     * @return
     */
    @Override
    public Page<Map<String, Object>> getCategoryTree(Integer merchantId, Page<Map<String, Object>> page) {
        return productsMapper.getCategoryTree(merchantId, page);
    }



    /**
     * 根据分类名称获取商品
     * @param categoryName
     * @param merchantId
     * @param page
     * @return
     */
    @Override
    public Page<ProductDetailDTO> getProductsByCategoryName(String categoryName, Integer merchantId, Page<ProductDetailDTO> page) {
        return productsMapper.getProductsByCategoryName(categoryName, merchantId, page);
    }


    /**
     * 根据子分类ID获取商品
     * @param categoryId
     * @param page
     * @return
     */
    @Override
    public Page<ProductDetailDTO> getProductsBySubCategory(Integer categoryId,Integer merchantId,Page<ProductDetailDTO> page) {
        return productsMapper.getProductsBySubCategory(categoryId,merchantId, page);
    }

    /**
     * 增量同步数据到ES
     * @return
     */
    @Override
    public List<ProductEs> fetchOneForEs(int productId) {
        return productsMapper.fetchOneForEs(productId);
    }

    /**
     * 全量同步数据到ES
     *
     * @return
     */
    @Override
    public Page<ProductEs> fetchAllProductsES(int page, int size) {
        Page<ProductEs> pageRequest = new Page<>(page, size);
        Page<ProductEs> productEsPage = productsMapper.fetchAllProductsES(pageRequest);
        return productEsPage;
    }


}
