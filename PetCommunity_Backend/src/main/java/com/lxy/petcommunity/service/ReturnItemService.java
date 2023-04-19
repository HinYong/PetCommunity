package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Order;
import com.lxy.petcommunity.bean.ReturnItem;
import com.lxy.petcommunity.dao.ReturnItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnItemService {
    @Autowired
    ReturnItemDao dao;

    // 管理员端
    // 获取指定状态所有订单的数量
    public int getReturnItemsCountByStatus(int status) {
        return dao.getReturnItemsCountByStatus(status);
    }

    // 根据订单号返回所有退货条目的数量
    public int getReturnItemsCountByOrderID(String order_id) {
        return dao.getReturnItemsCountByOrderID(order_id);
    }

    // 根据订单号返回指定退货状态的所有条目的数量
    public int getReturnItemsCountByStatusAndOrderID(int return_status, String order_id) {
        return dao.getReturnItemsCountByStatusAndOrderID(return_status, order_id);
    }

    // 分页返回指定退货状态的所有退货条目
    public List<ReturnItem> getAllReturnItemsByStatus(int page, int return_status, int limit) {
        return dao.getAllReturnItemsByStatus(page, return_status, limit);
    }

    // 根据指定订单号以及退货状态查找该订单中的所有退货项目，并且分页返回
    public List<ReturnItem> findReturnItemsByOrderIdAndStatus(int page, int return_status, int limit, String order_id) {
        return dao.findReturnItemsByOrderIdAndStatus(page,return_status,limit,order_id);
    }

    // 管理员同意退货
    public int agreeReturn(int return_id) {
        return dao.agreeReturn(return_id);
    }

    // 管理员拒绝退货
    public int refuseReturn(int return_id, String refuse_return_reason) {
        return dao.refuseReturn(return_id, refuse_return_reason);
    }

    // 管理员将拒绝退款的货物退回
    public int refuseReturnDelivery(int return_id, String refuse_delivery_id, String refuse_delivery_company) {
        return dao.refuseReturnDelivery(return_id, refuse_delivery_id, refuse_delivery_company);
    }

    // 用户端
    // 用户创建退款订单
    public int createReturnItem(String user_openid, int order_id, int item_id, int return_count, int goods_id, double return_totalPrice, String return_reason, String return_delivery_id, String return_delivery_company, String contact_phone){
        return dao.createReturnItem(user_openid, order_id, item_id, return_count, goods_id, return_totalPrice, return_reason, return_delivery_id, return_delivery_company, contact_phone);
    }

    // 根据退款单号将订单返回用户端
    public ReturnItem getReturnItemByReturnId(String return_id, String user_openid){
        return dao.getReturnItemByReturnId(return_id, user_openid);
    }

}
