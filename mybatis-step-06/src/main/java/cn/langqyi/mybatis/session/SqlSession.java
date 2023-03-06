package cn.langqyi.mybatis.session;

/**
 * sqlSession是用来执行Sql，获取映射器对象以及后续管理事务操作的标准接口
 */
public interface SqlSession {

    /**
     * 根据指定sqlId插入一条数据
     * @param sqlid
     * @param parameter
     * @return
     */
    int insert(String sqlid, Object parameter);

    /**
     * 根据指定的sqlId删除对应的数据
     * @param sqlid
     * @param parameter
     * @return
     */
    int delete(String sqlid, Object parameter);

    /**
     * Retrieve a single row mapped from the statement key
     * 根据指定的SqlID获取一条记录的封装对象
     *
     * @param <T>       the returned object type 封装之后的对象类型
     * @param sqlid sqlID
     * @return Mapped object 封装之后的对象
     */
    <T> T selectOne(String sqlid);

    /**
     * Retrieve a single row mapped from the statement key and parameter.
     * 根据指定的SqlID获取一条记录的封装对象，只不过这个方法容许我们可以给sql传递一些参数
     * 一般在实际使用中，这个参数传递的是pojo，或者Map或者ImmutableMap
     *
     * @param <T>       the returned object type
     * @param sqlid Unique identifier matching the statement to use.
     * @param parameter A parameter object to pass to the statement.
     * @return Mapped object
     */
    <T> T selectOne(String sqlid, Object parameter);

    /**
     * Retrieves a mapper.
     * 得到映射器，这个巧妙的使用了泛型，使得类型安全
     *
     * @param <T>  the mapper type
     * @param type Mapper interface class
     * @return a mapper bound to this SqlSession
     */
    <T> T getMapper(Class<T> type);

    /**
     * 根据指定的sqlid更新对应的数据
     * @param sqlid
     * @param parameter
     */
    int update(String sqlid, Object parameter);

    /**
     * 传入packagename+id作为sqlid返回sql的类型：是insert还是select等等
     * insert，update，delete
     * @param sqlid
     * @return
     */
    String getSqltype(String sqlid);

}
