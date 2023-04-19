package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow {
    int follow_id;
    String follower_openid;
    String target_user_openid;
}
