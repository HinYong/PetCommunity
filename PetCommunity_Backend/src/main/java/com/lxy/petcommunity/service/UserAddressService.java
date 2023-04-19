package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Order;
import com.lxy.petcommunity.bean.User;
import com.lxy.petcommunity.bean.UserAddress;
import com.lxy.petcommunity.dao.OrderDao;
import com.lxy.petcommunity.dao.UserAddressDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAddressService {
    @Autowired
    UserAddressDao dao;

    // 用户端
    // 获取该用户的所有地址
    public List getUserAddress(String openid) {
        return dao.getUserAddress(openid);
    }

    // 获取用户的默认地址
    public UserAddress getDefaultUserAddress(String openid) {
        return dao.getDefaultUserAddress(openid);
    }

    // 根据用户id以及地址的id获取相应地址
    public UserAddress getUserAddressById(String openid, int address_id) {
        return dao.getUserAddressById(openid, address_id);
    }

    // 用户新建地址
    public int createUserAddress(String openid, String receiver_name, String receiver_phone, String province, String city, String area, String detail_address, int is_default) {
        return dao.createUserAddress(openid, receiver_name, receiver_phone, province, city, area, detail_address, is_default);
    }

    // 用户编辑地址
    public int editUserAddress(String openid, int id, String receiver_name, String receiver_phone, String province, String city, String area, String detail_address, int is_default) {
        return dao.editUserAddress(openid, id, receiver_name, receiver_phone, province, city, area, detail_address, is_default);
    }

    // 用户删除地址，只是将userid置为空，代表该条目无人所属，但是还会在数据库中存在，因为有订单关联
    public int delUserAddress(String openid, int addressid) {
        return dao.delUserAddress(openid, addressid);
    }
}
