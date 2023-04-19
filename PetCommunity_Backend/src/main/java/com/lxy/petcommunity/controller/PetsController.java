package com.lxy.petcommunity.controller;

import com.lxy.petcommunity.bean.Pets;
import com.lxy.petcommunity.bean.ResBody;
import com.lxy.petcommunity.service.PetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PetsController {
    @Autowired
    PetsService service;

    // 分页返回获取所有宠物条目到管理员端
    @GetMapping("/api/getAllPets")
    public ResBody getAllPets(@RequestParam int page,
                               @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = service.getAllCount();
        List<Pets> list= service.getAllPets(page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员手动添加宠物信息条目
    @PostMapping("/api/addPets")
    // @RequestBody注解，将前端传过来的json键值对中的键的名称与对象的成员变量的名字进行匹配并且赋值
    public ResBody addPets(@RequestBody Pets pets) {
        ResBody resBody = new ResBody();
        int i = service.addPets(pets);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("添加成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("添加失败");
        }
        return resBody;
    }

    // 管理员端添加轮播图
    @GetMapping("/api/updatePetsSwiperImages")
    public ResBody updatePetsSwiperImages(@RequestParam int pet_id, @RequestParam String swiper_images) {
        ResBody resBody = new ResBody();
        int i = service.updatePetsSwiperImages(pet_id, swiper_images);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("更换成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("更换失败");
        }
        return resBody;
    }

    // 管理员端修改宠物信息条目
    @PostMapping("/api/updatePets")
    public ResBody updatePets(@RequestBody Pets pets) {
        ResBody resBody = new ResBody();
        int i = service.updatePets(pets);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("修改成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("修改失败");
        }
        return resBody;
    }

    // 管理员端删除商品
    @GetMapping("/api/delPets")
    public ResBody delPets(@RequestParam int pet_id) {
        ResBody resBody = new ResBody();
        int i = service.delPets(pet_id);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("删除成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("删除失败");
        }
        return resBody;
    }

    // 管理员端根据关键字查找宠物信息条目
    @GetMapping("/api/findPets")
    public ResBody findPets(@RequestParam int page,
                                    @RequestParam int limit,
                                    @RequestParam String name) {
        ResBody resBody = new ResBody();
        List<Pets> list= service.findPets(page, limit, name);
        int count = service.getCountByName(name);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 用户端
    // 用户端获取所有宠物信息条目
    @GetMapping("/wx/getAllPets")
    public List getAllPets(){
        List list = service.getAllPets();
        return list;
    }

    // 根据id返回宠物信息条目到用户端
    @GetMapping("/wx/getPetsById")
    public Pets getPetsById(@RequestParam int pet_id){
        Pets pets = service.getPetsById(pet_id);
        return pets;
    }

}
