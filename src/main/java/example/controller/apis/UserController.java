package example.controller.apis;

import example.controller.CheckIsLogin;
import example.dao.impl.UserDaoImpl;
import example.pojo.User;
import example.tools.ModelResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
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
        Boolean isLogin=new CheckIsLogin(request.getSession()).CheckIsLogin();
        if(!isLogin){
            return ModelResult.newError("401",false);
        }
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
    public ModelResult findUserAll(HttpServletRequest request) throws IOException {
        Boolean isLogin=new CheckIsLogin(request.getSession()).CheckIsLogin();
        if(!isLogin){
            return ModelResult.newError("401",false);
        }
        UserDaoImpl usertest=new UserDaoImpl();
        List<User> list=usertest.findUserAll();
        return ModelResult.newSuccess(list);
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
    public ModelResult login(HttpServletRequest request, HttpServletResponse response,@RequestBody User userInfo) throws IOException {
        String username=userInfo.getName();
        String password=userInfo.getPassword();
        UserDaoImpl usertest=new UserDaoImpl();
        User user=usertest.findUserByName(username);
        if(user!=null){
            if(user.getName().equals(username)&&user.getPassword().equals(password)){
                HttpSession session=request.getSession();
                session.setAttribute("username", username);
                Map<String,Object> map=new HashMap<>();
                Map<String,Object> userData=new HashMap<>();
                userData.put("name",user.getName());
                map.put("userData",userData);
                return ModelResult.newSuccess(map);
            }else {
                return ModelResult.newError("401","用户名或密码不正确",false);
            }
        }else {
            return ModelResult.newError("401","用户名不存在",false);
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
    public ModelResult register(HttpServletRequest request, HttpServletResponse response,@RequestBody User userInfo) throws IOException {
        String username=userInfo.getName();
        UserDaoImpl usertest=new UserDaoImpl();
        int num=usertest.register(userInfo);
        if(num>=0){
            HttpSession session=request.getSession();
            session.setAttribute("username", username);
            Map<String,Object> map=new HashMap<>();
            Map<String,Object> userData=new HashMap<>();
            userData.put("name",username);
            map.put("userData",userData);
            return ModelResult.newSuccess(map);
        }else {
            return ModelResult.newError("401","用户名不存在",false);
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
}