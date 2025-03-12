package com.product.service.impl;

import com.product.pojo.ProductCategories;
import com.product.mapper.ProductCategoriesMapper;
import com.product.service.ProductCategoriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品分类表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Service
public class ProductCategoriesServiceImpl extends ServiceImpl<ProductCategoriesMapper, ProductCategories> implements ProductCategoriesService {

}
