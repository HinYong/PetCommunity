package com.lxy.petcommunity.controller;

import com.lxy.petcommunity.bean.Help;
import com.lxy.petcommunity.bean.ResBody;
import com.lxy.petcommunity.service.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelpController {

    @Autowired
    HelpService service;

    // 管理员端
    // 管理员端获得所有求助请求，分页取数据
    @GetMapping("/api/getAllHelps")
    public ResBody getAllHelps(@RequestParam int page,
                               @RequestParam int limit){
        ResBody resBody = new ResBody();
        int count = service.getAllHelpsCount();
        List<Help> list= service.getAllHelps(page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 根据关键字查找求助请求返回到管理员端
    @GetMapping("/api/findHelpByContent")
    public ResBody findHelpByContent(@RequestParam int page,
                                           @RequestParam int limit,
                                           @RequestParam String help_content) {
        ResBody resBody = new ResBody();
        int count = service.getHelpsCountByContent(help_content);
        List<Help> list= service.findHelpByContent(page, limit, help_content);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端删除求助请求
    @GetMapping("/api/delHelp")
    public ResBody delHelp_admin(@RequestParam int help_id) {
        ResBody resBody = new ResBody();
        int i = service.delHelp(help_id);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("删除成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("删除失败");
        }
        return resBody;
    }

    // 宠物保护机构端
    // 宠物保护机构端获得所有求助请求，分页取数据
    @GetMapping("/agency/getAllHelpsByAgencyIdAndStatus")
    public ResBody getAllHelpsByAgencyIdAndStatus(@RequestParam int agency_id,
                                                     @RequestParam int status,
                                                     @RequestParam int page,
                                                     @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = service.getAllHelpsCountByAgencyIdAndStatus(agency_id, status);
        List<Help> list= service.getAllHelpsByAgencyIdAndStatus(agency_id, status, page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 根据单号查找求助请求返回到宠物保护机构端
    @GetMapping("/agency/findHelpByHelpId")
    public ResBody findHelpByHelpId(@RequestParam int page,
                                    @RequestParam int limit,
                                    @RequestParam String help_id,
                                    @RequestParam int agency_id,
                                    @RequestParam int status) {
        ResBody resBody = new ResBody();
        List<Help> list;
        // 点击数据加载按钮，加载全部数据
        if(help_id.equals("")){
            return getAllHelpsByAgencyIdAndStatus(agency_id, status, page, limit);
        }
        else {
            int i = Integer.valueOf(help_id);
            list= service.findHelpByHelpId(page, limit, i, agency_id, status);
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

    // 处理请求
    @GetMapping("/agency/processHelp")
    public ResBody processHelp(@RequestParam int help_id,
                               @RequestParam String staff_name,
                               @RequestParam String staff_phone){
        ResBody resBody = new ResBody();
        int i = service.processHelp(help_id, staff_name, staff_phone);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("受理成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("受理失败");
        }
        return resBody;
    }

    // 处理结束
    @GetMapping("/agency/finishHelp")
    public ResBody finishHelp(@RequestParam int help_id){
        ResBody resBody = new ResBody();
        int i = service.finishHelp(help_id);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("已完成");
        }else{
            resBody.setCode(500);
            resBody.setMsg("操作失败");
        }
        return resBody;
    }


    // 用户端
    // 获取用户发布的指定状态的求助请求
    @GetMapping("/wx/getPublishHelpsByUserOpenIdAndStatus")
    public List<Help> getPublishHelpsByUserOpenIdAndStatus(@RequestParam String user_openid,
                                                           @RequestParam int status,
                                                           @RequestParam int limit,
                                                           @RequestParam int page){
        return service.getPublishHelpsByUserOpenIdAndStatus(user_openid, status, limit, page);
    }

    // 用户发布求助请求
    @GetMapping("/wx/publishHelp")
    public int publishHelp(@RequestParam String user_openid,
                           @RequestParam String contact_name,
                           @RequestParam String contact_phone,
                           @RequestParam String province,
                           @RequestParam String city,
                           @RequestParam String detail_address,
                           @RequestParam String help_content,
                           @RequestParam String help_images){
        boolean flag = service.publishHelp(user_openid, contact_name, contact_phone, province, city, detail_address, help_content, help_images);
        if(flag){
            return 200;
        }
        else {
            return 500;
        }
    }

    // 用户取消求助请求
    @GetMapping("/wx/cancelHelp")
    public int cancelHelp(@RequestParam String user_openid, @RequestParam int help_id) {
        int i = service.cancelHelp(user_openid, help_id);
        if (i == 1){
            return 200;
        }else{
            return 500;
        }
    }
}
