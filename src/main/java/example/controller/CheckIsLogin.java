package example.controller;
import javax.servlet.http.HttpSession;

public class CheckIsLogin {
    private HttpSession session1;
    public CheckIsLogin(HttpSession session) {
        session1=session;
    }

    public Boolean CheckIsLogin(){
        Object username= session1.getAttribute("username");
        return username!=null;
    }
}
