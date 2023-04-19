package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogComment {
    Integer blog_comment_id;  // 博客评论主键id
    Integer blog_id;  // 评论所属的博客id
    Integer father_comment_id;  // 该评论的父评论id，默认为空，空值代表一级评论，需要向sub_comment添加数据
    Integer reply_target_id;  // 该评论回复目标评论的id，默认为空
    String user_openid;  // 该评论所属用户id
    Integer grade;  // 评论的级数，1代表1级评论，2代表2级评论
    Integer likes;  // 评论点赞数量
    String comment_content;  // 评论内容
    Date comment_time;  // 评论时间
    // 非数据库字段
    User user;  // 发布评论的用户对象
    // 管理员端的业务不需要对于以下的成员变量进行赋值
    User reply_target_User;  // 该评论的回复评论所属用户，用于展示
    List<BlogComment> sub_commentList;  // 一级评论拥有的子评论列表，一级评论才会有
    boolean isLiked;  // 该评论当前用户是否有点赞过，用于前端展示爱心是否点亮
}
