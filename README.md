<p align="center">
  <a href="https://opentiny.design/tiny-engine" target="_blank" rel="noopener noreferrer">
    <img alt="OpenTiny Logo" src="logo.svg" height="100" style="max-width:100%;">
  </a>
</p>
<p align="center">Tiny Engine Web Service is a RESTful API responsible for providing data services, code generation services, and code release services to the front end.  It does not directly operate on the database, and data operations request interfaces from TinyEngine Data Center.</p>


English | [简体中文](README.zh-CN.md)

Local Boot Steps:
* Click the Fork button in the upper right corner of the tiny-engine-backend-java repository to fork the upstream repository to your personal repository
* Clone personal warehouse to local
*  The installation depends on JDK 1.8 and Maven 3.5 or later
* Modify the configuration of the connection database in the tiny-engine-backend-java/app/src/main/resources/application-dev.yml file
* Start the project in tiny-engine-backend-java/app/src/main/java/com/tinyengine/it/TinyEngineApplication for local development

For details, see the section on how to start a local front-end and backend in [TinyEngine Official Website - User Manual](https://opentiny.design/tiny-engine#/help-center/course/backend/51).

### Directory Rules

Before development, you need to understand the overall directory structure of the project and compile code according to the following rules

```
├── README.md
├── app                                                            // Basic services
│   └── src                      
│       └── main 
│           └── java 
│               ├── com.tinyengine.it
│               │            ├── config                           // Profiles
│               │            └── TinyEngineApplication            // Startup class, main entrance
│               └── resource 
│                      ├── sql                                    // SQL files, including table creation files and SQL files of basic data
│                      │   ├── h2                                 
│                      │   ├──mysql                               
│                      │   └──postgresql                          
│                      └── application.yml
│                      
│                                                                // Configuration information, some of the configurations that are mainly used to manage the Springboot application globally, some configurations related to MybatisPlus, etc
└── base                                                         // Business function services
    └── src 
        ├── main                    
        │   └── java          
        │       └── com
        │           └── tinyengine
        │               └── it
        │                   ├── common                            // Public Documents   
        │                   │   ├── base                          // Public entity classes
        │                   │   │   └── BaseEntity                
        │                   │   ├── enums                         // Public enumeration classes
        │                   │   │   └── Enums                     
        │                   │   ├── exception                     // Public anomaly classes
        │                   │   │   └── ExceptionEnum             
        │                   │   ├── handler                       // Data type processor
        │                   │   │   └── ListTypeHandler           
        │                   │   ├── log                           // System logs
        │                   │   │   └── SystemControllerLog       
        │                   │   └── utils                         // Utilities
        │                   │       └── Utils                     
        │                   ├── config                            // Configure the class
        │                   │   └── AiChatConfig                      
        │                   ├── controller                        // Business control layer
        │                   │   └── AppController                 
        │                   ├── gateway                           // gateway
        │                   │   └── ai
        │                   │       └── AiChatClient                  
        │                   ├── mapper                            // Data access layer
        │                   │   └── AppMapper                     
        │                   ├── model                             // Model entity class
        │                   │   ├── dto
        │                   │   │   └── BlockDto                  
        │                   │   └── entity
        │                   │       └── Block                     
        │                   └── service                           // Business logic layer
        │                       ├── app                           // app module
        │                       │   ├── impl                      // APP module business implementation class
        │                       │   │   └── AppServiceImpl        
        │                       │   └── AppService                // Business logic interface of the app module
        │                       ├── material                      // Material module
        │                       │   ├── impl                      // The business implementation class of the material module
        │                       │   │   └── BlockServiceImpl      
        │                       │   └── BlockService              // Taking blocks as an example, this layer is the business logic interface related to the material
        │                       └── platform                      // Designer module
        │                           ├── impl                      // The business implementation class of the designer module
        │                           │   └── PlatformServiceImpl   
        │                           └── PlatformService           // The business logic interface of the designer module
        └── test                                                  // The test directory is divided into test cases of different modules according to the above directory                 
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


### Interface Return Specifications

##### 1.Return Format

- Correct data
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
- Error Data
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

### Manual

For more information about how to use the server, please refer to [TinyEngine Official Website - User Manual](https://opentiny.design/tiny-engine#/help-center/course/backend/51)


#### Local Runtime Configuration:

JDK1.8，
Maven 3.5 or later is sufficient，
mysql 8

### Database table mapping before and after data migration

- Blocks -------> historical association changes in blocks:
  After being refactored by the blocks__histories of nodejs, ref_id the block table associated with this property is directly added to the t_block_history table of the Java database, and there is no need to blocks__histories the associated table

- Material -------> block association changes:
  The block-history_id field in the material block history relationship table of the block_histories_materials__materials_user_blocks of nodejs is used to find the primary key of the block in the block_id block_histories table, and then the block information is found in the block table through the block_id, in order to query more conveniently. In the Java database table, the relationship table between blocks and materials is directly created r_material_block

***Summarizing the changes in the database table can be roughly divided into the following types***

- The definition of the field in the database table is more standardized, and the previous hump is changed to an underscore name, such as isDefault -> is_default
- The meaning of the definition of the database table field is clearer, such as app -> app_id, created_at -> created_time
- Most of the database tables have new tenant_id, site_id, renter_id, and platform_id fields, as well as designer tables t_platform and designer history tables t_platform_history due to tenant isolation
- The database table relationship is clearer and clearer, and some useless table relationships are deleted, such as the table relationship between the block and the block history, which is directly t_block_history added to the block history table, that is, the ref_id as a foreign key
- The block_categories block classification was deleted, and the block grouping and classification were merged into a block grouping, taking into account the similar functions and functions of grouping and classification
- The t_business_category of the business classification table and the relationship table between the material and the business classification are reserved, and r_material_category


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


### 🤝 Participating in contributions  Q

If you are interested in our open source project, please join us!

Please read [the Contribution Guide](CONTRIBUTING.md) before participating in the contribution.

- Add official assistant WeChat opentiny-official and join the technical exchange group
- Join the mailing list opentiny@googlegroups.com

### Open source protocol

[MIT](LICENSE)
