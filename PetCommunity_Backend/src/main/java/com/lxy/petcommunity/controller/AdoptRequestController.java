package com.lxy.petcommunity.controller;

import com.lxy.petcommunity.bean.AdoptRequest;
import com.lxy.petcommunity.bean.ResBody;
import com.lxy.petcommunity.service.AdoptRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdoptRequestController {

    @Autowired
    AdoptRequestService service;

    // 管理员端
    // 管理员端获得所有领养申请，分页取数据
    @GetMapping("/api/getAllAdoptRequests")
    public ResBody getAllAdoptRequests(@RequestParam int page,
                                       @RequestParam int limit){
        ResBody resBody = new ResBody();
        int count = service.getAllAdoptRequestsCount();
        List<AdoptRequest> list= service.getAllAdoptRequests(page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 根据关键字查找领养申请返回到管理员端
    @GetMapping("/api/findAdoptRequestByContent")
    public ResBody findAdoptRequestByContent(@RequestParam int page,
                                             @RequestParam int limit,
                                             @RequestParam String request_content) {
        ResBody resBody = new ResBody();
        int count = service.getAdoptRequestsCountByContent(request_content);
        List<AdoptRequest> list= service.findAdoptRequestByContent(page, limit, request_content);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 宠物保护机构端
    // 宠物保护机构端获得所有领养申请，分页取数据
    @GetMapping("/agency/getAllAdoptRequestsByAgencyIdAndStatus")
    public ResBody getAllAdoptRequestsByAgencyIdAndStatus(@RequestParam int agency_id,
                                                          @RequestParam int status, 
                                                          @RequestParam int page,
                                                          @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = service.getAllAdoptRequestsCountByAgencyIdAndStatus(agency_id, status);
        List<AdoptRequest> list= service.getAllAdoptRequestsByAgencyIdAndStatus(agency_id, status, page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 根据单号查找领养申请返回到宠物保护机构端
    @GetMapping("/agency/findAdoptRequestByAdoptRequestId")
    public ResBody findAdoptRequestByAdoptRequestId(@RequestParam int page,
                                                    @RequestParam int limit,
                                                    @RequestParam String request_id,
                                                    @RequestParam int agency_id,
                                                    @RequestParam int status) {
        ResBody resBody = new ResBody();
        List<AdoptRequest> list;
        // 点击数据加载按钮，加载全部数据
        if(request_id.equals("")){
            return getAllAdoptRequestsByAgencyIdAndStatus(agency_id, status, page, limit);
        }
        else {
            int i = Integer.valueOf(request_id);
            list= service.findAdoptRequestByAdoptRequestId(page, limit, i, agency_id, status);
            int count;
            if(list==null){
                count = 0;
            }
            else {
                count = list.size();
            }
            resBody.setCount(count);
            resBody.setData(list);
            resBody.setCode(0);
            return resBody;
        }
    }

    // 初审通过
    @GetMapping("/agency/preagreeAdoptRequest")
    public ResBody preagreeAdoptRequest(@RequestParam int request_id){
        ResBody resBody = new ResBody();
        int i = service.preagreeAdoptRequest(request_id);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("已通过");
        }else{
            resBody.setCode(500);
            resBody.setMsg("操作失败");
        }
        return resBody;
    }

    // 线下领养手续全部完成
    @GetMapping("/agency/finishAdoptRequest")
    public ResBody finishAdoptRequest(@RequestParam int request_id, @RequestParam int adopt_id){
        ResBody resBody = new ResBody();
        int i = service.finishAdoptRequest(request_id, adopt_id);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("已完成");
        }else{
            resBody.setCode(500);
            resBody.setMsg("操作失败");
        }
        return resBody;
    }

    // 拒绝领养申请
    @GetMapping("/agency/disagreeAdoptRequest")
    public ResBody disagreeAdoptRequest(@RequestParam int request_id,
                                        @RequestParam int adopt_id,
                                        @RequestParam String refuse_reason){
        ResBody resBody = new ResBody();
        int i = service.disagreeAdoptRequest(request_id, adopt_id, refuse_reason);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("已拒绝");
        }else{
            resBody.setCode(500);
            resBody.setMsg("操作失败");
        }
        return resBody;
    }


    // 用户端
    // 获取用户发布的指定状态的领养申请
    @GetMapping("/wx/getPublishAdoptRequestsByUserOpenIdAndStatus")
    public List<AdoptRequest> getPublishAdoptRequestsByUserOpenIdAndStatus(@RequestParam String user_openid,
                                                                           @RequestParam int status,
                                                                           @RequestParam int limit,
                                                                           @RequestParam int page){
        return service.getPublishAdoptRequestsByUserOpenIdAndStatus(user_openid, status, limit, page);
    }

    // 用户发布领养申请
    @GetMapping("/wx/publishAdoptRequest")
    public int publishAdoptRequest(@RequestParam int adopt_id,
                                   @RequestParam int agency_id,
                                   @RequestParam String user_openid,
                                   @RequestParam String contact_name,
                                   @RequestParam String contact_phone,
                                   @RequestParam String contact_address,
                                   @RequestParam String request_content,
                                   @RequestParam String request_images){
        int i = service.publishAdoptRequest(adopt_id, agency_id, user_openid, contact_name, contact_phone, contact_address, request_content, request_images);
        if(i == 1){
            return 200;
        }
        else {
            return 500;
        }
    }

    // 用户取消领养申请
    @GetMapping("/wx/cancelAdoptRequest")
    public int cancelAdoptRequest(@RequestParam String user_openid, @RequestParam int request_id, @RequestParam int adopt_id) {
        int i = service.cancelAdoptRequest(user_openid, request_id, adopt_id);
        if (i == 1){
            return 200;
        }else{
            return 500;
        }
    }
}
