package com.search.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件名: ProductEs
 * 创建者: @一茶
 * 创建时间:2025/4/29 17:30
 * 描述：Es返回数据实体类
 * @Document() :  索引
 *      indexName : 索引名
 *      createIndex : 是否创建索引
 * @Id : 主键ID
 * @Field() : 字段
 *      type : 字段类型
 *      analyzer : 分词器
 *      format : 日期格式
 *      includeInParent : 是否包含在父文档中
 *      includeInRoot : 是否包含在根文档中
 *      nested : 是否嵌套
 *      index : 是否索引
 *      store : 是否存储
 *      searchAnalyzer : 搜索分词器
 *      searchAnalyzer : 搜索分词器
 *      copyTo : 拷贝到其他字段中
 *      ignoreAbove : 忽略超过指定长度的字段
 *      ignoreMalformed : 忽略格式错误的字段
 *      nullValue : 空值
 *      pattern : 日期格式
 *      precisionStep : 精度步长
 *      suffixes : 后缀
 *      tokenizer : 分词器
 *      type : 字段类型
 *      value : 默认值
 */
@Document(indexName = "lf_products",createIndex = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEs {
    @Id
    private Integer productId;
    @Field(type = FieldType.Integer)
    private Integer merchantId;
    @Field(type = FieldType.Integer)
    private Integer categoryId;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String productName;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String description;
    @Field(type = FieldType.Text)
    private String status;
    @Field(type = FieldType.Integer)
    private Integer salesCount;
    @Field(type = FieldType.Integer)
    private Integer isRecommend;
    @Field(type = FieldType.Integer)
    private Integer sortOrder;
    @Field(type = FieldType.Text)
    private String mainImage;
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private LocalDateTime createdTime;
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private LocalDateTime updatedTime;
    @Field(type = FieldType.Nested, includeInParent = true)
    private List<ProductImageEs> images;
    @Field(type = FieldType.Nested, includeInParent = true)
    private List<ProductSkuEs> skus;
}
