package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogCommentLikes {
    Integer id;
    String user_openid;
    Integer blog_comment_id;
}
