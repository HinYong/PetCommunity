package com.lxy.petcommunity.controller;

import com.lxy.petcommunity.bean.Goods;
import com.lxy.petcommunity.bean.ResBody;
import com.lxy.petcommunity.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {
    @Autowired
    GoodsService service;

    // 管理员端
    // 管理员端获取所有商品
    @GetMapping("/api/getAllGoods")
    public ResBody getAllGoods(@RequestParam int page,
                               @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = service.getAllCount();
        List<Goods> list= service.getAllGoods(page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端获取所有上架商品
    @GetMapping("/api/getSellingGoods")
    public ResBody getSellingGoods(@RequestParam int page,
                               @RequestParam int limit) {
        ResBody resBody = new ResBody();
        List<Goods> list= service.getSellingGoods(page, limit);
        int count = service.getCountByStatus(1);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端获取所有下架商品
    @GetMapping("/api/getSoldoutGoods")
    public ResBody getSoldoutGoods(@RequestParam int page,
                                   @RequestParam int limit) {
        ResBody resBody = new ResBody();
        List<Goods> list= service.getSoldoutGoods(page, limit);
        int count = service.getCountByStatus(0);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端添加商品，默认为上架
    @PostMapping("/api/addGoods")
    // @RequestBody注解，将前端传过来的json键值对中的键的名称与对象的成员变量的名字进行匹配并且赋值
    public ResBody addGoods(@RequestBody Goods goods) {
        ResBody resBody = new ResBody();
        int i = service.addGoods(goods);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("添加成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("添加失败");
        }
        return resBody;
    }

    // 管理员端更改商品轮播图
    @GetMapping("/api/updateSwiperImages")
    public ResBody updateSwiperImages(@RequestParam int _id, @RequestParam String swiper_images) {
        ResBody resBody = new ResBody();
        int i = service.updateSwiperImages(_id, swiper_images);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("更换成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("更换失败");
        }
        return resBody;
    }

    // 管理员端修改商品信息
    @PostMapping("/api/updateGoods")
    public ResBody updateGoods(@RequestBody Goods goods) {
        ResBody resBody = new ResBody();
        int i = service.updateGoods(goods);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("修改成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("修改失败");
        }
        return resBody;
    }

    // 管理员端上架商品
    @GetMapping("/api/sellGoods")
    public ResBody sellGoods(@RequestParam int _id) {
        ResBody resBody = new ResBody();
        int i = service.sellGoods(_id);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("上架成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("上架失败");
        }
        return resBody;
    }

    // 管理员端下架商品
    @GetMapping("/api/soldoutGoods")
    public ResBody soldoutGoods(@RequestParam int _id) {
        ResBody resBody = new ResBody();
        int i = service.soldoutGoods(_id);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("下架成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("下架失败");
        }
        return resBody;
    }

    // 管理员端删除商品
    @GetMapping("/api/delGoods")
    public ResBody delGoods(@RequestParam int _id) {
        ResBody resBody = new ResBody();
        int i = service.delGoods(_id);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("删除成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("删除失败");
        }
        return resBody;
    }

    // 管理员端查找上架商品
    @GetMapping("/api/findSellingGoods")
    public ResBody findSellingGoods(@RequestParam int page,
                            @RequestParam int limit,
                            @RequestParam String name) {
        ResBody resBody = new ResBody();
        List<Goods> list= service.findGoods(page, limit, name, 1);
        int count = service.getCountByStatusAndName(name, 1);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端查找下架商品
    @GetMapping("/api/findSoldoutGoods")
    public ResBody findSoldoutGoods(@RequestParam int page,
                            @RequestParam int limit,
                            @RequestParam String name) {
        ResBody resBody = new ResBody();
        List<Goods> list= service.findGoods(page, limit, name, 0);
        int count = service.getCountByStatusAndName(name, 0);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 用户端
    // 用户端获取所有商品，只返回有上架的
    @GetMapping("/wx/getAllGoods")
    public List getAllGoods(){
        List list = service.getAllGoods();
        return list;
    }

    // 根据id返回商品对象到用户端
    @GetMapping("/wx/getGoodsById")
    public Goods getGoodsById(@RequestParam int id){
        Goods goods = service.getGoodsById(id);
        return goods;
    }

    // 获取商品详情页下方推荐列表
    @GetMapping("/wx/getRecomList")
    public List<Goods> getRecomList(@RequestParam int id){
        List<Goods> recomList = service.getRecomList(id);
        return recomList;
    }

}
