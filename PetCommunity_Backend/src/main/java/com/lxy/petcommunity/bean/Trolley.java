package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trolley {
    Integer id;  // 购物车条目id
    Integer goods_id;  // 该条目中商品的id
    String user_openid;  // 该条目所属微信用户的openid
    Integer count;  // 该条目中商品数量
    Goods goods;  // 该条目商品的对象
    User user; // 该条目所属用户对象
}
