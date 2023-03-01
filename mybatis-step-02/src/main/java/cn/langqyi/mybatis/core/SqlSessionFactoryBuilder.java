package cn.langqyi.mybatis.core;

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
        Element envirmentEle = (Element) document.selectSingleNode("/configuration/environments/environment[@id=" + aDefault + "]");
        Element transactionManagerEle = envirmentEle.element("transactionManager");
        Element dataSourceEle = envirmentEle.element("dataSource");

        // 构建dataSource对象，传入数据库的基本配置信息dirver,url,username,password
        DataSource dataSource = getDataSource(dataSourceEle);
        // 构造事务管理器，传入dataSource对象
        Transaction transaction = getTransaction(transactionManagerEle, dataSource);
        // 构建sql对象的集合



        // 创建并返回sqlSessionFactory
        return null;
    }

    // private方法是因为这个方法只是在build中使用
    private DataSource getDataSource(Element dataSourceEle) {
        DataSource dataSource = null;
        String type = dataSourceEle.attributeValue("type").trim().toUpperCase();
        if (UNPOOLED_DATASOURCE.equals(type)){
            Map<String, String> map = new HashMap<>();
            dataSourceEle.elements().forEach(propertyEle -> {
                map.put(propertyEle.attributeValue("name"), propertyEle.attributeValue("value"));
            });
            dataSource = new UNPOOLEDataSource(map.get("drive"), map.get("url"), map.get("username"), map.get("password"));
        } else if (POOLED_DATASOURCE.equals(type)){
            System.out.println("还没有实现POOLED数据源呢！！！！！");
        } else {
            System.out.println("还没有实现JNDI数据源呢！！！！！");
        }

        return dataSource;
    }

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
}
