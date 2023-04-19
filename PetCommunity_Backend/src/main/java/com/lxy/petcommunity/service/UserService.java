package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Follow;
import com.lxy.petcommunity.bean.User;
import com.lxy.petcommunity.dao.FollowDao;
import com.lxy.petcommunity.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDao dao;
    @Autowired
    FollowDao followDao;

    // 管理员端
    // 管理员端获得所有用户数量
    public int getCount() {
        return dao.getCount();
    }

    // 管理员端获得所有用户
    public List<User> getUsers(int page, int limit) {
        return dao.getUsers(page,limit);
    }

    // 管理员端获得指定openid的用户
    public List<User> getUserByOpenid(int page, int limit, String openid) {
        return dao.getUserByOpenid(page,limit,openid);
    }

    // 用户端
    // 根据openid查找用户，判断是否为新用户
    public User selectById(String openid) {
        return dao.selectById(openid);
    }

    // 用户注册后插入数据库
    public void insert(User user) {
        dao.insert(user);
    }

    // 用户登录后更新最近登录时间、昵称、头像
    public void updateById(User user) {
        dao.updateById(user);
    }

    // 将用户对象返回到用户主页
    public User getUserInfoByOpenid(String openid) {
        return dao.getUserInfoByOpenid(openid);
    }

    // 用户获取所有的关注用户列表
    public List<User> getFollowingListByOpenid(String user_openid, int limit, int page) {
        return dao.getFollowingListByOpenid(user_openid, limit, page);
    }

    // 用户获取所有的粉丝列表
    public List<User> getFollowerListByOpenid(String user_openid, int limit, int page) {
        return dao.getFollowerListByOpenid(user_openid, limit, page);
    }

    // 根据用户昵称查找用户
    public List<User> findUserByName(int page, int limit, String name){
        return dao.findUserByName(page, limit, name);
    }

    // 根据用户公开账号查找用户
    public List<User> findUserByPublicId(int page, int limit, int public_id){
        return dao.findUserByPublicId(page, limit, public_id);
    }

    // 检查某用户是否为当前用户的关注
    public boolean checkIsFollowing(String myopenid, String othersopenid){
        return followDao.checkIsFollowing(myopenid, othersopenid);
    }

    // 关注
    public int Following(String myopenid, String othersopenid){
        return followDao.Following(myopenid, othersopenid);
    }

    // 取消关注
    public int cancelFollowing(String myopenid, String othersopenid){
        return followDao.cancelFollowing(myopenid, othersopenid);
    }
}
