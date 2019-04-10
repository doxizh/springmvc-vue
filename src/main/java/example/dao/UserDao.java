package example.dao;

import com.github.pagehelper.PageInfo;
import example.pojo.User;

import java.util.ArrayList;
import java.util.List;

public interface UserDao {
    public User findUserByIdTest(int id);
    public User findUserByName(String name);
    public PageInfo<User> findUserAll(int pageNum,int pageSize);
    public int register(User user);
    public int updatePassword(User user);
    public Boolean batchAddUser();
}
