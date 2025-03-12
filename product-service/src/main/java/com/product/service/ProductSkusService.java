package com.product.service;

import com.core.utils.Result;
import com.product.dto.ProductStockDTO;
import com.product.pojo.ProductSkus;
import com.baomidou.mybatisplus.extension.service.IService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 商品SKU表 服务类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface ProductSkusService extends IService<ProductSkus> {
    Result checkStock(List<ProductStockDTO> stockList);

    Result deductStock(List<ProductStockDTO> stockList);

    @GlobalTransactional(name = "restore-stock-tx", rollbackFor = Exception.class)
    Result restoreStock(List<ProductStockDTO> stockList);
}
