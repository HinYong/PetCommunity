package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.Goods;
import com.lxy.petcommunity.bean.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.*;

@Repository
public class GoodsDao {
    @Autowired
    JdbcTemplate template;

    // 管理员端

    // 返回所有商品的数量
    public int getAllCount() {
        int count = template.queryForObject("select count(*) from goods", Integer.class);
        return count;
    }

    // 根据商品状态返回查找到的数据条目数量，用于前端分页
    public int getCountByStatus(int status) {
        int count = template.queryForObject("select count(*) from goods where status = " + status, Integer.class);
        return count;
    }

    // 根据商品状态和名称返回查找到的数据条目数量，用于前端分页
    public int getCountByStatusAndName(String name, int status) {
        int count = template.queryForObject("select count(*) from goods where status = " + status + " and name like '%"+name+"%'", Integer.class);
        return count;
    }

    // 获取所有商品，无论是否上架或者下架
    public List<Goods> getAllGoods(int page, int limit) {
        List<Goods> list = template.query("select * from goods limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(Goods.class));
        if (!list.isEmpty()){
            for (Goods good:list){
                List<Type> types = template.query("select * from `type` where id = "+good.getType_id()  ,
                        new BeanPropertyRowMapper(Type.class));
                if(!types.isEmpty()){
                    good.setType(types.get(0));
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 获取上架商品
    public List<Goods> getSellingGoods(int page, int limit) {
        List<Goods> list = template.query("select * from goods where status = 1 limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(Goods.class));
        if (!list.isEmpty()){
            for (Goods good:list){
                List<Type> types = template.query("select * from `type` where id = "+good.getType_id()  ,
                        new BeanPropertyRowMapper(Type.class));
                if(!types.isEmpty()){
                    good.setType(types.get(0));
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 获取下架商品
    public List<Goods> getSoldoutGoods(int page, int limit) {
        List<Goods> list = template.query("select * from goods where status = 0 limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(Goods.class));
        if (!list.isEmpty()){
            for (Goods good:list){
                List<Type> types = template.query("select * from `type` where id = "+good.getType_id()  ,
                        new BeanPropertyRowMapper(Type.class));
                if(!types.isEmpty()){
                    good.setType(types.get(0));
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 添加商品
    public int addGoods(Goods goods) {
        return template.update("insert into goods values(null,?,?,?,?,1,?,null)",
                goods.getImage(),goods.getName(),goods.getPrice(),goods.getType_id(),goods.getImage());
    }

    // 编辑商品，需要将原来的主图片从服务器删除
    public int updateGoods(Goods goods) {
        // 获取旧的主图片路径
        String old_image = template.queryForObject("select image from goods where _id = " + goods.get_id(), String.class);
        // 删除之前上传的旧的主图片
        old_image = old_image.replace("http://localhost:8081/", "src/main/resources/static/");
        File file = new File(old_image);
        file.delete();
        return template.update("update goods set `image` = ? ,`name` = ? ,`price` = ? ,`type_id` = ?  where _id = ?",
                goods.getImage(),goods.getName(),goods.getPrice(),goods.getType_id(),goods.get_id());
    }

    // 更新轮播图，需要将原来的轮播图从服务器删除
    public int updateSwiperImages(int _id, String swiper_images) {
        // 获取之前的轮播图路径
        String old_swiper_images = template.queryForObject("select swiper_images from goods where _id = "+ _id, String.class);
        String[] old_Swiper_List = old_swiper_images.split(",");
        // 删除之前上传的旧的轮播图
        for(String s:old_Swiper_List){
            s = s.replace("http://localhost:8081/", "src/main/resources/static/");
            File file = new File(s);
            file.delete();
        }
        // 更新轮播图路径
        return template.update("update goods set `swiper_images` = ? where _id = ?", swiper_images, _id);
    }

    // 上架商品
    public int sellGoods(int id){
        return template.update("update goods set status = 1 where _id=?",id);
    }

    // 下架商品
    public int soldoutGoods(int id){
        return template.update("update goods set status = 0 where _id=?",id);
    }

    // 删除商品
    public int delGoods(int id) {
        // 获取主图片路径
        String old_image = template.queryForObject("select image from goods where _id = " + id, String.class);
        // 删除之前上传的主图片
        old_image = old_image.replace("http://localhost:8081/", "src/main/resources/static/");
        File file = new File(old_image);
        file.delete();
        // 获取轮播图路径
        String old_swiper_images = template.queryForObject("select swiper_images from goods where _id = "+ id, String.class);
        String[] old_Swiper_List = old_swiper_images.split(",");
        // 删除之前上传的轮播图
        for(String s:old_Swiper_List){
            s = s.replace("http://localhost:8081/", "src/main/resources/static/");
            File file1 = new File(s);
            file1.delete();
        }
        return template.update("DELETE from goods where _id=?",id);
    }

    // 根据关键字和商品状态将查找的商品返回到管理员端
    public List<Goods> findGoods(int page, int limit, String name, int status) {
        List<Goods> list = template.query("select * from goods where status = " + status + " and `name` like '%"+name+"%' limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(Goods.class));
        if (!list.isEmpty()){
            for (Goods good:list){
                List<Type> types = template.query("select * from `type` where id = "+good.getType_id()  ,
                        new BeanPropertyRowMapper(Type.class));
                if(!types.isEmpty()){
                    good.setType(types.get(0));
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 用户端
    // 获取所有商品
    public List getAllGoods() {
        List<Goods> list = template.query("select * from goods where status = 1"  ,
                new BeanPropertyRowMapper(Goods.class));
        if (!list.isEmpty()){
            for (Goods good:list){
                List<Type> types = template.query("select * from `type` where id = "+good.getType_id()  ,
                        new BeanPropertyRowMapper(Type.class));
                if(!types.isEmpty()){
                    good.setType(types.get(0));
                }
                String swiper_images = good.getSwiper_images();
                String[] Swiper_List = swiper_images.split(",");
                good.setSwiper_List(Arrays.asList(Swiper_List));
            }
            return list;
        }else{
            return null;
        }
    }

    // 根据id返回商品对象到用户端
    public Goods getGoodsById(int id) {
        List<Goods> list = template.query("select * from goods where _id = ?" ,new Object[]{id},
                new BeanPropertyRowMapper(Goods.class));
        if (!list.isEmpty()){
            Goods good =  list.get(0);
            List<Type> types = template.query("select * from `type` where id = "+good.getType_id()  ,
                    new BeanPropertyRowMapper(Type.class));
            if(!types.isEmpty()){
                good.setType(types.get(0));
            }
            String swiper_images = good.getSwiper_images();
            String[] Swiper_List = swiper_images.split(",");
            good.setSwiper_List(Arrays.asList(Swiper_List));
            return good;
        }else{
            return null;
        }
    }

    // 获取商品详情页下方推荐列表
    public List<Goods> getRecomList(int id) {
        List<Goods> recomList = new ArrayList<>();
        List<Goods> goodsList = template.query("select * from goods where _id = ?" ,new Object[]{id},
                new BeanPropertyRowMapper(Goods.class));
        Goods goods = goodsList.get(0);
        List<Goods> list = template.query("select * from goods where _id != ? and type_id = ?" ,new Object[]{id, goods.getType_id()},
                new BeanPropertyRowMapper(Goods.class));
        Random r = new Random();
        int index = r.nextInt(list.size()-5);
        for(int i=index;i<index+6;i++){
            recomList.add(list.get(i));
        }
        return recomList;
    }
}
