package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.Follow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FollowDao {
    @Autowired
    JdbcTemplate template;

    // 返回某用户关注的用户数量
    public int getFollowingCount(String openid){
        return template.queryForObject("select count(*) from follow where follower_openid = '" + openid + "'", Integer.class);
    }

    // 返回某用户的粉丝数量
    public int getFollowerCount(String openid){
        return template.queryForObject("select count(*) from follow where target_user_openid = '" + openid + "'", Integer.class);
    }

    // 返回某用户所有关注
    public List<Follow> getAllFollowing(String user_openid, int limit, int page){
        return template.query("select * from follow where follower_openid = ? limit ?,?" ,new Object[]{user_openid,(page-1)*limit,limit},
                new BeanPropertyRowMapper(Follow.class));
    }

    // 返回某用户所有粉丝
    public List<Follow> getAllFollower(String user_openid, int limit, int page){
        return template.query("select * from follow where target_user_openid = ? limit ?,?" ,new Object[]{user_openid,(page-1)*limit,limit},
                new BeanPropertyRowMapper(Follow.class));
    }

    // 检查某用户是否为当前用户的关注
    public boolean checkIsFollowing(String myopenid, String othersopenid){
        List<Follow> list = template.query("select * from follow where follower_openid = '" + myopenid + "' and target_user_openid = '" + othersopenid + "'",
                new BeanPropertyRowMapper(Follow.class));
        if(!list.isEmpty()){
            return true;
        }
        else {
            return false;
        }
    }

    // 关注
    public int Following(String myopenid, String othersopenid){
        return template.update("insert into follow values(null,?,?)", myopenid, othersopenid);
    }

    // 取消关注
    public int cancelFollowing(String myopenid, String othersopenid){
        return template.update("delete from follow where follower_openid=? and target_user_openid=?", myopenid, othersopenid);
    }
}
