package com.community.service;

import com.community.pojo.UsersComments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.core.utils.Result;

/**
 * <p>
 * 交流社区评论表 服务类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface UsersCommentsService extends IService<UsersComments> {

    Result addComment(UsersComments comment);
    Result removeById(Integer id);
}
