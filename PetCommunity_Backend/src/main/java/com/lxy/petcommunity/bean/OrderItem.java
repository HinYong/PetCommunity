package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    Integer item_id;  // 订单细节项id主键
    Integer order_id;  // 订单号
    Integer goods_id;  //商品id
    String user_openid;  // 微信用户openid
    Integer count;  // 商品数量
    Goods goods;  // 商品对象
    Integer is_commented;  // 该订单细节项是否已经评论，0为未评论，1为已评论
    Double item_totalPrice;  // 细节项总价
    Integer return_id; // 订单退货状态，如果是null则代表没有退货，如果有数据则说明退货
}
