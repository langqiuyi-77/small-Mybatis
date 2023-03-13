package cn.langqyi.mybatis.session;

import cn.langqyi.mybatis.binding.MapperRegistry;
import cn.langqyi.mybatis.builder.BaseBuilder;
import cn.langqyi.mybatis.builder.xml.xmlConfigBuilder;
import cn.langqyi.mybatis.dataSource.UnpooledDatasource;
import cn.langqyi.mybatis.io.Resources;
import cn.langqyi.mybatis.mapping.MappedStatement;
import cn.langqyi.mybatis.session.defaults.DefaultSqlSessionFactory;
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

public class SqlSessionFactoryBuilder {
    //private MapperRegistry mapperRegistry;
    //private Map<String, MappedStatement> mappedStatements;
    //builder模式不要有成员变量

    public SqlSessionFactory build(InputStream resourceAsStream) throws DocumentException {
        //1. 创建一个xml解析创建Configuration对象
        BaseBuilder builder = new xmlConfigBuilder(resourceAsStream);
        //2. 创建SqlSessionFactory并返回
        return new DefaultSqlSessionFactory(((xmlConfigBuilder) builder).parse());
    }
}
