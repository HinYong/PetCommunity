package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    Integer id;  // 主键id
    String user_openid;  // 微信用户openid
    Integer goods_id;  // 商品编号
    String content;  // 评论内容
    Date time;  // 评论时间
    User user;  // 评论用户
    Goods goods;  // 评论的商品
    Integer orderItem_id;  // 该评论所属的订单细节项
}
