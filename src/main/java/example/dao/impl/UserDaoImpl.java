package example.dao.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import example.dao.UserDao;
import example.pojo.User;
import example.pojo.UserRole;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static example.dao.ConnectSql.getSqlSessionFactory;

public class UserDaoImpl implements UserDao {
    @Override
    public User findUserByIdTest(int id) {
        // 通过工厂得到SqlSession
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 通过SqlSession操作数据库
        // 第一个参数：映射文件中statement的id，等于=namespace+"."+statement的id
        // 第二个参数：指定和映射文件中所匹配的parameterType类型的参数
        // sqlSession.selectOne结果 是与映射文件中所匹配的resultType类型的对象

        // selectOne查询出一条记录（这种很麻烦的！！！往后看看）
        //这里的参数user.findUserById，user为命名空间，要与user.xml中的对应起来，
        //同理，findUserById也要在user.xml中存在，不然都会报错
        assert sqlSession != null;
        User user = sqlSession.selectOne("user.findUserById", id);
        System.out.println(user.getId());
        System.out.println(user.getName());
        System.out.println(user.getPassword());
        // 释放资源
        sqlSession.close();
        return user;
    }

    @Override
    public User findUserByName(String name) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        User user = sqlSession.selectOne("user.findUserByName",name);
        sqlSession.close();
        return user;
    }

    @Override
    public List<User> findUserByNames(List list) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        List<User> userList = sqlSession.selectList("user.findUserByNames",list);
        sqlSession.close();
        return userList;
    }

    @Override
    public PageInfo<User> findUserAll(int pageNum,int pageSize) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        PageHelper.startPage(pageNum,pageSize);
        List<User> list= sqlSession.selectList("user.findUserAll");
        sqlSession.close();

        return new PageInfo<>(list);
    }

    @Override
    public int register(User user) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        int num=sqlSession.insert("user.register",user);
        sqlSession.commit();
        sqlSession.close();
        return num;
    }

    @Override
    public int updatePassword(User user) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        int num=sqlSession.update("user.updatePassword",user);
        sqlSession.commit();
        sqlSession.close();
        return num;
    }

    @Override
    public Boolean batchAddUser(List<User> list,List roleList) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        for (int i = 0; i < list.size(); i++) {
            User user=list.get(i);
            sqlSession.insert("user.register",user);
            if(roleList!=null&&roleList.size()>0){
                List<Map<String,Object>> list1 = new ArrayList<>();
                getRoleList(roleList, user, list1);
                sqlSession.insert("UserRole.batchAddUserRole",list1);
            }
        }
        sqlSession.commit();
        sqlSession.close();
        return true;
    }

    public static void getRoleList(List roleList, User user, List<Map<String, Object>> list1) {
        for (int j = 0; j < roleList.size(); j++) {
            Map<String,Object> map1=new HashMap<>();
            map1.put("userId",user.getId());
            map1.put("roleId",roleList.get(j));
            list1.add(map1);
        }
    }

    @Override
    public PageInfo<User> searchUserByName(String name,int pageSize) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        PageHelper.startPage(1,pageSize);
        List<User> list= sqlSession.selectList("user.searchUserByName",name);
        sqlSession.close();

        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<User> searchUser(Map map) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<User> list= sqlSession.selectList("user.searchUser",map);
        if(map.get("roleId")!=null&&list.size()>0){
            int total = (int) new PageInfo<>(list).getTotal();
            list = sqlSession.selectList("user.searchUserByRoleId",list);
            sqlSession.close();
            PageInfo<User> list1=new PageInfo<>(list);
            list1.setTotal(total);
            return list1;
        }
        sqlSession.close();
        return new PageInfo<>(list);
    }

    @Override
    public int deleteUser(int id) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        int num= sqlSession.delete("user.deleteUser",id);
        sqlSession.commit();
        sqlSession.close();
        return num;
    }

    @Override
    public int deleteUserRole(int userId) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        int num= sqlSession.delete("UserRole.deleteUserRole",userId);
        sqlSession.commit();
        sqlSession.close();
        return num;
    }

    @Override
    public int batchDeleteUser(List list) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        int num= sqlSession.delete("user.batchDeleteUser",list);
        sqlSession.commit();
        sqlSession.close();
        return num;
    }

    @Override
    public int batchDeleteUserRole(List list) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        int num= sqlSession.delete("UserRole.batchDeleteUserRole",list);
        sqlSession.commit();
        sqlSession.close();
        return num;
    }

    @Override
    public int editUser(int id,String nickname) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        User user =new User();
        user.setId(id);
        user.setNickname(nickname);
        int num= sqlSession.update("user.editUser",user);
        sqlSession.commit();
        sqlSession.close();
        return num;
    }

    @Override
    public int createUserRole(UserRole userRole) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        int num= sqlSession.insert("UserRole.addUserRole",userRole);
        sqlSession.commit();
        sqlSession.close();
        return num;
    }

    @Override
    public int createUserRole(List list) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        int num= sqlSession.insert("UserRole.batchAddUserRole",list);
        sqlSession.commit();
        sqlSession.close();
        return num;
    }
}
