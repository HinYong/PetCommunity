package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class BlogDao {
    @Autowired
    JdbcTemplate template;
    @Autowired
    BlogCommentDao blogCommentDao;
    @Autowired
    BlogLikesDao blogLikesDao;
    @Autowired
    BlogCollectionDao blogCollectionDao;

    // 管理员端
    // 返回所有博客数量
    public int getBlogsCount() {
        int count = template.queryForObject("select count(*) from `blog`", Integer.class);
        return count;
    }

    // 返回含有查找关键字的博客数量
    public int getBlogsCountByContent(String blog_content) {
        int count = template.queryForObject("select count(*) from `blog` where `blog_content` like '%" + blog_content + "%'", Integer.class);
        return count;
    }

    // 管理员端获得所有博客，分页取数据
    public List<Blog> getAllBlogs(int page,int limit) {
        List<Blog> list;
        list = template.query("select * from `blog` limit ?,?" ,new Object[]{(page-1)*limit,limit} ,
                    new BeanPropertyRowMapper(Blog.class));
        if (list!=null&&list.size()!=0){
            for (Blog blog:list){
                // 获取User对象
                List<User> users = template.query("select * from `user` where open_id = '"+blog.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                blog.setUser(users.get(0));
            }
            return list;
        }else{
            return null;
        }
    }

    // 根据关键字查找博客返回到管理员端
    public List<Blog> findBlogByContent_admin(int page, int limit, String blog_content) {
        List<Blog> list = template.query("select * from blog where `blog_content` like '%"+blog_content+"%' limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(Blog.class));
        if (!list.isEmpty()){
            for (Blog blog:list){
                // 获取User对象
                List<User> users = template.query("select * from `user` where open_id = '"+blog.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                blog.setUser(users.get(0));
            }
            return list;
        }else{
            return null;
        }
    }

    // 删除博客，需要删除文件夹的图片，管理员端和用户端都使用该函数进行删除
    public int delBlog(int blog_id) {
        // 获取上传图片路径
        String blog_images = template.queryForObject("select images from blog where blog_id = "+ blog_id, String.class);
        if(blog_images!=null){
            String[] blog_images_List = blog_images.split(",");
            // 删除之前上传的轮播图
            for(String s:blog_images_List){
                s = s.replace("http://localhost:8081/", "src/main/resources/static/");
                File file1 = new File(s);
                file1.delete();
            }
        }

        return template.update("DELETE from `blog` where blog_id = ?",blog_id);
    }

    // 用户端
    // 社区首页，按点赞数量获取博客，但是没有进入到详情页，所以暂时不需要对一级评论列表赋值
    public List<Blog> getAllBlogsByLikes(int limit, int page, String searchContent) {
        List<Blog> list;
        // 无搜索内容
        if (searchContent==null||searchContent.equals("")||searchContent.equals("null")){
             list = template.query("select * from `blog` order by `likes` desc limit ?,?", new Object[]{(page-1)*limit,limit},
                    new BeanPropertyRowMapper(Blog.class));
        }
        // 有搜索内容
        else {
            list = template.query("select * from `blog` where `blog_content` like '%" + searchContent + "%'  order by `likes` desc limit ?,?", new Object[]{(page-1)*limit,limit},
                    new BeanPropertyRowMapper(Blog.class));
        }
        if (list!=null&&list.size()!=0){
            for (Blog blog:list){
                // 设置用户对象
                List<User> users = template.query("select * from `user` where open_id = '"+blog.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                blog.setUser(users.get(0));
                // 设置图片集
                String images = blog.getImages();
                if(images!=null){
                    String[] images_List = images.split(",");
                    blog.setImages_List(Arrays.asList(images_List));
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 社区首页，按收藏数量获取博客，但是没有进入到详情页，所以暂时不需要对一级评论列表赋值
    public List<Blog> getAllBlogsByStars(int limit, int page, String searchContent) {
        List<Blog> list;
        // 无搜索内容
        if (searchContent==null||searchContent.equals("")||searchContent.equals("null")){
            list = template.query("select * from `blog` order by `stars` desc limit ?,?", new Object[]{(page-1)*limit,limit},
                    new BeanPropertyRowMapper(Blog.class));
        }
        // 有搜索内容
        else {
            list = template.query("select * from `blog` where `blog_content` like '%" + searchContent + "%' order by `stars` desc limit ?,?", new Object[]{(page-1)*limit,limit},
                    new BeanPropertyRowMapper(Blog.class));
        }
        if (list!=null&&list.size()!=0){
            for (Blog blog:list){
                // 设置用户对象
                List<User> users = template.query("select * from `user` where open_id = '"+blog.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                blog.setUser(users.get(0));
                // 设置图片集
                String images = blog.getImages();
                if(images!=null){
                    String[] images_List = images.split(",");
                    blog.setImages_List(Arrays.asList(images_List));
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 社区首页，按最新时间获取博客，但是没有进入到详情页，所以暂时不需要对一级评论列表赋值
    public List<Blog> getAllBlogsByTime(int limit, int page, String searchContent) {
        List<Blog> list;
        // 无搜索内容
        if (searchContent==null||searchContent.equals("")||searchContent.equals("null")){
            list = template.query("select * from `blog` order by `publish_time` desc limit ?,?", new Object[]{(page-1)*limit,limit},
                    new BeanPropertyRowMapper(Blog.class));
        }
        // 有搜索内容
        else {
            list = template.query("select * from `blog` where `blog_content` like '%" + searchContent + "%' order by `publish_time` desc limit ?,?", new Object[]{(page-1)*limit,limit},
                    new BeanPropertyRowMapper(Blog.class));
        }
        if (list!=null&&list.size()!=0){
            for (Blog blog:list){
                // 设置用户对象
                List<User> users = template.query("select * from `user` where open_id = '"+blog.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                blog.setUser(users.get(0));
                // 设置图片集
                String images = blog.getImages();
                if(images!=null){
                    String[] images_List = images.split(",");
                    blog.setImages_List(Arrays.asList(images_List));
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 社区首页，获取关注的人的博客，按时间顺序展示，但是没有进入到详情页，所以暂时不需要对一级评论列表赋值
    public List<Blog> getAllBlogsByFollowing(int limit, int page, String user_openid, String searchContent) {
        List<String> s = template.queryForList("select target_user_openid from follow where follower_openid = '" + user_openid + "'", String.class);
        if(!s.isEmpty()){
            // 筛选关注的人的openid并且进行字符串拼接
            String sql = "select * from `blog` where ";
            for(int i=0;i<s.size()-1;i++){
                sql += "user_openid = '" + s.get(i) + "' or ";
            }
            sql += "user_openid = '" + s.get(s.size()-1) + "' ";
            // 获取关注的人的博客，按时间顺序展示
            List<Blog> list;
            // 无搜索内容
            if (searchContent==null||searchContent.equals("")||searchContent.equals("null")){
                list = template.query(sql + "order by `publish_time` desc limit ?,?", new Object[]{(page-1)*limit,limit},
                        new BeanPropertyRowMapper(Blog.class));
            }
            // 有搜索内容
            else {
                list = template.query(sql + "and `blog_content` like '%" + searchContent + "%' order by `publish_time` desc limit ?,?", new Object[]{(page-1)*limit,limit},
                        new BeanPropertyRowMapper(Blog.class));
            }
            if (list!=null&&list.size()!=0){
                for (Blog blog:list){
                    // 设置用户对象
                    List<User> users = template.query("select * from `user` where open_id = '"+blog.getUser_openid()+"'"  ,
                            new BeanPropertyRowMapper(User.class));
                    blog.setUser(users.get(0));
                    // 设置图片集
                    String images = blog.getImages();
                    if(images!=null){
                        String[] images_List = images.split(",");
                        blog.setImages_List(Arrays.asList(images_List));
                    }
                }
                return list;
            }else{
                return null;
            }
        }
        else{
            return null;
        }
    }

    // 判断当前博客是否已经点赞过
    public boolean isLiked(String user_openid, int blog_id) {
        List<BlogLikes> list = template.query("select * from blog_likes where blog_id = "+blog_id+" and user_openid='" + user_openid + "'",
                new BeanPropertyRowMapper<>(BlogLikes.class));
        if(!list.isEmpty()){
            return true;
        }
        else {
            return false;
        }
    }

    // 博客点赞
    public int likeBlog(String user_openid, int blog_id){
        // 加入点赞表中
        template.update("insert into blog_likes values(null,?,?)",user_openid, blog_id);
        // 改变点赞数量
        return template.update("update blog set `likes` = `likes` + 1 where blog_id = ?", blog_id);
    }

    // 博客取消点赞
    public int cancelLikeBlog(String user_openid, int blog_id){
        // 从点赞表中删除
        template.update("delete from blog_likes where user_openid = ? and blog_id = ?", user_openid, blog_id);
        // 改变点赞数量
        return template.update("update blog set `likes` = `likes` - 1 where blog_id = ?", blog_id);
    }

    // 判断当前博客是否已经收藏过
    public boolean isStared(String user_openid, int blog_id) {
        List<BlogCollection> list = template.query("select * from blog_collection where blog_id = "+blog_id+" and user_openid='" + user_openid + "'",
                new BeanPropertyRowMapper<>(BlogCollection.class));
        if(!list.isEmpty()){
            return true;
        }
        else {
            return false;
        }
    }

    // 博客收藏
    public int starBlog(String user_openid, int blog_id){
        // 加入收藏表中
        template.update("insert into blog_collection values(null,?,?)",user_openid, blog_id);
        // 改变收藏数量
        return template.update("update blog set stars = stars + 1 where blog_id = ?", blog_id);
    }

    // 博客取消收藏
    public int cancelStarBlog(String user_openid, int blog_id){
        // 从收藏表中删除
        template.update("delete from blog_collection where user_openid = ? and blog_id = ?", user_openid, blog_id);
        // 改变收藏数量
        return template.update("update blog set stars = stars - 1 where blog_id = ?", blog_id);
    }

    // 获取用户发布的博客数量
    public int getTotalBlogCountByOpenId(String user_openid){
        return template.queryForObject("select count(*) from blog where user_openid = '" + user_openid + "'", Integer.class);
    }

    // 获取用户的总获赞数
    public int getTotalLikedCountByOpenId(String user_openid){
        List<Blog> list = template.query("select * from blog where user_openid = '" + user_openid + "'",
                new BeanPropertyRowMapper<>(Blog.class));
        int totalLikes = 0;
        if(!list.isEmpty()){
            for(Blog b:list){
                totalLikes+=b.getLikes();
            }
        }
        return totalLikes;
    }

    // 获取用户的总被收藏数
    public int getTotalStaredCountByOpenId(String user_openid){
        List<Blog> list = template.query("select * from blog where user_openid = '" + user_openid + "'",
                new BeanPropertyRowMapper<>(Blog.class));
        int totalStars = 0;
        if(!list.isEmpty()){
            for(Blog b:list){
                totalStars+=b.getStars();
            }
        }
        return totalStars;
    }

    // 获取指定用户发布的所有博客
    public List<Blog> getPublishBlogsByUserOpenId(String user_openid, int limit, int page) {
        List<Blog> list = template.query("select * from `blog` where user_openid = ? limit ?,?", new Object[]{user_openid,(page-1)*limit,limit},
                new BeanPropertyRowMapper(Blog.class));
        if (list!=null&&list.size()!=0){
            for (Blog blog:list){
                // 设置用户对象
                List<User> users = template.query("select * from `user` where open_id = '"+blog.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                blog.setUser(users.get(0));
                // 设置图片集
                String images = blog.getImages();
                if(images!=null){
                    String[] images_List = images.split(",");
                    blog.setImages_List(Arrays.asList(images_List));
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 获取指定用户收藏的所有博客
    public List<Blog> getStarsBlogsByUserOpenId(String user_openid) {
        List<BlogCollection> blogCollections = blogCollectionDao.getCollectionByOpenId(user_openid);
        if (blogCollections !=null){
            List<Blog> list = new ArrayList<>();
            // 遍历所有收藏的博客
            for (BlogCollection blogCollection : blogCollections){
                List<Blog> temp = template.query("select * from blog where blog_id = ?", new Object[]{blogCollection.getBlog_id()},
                        new BeanPropertyRowMapper(Blog.class));
                Blog blog = temp.get(0);
                // 设置用户对象
                List<User> users = template.query("select * from `user` where open_id = '"+blog.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                blog.setUser(users.get(0));
                // 设置图片集
                String images = blog.getImages();
                if(images!=null){
                    String[] images_List = images.split(",");
                    blog.setImages_List(Arrays.asList(images_List));
                }
                // 将当前博客加入返回的列表
                list.add(blog);
            }
            return list;
        }else{
            return null;
        }
    }

    // 获取指定用户点赞的所有博客
    public List<Blog> getLikesBlogsByUserOpenId(String user_openid) {
        List<BlogLikes> likes = blogLikesDao.getLikesByOpenId(user_openid);
        if (likes!=null){
            List<Blog> list = new ArrayList<>();
            // 遍历所有点赞的博客
            for (BlogLikes l:likes){
                List<Blog> temp = template.query("select * from blog where blog_id = ?", new Object[]{l.getBlog_id()},
                        new BeanPropertyRowMapper(Blog.class));
                Blog blog = temp.get(0);
                // 设置用户对象
                List<User> users = template.query("select * from `user` where open_id = '"+blog.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                blog.setUser(users.get(0));
                // 设置图片集
                String images = blog.getImages();
                if(images!=null){
                    String[] images_List = images.split(",");
                    blog.setImages_List(Arrays.asList(images_List));
                }
                // 将当前博客加入返回的列表
                list.add(blog);
            }
            return list;
        }else{
            return null;
        }
    }

    // 用户发布博客
    public void publishBlog(String user_openid, String blog_content, String blog_images, String location){
        if(blog_images.equals("")){
            blog_images = null;
        }
        if(location.equals("")){
            location = null;
        }
        template.update("insert into blog values (null,?,?,?,?,?,0,0,0)",
                user_openid, blog_content, blog_images, location, new Date());
    }
}
