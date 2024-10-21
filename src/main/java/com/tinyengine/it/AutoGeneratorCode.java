package com.tinyengine.it;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.SQLException;
import java.util.Collections;

public class AutoGeneratorCode {
    /**
     * 执行 run
     */
    public static void main(String[] args) throws SQLException {
        FastAutoGenerator.create("jdbc:mariadb://localhost:3306/tiny_engine_data_java?useUnicode=true&useSSL=false&characterEncoding=utf8", "root", "111111")
                // 全局配置
                .globalConfig((scanner, builder) -> {
                    builder.author(scanner.apply("请输入作者名称"))
                            .dateType(DateType.TIME_PACK) // 时间策略
                            .commentDate("yyyy-MM-dd") // 注释日期
                            .enableSwagger() // 开启swagger模式
                            .disableOpenDir() // 禁止打开输出目录
                            .outputDir(System.getProperty("user.dir") + "/src/main/java"); // 指定输出目录

                })
                // 包配置
                .packageConfig((scanner, builder) -> {
                    builder.parent("com.tinyengine.it")
                            .moduleName("")
                            // .controller(scanner.apply("请输入controller包名称")) // 注释掉这一行
                            .service(scanner.apply("请输入service包名称"))
                            .serviceImpl(scanner.apply("请输入impl包名称"))
                            .mapper("mapper") // scanner.apply("请输入mapper包名称"))
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mappers"))
                            .entity("model.entity"); // scanner.apply("请输入entity包名称"))
                    // .other("model.dto"); // scanner.apply("请输入dto包名称")); // 设置dto包名
                })
                .templateConfig(builder -> {
                    builder.entity("templates/entity.java")
                            .service("templates/service.java")
                            .serviceImpl("templates/serviceImpl.java")
                            .mapper("templates/mapper.java")
                            .xml("templates/mapper.xml")
                            .controller(""); // 注释掉这一行
                })
                // 策略配置
                .strategyConfig((scanner, builder) -> {
                    builder.addInclude(scanner.apply("请输入表名，多个表名用,隔开"))
                            .addTablePrefix("t_", "r_") // 设置过滤表前缀
                            .serviceBuilder()
                            .formatServiceFileName("%sService") // scanner.apply("请输入service名称"))
                            .formatServiceImplFileName("%sServiceImpl") // scanner.apply("请输入service实现类impl名称"))
                            .entityBuilder()
                            .enableLombok() // 开启Lombok
                            // .controllerBuilder() // 注释掉这一行
                            // .formatFileName("%sController") // scanner.apply("请输入controller名称"))
                            // .enableRestStyle() // 注释掉这一行
                            .mapperBuilder()
                            .formatMapperFileName("%sMapper") // scanner.apply("请输入mapper名称"))
                            .formatXmlFileName("%sMapper") // scanner.apply("请输入mapper xml名称"))
                            .enableMapperAnnotation() // 开启@Mapper
                            .superClass(BaseMapper.class); // 继承的父类

                })
                .templateEngine(new FreemarkerTemplateEngine()) // 模板引擎配置
                .execute();
    }
}
