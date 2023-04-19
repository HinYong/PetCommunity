package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    String openId;  // 微信用户openid
    Date createTime;  // 用户创建时间
    Date lastVisitTime;  // 用户最近登陆时间
    String sessionKey;  // 用户的sessionKey
    String avatarUrl;  // 用户头像的地址
    Integer gender;  // 用户性别
    String nickName;  // 用户昵称
    Integer public_id;  // 用户公开账号，用于搜索
    // 以下字段数据库中没有
    Integer followerNumber;  // 用户粉丝数量
    Integer followingNumber;  // 用户关注数量
    Integer totalBlogCount;  // 用户发布的博客数量，发博客时自增
    Integer totalLikeCount;  // 用户博客总的点赞数
    Integer totalStarCount;  // 用户博客总的收藏数
    List<User> followerList;  // 用户的粉丝列表
    List<User> followingList;  // 用户的关注列表
}
