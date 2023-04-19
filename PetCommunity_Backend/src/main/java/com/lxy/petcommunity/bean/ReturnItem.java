package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnItem {
    Integer return_id;  // 退款的id主键
    Integer order_id;  // 退货细节项所属的订单号
    Integer item_id;  // 退款的订单细节项id
    Integer goods_id;  // 退货商品id
    String user_openid;  // 微信用户openid
    Goods goods;  // 退货商品对象
    User user; // 退货用户
    Integer return_count;  // 退货商品数量
    Double return_totalPrice;  // 退货的总价
    // 订单退货状态
    // 0 没有退货申请
    // 1 卖家通过退货申请，等待验收
    // 2 卖家签收并且同意退货
    // 3 卖家拒绝退货，退货失败，也就是直接拒签快递，发回去给买家和买家退货的快递单号一致
    Integer return_status;
    Date return_apply_time;  // 订单发起退货时间
    Date return_finish_time;  // 订单完成退货时间
    String return_reason; // 退货原因
    String return_delivery_id;  // 退货快递单号
    String return_delivery_company;  // 退货快递公司
    String contact_phone;  // 买家联系方式
    String refuse_return_reason;  // 商家拒绝退货原因
    String refuse_delivery_id;  // 拒绝退货，退回用户的快递单号
    String refuse_delivery_company;  // 拒绝退货，退回用户的快递公司
}
