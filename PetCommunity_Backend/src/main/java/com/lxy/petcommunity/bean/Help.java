package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Help {
    Integer help_id;  // 主键id
    String user_openid;  // 用户id
    String contact_name;  // 联系人名称
    String contact_phone;  // 联系人电话
    String province;  // 省份
    String city;  // 市
    String detail_address;  // 详细地址
    String help_content;  // 发布的文字内容
    String images;  // 发布的图片集的路径
    Date publish_time;  // 发布时间
    Date process_time;  // 处理时间
    Date finish_time;  // 完成时间
    Date cancel_time;  // 取消时间
    Integer agency_id;  // 处理请求的所属机构id
    Integer status;  // 状态，0为未受理，1为受理中，2为已完成，3为已取消
    String staff_name;  // 机构对接员工姓名
    String staff_phone;  // 机构对接员工电话
    // 非数据库字段
    User user;  // 发布博客的用户对象
    Agency agency;  // 受理的机构对象
    List<String> images_List;  // 图片路径列表，管理员前端不需要，小程序前端需要
    String address;  // 地址拼接字符串
}
