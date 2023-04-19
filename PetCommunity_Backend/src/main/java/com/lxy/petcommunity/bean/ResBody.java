package com.lxy.petcommunity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResBody<T> {
    public Integer code;  // http状态码
    public String msg;  // 信息提示
    public long count;  // 数据条目数量
    public List<T> data = new ArrayList<T>();  // 查询的数据列表
}