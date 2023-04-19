package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class AdoptRequestDao {
    @Autowired
    JdbcTemplate template;
    @Autowired
    AgencyDao agencyDao;
    @Autowired
    AdoptDao adoptDao;

    // 管理员端
    // 返回所有领养申请数量
    public int getAllAdoptRequestsCount() {
        int count = template.queryForObject("select count(*) from `adopt_request`", Integer.class);
        return count;
    }

    // 返回含有查找关键字的领养申请数量
    public int getAdoptRequestsCountByContent(String request_content) {
        int count = template.queryForObject("select count(*) from `adopt_request` where `request_content` like '%" + request_content + "%'", Integer.class);
        return count;
    }

    // 管理员端获得所有领养申请，分页取数据
    public List<AdoptRequest> getAllAdoptRequests(int page,int limit) {
        List<AdoptRequest> list;
        list = template.query("select * from `adopt_request` limit ?,?" ,new Object[]{(page-1)*limit,limit} ,
                    new BeanPropertyRowMapper(AdoptRequest.class));
        if (list!=null&&list.size()!=0){
            for (AdoptRequest adoptRequest:list){
                // 获取User对象
                List<User> users = template.query("select * from `user` where open_id = '"+adoptRequest.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                adoptRequest.setUser(users.get(0));
                // 设置救助机构对象
                List<Agency> agencies = template.query("select * from `agency` where agency_id = "+adoptRequest.getAgency_id()  ,
                        new BeanPropertyRowMapper(Agency.class));
                adoptRequest.setAgency(agencies.get(0));
            }
            return list;
        }else{
            return null;
        }
    }

    // 根据关键字查找领养申请返回到管理员端
    public List<AdoptRequest> findAdoptRequestByContent(int page, int limit, String request_content) {
        List<AdoptRequest> list = template.query("select * from `adopt_request` where `request_content` like '%"+request_content+"%' limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(AdoptRequest.class));
        if (!list.isEmpty()){
            for (AdoptRequest adoptRequest:list){
                // 获取User对象
                List<User> users = template.query("select * from `user` where open_id = '"+adoptRequest.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                adoptRequest.setUser(users.get(0));
                // 设置救助机构对象
                List<Agency> agencies = template.query("select * from `agency` where agency_id = "+adoptRequest.getAgency_id()  ,
                        new BeanPropertyRowMapper(Agency.class));
                adoptRequest.setAgency(agencies.get(0));
            }
            return list;
        }else{
            return null;
        }
    }

    // 宠物保护机构端
    // 返回指定状态的所有领养申请数量到宠物保护机构端
    public int getAllAdoptRequestsCountByAgencyIdAndStatus(int agency_id, int status) {
        int count = template.queryForObject("select count(*) from `adopt_request` where agency_id=" + agency_id + " and status = "+status, Integer.class);
        return count;
    }

    // 宠物保护机构端获得所有领养申请，分页取数据
    public List<AdoptRequest> getAllAdoptRequestsByAgencyIdAndStatus(int agency_id, int status, int page, int limit) {
        List<AdoptRequest> list;
        list = template.query("select * from `adopt_request` where agency_id = ? and status = ? limit ?,?" ,new Object[]{agency_id,status,(page-1)*limit,limit} ,
                new BeanPropertyRowMapper(AdoptRequest.class));
        if (list!=null&&list.size()!=0){
            for (AdoptRequest adoptRequest:list){
                // 获取User对象
                List<User> users = template.query("select * from `user` where open_id = '"+adoptRequest.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                adoptRequest.setUser(users.get(0));
                // 获取Adopt对象
                List<Adopt> adopts = template.query("select * from `adopt` where `adopt_id` = " + adoptRequest.getAdopt_id(),
                        new BeanPropertyRowMapper(Adopt.class));
                Adopt adopt = adopts.get(0);
                // 设置Adopt对象的宠物种类
                List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + adopt.getPet_type_id(),
                        new BeanPropertyRowMapper(PetsType.class));
                if(!petsTypes.isEmpty()){
                    adopt.setPetsType(petsTypes.get(0));
                }
                adoptRequest.setAdopt(adopt);
            }
            return list;
        }else{
            return null;
        }
    }

    // 根据单号和状态查找领养申请返回到宠物保护机构端，分页取数据
    public List<AdoptRequest> findAdoptRequestByAdoptRequestId(int page, int limit, int request_id, int agency_id, int status) {
        List<AdoptRequest> list = template.query("select * from `adopt_request` where `request_id` = ? and agency_id = ? and status = ? limit ?,?" ,new Object[]{request_id,agency_id,status,(page-1)*limit,limit},
                new BeanPropertyRowMapper(AdoptRequest.class));
        if (list!=null&&list.size()!=0){
            for (AdoptRequest adoptRequest:list){
                // 获取User对象
                List<User> users = template.query("select * from `user` where open_id = '"+adoptRequest.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                adoptRequest.setUser(users.get(0));
                // 获取Adopt对象
                List<Adopt> adopts = template.query("select * from `adopt` where `adopt_id` = " + adoptRequest.getAdopt_id(),
                        new BeanPropertyRowMapper(Adopt.class));
                Adopt adopt = adopts.get(0);
                // 设置Adopt对象的宠物种类
                List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + adopt.getPet_type_id(),
                        new BeanPropertyRowMapper(PetsType.class));
                if(!petsTypes.isEmpty()){
                    adopt.setPetsType(petsTypes.get(0));
                }
                adoptRequest.setAdopt(adopt);
            }
            return list;
        }else{
            return null;
        }
    }

    // 初审通过
    public int preagreeAdoptRequest(int request_id){
        // 修改领养申请的状态
        return template.update("update `adopt_request` set status = ?, process_time = ? where request_id = ?",1, new Date(), request_id);
    }

    // 线下领养手续全部完成
    public int finishAdoptRequest(int request_id, int adopt_id){
        // 修改领养申请的状态
        int i = template.update("update `adopt_request` set status = ?, finish_time = ? where request_id = ?",2, new Date(), request_id);
        // 结束领养需求
        if(i==1){
            adoptDao.finishAdopt(adopt_id);
        }
        return i;
    }

    // 拒绝领养申请
    public int disagreeAdoptRequest(int request_id, int adopt_id, String refuse_reason){
        // 修改领养申请的状态
        int i = template.update("update `adopt_request` set status = ?, refuse_time = ?, refuse_reason = ? where request_id = ?",3, new Date(), refuse_reason, request_id);
        // 重新发布领养需求
        if(i==1){
            adoptDao.republishAdopt(adopt_id);
        }
        return i;
    }

    // 用户端
    // 获取用户发布的指定状态的领养申请
    public List<AdoptRequest> getPublishAdoptRequestsByUserOpenIdAndStatus(String user_openid, int status, int limit, int page) {
        List<AdoptRequest> list = template.query("select * from `adopt_request` where user_openid = ? and status = ? limit ?,?", new Object[]{user_openid,status,(page-1)*limit,limit},
                new BeanPropertyRowMapper(AdoptRequest.class));
        if (list!=null&&list.size()!=0){
            for (AdoptRequest adoptRequest:list){
                // 设置救助机构对象
                List<Agency> agencies = template.query("select * from `agency` where agency_id = "+adoptRequest.getAgency_id()  ,
                        new BeanPropertyRowMapper(Agency.class));
                adoptRequest.setAgency(agencies.get(0));
                // 获取Adopt对象
                List<Adopt> adopts = template.query("select * from `adopt` where `adopt_id` = " + adoptRequest.getAdopt_id(),
                        new BeanPropertyRowMapper(Adopt.class));
                Adopt adopt = adopts.get(0);
                // 设置Adopt对象的宠物种类
                List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + adopt.getPet_type_id(),
                        new BeanPropertyRowMapper(PetsType.class));
                if(!petsTypes.isEmpty()){
                    adopt.setPetsType(petsTypes.get(0));
                }
                adopt.setAgency(agencies.get(0));
                // 设置领养需求的图片列表
                String adoptImages = adopt.getImages();
                if(adoptImages!=null){
                    String[] adopt_images_List = adoptImages.split(",");
                    adopt.setImages_List(Arrays.asList(adopt_images_List));
                }
                adoptRequest.setAdopt(adopt);
                // 设置用户领养申请的图片列表
                String images = adoptRequest.getImages();
                if(images!=null){
                    String[] images_List = images.split(",");
                    adoptRequest.setImages_List(Arrays.asList(images_List));
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 用户发布领养申请
    public int publishAdoptRequest(int adopt_id, int agency_id, String user_openid, String contact_name, String contact_phone, String contact_address, String request_content, String request_images){
        int i = template.update("insert into `adopt_request` values (null,?,?,?,?,?,?,?,?,0,?,null,null,null,null,null)",
                adopt_id, agency_id, user_openid, contact_name, contact_phone, contact_address, request_content, request_images, new Date());
        if(i==1){
            template.update("update `adopt` set status = 1 where adopt_id = ?", adopt_id);
        }
        return i;
    }

    // 用户取消领养申请
    public int cancelAdoptRequest(String user_openid, int request_id, int adopt_id){
        int i = template.update("update `adopt_request` set status = 4, cancel_time = ? where user_openid = ? and request_id = ?", new Date(), user_openid, request_id);
        // 重新发布领养需求
        if(i==1){
            adoptDao.republishAdopt(adopt_id);
        }
        return i;
    }
}
