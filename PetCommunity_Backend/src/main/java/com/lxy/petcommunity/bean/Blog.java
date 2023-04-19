package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    Integer blog_id;  // 主键id
    String user_openid;  // 用户id
    String blog_content;  // 发布的文字内容
    String images;  // 发布的图片集的路径
    String location;  // 发布位置
    Date publish_time;  // 发布时间
    Integer likes;  // 点赞数
    Integer stars;  // 收藏数
    Integer blog_comment_count;  // 评论数
    // 非数据库字段
    User user;  // 发布博客的用户对象
    List<String> images_List;  // 图片路径列表，管理员前端不需要，小程序前端需要
    // 管理员端的业务以及用户端没有进入到博客详情页时不需要对于以下的成员变量进行赋值
    List<BlogComment> blog_father_commentList;  // 该博客的一级评论列表
}
