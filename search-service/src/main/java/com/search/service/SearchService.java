package com.search.service;

import com.search.pojo.ProductEs;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 文件名: SearchService
 * 创建者: @一茶
 * 创建时间:2025/4/29 18:08
 * 描述：
 */
@Service
public interface SearchService {
    List<ProductEs> searchProducts(String keyword, Integer categoryId, BigDecimal price, int page, int size);

    List<ProductEs> searchProductsInMerchant(String keyword, Integer merchantId, Integer categoryId, int page, int size);
}
