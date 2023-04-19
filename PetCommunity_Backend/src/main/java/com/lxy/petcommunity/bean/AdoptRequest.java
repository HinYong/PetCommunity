package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdoptRequest {
    Integer request_id;  // 主键id
    Integer adopt_id;  // 领养需求id
    Integer agency_id;  // 宠物救助机构id
    String user_openid;  // 用户id
    String contact_name;  // 联系人名称
    String contact_phone;  // 联系人电话
    String contact_address;  // 联系人地址
    String request_content;  // 提交申请的文字内容
    String images;  // 提交申请的图片集的路径
    Integer status;  // 需求状态，0为未审核，1为通过初审，2为手续完成，3为拒绝，4为用户取消申请
    Date request_time;  // 发布时间
    Date process_time;  // 初审通过时间
    Date finish_time;  // 完成领养手续时间
    Date refuse_time;  // 拒绝领养时间
    Date cancel_time;  // 用户取消领养时间
    String refuse_reason;  // 拒绝理由
    // 非数据库字段
    Adopt adopt;  // 领养请求对象
    Agency agency;  // 宠物救助机构对象
    User user;  // 用户对象
    List<String> images_List;  // 图片路径列表
}
