package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.BlogLikes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogLikesDao {
    @Autowired
    JdbcTemplate template;

    // 返回用户的点赞博客列表
    public List<BlogLikes> getLikesByOpenId(String user_openid){
        List<BlogLikes> list = template.query("select * from blog_likes where user_openid = '" + user_openid + "'",
                new BeanPropertyRowMapper<>(BlogLikes.class));
        if(!list.isEmpty()){
            return list;
        }
        else {
            return null;
        }
    }
}
