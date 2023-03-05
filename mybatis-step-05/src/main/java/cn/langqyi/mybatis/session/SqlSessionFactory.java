package cn.langqyi.mybatis.session;

public interface SqlSessionFactory {

    /**
     * 打开一个 session
     * @return
     */
    SqlSession openSession();
}
