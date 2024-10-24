package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.common.exception.ServiceException;

import java.util.List;

@Service
public class ${table.serviceImplName} implements ${table.serviceName} {
    @Autowired
    private ${table.mapperName} ${table.entityPath}Mapper;

    /**
    *  查询表${table.name}所有数据
    */
    @Override
    public List<${entity}> findAll${entity}() throws ServiceException {
        return ${table.entityPath}Mapper.queryAll${entity}();
    }

<#list table.fields as field>
 <#if field.keyFlag>
    /**
    *  根据主键${field.propertyName}查询表${table.name}信息
    *  @param ${field.propertyName}
    */
    @Override
    public ${entity} find${entity}ById(@Param("${field.propertyName}") ${field.propertyType} ${field.propertyName}) throws ServiceException {
        return ${table.entityPath}Mapper.query${entity}ById(${field.propertyName});
    }
 </#if>
</#list>

    /**
    *  根据条件查询表${table.name}数据
    *  @param ${table.entityPath}
    */
    @Override
    public List<${entity}> find${entity}ByCondition(${entity} ${table.entityPath}) throws ServiceException {
        return ${table.entityPath}Mapper.query${entity}ByCondition(${table.entityPath});
    }

<#list table.fields as field>
 <#if field.keyFlag>
    /**
    *  根据主键${field.propertyName}删除表${table.name}数据
    *  @param ${field.propertyName}
    */
    @Override
    public Integer delete${entity}ById(@Param("${field.propertyName}") ${field.propertyType} ${field.propertyName}) throws ServiceException {
        return ${table.entityPath}Mapper.delete${entity}ById(${field.propertyName});
    }
 </#if>
</#list>

<#list table.fields as field>
 <#if field.keyFlag>
    /**
    *  根据主键${field.propertyName}更新表${table.name}数据
    *  @param ${table.entityPath}
    */
    @Override
    public Integer update${entity}ById(${entity} ${table.entityPath}) throws ServiceException {
        return ${table.entityPath}Mapper.update${entity}ById(${table.entityPath});
    }
 </#if>
</#list>

<#list table.fields as field>
 <#if field.keyFlag>
    /**
    *  新增表${table.name}数据
    *  @param ${table.entityPath}
    */
    @Override
    public Integer create${entity}(${entity} ${table.entityPath}) throws ServiceException {
        return ${table.entityPath}Mapper.create${entity}(${table.entityPath});
    }
 </#if>
</#list>
}
