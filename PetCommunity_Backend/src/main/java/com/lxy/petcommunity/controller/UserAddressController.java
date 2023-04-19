package com.lxy.petcommunity.controller;

import com.lxy.petcommunity.bean.Order;
import com.lxy.petcommunity.bean.ResBody;
import com.lxy.petcommunity.bean.UserAddress;
import com.lxy.petcommunity.service.OrderService;
import com.lxy.petcommunity.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserAddressController {
    @Autowired
    UserAddressService service;

    // 用户端获取所有地址
    @GetMapping("/wx/getUserAddress")
    public List getUserAddress(@RequestParam String openid){
        List list = service.getUserAddress(openid);
        return list;
    }

    // 用户端获取默认地址
    @GetMapping("/wx/getDefaultUserAddress")
    public UserAddress getDefaultUserAddress(@RequestParam String openid){
        return service.getDefaultUserAddress(openid);
    }

    // 用户端获取指定地址
    @GetMapping("/wx/getUserAddressById")
    public UserAddress getUserAddressById(@RequestParam String openid, @RequestParam int address_id){
        return service.getUserAddressById(openid, address_id);
    }

    // 用户端新增地址
    @GetMapping("/wx/createUserAddress")
    public int createUserAddress(@RequestParam String openid,
                                 @RequestParam String receiver_name,
                                 @RequestParam String receiver_phone,
                                 @RequestParam String province,
                                 @RequestParam String city,
                                 @RequestParam String area,
                                 @RequestParam String detail_address,
                                 @RequestParam int is_default){
        return service.createUserAddress(openid, receiver_name, receiver_phone, province, city, area, detail_address, is_default);
    }

    // 用户端编辑地址
    @GetMapping("/wx/editUserAddress")
    public int editUserAddress(@RequestParam String openid,
                               @RequestParam int id,
                               @RequestParam String receiver_name,
                               @RequestParam String receiver_phone,
                               @RequestParam String province,
                               @RequestParam String city,
                               @RequestParam String area,
                               @RequestParam String detail_address,
                               @RequestParam int is_default){
        return service.editUserAddress(openid, id, receiver_name, receiver_phone, province, city, area, detail_address, is_default);
    }

    // 用户端删除地址
    @GetMapping("/wx/delUserAddress")
    public int delUserAddress(@RequestParam String openid, @RequestParam int addressid){
        return service.delUserAddress(openid, addressid);
    }

}
