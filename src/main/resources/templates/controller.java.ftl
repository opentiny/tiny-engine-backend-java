package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.annotation.Validated;
<#if restControllerStyle>
    import org.springframework.web.bind.annotation.RestController;
<#else>
    import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
    import ${superControllerClassPackage};
</#if>
import com.tinyengine.it.common.base.Result;
import java.util.ArrayList;
import java.util.List;

/**
* <p>
    * ${table.comment!} 前端控制器
    * </p>
*
* @author ${author}
* @since ${date}
*/
@Validated
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
public class ${table.controllerName} {
    </#if>
    @Autowired
    private ${table.serviceName} ${table.serviceName?uncap_first};

    @GetMapping("/")
    public Result< List<${entity}>> getAll${entity}() {
    List<${entity}> ${entity}List = new ArrayList<${entity}>();
    ${entity}List = ${table.serviceName?uncap_first}.findAll${entity}();
    return Result.success(${entity}List);
    }

    @GetMapping("/{id}")
    public Result<${entity}> get${entity}ById(@PathVariable Integer id) {
        ${entity} ${entity?uncap_first} = ${table.serviceName?uncap_first}.find${entity}ById(id);
        return Result.success(${entity?uncap_first});
    }

    @PostMapping("/")
    public Result<${entity}> create${entity}(@Valid @RequestBody ${entity} ${entity?uncap_first}) {
        ${table.serviceName?uncap_first}.create${entity}(${entity?uncap_first});
        int id = ${entity?uncap_first}.getId();
        ${entity?uncap_first} = ${table.serviceName?uncap_first}.find${entity}ById(id);
        return Result.success(${entity?uncap_first});
    }

    @PutMapping("/{id}")
    public Result<${entity}> update${entity}(@PathVariable Integer id, @RequestBody ${entity} ${entity?uncap_first}) {
        ${entity?uncap_first}.setId(id);
        ${table.serviceName?uncap_first}.update${entity}ById(${entity?uncap_first});
        ${entity?uncap_first} = ${table.serviceName?uncap_first}.find${entity}ById(id);
        return Result.success(${entity?uncap_first});
    }

    @DeleteMapping("/{id}")
    public Result<${entity}> delete${entity}(@PathVariable Integer id) {
        ${entity} ${entity?uncap_first} = ${table.serviceName?uncap_first}.find${entity}ById(id);
        ${table.serviceName?uncap_first}.delete${entity}ById(id);
        return Result.success(${entity?uncap_first});
    }
}
</#if>
