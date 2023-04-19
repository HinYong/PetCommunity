package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Comment;
import com.lxy.petcommunity.dao.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDao dao;

    // 管理员端
    // 管理员端获取所有评论
    public List<Comment> getAllComments(int page, int limit) {
        return dao.getAllComments(page,limit);
    }

    // 管理员端查找某种商品的评论
    public List<Comment> findComment(int page, int limit, String name) {
        return dao.findComment(page,limit,name);
    }

    // 管理员端删除评论
    public int delComment(int id, int orderItem_id) {
        return dao.delComment(id, orderItem_id);
    }

    // 管理员端获取所有评论的数量
    public int getCount() {
        return dao.getCount();
    }

    // 管理员端查找某种商品的评论数量
    public int getCount(String name) {
        return dao.getCount(name);
    }

    // 用户端
    // 用户端获取某种商品的评论数
    public int getCommentCountByGoodsId(String id) {
        return dao.getCommentCountByGoodsId(id);
    }

    // 用户端获取某件商品的所有评论
    public List getAllCommentsByGoodsId(String id) {
        return dao.getAllCommentsByGoodsId(id);
    }

    // 用户端用户添加评论
    public void addComment(String openid, int goods_id, int orderitem_id, String content) {
        dao.addComment(openid,goods_id,orderitem_id,content);
    }

    // 用户端查看某一个订单项自己的评论
    public String checkComment(int orderitem_id) {
        return dao.checkComment(orderitem_id);
    }
}
