package cn.langqyi.mybatis.util;

import java.io.InputStream;

/**
 * 资源工作类
 */
public class Resource {

    /**
     * 从类路径中获得配置文件的输入流
     * @param config
     * @return
     */
    public static InputStream getResourceAsStream(String config) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(config);
    }

}
