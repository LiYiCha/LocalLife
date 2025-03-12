package com.product.mapper;

import com.product.pojo.ProductImages;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

/**
 * <p>
 * 商品图片表 Mapper 接口
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface ProductImagesMapper extends BaseMapper<ProductImages> {

    //删除商品图片
    @Delete("DELETE FROM product_images WHERE product_id = #{productId}")
    int deleteByProductId(int productId);
}
