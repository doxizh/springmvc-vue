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
        RoleDaoImpl Roletest=new RoleDaoImpl();
        Role Role=Roletest.findRoleById(1);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id",Role.getId());
        map.put("name",Role.getRoleName());
        return ModelResult.newSuccess(map);
    }
    @RequestMapping("/findRoleAll")
    @ResponseBody
    public ModelResult findRoleAll(HttpServletRequest request,int pageSize,int pageNum) throws IOException {
        RoleDaoImpl Roletest=new RoleDaoImpl();
        PageInfo<Role> list=Roletest.findRoleAll(pageNum,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("total",list.getTotal());
        map.put("list",list.getList());
        return ModelResult.newSuccess(map);
    }

    @RequestMapping("/addRole")
    @ResponseBody
    public ModelResult addRole(HttpServletRequest request, HttpServletResponse response,@RequestBody Map map) throws IOException {
        String roleName= (String) map.get("roleName");
        RoleDaoImpl roleDaoImpl=new RoleDaoImpl();
        Role Role=roleDaoImpl.findRoleByName(roleName);
        if(Role!=null){
            return ModelResult.newError("403","角色名已存在",false);
        }else {
            Role Role1=new Role();
            Role1.setRoleName(roleName);
            int num=roleDaoImpl.addRole(Role1);
            if(num>=0){
                return ModelResult.newSuccess(true);
            }else {
                return ModelResult.newError("新增失败");
            }
        }
    }

    @RequestMapping("/searchRole")
    @ResponseBody
    public ModelResult searchRole(HttpServletRequest request, HttpServletResponse response,@RequestBody Map map) throws IOException {
        RoleDaoImpl Roletest=new RoleDaoImpl();
        PageInfo<Role> list=Roletest.searchRole(map);
        Map<String,Object> result = new HashMap<>();
        result.put("total",list.getTotal());
        result.put("list",list.getList());
        return ModelResult.newSuccess(result);
    }

    @RequestMapping("/deleteRole")
    @ResponseBody
    public ModelResult deleteRole(HttpServletRequest request, HttpServletResponse response,@RequestBody Map map) throws IOException {
        int id = (int) map.get("id");
        RoleDaoImpl Roletest=new RoleDaoImpl();
        int num=Roletest.deleteRole(id);
        return num>=0?ModelResult.newSuccess(true):ModelResult.newError("删除失败");
    }

    @RequestMapping("/batchDeleteRole")
    @ResponseBody
    public ModelResult batchDeleteRole(HttpServletRequest request, HttpServletResponse response,@RequestBody Map map) throws IOException {
        List list = (List) map.get("ids");
        RoleDaoImpl Roletest=new RoleDaoImpl();
        int num=Roletest.batchDeleteRole(list);
        return num>=0?ModelResult.newSuccess(true):ModelResult.newError("删除失败");
    }

    @RequestMapping("/editRole")
    @ResponseBody
    public ModelResult editRole(HttpServletRequest request, HttpServletResponse response,@RequestBody Map map) throws IOException {
        int id = (int) map.get("id");
        String roleName = (String) map.get("roleName");
        RoleDaoImpl Roletest=new RoleDaoImpl();
        int num=Roletest.editRole(id,roleName);
        return num>=0?ModelResult.newSuccess(true):ModelResult.newError("删除失败");
    }
}
