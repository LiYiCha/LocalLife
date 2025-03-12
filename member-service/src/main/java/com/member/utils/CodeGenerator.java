package com.member.utils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;


/** * 代码生成器 */
public class CodeGenerator {
    public static void main(String[] args) {
        // 连接数据库
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/life_product?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", "root", "114706")
                .globalConfig(builder -> {
                    builder.author("一茶") // 设置作者
                            //.fileOverride() // 覆盖已生成文件
                            // 设置日期时间
                            .dateType(DateType.ONLY_DATE)
                            .outputDir("D:\\exploitation\\work1\\LocalLife\\produc-service\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.product") // 设置父包名
                            .entity("pojo") // 设置 Entity 包名
                            .service("service") // 设置 Service 包名
                            .serviceImpl("service.impl") // 设置 Service Impl 包名
                            .mapper("mapper") // 设置 Mapper 包名
                            .xml("mapper") // 设置 Mapper XML 包名
                            .controller("controller") // 设置 Controller 包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D:\\exploitation\\work1\\LocalLife\\produc-service\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("products","product_skus","product_images","product_categories","product_reviews","shopping_cart") // 设置需要生成的表名
                            .addTablePrefix("t_"); // 设置过滤表前
                    // 新增数据，自动为创建时间赋值
                    IFill createFill = new Column("created_time", FieldFill.INSERT);
                    IFill updateFill = new Column("updated_time", FieldFill.UPDATE);
                    builder.entityBuilder()
                            // 设置id类型
                            .idType(IdType.AUTO)
                            // 开启 Lombok
                            .enableLombok()
                            // 开启连续设置模式
                            .enableChainModel()
                            // 驼峰命名模式
                            .naming(NamingStrategy.underline_to_camel)
                            .columnNaming(NamingStrategy.underline_to_camel)
                            // 自动为创建时间、修改时间赋值
                            .addTableFills(createFill).addTableFills(updateFill)
                            .enableTableFieldAnnotation(); // 启用字段注解
                            // 逻辑删除字段
                            //.logicDeleteColumnName("is_deleted");

                    // Restful 风格
                    builder.controllerBuilder().enableRestStyle();
                    // 去除 Service 前缀的 I
                    builder.serviceBuilder().formatServiceFileName("%sService");
                    // mapper 设置
                    builder.mapperBuilder()
                            .enableBaseResultMap()
                            .enableBaseColumnList();
                })
                // 固定
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
