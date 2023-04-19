package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    Integer id;  // 订单主键id
    String user_openid;  // 微信用户的openid
    User user;  // 订单所属用户
    List<OrderItem> orderItems;  // 包含订单细节项的列表
    List<ReturnItem> returnItems;  // 包含该订单所有的退款项
    Integer status;  // 订单所处状态，1是未发货，2是已发货，3是确认收货，4是退货中
    Integer address_id;  // 该订单所对应的收货信息id
    String receiver_info;  // 该订单收货信息的字符串化
    String delivery_id;  // 该订单的快递单号
    String delivery_company;  // 该订单承运的快递公司
    Date create_time;  // 订单创建时间
    Date delivery_time;  // 订单发货时间
    Date finish_time;  // 订单完成时间
    Date cancel_time;  // 订单取消时间
    Double totalPrice; // 订单总价
    Integer is_in_returnStatus; // 是否有退款申请，0为无，1为有，用于展示给管理员看
    String orderItems_toString; // 用于管理员端展示订单项详情
    String returnItems_toString; // 用于管理员端展示退款项详情
}
