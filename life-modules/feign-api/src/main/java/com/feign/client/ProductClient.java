package com.feign.client;

import com.core.utils.Result;
import com.feign.dto.ProductStockDTO;
import com.feign.pojo.ProductEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "product-service")
public interface ProductClient {

    // 校验库存
    @PostMapping("/productSkus/checkStock")
    Result checkStock(@RequestBody List<ProductStockDTO> stockList);

    // 扣减库存
    @PostMapping("/productSkus/deductStock")
    Result deductStock(@RequestBody List<ProductStockDTO> stockList);

    // 回滚库存
    @PostMapping("/productSkus/restoreStock")
    Result restoreStock(@RequestBody List<ProductStockDTO> stockList);

    //增加销量
    @PostMapping("/products/addSalesBatch")
    Result addSalesBatch(@RequestBody List<Map<String, Integer>> stockList);

    //查询用户购物车
    @GetMapping("/shoppingCart/user")
    Result getCartByUser(@RequestParam("userId") Integer userId);

    //批量删除购物车
    @PostMapping("/shoppingCart/batchDelete")
    Result deleteCartItems(@RequestBody List<Integer> ids);

    //ES增量同步数据
    @GetMapping("/products/es")
    ProductEs getProductForEs(@RequestParam("productId") Integer productId);

    //ES全量同步数据
    @GetMapping("/products/es/all")
    List<ProductEs> syncAllProductsToES(@RequestParam("page") Integer page , @RequestParam("size") Integer size);
}
