package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Blog;
import com.lxy.petcommunity.dao.BlogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    BlogDao dao;

    // 管理员端
    // 返回所有博客数量
    public int getBlogsCount() {
        return dao.getBlogsCount();
    }

    // 返回含有查找关键字的博客数量
    public int getBlogsCountByContent(String blog_content) {
        return dao.getBlogsCountByContent(blog_content);
    }

    // 管理员端获得所有博客，分页取数据
    public List<Blog> getAllBlogs(int page, int limit) {
        return dao.getAllBlogs(page, limit);
    }

    // 根据关键字查找博客返回到管理员端
    public List<Blog> findBlogByContent_admin(int page, int limit, String blog_content) {
        return dao.findBlogByContent_admin(page, limit, blog_content);
    }

    // 删除博客，管理员端和用户端都使用该函数进行删除
    public int delBlog(int blog_id) {
        return dao.delBlog(blog_id);
    }

    // 用户端
    // 社区首页，按点赞数量获取博客
    public List<Blog> getAllBlogsByLikes(int limit, int page, String searchContent) {
        return dao.getAllBlogsByLikes(limit, page, searchContent);
    }

    // 社区首页，按收藏数量获取博客
    public List<Blog> getAllBlogsByStars(int limit, int page, String searchContent) {
        return dao.getAllBlogsByStars(limit, page, searchContent);
    }

    // 社区首页，按最新时间获取博客
    public List<Blog> getAllBlogsByTime(int limit, int page, String searchContent) {
        return dao.getAllBlogsByTime(limit, page, searchContent);
    }

    // 社区首页，按关注的人获取博客
    public List<Blog> getAllBlogsByFollowing(int limit, int page, String user_openid, String searchContent) {
        return dao.getAllBlogsByFollowing(limit, page, user_openid, searchContent);
    }

    // 判断当前博客是否已经点赞过
    public boolean isLiked(String user_openid, int blog_id) {
        return dao.isLiked(user_openid, blog_id);
    }

    // 博客点赞
    public int likeBlog(String user_openid, int blog_id){
        return dao.likeBlog(user_openid, blog_id);
    }

    // 博客取消点赞
    public int cancelLikeBlog(String user_openid, int blog_id){
        return dao.cancelLikeBlog(user_openid, blog_id);
    }

    // 判断当前博客是否已经收藏过
    public boolean isStared(String user_openid, int blog_id) {
        return dao.isStared(user_openid, blog_id);
    }

    // 博客收藏
    public int starBlog(String user_openid, int blog_id){
        return dao.starBlog(user_openid, blog_id);
    }

    // 博客取消收藏
    public int cancelStarBlog(String user_openid, int blog_id){
        return dao.cancelStarBlog(user_openid, blog_id);
    }

    // 获取指定用户发布的所有博客
    public List<Blog> getPublishBlogsByUserOpenId(String user_openid, int limit, int page) {
        return dao.getPublishBlogsByUserOpenId(user_openid, limit, page);
    }

    // 获取指定用户收藏的所有博客
    public List<Blog> getStarsBlogsByUserOpenId(String user_openid) {
        return dao.getStarsBlogsByUserOpenId(user_openid);
    }

    // 获取指定用户点赞的所有博客
    public List<Blog> getLikesBlogsByUserOpenId(String user_openid) {
        return dao.getLikesBlogsByUserOpenId(user_openid);
    }

    // 用户发布博客
    public void publishBlog(String user_openid, String blog_content, String blog_images, String location){
        dao.publishBlog(user_openid, blog_content, blog_images, location);
    }

}
