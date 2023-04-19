package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Trolley;
import com.lxy.petcommunity.dao.TrolleyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrolleyService {
    @Autowired
    TrolleyDao dao;

    // 获取购物车条目
    public List getTrolley(String openid) {
        return dao.getTrolley(openid);
    }

    // 用于购物车更新的业务中，先清空购物车后，将前端传过来的数据用addTrolley添加到数据库中
    public void delTrolley(String user_openid) {
        dao.delTrolley(user_openid);
    }

    // 将前端传过来的数据添加到数据库中
    public void addTrolley(String user_openid, Integer goods_id, Integer count) {
        dao.addTrolley(user_openid,goods_id,count);
    }

    // 用户向购物车添加商品，每次只添加一件
    public void addItemToTrolley(String user_openid, int goods_id) {
        dao.addItemToTrolley(user_openid,goods_id);
    }

    // 在controller中循环遍历前端传过来的购物车列表，循环调用该函数对于提交的订单中的每一项进行购买
    public double buyFromTrolley(int order_id, String user_openid, Integer goods_id, Integer count) {
        return dao.buyFromTrolley(order_id, user_openid,goods_id,count);
    }
}
