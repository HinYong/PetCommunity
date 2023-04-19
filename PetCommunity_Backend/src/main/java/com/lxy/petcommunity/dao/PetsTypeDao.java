package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.PetsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PetsTypeDao {
    @Autowired
    JdbcTemplate template;

    public int getCount() {
        int count = template.queryForObject("select count(*) from pets_type", Integer.class);
        return count;
    }

    public int getCount(String pet_type_name) {
        int count = template.queryForObject("select count(*) from pets_type where pet_type_name like '%"+pet_type_name+"%' ", Integer.class);
        return count;
    }

    // 分页查询
    public List<PetsType> getAllPetsTypes_limit(int page, int limit) {
        List<PetsType> list = template.query("select * from pets_type limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(PetsType.class));
        if (list!=null&&list.size()!=0){
            return list;
        }else{
            return null;
        }
    }

    public List<PetsType> findPetsType(int page, int limit, String pet_type_name) {
        List<PetsType> list = template.query("select * from pets_type where pet_type_name like '%"+pet_type_name+"%' limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(PetsType.class));
        if (list!=null&&list.size()!=0){
            return list;
        }else{
            return null;
        }
    }

    public int addPetsType(PetsType petsType) {
        return template.update("insert into pets_type values(null,?)",
                petsType.getPet_type_name());
    }

    public int updatePetsType(PetsType petsType) {
        return template.update("update pets_type set `pet_type_name` = ? where pet_type_id = ?",
                petsType.getPet_type_name(), petsType.getPet_type_id());
    }

    public int delPetsType(int pet_type_id) {
        return template.update("DELETE from pets_type where pet_type_id=?",pet_type_id);
    }

    // 用户端
    // 返回所有宠物类型
    public List<PetsType> getAllPetsTypes() {
        List<PetsType> list = template.query("select * from pets_type "  ,
                new BeanPropertyRowMapper(PetsType.class));
        if (list!=null&&list.size()!=0){
            return list;
        }else{
            return null;
        }
    }

}
