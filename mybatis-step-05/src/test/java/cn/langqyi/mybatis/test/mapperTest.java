package cn.langqyi.mybatis.test;

import cn.langqyi.mybatis.binding.MapperRegistry;
import cn.langqyi.mybatis.mapping.MappedStatement;
import cn.langqyi.mybatis.session.SqlSession;
import cn.langqyi.mybatis.session.defaults.DefaultSqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 对mapper代理类的测试
 */
public class mapperTest {

    @Test
    public void testMapper() {
        //模拟的方法，不经过MapperProxyFactory去创建SQLSession，直接new一个对象处理
        MapperRegistry mapperRegistry = new MapperRegistry();
        mapperRegistry.addMapper(UserDao.class);    // 为UserDao接口创建一个动态代理实现类工厂放在mapperRegistry的knowmappers对象中
        Map<String, MappedStatement> mappedStatements = new HashMap<>();
        mappedStatements.put("cn.langqyi.mybatis.test.UserDao.insertUser",
                new MappedStatement("cn.langqyi.mybatis.test.UserDao.insertUser", null, "insert into t_user(id,name,password) values(#{id}, #{name}, #{password})", null, "insert"));
        mappedStatements.put("cn.langqyi.mybatis.test.UserDao.selectUserById",
                new MappedStatement("cn.langqyi.mybatis.test.UserDao.selectUserById", "cn.langqyi.mybatis.test.User", "select * from t_user where id = #{id}", null, "select"));
        SqlSession sqlSession = new DefaultSqlSession(mapperRegistry, mappedStatements);

        //使用getMapper得到动态代理实现类
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        mapper.insertUser(null);
        mapper.selectUserById(1);
    }

}
