package com.lxy.petcommunity.controller;

import com.lxy.petcommunity.bean.PetsType;
import com.lxy.petcommunity.bean.ResBody;
import com.lxy.petcommunity.service.PetsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class PetsTypeController {
    @Autowired
    PetsTypeService service;

    // 管理员端
    // 添加宠物类型
    @PostMapping("/api/addPetsType")
    public ResBody addPetsType(@RequestBody PetsType petsType) {
        ResBody resBody = new ResBody();
        int i = service.addPetsType(petsType);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("添加成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("添加失败");
        }
        return resBody;
    }

    // 编辑宠物类型
    @PostMapping("/api/updatePetsType")
    public ResBody updatePetsType(@RequestBody PetsType petsType) {
        ResBody resBody = new ResBody();
        int i = service.updatePetsType(petsType);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("修改成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("修改失败");
        }
        return resBody;
    }

    // 删除宠物类型
    @GetMapping("/api/delPetsType")
    public ResBody delPetsType(@RequestParam int pet_type_id) {
        ResBody resBody = new ResBody();
        int i = service.delPetsType(pet_type_id);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("删除成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("删除失败");
        }
        return resBody;
    }

    // 查找宠物类型
    @GetMapping("/api/findPetsType")
    public ResBody findPetsType(@RequestParam int page,
                                @RequestParam int limit,
                                @RequestParam String pet_type_name) {
        ResBody resBody = new ResBody();
        int count = service.getCount(pet_type_name);
        List<PetsType> list= service.findPetsType(page, limit, pet_type_name);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 分页显示所有类型调用的接口
    @GetMapping("/api/getAllPetsTypes_limit")
    public ResBody getAllPetsTypes_limit(@RequestParam int page,
                               @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = service.getCount();
        List<PetsType> list= service.getAllPetsTypes_limit(page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 用于编辑页面的下拉菜单显示所有宠物类型
    @GetMapping("/api/getAllPetsTypes")
    public ResBody getAllPetsTypesForEdit() {
        ResBody resBody = new ResBody();
        List<PetsType> list= service.getAllPetsTypes();
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 用户端
    @GetMapping("/wx/getAllPetsTypes")
    public List getAllPetsTypesForUser() {
        List<PetsType> list= service.getAllPetsTypes();
        List result = new ArrayList();
        for (PetsType petsType:list){
            HashMap map = new HashMap();
            map.put("id",petsType.getPet_type_id());
            map.put("title",petsType.getPet_type_name());
            // children列表用于前端装载属于该类型的商品
            map.put("children",new ArrayList<>());
            result.add(map);
        }
        return result;
    }
}
