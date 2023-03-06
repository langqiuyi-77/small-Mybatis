package cn.langqyi.mybatis.session;

import cn.langqyi.mybatis.binding.MapperRegistry;
import cn.langqyi.mybatis.io.Resources;
import cn.langqyi.mybatis.mapping.MappedStatement;
import cn.langqyi.mybatis.session.defaults.DefaultSqlSessionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SqlSessionFactoryBuilder {
    private MapperRegistry mapperRegistry;
    private Map<String, MappedStatement> mappedStatements;

    public SqlSessionFactory build(InputStream resourceAsStream) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(resourceAsStream);

        // 创建mapperRegister和mapperstatement的集合
        mapperRegistry = new MapperRegistry();

        // 构建sql对象(MappedStatement)的集合以及注册映射器
        Element mappersEle = (Element) document.selectSingleNode("//mappers");
        mappedStatements = getMappedStatements(mappersEle);

        return new DefaultSqlSessionFactory(mapperRegistry, mappedStatements);
    }

    /**
     * 创建出mappedStatements；并且根据包名注册映射器
     *
     * 注册映射器: 这个packagename都是从mapper_xml文件中namespace中获得的
     * @param mappersEle
     * @return
     */
    private Map<String, MappedStatement> getMappedStatements(Element mappersEle) {
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
