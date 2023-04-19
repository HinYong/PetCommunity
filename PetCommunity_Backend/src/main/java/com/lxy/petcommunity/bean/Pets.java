package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pets {
    Integer pet_id;  // 宠物词条id主键
    Integer pet_type_id;  // 所属宠物类型
    String name;  // 宠物名称
    String English_name;  // 宠物英文名
    String character;  // 宠物性格简介
    String hometown;  // 发源地
    String illness;  // 易患病
    String lifetime;  // 寿命
    String common_price;  // 市场价
    String image;  // 宠物主图片的地址
    String swiper_images;  // 宠物详情页轮播图
    String introduce;  // 宠物简介
    String appearance;  // 宠物外观特征介绍
    String character_detail;  // 宠物性格特征详细介绍
    String feed_knowledge;  // 宠物喂养建议
    PetsType pet_type;  // 宠物所属种类
    String sourceURL;  // 来源网页
    List<String> swiper_List;  // 轮播图图片的地址列表，管理员前端不需要，小程序前端需要
}
