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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDao {
    @Autowired
    JdbcTemplate template;

    // 管理员端
    // 返回指定状态的所有订单数量
    public int getOrdersCountByStatus(int status) {
        int count = template.queryForObject("select count(*) from `order` where status = " + status, Integer.class);
        return count;
    }

    // 管理员端获得指定状态的所有订单，分页取数据
    public List<Order> getAllOrdersByStatus(int page, int status, int limit) {
        List<Order> list;
        list = template.query("select * from `order` where status = " + status + " limit ?,?" ,new Object[]{(page-1)*limit,limit} ,
                    new BeanPropertyRowMapper(Order.class));
        if (list!=null&&list.size()!=0){
            for (Order order:list){
                // 获取User对象
                List<User> users = template.query("select * from `user` where open_id = '"+order.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                order.setUser(users.get(0));

                // 获取订单项
                List<OrderItem> orderItems = template.query("select * from orderitem where user_openid = '" + order.getUser_openid() + "' and order_id = "+order.getId(),
                        new BeanPropertyRowMapper(OrderItem.class));
                String orderItems_toString = "";
                for (OrderItem orderItem:orderItems){
                    List<Goods> goods = template.query("select * from goods where _id ="+orderItem.getGoods_id()  ,
                            new BeanPropertyRowMapper(Goods.class));
                    if(!goods.isEmpty()){
                        Goods good = goods.get(0);
                        List<Type> types = template.query("select * from `type` where id = "+ good.getType_id()  ,
                                new BeanPropertyRowMapper(Type.class));
                        if(!types.isEmpty()){
                            good.setType(types.get(0));
                        }
                        orderItem.setGoods(good);
                        orderItems_toString += "商品ID: " + good.get_id()
                                + " 商品名称: " + good.getName()
                                + " 商品类型: " + good.getType().getName()
                                + " 数量: " + orderItem.getCount()
                                + " 小计: " + orderItem.getItem_totalPrice()+"元<br>";
                    }
                }
                order.setOrderItems(orderItems);
                order.setOrderItems_toString(orderItems_toString);

                // 如果有退款项，则获取退款项
                if(order.getIs_in_returnStatus()==1){
                    List<ReturnItem> returnItems = template.query("select * from returnitem where user_openid = '" + order.getUser_openid() + "' and order_id = "+order.getId(),
                            new BeanPropertyRowMapper(ReturnItem.class));
                    String returnItems_toString = "";
                    for (ReturnItem returnItem:returnItems){
                        List<Goods> goods = template.query("select * from goods where _id =" + returnItem.getGoods_id()  ,
                                new BeanPropertyRowMapper(Goods.class));
                        if(!goods.isEmpty()){
                            Goods good = goods.get(0);
                            List<Type> types = template.query("select * from `type` where id = "+ good.getType_id()  ,
                                    new BeanPropertyRowMapper(Type.class));
                            if(!types.isEmpty()){
                                good.setType(types.get(0));
                            }
                            returnItem.setGoods(good);
                            String returnItem_status;
                            if(returnItem.getReturn_status()==1){
                                returnItem_status = "待验收";
                            }
                            else if(returnItem.getReturn_status()==2){
                                returnItem_status = "已同意";
                            }
                            else{
                                returnItem_status = "已拒绝";
                            }
                            returnItems_toString += "退货单号: " + returnItem.getReturn_id()
                                    + " 商品ID: " + good.get_id()
                                    + " 商品名称: " + good.getName()
                                    + " 商品类型: " + good.getType().getName()
                                    + " 退货数量: " + returnItem.getReturn_count()
                                    + " 小计: " + returnItem.getReturn_totalPrice()+"元"
                                    + " 退款状态: " + returnItem_status +"<br>";
                        }
                    }
                    order.setReturnItems(returnItems);
                    order.setReturnItems_toString(returnItems_toString);
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 根据订单号查找指定状态下的订单
    public List<Order> findOrderByIdAndStatus(int page, int status, int limit, String order_id) {
        List<Order> list;
        list = template.query("select * from `order` where status = " + status + " and id = " + order_id + " limit ?,?" ,new Object[]{(page-1)*limit,limit} ,
                    new BeanPropertyRowMapper(Order.class));
        if (list!=null&&list.size()!=0){
            for (Order order:list){
                List<User> users = template.query("select * from `user` where open_id = '"+order.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                order.setUser(users.get(0));

                // 获取订单项
                List<OrderItem> orderItems = template.query("select * from orderitem where user_openid = '" + order.getUser_openid() + "' and order_id = "+order.getId(),
                        new BeanPropertyRowMapper(OrderItem.class));
                String orderItems_toString = "";
                for (OrderItem orderItem:orderItems){
                    List<Goods> goods = template.query("select * from goods where _id ="+orderItem.getGoods_id()  ,
                            new BeanPropertyRowMapper(Goods.class));
                    if(!goods.isEmpty()){
                        Goods good = goods.get(0);
                        List<Type> types = template.query("select * from `type` where id = "+ good.getType_id()  ,
                                new BeanPropertyRowMapper(Type.class));
                        if(!types.isEmpty()){
                            good.setType(types.get(0));
                        }
                        orderItem.setGoods(good);
                        orderItems_toString += "商品ID: " + good.get_id()
                                + " 商品名称: " + good.getName()
                                + " 商品类型: " + good.getType().getName()
                                + " 数量: " + orderItem.getCount()
                                + " 小计: " + orderItem.getItem_totalPrice()+"元<br>";
                    }
                }
                order.setOrderItems(orderItems);
                order.setOrderItems_toString(orderItems_toString);

                // 如果有退款项，则获取退款项
                if(order.getIs_in_returnStatus()==1){
                    List<ReturnItem> returnItems = template.query("select * from returnitem where user_openid = '" + order.getUser_openid() + "' and order_id = "+order.getId(),
                            new BeanPropertyRowMapper(ReturnItem.class));
                    String returnItems_toString = "";
                    for (ReturnItem returnItem:returnItems){
                        List<Goods> goods = template.query("select * from goods where _id =" + returnItem.getGoods_id()  ,
                                new BeanPropertyRowMapper(Goods.class));
                        if(!goods.isEmpty()){
                            Goods good = goods.get(0);
                            List<Type> types = template.query("select * from `type` where id = "+ good.getType_id()  ,
                                    new BeanPropertyRowMapper(Type.class));
                            if(!types.isEmpty()){
                                good.setType(types.get(0));
                            }
                            returnItem.setGoods(good);
                            String returnItem_status;
                            if(returnItem.getReturn_status()==1){
                                returnItem_status = "待验收";
                            }
                            else if(returnItem.getReturn_status()==2){
                                returnItem_status = "已同意";
                            }
                            else{
                                returnItem_status = "已拒绝";
                            }
                            returnItems_toString += "退货单号: " + returnItem.getReturn_id()
                                    + " 商品ID: " + good.get_id()
                                    + " 商品名称: " + good.getName()
                                    + " 商品类型: " + good.getType().getName()
                                    + " 退货数量: " + returnItem.getReturn_count()
                                    + " 小计: " + returnItem.getReturn_totalPrice()+"元"
                                    + " 退款状态: " + returnItem_status +"<br>";
                        }
                    }
                    order.setReturnItems(returnItems);
                    order.setReturnItems_toString(returnItems_toString);
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 管理员删除订单
    public int delOrder(int orderid) {
        return template.update("DELETE from `order` where id = ?",orderid);
    }

    // 管理员发货，插入发货时间
    public int orderDelivery(int orderid, String deliveryId, String deliveryCompany){
        return template.update("update `order` set status = 2, delivery_id = ?, delivery_company = ?, delivery_time = ? where id = ?", deliveryId, deliveryCompany, new Date(), orderid);
    }

    // 用户端
    // 获取所有订单
    public List getAllOrders(String openid) {
        List<Order> list = template.query("select * from `order` where user_openid = '" + openid + "'" ,
                    new BeanPropertyRowMapper(Order.class));
        if (list!=null&&list.size()!=0){
            for (Order order:list){
                List<User> users = template.query("select * from `user` where open_id = '"+order.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                order.setUser(users.get(0));

                // 获取订单细节项
                List<OrderItem> orderItems = template.query("select * from orderitem where user_openid = '" + order.getUser_openid() + "' and order_id = "+order.getId(),
                        new BeanPropertyRowMapper(OrderItem.class));
                for (OrderItem orderItem:orderItems){
                    List<Goods> goods = template.query("select * from goods where _id ="+orderItem.getGoods_id()  ,
                            new BeanPropertyRowMapper(Goods.class));
                    if(!goods.isEmpty()){
                        Goods good = goods.get(0);
                        List<Type> types = template.query("select * from `type` where id = "+ good.getType_id()  ,
                                new BeanPropertyRowMapper(Type.class));
                        if(!types.isEmpty()){
                            good.setType(types.get(0));
                        }
                        orderItem.setGoods(good);
                    }
                }
                order.setOrderItems(orderItems);

                // 如果有退款项，则获取退款项
                if(order.getIs_in_returnStatus()==1) {
                    List<ReturnItem> returnItems = template.query("select * from returnitem where user_openid = '" + order.getUser_openid() + "' and order_id = " + order.getId(),
                            new BeanPropertyRowMapper(ReturnItem.class));
                    for (ReturnItem returnItem : returnItems) {
                        List<Goods> goods = template.query("select * from goods where _id =" + returnItem.getGoods_id(),
                                new BeanPropertyRowMapper(Goods.class));
                        if (!goods.isEmpty()) {
                            Goods good = goods.get(0);
                            List<Type> types = template.query("select * from `type` where id = " + good.getType_id(),
                                    new BeanPropertyRowMapper(Type.class));
                            if (!types.isEmpty()) {
                                good.setType(types.get(0));
                            }
                            returnItem.setGoods(good);
                        }
                    }
                    order.setReturnItems(returnItems);
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 根据订单号将订单返回用户端
    public Order getOrderById(int orderid, String user_openid){
        List<Order> list = template.query("select * from `order` where user_openid = '" + user_openid + "' and id = " + orderid ,
                new BeanPropertyRowMapper(Order.class));
        if (list!=null&&list.size()!=0){
            for (Order order:list) {
                List<User> users = template.query("select * from `user` where open_id = '" + order.getUser_openid() + "'",
                        new BeanPropertyRowMapper(User.class));
                order.setUser(users.get(0));

                // 获取订单细节项
                List<OrderItem> orderItems = template.query("select * from orderitem where user_openid = '" + order.getUser_openid() + "' and order_id = " + order.getId(),
                        new BeanPropertyRowMapper(OrderItem.class));
                for (OrderItem orderItem : orderItems) {
                    List<Goods> goods = template.query("select * from goods where _id =" + orderItem.getGoods_id(),
                            new BeanPropertyRowMapper(Goods.class));
                    if (!goods.isEmpty()) {
                        Goods good = goods.get(0);
                        List<Type> types = template.query("select * from `type` where id = " + good.getType_id(),
                                new BeanPropertyRowMapper(Type.class));
                        if (!types.isEmpty()) {
                            good.setType(types.get(0));
                        }
                        orderItem.setGoods(good);
                    }
                }
                order.setOrderItems(orderItems);

                // 如果有退款项,则获取退款项
                if(order.getIs_in_returnStatus()==1) {
                    List<ReturnItem> returnItems = template.query("select * from returnitem where user_openid = '" + order.getUser_openid() + "' and order_id = " + order.getId(),
                            new BeanPropertyRowMapper(ReturnItem.class));
                    for (ReturnItem returnItem : returnItems) {
                        List<Goods> goods = template.query("select * from goods where _id =" + returnItem.getGoods_id(),
                                new BeanPropertyRowMapper(Goods.class));
                        if (!goods.isEmpty()) {
                            Goods good = goods.get(0);
                            List<Type> types = template.query("select * from `type` where id = " + good.getType_id(),
                                    new BeanPropertyRowMapper(Type.class));
                            if (!types.isEmpty()) {
                                good.setType(types.get(0));
                            }
                            returnItem.setGoods(good);
                        }
                    }
                    order.setReturnItems(returnItems);
                }
            }
            return list.get(0);
        }else{
            return null;
        }
    }

    // 新建订单，并且返回订单号
    public int createOrder(String user_openid1, int addressid, String receiverInfo){
        String sql = "insert into `order` values(null,?,?,?,?,null,null,?,null,null,null,0,0)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                // 指定主键
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                preparedStatement.setString(1, user_openid1);
                preparedStatement.setInt(2, 1);
                preparedStatement.setInt(3, addressid);
                preparedStatement.setString(4, receiverInfo);
                preparedStatement.setObject(5, new java.sql.Timestamp(new Date().getTime()));
                return preparedStatement;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    // 更新订单总价
    public int updateTotalPrice(double totalPrice, int order_id){
        return template.update("update `order` set totalPrice = ? where id = ?", totalPrice, order_id);
    }

    // 更新订单是否有退款情况存在
    public int updateReturnStatus(int is_in_returnStatus, int order_id){
        return template.update("update `order` set is_in_returnStatus = ? where id = ?", is_in_returnStatus, order_id);
    }

    // 直接购买
    public int buyDirectly(int order_id, String user_openid, Integer goods_id, Integer count) {
        List<Goods> goods = template.query("select * from goods where _id = " + goods_id, new BeanPropertyRowMapper(Goods.class));
        if(!goods.isEmpty()){
            Goods good = goods.get(0);
            double totalPrice = Double.valueOf(good.getPrice())*count;
            template.update("insert into `orderitem` values(null,?,?,?,?,0,?,null)",
                    order_id, user_openid, goods_id, count, totalPrice);
            return template.update("update `order` set totalPrice = ? where id = ?", totalPrice, order_id);
        }
        else {
            return 0;
        }
    }

    // 用户确认收货，插入收货时间
    public int userConfirmOrder(int orderid, String user_openid){
        return template.update("update `order` set status=3, finish_time = ? where id = ? and user_openid = ?", new Date(), orderid, user_openid);
    }

    // 用户对于未发货的订单进行取消
    public int cancelOrder(String user_openid, int order_id){
        return template.update("update `order` set status = 4, cancel_time = ? where id = ? and user_openid = ?", new Date(), order_id, user_openid);
    }
}
