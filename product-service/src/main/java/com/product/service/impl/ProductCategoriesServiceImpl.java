package com.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.utils.Result;
import com.product.pojo.ProductCategories;
import com.product.mapper.ProductCategoriesMapper;
import com.product.service.ProductCategoriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
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

    @Resource
    private ProductCategoriesMapper pcMapper;

    /**
     * 商家查询分类
     * @param merchantId 商家ID
     * @param page 页码
     * @param size 每页数量
     * @return
     */
    @Override
    public Result getByMerchantId(Integer merchantId, Integer page, Integer size) {
        Page<ProductCategories> page1 = new Page<>(page, size);
        pcMapper.getByMerchantId(merchantId, page1);
        return null;
    }
}
