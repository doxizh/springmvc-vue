package example.dao;

import com.github.pagehelper.PageInfo;
import example.pojo.User;
import example.pojo.UserRole;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UserDao {
    public User findUserByIdTest(int id);
    public User findUserByName(String name);
    public List<User> findUserByNames(List list);
    public PageInfo<User> findUserAll(int pageNum,int pageSize);
    public int register(User user);
    public int updatePassword(User user);
    public Boolean batchAddUser(List<User> list,List roleList);
    public PageInfo<User> searchUserByName(String name,int pageSize);
    public PageInfo<User> searchUser(Map map);
    public int deleteUser(int id);
    public int deleteUserRole(int userId);
    public int batchDeleteUser(List list);
    public int batchDeleteUserRole(List list);
    public int editUser(int id,String nickname);
    public int createUserRole(UserRole userRole);
    public int createUserRole(List list);
}
