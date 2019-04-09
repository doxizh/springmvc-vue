package example.dao;

import example.pojo.User;

import java.util.ArrayList;
import java.util.List;

public interface UserDao {
    public User findUserByIdTest(int id);
    public User findUserByName(String name);
    public List<User> findUserAll();
    public int register(User user);
    public int updatePassword(User user);
}
