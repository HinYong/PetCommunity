package com.lxy.petcommunity.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {
    @Autowired
    JdbcTemplate template;

    // 更新订单细节项的退款单号
    public int updateReturn_id(int return_id, int item_id){
        return template.update("update `orderitem` set return_id = ? where item_id = ?", return_id, item_id);
    }
}
