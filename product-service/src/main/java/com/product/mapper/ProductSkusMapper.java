package com.product.mapper;

import com.product.pojo.ProductSkus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 商品SKU表 Mapper 接口
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface ProductSkusMapper extends BaseMapper<ProductSkus> {

        // 批量查询商品SKU列表
        List<ProductSkus> batchSelectForUpdate(@Param("productIds") List<Integer> productIds);

        // 查询商品SKU列表
        @Select("SELECT * FROM product_skus WHERE product_id = #{productId} FOR UPDATE")
        List<ProductSkus> selectByProductIdForUpdate(Integer productId);

        // 扣减库存
        @Update("UPDATE product_skus SET stock_quantity = stock_quantity - #{quantity} " +
                "WHERE sku_id = #{skuId} AND stock_quantity >= #{quantity}")
        int deductStock(@Param("skuId") Integer skuId, @Param("quantity") Integer quantity);

        // 还原库存(保证库存不为0)
        @Update("UPDATE product_skus SET stock_quantity = GREATEST(0, stock_quantity + #{restore}) " +
                "WHERE sku_id = #{skuId}")
        int restoreStock(@Param("skuId") Integer skuId, @Param("restore") Integer restore);

        //删除商品SKU
        @Update("DELETE FROM product_skus WHERE product_id = #{productId}")
        int deleteByProductId(Integer productId);
}
