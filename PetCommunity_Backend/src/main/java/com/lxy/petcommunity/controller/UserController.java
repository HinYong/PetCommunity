package com.lxy.petcommunity.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxy.petcommunity.bean.ResBody;
import com.lxy.petcommunity.bean.User;
import com.lxy.petcommunity.service.UserService;
import com.lxy.petcommunity.util.WechatUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService service;

    // 管理员端
    // 管理员端获得所有用户
    @GetMapping("/api/getUsers")
    public ResBody getUsers(@RequestParam int page,
                               @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = service.getCount();
        List<User> list= service.getUsers(page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端根据用户id查找用户并且分页返回
    @GetMapping("/api/getUserByOpenid")
    public ResBody getUserByOpenid(@RequestParam int page,
                                    @RequestParam int limit,
                                    @RequestParam String openid) {
        ResBody resBody = new ResBody();
        List<User> list = service.getUserByOpenid(page, limit, openid);
        resBody.setCount(list.size());
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 用户端
    // 用户端登录并且获取openid
    @GetMapping("wx/login")
    public ResBody user_login(@RequestParam(value = "code", required = false) String code,
                              @RequestParam(value = "rawData", required = false) String rawData,
                              @RequestParam(value = "signature", required = false) String signature,
                              @RequestParam(value = "encrypteData", required = false) String encrypteData,
                              @RequestParam(value = "iv", required = false) String iv) {
        // 用户非敏感信息：rawData
        // 签名：signature
        JSONObject rawDataJson = JSON.parseObject(rawData);
        // 1.接收小程序发送的code
        // 2.开发者服务器 登录凭证校验接口 appi + appsecret + code
        JSONObject SessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(code);
        // 3.接收微信接口服务 获取返回的参数
        String openid = SessionKeyOpenId.getString("openid");
        String sessionKey = SessionKeyOpenId.getString("session_key");
        // 4.校验签名 小程序发送的签名signature与服务器端生成的签名signature2 = sha1(rawData + sessionKey)
        String signature2 = DigestUtils.sha1Hex(rawData + sessionKey);
        //System.out.println(signature);
        //System.out.println(signature2);
        System.out.println(openid);
        //if (!signature.equals(signature2)) {
        //    return new ResBody(500, "签名校验失败",0, null);
        //}
        // 5.根据返回的User实体类，判断用户是否是新用户，是的话，将用户信息存到数据库；不是的话，更新最新登录时间
        User user = service.selectById(openid);
        if (user == null) {
            // 用户信息入库
            String nickName = rawDataJson.getString("nickName");
            String avatarUrl = rawDataJson.getString("avatarUrl");
            String gender = rawDataJson.getString("gender");

            user = new User();
            user.setOpenId(openid);
            user.setCreateTime(new Date());
            user.setLastVisitTime(new Date());
            user.setSessionKey(sessionKey);
            user.setAvatarUrl(avatarUrl);
            user.setGender(Integer.parseInt(gender));
            user.setNickName(nickName);

            service.insert(user);
        } else {
            // 已存在，更新用户登录时间、昵称、头像
            user.setLastVisitTime(new Date());
            service.updateById(user);
        }
        ResBody result = new ResBody(200, openid, 0, null );
        return result;
    }

    // 获取用户基本信息
    @GetMapping("wx/getUserInfoByOpenid")
    public User getUserInfoByOpenid(@RequestParam String user_openid){
        User user = service.getUserInfoByOpenid(user_openid);
        return user;
    }

    // 用户获取所有的关注用户列表
    @GetMapping("wx/getFollowingListByOpenid")
    public List<User> getFollowingListByOpenid(@RequestParam String user_openid,
                                               @RequestParam int limit,
                                               @RequestParam int page) {
        return service.getFollowingListByOpenid(user_openid, limit, page);
    }

    // 用户获取所有的粉丝列表
    @GetMapping("wx/getFollowerListByOpenid")
    public List<User> getFollowerListByOpenid(@RequestParam String user_openid,
                                              @RequestParam int limit,
                                              @RequestParam int page) {
        return service.getFollowerListByOpenid(user_openid, limit, page);
    }

    // 根据用户昵称查找用户
    @GetMapping("/wx/findUserByName")
    public List<User> findUserByName(@RequestParam int page,
                                     @RequestParam int limit,
                                     @RequestParam String name) {
        List<User> list= service.findUserByName(page, limit, name);
        return list;
    }

    // 根据用户公共id查找用户
    @GetMapping("/wx/findUserByPublicId")
    public List<User> findUserByPublicId(@RequestParam int page,
                                         @RequestParam int limit,
                                         @RequestParam int public_id) {
        List<User> list= service.findUserByPublicId(page, limit, public_id);
        return list;
    }

    // 检查某用户是否为当前用户的关注
    @GetMapping("/wx/checkIsFollowing")
    public boolean checkIsFollowing(@RequestParam String myopenid, @RequestParam String othersopenid){
        return service.checkIsFollowing(myopenid, othersopenid);
    }

    // 关注
    @GetMapping("/wx/Following")
    public int Following(@RequestParam String myopenid, @RequestParam String othersopenid){
        if(service.Following(myopenid, othersopenid)!=0){
            return 200;
        }
        else {
            return 500;
        }
    }

    // 取消关注
    @GetMapping("/wx/cancelFollowing")
    public int cancelFollowing(@RequestParam String myopenid, @RequestParam String othersopenid){
        if(service.cancelFollowing(myopenid, othersopenid)!=0){
            return 200;
        }
        else {
            return 500;
        }
    }
}
