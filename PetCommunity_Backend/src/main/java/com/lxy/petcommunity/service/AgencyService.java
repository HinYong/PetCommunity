package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Agency;
import com.lxy.petcommunity.dao.AgencyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyService {
    @Autowired
    AgencyDao dao;

    // 管理员端
    // 管理员端获得所有宠物救助机构的数量
    public int getAllAgenciesCount() {
        return dao.getAllAgenciesCount();
    }

    // 管理员端获得名称含有关键字的宠物救助机构的数量
    public int getAgenciesCountByName(String agency_name) {
        return dao.getAgenciesCountByName(agency_name);
    }

    // 管理员端获得地址含有关键字的宠物救助机构的数量
    public int getAgenciesCountByArea(String area) {
        return dao.getAgenciesCountByArea(area);
    }

    // 管理员端获得所有宠物救助机构，分页返回
    public List<Agency> getAllAgencies(int page, int limit) {
        return dao.getAllAgencies(page, limit);
    }

    // 管理员端获得名称含有关键字的宠物救助机构，分页返回
    public List<Agency> getAgenciesByName(int page, int limit, String agency_name) {
        return dao.getAgenciesByName(page, limit, agency_name);
    }

    // 管理员端获得地址含有关键字的宠物救助机构，分页返回
    public List<Agency> getAgenciesByArea(int page, int limit, String area) {
        return dao.getAgenciesByArea(page, limit, area);
    }

    // 宠物救助机构端
    // 查找宠物机构，用于登录验证
    public Agency findAgency(String agency_name, String password) {
        return dao.findAgency(agency_name,password);
    }

    // 宠物机构信息更新
    public int updateAgency(int agency_id, Agency agency) {
        return dao.updateAgency(agency_id,agency);
    }

    // 宠物机构密码更新
    public int updatePassword(int agency_id, String password){
        return dao.updatePassword(agency_id, password);
    }

    // 小程序用户端
    // 按照城市关键字检索机构
    public List findAgencyByCity(String city){
        return dao.findAgencyByCity(city);
    }

    // 按照关键字检索机构
    public List findAgencyByKeyword(String Keyword){
        return dao.findAgencyByKeyword(Keyword);
    }
}
