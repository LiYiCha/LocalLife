package com.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.utils.Result;
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

    Result searchProducts(String keyword, int limit);

    Result getProductsInfo(int product_id);

    Page<Products> getProductsPage(int page, int size);

    @Transactional
    Result deleteProduct(int productId);

    @Transactional
    Result batchDeleteProducts(List<Integer> productIds);
}
