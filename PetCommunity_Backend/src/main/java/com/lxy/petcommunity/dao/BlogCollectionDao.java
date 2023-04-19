package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.BlogCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogCollectionDao {
    @Autowired
    JdbcTemplate template;

    // 返回用户的收藏博客列表
    public List<BlogCollection> getCollectionByOpenId(String user_openid){
        List<BlogCollection> list = template.query("select * from blog_collection where user_openid = '" + user_openid + "'",
                new BeanPropertyRowMapper<>(BlogCollection.class));
        if(!list.isEmpty()){
            return list;
        }
        else {
            return null;
        }
    }
}
