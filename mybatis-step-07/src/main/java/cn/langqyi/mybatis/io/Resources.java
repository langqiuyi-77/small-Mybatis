package cn.langqyi.mybatis.io;

import java.io.InputStream;

public class Resources {

    public static InputStream getResourceAsStream(String s) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(s);
    }


    /**
     * 获得对应类的包名
     * @param namespace
     * @return
     */
    public static String classForPackageName(String namespace) throws ClassNotFoundException {
        return Thread.currentThread().getContextClassLoader().loadClass(namespace).getPackage().getName();
    }
}
