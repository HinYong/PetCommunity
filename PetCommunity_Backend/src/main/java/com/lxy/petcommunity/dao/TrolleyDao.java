package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.*;
import com.lxy.petcommunity.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class TrolleyDao {
    @Autowired
    JdbcTemplate template;

    // 获取购物车条目
    public List getTrolley(String openid) {
        List<Trolley> list = template.query("select * from `trolley` where user_openid = '"+openid+"'"  ,
                new BeanPropertyRowMapper(Trolley.class));
        if (list!=null&&list.size()!=0){
            Iterator<Trolley> iterator = list.iterator();
            while(iterator.hasNext()){
                Trolley trolley = iterator.next();
                List<User> users = template.query("select * from `user` where open_id = '"+ trolley.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                trolley.setUser(users.get(0));
                List<Goods> goods = template.query("select * from goods where _id ="+ trolley.getGoods_id()  ,
                        new BeanPropertyRowMapper(Goods.class));
                if(!goods.isEmpty()){
                    Goods good = goods.get(0);
                    // 若该条目对应的商品被下架了，则不会将该购物车条目发送到前端
                    if(good.getStatus()==0){
                        iterator.remove();
                    }
                    else {
                        trolley.setGoods(good);
                    }
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 用于购物车更新的业务中，先清空购物车后，将前端传过来的数据用addTrolley添加到数据库中
    public void delTrolley(String user_openid) {
        template.update("DELETE from `trolley` where user_openid=?",user_openid);
    }

    // 用于购物车更新的业务中，将前端传过来的商品条目添加到数据库中，每次添加一条
    public void addTrolley(String user_openid, Integer goods_id, Integer count) {
        template.update("insert into `trolley` values(null,?,?,?)",
                user_openid,goods_id,count);
    }

    // 用户向购物车添加商品，每次只添加一件
    public void addItemToTrolley(String user_openid, int goods_id) {
        int count = template.queryForObject("select count(*) from `trolley` where user_openid = ? and goods_id = ?",
                new Object[]{user_openid,goods_id},Integer.class);
        // 之前有该商品，则count+1
        if (count!=0){
            template.update("update `trolley` set `count`=`count`+1 where user_openid = ? and goods_id = ?",
                    user_openid,goods_id);
        }
        //之前没有该商品，则将count置为1
        else{
            template.update("insert into `trolley` values(null,?,?,?)",
                    user_openid,goods_id,1);
        }
    }

    // 在controller中循环遍历前端传过来的购物车列表，循环调用该函数对于提交的订单中的每一项进行购买
    public double buyFromTrolley(int order_id, String user_openid, Integer goods_id, Integer count) {
        List<Goods> goods = template.query("select * from goods where _id = " + goods_id, new BeanPropertyRowMapper(Goods.class));
        if(!goods.isEmpty()){
            Goods good = goods.get(0);
            double item_totalprice = Double.valueOf(good.getPrice())*count;
            template.update("insert into `orderitem` values(null,?,?,?,?,?,?,null)",
                    order_id, user_openid, goods_id, count, 0, item_totalprice);
            template.update("delete from trolley where user_openid = ? and goods_id= ?", user_openid, goods_id);
            return item_totalprice;
        }
        else {
            return 0;
        }
    }
}
