package example.controller;

import example.dao.impl.UserDaoImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/home")
public class IndexController {
    @RequestMapping("/index")
    public String index(){
        return "index";
    }
    @RequestMapping("/findUserByIdTest")
    public String findUserByIdTest() throws IOException {
        UserDaoImpl usertest=new UserDaoImpl();
        usertest.findUserByIdTest(1);
        return "index";
    }
}
