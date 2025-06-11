package com.product.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataresource.pojo.ProductEs;
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
     * @param merchantId
     * @param page
     * @return
     */
    Page<ProductDetailDTO> searchByKeyword(@Param("keyword") String keyword,
                                   @Param("merchantId") Integer merchantId,
                                   @Param("page") Page<ProductDetailDTO> page);

    /**
     * 获取推荐商品
     * @param limit
     * @return
     */
    @Select("SELECT p.product_id, p.product_name, p.description, p.status,p.main_image AS mainImage, " +
            "MIN(ps.price) AS min_price " +
            "FROM products p " +
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
    List<ProductDetailDTO> getProductDetailById(@Param("productId") int productId);

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
     * 分页查询商品列表
     * @param page
     * @return
     */
    Page<ProductDetailDTO> getProductsPage(Page<ProductDetailDTO> page);


    /**
     * 分页查询热销（推荐）商品
     * @param page 分页对象
     * @return 热销商品列表
     */
    Page<ProductDetailDTO> getHootProducts(Page<ProductDetailDTO> page);

    /**
     * 获取商品分类树
     * @return 商品分类树
     */
    Page<Map<String, Object>> getCategoryTree(
            @Param("merchantId") Integer merchantId,
            @Param("page") Page<Map<String, Object>> page
    );


    /**
     * 根据分类名称查询商品
     * @param categoryName 分类名称
     * @param merchantId 商家ID
     * @param page 分页对象
     * @return 商品列表
     */
    Page<ProductDetailDTO> getProductsByCategoryName(
            @Param("categoryName") String categoryName,
            @Param("merchantId") Integer merchantId,
            @Param("page") Page<ProductDetailDTO> page
    );


    /**
     * 根据二级分类ID查询商品
     * @param categoryId 二级分类ID
     * @param page 分页对象
     * @return 商品列表
     */
    Page<ProductDetailDTO> getProductsBySubCategory(
            @Param("categoryId") Integer categoryId,
            @Param("merchantId") Integer merchantId,
            @Param("page") Page<ProductDetailDTO> page);

    /**
     * ES首次全量同步数据
     */
    Page<ProductEs> fetchAllProductsES(@Param("page") Page<ProductEs> page);


    /**
     * ES增量同步数据
     * @param productId
     * @return
     */
    List<ProductEs> fetchOneForEs(@Param("productId") int productId);
    /**
     * 获取商品总数
     * @return 商品总数
     */
    @Select("SELECT COUNT(*) FROM products")
    Long getTotalProductCount();

    /**
     * 根据商家ID获取商品列表
     * @param merchantId 商家ID
     * @param pageRequest 分页对象
     * @return 商品列表
     */
    Page<ProductDetailDTO> getByMerchant(Integer merchantId, Page<ProductDetailDTO> pageRequest);
}
