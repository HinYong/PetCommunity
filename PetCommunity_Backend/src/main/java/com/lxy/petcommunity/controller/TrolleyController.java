package com.lxy.petcommunity.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxy.petcommunity.bean.Trolley;
import com.lxy.petcommunity.bean.UserAddress;
import com.lxy.petcommunity.service.OrderService;
import com.lxy.petcommunity.service.TrolleyService;
import com.lxy.petcommunity.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TrolleyController {
    // 引入了多个service需要写自动注入的注释
    @Autowired
    TrolleyService service;
    @Autowired
    OrderService orderService;
    @Autowired
    UserAddressService userAddressService;

    // 获取购物车
    @GetMapping("/wx/getTrolley")
    public List getTrolley(@RequestParam String openid){
        List list = service.getTrolley(openid);
        return list;
    }

    // 购物车更新
    @PostMapping("/wx/updateTrolley")
    @ResponseBody
    public int updateTrolley(@RequestParam String list,@RequestParam String openid){
        JSONArray jsonArray = JSONArray.parseArray(list);
        List<Trolley> trolleys = JSONObject.parseArray(jsonArray.toJSONString(), Trolley.class);
        if (trolleys.isEmpty()){
            service.delTrolley(openid);
        }else {
            service.delTrolley(openid);
            for (Trolley trolley:trolleys){
                Integer goods_id = trolley.getGoods_id();
                Integer count = trolley.getCount();
                service.addTrolley(openid,goods_id,count);
            }
        }
        return 200;
    }

    // 在购物车中勾选进行购买
    @PostMapping("/wx/buyFromTrolley")
    @ResponseBody
    public int buyFromTrolley(@RequestParam String list, @RequestParam String openid, @RequestParam int addressid){
        System.out.println(list);
        JSONArray jsonArray = JSONArray.parseArray(list);
        List<Trolley> trolleys = JSONObject.parseArray(jsonArray.toJSONString(), Trolley.class);
        // 获取地址
        UserAddress userAddress = userAddressService.getUserAddressById(openid, addressid);
        String receiverInfo = "收件人："+userAddress.getReceiver_name()
                +"  收件人电话："+userAddress.getReceiver_phone()
                +"  地址："+userAddress.getProvince()
                +" "+userAddress.getCity()
                +" "+userAddress.getArea()
                +" "+userAddress.getDetail_address();
        // 创建订单并且取到订单号
        int order_id = orderService.createOrder(openid, addressid, receiverInfo);
        double OrderTotalPrice = 0;
        // 遍历前端返回的所有物品列表
        for (Trolley trolley:trolleys){
            Integer goods_id = trolley.getGoods_id();
            Integer count = trolley.getCount();
            OrderTotalPrice += service.buyFromTrolley(order_id,openid,goods_id,count);
        }
        orderService.updateTotalPrice(OrderTotalPrice, order_id);
        return 200;
    }

    // 在详情页或者首页将物品添加到购物车，每次只添加1件
    @GetMapping("/wx/addToTrolley")
    public int addItemToTrolley(@RequestParam String user_openid,@RequestParam int goods_id){
        service.addItemToTrolley(user_openid,goods_id);
        return 200;
    }
}
