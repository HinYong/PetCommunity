package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.Follow;
import com.lxy.petcommunity.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {
    @Autowired
    JdbcTemplate template;
    @Autowired
    BlogDao blogDao;
    @Autowired
    FollowDao followDao;

    // 管理员端
    // 根据openid查找用户，判断是否为新用户
    public User selectById(String openid) {
        List<User> list = template.query("select * from `user` where open_id = ?" ,
                new Object[]{openid}, new BeanPropertyRowMapper(User.class));
        if (!list.isEmpty()){
            return list.get(0);
        }else{
            return null;
        }
    }

    // 如果是新用户，则用户注册后插入数据库
    public void insert(User user) {
        template.update("insert into `user` values(?,?,?,?,?,?,?,null)",
                user.getOpenId(),user.getCreateTime(),user.getLastVisitTime(),
                user.getSessionKey(),
                user.getAvatarUrl(),user.getGender(),user.getNickName());
    }

    // 用户登录后更新最近登录时间、昵称、头像
    public void updateById(User user) {
        template.update("update `user` set `last_visit_time` = ?, avatar_url = ?, nick_name = ? where open_id = ?",
                user.getLastVisitTime(),user.getAvatarUrl(),user.getNickName(),user.getOpenId());
    }

    // 管理员端获得所有用户数量
    public int getCount() {
        int count = template.queryForObject("select count(*) from `user`", Integer.class);
        return count;
    }

    // 管理员端获得所有用户
    public List<User> getUsers(int page, int limit) {
        List<User> list = template.query("select * from `user` limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(User.class));
        if (!list.isEmpty()){
            return list;
        }else{
            return null;
        }
    }

    // 管理员端获得指定openid的用户
    public List<User> getUserByOpenid(int page, int limit, String openid) {
        List<User> list = template.query("select * from `user` where open_id = '" + openid + "' limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(User.class));
        return list;
    }

    // 用户端
    // 根据用户id获取用户基本信息
    public User getUserInfoByOpenid(String openid) {
        List<User> list = template.query("select * from `user` where open_id = '" + openid + "'", new BeanPropertyRowMapper<>(User.class));
        if(!list.isEmpty()){
            User user = list.get(0);
            // 用户博客总的被点赞数
            user.setTotalLikeCount(blogDao.getTotalLikedCountByOpenId(openid));
            // 用户博客总的被收藏数
            user.setTotalStarCount(blogDao.getTotalStaredCountByOpenId(openid));
            // 用户发布的博客数量
            user.setTotalBlogCount(blogDao.getTotalBlogCountByOpenId(openid));
            // 用户首页不用将关注和粉丝列表传过去，只要数量就行
            // 用户的关注数量
            int followingcount = followDao.getFollowingCount(openid);
            user.setFollowingNumber(followingcount);
            // 用户的粉丝数量
            int followercount = followDao.getFollowerCount(openid);
            user.setFollowerNumber(followercount);
            return user;
        }
        else {
            return null;
        }
    }

    // 用户分页获取所有的关注用户列表
    public List<User> getFollowingListByOpenid(String user_openid, int limit, int page) {
        // 获取指定的用户所有关注用户的openid
        List<Follow> list = followDao.getAllFollowing(user_openid, limit, page);
        if(!list.isEmpty()){
            List<User> Followings = new ArrayList<>();
            // 进行用户对象赋值
            for(Follow follow: list){
                String target_user_openid = follow.getTarget_user_openid();
                List<User> l = template.query("select * from `user` where open_id = '" + target_user_openid + "'",
                        new BeanPropertyRowMapper<>(User.class));
                Followings.add(l.get(0));
            }
            return Followings;
        }
        else {
            return null;
        }
    }

    // 用户分页获取所有的粉丝列表
    public List<User> getFollowerListByOpenid(String user_openid, int limit, int page) {
        // 获取指定的用户所有粉丝的openid
        List<Follow> list = followDao.getAllFollower(user_openid, limit, page);
        if(!list.isEmpty()){
            List<User> Followers = new ArrayList<>();
            // 进行用户对象赋值
            for(Follow follow: list){
                String follower_openid = follow.getFollower_openid();
                List<User> l = template.query("select * from `user` where open_id = '" + follower_openid + "'",
                        new BeanPropertyRowMapper<>(User.class));
                Followers.add(l.get(0));
            }
            return Followers;
        }
        else {
            return null;
        }
    }

    // 用户通过用户名查找
    public List<User> findUserByName(int page, int limit, String name){
        List<User> list = template.query("select * from `user` where `nick_name` like '%"+name+"%' limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(User.class));
        if (!list.isEmpty()){
            return list;
        }else{
            return null;
        }
    }

    // 用户用公开账号查找
    public List<User> findUserByPublicId(int page, int limit, int public_id){
        List<User> list = template.query("select * from `user` where `public_id` = ? limit ?,?" ,new Object[]{public_id,(page-1)*limit,limit},
                new BeanPropertyRowMapper(User.class));
        if (!list.isEmpty()){
            return list;
        }else{
            return null;
        }
    }

}
