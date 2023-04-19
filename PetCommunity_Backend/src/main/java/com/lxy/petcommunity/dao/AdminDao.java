package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminDao {
    @Autowired
    JdbcTemplate template;

    // 查找管理员，用于登录验证
    public Admin findAdmin(String username, String password) {
        List<Admin> list = template.query("select * from admin where username = ? && password = ?" ,
                new Object[]{username,password}, new BeanPropertyRowMapper(Admin.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    // 更新
    public int updateAdmin(int id, Admin admin) {
        return template.update("update admin set username = ?, password = ?, email = ?, phone = ?, sex = ? where id = ?",
                admin.getUsername(), admin.getPassword(), admin.getEmail(), admin.getPhone(), admin.getSex(), id);
    }

    // 改密码
    public int updatePassword(int id, String password) {
        return template.update("update admin set password = ? where id = ?", password, id);
    }
}
