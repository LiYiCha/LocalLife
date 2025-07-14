package com.search.controller;

import com.core.utils.Result;
import com.search.service.SearchService;
import com.search.service.impl.EsSyncService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 文件名: SearchController
 * 创建者: @一茶
 * 创建时间:2025/5/5 16:50
 * 描述：
 */
@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;
    private final EsSyncService esSyncService;


    public SearchController(SearchService searchService, EsSyncService esSyncService) {
        this.searchService = searchService;
        this.esSyncService = esSyncService;
    }

    /**
     * 搜索商品
     *
     * @param keyword
     * @param categoryId
     * @param price
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/products")
    public Result searchProducts(String keyword, Integer categoryId, BigDecimal price, int page, int size) {
        return Result.success(searchService.searchProducts(keyword, categoryId, price, page, size));
    }

    /**
     * 搜索商品(商家)
     *
     * @param keyword
     * @param merchantId
     * @param categoryId
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/productsInMerchant")
    public Result searchProductsInMerchant(String keyword, Integer merchantId, Integer categoryId, int page, int size) {
        return Result.success(searchService.searchProductsInMerchant(keyword, merchantId, categoryId, page, size));
    }

    /**
     * 全量同步
     * @param pageSize
     * @return
     */
    @PostMapping("/products/es")
    public Result fullSync(@RequestParam("pageSize") int pageSize){
        return esSyncService.fullSync(pageSize);
    }
}
