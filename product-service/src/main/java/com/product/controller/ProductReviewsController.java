package com.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.utils.Result;
import com.product.pojo.ProductReviews;
import com.product.service.ProductReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 商品评价表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@RestController
@RequestMapping("/productReviews")
public class ProductReviewsController {

    @Autowired
    private ProductReviewsService productReviewsService;

    /**
     * 根据商品ID获取评价
     *
     * @param productId 商品ID
     * @param page      页码
     * @param size      每页数量
     * @return 评价列表
     */
    @GetMapping("/product/{productId}")
    public Result getReviewsByProduct(
            @RequestParam("productId") Integer productId,
            @RequestParam(defaultValue = "1",name = "page",required = false) Integer page,
            @RequestParam(defaultValue = "10",name = "size",required = false) Integer size) {
        QueryWrapper<ProductReviews> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId)
                .orderByDesc("created_time");
        Page<ProductReviews> pageResult = productReviewsService.page(new Page<>(page, size), queryWrapper);
        return Result.success(pageResult);
    }

    /**
     * 创建评价
     *
     * @param review 评价对象
     * @return 创建结果
     */
    @PostMapping("/add")
    public Result createReview(@RequestBody ProductReviews review) {
        boolean success = productReviewsService.save(review);
        return success ? Result.success() : Result.error("创建评价失败");
    }

    /**
     * 回复评价
     *
     * @param review 评价对象
     * @return 回复结果
     */
    @PostMapping("/reply")
    public Result replyReview(@RequestBody ProductReviews review) {
        review.setReplyTime(LocalDateTime.now());
        boolean success = productReviewsService.updateById(review);
        return success ? Result.success() : Result.error("回复评价失败");
    }

    /**
     * 删除评价
     *
     * @param id 评价ID
     * @return 删除结果
     */
    @PostMapping("/del/{id}")
    public Result deleteReview(@RequestParam("id") Long id) {
        boolean success = productReviewsService.removeById(id);
        return success ? Result.success() : Result.error("删除评价失败");
    }
}
