package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.Goods;
import com.lxy.petcommunity.dao.GoodsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    GoodsDao dao;

    // 管理员端
    // 获取所有商品的数量
    public int getAllCount() {
        return dao.getAllCount();
    }

    // 根据商品名称以及状态获取数量
    public int getCountByStatusAndName(String name, int status) {
        return dao.getCountByStatusAndName(name, status);
    }

    // 根据商品状态获取数量
    public int getCountByStatus(int status) {
        return dao.getCountByStatus(status);
    }

    // 返回所有的商品，不管是否上架
    public List<Goods> getAllGoods(int page, int limit) {
        return dao.getAllGoods(page,limit);
    }

    // 返回上架的所有商品
    public List<Goods> getSellingGoods(int page, int limit) {
        return dao.getSellingGoods(page,limit);
    }

    // 返回下架的所有商品
    public List<Goods> getSoldoutGoods(int page, int limit) {
        return dao.getSoldoutGoods(page,limit);
    }

    // 添加商品
    public int addGoods(Goods goods) {
        return dao.addGoods(goods);
    }

    // 更新商品
    public int updateGoods(Goods goods) {
        return dao.updateGoods(goods);
    }

    // 添加商品轮播图
    public int updateSwiperImages(int _id, String swiper_images) {
        return dao.updateSwiperImages(_id, swiper_images);
    }

    // 上架商品
    public int sellGoods(int id) {
        return dao.sellGoods(id);
    }

    // 下架商品
    public int soldoutGoods(int id) {
        return dao.soldoutGoods(id);
    }

    // 删除商品
    public int delGoods(int id) {
        return dao.delGoods(id);
    }

    public List<Goods> findGoods(int page, int limit, String name, int status) {
        return dao.findGoods(page,limit,name,status);
    }

    // 用户端
    // 返回所有有上架的商品
    public List getAllGoods() {
        return dao.getAllGoods();
    }

    // 根据id返回商品对象到用户端
    public Goods getGoodsById(int id) {
        return dao.getGoodsById(id);
    }

    // 获取商品详情页下方推荐列表
    public List<Goods> getRecomList(int id) {
        return dao.getRecomList(id);
    }
}
