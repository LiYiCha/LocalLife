package com.search.service.impl;

import com.core.utils.Result;
import com.feign.client.ProductClient;
import com.feign.pojo.ProductEs;
import jakarta.annotation.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EsSyncService {

    @Resource
    private ProductClient productsClient;

    @Resource
    private ElasticsearchOperations eso;

    /**
     * 全量同步数据到ES
     * @param pageSize 每页大小
     */
    public Result fullSync(int pageSize) {
        try {
            int pageNum = 1;
            boolean hasMore = true;

            while (hasMore) {
                //1.  调用Feign接口获取商品数据
                List<ProductEs> productEsList = productsClient.syncAllProductsToES(pageNum, pageSize);

                if (productEsList.isEmpty()) {
                    hasMore = false;
                } else {
                    //2.  批量插入到ES中
                    List<IndexQuery> queries = productEsList.stream()
                            .map(p -> new IndexQueryBuilder().withId(p.getProductId().toString()).withObject(p).build())
                            .collect(Collectors.toList());
                    eso.bulkIndex(queries, IndexCoordinates.of("lf_products"));
                    pageNum++;
                }
            }
            if (!hasMore){
                return Result.error("Elasticsearch 全量同步失败");
            }else {
                return Result.error("Elasticsearch 全量同步失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Elasticsearch 全量同步失败", e);
        }
    }

    /**
     * 增量同步数据到ES
     * @param productId 商品ID
     */
    public void syncProductToEs(Integer productId) {
        ProductEs productEs = productsClient.getProductForEs(productId);
        if (productEs != null) {
            IndexQuery query = new IndexQueryBuilder()
                    .withId(productEs.getProductId().toString())
                    .withObject(productEs)
                    .build();
            eso.index(query, IndexCoordinates.of("lf_products"));
        }
    }

    /**
     * 从ES中删除指定ID的商品
     * @param productId 商品ID
     */
    public void deleteFromEs(Integer productId) {
        eso.delete(String.valueOf(productId), IndexCoordinates.of("lf_products"));
    }

}
