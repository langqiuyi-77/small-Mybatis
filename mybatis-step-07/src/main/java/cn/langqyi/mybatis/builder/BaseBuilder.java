package cn.langqyi.mybatis.builder;

import cn.langqyi.mybatis.session.Configuration;

/**
 * 用来创建configuration的builder类接口
 * 构造器基类，建造者模式
 */
public abstract class BaseBuilder {
    /*
        必须在定义时或者构造器中进行初始化赋值，
        而且final变量一旦被初始化赋值之后，就不能再被赋值了
        configuration使用final修饰，程序运行后configuration是不会变的
     */
    protected final Configuration configuration; //protected表示可以被类的子类访问到

    public BaseBuilder() {
        configuration = new Configuration();
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
