package com.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.search.pojo.ProductEs;
import com.search.service.SearchService;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件名: SearchService
 * 创建者: @一茶
 * 创建时间:2025/4/29 18:06
 * 描述：商品搜索服务实现类
 * ElasticsearchOperations: 搜索操作类
 *      search(): 搜索
 *      searchForPage(): 分页搜索
 *      searchForStream(): 流式搜索
 *      searchForSuggest(): 建议搜索
 *      searchForHighlight(): 高亮搜索
 *      searchForScroll(): 滚动搜索
 *      searchForCompletion(): 提示搜索
 */
@Service
public class SearchServiceImpl implements SearchService {

    private final ElasticsearchOperations eso;

    public SearchServiceImpl(ElasticsearchOperations elasticsearchOperations) {
        this.eso = elasticsearchOperations;
    }

    /**
     * 搜索商品（全局）
     * @param keyword 关键字（支持模糊匹配商品名称和描述）
     * @param categoryId 分类ID（可选，用于过滤分类）
     * @param price 价格范围（可选，用于过滤价格）
     * @param page 页码（从0开始）
     * @param size 每页数量
     * @return 商品列表
     */
    @Override
    public List<ProductEs> searchProducts(String keyword, Integer categoryId, BigDecimal price, int page, int size) {
        // 构建布尔查询
        BoolQuery.Builder queryBuilder = QueryBuilders.bool();

        // 关键字匹配（支持商品名称和描述的模糊匹配）
        if (keyword != null && !keyword.isEmpty()) {
            queryBuilder.must(QueryBuilders.multiMatch()
                    .fields("productName", "description")
                    .query(keyword)
                    .build()._toQuery());
        }

        // 分类过滤
        if (categoryId != null) {
            queryBuilder.filter(QueryBuilders.term().field("categoryId").value(categoryId).build()._toQuery());
        }

        // 价格过滤（针对 SKU 的价格范围）
        if (price != null) {
            queryBuilder.filter(QueryBuilders.nested()
                    .path("skus") // 嵌套字段 skus 需要用nested来查询
                    .query(QueryBuilders.range()
                            .field("skus.price")
                            .lte(JsonData.fromJson(price.toString()))
                            .build()._toQuery())
                    .build()._toQuery());
        }

        // 构建分页查询
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(queryBuilder.build()._toQuery())
                .withPageable(PageRequest.of(page, size))
                .build();

        // 执行查询并返回结果
        SearchHits<ProductEs> searchHits = eso.search(searchQuery, ProductEs.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * 搜索商品（商户）
     * @param keyword 关键字（支持模糊匹配商品名称和描述）
     * @param merchantId 商户ID（用于过滤商户）
     * @param categoryId 分类ID（可选，用于过滤分类）
     * @param page 页码（从0开始）
     * @param size 每页数量
     * @return 商品列表
     */
    @Override
    public List<ProductEs> searchProductsInMerchant(String keyword, Integer merchantId, Integer categoryId, int page, int size) {
        // 构建布尔查询
        BoolQuery.Builder queryBuilder = QueryBuilders.bool();

        // 商户过滤（必须指定商户ID）
        if (merchantId != null) {
            queryBuilder.filter(QueryBuilders.term().field("merchantId").value(merchantId).build()._toQuery());
        } else {
            throw new IllegalArgumentException("商户ID不能为空");
        }

        // 关键字匹配（支持商品名称和描述的模糊匹配）
        if (keyword != null && !keyword.isEmpty()) {
            queryBuilder.must(QueryBuilders.multiMatch()
                    .fields("productName", "description")
                    .query(keyword)
                    .build()._toQuery());
        }

        // 分类过滤
        if (categoryId != null) {
            queryBuilder.filter(QueryBuilders.term().field("categoryId").value(categoryId).build()._toQuery());
        }

        // 构建分页查询
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(queryBuilder.build()._toQuery())
                .withPageable(PageRequest.of(page, size))
                .build();

        // 执行查询并返回结果
        SearchHits<ProductEs> searchHits = eso.search(searchQuery, ProductEs.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
