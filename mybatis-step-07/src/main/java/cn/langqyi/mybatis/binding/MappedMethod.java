package cn.langqyi.mybatis.binding;

import cn.langqyi.mybatis.session.Configuration;
import cn.langqyi.mybatis.session.SqlSession;

import java.lang.reflect.Method;

/**
 * 方法到SQL的绑定
 */
public class MappedMethod {

    private final String sqltype;
    private final String sqlid;

    public MappedMethod(Method method, Configuration configuration) {
        //在创建的时候就要设置sqltype
        sqlid = method.getDeclaringClass().getName() + "." + method.getName();
        sqltype = configuration.getMappedStatement(sqlid).getSqlType();
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        //根据不同的类型调用不同的方法
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
