package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Admin;
import com.lxy.petcommunity.dao.AdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    AdminDao dao;

    // 查找管理员，用于登录验证
    public Admin findAdmin(String username, String password) {
        return dao.findAdmin(username,password);
    }

    // 管理员信息更新
    public int updateAdmin(int id, Admin admin) {
        return dao.updateAdmin(id,admin);
    }

    //管理员密码更新
    public int updatePassword(int id, String password){
        return dao.updatePassword(id, password);
    }
}
