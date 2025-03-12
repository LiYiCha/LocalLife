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
 * 交流社区评论表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("users_comments")
public class UsersComments implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    /**
     * 关联到 forum_posts 表
     */
    @TableField("post_id")
    private Integer postId;

    /**
     * 关联到 users 表中的用户
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 评论内容
     */
    @TableField("content")
    private String content;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
}
