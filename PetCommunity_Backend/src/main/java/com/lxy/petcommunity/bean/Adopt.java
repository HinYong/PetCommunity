package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Adopt {
    Integer adopt_id;  // 主键id
    Integer agency_id;  // 发布领养需求的机构id
    Integer pet_type_id;  // 宠物种类id
    String pet_name;  // 宠物品种名称
    String pet_age;  // 宠物年龄
    String images;  // 宠物图片集
    String pet_description;  // 领养宠物描述
    String adopt_requirement;  // 领养要求
    Date publish_time;  // 发布时间
    Date finish_time;  // 完成时间
    Integer status;  // 需求状态，0为未有人申请，1为有人提交申请，2为已完成
    // 非数据库字段
    Agency agency;  // 发布领养需求的机构对象
    PetsType petsType;  // 宠物类型对象
    List<String> images_List;  // 图片路径列表
}
