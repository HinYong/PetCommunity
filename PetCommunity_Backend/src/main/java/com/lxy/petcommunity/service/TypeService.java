package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Type;
import com.lxy.petcommunity.dao.TypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {
    @Autowired
    TypeDao dao;

    // 管理员端
    // 获取所有商品类型的数量
    public int getCount() {
        return dao.getCount();
    }

    // 根据关键字返回商品类型的数量
    public int getCount(String name) {
        return dao.getCount(name);
    }

    // 将商品类型分页返回到管理员端
    public List<Type> getAllTypes_limit(int page, int limit) {
        return dao.getAllTypes_limit(page,limit);
    }

    // 返回所有商品类型，既用于管理员端添加商品的下拉菜单，也用于用户端的业务
    public List<Type> getAllTypes() {
        return dao.getAllTypes();
    }

    // 根据关键字查找类型并且分页返回到管理员端
    public List<Type> findType(int page, int limit, String name) {
        return dao.findType(page,limit,name);
    }

    // 管理员端添加新的商品类型
    public int addType(Type type) {
        return dao.addType(type);
    }

    // 管理员端编辑商品类型
    public int updateType(Type type) {
        return dao.updateType(type);
    }

    // 管理员端删除商品类型
    public int delType(int id) {
        return dao.delType(id);
    }
}
