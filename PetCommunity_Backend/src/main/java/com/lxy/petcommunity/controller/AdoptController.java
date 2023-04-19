package com.lxy.petcommunity.controller;

import com.lxy.petcommunity.bean.Adopt;
import com.lxy.petcommunity.bean.ResBody;
import com.lxy.petcommunity.service.AdoptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdoptController {
    @Autowired
    AdoptService service;

    // 管理员端获得所有领养需求，分页取数据
    @GetMapping("/api/getAllAdopts")
    public ResBody getAllAdopts(@RequestParam int page,
                               @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = service.getAllAdoptsCount();
        List<Adopt> list= service.getAllAdopts(page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 根据机构名称查找领养需求返回到管理员端，分页取数据
    @GetMapping("/api/findAdoptsByAgencyName")
    public ResBody findAdoptsByAgencyName(@RequestParam int page,
                                         @RequestParam int limit,
                                         @RequestParam String agency_name) {
        ResBody resBody = new ResBody();
        int count = service.getAdoptsCountByAgencyName(agency_name);
        List<Adopt> list= service.findAdoptsByAgencyName(page, limit, agency_name);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 根据关键字查找领养需求返回到管理员端，分页取数据
    @GetMapping("/api/findAdoptsByKeyword")
    public ResBody findAdoptsByKeyword(@RequestParam int page,
                                       @RequestParam int limit,
                                       @RequestParam String Keyword) {
        ResBody resBody = new ResBody();
        int count = service.getAdoptsCountByKeyword(Keyword);
        List<Adopt> list= service.findAdoptsByKeyword(page, limit, Keyword);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 管理员端删除领养需求
    @GetMapping("/api/delAdopt")
    public ResBody delAdopt(@RequestParam int adopt_id) {
        ResBody resBody = new ResBody();
        int i = service.delAdopt(adopt_id);
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
    // 宠物保护机构端获得指定状态的所有领养需求，分页取数据
    @GetMapping("/agency/getAllAdoptsByAgencyIdAndStatus")
    public ResBody getAllAdoptsByAgencyIdAndStatus(@RequestParam int page,
                                                   @RequestParam int limit,
                                                   @RequestParam int agency_id,
                                                   @RequestParam int status) {
        ResBody resBody = new ResBody();
        int count = service.getAllAdoptsCountByAgencyIdAndStatus(agency_id, status);
        List<Adopt> list= service.getAllAdoptsByAgencyIdAndStatus(page, limit, agency_id, status);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 根据关键字和状态查找领养需求返回到宠物保护机构端，分页取数据
    @GetMapping("/agency/findAdoptsByKeywordAndStatus")
    public ResBody findAdoptsByKeywordAndStatus(@RequestParam int page,
                                                @RequestParam int limit,
                                                @RequestParam String Keyword,
                                                @RequestParam int agency_id,
                                                @RequestParam int status) {
        ResBody resBody = new ResBody();
        int count = service.getAdoptsCountByKeywordAndStatus(Keyword, agency_id, status);
        List<Adopt> list= service.findAdoptsByKeywordAndStatus(page, limit, Keyword, agency_id, status);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    // 根据领养号和状态查找领养需求返回到宠物保护机构端，分页取数据
    @GetMapping("/agency/findAdoptByAdoptIdAndStatus")
    public ResBody findAdoptByAdoptIdAndStatus(@RequestParam int page,
                                               @RequestParam int limit,
                                               @RequestParam String adopt_id,
                                               @RequestParam int agency_id,
                                               @RequestParam int status) {
        ResBody resBody = new ResBody();
        List<Adopt> list;
        // 点击数据加载按钮，加载全部数据
        if(adopt_id.equals("")){
            return getAllAdoptsByAgencyIdAndStatus(page, limit, agency_id, status);
        }
        else {
            int i = Integer.valueOf(adopt_id);
            list= service.findAdoptByAdoptIdAndStatus(page, limit, i, agency_id, status);
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
    
    // 发布领养需求
    @PostMapping("/agency/publishAdopt")
    public ResBody publishAdopt(@RequestBody Adopt adopt) {
        ResBody resBody = new ResBody();
        int i = service.publishAdopt(adopt);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("发布成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("发布失败");
        }
        return resBody;
    }

    // 更新领养需求
    @PostMapping("/agency/updateAdopt")
    public ResBody updateAdopt(@RequestBody Adopt adopt) {
        ResBody resBody = new ResBody();
        int i = service.updateAdopt(adopt);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("修改成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("修改失败");
        }
        return resBody;
    }

    // 宠物救助机构端上传图片
    @GetMapping("/agency/updateAdoptImages")
    public ResBody updateAdoptImages(@RequestParam int adopt_id, @RequestParam String images) {
        ResBody resBody = new ResBody();
        int i = service.updateAdoptImages(adopt_id, images);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("更换成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("更换失败");
        }
        return resBody;
    }

    // 宠物救助机构端删除领养需求，需要删除文件夹的图片
    @GetMapping("/agency/delAdopt")
    public ResBody delAdopt_Agency(@RequestParam int adopt_id) {
        ResBody resBody = new ResBody();
        int i = service.delAdopt(adopt_id);
        if (i == 1){
            resBody.setCode(200);
            resBody.setMsg("删除成功");
        }else{
            resBody.setCode(500);
            resBody.setMsg("删除失败");
        }
        return resBody;
    }
    
    // 用户端
    // 按宠物种类分页返回所有发布的领养需求
    @GetMapping("/wx/getAllPublishAdoptsByPetType")
    public List getAllPublishAdoptsByPetType(@RequestParam int page,
                                             @RequestParam int limit,
                                             @RequestParam int pet_type_id,
                                             @RequestParam String searchContent){
        List list = service.getAllPublishAdoptsByPetType(page, limit, pet_type_id, searchContent);
        return list;
    }

    // 根据领养号查找领养需求
    @GetMapping("/wx/findAdoptByAdoptId")
    public Adopt findAdoptByAdoptId(@RequestParam int adopt_id) {
        return service.findAdoptByAdoptId(adopt_id);
    }

}
