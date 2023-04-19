package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.Blog;
import com.lxy.petcommunity.bean.BlogComment;
import com.lxy.petcommunity.bean.BlogCommentLikes;
import com.lxy.petcommunity.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class BlogCommentDao {
    @Autowired
    JdbcTemplate template;

    // 管理员端
    // 返回所有的评论数量
    public int getBlogCommentsCount(){
        int count = template.queryForObject("select count(*) from `blog_comment`", Integer.class);
        return count;
    }

    // 返回含有某些关键字的评论数量
    public int getBlogCommentsCountByContent(String comment_content){
        int count = template.queryForObject("select count(*) from `blog_comment` where comment_content like '%" + comment_content + "%'", Integer.class);
        return count;
    }

    // 分页返回所有的评论到管理员端，不分一级还是二级
    // 因为管理员端只需要对于评论进行查看，对于有违规文字或者违规图片的评论进行删除，并不需要知道哪一条评论对应哪一条博客，也不需要管评论下面有什么其他二级评论
    public List<BlogComment> getAllBlogComments(int page,int limit){
        List<BlogComment> list;
        list = template.query("select * from `blog_comment` limit ?,?" ,new Object[]{(page-1)*limit,limit} ,
                new BeanPropertyRowMapper(BlogComment.class));
        if (list!=null&&list.size()!=0) {
            for (BlogComment blogComment : list) {
                // 设置评论的用户对象
                List<User> users = template.query("select * from `user` where open_id = '"+blogComment.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                blogComment.setUser(users.get(0));
            }
            return list;
        }
        else {
            return null;
        }
    }

    // 分页返回含有关键字内容的评论到管理员端
    // 因为管理员端只需要对于评论进行查看，对于有违规文字或者违规图片的评论进行删除，并不需要知道哪一条评论对应哪一条博客，也不需要管评论下面有什么其他二级评论
    public List<BlogComment> findBlogCommentsByContent(int page, int limit, String comment_content){
        List<BlogComment> list;
        list = template.query("select * from `blog_comment` where comment_content like '%" + comment_content + "%' limit ?,?" ,new Object[]{(page-1)*limit,limit} ,
                new BeanPropertyRowMapper(BlogComment.class));
        if (list!=null&&list.size()!=0) {
            for (BlogComment blogComment : list) {
                // 设置评论的用户对象
                List<User> users = template.query("select * from `user` where open_id = '"+blogComment.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                blogComment.setUser(users.get(0));
            }
            return list;
        }
        else {
            return null;
        }
    }

    // 删除评论，管理员端和用户端共用
    public int delBlogComment(int blog_id, int blog_comment_id, int grade){
        int delCount = 1;  // 本身自己一条
        // 若是一级评论则要删除他的所有二级评论
        if(grade==1){
            List<BlogComment> list = template.query("select * from `blog_comment` where father_comment_id = ?" ,new Object[]{blog_comment_id} ,
                    new BeanPropertyRowMapper(BlogComment.class));
            if(!list.isEmpty()){
                delCount+=list.size();
                for(BlogComment blogComment:list){
                    template.update("delete from blog_comment where blog_comment_id = ?", blogComment.getBlog_comment_id());
                }
            }
        }
        // 修改博客评论数目
        template.update("update blog set blog_comment_count = blog_comment_count - ? where blog_id = ?", delCount, blog_id);
        // 删除blog_comment_id评论
        return template.update("delete from blog_comment where blog_comment_id = ?", blog_comment_id);
    }

    // 用户端
    // 根据blog_id分页返回该博客的所有一级评论，每条一级评论的sub_commentList变量存了这条一级评论下的所有二级评论
    public List<BlogComment> getAllFatherBlogCommentsByBlogId(int page, int limit, int blog_id, String user_openid){
        List<BlogComment> list;
        // 获取所有一级评论
        list = template.query("select * from `blog_comment` where blog_id=? and grade = 1 limit ?,?" ,new Object[]{blog_id,(page-1)*limit,limit} ,
                new BeanPropertyRowMapper(BlogComment.class));
        if (list!=null&&list.size()!=0) {
            // 遍历所有一级评论
            for (BlogComment fatherBlogComment : list) {
                // 设置一级评论的用户对象
                List<User> users = template.query("select * from `user` where open_id = '"+fatherBlogComment.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                fatherBlogComment.setUser(users.get(0));
                // 设置该一级评论当前用户是否点赞过
                fatherBlogComment.setLiked(isCommentLiked(user_openid, fatherBlogComment.getBlog_comment_id()));
                // 设置子评论列表
                List<BlogComment> subBlogCommentList = template.query("select * from blog_comment where blog_id = " + blog_id + " and father_comment_id = " + fatherBlogComment.getBlog_comment_id(), new BeanPropertyRowMapper(BlogComment.class));
                if (!subBlogCommentList.isEmpty()) {
                    for (BlogComment subBlogComment : subBlogCommentList) {
                        // 设置子评论发布的用户对象
                        // 设置评论的用户对象
                        List<User> users1 = template.query("select * from `user` where open_id = '"+subBlogComment.getUser_openid()+"'"  ,
                                new BeanPropertyRowMapper(User.class));
                        subBlogComment.setUser(users1.get(0));
                        // 如果有回复其他评论，则设置回复目标评论所属的用户对象
                        if (subBlogComment.getReply_target_id() != null) {
                            List<BlogComment> replys = template.query("select * from blog_comment where blog_id = " + blog_id + " and blog_comment_id = " + subBlogComment.getReply_target_id(),
                                    new BeanPropertyRowMapper<>(BlogComment.class));
                            // 获得回复的评论对象
                            BlogComment reply_target = replys.get(0);
                            // 设置回复评论所属的用户对象
                            List<User> users2 = template.query("select * from `user` where open_id = '"+reply_target.getUser_openid()+"'"  ,
                                    new BeanPropertyRowMapper(User.class));
                            subBlogComment.setReply_target_User(users2.get(0));
                        }
                        // 设置子评论当前用户是否点赞过
                        subBlogComment.setLiked(isCommentLiked(user_openid, subBlogComment.getBlog_comment_id()));
                    }
                    // 设置子评论列表
                    fatherBlogComment.setSub_commentList(subBlogCommentList);
                }
            }
            return list;
        }
        else {
            return null;
        }
    }

    // 用户发表一级评论，并且将评论对象返回前端
    public BlogComment publishFatherComment(String user_openid, int blog_id, String comment_content) {
        // 修改博客的评论数量
        template.update("update blog set blog_comment_count = blog_comment_count +1 where blog_id = ?", blog_id);
        // 插入评论表，并且拿到插入的blog_comment_id
        String sql = "insert into `blog_comment` values(null,?,null,null,?,1,0,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Date date = new Date();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                // 指定主键
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"blog_comment_id"});
                preparedStatement.setInt(1, blog_id);
                preparedStatement.setString(2, user_openid);
                preparedStatement.setString(3, comment_content);
                preparedStatement.setObject(4, new java.sql.Timestamp(date.getTime()));
                return preparedStatement;
            }
        }, keyHolder);
        int blog_comment_id = keyHolder.getKey().intValue();
        // 创建要返回的blog_comment对象
        BlogComment blogComment = new BlogComment();
        blogComment.setBlog_comment_id(blog_comment_id);
        blogComment.setBlog_id(blog_id);
        blogComment.setUser_openid(user_openid);
        // 设置用户对象
        List<User> users = template.query("select * from `user` where open_id = '" + user_openid + "'",
                new BeanPropertyRowMapper<>(User.class));
        blogComment.setUser(users.get(0));
        blogComment.setGrade(1);
        blogComment.setLikes(0);
        blogComment.setComment_content(comment_content);
        blogComment.setComment_time(date);
        blogComment.setLiked(false);
        return blogComment;
    }

    // 用户发表回复一级评论的二级评论，并且将发表的评论对象返回前端
    // father_comment_id为所属一级评论的id，没有reply_target_id
    public BlogComment publishSubComment(String user_openid, int blog_id, String comment_content, int father_comment_id) {
        // 修改博客的评论数量
        template.update("update blog set blog_comment_count = blog_comment_count +1 where blog_id = ?", blog_id);
        // 插入评论表，并且拿到插入的blog_comment_id
        String sql = "insert into `blog_comment` values(null,?,?,null,?,2,0,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Date date = new Date();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                // 指定主键
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"blog_comment_id"});
                preparedStatement.setInt(1, blog_id);
                preparedStatement.setInt(2, father_comment_id);
                preparedStatement.setString(3, user_openid);
                preparedStatement.setString(4, comment_content);
                preparedStatement.setObject(5, new java.sql.Timestamp(date.getTime()));
                return preparedStatement;
            }
        }, keyHolder);
        int blog_comment_id = keyHolder.getKey().intValue();
        // 创建要返回的blog_comment对象
        BlogComment blogComment = new BlogComment();
        blogComment.setBlog_comment_id(blog_comment_id);
        blogComment.setBlog_id(blog_id);
        // 设置所属的一级评论id
        blogComment.setFather_comment_id(father_comment_id);
        // 设置用户对象
        blogComment.setUser_openid(user_openid);
        List<User> users = template.query("select * from `user` where open_id = '" + user_openid + "'",
                new BeanPropertyRowMapper<>(User.class));
        blogComment.setUser(users.get(0));
        blogComment.setGrade(2);
        blogComment.setLikes(0);
        blogComment.setComment_content(comment_content);
        blogComment.setComment_time(date);
        blogComment.setLiked(false);
        return blogComment;
    }

    // 用户发表回复二级评论的二级评论，并且将发表的评论对象返回前端
    // father_comment_id为所属一级评论的id，reply_target_id为所回复评论的id
    public BlogComment replySubComment(String user_openid, int blog_id, String comment_content, int father_comment_id, int reply_target_id, String reply_target_userid) {
        // 修改博客的评论数量
        template.update("update blog set blog_comment_count = blog_comment_count +1 where blog_id = ?", blog_id);
        // 插入评论表，并且拿到插入的blog_comment_id
        String sql = "insert into `blog_comment` values(null,?,?,?,?,2,0,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Date date = new Date();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                // 指定主键
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"blog_comment_id"});
                preparedStatement.setInt(1, blog_id);
                preparedStatement.setInt(2, father_comment_id);
                preparedStatement.setInt(3, reply_target_id);
                preparedStatement.setString(4, user_openid);
                preparedStatement.setString(5, comment_content);
                preparedStatement.setObject(6, new java.sql.Timestamp(date.getTime()));
                return preparedStatement;
            }
        }, keyHolder);
        int blog_comment_id = keyHolder.getKey().intValue();
        // 创建要返回的blog_comment对象
        BlogComment blogComment = new BlogComment();
        blogComment.setBlog_comment_id(blog_comment_id);
        blogComment.setBlog_id(blog_id);
        // 设置所属的一级评论id
        blogComment.setFather_comment_id(father_comment_id);
        // 设置用户对象
        blogComment.setUser_openid(user_openid);
        List<User> users = template.query("select * from `user` where open_id = '" + user_openid + "'",
                new BeanPropertyRowMapper<>(User.class));
        blogComment.setUser(users.get(0));
        // 设置所回复评论的blog_comment_id
        blogComment.setReply_target_id(reply_target_id);
        // 设置所回复的博客所属的用户对象
        List<User> reply_target_users = template.query("select * from `user` where open_id = '" + reply_target_userid + "'",
                new BeanPropertyRowMapper<>(User.class));
        blogComment.setReply_target_User(reply_target_users.get(0));
        blogComment.setGrade(2);
        blogComment.setLikes(0);
        blogComment.setComment_content(comment_content);
        blogComment.setComment_time(date);
        blogComment.setLiked(false);
        return blogComment;
    }

    // 判断一条评论当前用户是否已经点赞过
    public boolean isCommentLiked(String user_openid, int blog_comment_id) {
        List<BlogCommentLikes> list = template.query("select * from blog_comment_likes where blog_comment_id = " + blog_comment_id + " and user_openid='" + user_openid + "'",
                new BeanPropertyRowMapper<>(BlogCommentLikes.class));
        if(!list.isEmpty()){
            return true;
        }
        else {
            return false;
        }
    }

    // 用户点赞评论
    public int likeBlogComment(String user_openid, int blog_comment_id){
        // 插入评论点赞表
        template.update("insert into blog_comment_likes values (null,?,?)", user_openid, blog_comment_id);
        // 修改评论的点赞数量
        return template.update("update blog_comment set `likes` = `likes` + 1 where blog_comment_id = ?", blog_comment_id);
    }

    // 用户取消点赞评论
    public int cancelLikeBlogComment(String user_openid, int blog_comment_id){
        // 从评论点赞表删除
        template.update("delete from blog_comment_likes where user_openid = ? and blog_comment_id = ?", user_openid, blog_comment_id);
        // 修改评论的点赞数量
        return template.update("update blog_comment set `likes` = `likes` - 1 where blog_comment_id = ?", blog_comment_id);
    }
}
