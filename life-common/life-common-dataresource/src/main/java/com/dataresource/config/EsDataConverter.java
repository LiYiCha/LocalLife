package com.dataresource.config;

import com.dataresource.pojo.ProductEs;
import com.dataresource.pojo.ProductSkuEs;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据转换器，用于将MySQL数据转换为Elasticsearch数据结构
 */
public class EsDataConverter {
    private List<ProductEs> convertToProductEs(List<Map<String, Object>> mysqlData) {
        return mysqlData.stream().map(data -> {
            ProductEs productEs = new ProductEs();

            // 基本字段
            productEs.setProductId((Integer) data.get("productId"));
            productEs.setMerchantId((Integer) data.get("merchantId"));
            productEs.setCategoryId((Integer) data.get("categoryId"));
            productEs.setProductName((String) data.get("productName"));
            productEs.setDescription((String) data.get("description"));
            productEs.setStatus((String) data.get("status"));
            productEs.setSalesCount((Integer) data.get("salesCount"));
            productEs.setIsRecommend((Integer) data.get("isRecommend"));
            productEs.setSortOrder((Integer) data.get("sortOrder"));
            productEs.setMainImage((String) data.get("mainImage"));

            // 嵌套字段：SKU
            productEs.setSkus(Collections.singletonList(new ProductSkuEs(
                    (Integer) data.get("skuId"),                   // SKU ID
                    (String) data.get("skuName"),                  // SKU 名称
                    new BigDecimal(data.get("price").toString()),  // 价格
                    (Integer) data.get("stockQuantity"),           // 库存数量
                    (String) data.get("skuImageUrl")               // SKU 图片URL
            )));

            return productEs;
        }).collect(Collectors.toList());
    }
}
