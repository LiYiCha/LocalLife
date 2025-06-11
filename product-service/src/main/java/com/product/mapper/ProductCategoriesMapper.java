package com.product.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.product.pojo.ProductCategories;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商品分类表 Mapper 接口
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface ProductCategoriesMapper extends BaseMapper<ProductCategories> {

    Page<ProductCategories> getByMerchantId(Integer merchantId, Page<ProductCategories> page);
}
