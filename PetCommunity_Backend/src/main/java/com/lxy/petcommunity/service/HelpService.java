package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Help;
import com.lxy.petcommunity.dao.HelpDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class HelpService {
    @Autowired
    HelpDao dao;

    // 管理员端
    // 返回所有求助请求数量
    public int getAllHelpsCount() {
        return dao.getAllHelpsCount();
    }

    // 返回含有查找关键字的求助请求数量
    public int getHelpsCountByContent(String help_content) {
        return dao.getHelpsCountByContent(help_content);
    }

    // 管理员端获得所有求助请求，分页取数据
    public List<Help> getAllHelps(int page, int limit) {
        return dao.getAllHelps(page, limit);
    }

    // 根据关键字查找求助请求返回到管理员端
    public List<Help> findHelpByContent(int page, int limit, String help_content) {
        return dao.findHelpByContent(page, limit, help_content);
    }

    // 删除求助请求，需要删除文件夹的图片，管理员端和用户端都使用该函数进行删除
    public int delHelp(int help_id) {
        return dao.delHelp(help_id);
    }


    // 宠物保护机构端
    // 返回指定状态的所有求助请求数量到宠物保护机构端
    public int getAllHelpsCountByAgencyIdAndStatus(int agency_id, int status) {
        return dao.getAllHelpsCountByAgencyIdAndStatus(agency_id, status);
    }

    // 宠物保护机构端获得所有求助请求，分页取数据
    public List<Help> getAllHelpsByAgencyIdAndStatus(int agency_id, int status, int page, int limit) {
        return dao.getAllHelpsByAgencyIdAndStatus(agency_id, status, page, limit);
    }

    // 根据单号查找求助请求返回到宠物保护机构端
    public List<Help> findHelpByHelpId(int page, int limit, int help_id, int agency_id, int status) {
        return dao.findHelpByHelpId(page, limit, help_id, agency_id, status);
    }

    // 处理请求
    public int processHelp(int help_id, String staff_name, String staff_phone){
        return dao.processHelp(help_id, staff_name, staff_phone);
    }

    // 处理结束
    public int finishHelp(int help_id){
        return dao.finishHelp(help_id);
    }


    // 用户端
    // 获取用户发布的指定状态的求助请求
    public List<Help> getPublishHelpsByUserOpenIdAndStatus(String user_openid, int status, int limit, int page) {
        return dao.getPublishHelpsByUserOpenIdAndStatus(user_openid, status, limit, page);
    }

    // 用户发布求助请求
    public boolean publishHelp(String user_openid, String contact_name, String contact_phone, String province, String city, String detail_address, String help_content, String help_images){
        return dao.publishHelp(user_openid, contact_name, contact_phone, province, city, detail_address, help_content, help_images);
    }

    // 用户取消请求
    public int cancelHelp(String user_openid, int help_id){
        return dao.cancelHelp(user_openid, help_id);
    }
}
