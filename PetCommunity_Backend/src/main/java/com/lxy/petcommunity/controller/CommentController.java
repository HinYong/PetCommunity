package com.lxy.petcommunity.controller;

import com.lxy.petcommunity.bean.Comment;
import com.lxy.petcommunity.bean.ResBody;
import com.lxy.petcommunity.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    CommentService service;

    // 管理员端
    // 管理员端获取所有评论
    @GetMapping("/api/getAllComments")
    public ResBody getAllComments(@RequestParam int page,
                                  @RequestParam int limit){
        ResBody resBody = new ResBody();
        int count = service.getCount();
        List<Comment> list= service.getAllComments(page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端查找某种商品的评论
    @GetMapping("/api/findComment")
    public ResBody findComment(@RequestParam int page,
                             @RequestParam int limit,
                             @RequestParam String name) {
        ResBody resBody = new ResBody();
        int count = service.getCount(name);
        List<Comment> list= service.findComment(page, limit, name);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端删除评论
    @GetMapping("/api/delComment")
    public ResBody delComment(@RequestParam int id, @RequestParam int orderItem_id) {
        ResBody resBody = new ResBody();
        int i = service.delComment(id, orderItem_id);
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
    // 用户端获取某种商品的评论数
    @GetMapping("/wx/getCommentCountByGoodsId")
    public int getCommentCountByGoodsId(@RequestParam String id){
        int count = service.getCommentCountByGoodsId(id);
        return count;
    }

    // 用户端获取某件商品的所有评论
    @GetMapping("/wx/getAllCommentsByGoodsId")
    public List getAllCommentsByGoodsId(@RequestParam String id){
        List list = service.getAllCommentsByGoodsId(id);
        return list;
    }

    // 用户端用户添加评论
    @GetMapping("/wx/addComment")
    public int addComment(@RequestParam String openid, @RequestParam int goods_id, @RequestParam int orderitem_id,
                          @RequestParam String content){
        service.addComment(openid,goods_id,orderitem_id,content);
        return 200;
    }

    // 用户端查看某一个订单项自己的评论
    @GetMapping("/wx/checkComment")
    public String checkComment(@RequestParam int orderitem_id){
        return service.checkComment(orderitem_id);
    }

}
