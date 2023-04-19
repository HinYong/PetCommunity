package com.lxy.petcommunity.controller;

import com.lxy.petcommunity.bean.Blog;
import com.lxy.petcommunity.bean.BlogComment;
import com.lxy.petcommunity.bean.Comment;
import com.lxy.petcommunity.bean.ResBody;
import com.lxy.petcommunity.service.BlogCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BlogCommentController {

    @Autowired
    BlogCommentService service;

    // 管理员端
    // 管理员端获取所有评论
    @GetMapping("/api/getAllBlogComments")
    public ResBody getAllBlogComments(@RequestParam int page,
                                  @RequestParam int limit){
        ResBody resBody = new ResBody();
        int count = service.getBlogCommentsCount();
        List<BlogComment> list= service.getAllBlogComments(page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端查找含有某些关键字的评论
    @GetMapping("/api/findBlogCommentsByContent")
    public ResBody findBlogCommentsByContent(@RequestParam int page,
                                             @RequestParam int limit,
                                             @RequestParam String comment_content) {
        ResBody resBody = new ResBody();
        int count = service.getBlogCommentsCountByContent(comment_content);
        List<BlogComment> list= service.findBlogCommentsByContent(page, limit, comment_content);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端删除评论
    @GetMapping("/api/delBlogComment")
    public ResBody delBlogComment_admin(@RequestParam int blog_id, @RequestParam int blog_comment_id, @RequestParam int grade) {
        ResBody resBody = new ResBody();
        int i = service.delBlogComment(blog_id, blog_comment_id, grade);
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
    // 分页返回指定博客号的所有一级评论
    @GetMapping("/wx/getAllFatherBlogCommentsByBlogId")
    public List<BlogComment> getAllFatherBlogCommentsByBlogId(@RequestParam int page,
                                                              @RequestParam int limit,
                                                              @RequestParam int blog_id,
                                                              @RequestParam String user_openid){
        return service.getAllFatherBlogCommentsByBlogId(page, limit, blog_id, user_openid);
    }

    // 用户发表一级评论，并且将发表的评论对象返回前端
    @GetMapping("/wx/publishFatherComment")
    public BlogComment publishFatherComment(@RequestParam String user_openid,
                                            @RequestParam int blog_id,
                                            @RequestParam String comment_content){
        return service.publishFatherComment(user_openid, blog_id, comment_content);
    }

    // 用户发表回复一级评论的二级评论，并且将发表的评论对象返回前端
    // father_comment_id为所属一级评论的id，没有reply_target_id
    @GetMapping("/wx/publishSubComment")
    public BlogComment publishSubComment(@RequestParam String user_openid,
                                 @RequestParam int blog_id,
                                 @RequestParam String comment_content,
                                 @RequestParam int father_comment_id){
        return service.publishSubComment(user_openid, blog_id, comment_content, father_comment_id);
    }

    // 用户发表回复二级评论的二级评论，并且将发表的评论对象返回前端
    // father_comment_id为所属一级评论的id，reply_target_id为所回复评论的id
    @GetMapping("/wx/replySubComment")
    public BlogComment replySubComment(@RequestParam String user_openid,
                                         @RequestParam int blog_id,
                                         @RequestParam String comment_content,
                                         @RequestParam int father_comment_id,
                                         @RequestParam int reply_target_id,
                                         @RequestParam String reply_target_userid){
        return service.replySubComment(user_openid, blog_id, comment_content, father_comment_id, reply_target_id, reply_target_userid);
    }

    // 用户点赞评论
    @GetMapping("/wx/likeBlogComment")
    public int likeBlogComment(@RequestParam String user_openid, @RequestParam int blog_comment_id){
        int i = service.likeBlogComment(user_openid, blog_comment_id);
        if (i == 1){
            return 200;
        }else{
            return 500;
        }
    }

    // 用户取消点赞评论
    @GetMapping("/wx/cancelLikeBlogComment")
    public int cancelLikeBlogComment(@RequestParam String user_openid, @RequestParam int blog_comment_id){
        int i = service.cancelLikeBlogComment(user_openid, blog_comment_id);
        if (i == 1){
            return 200;
        }else{
            return 500;
        }
    }

    // 用户端删除评论
    @GetMapping("/wx/delBlogComment")
    public int delBlogComment(@RequestParam int blog_id, @RequestParam int blog_comment_id, @RequestParam int grade) {
        int i = service.delBlogComment(blog_id, blog_comment_id, grade);
        if (i == 1){
            return 200;
        }else{
            return 500;
        }
    }

}
