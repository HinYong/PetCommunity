package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Blog;
import com.lxy.petcommunity.bean.BlogComment;
import com.lxy.petcommunity.dao.BlogCommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogCommentService {
    @Autowired
    BlogCommentDao dao;

    // 管理员端
    // 返回所有的评论数量
    public int getBlogCommentsCount(){
        return dao.getBlogCommentsCount();
    }

     // 返回指定博客编号的所有的评论数量
    public int getBlogCommentsCountByContent(String comment_content){
        return dao.getBlogCommentsCountByContent(comment_content);
    }

    // 管理员端获取所有评论
    public List<BlogComment> getAllBlogComments(int page, int limit) {
        return dao.getAllBlogComments(page,limit);
    }

    // 管理员端查找含有某些关键字的评论
    public List<BlogComment> findBlogCommentsByContent(int page, int limit, String comment_content) {
        return dao.findBlogCommentsByContent(page,limit,comment_content);
    }

    // 删除评论，管理员端和用户端共用
    public int delBlogComment(int blog_id, int blog_comment_id, int grade){
        return dao.delBlogComment(blog_id, blog_comment_id, grade);
    }

    // 用户端
    // 分页返回指定博客号的所有一级评论
    public List<BlogComment> getAllFatherBlogCommentsByBlogId(int page, int limit, int blog_id, String user_openid){
        return dao.getAllFatherBlogCommentsByBlogId(page, limit, blog_id, user_openid);
    }

    // 用户发表一级评论，并且将评论对象返回前端
    public BlogComment publishFatherComment(String user_openid, int blog_id, String comment_content) {
        return dao.publishFatherComment(user_openid, blog_id, comment_content);
    }

    // 用户发表回复一级评论的二级评论，并且将发表的评论对象返回前端
    // father_comment_id为所属一级评论的id，没有reply_target_id
    public BlogComment publishSubComment(String user_openid, int blog_id, String comment_content, int father_comment_id) {
        return dao.publishSubComment(user_openid, blog_id, comment_content, father_comment_id);
    }

    // 用户发表回复二级评论的二级评论，并且将发表的评论对象返回前端
    // father_comment_id为所属一级评论的id，reply_target_id为所回复评论的id
    public BlogComment replySubComment(String user_openid, int blog_id, String comment_content, int father_comment_id, int reply_target_id,String reply_target_userid) {
        return dao.replySubComment(user_openid, blog_id, comment_content, father_comment_id, reply_target_id, reply_target_userid);
    }

    // 用户点赞评论
    public int likeBlogComment(String user_openid, int blog_comment_id){
        return dao.likeBlogComment(user_openid, blog_comment_id);
    }

    // 用户取消点赞评论
    public int cancelLikeBlogComment(String user_openid, int blog_comment_id){
        return dao.cancelLikeBlogComment(user_openid, blog_comment_id);
    }
}
