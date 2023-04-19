package com.lxy.petcommunity.controller;

import com.lxy.petcommunity.bean.Admin;
import com.lxy.petcommunity.bean.ResBody;
import com.lxy.petcommunity.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
public class AdminController {
    @Autowired
    AdminService service;
    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    // 管理员登录
    @PostMapping("/admin/loginByPassword")
    public ResBody loginByPassword(@RequestBody Map<String, Object> params,
                                   HttpSession session) {
        ResBody resBody = new ResBody();
        String username = params.get("username").toString();
        String password = params.get("password").toString();
        Admin admin = service.findAdmin(username,password);
        if (admin == null){
            resBody.setCode(500);
            resBody.setMsg("登录失败，用户名或密码错误！");
        }else {
            session.setAttribute("admin",admin);
            LOG.info(admin.toString());
            resBody.setCode(200);
            resBody.setMsg("登录成功！");
        }
        return resBody;
    }

    // 管理员信息更新
    @PostMapping("/admin/updateAdmin")
    public ResBody updateAdmin(@RequestBody Map<String, Object> params,
                              HttpSession session) {
        ResBody resBody = new ResBody();
        String newname = params.get("name").toString();
        String newemail = params.get("email").toString();
        String newphone = params.get("phone").toString();
        Admin admin = (Admin) session.getAttribute("admin");
        admin.setUsername(newname);
        admin.setEmail(newemail);
        admin.setPhone(newphone);
        int i = service.updateAdmin(admin.getId(),admin);
        if (i != 1){
            resBody.setCode(500);
            resBody.setMsg("更新失败，后台出错");
        }else {
            session.setAttribute("admin",admin);
            LOG.info(admin.toString());
            resBody.setCode(200);
            resBody.setMsg("更新成功");
        }
        return resBody;
    }

    // 管理员改密码
    @PostMapping("/admin/updatePassword")
    public ResBody updatePass(@RequestBody Map<String, Object> params,
                              HttpSession session) {
        ResBody resBody = new ResBody();
        String oldPsw = params.get("oldPassword").toString();
        String newPsw1 = params.get("newPassword1").toString();
        String newPsw2 = params.get("newPassword2").toString();
        Admin admin = (Admin) session.getAttribute("admin");
        if (!newPsw1.equals(newPsw2)){
            resBody.setCode(500);
            resBody.setMsg("修改失败，两次输入的密码不一致");
        }
        else if (!oldPsw.equals(admin.getPassword())){
            resBody.setCode(500);
            resBody.setMsg("修改失败，旧密码错误");
        }
        else {
            admin.setPassword(newPsw1);
            int i = service.updatePassword(admin.getId(), newPsw1);
            if (i != 1){
                resBody.setCode(500);
                resBody.setMsg("修改失败，后台出错");
            }else {
                session.setAttribute("admin",admin);
                LOG.info(admin.toString());
                resBody.setCode(200);
                resBody.setMsg("修改密码成功");
            }
        }
        return resBody;
    }
}
