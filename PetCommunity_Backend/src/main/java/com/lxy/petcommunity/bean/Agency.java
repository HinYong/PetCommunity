package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Agency {
    Integer agency_id;  // 主键id
    String agency_name;  // 机构用户名
    String province;  // 所属省份
    String city;  // 所属城市
    String detail_address;  // 详细地址
    String total_address;  // 完整地址
    String password;  // 机构密码
    String email;  // 机构邮箱
    String phone;  // 机构联系电话
    String website;  // 机构网站
}
