package cn.langqyi.mybatis.binding;

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

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
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
            //根据不同的类型调用不同的方法
            String sqlid = method.getDeclaringClass().getName() + "." + method.getName();
            String sqltype = sqlSession.getSqltype(sqlid);
            if ("INSERT".equals(sqltype))
                return sqlSession.insert(sqlid, args);
            else if ("DELETE".equals(sqltype))
                return sqlSession.delete(sqlid, args);
            else if ("UPDATE".equals(sqltype))
                return sqlSession.update(sqlid, args);
            else
                return sqlSession.selectOne(sqlid, args);
        }

    }
}
