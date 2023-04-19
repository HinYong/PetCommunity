package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Adopt;
import com.lxy.petcommunity.dao.AdoptDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptService {
    @Autowired
    AdoptDao dao;

    // 管理员端
    // 返回所有领养需求数量
    public int getAllAdoptsCount() {
        return dao.getAllAdoptsCount();
    }

    // 按照机构名称查找领养需求数量
    public int getAdoptsCountByAgencyName(String agency_name) {
        return dao.getAdoptsCountByAgencyName(agency_name);
    }

    // 按照关键字查找领养需求数量
    public int getAdoptsCountByKeyword(String Keyword) {
        return dao.getAdoptsCountByKeyword(Keyword);
    }

    // 管理员端获得所有领养需求，分页取数据
    public List<Adopt> getAllAdopts(int page, int limit) {
        return dao.getAllAdopts(page, limit);
    }

    // 根据机构名称查找领养需求返回到管理员端，分页取数据
    public List<Adopt> findAdoptsByAgencyName(int page, int limit, String agency_name) {
        return dao.findAdoptsByAgencyName(page, limit, agency_name);
    }

    // 根据关键字查找领养需求返回到管理员端，分页取数据
    public List<Adopt> findAdoptsByKeyword(int page, int limit, String Keyword) {
        return dao.findAdoptsByKeyword(page, limit, Keyword);
    }

    // 管理员端和宠物救助机构端删除领养需求，需要删除文件夹的图片
    public int delAdopt(int adopt_id) {
        return dao.delAdopt(adopt_id);
    }


    // 宠物保护机构端
    // 返回指定状态的所有领养需求数量到宠物保护机构端
    public int getAllAdoptsCountByAgencyIdAndStatus(int agency_id, int status) {
        return dao.getAllAdoptsCountByAgencyIdAndStatus(agency_id, status);
    }

    // 宠物保护机构端获得指定状态的所有领养需求，分页取数据
    public List<Adopt> getAllAdoptsByAgencyIdAndStatus(int page, int limit, int agency_id, int status) {
        return dao.getAllAdoptsByAgencyIdAndStatus(page, limit, agency_id, status);
    }

    // 返回指定状态与含有关键字的领养需求数量到宠物保护机构端
    public int getAdoptsCountByKeywordAndStatus(String Keyword, int agency_id, int status) {
        return dao.getAdoptsCountByKeywordAndStatus(Keyword, agency_id, status);
    }

    // 宠物保护机构端获得指定状态与含有关键字的领养需求，分页取数据
    public List<Adopt> findAdoptsByKeywordAndStatus(int page, int limit, String Keyword, int agency_id, int status) {
        return dao.findAdoptsByKeywordAndStatus(page, limit, Keyword, agency_id, status);
    }

    // 根据领养号和状态查找领养需求返回到宠物保护机构端，分页取数据
    public List<Adopt> findAdoptByAdoptIdAndStatus(int page, int limit, int adopt_id, int agency_id, int status) {
        return dao.findAdoptByAdoptIdAndStatus(page, limit, adopt_id, agency_id, status);
    }

    // 发布领养需求
    public int publishAdopt(Adopt adopt) {
        return dao.publishAdopt(adopt);
    }

    // 更新领养需求
    public int updateAdopt(Adopt adopt) {
        return dao.updateAdopt(adopt);
    }

    // 宠物救助机构端上传图片，需要将原来的图片从服务器上删除
    public int updateAdoptImages(int adopt_id, String images) {
        return dao.updateAdoptImages(adopt_id, images);
    }

    // 用户端
    // 按宠物种类分页返回所有发布的领养需求
    public List getAllPublishAdoptsByPetType(int page, int limit, int pet_type_id, String searchContent) {
        return dao.getAllPublishAdoptsByPetType(page, limit, pet_type_id, searchContent);
    }

    // 根据领养号查找领养需求
    public Adopt findAdoptByAdoptId(int adopt_id) {
        return dao.findAdoptByAdoptId(adopt_id);
    }

}
