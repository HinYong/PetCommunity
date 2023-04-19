package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Order;
import com.lxy.petcommunity.bean.OrderItem;
import com.lxy.petcommunity.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderDao dao;

    // 管理员端
    // 获取指定状态所有订单的数量
    public int getOrdersCountByStatus(int status) {
        return dao.getOrdersCountByStatus(status);
    }

    // 分页返回指定状态的所有订单到管理员端
    public List<Order> getAllOrdersByStatus(int page, int status, int limit) {
        return dao.getAllOrdersByStatus(page, status, limit);
    }

    // 在指定状态下按照订单号查找并返回管理员端
    public List<Order> findOrderByIdAndStatus(int page, int status, int limit, String order_id) {
        return dao.findOrderByIdAndStatus(page,status,limit,order_id);
    }

    // 管理员删除订单
    public int delOrder(int id) {
        return dao.delOrder(id);
    }

    // 管理员发货
    public int orderDelivery(int orderid, String deliveryId, String deliveryCompany) {
        return dao.orderDelivery(orderid, deliveryId, deliveryCompany);
    }

    // 用户端
    // 用户端获取所有订单
    public List getAllOrders(String openid) {
        return dao.getAllOrders(openid);
    }

    // 用户端获得指定订单详情
    public Order getOrderById(int orderid, String user_openid){
        return dao.getOrderById(orderid, user_openid);
    }

    // 新建订单，并且返回新增的订单主键编号
    public int createOrder(String user_openid1, int addressid, String receiverInfo){
        return dao.createOrder(user_openid1,addressid,receiverInfo);
    }

    // 更新订单总价
    public int updateTotalPrice(double totalPrice, int order_id){
        return dao.updateTotalPrice(totalPrice, order_id);
    }

    // 更新订单是否有退款情况存在
    public int updateReturnStatus(int is_in_returnStatus, int order_id){
        return dao.updateReturnStatus(is_in_returnStatus, order_id);
    }

    // 直接在商品详情页购买
    public int buyDirectly(int order_id, String user_openid, Integer goods_id, Integer count) {
        return dao.buyDirectly(order_id, user_openid,goods_id,count);
    }

    // 用户确认收货
    public int userConfirmOrder(int orderid, String user_openid){
        return dao.userConfirmOrder(orderid, user_openid);
    }

    // 用户对于未发货的订单进行取消
    public int cancelOrder(String user_openid, int order_id){
        return dao.cancelOrder(user_openid, order_id);
    }
}
