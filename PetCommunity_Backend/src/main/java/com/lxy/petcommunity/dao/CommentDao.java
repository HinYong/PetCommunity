package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CommentDao {
    @Autowired
    JdbcTemplate template;

    // 管理员端
    // 管理员端获取所有商品的评论数量
    public int getCount() {
        int count = template.queryForObject("select count(*) from  `comment`", Integer.class);
        return count;
    }

    // 管理员端查找某种商品的评论数量
    public int getCount(String name) {
        int count = 0;
        List<Comment> list = template.query("select * from `comment`",
                new BeanPropertyRowMapper(Comment.class));
        if (!list.isEmpty()){
            for (Comment comment:list){
               List<Goods> goods = template.query("select * from goods where _id ="+comment.getGoods_id() +" and `name` like '%"+name+"%'" ,
                        new BeanPropertyRowMapper(Goods.class));
                if (!goods.isEmpty()){
                    count++;
                }
            }
            return count;
        }else{
            return 0;
        }
    }

    // 管理员端获取所有评论
    public List<Comment> getAllComments(int page, int limit) {
        List<Comment> list = template.query("select * from `comment` limit ?,?" ,new Object[]{(page-1)*limit,limit} ,
                new BeanPropertyRowMapper(Comment.class));
        if (list!=null&&list.size()>0){
            for (Comment comment:list){
                List<User> users = template.query("select * from `user` where open_id = '"+comment.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                List<Goods> goods = template.query("select * from goods where _id ="+comment.getGoods_id()  ,
                        new BeanPropertyRowMapper(Goods.class));
                if(!goods.isEmpty()){
                    Goods good = goods.get(0);
                    List<Type> types = template.query("select * from `type` where id = "+ good.getType_id()  ,
                            new BeanPropertyRowMapper(Type.class));
                    if(!types.isEmpty()){
                        good.setType(types.get(0));
                    }
                    comment.setGoods(good);
                }
                comment.setUser(users.get(0));
            }
            return list;
        }else{
            return null;
        }
    }

    // 管理员端查找某种商品的评论
    public List<Comment> findComment(int page, int limit, String name) {
        List<Comment> result = new ArrayList<>();
        List<Comment> list = template.query("select * from `comment` limit ?,?" ,new Object[]{(page-1)*limit,limit} ,
                new BeanPropertyRowMapper(Comment.class));
        if (list!=null&&list.size()>0){
            for (Comment comment:list){
                List<User> users = template.query("select * from `user` where open_id = '"+comment.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                List<Goods> goods = template.query("select * from goods where _id ="+comment.getGoods_id() +" and `name` like '%"+name+"%'" ,
                        new BeanPropertyRowMapper(Goods.class));
                // 判断当前的order中商品的名称是否含有关键字
                if (!goods.isEmpty()){
                    Goods good = goods.get(0);
                    List<Type> types = template.query("select * from `type` where id = "+ good.getType_id()  ,
                            new BeanPropertyRowMapper(Type.class));
                    if(!types.isEmpty()){
                        good.setType(types.get(0));
                    }
                    comment.setUser(users.get(0));
                    comment.setGoods(good);
                    result.add(comment);
                }
            }
            return result;
        }else{
            return null;
        }
    }

    // 管理员端删除评论
    public int delComment(int id, int orderItem_id) {
        template.update("update `orderitem` set is_commented = 0 where item_id = ?", orderItem_id);
        return template.update("DELETE from `comment` where id = ?",id);
    }

    // 用户端
    // 用户端获取某种商品的评论数量
    public int getCommentCountByGoodsId(String id) {
        int count = template.queryForObject("select count(*) from comment where goods_id = "+id, Integer.class);
        return count;
    }

    // 用户端获取某种商品的所有评论
    public List getAllCommentsByGoodsId(String id) {
        List<Comment> list = template.query("select * from `comment` where goods_id = " + id ,
                new BeanPropertyRowMapper(Comment.class));
        if (list!=null&&list.size()>0){
            for (Comment comment:list){
                List<User> users = template.query("select * from `user` where open_id = '"+comment.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                List<Goods> goods = template.query("select * from goods where _id ="+comment.getGoods_id()  ,
                        new BeanPropertyRowMapper(Goods.class));
                if(!goods.isEmpty()){
                    comment.setGoods(goods.get(0));
                }
                comment.setUser(users.get(0));
            }
            return list;
        }else{
            return null;
        }
    }

    // 用户端添加评论
    public void addComment(String openid, int goods_id, int orderitem_id, String content) {
        template.update("insert into comment values(null,?,?,?,?,?)",
                openid,goods_id,orderitem_id,content,new Date());
        template.update("update `orderitem` set `is_commented` = 1 WHERE `item_id` = ?", orderitem_id);
    }

    // 用户端查看某一个订单项自己的评论
    public String checkComment(int orderitem_id) {
        List<String> list = template.queryForList("select content from `comment` where orderitem_id = "+ orderitem_id , String.class);
        return list.get(0);
    }
}
