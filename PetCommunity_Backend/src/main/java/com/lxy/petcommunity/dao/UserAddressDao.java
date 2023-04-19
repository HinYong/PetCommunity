package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserAddressDao {
    @Autowired
    JdbcTemplate template;

    // 用户端
    // 获取该用户的所有地址
    public List getUserAddress(String openid) {
        List<UserAddress> list = template.query("select * from `useraddress` where user_openid = '"+openid+"' order by is_default desc"  ,
                new BeanPropertyRowMapper(UserAddress.class));
        if (list.size()!=0){
            return list;
        }else{
            return null;
        }
    }

    // 获取用户的默认地址
    public UserAddress getDefaultUserAddress(String openid) {
        List<UserAddress> list = template.query("select * from `useraddress` where user_openid = '"+openid+"' order by is_default desc"  ,
                new BeanPropertyRowMapper(UserAddress.class));
        if (list.size()!=0){
            return list.get(0);
        }else{
            return null;
        }
    }

    // 根据用户id以及地址的id获取相应地址
    public UserAddress getUserAddressById(String openid, int address_id) {
        List<UserAddress> list = template.query("select * from `useraddress` where user_openid = '"+openid+"' and id = " + address_id ,
                new BeanPropertyRowMapper(UserAddress.class));
        if (list.size()!=0){
            return list.get(0);
        }else{
            return null;
        }
    }

    // 取消默认地址
    public int cancelDefaultAddress(String openid){
        return template.update("update useraddress set is_default = 0 where is_default=1 and user_openid ='"+openid+"'");
    }

    // 用户新建地址
    public int createUserAddress(String openid, String receiver_name, String receiver_phone, String province, String city, String area, String detail_address, int is_default) {
        if(is_default==1){
            cancelDefaultAddress(openid);
        }
        return template.update("insert into `useraddress` values(null,?,?,?,?,?,?,?,?)",
                openid, receiver_name, receiver_phone, province, city, area, detail_address, is_default);
    }

    // 用户编辑地址
    public int editUserAddress(String openid, int id, String receiver_name, String receiver_phone, String province, String city, String area, String detail_address, int is_default) {
        if(is_default==1){
            cancelDefaultAddress(openid);
        }
        return template.update("update `useraddress` set receiver_name=?, receiver_phone=?, province=?, city=?, area=?, detail_address=?, is_default=? where user_openid=? and id=?",
                receiver_name, receiver_phone, province, city, area, detail_address, is_default, openid, id);
    }

    // 用户删除地址，只是将userid置为空，代表该条目无人所属，但是还会在数据库中存在，因为有订单关联
    public int delUserAddress(String openid, int addressid) {
        return template.update("update `useraddress` set user_openid = null where user_openid = ? and id = ?",openid, addressid);
    }

}
