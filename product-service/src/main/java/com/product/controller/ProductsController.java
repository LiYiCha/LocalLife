package com.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.utils.Result;
import com.dataresource.pojo.ProductEs;
import com.product.dto.ProductDetailDTO;
import com.product.pojo.Products;
import com.product.service.ProductsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Api(tags = "商品信息控制器")
@Slf4j
@RestController
@RequestMapping("/api/product/products")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    /**
     * 分页查询商品列表
     */
    @ApiOperation(value = "分页查询商品列表")
    @GetMapping("/page")
    public Result getProductsPage(
            @RequestParam(defaultValue = "1", required = false,name = "page") Integer page,
            @RequestParam(defaultValue = "10",required = false,name = "size") Integer size) {
        Page<ProductDetailDTO> pageResult = productsService.getProductsPage(page,size);
        return Result.success(pageResult);
    }

    /**
     * 商品搜索
     */
    @ApiOperation(value = "商品搜索")
    @GetMapping("/search")
    public Result searchProducts(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "merchantId", required = false) Integer merchantId,
            @RequestParam(value = "page", defaultValue = "1",required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10",required = false) Integer size) {
        return productsService.searchProducts(keyword,merchantId, page,size);
    }
    /**
     * 增加销量
     */
    @ApiOperation(value = "增加销量")
    @PostMapping("/addSalesBatch")
    public Result addSalesBatch(@RequestBody List<Map<String, Integer>> stockList){
        boolean success = productsService.addSealedCount(stockList);
        return success ? Result.success() : Result.error("增加销量失败");
    }

    /**
     * 根据商品ID获取商品信息
     */
    @ApiOperation(value = "根据商品ID获取商品信息")
    @GetMapping("/getById")
    public Result getProductById(@RequestParam("id") Integer product_id) {
        return productsService.getProductsInfo(product_id);
    }

    /**
     * 创建商品
     */
    @ApiOperation(value="创建商品")
    @PostMapping("/add")
    public Result createProduct(@RequestBody Products product) {
        boolean success = productsService.addProducts(product);
        return success ? Result.success() : Result.error("创建商品失败");
    }

    /**
     * 更新商品信息
     */
    @ApiOperation(value="更新商品信息")
    @PostMapping("/update")
    public Result updateProduct(@RequestBody Products product) {
        boolean success = productsService.updateByProductId(product);
        return success ? Result.success() : Result.error("更新商品失败");
    }

    /**
     * 删除商品
     */
    @ApiOperation(value="删除商品")
    @PostMapping("/del")
    public Result deleteProduct(@RequestParam("id") Integer id) {
        return productsService.deleteProduct(id);
    }

    /**
     * 批量删除商品
     */
    @ApiOperation(value="批量删除商品")
    @PostMapping("/batchDelete")
    public Result batchDeleteProducts(@RequestBody List<Integer> ids) {
        return productsService.batchDeleteProducts(ids);
    }

    /**
     * 获取热门商品
     */
    @ApiOperation(value="获取热门商品")
    @GetMapping("/hot")
    public Result getHootProducts(@RequestParam(defaultValue = "1",name="page",required = false) Integer page,
                                  @RequestParam(defaultValue = "10",name="size",required = false) Integer size) {
        return Result.success(productsService.getHootProducts(page,size));
    }

    /**
     * 获取分类树
     */
    @ApiOperation(value = "获取分类树")
    @GetMapping("/categoryTree")
    public Result getCategoryTree(
            @RequestParam(name="merchantId",required = false) Integer merchantId,
            @RequestParam(name="page",defaultValue = "1", required = false) Integer page,
            @RequestParam(name="size",defaultValue = "10", required = false) Integer size
    ) {
        Page<Map<String, Object>> pageRequest = new Page<>(page, size);
        Page<Map<String, Object>> categoryTree = productsService.getCategoryTree(merchantId, pageRequest);
        return Result.success(categoryTree);
    }



    /**
     * 根据分类名称查询商品
     */
    @ApiOperation(value = "根据分类名称查询商品")
    @GetMapping("/byCategoryName")
    public Result getProductsByCategoryName(
            @RequestParam("categoryName") String categoryName,
            @RequestParam(name = "merchantId",required = false) Integer merchantId,
            @RequestParam(name = "page",defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size",defaultValue = "10", required = false) Integer size
    ) {
        Page<ProductDetailDTO> pageRequest = new Page<>(page, size);
        Page<ProductDetailDTO> productsPage = productsService.getProductsByCategoryName(categoryName, merchantId,pageRequest);
        return Result.success(productsPage);
    }




    /**
     * 根据二级分类ID查询商品
     * @param categoryId
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "根据二级分类ID查询商品")
    @GetMapping("/bySubCategory")
    public Result getProductsBySubCategory(
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam(name="merchantId",required = false) Integer merchantId,
            @RequestParam(defaultValue = "1", required = false,name = "page") Integer page,
            @RequestParam(defaultValue = "10", required = false,name = "size") Integer size) {
        Page<ProductDetailDTO>  pageRequest = new Page<>(page, size);
        Page<ProductDetailDTO> productsPage = productsService.getProductsBySubCategory(categoryId,merchantId, pageRequest);
        return Result.success(productsPage);
    }

    @ApiOperation(value ="商家获取商品列表")
    @GetMapping("/getByMerchant")
    public Result getByMerchant(@RequestParam("merchantId") Integer merchantId,
                                @RequestParam(defaultValue = "1", required = false,name = "page") Integer page,
                                @RequestParam(defaultValue = "10", required = false,name = "size") Integer size) {
        Page<ProductDetailDTO>  pageRequest = new Page<>(page, size);
        Page<ProductDetailDTO> productsPage = productsService.getProductsByMerchantId(merchantId, pageRequest);
        return Result.success(productsPage);
    }
    /**
     * ES首次全量同步数据
     */
    @GetMapping("/es/all")
    public Page<ProductEs> getAllProductsForEs(@RequestParam("page") int page, @RequestParam int size) {
        return productsService.fetchAllProductsES((page - 1) * size, size);
    }

    /**
     * 获取商品到ES
     * @param productId
     * @return
     */
    @GetMapping("/es")
    public List<ProductEs> getProductForEs(@RequestParam("productId") int productId) {
        return productsService.fetchOneForEs(productId);
    }

}
