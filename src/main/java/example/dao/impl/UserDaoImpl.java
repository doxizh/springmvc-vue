package example.dao.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import example.dao.UserDao;
import example.pojo.User;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

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
        //这里的参数test.findUserById，test为命名空间，要与user.xml中的对应起来，
        //同理，findUserById也要在user.xml中存在，不然都会报错
        assert sqlSession != null;
        User user = sqlSession.selectOne("test.findUserById", id);
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
        User user = sqlSession.selectOne("test.findUserByName",name);
        sqlSession.close();
        return user;
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
        List<User> list= sqlSession.selectList("test.findUserAll");
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
        int num=sqlSession.insert("test.register",user);
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
        int num=sqlSession.update("test.updatePassword",user);
        sqlSession.commit();
        sqlSession.close();
        return num;
    }

    @Override
    public Boolean batchAddUser() {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;

        for(int i=1;i<101;i++){
            User user= new User();
            user.setPassword("123456");
            user.setName("测试".concat(String.valueOf(i)));
            sqlSession.insert("test.register",user);
        }
        sqlSession.commit();
        sqlSession.close();
        return true;
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
        List<User> list= sqlSession.selectList("test.searchUserByName",name);
        sqlSession.close();

        return new PageInfo<>(list);
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
        int num= sqlSession.delete("test.batchDeleteUser",list);
        sqlSession.commit();
        sqlSession.close();
        return num;
    }
}
