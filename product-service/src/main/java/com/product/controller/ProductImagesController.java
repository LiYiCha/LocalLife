package com.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.config.ReggieConfig;
import com.core.utils.FileUploadUtil;
import com.core.utils.Result;
import com.product.pojo.ProductImages;
import com.product.service.ProductImagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 商品图片表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Api(tags = "商品图片控制器")
@RestController
@RequestMapping("/api/product/productImages")
public class ProductImagesController {

    @Autowired
    private ProductImagesService productImagesService;

    @Autowired
    private ReggieConfig reggieConfig;
    /**
     * 根据商品id获取图片
     * @param productId
     * @return
     */
    @ApiOperation(value = "根据商品id获取图片")
    @GetMapping("/product")
    public Result getImagesByProduct(@RequestParam("productId") Integer productId) {
        QueryWrapper<ProductImages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId)
                .orderByAsc("sort_order");
        List<ProductImages> images = productImagesService.list(queryWrapper);
        return Result.success(images);
    }

    /**
     * 创建图片
     * @param image
     * @return
     */
    @ApiOperation(value = "创建图片")
    @PostMapping("/add")
    public Result createImage(@RequestBody ProductImages image) {
        boolean success = productImagesService.save(image);
        return success ? Result.success() : Result.error("创建图片失败");
    }

    /**
     * 更新图片
     * @param image
     * @return
     */
    @ApiOperation(value = "更新图片")
    @PostMapping("/update")
    public Result updateImage(@RequestBody ProductImages image) {
        boolean success = productImagesService.updateById(image);
        return success ? Result.success() : Result.error("更新图片失败");
    }

    /**
     * 删除图片
     * @param id
     * @return
     */
    @ApiOperation(value = "删除图片")
    @PostMapping("/del")
    public Result deleteImage(@RequestParam("id") Integer id) {
        boolean success = productImagesService.removeById(id);
        return success ? Result.success() : Result.error("删除图片失败");
    }

    /**
     * 上传媒体文件
     * @param file
     * @return
     */
    @PostMapping("/uploadFile")
    public Result uploadMedia(@RequestParam("files") MultipartFile file) {
        try {
            String fileName = FileUploadUtil.uploadFile(file, reggieConfig.getPath());
            // 返回媒体访问 URL
            String mediaUrl = "/files/" + fileName;
            return Result.success(mediaUrl);
        } catch (RuntimeException e) {
            return Result.error("上传文件失败:"+e.getMessage());
        }
    }
    /**
     * 下载媒体文件
     * @param fileName
     * @return
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("fileName") String fileName) {
        return FileUploadUtil.downloadFile(fileName, reggieConfig.getPath());
    }

    /**
     * 删除媒体文件
     * @param fileName
     * @return
     */
    @PostMapping("/delete")
    public Result deleteFile(@RequestParam("fileName") String fileName) {
        try {
            FileUploadUtil.deleteFile(fileName, reggieConfig.getPath());
            return Result.success("文件删除成功");
        } catch (RuntimeException e) {
            return Result.error("文件删除失败:"+e.getMessage());
        }
    }
}
