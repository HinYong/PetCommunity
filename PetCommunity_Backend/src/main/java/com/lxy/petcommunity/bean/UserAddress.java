package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddress {
    Integer id;  // 用户地址主键id
    String user_openid;  // 微信用户的openid
    String receiver_name;  // 收货人名称
    String receiver_phone;  // 收货人联系电话
    String province;  // 收货省份
    String city;  // 收货城市
    String area;  // 收货的区县
    String detail_address;  // 详细地址
    Integer is_default;  // 是否设为默认地址，0否1是
}
