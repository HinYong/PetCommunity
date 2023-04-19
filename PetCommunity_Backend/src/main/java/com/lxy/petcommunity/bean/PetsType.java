package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetsType {
    Integer pet_type_id;  // 宠物品种类型id
    String pet_type_name;  // 宠物品种类型名称
}


