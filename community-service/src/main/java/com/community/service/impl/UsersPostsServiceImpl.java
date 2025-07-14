package com.community.service.impl;

import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.pojo.UsersPosts;
import com.community.mapper.UsersPostsMapper;
import com.community.service.UsersPostsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.utils.Result;
import jakarta.annotation.Resource;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * <p>
 * 交流社区帖子表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Service
public class UsersPostsServiceImpl extends ServiceImpl<UsersPostsMapper, UsersPosts> implements UsersPostsService {


    @Resource
    private UsersPostsMapper usersPostsMapper;
    @Resource
    private ElasticsearchOperations eso;

    /**
     * 根据标题搜索帖子（分页）
     * @param title
     * @param page
     * @param size
     * @return
     */
    @Override
    @Cacheable(value = "postsCache", key = "#title + '_' + #page + '_' + #size")
    public Result searchPosts(String title, int page, int size) {
        try {
            Page<UsersPosts> pages = new Page<>(page, size);
            IPage<UsersPosts> resultPage = usersPostsMapper.search(pages, title);
            return Result.success(resultPage);
        } catch (Exception e) {
            log.error("搜索帖子失败", e);
            return Result.error("搜索帖子失败");
        }

    }


    /**
     * 点赞帖子
     * @param postId
     * @return
     */
    @Override
    public Result likePost(Integer postId){
        int like = usersPostsMapper.likePost(postId);
        return like > 0 ? Result.success() : Result.error("点赞失败");
    }


}
