package com.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.utils.Result;
import com.product.dto.ProductDetailDTO;
import com.product.mapper.ProductImagesMapper;
import com.product.mapper.ProductReviewsMapper;
import com.product.mapper.ProductSkusMapper;
import com.product.mapper.ProductsMapper;
import com.product.pojo.Products;
import com.product.service.ProductsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param limit
     * @return
     */
    @Override
    public Result searchProducts(String keyword, int limit) {
        try {
            // 1. 模糊搜索商品
            List<Products> searchResults = productsMapper.searchByKeyword(keyword, limit);

            // 2. 如果搜索结果不足，补充推荐商品
            if (searchResults.size() < limit) {
                int remaining = limit - searchResults.size();
                List<Products> recommendedProducts = productsMapper.getRecommendedProducts(remaining);
                searchResults.addAll(recommendedProducts);
            }

            // 3. 返回结果
            return Result.success(searchResults);
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
            ProductDetailDTO productDetail = productsMapper.getProductDetailById(productId);
            if (productDetail == null) {
                return Result.error("商品不存在");
            }

            // 2. 返回结果
            return Result.success(productDetail);
        } catch (Exception e) {
            log.error("获取商品信息失败", e);
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
    public Page<Products> getProductsPage(int page, int size) {
            Page<Products> pageResult = productsMapper.getProductsPage(new Page<>(page, size));
            return pageResult;
    }

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
            // 5. 返回结果
            return Result.success();
        } catch (Exception e) {
            log.error("删除商品及其相关数据失败", e);
            return Result.error("删除商品及其相关数据失败");
        }
    }
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
            }

            // 5. 返回结果
            return Result.success();
        } catch (Exception e) {
            log.error("批量删除商品及其相关数据失败", e);
            return Result.error("批量删除商品及其相关数据失败");
        }
    }
}
