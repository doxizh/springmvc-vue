package example.filter;

import com.alibaba.fastjson.JSON;
import example.controller.CheckIsLogin;
import example.tools.ModelResult;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class userFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Boolean isLogin=new CheckIsLogin(httpServletRequest.getSession()).CheckIsLogin();
        String url=httpServletRequest.getRequestURL().toString();
        System.out.println(url);
        List<String> list=new ArrayList<>();
        list.add("login");
        list.add("register");
        Boolean flag=false;
        for (int i = 0; i <list.size() ; i++) {
            if(url.endsWith(list.get(i))){
                flag=true;
                break;
            }
        }
        if (!flag){
            if(!isLogin){
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("text/html; charset=utf-8");
                PrintWriter writer = httpServletResponse.getWriter();
                writer.append(JSON.toJSONString(ModelResult.newError("401","未登录",false)));
                writer.close();
                return false;
            }else {
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
