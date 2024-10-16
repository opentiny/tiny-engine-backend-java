package ${package.Service};

import ${package.Entity}.${entity};
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ${table.serviceName}{

    /**
    *  查询表${table.name}所有信息
    */
    List<${entity}> findAll${entity}();

<#list table.fields as field>
 <#if field.keyFlag>
    /**
    *  根据主键${field.propertyName}查询表${table.name}信息
    *  @param ${field.propertyName}
    */
    ${entity} find${entity}ById(@Param("${field.propertyName}") ${field.propertyType} ${field.propertyName});
 </#if>
</#list>

    /**
    *  根据条件查询表${table.name}信息
    *  @param ${table.entityPath}
    */
    List<${entity}> find${entity}ByCondition(${entity} ${table.entityPath});

<#list table.fields as field>
 <#if field.keyFlag>
    /**
    *  根据主键${field.propertyName}删除${table.name}数据
    *  @param ${field.propertyName}
    */
    Integer delete${entity}ById(@Param("${field.propertyName}") ${field.propertyType} ${field.propertyName});
 </#if>
</#list>

<#list table.fields as field>
 <#if field.keyFlag>
    /**
    *  根据主键${field.propertyName}更新表${table.name}信息
    *  @param ${table.entityPath}
    */
    Integer update${entity}ById(${entity} ${table.entityPath});
 </#if>
</#list>

<#list table.fields as field>
 <#if field.keyFlag>
    /**
    *  新增表${table.name}数据
    *  @param ${table.entityPath}
    */
    Integer create${entity}(${entity} ${table.entityPath});
 </#if>
</#list>
}
