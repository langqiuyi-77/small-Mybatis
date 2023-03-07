package cn.langqyi.mybatis.test;

public interface UserDao {

    public int insertUser(User user);

    public User selectUserById(int id);
}
