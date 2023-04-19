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
public class ReturnItemController {
    @Autowired
    ReturnItemService service;

    // 管理员端
    // 管理员端获取不同退货状态的所有退货单
    @GetMapping("/api/getAllReturnItemsByStatus")
    public ResBody getAllReturnItemsByStatus(@RequestParam int page,
                                @RequestParam int return_status,
                                @RequestParam int limit){
        ResBody resBody = new ResBody();
        int count = service.getReturnItemsCountByStatus(return_status);
        List<ReturnItem> list= service.getAllReturnItemsByStatus(page, return_status, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端根据指定订单号以及退货状态查找该订单中的所有退货项目，并且分页返回
    @GetMapping("/api/findReturnItemsByOrderIdAndStatus")
    public ResBody findReturnItemsByOrderIdAndStatus(@RequestParam int page,
                            @RequestParam int limit,
                            @RequestParam int return_status,
                            @RequestParam String order_id) {
        ResBody resBody = new ResBody();
        List<ReturnItem> list= service.findReturnItemsByOrderIdAndStatus(page, return_status, limit, order_id);
        int count = service.getReturnItemsCountByStatusAndOrderID(return_status, order_id);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员同意退款
    @GetMapping("/api/agreeReturn")
    public ResBody agreeReturn(@RequestParam int return_id){
        ResBody resBody = new ResBody();
        int i = service.agreeReturn(return_id);
        if (i == 1){
            resBody.setCode(200);
        }else{
            resBody.setCode(500);
        }
        return resBody;
    }

    // 管理员拒绝退款
    @GetMapping("/api/refuseReturn")
    public ResBody refuseReturn(@RequestParam int return_id, @RequestParam String refuse_return_reason){
        ResBody resBody = new ResBody();
        int i = service.refuseReturn(return_id, refuse_return_reason);
        if (i == 1){
            resBody.setCode(200);
        }else{
            resBody.setCode(500);
        }
        return resBody;
    }

    // 管理员将拒绝退款的货物退回
    @GetMapping("/api/refuseReturnDelivery")
    public ResBody refuseReturnDelivery(@RequestParam int return_id, @RequestParam String refuse_delivery_id, @RequestParam String refuse_delivery_company){
        ResBody resBody = new ResBody();
        int i = service.refuseReturnDelivery(return_id, refuse_delivery_id, refuse_delivery_company);
        if (i == 1){
            resBody.setCode(200);
        }else{
            resBody.setCode(500);
        }
        return resBody;
    }

    // 用户端
    // 用户端根据退货单号获得退货条目详情
    @GetMapping("/wx/getReturnItemByReturnId")
    public ReturnItem getReturnItemByReturnId(@RequestParam String return_id, @RequestParam String user_openid){
        return service.getReturnItemByReturnId(return_id, user_openid);
    }
}
