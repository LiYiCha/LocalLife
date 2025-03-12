package com.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.utils.Result;
import com.product.pojo.Products;
import com.product.service.ProductsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Slf4j
@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    /**
     * 分页查询商品列表
     */
    @GetMapping("/page")
    public Result getProductsPage(
            @RequestParam(defaultValue = "1", required = false,name = "page") Integer page,
            @RequestParam(defaultValue = "10",required = false,name = "size") Integer size) {
        Page<Products> pageResult = productsService.getProductsPage(page,size);
        return Result.success(pageResult);
    }
    /**
     * 商品搜索
     *
     * @param keyword 搜索关键词
     * @param limit   返回结果数量
     * @return 搜索结果
     */
    @GetMapping("/search")
    public Result searchProducts(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return productsService.searchProducts(keyword, limit);
    }
    /**
     * 增加销量
     */
    @PostMapping("/addSalesBatch")
    public Result addSalesBatch(@RequestBody List<Map<String, Integer>> stockList){
        boolean success = productsService.addSealedCount(stockList);
        return success ? Result.success() : Result.error("增加销量失败");
    }

    /**
     * 根据商品ID获取商品信息
     */
    @GetMapping("/getById/{id}")
    public Result getProductById(@RequestParam("id") Integer product_id) {
        return productsService.getProductsInfo(product_id);
    }

    /**
     * 创建商品
     */
    @PostMapping("/add")
    public Result createProduct(@RequestBody Products product) {
        boolean success = productsService.save(product);
        return success ? Result.success() : Result.error("创建商品失败");
    }

    /**
     * 更新商品信息
     */
    @PostMapping("/update")
    public Result updateProduct(@RequestBody Products product) {
        boolean success = productsService.updateById(product);
        return success ? Result.success() : Result.error("更新商品失败");
    }

    /**
     * 删除商品
     */
    @PostMapping("/del/{id}")
    public Result deleteProduct(@RequestParam("id") Integer id) {
        return productsService.deleteProduct(id);
    }

    /**
     * 批量删除商品
     */
    @PostMapping("/batchDelete")
    public Result batchDeleteProducts(@RequestBody List<Integer> ids) {
        return productsService.batchDeleteProducts(ids);
    }
}
