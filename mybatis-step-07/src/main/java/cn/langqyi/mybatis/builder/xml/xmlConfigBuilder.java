package cn.langqyi.mybatis.builder.xml;

import cn.langqyi.mybatis.binding.MapperRegistry;
import cn.langqyi.mybatis.builder.BaseBuilder;
import cn.langqyi.mybatis.dataSource.UnpooledDatasource;
import cn.langqyi.mybatis.io.Resources;
import cn.langqyi.mybatis.mapping.MappedStatement;
import cn.langqyi.mybatis.session.Configuration;
import cn.langqyi.mybatis.transaction.Transaction;
import cn.langqyi.mybatis.transaction.jdbc.JDBCTransaction;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static cn.langqyi.mybatis.util.Const.POOLED_DATASOURCE;
import static cn.langqyi.mybatis.util.Const.UNPOOLED_DATASOURCE;

/**
 * 通过解析xml方法获得Configuration类
 */
public class xmlConfigBuilder extends BaseBuilder {
    private Document document;

    public xmlConfigBuilder(InputStream confi) {
        super();    //调用父类的方法创建一个configuration类
        SAXReader saxReader = new SAXReader();
        document = null;
        try { //在这里解决这个异常
            document = saxReader.read(confi);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提供给外部的接口，解析配置文件构建出configuration类并返回
     * @return Configuration
     */
    public Configuration parse() {
        //1. 获得使用的environment
        Element environmentsEle = (Element) document.selectSingleNode("/configuration/environments");
        String aDefault = environmentsEle.attributeValue("default");
        Element envirmentEle = (Element) document.selectSingleNode("/configuration/environments/environment[@id='" + aDefault + "']");
        Element transactionManagerEle = envirmentEle.element("transactionManager");
        Element dataSourceEle = envirmentEle.element("dataSource");

        //2. 获得数据源
        DataSource dataSource = getDataSource(dataSourceEle);
        //3. 获得事务管理器
        Transaction transaction = getTransaction(transactionManagerEle, dataSource);
        //4. 获得对应的sql语句对象并注册
        MapperRegistry mapperRegistry = new MapperRegistry();
        Element mappersEle = (Element) document.selectSingleNode("//mappers");
        Map<String, MappedStatement> mappedStatements = getMappedStatements(mapperRegistry, mappersEle);

        //5. 构建Configuration类
        return Configuration.builder().Transaction(transaction).MapperRegistry(mapperRegistry).MappedStatements(mappedStatements).build();
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
            dataSource = new UnpooledDatasource(map.get("driver"), map.get("url"), map.get("username"), map.get("password"));
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

    /**
     * 创建出mappedStatements；并且根据包名注册映射器
     *
     * 注册映射器: 这个packagename都是从mapper_xml文件中namespace中获得的
     * @param mappersEle
     * @return
     */
    private Map<String, MappedStatement> getMappedStatements(MapperRegistry mapperRegistry, Element mappersEle) {
        Map<String, MappedStatement> mappedStatements = new HashMap<>();
        mappersEle.elements().forEach(mapperEle -> {
            // 1. 获取当前mapper文件的类路径地址
            String resourcePath = mapperEle.attributeValue("resource");

            // 2. 获取当前mapper文件的document解析sql为mappedStatement
            SAXReader saxReader = new SAXReader();
            try {
                Document document = saxReader.read(Resources.getResourceAsStream(resourcePath)); //获得xml文件
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
                    mappedStatements.put(namespace + "." + id, new MappedStatement(namespace + "." + id, resultType, sql, parameterType, sqlType));
                });
                // 5. 通过 namespace=得到包 注册映射器：生成代理类
                mapperRegistry.addMappers(Resources.classForPackageName(namespace));
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        return mappedStatements;
    }
}
