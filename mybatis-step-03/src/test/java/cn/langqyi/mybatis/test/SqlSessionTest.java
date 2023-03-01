package cn.langqyi.mybatis.test;

import cn.langqyi.mybatis.core.SqlSession;
import cn.langqyi.mybatis.core.SqlSessionFactoryBuilder;
import cn.langqyi.mybatis.core.sqlSessionFactory;
import cn.langqyi.mybatis.util.Resource;
import org.junit.Test;

public class SqlSessionTest {

    /**
     * 测试sqlSession的insert
     * @throws Exception
     */
    @Test
    public void testInsert() throws Exception {
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        sqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resource.getResourceAsStream("godbatis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // sqlid要是namespace.id才可以
        User obj = new User("222", "liling", "123456");
        int count = sqlSession.insert("user.insertUser", obj);
        sqlSession.commit();
    }

    /**
     * 测试sqlSession的selectOne
     */
    @Test
    public void testSelectOne() throws Exception {
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        sqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resource.getResourceAsStream("godbatis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User obj = new User("222", null, null);
        Object object = sqlSession.selectOne("user.selectUserById", obj);
        System.out.println(object.toString());
    }
}
