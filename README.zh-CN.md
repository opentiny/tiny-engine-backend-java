<p align="center">
  <a href="https://opentiny.design/tiny-engine" target="_blank" rel="noopener noreferrer">
    <img alt="OpenTiny Logo" src="logo.svg" height="100" style="max-width:100%;">
  </a>
</p>

<p align="center">Tiny Engine Backend Java是一个基于springboot框架的RESTful API，负责处理业务逻辑并向前端提供数据服务。

[English](README.md) | 简体中文


本地启动步骤：

* 点击 tiny-engine-backend-java 代码仓库右上角的 Fork 按钮，将上游仓库 Fork 到个人仓库
* Clone 个人仓库到本地
*  安装依赖JDK1.8,Maven 3.5以上即可
* 在 tiny-engine-backend-java/app/src/main/resources/application-dev.yml文件里修改连接数据库相关配置
* 在 tiny-engine-backend-java/app/src/main/java/com/tinyengine/it/TinyEngineApplication主函数入口启动项目进行本地开发

详细请看[TinyEngine 官网-使用手册-平台开发指南-前后端代码本地启动联调](https://opentiny.design/tiny-engine#/help-center/course/dev/1200)章节


### 目录规则

开发前需要了解项目整体目录结构，并按照如下规则去进行目录规则编写代码

```
├── README.md
├── app                                                            // 基础服务
│   └── src                      
│       └── main 
│           └── java 
│               ├── com.tinyengine.it
│               │            ├── config                           // 配置文件
│               │            └── TinyEngineApplication            // 启动类，主入口
│               └── resource 
│                      ├── sql                                    // sql文件，包括创表文件和基础数据sql文件
│                      │   ├── h2                                 
│                      │   ├──mysql                               
│                      │   └──postgresql                          
│                      └── application.yml
│                      
│                                                                // 配置信息，一些主要用于全局管理springboot应用程序的各种配置，和mybatisplus相关的一些配置等等
└── base                                                         // 业务功能服务
    └── src 
        ├── main                    
        │   └── java          
        │       └── com
        │           └── tinyengine
        │               └── it
        │                   ├── common                            // 公共文件   
        │                   │   ├── base                          // 公共实体类
        │                   │   │   └── BaseEntity                
        │                   │   ├── enums                         // 公共枚举类
        │                   │   │   └── Enums                     
        │                   │   ├── exception                     // 公共异常类
        │                   │   │   └── ExceptionEnum             
        │                   │   ├── handler                       // 数据类型处理器
        │                   │   │   └── ListTypeHandler           
        │                   │   ├── log                           // 系统日志
        │                   │   │   └── SystemControllerLog       
        │                   │   └── utils                         // 工具类
        │                   │       └── Utils                     
        │                   ├── config                            // 配置类
        │                   │   └── AiChatConfig                      
        │                   ├── controller                        // 业务控制层
        │                   │   └── AppController                 
        │                   ├── gateway                           // 网关
        │                   │   └── ai
        │                   │       └── AiChatClient                  
        │                   ├── mapper                            // 数据访问层
        │                   │   └── AppMapper                     
        │                   ├── model                             // 模型实体类
        │                   │   ├── dto
        │                   │   │   └── BlockDto                  
        │                   │   └── entity
        │                   │       └── Block                     
        │                   └── service                           // 业务逻辑层
        │                       ├── app                           // app模块
        │                       │   ├── impl                      // app模块业务实现类
        │                       │   │   └── AppServiceImpl        
        │                       │   └── AppService                // app模块业务逻辑接口
        │                       ├── material                      // 物料模块
        │                       │   ├── impl                      // 物料模块业务实现类
        │                       │   │   └── BlockServiceImpl      
        │                       │   └── BlockService              // 以区块举例，这层为物料相关的业务逻辑接口
        │                       └── platform                      // 设计器模块
        │                           ├── impl                      // 设计器模块业务实现类
        │                           │   └── PlatformServiceImpl   
        │                           └── PlatformService           // 设计器模块业务逻辑接口
        └── test                                                  // test目录下放不同模块的测试用例，根据上面目录进行划分                 
            ├── java
            │    └── com 
            │        └── tinyengine
            │            └── it
            │                ├── common
            │                │   ├── base
            │                │   │   └── ResultTest
            │                │   ├── exception
            │                │   │   └── GlobalExceptionAdviceTest
            │                │   ├── handler
            │                │   │   └── ListTypeHandlerTest
            │                │   ├── log
            │                │   │   └── SystemLogAspectTest
            │                │   └── utils
            │                │       └── UtilsTest
            │                ├── controller
            │                │   └── AppControllerTest
            │                ├── gateway
            │                │   └── ai
            │                │       └── AiChatClientTest
            │                ├── mapper
            │                │   └── AppMapperTest
            │                └── service     
            │                    ├── app
            │                    │   └── impl
            │                    │       └── AppServiceImplTest
            │                    ├── material
            │                    │   └── impl
            │                    │       └── BlockServiceImplTest
            │                    └── platform
            │                        └── impl        
            │                            └── PlatformServiceImplTest              
            └── resources                     
```


### 接口返回规范

##### 1.返回格式

- 正常数据
```java
{
    "data": {
        "id": 1,
        "createdBy": "1"
    }
    "code": "200",
    "message": "操作成功",
    "error": null,
    "errMsg": null,
    "success": true
}
```
- 错误数据
```java
{
    "data": null,
    "code": "CM003",
    "message": "重复创建，请修改传入参数。",
    "error": {
        "code": "CM003",
        "message": "重复创建，请修改传入参数。"
    },
    "errMsg": "重复创建，请修改传入参数。",
    "success": false
}
```

### 使用手册

具体服务端使用文档请查看[TinyEngine 官网-使用手册-平台开发指南](https://opentiny.design/tiny-engine#/help-center/course/dev/90)


#### 本地运行时配置：

JDK1.8，
Maven 3.5以上即可，
mysql 8

### 数据迁移前后数据库表映射

- 区块 ------->区块历史关联变化:
由nodejs的blocks__histories重构后直接在java数据库的t_block_history表里加了ref_id这个属性关联的区块表，就不需要blocks__histories关联表了

- 物料 ------->区块关联变化:
由nodejs的block_histories_materials__materials_user_blocks的物料区块历史关系表里的block-history_id字段去block_histories表里找block_id区块主键，再通过block_id去blocks表里找区块信息，为了查询更方便，在java数据库表里直接建了区块和物料的关系表r_material_block

***总结数据库表变化大概分为以下几种***

- 数据库表字段定义的更规范，由以前的驼峰变成下划线命名，比如isDefault -> is_default
- 数据库表字段定义的意义更清晰明了，比如app -> app_id，created_at -> created_time
- 数据库表里大部分新增了tenant_id、site_id、renter_id、platform_id字段以及设计器表t_platform和设计器历史表t_platform_history，由于要进行租户隔离
- 数据库表关系更清晰明了，删除了一些没用的表关系，比如区块和区块历史的表关系，是直接在区块历史表里t_block_history加区块的id即ref_id做为外键
- 删除了block_categories区块分类，把区块分组和分类合并成了区块分组，考虑到分组和分类差不多的功能和作用
- 预留了业务分类表t_business_category、物料与业务分类的关系表r_material_category


| nodejs数据库表 |           java数据库表           | 新增属性                                                                                    | 删除属性                                                                        |                                                                                                                          修改属性                                                                                                                           |
| :----- |:----------------------------:|:----------------------------------------------------------------------------------------|:----------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| app_extensions |       t_app_extension        | tenant_id、site_id、renter_id                                                             |                                                                             |                                                                        app -> app_id、updated_by -> last_updated_by、created_at -> created_time、 updated_at -> last_updated_time、                                                                         |                                    
| apps |            t_app             | site_id、renter_id                                                                       | tpl-groups、created_by、updated_by                                            | platform -> platform_id、platform_history -> platform_history_id、 obs_url -> publish_url、home_page -> home_page_id、tenant -> tenant_id、createdBy -> created_by、updatedBy -> last_updated_by、 created_at -> created_time、 updated_at -> last_updated_time |
| block_groups |        t_block_group         | platform_id、tenant_id、site_id、renter_id                                                 |                                                                             |                                                               app -> app_id、 dec -> description、updated_by -> last_updated_by、created_at -> created_time、 updated_at -> last_updated_time                                                               |
| block_histories |       t_block_history        | framework、 tags、is_official、 public、is_default、tiny_reserved、platform_id、block_group_id |                                                                             |                                                           created_app -> app_id、block_id -> ref_id、updated_by -> last_updated_by、created_at -> created_time、 updated_at -> last_updated_time、                                                           |
| blocks |           t_block            | latest_version、 i18n                                                                    | created_by、updated_by、author、                                               |    name_cn -> name、current_history -> latest_history_id、occupier -> occupier_by、isOfficial -> is_official、isDefault -> is_default、createdBy -> created_by、updatedBy -> last_updated_by、 created_at -> created_time、 updated_at -> last_updated_time     |
| blocks_groups__block_groups_blocks |  r_block_group_block    | |                                                                             |block-group_id -> block_group_id、 |
|blocks_carriers_relations|t_block_carriers_relation|tenant_id、site_id、renter_id|                                                                             |block -> block_id、 host ->host_id、created_at -> created_time、updated_by -> last_updated_by、updated_at -> last_updated_time |
| block_histories_material_histories__material_histories_blocks |   r_material_history_block   |    |                                                                             |                   material-history_id ->  material_history_id、  block-history_id -> block_history_id                                                                                            |
| material_histories |      t_material_history      | image_url、build_info、tgz_url、 material_size、site_id、renter_id                           |                                                                             |                                                           material -> ref_id、 tenant -> tenant_id 、updated_by -> last_updated_by、created_at -> created_time、 updated_at -> last_updated_time                                                            |
| material_histories_components__user_components_mhs | r_material_history_component |                                                                                         |                                                                             |                                                                                     material-history_id -> material_history_id、 user-component_id ->  component_id                                                                                      |
| materials |          t_material          | material_category_id、material_size、tgz_url、unzip_tgz_root_path_url、unzip_tgz_files、tenant_id、site_id、renter_id                                                                                        | name_cn、user_components、latest                                              |                                              version -> latest_version、material_histories -> latest_history_id、isOfficial -> is_official、isDefault ->is_default、component_library -> component_library_id、                                              |
| materials_user_components__user_components_materials |     r_material_component     | |                                                                             |                                                                                                           user-component_id ->  component_id                                                                                                            |
| pages |            t_page            | latest_version、latest_history_id、tenant_id、site_id、renter_id| created_by、updated_by                                                       |                                                 app -> app_id、occupier -> occupier_by、createdBy -> created_by、updatedBy -> last_updated_by、created_at -> created_time、 updated_at -> last_updated_time                                                  |
| pages_histories |        t_page_history        | ref_id、version、app_id、depth、is_page、is_default、is_published、tenant_id、site_id、renter_id| time                                                                        |                                                                                updated_by -> last_updated_by、created_at -> created_time、 updated_at -> last_updated_time                                                                                |
| templates |       t_page_template        |name、status、is_preset、image_url、tenant_id、site_id、renter_id、platform_id、 | name_en、name_cn、thumbnail、tags、created_app、create_app、created_by、updated_by |content ->page_content、tpl_type ->type、createdBy -> created_by、updatedBy -> last_updated_by、created_at -> created_time、 updated_at -> last_updated_time |
| i18n_entries |         t_i18n_entry         | tenant_id、site_id、renter_id                                                             |                                                                             |                                                               host -> host_id、lang -> lang_id、 updated_by -> last_updated_by、created_at -> created_time、 updated_at -> last_updated_time                                                                |
| i18n_langs |         t_i18n_lang          |                                                                                         |                                                                             |                                                                                updated_by -> last_updated_by、created_at -> created_time、 updated_at -> last_updated_time                                                                                | 
| sources |         t_datasource         | platform_id、tenant_id、site_id、renter_id|                                                                             |                                                               app -> app_id、desc -> description、updated_by -> last_updated_by、created_at -> created_time、 updated_at -> last_updated_time                                                               |
| task_record |        t_task_record         | build_id、tenant_id、site_id、renter_id| uniqueId、created_by、updated_by                                              |             teamId ->team_id、taskTypeId ->task_type、taskName ->task_name、taskStatus ->task_status、taskResult ->task_result、createdBy -> created_by、updatedBy -> last_updated_by、created_at -> created_time、 updated_at -> last_updated_time             |
| user_components |         t_component          |name_en、tenant_id、site_id、renter_id | component                                                                   |                                            isOfficial ->is_official、isDefault -> is_default、library -> library_id、updated_by -> last_updated_by、created_at -> created_time、 updated_at -> last_updated_time                                             |
| users-permissions_user |            t_user            |enable、tenant_id、site_id、renter_id | provider、password、resetPasswordToken、confirmationToken、confirmed、blocked    |                                                                                updated_by -> last_updated_by、created_at -> created_time、 updated_at -> last_updated_time                                                                                |
| tenants |           t_tenant           | | created_by、updated_by                                                       |                                                         tenant_id -> org_code、createdBy -> created_by、updatedBy -> last_updated_by、created_at -> created_time、 updated_at -> last_updated_time                                                          |
| platforms                                                     | t_platform| latest_history_id、site_id、renter_id                                                                                                               |   is_java、created_by、updated_by                                                                          | theme -> theme_id、latest -> latest_version、material_history -> material_history_id、business_category -> business_category_id、tenant -> tenant_id、createdBy -> created_by、updatedBy -> last_updated_by、created_at -> created_time、updated_at -> last_updated_time    |
|platform_histories|t_platform_history|publish_url、image_url、tenant_id、site_id、renter_id | |platform -> ref_id、material_history -> material_history_id、created_at -> created_time、updated_by -> last_updated_by、updated_at -> last_updated_time |


### 🤝 参与贡献

如果你对我们的开源项目感兴趣，欢迎加入我们！🎉

参与贡献之前请先阅读[贡献指南](CONTRIBUTING.zh-CN.md)。

- 添加官方小助手微信 opentiny-official，加入技术交流群
- 加入邮件列表 opentiny@googlegroups.com

### 开源协议

[MIT](LICENSE)
