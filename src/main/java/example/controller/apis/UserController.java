package example.controller.apis;

import com.github.pagehelper.PageInfo;
import example.dao.impl.UserDaoImpl;
import example.pojo.User;
import example.pojo.UserRole;
import example.tools.ModelResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/front")
public class UserController {
    @RequestMapping("/findUserByIdTest")
    @ResponseBody
    public ModelResult findUserByIdTest(HttpServletRequest request) throws IOException {
        UserDaoImpl usertest=new UserDaoImpl();
        User user=usertest.findUserByIdTest(1);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id",user.getId());
        map.put("name",user.getName());
        map.put("password",user.getPassword());
        return ModelResult.newSuccess(map);
    }
    @RequestMapping("/findUserAll")
    @ResponseBody
    public ModelResult findUserAll(HttpServletRequest request,int pageSize,int pageNum) throws IOException {
        UserDaoImpl usertest=new UserDaoImpl();
        PageInfo<User> list=usertest.findUserAll(pageNum,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("total",list.getTotal());
        map.put("list",list.getList());
        return ModelResult.newSuccess(map);
    }
    @RequestMapping("/isLogin")
    @ResponseBody
    public ModelResult isLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session=request.getSession();
        /*Cookie []cookies=request.getCookies();
        String c = null;
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("JSESSIONID")){
                c=cookie.getValue();
                break;
            }
        }
        if(c!=null){
            if(session.getId().equals(c)){
                return ModelResult.newSuccess(true);
            }
        }*/
        if(session.isNew()){
            System.out.println("新session");
        }else {
            String username= (String) session.getAttribute("username");
            if(username!=null){
                return ModelResult.newSuccess(true);
            }
        }
        return ModelResult.newError("401","当前用户未登录",false);
    }
    @RequestMapping("/login")
    @ResponseBody
    public ModelResult login(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String,Object> map) throws IOException {
        String username= (String) map.get("name");
        String password= (String) map.get("password");
        UserDaoImpl usertest=new UserDaoImpl();
        User user=usertest.findUserByName(username);
        if(user!=null){
            if(user.getName().equals(username)&&user.getPassword().equals(password)){
                HttpSession session=request.getSession();
                session.setAttribute("username", username);
                Map<String,Object> map1=new HashMap<>();
                Map<String,Object> userData=new HashMap<>();
                userData.put("name",user.getName());
                map1.put("userData",userData);
                return ModelResult.newSuccess(map1);
            }else {
                return ModelResult.newError("403","用户名或密码不正确",false);
            }
        }else {
            return ModelResult.newError("403","用户名不存在",false);
        }
    }
    @RequestMapping("/logout")
    @ResponseBody
    public ModelResult logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.isNew();
        session.removeAttribute("username");
        return ModelResult.newSuccess(true);
    }

    @RequestMapping("/register")
    @ResponseBody
    public ModelResult register(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String,Object> map) throws IOException {
        String username= (String) map.get("name");
        String password = (String) map.get("password");
        if(username==null||password==null||"".equals(username)||"".equals(password)){
            return ModelResult.newError("400","参数不正确",false);
        }
        UserDaoImpl userDao=new UserDaoImpl();
        User findUser=userDao.findUserByName(username);
        if(findUser!=null){
            return ModelResult.newError("403","用户名已存在",false);
        }else {
            User user=new User();
            user.setName(username);
            user.setPassword(password);
            int num = userDao.register(user);
            if (num > 0) {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(0);
                int num1 = userDao.createUserRole(userRole);
                if(num1>0){
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    Map<String, Object> map1 = new HashMap<>();
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("name", username);
                    map1.put("userData", userData);
                    return ModelResult.newSuccess(map1);
                }else {
                    return ModelResult.newError("403", "创建默认角色失败", false);
                }
            } else {
                return ModelResult.newError("403", "注册失败", false);
            }
        }
    }

    @RequestMapping("/updatePassword")
    @ResponseBody
    public ModelResult updatePassword(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String,Object> map) throws IOException {
        String oldPassword= (String) map.get("oldPassword");
        String newPassword= (String) map.get("newPassword");
        HttpSession session = request.getSession();
        String username= (String) session.getAttribute("username");
        UserDaoImpl usertest=new UserDaoImpl();
        User user =usertest.findUserByName(username);
        if(user!=null){
            if(user.getPassword().equals(oldPassword)){
                user.setPassword(newPassword);
                usertest.updatePassword(user);
                return ModelResult.newSuccess(true);
            }else {
                return ModelResult.newError("403","原密码不正确",false);
            }
        }else {
            return ModelResult.newError("403","用户不存在",false);
        }
    }

    @RequestMapping("/addUser")
    @ResponseBody
    public ModelResult addUser(HttpServletRequest request, HttpServletResponse response,@RequestBody Map map) throws IOException {
        String name= (String) map.get("name");
        String nickname= (String) map.get("nickname");
        List list = (List) map.get("roleIds");
        UserDaoImpl userDao=new UserDaoImpl();
        User user=userDao.findUserByName(name);
        if(user!=null){
            return ModelResult.newError("403","用户名已存在",false);
        }else {
            User user1=new User();
            user1.setPassword("123456");
            user1.setName(name);
            user1.setNickname(nickname);
            userDao.register(user1);
            if(list!=null&&list.size()>0){
                userDao.deleteUserRole((int) user1.getId());
                List<Map<String,Object>> list1 = new ArrayList<>();
                UserDaoImpl.getRoleList(list, user1, list1);
                userDao.createUserRole(list1);
            }
            return ModelResult.newSuccess(true);
        }
    }

    @RequestMapping("/batchAddUser")
    @ResponseBody
    public ModelResult batchAddUser(HttpServletRequest request, HttpServletResponse response,@RequestBody Map map) throws IOException {
        String name= (String) map.get("name");
        String nickname= (String) map.get("nickname");
        int num = (int) map.get("num");
        int index = (int) map.get("index");
        List roleList = (List) map.get("roleIds");
        List<String> nameList= new ArrayList<>();
        List<User> addUserList= new ArrayList<>();
        for (int i = index; i < num+index ; i++) {
            String name1=name.concat(String.valueOf(i));
            String nickname1=nickname.concat(String.valueOf(i));
            nameList.add(name1);
            User user1=new User();
            user1.setPassword("123456");
            user1.setName(name1);
            user1.setNickname(nickname1);
            addUserList.add(user1);
        }
        UserDaoImpl userDao=new UserDaoImpl();
        List<User> userList=userDao.findUserByNames(nameList);
        if(userList!=null&&userList.size()>0){
            return ModelResult.newError("403","用户名已存在",false);
        }else {
            Boolean flag=userDao.batchAddUser(addUserList,roleList);
            return flag?ModelResult.newSuccess(true):ModelResult.newError(false);
        }
    }

    @RequestMapping("/searchUserByName")
    @ResponseBody
    public ModelResult searchUserByName(HttpServletRequest request, HttpServletResponse response,@RequestBody Map map) throws IOException {
        String name = (String) map.get("name");
        int pageSize = (int) map.get("pageSize");
        if(name.equals("")){
            return findUserAll(request,pageSize,1);
        }
        UserDaoImpl usertest=new UserDaoImpl();
        PageInfo<User> list=usertest.searchUserByName(name,pageSize);
        Map<String,Object> result = new HashMap<>();
        result.put("total",list.getTotal());
        result.put("list",list.getList());
        return ModelResult.newSuccess(result);
    }

    @RequestMapping("/searchUser")
    @ResponseBody
    public ModelResult searchUser(HttpServletRequest request, HttpServletResponse response,@RequestBody Map map) throws IOException {
        UserDaoImpl usertest=new UserDaoImpl();
        PageInfo<User> list=usertest.searchUser(map);
        Map<String,Object> result = new HashMap<>();
        result.put("total",list.getTotal());
        result.put("list",list.getList());
        return ModelResult.newSuccess(result);
    }

    @RequestMapping("/deleteUser")
    @ResponseBody
    public ModelResult deleteUser(HttpServletRequest request, HttpServletResponse response,@RequestBody Map map) throws IOException {
        int id = (int) map.get("id");
        UserDaoImpl usertest=new UserDaoImpl();
        usertest.deleteUserRole(id);
        usertest.deleteUser(id);
        return ModelResult.newSuccess(true);
    }

    @RequestMapping("/batchDeleteUser")
    @ResponseBody
    public ModelResult batchDeleteUser(HttpServletRequest request, HttpServletResponse response,@RequestBody Map map) throws IOException {
        List list = (List) map.get("ids");
        UserDaoImpl usertest=new UserDaoImpl();
        usertest.batchDeleteUserRole(list);
        usertest.batchDeleteUser(list);
        return ModelResult.newSuccess(true);
    }

    @RequestMapping("/editUser")
    @ResponseBody
    public ModelResult editUser(HttpServletRequest request, HttpServletResponse response,@RequestBody Map map) throws IOException {
        int id = (int) map.get("id");
        String nickname = (String) map.get("nickname");
        List list = (List) map.get("roleIds");
        UserDaoImpl usertest=new UserDaoImpl();
        usertest.editUser(id,nickname);
        if(list!=null&&list.size()>0){
            usertest.deleteUserRole(id);
            List<Map<String,Object>> list1 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String,Object> map1=new HashMap<>();
                map1.put("userId",id);
                map1.put("roleId",list.get(i));
                list1.add(map1);
            }
            usertest.createUserRole(list1);
        }
        return ModelResult.newSuccess(true);
    }
}
