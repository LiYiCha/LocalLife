package com.community.pojo;

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
 * 交流社区帖子表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("users_posts")
public class UsersPosts implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 帖子ID
     */
    @TableId(value = "post_id", type = IdType.AUTO)
    private Integer postId;

    /**
     * 关联到 users 表中的用户
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 帖子标题
     */
    @TableField("title")
    private String title;

    /**
     * 帖子内容
     */
    @TableField("content")
    private String content;

    /**
     * 帖子类别
     */
    @TableField("category")
    private String category;

    /**
     * 点赞数
     */
    @TableField("liked")
    private Integer liked;

    /**
     * 评论数
     */
    @TableField("comments")
    private Integer comments;

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
