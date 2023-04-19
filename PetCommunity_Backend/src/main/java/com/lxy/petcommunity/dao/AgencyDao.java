package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.Agency;
import com.lxy.petcommunity.bean.Agency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AgencyDao {
    @Autowired
    JdbcTemplate template;

    // 管理员端
    // 管理员端获得所有宠物救助机构的数量
    public int getAllAgenciesCount() {
        int count = template.queryForObject("select count(*) from `agency`", Integer.class);
        return count;
    }

    // 管理员端获得名称含有关键字的宠物救助机构的数量
    public int getAgenciesCountByName(String agency_name) {
        int count = template.queryForObject("select count(*) from `agency` where agency_name like '%" + agency_name + "%'", Integer.class);
        return count;
    }

    // 管理员端获得地址含有关键字的宠物救助机构的数量
    public int getAgenciesCountByArea(String area) {
        int count = template.queryForObject("select count(*) from `agency` where total_address like '%" + area + "%'", Integer.class);
        return count;
    }

    // 管理员端获得所有宠物救助机构，分页返回
    public List<Agency> getAllAgencies(int page, int limit) {
        List<Agency> list = template.query("select * from `agency` limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(Agency.class));
        if (!list.isEmpty()){
            return list;
        }else{
            return null;
        }
    }

    // 管理员端获得名称含有关键字的宠物救助机构，分页返回
    public List<Agency> getAgenciesByName(int page, int limit, String agency_name) {
        List<Agency> list = template.query("select * from `agency` where agency_name like '%" + agency_name + "%' limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(Agency.class));
        if (!list.isEmpty()){
            return list;
        }else{
            return null;
        }
    }

    // 管理员端获得地址含有关键字的宠物救助机构，分页返回
    public List<Agency> getAgenciesByArea(int page, int limit, String area) {
        List<Agency> list = template.query("select * from `agency` where total_address like '%" + area + "%' limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(Agency.class));
        if (!list.isEmpty()){
            return list;
        }else{
            return null;
        }
    }

    // 宠物救助机构端
    // 查找机构，用于登录验证
    public Agency findAgency(String agency_name, String password) {
        List<Agency> list = template.query("select * from agency where agency_name = ? && password = ?" ,
                new Object[]{agency_name,password}, new BeanPropertyRowMapper(Agency.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    // 更新信息
    public int updateAgency(int agency_id, Agency agency) {
        return template.update("update agency set agency_name = ?, province = ?, city = ?, email = ?, phone = ? where agency_id = ?",
                agency.getAgency_name(), agency.getProvince(), agency.getCity(), agency.getEmail(), agency.getPhone(), agency_id);
    }

    // 改密码
    public int updatePassword(int agency_id, String password) {
        return template.update("update agency set password = ? where agency_id = ?", password, agency_id);
    }

    // 小程序用户端
    // 按照城市关键字检索机构，用于用户发布救助请求时的检查
    public List findAgencyByCity(String city){
        List<Agency> list = template.query("select * from agency where city like '%" + city + "%'" ,
                new BeanPropertyRowMapper(Agency.class));
        if (list!=null && list.size()>0){
            return list;
        }else{
            return null;
        }
    }

    // 用于根据地址或者机构名字关键字检索机构发布的领养需求
    public List findAgencyByKeyword(String Keyword){
        List<Agency> list = template.query("select * from agency where total_address like '%" + Keyword + "%' " +
                        "or agency_name like '%" + Keyword + "%'" ,
                new BeanPropertyRowMapper(Agency.class));
        if (list!=null && list.size()>0){
            return list;
        }else{
            return null;
        }
    }
}
