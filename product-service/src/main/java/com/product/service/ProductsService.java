package com.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.utils.Result;
import com.dataresource.pojo.ProductEs;
import com.product.dto.ProductDetailDTO;
import com.product.pojo.Products;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息表 服务类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface ProductsService extends IService<Products> {

    boolean addSealedCount(List<Map<String, Integer>> stockList);

    Result searchProducts(String keyword, Integer merchantId, Integer page,Integer size);

    Result getProductsInfo(int product_id);

    Page<ProductDetailDTO> getProductsPage(Integer page, Integer size);

    @Transactional
    Result deleteProduct(int productId);

    @Transactional
    Result batchDeleteProducts(List<Integer> productIds);


    Page<ProductDetailDTO> getHootProducts(Integer page, Integer size);

    Page<Map<String, Object>> getCategoryTree(Integer merchantId, Page<Map<String, Object>> page);

    Page<ProductDetailDTO> getProductsByCategoryName(String categoryName, Integer merchantId, Page<ProductDetailDTO> page);

    Page<ProductDetailDTO> getProductsBySubCategory(Integer categoryId,Integer merchantId, Page<ProductDetailDTO> page);

    List<ProductEs> fetchOneForEs(int productId);

    Page<ProductEs> fetchAllProductsES(int i, int size);

    boolean addProducts(Products product);

    boolean updateByProductId(Products product);

    Page<ProductDetailDTO> getProductsByMerchantId(Integer merchantId, Page<ProductDetailDTO> pageRequest);
}
