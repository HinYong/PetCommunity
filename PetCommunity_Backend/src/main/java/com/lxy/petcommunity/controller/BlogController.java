package com.lxy.petcommunity.controller;

import com.lxy.petcommunity.bean.Blog;
import com.lxy.petcommunity.bean.ResBody;
import com.lxy.petcommunity.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BlogController {

    @Autowired
    BlogService service;

    // 管理员端
    // 管理员端获取所有博客
    @GetMapping("/api/getAllBlogs")
    public ResBody getAllBlogs(@RequestParam int page,
                               @RequestParam int limit){
        ResBody resBody = new ResBody();
        int count = service.getBlogsCount();
        List<Blog> list= service.getAllBlogs(page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 根据关键字查找博客返回到管理员端
    @GetMapping("/api/findBlogByContent")
    public ResBody findBlogByContent_admin(@RequestParam int page,
                                           @RequestParam int limit,
                                           @RequestParam String blog_content) {
        ResBody resBody = new ResBody();
        int count = service.getBlogsCountByContent(blog_content);
        List<Blog> list= service.findBlogByContent_admin(page, limit, blog_content);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端删除博客
    @GetMapping("/api/delBlog")
    public ResBody delBlog_admin(@RequestParam int blog_id) {
        ResBody resBody = new ResBody();
        int i = service.delBlog(blog_id);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("删除成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("删除失败");
        }
        return resBody;
    }

    // 用户端
    // 社区首页，按点赞数量分页获取博客
    @GetMapping("/wx/getAllBlogsByLikes")
    public List<Blog> getAllBlogsByLikes(@RequestParam int limit,
                                         @RequestParam int page,
                                         @RequestParam String searchContent){
        return service.getAllBlogsByLikes(limit, page, searchContent);
    }

    // 社区首页，按收藏数量分页获取博客
    @GetMapping("/wx/getAllBlogsByStars")
    public List<Blog> getAllBlogsByStars(@RequestParam int limit,
                                         @RequestParam int page,
                                         @RequestParam String searchContent){
        return service.getAllBlogsByStars(limit, page, searchContent);
    }

    // 社区首页，按最新时间分页获取博客
    @GetMapping("/wx/getAllBlogsByTime")
    public List<Blog> getAllBlogsByTime(@RequestParam int limit,
                                        @RequestParam int page,
                                        @RequestParam String searchContent){
        return service.getAllBlogsByTime(limit, page, searchContent);
    }

    // 社区首页，按时间获取关注的人的博客
    @GetMapping("/wx/getAllBlogsByFollowing")
    public List<Blog> getAllBlogsByFollowing(@RequestParam int limit,
                                             @RequestParam int page,
                                             @RequestParam String user_openid,
                                             @RequestParam String searchContent){
        return service.getAllBlogsByFollowing(limit, page, user_openid, searchContent);
    }

    // 判断当前博客是否已经点赞过
    @GetMapping("/wx/isLiked")
    public boolean isLiked(@RequestParam String user_openid,
                           @RequestParam int blog_id) {
        return service.isLiked(user_openid, blog_id);
    }

    // 用户点赞博客
    @GetMapping("/wx/likeBlog")
    public int likeBlog(@RequestParam String user_openid,
                        @RequestParam int blog_id) {
        int i = service.likeBlog(user_openid, blog_id);
        if (i == 1){
            return 200;
        }else{
            return 500;
        }
    }

    // 用户取消点赞博客
    @GetMapping("/wx/cancelLikeBlog")
    public int cancelLikeBlog(@RequestParam String user_openid,
                              @RequestParam int blog_id) {
        int i = service.cancelLikeBlog(user_openid, blog_id);
        if (i == 1){
            return 200;
        }else{
            return 500;
        }
    }

    // 判断当前博客是否已经收藏过
    @GetMapping("/wx/isStared")
    public boolean isStared(@RequestParam String user_openid,
                            @RequestParam int blog_id) {
        return service.isStared(user_openid, blog_id);
    }

    // 用户收藏博客
    @GetMapping("/wx/starBlog")
    public int starBlog(@RequestParam String user_openid,
                        @RequestParam int blog_id) {
        int i = service.starBlog(user_openid, blog_id);
        if (i == 1){
            return 200;
        }else{
            return 500;
        }
    }

    // 用户取消收藏博客
    @GetMapping("/wx/cancelStarBlog")
    public int cancelStarBlog(@RequestParam String user_openid,
                              @RequestParam int blog_id) {
        int i = service.cancelStarBlog(user_openid, blog_id);
        if (i == 1){
            return 200;
        }else{
            return 500;
        }
    }

    // 用户删除博客
    @GetMapping("/wx/delBlog")
    public int delBlog(@RequestParam int blog_id) {
        int i = service.delBlog(blog_id);
        if (i == 1){
            return 200;
        }else{
            return 500;
        }
    }

    // 获取指定用户发布的所有博客
    @GetMapping("/wx/getPublishBlogsByUserOpenId")
    public List<Blog> getPublishBlogsByUserOpenId(@RequestParam String user_openid,
                                                  @RequestParam int limit,
                                                  @RequestParam int page){
        return service.getPublishBlogsByUserOpenId(user_openid, limit, page);
    }

    // 获取指定用户收藏的所有博客
    @GetMapping("/wx/getStarsBlogsByUserOpenId")
    public List<Blog> getStarsBlogsByUserOpenId(@RequestParam String user_openid,
                                                @RequestParam int limit,
                                                @RequestParam int page){
        List<Blog> list = service.getStarsBlogsByUserOpenId(user_openid);
        if(list!=null){
            List<Blog> returnlist = new ArrayList<>();
            for(int i=(page-1)*limit;i<((page-1)*limit+limit)&&i<list.size();i++){
                returnlist.add(list.get(i));
            }
            return returnlist;
        }
        else {
            return null;
        }
    }

    // 获取指定用户点赞的所有博客
    @GetMapping("/wx/getLikesBlogsByUserOpenId")
    public List<Blog> getLikesBlogsByUserOpenId(@RequestParam String user_openid,
                                                @RequestParam int limit,
                                                @RequestParam int page){
        List<Blog> list = service.getLikesBlogsByUserOpenId(user_openid);
        if(list!=null){
            List<Blog> returnlist = new ArrayList<>();
            for(int i=(page-1)*limit;i<((page-1)*limit+limit)&&i<list.size();i++){
                returnlist.add(list.get(i));
            }
            return returnlist;
        }
        else {
            return null;
        }
    }

    // 用户发布博客
    @GetMapping("/wx/publishBlog")
    public void publishBlog(@RequestParam String user_openid,
                            @RequestParam String blog_content,
                            @RequestParam String blog_images,
                            @RequestParam String location){
        service.publishBlog(user_openid, blog_content, blog_images, location);
    }
}
