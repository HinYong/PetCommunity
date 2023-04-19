package com.lxy.petcommunity.controller;

import com.lxy.petcommunity.bean.ResBody;
import com.lxy.petcommunity.bean.Type;
import com.lxy.petcommunity.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class TypeController {
    @Autowired
    TypeService service;

    // 管理员端
    // 管理员添加新的商品类型
    @PostMapping("/api/addType")
    public ResBody addType(@RequestBody Type type) {
        ResBody resBody = new ResBody();
        int i = service.addType(type);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("添加成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("添加失败");
        }
        return resBody;
    }

    // 管理员更新商品类型
    @PostMapping("/api/updateType")
    public ResBody updateType(@RequestBody Type type) {
        ResBody resBody = new ResBody();
        int i = service.updateType(type);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("修改成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("修改失败");
        }
        return resBody;
    }

    // 管理员删除商品类型
    @GetMapping("/api/delType")
    public ResBody delType(@RequestParam int id) {
        ResBody resBody = new ResBody();
        int i = service.delType(id);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("删除成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("删除失败");
        }
        return resBody;
    }

    // 根据关键字查找类型并且分页返回到管理员端
    @GetMapping("/api/findType")
    public ResBody findType(@RequestParam int page,
                                @RequestParam int limit,
                                @RequestParam String name) {
        ResBody resBody = new ResBody();
        int count = service.getCount(name);
        List<Type> list= service.findType(page, limit, name);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端分页显示所有商品类型
    @GetMapping("/api/getAllTypes_limit")
    public ResBody getAllTypes(@RequestParam int page,
                               @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = service.getCount();
        List<Type> list= service.getAllTypes_limit(page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 用于编辑页面的下拉菜单显示所有商品类型
    @GetMapping("/api/getAllTypes")
    public ResBody getAllTypesForEdit() {
        ResBody resBody = new ResBody();
        List<Type> list= service.getAllTypes();
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 用户端
    // 将所有商品类型返回到用户端
    @GetMapping("/wx/getAllTypes")
    public List getAllTypesForUser() {
        List<Type> list= service.getAllTypes();
        List result = new ArrayList();
        for (Type type:list){
            HashMap map = new HashMap();
            map.put("id",type.getId());
            map.put("title",type.getName());
            // children列表用于前端装载属于该类型的商品
            map.put("children",new ArrayList<>());
            result.add(map);
        }
        return result;
    }
}
