package cn.langqyi.mybatis.binding;

import cn.langqyi.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * 我并不明白为什么要使用泛型，直接应用于所有接口不可以吗???
 * 动力节点老杜讲动态代理的时候自己写的工具类也不是泛型啊
 * @param <T>
 */
public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface;

    private final Map<Method,MappedMethod> mappedMethods;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
        this.mappedMethods = new HashMap<>();
    }

    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy(sqlSession, mapperInterface, mappedMethods);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }
}
