package com.product.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.product.dto.ProductDetailDTO;
import com.product.dto.SkuDTO;
import com.product.pojo.Products;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息表 Mapper 接口
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface ProductsMapper extends BaseMapper<Products> {
    /**
     * 批量更新商品销量
     * @param salesList
     * @return
     */
    @Update("<script>" +
            "UPDATE products " +
            "SET sales_count = sales_count + CASE product_id " +
            "<foreach collection='list' item='item' separator=' '>" +
            "WHEN #{item.productId} THEN #{item.quantity} " +
            "</foreach>" +
            "END " +
            "WHERE product_id IN " +
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>" +
            "#{item.productId}" +
            "</foreach>" +
            "</script>")
    int addSalesBatch(@Param("list") List<Map<String, Integer>> salesList);

    /**
     * 根据关键词搜索商品
     * @param keyword
     * @param limit
     * @return
     */
    @Select("SELECT p.product_id, p.product_name, p.description, p.status, " +
            "MIN(ps.price) AS min_price, pi.image_url AS main_image " +
            "FROM products p " +
            "LEFT JOIN product_images pi ON p.product_id = pi.product_id AND pi.is_main = 1 " +
            "LEFT JOIN product_skus ps ON p.product_id = ps.product_id " +
            "WHERE (p.product_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR p.description LIKE CONCAT('%', #{keyword}, '%')) " +
            "GROUP BY p.product_id, pi.image_url " +
            "ORDER BY min_price ASC " +
            "LIMIT #{limit}")
    List<Products> searchByKeyword(@Param("keyword") String keyword, @Param("limit") int limit);

    /**
     * 获取推荐商品
     * @param limit
     * @return
     */
    @Select("SELECT p.product_id, p.product_name, p.description, p.status, " +
            "MIN(ps.price) AS min_price, MIN(pi.image_url) AS main_image " +
            "FROM products p " +
            "LEFT JOIN product_images pi ON p.product_id = pi.product_id AND pi.is_main = 1 " +
            "LEFT JOIN product_skus ps ON p.product_id = ps.product_id " +
            "GROUP BY p.product_id " +
            "ORDER BY p.sales_count DESC " +
            "LIMIT #{limit}")
    List<Products> getRecommendedProducts(@Param("limit") int limit);


    /**
     * 根据商品ID获取商品详细信息
     *
     * @param productId 商品ID
     * @return 商品详细信息
     */
    @Select("SELECT p.product_id, p.product_name, p.description, p.status, p.sales_count, p.is_recommend, p.sort_order, " +
            "pi.image_url AS image_url, " +
            "ps.sku_id, ps.sku_name, ps.price, ps.stock_quantity, ps.image_url AS sku_image_url " +
            "FROM products p " +
            "LEFT JOIN product_images pi ON p.product_id = pi.product_id " +
            "LEFT JOIN product_skus ps ON p.product_id = ps.product_id " +
            "WHERE p.product_id = #{productId}")
    @Results({
            @Result(property = "productId", column = "product_id"),
            @Result(property = "productName", column = "product_name"),
            @Result(property = "description", column = "description"),
            @Result(property = "status", column = "status"),
            @Result(property = "salesCount", column = "sales_count"),
            @Result(property = "isRecommend", column = "is_recommend"),
            @Result(property = "sortOrder", column = "sort_order"),
            @Result(property = "imageUrls", column = "product_id",
                    many = @Many(select = "getProductImagesByProductId")),
            @Result(property = "skus", column = "product_id",
                    many = @Many(select = "getProductSkusByProductId"))
    })
    ProductDetailDTO getProductDetailById(@Param("productId") int productId);

    /**
     * 根据商品ID获取商品图片列表
     */
    @Select("SELECT image_url FROM product_images WHERE product_id = #{productId}")
    List<String> getProductImagesByProductId(@Param("productId") int productId);

    /**
     * 根据商品ID获取商品SKU列表
     */
    @Select("SELECT sku_id AS skuId, sku_name AS skuName, price, stock_quantity AS stockQuantity, image_url AS imageUrl " +
            "FROM product_skus WHERE product_id = #{productId}")
    List<SkuDTO> getProductSkusByProductId(@Param("productId") int productId);

    /**
     * 分页查询商品
     */
    Page<Products> getProductsPage(Page<Products> page);
}
