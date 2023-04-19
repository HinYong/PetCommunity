package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Order;
import com.lxy.petcommunity.dao.OrderItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    OrderItemDao dao;

    // 更新订单细节项的退款单号
    public int updateReturn_id(int return_id, int item_id){
        return dao.updateReturn_id(return_id, item_id);
    }

}
