package cn.langqyi.mybatis.core;

/**
 * 这是Sql的映射类
 */
public class MappedStatement {

    private String sql; //sql语句select * from t_user where id = #{id}
    private String id; //user.insertUser加上namespace
    private String parameterType;
    private String resultType;  //cn.langqyi.mybatis.test.User"

    private String sqlType; // INSERT, SELECT...

    public MappedStatement(String sql, String id, String parameterType, String resultType, String sqlType) {
        this.sql = sql;
        this.id = id;
        this.parameterType = parameterType;
        this.resultType = resultType;
        this.sqlType = sqlType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }
}
