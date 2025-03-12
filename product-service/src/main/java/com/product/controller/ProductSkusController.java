package com.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.utils.Result;
import com.product.dto.ProductStockDTO;
import com.product.pojo.ProductSkus;
import com.product.service.ProductSkusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品SKU表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@RestController
@RequestMapping("/productSkus")
public class ProductSkusController {

    @Autowired
    private ProductSkusService productSkusService;

    /**
     * 批量校验库存
     *
     * @param stockList
     * @return
     */
    @PostMapping("/checkStock")
    public Result checkStock(@RequestBody List<ProductStockDTO> stockList) {
        return productSkusService.checkStock(stockList);
    }

    /**
     * 批量扣减库存
     *
     * @param stockList
     * @return
     */
    @PostMapping("/deductStock")
    public Result deductStock(@RequestBody List<ProductStockDTO> stockList) {
        return productSkusService.deductStock(stockList);
    }

    /**
     * 批量恢复库存
     *
     * @param stockList
     * @return
     */
    @PostMapping("/restoreStock")
    public Result restoreStock(@RequestBody List<ProductStockDTO> stockList) {
        return productSkusService.restoreStock(stockList);
    }
    /**
     * 根据商品ID获取SKU列表
     *
     * @param productId
     * @return
     */
    @GetMapping("/product/{productId}")
    public Result getSkusByProduct(@RequestParam("productId") Integer productId) {
        QueryWrapper<ProductSkus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        List<ProductSkus> skus = productSkusService.list(queryWrapper);
        return Result.success(skus);
    }

    /**
     * 创建SKU
     *
     * @param sku
     * @return
     */
    @PostMapping("add")
    public Result createSku(@RequestBody ProductSkus sku) {
        boolean success = productSkusService.save(sku);
        return success ? Result.success() : Result.error("创建SKU失败");
    }

    /**
     * 更新SKU
     *
     * @param sku
     * @return
     */
    @PostMapping("/update")
    public Result updateSku(@RequestBody ProductSkus sku) {
        boolean success = productSkusService.updateById(sku);
        return success ? Result.success() : Result.error("更新SKU失败");
    }

    /**
     * 删除SKU
     *
     * @param id
     * @return
     */
    @PostMapping("/del/{id}")
    public Result deleteSku(@RequestParam("id") Integer id) {
        boolean success = productSkusService.removeById(id);
        return success ? Result.success() : Result.error("删除SKU失败");
    }
}
