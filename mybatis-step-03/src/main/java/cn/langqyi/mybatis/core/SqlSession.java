package cn.langqyi.mybatis.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Map;

/**
 * 这是Mybatis的门面模式的接口
 */
public class SqlSession {

    private Transaction transaction;
    private Map<String, MappedStatement> mappedStatements;

    public SqlSession(Transaction transaction, Map<String, MappedStatement> mappedStatements) {
        this.transaction = transaction;
        this.mappedStatements = mappedStatements;
    }

    /**
     * 提交事务
     */
    public void commit() {
        transaction.commit();
    }

    /**
     * 回滚事务
     */
    public void rollback() {
        transaction.rollback();
    }

    /**
     * 插入元素通过pojo对象
     * @param sqlId namespace.id
     * @param obj
     * @return
     */
    public int insert(String sqlId, Object pojo) throws Exception {
        //1. 获取连接对象
        Connection conn = transaction.getConnection();
        //2. 获取sql语句：insert into t_user(id,name,password) values(#{id}, #{name}, #{password})
        String sql = mappedStatements.get(sqlId).getSql();
        //3. 将sql语句更改为使用占位符：insert into t_user(id,name,password) values(?, ?, ?)
        String newSql = sql.replaceAll("#\\{[a-zA-Z0-9]*}", "?"); //正则表达式
        //4. 生成对应的statement
        PreparedStatement stmt = conn.prepareStatement(newSql);
        //5. 设置参数，要求在sql中的#{}里面名字是能直接映射到pojo对象的，通过反射实现根据属性名访问属性
        int fromchar = 0, index = 1;
        while (true) {
            int leftindex = sql.indexOf('{', fromchar);
            if (leftindex < 0) break;
            int rightindex = sql.indexOf('}', fromchar);
            fromchar = rightindex + 1;
            //截取内部的属性名
            String fieName = sql.substring(leftindex + 1, rightindex);
            //在这里不知道是使用setInt还是setString...的，为方便，我们就设置是string的，这是一大缺点
//            stmt.setString(index++, (String) pojo.getClass().getField(fieName).get(pojo)); getField只能是public的类
            //stmt.setString(index++, (String) pojo.getClass().getDeclaredField(fieName).get(pojo));
            //因为是private方法所有必须使用get方法来获得值
            Method method = pojo.getClass().getMethod("get" + fieName.toUpperCase().charAt(0) + fieName.substring(1));
            stmt.setString(index++, method.invoke(pojo).toString());
        }
        //6. 执行sql
        return stmt.executeUpdate();
    }

    /**
     * 查询一个元素
     * @param sqlId
     * @param paremeter
     * @return
     * @throws Exception
     */
    public Object selectOne(String sqlId, Object paremeter) throws Exception {
        Object ret = null;
        //1. 获取连接对象
        Connection conn = transaction.getConnection();
        //2. 获取sql语句：select * from t_user where id = #{id}
        String sql = mappedStatements.get(sqlId).getSql();
        //3. 将sql语句更改为使用占位符：select * from t_user where id = ?
        String newSql = sql.replaceAll("#\\{[a-zA-Z0-9]*}", "?"); //正则表达式
        //4. 生成对应的statement
        PreparedStatement stmt = conn.prepareStatement(newSql);
        //5. 设置参数，要求在sql中的#{}里面名字是能直接映射到pojo对象的，通过反射实现根据属性名访问属性
        int fromchar = 0, index = 1;
        while (true) {
            int leftindex = sql.indexOf('{', fromchar);
            if (leftindex < 0) break;
            int rightindex = sql.indexOf('}', fromchar);
            fromchar = rightindex + 1;
            //截取内部的属性名
            String fieName = sql.substring(leftindex + 1, rightindex);
            //在这里不知道是使用setInt还是setString...的，为方便，我们就设置是string的，这是一大缺点
//            stmt.setString(index++, (String) pojo.getClass().getField(fieName).get(pojo)); getField只能是public的类
            //stmt.setString(index++, (String) pojo.getClass().getDeclaredField(fieName).get(pojo));
            //因为是private方法所有必须使用get方法来获得值
            Method method = paremeter.getClass().getMethod("get" + fieName.toUpperCase().charAt(0) + fieName.substring(1));
            stmt.setString(index++, method.invoke(paremeter).toString());
        }
        //6. 执行sql
        ResultSet resultSet =  stmt.executeQuery();
        //7. 结果集映射: 这里还是使用的反射，因为要把数据注入到对象的object的属性中，所以有规定resultset的col的名字要和object的属性相同
        if (resultSet.next()) {
            //a. 创建一个result对象的实例
            String resultType = mappedStatements.get(sqlId).getResultType();
            Class aClass = Thread.currentThread().getContextClassLoader().loadClass(resultType);
            ret = aClass.newInstance();
            //b. 因为是private方法所以要通过set方法将值设置进去
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {    //colindex是从1开始
                String columnName = metaData.getColumnName(i);
                //获取其set方法
                Method method = aClass.getMethod("set" + columnName.toUpperCase().charAt(0) + columnName.substring(1), aClass.getDeclaredField(columnName).getType());
                method.invoke(ret, resultSet.getString(i));
            }
        }
        return ret;
    }

}
