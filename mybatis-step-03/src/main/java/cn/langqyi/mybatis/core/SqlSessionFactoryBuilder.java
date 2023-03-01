package cn.langqyi.mybatis.core;

import cn.langqyi.mybatis.util.Resource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static cn.langqyi.mybatis.core.Const.*;

/**
 * sqlSessionFactory建造类
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactoryBuilder() {
    }

    /**
     * 解析xml文件，构建sqlSession对象
     * @param config
     * @return
     */
    public sqlSessionFactory build(InputStream config) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(config); //获得xml文件
        Element environmentsEle = (Element) document.selectSingleNode("/configuration/environments");
        String aDefault = environmentsEle.attributeValue("default");
        Element envirmentEle = (Element) document.selectSingleNode("/configuration/environments/environment[@id='" + aDefault + "']");
        Element transactionManagerEle = envirmentEle.element("transactionManager");
        Element dataSourceEle = envirmentEle.element("dataSource");

        // 构建dataSource对象，传入数据库的基本配置信息dirver,url,username,password
        DataSource dataSource = getDataSource(dataSourceEle);
        // 构造事务管理器，传入dataSource对象
        Transaction transaction = getTransaction(transactionManagerEle, dataSource);
        // 构建sql对象(MappedStatement)的集合
        Element mappersEle = (Element) document.selectSingleNode("//mappers");
        Map<String, MappedStatement> mappedStatements = getMappedStatements(mappersEle);

        // 创建并返回sqlSessionFactory
        return new sqlSessionFactory(transaction, mappedStatements);
    }

    /**
     * 获得MappedStatement；private方法是因为这个方法只是在build中使用
     * @param mappersEle
     * @return
     */
    private Map<String, MappedStatement> getMappedStatements(Element mappersEle) {
        Map<String, MappedStatement> mappedStatements = new HashMap<>();
        mappersEle.elements().forEach(mapperEle -> {
            // 1. 获取当前mapper文件的类路径地址
            String resourcePath = mapperEle.attributeValue("resource");
            // 2. 获取当前mapper文件的document
            SAXReader saxReader = new SAXReader();
            try {
                Document document = saxReader.read(Resource.getResourceAsStream(resourcePath)); //获得xml文件
                // 3. 获取当前mapper文件的namespace
                Element mapperele = (Element) document.selectSingleNode("/mapper");
                String namespace = mapperele.attributeValue("namespace");
                // 4. 变量所有的sql对象创建MappedStatement并且放入MappedStatements集合中
                mapperele.elements().forEach(ele -> {
                    String sql = ele.getTextTrim();//insert into t_user(id,name,password) values(#{id}, #{name}, #{password})
                    String id = ele.attributeValue("id");
                    String parameterType = ele.attributeValue("parameterType");
                    String resultType = ele.attributeValue("resultType");
                    String sqlType = ele.getName().toUpperCase();
                    mappedStatements.put(namespace + "." + id, new MappedStatement(sql, id, parameterType, resultType, sqlType));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return mappedStatements;
    }

    /**
     * 获得数据源
     * @param dataSourceEle
     * @return
     */
    private DataSource getDataSource(Element dataSourceEle) {
        DataSource dataSource = null;
        String type = dataSourceEle.attributeValue("type").trim().toUpperCase();
        if (UNPOOLED_DATASOURCE.equals(type)){
            Map<String, String> map = new HashMap<>();
            dataSourceEle.elements().forEach(propertyEle -> {
                map.put(propertyEle.attributeValue("name"), propertyEle.attributeValue("value"));
            });
            dataSource = new UNPOOLEDataSource(map.get("driver"), map.get("url"), map.get("username"), map.get("password"));
        } else if (POOLED_DATASOURCE.equals(type)){
            System.out.println("还没有实现POOLED数据源呢！！！！！");
        } else {
            System.out.println("还没有实现JNDI数据源呢！！！！！");
        }

        return dataSource;
    }

    /**
     * 获得事务管理器
     * @param transactionEle
     * @param dataSource
     * @return
     */
    private Transaction getTransaction(Element transactionEle, DataSource dataSource) {
        Transaction transaction = null;
        String type = transactionEle.attributeValue("type").trim().toUpperCase();
        if (type.equals("JDBC")) {
            transaction = new JDBCTransaction(dataSource);
        } else {
            System.out.println("MANAGED事务管理器还没实现呢！！！！");
        }
        return transaction;
    }

    // 局部测试看是否能正确地获得事务管理器，数据源，和sql对象集合
    public static void main(String[] args) throws DocumentException {
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        sqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resource.getResourceAsStream("godbatis-config.xml"));
        System.out.println(sqlSessionFactory);
    }
}
