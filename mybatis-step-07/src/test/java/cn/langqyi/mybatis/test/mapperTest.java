package cn.langqyi.mybatis.test;

import cn.langqyi.mybatis.binding.MapperRegistry;
import cn.langqyi.mybatis.io.Resources;
import cn.langqyi.mybatis.mapping.MappedStatement;
import cn.langqyi.mybatis.session.SqlSession;
import cn.langqyi.mybatis.session.SqlSessionFactory;
import cn.langqyi.mybatis.session.SqlSessionFactoryBuilder;
import cn.langqyi.mybatis.session.defaults.DefaultSqlSession;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 对mapper代理类的测试
 */
public class mapperTest {

    /**
     * 进一步实现对数据源的加载创建->初始化
     */
    @Test
    public void testDataSource() throws DocumentException {
        // 1. 创建sqlsessionfactoryBuilder对象
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        // 2. 创建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis-config.xml"));
        // 3. 创建sqlsession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //正常使用
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        mapper.insertUser(null);
        mapper.selectUserById(1);
    }

}
