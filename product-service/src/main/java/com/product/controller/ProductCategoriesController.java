package com.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.utils.Result;
import com.product.pojo.ProductCategories;
import com.product.service.ProductCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商品分类表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@RestController
@RequestMapping("/productCategories")
public class ProductCategoriesController {

    @Autowired
    private ProductCategoriesService productCategoriesService;

    /**
     * 分页查询商品分类
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/page")
    public Result getCategoriesPage(
            @RequestParam(defaultValue = "1",name = "page",required = false) Integer page,
            @RequestParam(defaultValue = "10",name = "size",required = false) Integer size) {
        Page<ProductCategories> pageResult = productCategoriesService.page(new Page<>(page, size));
        return Result.success(pageResult);
    }

    /**
     * 根据id查询商品分类
     *
     * @param id
     * @return
     */
    @GetMapping("/getById/{id}")
    public Result getCategoryById(@RequestParam("id") Integer id) {
        ProductCategories category = productCategoriesService.getById(id);
        return Result.success(category);
    }

    /**
     * 创建商品分类
     *
     * @param category
     * @return
     */
    @PostMapping("/add")
    public Result createCategory(@RequestBody ProductCategories category) {
        boolean success = productCategoriesService.save(category);
        return success ? Result.success() : Result.error("创建分类失败");
    }

    /**
     * 更新商品分类
     *
     * @param category
     * @return
     */
    @PostMapping("/update")
    public Result updateCategory(@RequestBody ProductCategories category) {
        boolean success = productCategoriesService.updateById(category);
        return success ? Result.success() : Result.error("更新分类失败");
    }

    /**
     * 删除商品分类
     *
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public Result deleteCategory(@RequestParam("id") Integer id) {
        boolean success = productCategoriesService.removeById(id);
        return success ? Result.success() : Result.error("删除分类失败");
    }
}
