package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.PetsType;
import com.lxy.petcommunity.dao.PetsTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetsTypeService {
    @Autowired
    PetsTypeDao dao;
    public int getCount() {
        return dao.getCount();
    }

    public int getCount(String pet_type_name) {
        return dao.getCount(pet_type_name);
    }

    public List<PetsType> getAllPetsTypes_limit(int page, int limit) {
        return dao.getAllPetsTypes_limit(page,limit);
    }

    public List<PetsType> findPetsType(int page, int limit, String pet_type_name) {
        return dao.findPetsType(page,limit,pet_type_name);
    }

    public int addPetsType(PetsType petsType) {
        return dao.addPetsType(petsType);
    }

    public int updatePetsType(PetsType petsType) {
        return dao.updatePetsType(petsType);
    }

    public int delPetsType(int pet_type_id) {
        return dao.delPetsType(pet_type_id);
    }

    // 用户端
    public List<PetsType> getAllPetsTypes() {
        return dao.getAllPetsTypes();
    }
}
