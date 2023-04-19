package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.Pets;
import com.lxy.petcommunity.bean.PetsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Repository
public class PetsDao {
    @Autowired
    JdbcTemplate template;

    // 管理员端
    // 返回所有宠物条目的数量
    public int getAllCount() {
        int count = template.queryForObject("select count(*) from pets", Integer.class);
        return count;
    }

    // 根据宠物名称返回查找到的数据条目数量，用于前端分页
    public int getCountByName(String name) {
        int count = template.queryForObject("select count(*) from pets where `name` like '%"+name+"%'", Integer.class);
        return count;
    }

    // 分页返回获取所有宠物条目到管理员端
    public List<Pets> getAllPets(int page, int limit) {
        List<Pets> list = template.query("select * from pets limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(Pets.class));
        if (!list.isEmpty()){
            for (Pets pets:list){
                List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = "+pets.getPet_type_id()  ,
                        new BeanPropertyRowMapper(PetsType.class));
                if(!petsTypes.isEmpty()){
                    pets.setPet_type(petsTypes.get(0));
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 管理员手动添加宠物信息条目
    public int addPets(Pets pets) {
        return template.update("insert into pets values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,null)",
                pets.getPet_type_id(), pets.getName(), pets.getEnglish_name(), pets.getCharacter(),
                pets.getHometown(), pets.getIllness(), pets.getLifetime(), pets.getCommon_price(),
                pets.getImage(), pets.getImage(), pets.getIntroduce(), pets.getAppearance(),
                pets.getCharacter_detail(), pets.getFeed_knowledge());
    }

    // 更新宠物信息条目，需要将原来的主图片从服务器删除
    public int updatePets(Pets pets) {
        // 获取旧的主图片路径
        String old_image = template.queryForObject("select image from pets where pet_id = " + pets.getPet_id(), String.class);
        // 删除之前上传的旧的主图片
        old_image = old_image.replace("http://localhost:8081/", "src/main/resources/static/");
        File file = new File(old_image);
        file.delete();
        return template.update("update pets set pet_type_id = ?, `name` = ?, English_name = ?, " +
                        "`character` = ?, hometown = ?, illness = ?, lifetime = ?, common_price = ?, " +
                        "image = ?, introduce = ?, appearance = ?, character_detail = ?, feed_knowledge = ? " +
                        "where pet_id = ?",
                pets.getPet_type_id(), pets.getName(), pets.getEnglish_name(), pets.getCharacter(),
                pets.getHometown(), pets.getIllness(), pets.getLifetime(), pets.getCommon_price(),
                pets.getImage(), pets.getIntroduce(), pets.getAppearance(), pets.getCharacter_detail(),
                pets.getFeed_knowledge(), pets.getPet_id());
    }

    // 更新轮播图，需要将原来的轮播图从服务器上删除
    public int updatePetsSwiperImages(int pet_id, String swiper_images) {
        // 获取之前的轮播图路径
        String old_swiper_images = template.queryForObject("select swiper_images from pets where pet_id = "+ pet_id, String.class);
        String[] old_Swiper_List = old_swiper_images.split(",");
        // 删除之前上传的旧的轮播图
        for(String s:old_Swiper_List){
            s = s.replace("http://localhost:8081/", "src/main/resources/static/");
            File file = new File(s);
            file.delete();
        }
        // 更新轮播图路径
        return template.update("update pets set `swiper_images` = ? where pet_id = ?", swiper_images, pet_id);
    }

    // 删除宠物信息条目
    public int delPets(int pet_id) {
        // 获取主图片路径
        String old_image = template.queryForObject("select image from pets where pet_id = " + pet_id, String.class);
        // 删除上传的主图片
        old_image = old_image.replace("http://localhost:8081/", "src/main/resources/static/");
        File file = new File(old_image);
        file.delete();
        // 获取轮播图路径
        String old_swiper_images = template.queryForObject("select swiper_images from pets where pet_id = "+ pet_id, String.class);
        String[] old_Swiper_List = old_swiper_images.split(",");
        // 删除上传的轮播图
        for(String s:old_Swiper_List){
            s = s.replace("http://localhost:8081/", "src/main/resources/static/");
            File file1 = new File(s);
            file1.delete();
        }
        return template.update("DELETE from pets where pet_id=?",pet_id);
    }

    // 根据关键字返回宠物信息条目到管理员端
    public List<Pets> findPets(int page, int limit, String name) {
        List<Pets> list = template.query("select * from pets where `name` like '%"+name+"%' limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(Pets.class));
        if (!list.isEmpty()){
            for (Pets pets:list){
                List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + pets.getPet_type_id(),
                        new BeanPropertyRowMapper(PetsType.class));
                if(!petsTypes.isEmpty()){
                    pets.setPet_type(petsTypes.get(0));
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 用户端
    // 返回所有宠物信息条目
    public List getAllPets() {
        List<Pets> list = template.query("select * from pets", new BeanPropertyRowMapper(Pets.class));
        if (!list.isEmpty()){
            for (Pets pets:list){
                List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + pets.getPet_type_id(),
                        new BeanPropertyRowMapper(PetsType.class));
                if(!petsTypes.isEmpty()){
                    pets.setPet_type(petsTypes.get(0));
                }
                String swiper_images = pets.getSwiper_images();
                String[] Swiper_List = swiper_images.split(",");
                pets.setSwiper_List(Arrays.asList(Swiper_List));
            }
            return list;
        }else{
            return null;
        }
    }

    // 根据id返回宠物信息条目
    public Pets getPetsById(int pet_id) {
        List<Pets> list = template.query("select * from pets where pet_id = ?" ,new Object[]{pet_id},
                new BeanPropertyRowMapper(Pets.class));
        if (!list.isEmpty()){
            for (Pets pets:list){
                List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + pets.getPet_type_id(),
                        new BeanPropertyRowMapper(PetsType.class));
                if(!petsTypes.isEmpty()){
                    pets.setPet_type(petsTypes.get(0));
                }
                String swiper_images = pets.getSwiper_images();
                String[] Swiper_List = swiper_images.split(",");
                pets.setSwiper_List(Arrays.asList(Swiper_List));
            }
            return list.get(0);
        }else{
            return null;
        }
    }
}
