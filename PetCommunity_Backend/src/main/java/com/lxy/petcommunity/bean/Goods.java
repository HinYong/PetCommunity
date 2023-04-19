package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    Integer _id;  // 商品id主键
    String image;  // 商品主图片的地址
    String name;  // 商品名称
    String price;  // 商品价格
    Integer type_id;  // 商品所属种类的id
    Type type;  // 商品所属种类
    Integer status; // 0为下架，1为上架，默认为1
    String swiper_images;  // 商品详情页轮播图
    List<String> swiper_List;  // 轮播图图片的地址列表，管理员前端不需要，小程序前端需要
}
