package com.lxy.petcommunity.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxy.petcommunity.bean.*;
import com.lxy.petcommunity.service.OrderItemService;
import com.lxy.petcommunity.service.OrderService;
import com.lxy.petcommunity.service.ReturnItemService;
import com.lxy.petcommunity.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    OrderService service;
    @Autowired
    UserAddressService userAddressService;
    @Autowired
    ReturnItemService returnItemService;
    @Autowired
    OrderItemService orderItemService;

    // 管理员端
    // 管理员端获取不同状态的订单
    @GetMapping("/api/getAllOrdersByStatus")
    public ResBody getOrdersByStatus(@RequestParam int page,
                                @RequestParam int status,
                                @RequestParam int limit){
        ResBody resBody = new ResBody();
        int count = service.getOrdersCountByStatus(status);
        List<Order> list= service.getAllOrdersByStatus(page, status, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端根据订单号查询订单
    @GetMapping("/api/findOrderByIdAndStatus")
    public ResBody findOrderByIdAndStatus(@RequestParam int page,
                            @RequestParam int limit,
                            @RequestParam int status,
                            @RequestParam String order_id) {
        ResBody resBody = new ResBody();
        List<Order> list= service.findOrderByIdAndStatus(page, status, limit, order_id);
        if(list==null){
            resBody.setCount(0);
        }
        else {
            resBody.setCount(list.size());
        }
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端删除订单
    @GetMapping("/api/delOrder")
    public ResBody delOrder(@RequestParam int id) {
        ResBody resBody = new ResBody();
        int i = service.delOrder(id);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("删除成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("删除失败");
        }
        return resBody;
    }

    // 管理员发货
    @GetMapping("/api/orderDelivery")
    public ResBody orderDelivery(@RequestParam String orderid, @RequestParam String deliveryId, @RequestParam String deliveryCompany) {
        ResBody resBody = new ResBody();
        System.out.println(orderid);
        int orderid1 = Integer.valueOf(orderid);
        int i = service.orderDelivery(orderid1, deliveryId, deliveryCompany);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("发货成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("发货失败");
        }
        return resBody;
    }

    // 用户端
    // 用户端获取所有订单
    @GetMapping("/wx/getAllOrders")
    public List getAllOrders(@RequestParam String openid){
        List list = service.getAllOrders(openid);
        return list;
    }

    // 用户端获得指定订单详情
    @GetMapping("/wx/getOrderById")
    public Order getOrderById(@RequestParam int orderid, @RequestParam String user_openid){
        return service.getOrderById(orderid, user_openid);
    }

    // 用户在详情页直接购买
    @PostMapping("/wx/buyDirectly")
    @ResponseBody
    public int buyDirectly(@RequestParam String list, @RequestParam String openid, @RequestParam int addressid){
        JSONArray jsonArray = JSONArray.parseArray(list);
        List<OrderItem> orderItems = JSONObject.parseArray(jsonArray.toJSONString(), OrderItem.class);
        if (orderItems.isEmpty()){
            return 200;
        }else {
            OrderItem orderItem = orderItems.get(0);
            // 获取地址
            UserAddress userAddress = userAddressService.getUserAddressById(openid, addressid);
            String receiverInfo = "收件人："+userAddress.getReceiver_name()
                    +"  收件人电话："+userAddress.getReceiver_phone()
                    +"  地址："+userAddress.getProvince()
                    +" "+userAddress.getCity()
                    +" "+userAddress.getArea()
                    +" "+userAddress.getDetail_address();
            System.out.println(receiverInfo);
            // 创建订单并且取到订单号
            int order_id = service.createOrder(openid, addressid, receiverInfo);
            // 因为前端只传了goods过来，没有goods_id，所以后端自动注入的时候orderitem的goods_id是空的，但是goods对象有东西
            Integer goods_id = orderItem.getGoods().get_id();
            Integer count = orderItem.getCount();
            service.buyDirectly(order_id,openid,goods_id,count);
        }
        return 200;
    }

    // 用户端确认收货
    @GetMapping("/wx/userConfirmOrder")
    public int userConfirmOrder(@RequestParam int orderid, @RequestParam String user_openid){
        return service.userConfirmOrder(orderid, user_openid);
    }

    // 未发货的订单用户可以取消订单
    @GetMapping("/wx/userCancelOrder")
    public int cancelOrder(@RequestParam String user_openid, @RequestParam int order_id){
        service.cancelOrder(user_openid, order_id);
        return 200;
    }

    // 用户发起退货申请
    @GetMapping("/wx/applyOrderItemReturn")
    public int applyOrderItemReturn(@RequestParam String user_openid, @RequestParam int order_id, @RequestParam int item_id, @RequestParam int goods_id, @RequestParam double return_totalprice, @RequestParam String return_reason, @RequestParam int return_count, @RequestParam String return_delivery_id, @RequestParam String return_delivery_company, @RequestParam String contact_phone){
        // 创建退货订单项并且取到退货编号
        int return_id = returnItemService.createReturnItem(user_openid,order_id,item_id,return_count,goods_id,return_totalprice,return_reason,return_delivery_id,return_delivery_company,contact_phone);
        // 将退货订单的编号存到orderitem中
        orderItemService.updateReturn_id(return_id,item_id);
        // 更新该细节项对应的订单状态，表明该订单中有退货事宜
        service.updateReturnStatus(1, order_id);
        return 200;
    }

}
