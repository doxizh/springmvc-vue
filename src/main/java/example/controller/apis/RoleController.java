package example.controller.apis;

import com.github.pagehelper.PageInfo;
import example.dao.RoleDao;
import example.dao.impl.RoleDaoImpl;
import example.pojo.Role;
import example.tools.ModelResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/front")
public class RoleController {
    @RequestMapping("/findRoleById")
    @ResponseBody
    public ModelResult findRoleById(HttpServletRequest request) throws IOException {
        RoleDaoImpl Roletest = new RoleDaoImpl();
        Role Role = Roletest.findRoleById(1);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", Role.getId());
        map.put("name", Role.getRoleName());
        return ModelResult.newSuccess(map);
    }

    @RequestMapping("/findRoleAll")
    @ResponseBody
    public ModelResult findRoleAll(HttpServletRequest request, @RequestBody Map map) throws IOException {
        RoleDaoImpl roleDao = new RoleDaoImpl();
        PageInfo<Role> list = roleDao.findRoleAll(map);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("total", list.getTotal());
        map1.put("list", list.getList());
        return ModelResult.newSuccess(map1);
    }

    @RequestMapping("/addRole")
    @ResponseBody
    public ModelResult addRole(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map) throws IOException {
        String roleName = (String) map.get("roleName");
        RoleDaoImpl roleDao = new RoleDaoImpl();
        Role role = roleDao.findRoleByName(roleName);
        if (role != null) {
            return ModelResult.newError("403", "角色名已存在", false);
        } else {
            roleDao.addRole(map);
            return ModelResult.newSuccess(true);
        }
    }

    @RequestMapping("/searchRole")
    @ResponseBody
    public ModelResult searchRole(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map) throws IOException {
        RoleDaoImpl roleDao = new RoleDaoImpl();
        PageInfo<Role> list = roleDao.searchRole(map);
        Map<String, Object> result = new HashMap<>();
        result.put("total", list.getTotal());
        result.put("list", list.getList());
        return ModelResult.newSuccess(result);
    }

    @RequestMapping("/deleteRole")
    @ResponseBody
    public ModelResult deleteRole(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map) throws IOException {
        int id = (int) map.get("id");
        RoleDaoImpl roleDao = new RoleDaoImpl();
        roleDao.deleteUserRole(id);
        roleDao.deleteRole(id);
        return ModelResult.newSuccess(true);
    }

    @RequestMapping("/batchDeleteRole")
    @ResponseBody
    public ModelResult batchDeleteRole(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map) throws IOException {
        List list = (List) map.get("ids");
        RoleDaoImpl roleDao = new RoleDaoImpl();
        roleDao.batchDeleteRoleUser(list);
        roleDao.batchDeleteRole(list);
        return ModelResult.newSuccess(true);
    }

    @RequestMapping("/editRole")
    @ResponseBody
    public ModelResult editRole(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map) throws IOException {
        RoleDaoImpl roleDao = new RoleDaoImpl();
        roleDao.editRole(map);
        return ModelResult.newSuccess(true);
    }
}
