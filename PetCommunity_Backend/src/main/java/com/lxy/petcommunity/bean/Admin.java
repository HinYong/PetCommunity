package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Admin {
    String email;  // 管理员邮箱
    String username;  // 管理员用户名
    String password;  // 管理员密码
    String phone;  // 管理员电话
    Integer sex;  // 管理员性别
    Integer id;  // 主键id
}
