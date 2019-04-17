package example.dao;

import com.github.pagehelper.PageInfo;
import example.pojo.Role;
import example.pojo.User;

import java.util.List;
import java.util.Map;

public interface RoleDao {
    public Role findRoleById(int id);
    public Role findRoleByName(String name);
    public PageInfo<Role> findRoleAll(Map map);
    public int addRole(Map map);
    public PageInfo<Role> searchRole(Map map);
    public int deleteRole(int id);
    public int deleteUserRole(int id);
    public int batchDeleteRole(List list);
    public int batchDeleteRoleUser(List list);
    public int editRole(Map map);
}
