package com.lxy.petcommunity.service;

import com.lxy.petcommunity.bean.AdoptRequest;
import com.lxy.petcommunity.dao.AdoptRequestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptRequestService {
    @Autowired
    AdoptRequestDao dao;

    // 管理员端
    // 返回所有领养申请数量
    public int getAllAdoptRequestsCount() {
        return dao.getAllAdoptRequestsCount();
    }

    // 返回含有查找关键字的领养申请数量
    public int getAdoptRequestsCountByContent(String request_content) {
        return dao.getAdoptRequestsCountByContent(request_content);
    }

    // 管理员端获得所有领养申请，分页取数据
    public List<AdoptRequest> getAllAdoptRequests(int page, int limit) {
        return dao.getAllAdoptRequests(page, limit);
    }

    // 根据关键字查找领养申请返回到管理员端
    public List<AdoptRequest> findAdoptRequestByContent(int page, int limit, String request_content) {
        return dao.findAdoptRequestByContent(page, limit, request_content);
    }

    // 宠物保护机构端
    // 返回指定状态的所有领养申请数量到宠物保护机构端
    public int getAllAdoptRequestsCountByAgencyIdAndStatus(int agency_id, int status) {
        return dao.getAllAdoptRequestsCountByAgencyIdAndStatus(agency_id, status);
    }

    // 宠物保护机构端获得所有领养申请，分页取数据
    public List<AdoptRequest> getAllAdoptRequestsByAgencyIdAndStatus(int agency_id, int status, int page, int limit) {
        return dao.getAllAdoptRequestsByAgencyIdAndStatus(agency_id, status, page, limit);
    }

    // 根据单号和状态查找领养申请返回到宠物保护机构端，分页取数据
    public List<AdoptRequest> findAdoptRequestByAdoptRequestId(int page, int limit, int request_id, int agency_id, int status) {
        return dao.findAdoptRequestByAdoptRequestId(page, limit, request_id, agency_id, status);
    }

    // 初审通过
    public int preagreeAdoptRequest(int request_id) {
        return dao.preagreeAdoptRequest(request_id);
    }

    // 线下领养手续全部完成
    public int finishAdoptRequest(int request_id, int adopt_id){
        return dao.finishAdoptRequest(request_id, adopt_id);
    }

    // 拒绝领养申请
    public int disagreeAdoptRequest(int request_id, int adopt_id, String refuse_reason) {
        return dao.disagreeAdoptRequest(request_id, adopt_id, refuse_reason);
    }

    // 用户端
    // 获取用户发布的指定状态的领养申请
    public List<AdoptRequest> getPublishAdoptRequestsByUserOpenIdAndStatus(String user_openid, int status, int limit, int page) {
        return dao.getPublishAdoptRequestsByUserOpenIdAndStatus(user_openid, status, limit, page);
    }

    // 用户发布领养申请
    public int publishAdoptRequest(int adopt_id, int agency_id, String user_openid, String contact_name, String contact_phone, String contact_address, String request_content, String request_images) {
        return dao.publishAdoptRequest(adopt_id, agency_id, user_openid, contact_name, contact_phone, contact_address, request_content, request_images);
    }

    // 用户取消领养申请
    public int cancelAdoptRequest(String user_openid, int request_id, int adopt_id) {
        return dao.cancelAdoptRequest(user_openid, request_id, adopt_id);
    }
}