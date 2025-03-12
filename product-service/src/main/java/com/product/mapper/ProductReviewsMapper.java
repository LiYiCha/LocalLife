package com.product.mapper;

import com.product.pojo.ProductReviews;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

/**
 * <p>
 * 商品评价表 Mapper 接口
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface ProductReviewsMapper extends BaseMapper<ProductReviews> {

    //删除商品评价
    @Delete("DELETE FROM product_reviews WHERE product_id = #{productId}")
    int deleteByProductId(int productId);
}
