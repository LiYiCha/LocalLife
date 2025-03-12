package com.product.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品分类表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("product_categories")
public class ProductCategories implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;

    /**
     * 商家ID
     */
    @TableField("merchant_id")
    private Integer merchantId;

    /**
     * 分类名称
     */
    @TableField("name")
    private String name;

    /**
     * 分类描述
     */
    @TableField("description")
    private String description;

    /**
     * 分类封面图
     */
    @TableField("cover_image")
    private String coverImage;

    /**
     * 分类层级：1-一级分类，2-二级分类
     */
    @TableField("level")
    private Byte level;

    /**
     * 父分类ID（一级分类为0）
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 是否展示：0-隐藏，1-显示
     */
    @TableField("is_show")
    private Byte isShow;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 状态：0-禁用，1-启用
     */
    @TableField("status")
    private Byte status;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.UPDATE)
    private LocalDateTime updatedTime;
}
