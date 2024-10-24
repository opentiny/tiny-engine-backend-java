package com.tinyengine.it.common.utils;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class PermissionInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取原始的参数
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];

        // 在这里获取 session 中的 tenant 参数
        String tenantId = "1"; // 示例值

        // 检查参数类型并添加 tenantId
        if (parameter instanceof Map) {
            ((Map<String, Object>) parameter).put("tenantId", tenantId);
        } else {
            // 使用反射获取 setter 方法
            String setterMethodName = "setTenantId";
            Class<?> parameterClass = parameter.getClass();
            Method setterMethod = parameterClass.getMethod(setterMethodName, String.class);
            setterMethod.invoke(parameter, tenantId);
        }

        // 执行原有的查询或更新操作
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以在这里进行一些初始化设置
    }
}