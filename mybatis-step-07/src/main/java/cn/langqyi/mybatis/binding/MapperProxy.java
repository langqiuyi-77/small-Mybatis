package cn.langqyi.mybatis.binding;

import cn.langqyi.mybatis.session.Configuration;
import cn.langqyi.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * MapperProxy是一个动态代理类，这意味着当使用它的实例替代被代理对象后，被代理对象的方法
 * 会转接到MapperProxy的invoke方法上
 */
public class MapperProxy<T> implements InvocationHandler {

    private static final long serialVersionUID = -6424540398559729838L;

    private SqlSession sqlSession;
    private final Class<T> mapperInterface;
    private final Map<Method,MappedMethod> mappedMethods;

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method,MappedMethod> mappedMethods) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
        this.mappedMethods = mappedMethods;
    }

    /**
     * 代理方法；invoke方法重要的；
     * 分为两个部分：
     *  1. 获得sqlsession -> 用来真正执行数据库操作
     *  2. 依据内容调用合适的sqlsession方法
     * @param proxy 代理对象
     * @param method 代理方法
     * @param args 代理方法的参数
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (Object.class.equals(method.getDeclaringClass())) {  //继承自Object方法
                //直接执行原有方法
                return method.invoke(this, args);
        } else {

            final MappedMethod mappedMethod = cachedMappedMethod(method);
            return mappedMethod.execute(sqlSession, args);
        }
    }

    //获得缓存中的mappedMethod或者new一个放进缓存
    private MappedMethod cachedMappedMethod(Method method) {
        if (mappedMethods.get(method) == null)
            mappedMethods.put(method, new MappedMethod(method, sqlSession.getConfiguration()));
        return mappedMethods.get(method);
    }
}
