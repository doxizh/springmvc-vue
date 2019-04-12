package example.dao.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import example.dao.RoleDao;
import example.pojo.Role;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static example.dao.ConnectSql.getSqlSessionFactory;

public class RoleDaoImpl implements RoleDao {
    @Override
    public Role findRoleById(int id) {
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
        //这里的参数Role.findRoleById，Role为命名空间，要与Role.xml中的对应起来，
        //同理，findRoleById也要在Role.xml中存在，不然都会报错
        assert sqlSession != null;
        Role Role = sqlSession.selectOne("Role.findRoleById", id);
        System.out.println(Role.getId());
        System.out.println(Role.getRoleName());
        // 释放资源
        sqlSession.close();
        return Role;
    }

    @Override
    public Role findRoleByName(String name) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        Role Role = sqlSession.selectOne("Role.findRoleByName",name);
        sqlSession.close();
        return Role;
    }

    @Override
    public PageInfo<Role> findRoleAll(int pageNum,int pageSize) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        PageHelper.startPage(pageNum,pageSize);
        List<Role> list= sqlSession.selectList("Role.findRoleAll");
        sqlSession.close();

        return new PageInfo<>(list);
    }

    @Override
    public int addRole(Role role) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        int num=sqlSession.insert("Role.addRole",role);
        sqlSession.commit();
        sqlSession.close();

        return num;
    }

    @Override
    public PageInfo<Role> searchRole(Map map) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        PageHelper.startPage(1, (int) map.get("pageSize"));
        List<Role> list= sqlSession.selectList("Role.searchRole",map);
        sqlSession.close();

        return new PageInfo<>(list);
    }

    @Override
    public int deleteRole(int id) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        int num= sqlSession.delete("Role.deleteRole",id);
        sqlSession.commit();
        sqlSession.close();
        return num;
    }

    @Override
    public int batchDeleteRole(List list) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        int num= sqlSession.delete("Role.batchDeleteRole",list);
        sqlSession.commit();
        sqlSession.close();
        return num;
    }

    @Override
    public int editRole(int id,String roleName) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSessionFactory().openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sqlSession != null;
        Role Role =new Role();
        Role.setId(id);
        Role.setRoleName(roleName);
        int num= sqlSession.update("Role.editRole",Role);
        sqlSession.commit();
        sqlSession.close();
        return num;
    }
}
