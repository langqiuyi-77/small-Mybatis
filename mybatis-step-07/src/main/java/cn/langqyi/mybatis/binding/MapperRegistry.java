package cn.langqyi.mybatis.binding;

import cn.hutool.core.lang.ClassScanner;
import cn.langqyi.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 包装一个可以扫描包路径的完成映射的注册器类
 * 自动扫描包下接口并把每一个接口类映射的代理类全部存入映射器代理的HashMap缓存中
 */
public class MapperRegistry {

    /**
     * 将已经添加的映射器代理加入HashMap
     */
    private final Map<Class<?>, MapperProxyFactory<?>> knowMappers = new HashMap<>();

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final MapperProxyFactory<T> mapperProxyFactory =  (MapperProxyFactory<T>) knowMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    public <T> void addMapper(Class<T> type) {
        /* Mapper 必须是接口才会注册 */
        if (type.isInterface()) {
            if (hasMapper(type)) {
                // 如果重复添加了，报错
                throw new RuntimeException("Type " + type + " is already known to the MapperRegistry.");
            }
            // 注册映射器代理工厂
            knowMappers.put(type, new MapperProxyFactory<>(type));
        }
    }

    private  <T> boolean hasMapper(Class<T> type) {
        return knowMappers.containsKey(type);
    }

    public void addMappers(String packageName) {
        Set<Class<?>> mapperSet = ClassScanner.scanPackage(packageName);
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }
}
