package com.search.mapper;

import com.search.pojo.ProductEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 文件名: SearchMapper
 * 创建者: @一茶
 * 创建时间:2025/4/30 14:58
 * 描述：需要继承ElasticsearchRepository接口，参数<映射对象，主键ID的数据类型>
 * 注意：
 * 1、映射对象必须是一个POJO类，不能是一个接口
 * 2、主键ID的数据类型必须是基本数据类型或者String类型
 * 3、映射对象必须有一个主键ID字段，并且使用@Id注解标注
 * 5、映射对象必须有一个getter和setter方法
 */
public interface SearchMapper extends ElasticsearchRepository<ProductEs, Integer> {
}
