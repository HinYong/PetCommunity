package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TypeDao {
    @Autowired
    JdbcTemplate template;

    // 管理员端
    // 获取所有商品类型的数量
    public int getCount() {
        int count = template.queryForObject("select count(*) from `type`", Integer.class);
        return count;
    }

    // 根据关键字返回商品类型的数量
    public int getCount(String name) {
        int count = template.queryForObject("select count(*) from `type` where `name` like '%"+name+"%' ", Integer.class);
        return count;
    }

    // 将商品类型分页返回到管理员端
    public List<Type> getAllTypes_limit(int page, int limit) {
        List<Type> list = template.query("select * from `type` limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(Type.class));
        if (list!=null&&list.size()!=0){
            return list;
        }else{
            return null;
        }
    }

    // 返回所有商品类型，既用于管理员端添加商品的下拉菜单，也用于用户端的业务
    public List<Type> getAllTypes() {
        List<Type> list = template.query("select * from `type` "  ,
                new BeanPropertyRowMapper(Type.class));
        if (list!=null&&list.size()!=0){
            return list;
        }else{
            return null;
        }
    }

    // 根据关键字查找类型并且分页返回到管理员端
    public List<Type> findType(int page, int limit, String name) {
        List<Type> list = template.query("select * from `type` where `name` like '%"+name+"%' limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(Type.class));
        if (list!=null&&list.size()!=0){
            return list;
        }else{
            return null;
        }
    }

    // 管理员端添加新的商品类型
    public int addType(Type type) {
        return template.update("insert into `type` values(null,?)",
                type.getName());
    }

    // 管理员端编辑商品类型
    public int updateType(Type type) {
        return template.update("update `type` set `name` = ? where id = ?",
                type.getName(), type.getId());
    }

    // 管理员端删除商品类型
    public int delType(int id) {
        return template.update("DELETE from `type` where id=?",id);
    }
}
