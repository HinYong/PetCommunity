package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Pets;
import com.lxy.petcommunity.dao.PetsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetsService {
    @Autowired
    PetsDao dao;

    // 管理员端
    // 返回所有宠物条目的数量
    public int getAllCount() {
        return dao.getAllCount();
    }

    // 根据宠物名称返回查找到的数据条目数量，用于前端分页
    public int getCountByName(String name) {
        return dao.getCountByName(name);
    }

    // 根据关键字返回宠物信息条目到管理员端
    public List<Pets> findPets(int page, int limit, String name) {
        return dao.findPets(page,limit,name);
    }

    // 分页返回获取所有宠物条目到管理员端
    public List<Pets> getAllPets(int page, int limit) {
        return dao.getAllPets(page,limit);
    }

    // 管理员手动添加宠物信息条目
    public int addPets(Pets pets) {
        return dao.addPets(pets);
    }

    // 添加轮播图，需要将原来的轮播图从服务器上删除
    public int updatePetsSwiperImages(int pet_id, String swiper_images) {
        return dao.updatePetsSwiperImages(pet_id, swiper_images);
    }

    // 更新宠物信息条目
    public int updatePets(Pets pets) {
        return dao.updatePets(pets);
    }

    // 删除宠物信息条目
    public int delPets(int pet_id) {
        return dao.delPets(pet_id);
    }

    // 用户端
    // 返回所有宠物信息条目
    public List getAllPets() {
        return dao.getAllPets();
    }

    // 根据id返回宠物信息条目
    public Pets getPetsById(int pet_id) {
        return dao.getPetsById(pet_id);
    }

}
