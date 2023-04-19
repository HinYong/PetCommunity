package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class ReturnItemDao {
    @Autowired
    JdbcTemplate template;

    // 管理员端
    // 返回指定退货状态的所有条目的数量
    public int getReturnItemsCountByStatus(int return_status) {
        int count = template.queryForObject("select count(*) from `returnitem` where return_status = " + return_status, Integer.class);
        return count;
    }

    // 根据订单号返回所有退货条目的数量
    public int getReturnItemsCountByOrderID(String order_id) {
        int count = template.queryForObject("select count(*) from `returnitem` where order_id = " + order_id, Integer.class);
        return count;
    }

    // 根据订单号返回指定退货状态的所有条目的数量
    public int getReturnItemsCountByStatusAndOrderID(int return_status, String order_id) {
        int count = template.queryForObject("select count(*) from `returnitem` where return_status = " + return_status + " and order_id = " + order_id, Integer.class);
        return count;
    }

    // 分页返回指定退货状态的所有退货条目
    public List<ReturnItem> getAllReturnItemsByStatus(int page, int return_status, int limit) {
        List<ReturnItem> list;
        list = template.query("select * from `returnitem` where return_status = " + return_status + " limit ?,?" ,new Object[]{(page-1)*limit,limit} ,
                new BeanPropertyRowMapper(ReturnItem.class));
        if (list!=null&&list.size()!=0){
            for (ReturnItem returnItem:list){
                List<User> users = template.query("select * from `user` where open_id = '"+returnItem.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                List<Goods> goods = template.query("select * from goods where _id ="+returnItem.getGoods_id()  ,
                        new BeanPropertyRowMapper(Goods.class));
                if(!goods.isEmpty()){
                    Goods good = goods.get(0);
                    List<Type> types = template.query("select * from `type` where id = "+ good.getType_id()  ,
                            new BeanPropertyRowMapper(Type.class));
                    if(!types.isEmpty()){
                        good.setType(types.get(0));
                    }
                    returnItem.setGoods(good);
                }
                returnItem.setUser(users.get(0));
            }
            return list;
        }else{
            return null;
        }
    }

    // 根据指定订单号以及退货状态查找该订单中的所有退货项目，并且分页返回
    public List<ReturnItem> findReturnItemsByOrderIdAndStatus(int page, int return_status, int limit, String order_id) {
        List<ReturnItem> list;
        list = template.query("select * from `returnitem` where return_status = " + return_status + " and order_id = " + order_id + " limit ?,?" ,new Object[]{(page-1)*limit,limit} ,
                new BeanPropertyRowMapper(ReturnItem.class));
        if (list!=null&&list.size()!=0){
            for (ReturnItem returnItem:list){
                List<User> users = template.query("select * from `user` where open_id = '"+returnItem.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                List<Goods> goods = template.query("select * from goods where _id ="+returnItem.getGoods_id()  ,
                        new BeanPropertyRowMapper(Goods.class));
                if(!goods.isEmpty()){
                    Goods good = goods.get(0);
                    List<Type> types = template.query("select * from `type` where id = "+ good.getType_id()  ,
                            new BeanPropertyRowMapper(Type.class));
                    if(!types.isEmpty()){
                        good.setType(types.get(0));
                    }
                    returnItem.setGoods(good);
                }
                returnItem.setUser(users.get(0));
            }
            return list;
        }else{
            return null;
        }
    }

    // 管理员同意退货
    public int agreeReturn(int return_id) {
        return template.update("update returnitem set return_status = 2, refuse_return_reason = null, refuse_delivery_id = null, refuse_delivery_company = null, return_finish_time=? where return_id=?", new Date(), return_id);
    }

    // 管理员拒绝退货
    public int refuseReturn(int return_id, String refuse_return_reason) {
        return template.update("update returnitem set return_status = 3, refuse_return_reason=?, return_finish_time=? where return_id=?",refuse_return_reason, new Date(), return_id);
    }

    // 管理员将拒绝退款的货物退回
    public int refuseReturnDelivery(int return_id, String refuse_delivery_id, String refuse_delivery_company) {
        return template.update("update returnitem set return_status = 3, refuse_delivery_id=?, refuse_delivery_company=? where return_id=?", refuse_delivery_id, refuse_delivery_company, return_id);
    }

    // 用户端
    // 用户发起退款请求
    public int createReturnItem(String user_openid, int order_id, int item_id, int return_count, int goods_id, double return_totalPrice, String return_reason, String return_delivery_id, String return_delivery_company, String contact_phone){
        String sql = "insert into `returnitem` values(null,?,?,?,?,?,1,?,?,null,?,?,?,?,null,null,null)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                // 指定主键
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                preparedStatement.setInt(1, order_id);
                preparedStatement.setInt(2, item_id);
                preparedStatement.setInt(3, goods_id);
                preparedStatement.setString(4, user_openid);
                preparedStatement.setInt(5, return_count);
                preparedStatement.setDouble(6, return_totalPrice);
                preparedStatement.setObject(7, new java.sql.Timestamp(new Date().getTime()));
                preparedStatement.setString(8, return_reason);
                preparedStatement.setString(9, return_delivery_id);
                preparedStatement.setString(10, return_delivery_company);
                preparedStatement.setString(11, contact_phone);
                return preparedStatement;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    // 根据退款单号将订单返回用户端
    public ReturnItem getReturnItemByReturnId(String return_id, String user_openid){
        List<ReturnItem> list = template.query("select * from `returnitem` where user_openid = '" + user_openid + "' and return_id = " + return_id ,
                new BeanPropertyRowMapper(ReturnItem.class));
        if (list!=null&&list.size()!=0){
            ReturnItem returnItem = list.get(0);
            List<User> users = template.query("select * from `user` where open_id = '"+ returnItem.getUser_openid()+"'"  ,
                    new BeanPropertyRowMapper(User.class));
            List<Goods> goods = template.query("select * from goods where _id ="+returnItem.getGoods_id()  ,
                    new BeanPropertyRowMapper(Goods.class));
            if(!goods.isEmpty()){
                Goods good = goods.get(0);
                List<Type> types = template.query("select * from `type` where id = "+ good.getType_id()  ,
                        new BeanPropertyRowMapper(Type.class));
                if(!types.isEmpty()){
                    good.setType(types.get(0));
                }
                returnItem.setGoods(good);
            }
            returnItem.setUser(users.get(0));
            return returnItem;
        }else{
            return null;
        }
    }

}



