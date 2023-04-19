package com.lxy.petcommunity.controller;

import com.lxy.petcommunity.bean.Agency;
import com.lxy.petcommunity.bean.ResBody;
import com.lxy.petcommunity.service.AgencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
public class AgencyController {
    @Autowired
    AgencyService service;
    private static final Logger LOG = LoggerFactory.getLogger(AgencyController.class);

    // 管理员端
    // 管理员端获得所有宠物救助机构，分页返回
    @GetMapping("/api/getAllAgencies")
    public ResBody getAllAgencies(@RequestParam int page,
                                  @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = service.getAllAgenciesCount();
        List<Agency> list= service.getAllAgencies(page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端获得名称含有关键字的宠物救助机构，分页返回
    @GetMapping("/api/getAgenciesByName")
    public ResBody getAgenciesByName(@RequestParam int page,
                                   @RequestParam int limit,
                                   @RequestParam String agency_name) {
        ResBody resBody = new ResBody();
        List<Agency> list = service.getAgenciesByName(page, limit, agency_name);
        int count = service.getAgenciesCountByName(agency_name);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端获得地址含有关键字的宠物救助机构，分页返回
    @GetMapping("/api/getAgenciesByArea")
    public ResBody getAgenciesByArea(@RequestParam int page,
                                     @RequestParam int limit,
                                     @RequestParam String area) {
        ResBody resBody = new ResBody();
        List<Agency> list = service.getAgenciesByArea(page, limit, area);
        int count = service.getAgenciesCountByArea(area);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 宠物救助机构端
    // 机构登录
    @PostMapping("/agency/loginByPassword")
    public ResBody loginByPassword(@RequestBody Map<String, Object> params,
                                   HttpSession session) {
        ResBody resBody = new ResBody();
        String agency_name = params.get("agency_name").toString();
        String password = params.get("password").toString();
        Agency agency = service.findAgency(agency_name,password);
        if (agency == null){
            resBody.setCode(500);
            resBody.setMsg("登录失败，用户名或密码错误！");
        }else {
            session.setAttribute("agency",agency);
            LOG.info(agency.toString());
            resBody.setCode(200);
            resBody.setMsg("登录成功！");
        }
        return resBody;
    }

    // 机构信息更新
    @PostMapping("/agency/updateAgency")
    public ResBody updateAgency(@RequestBody Map<String, Object> params,
                              HttpSession session) {
        ResBody resBody = new ResBody();
        String newemail = params.get("email").toString();
        String newphone = params.get("phone").toString();
        String newdetail_address = params.get("detail_address").toString();
        String newwebsite = params.get("website").toString();
        Agency agency = (Agency) session.getAttribute("agency");
        agency.setEmail(newemail);
        agency.setPhone(newphone);
        agency.setDetail_address(newdetail_address);
        agency.setWebsite(newwebsite);
        int i = service.updateAgency(agency.getAgency_id(),agency);
        if (i != 1){
            resBody.setCode(500);
            resBody.setMsg("更新失败，后台出错");
        }else {
            session.setAttribute("agency",agency);
            LOG.info(agency.toString());
            resBody.setCode(200);
            resBody.setMsg("更新成功");
        }
        return resBody;
    }

    // 机构改密码
    @PostMapping("/agency/updatePassword")
    public ResBody updatePass(@RequestBody Map<String, Object> params,
                              HttpSession session) {
        ResBody resBody = new ResBody();
        String oldPsw = params.get("oldPassword").toString();
        String newPsw1 = params.get("newPassword1").toString();
        String newPsw2 = params.get("newPassword2").toString();
        Agency agency = (Agency) session.getAttribute("agency");
        if (!newPsw1.equals(newPsw2)){
            resBody.setCode(500);
            resBody.setMsg("修改失败，两次输入的密码不一致");
        }
        else if (!oldPsw.equals(agency.getPassword())){
            resBody.setCode(500);
            resBody.setMsg("修改失败，旧密码错误");
        }
        else {
            agency.setPassword(newPsw1);
            int i = service.updatePassword(agency.getAgency_id(), newPsw1);
            if (i != 1){
                resBody.setCode(500);
                resBody.setMsg("修改失败，后台出错");
            }else {
                session.setAttribute("agency",agency);
                LOG.info(agency.toString());
                resBody.setCode(200);
                resBody.setMsg("修改密码成功");
            }
        }
        return resBody;
    }

    // 小程序用户端
    // 按照城市关键字检索机构
    @GetMapping("/wx/findAgencyByCity")
    public int findAgencyByCity(@RequestParam String city){
        List<Agency> list = service.findAgencyByCity(city);
        if (!list.isEmpty()){
            return 200;
        }else{
            return 500;
        }
    }

    // 按照关键字检索机构
    @GetMapping("/wx/findAgencyByKeyword")
    public int findAgencyByKeyword(@RequestParam String Keyword){
        List<Agency> list = service.findAgencyByKeyword(Keyword);
        if (!list.isEmpty()){
            return 200;
        }else{
            return 500;
        }
    }
}
