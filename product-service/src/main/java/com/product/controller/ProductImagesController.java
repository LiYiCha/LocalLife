package com.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.utils.Result;
import com.product.pojo.ProductImages;
import com.product.service.ProductImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品图片表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@RestController
@RequestMapping("/productImages")
public class ProductImagesController {

    @Autowired
    private ProductImagesService productImagesService;

    /**
     * 根据商品id获取图片
     * @param productId
     * @return
     */
    @GetMapping("/product/{productId}")
    public Result getImagesByProduct(@RequestParam("productId") Integer productId) {
        QueryWrapper<ProductImages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId)
                .orderByAsc("sort_order");
        List<ProductImages> images = productImagesService.list(queryWrapper);
        return Result.success(images);
    }

    /**
     * 创建图片
     * @param image
     * @return
     */
    @PostMapping("/add")
    public Result createImage(@RequestBody ProductImages image) {
        boolean success = productImagesService.save(image);
        return success ? Result.success() : Result.error("创建图片失败");
    }

    /**
     * 更新图片
     * @param image
     * @return
     */
    @PostMapping("/update")
    public Result updateImage(@RequestBody ProductImages image) {
        boolean success = productImagesService.updateById(image);
        return success ? Result.success() : Result.error("更新图片失败");
    }

    /**
     * 删除图片
     * @param id
     * @return
     */
    @PostMapping("/del/{id}")
    public Result deleteImage(@RequestParam("id") Integer id) {
        boolean success = productImagesService.removeById(id);
        return success ? Result.success() : Result.error("删除图片失败");
    }
}
