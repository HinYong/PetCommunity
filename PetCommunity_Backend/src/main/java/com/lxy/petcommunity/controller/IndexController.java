package com.lxy.petcommunity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    // 管理员端
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "page/admin/login";
    }

    @GetMapping("/welcome")
    public String console(Model model) {
        return "page/admin/welcome";
    }

    @GetMapping("/admin-info")
    public String admininfo(){
        return "page/admin/admin-info";
    }

    @GetMapping("/goods_in_selling")
    public String goods_in_selling(){
        return "page/admin/goods_in_selling";
    }

    @GetMapping("/goods_soldout")
    public String goods_soldout(){
        return "page/admin/goods_soldout";
    }

    @GetMapping("/type")
    public String type(){
        return "page/admin/type";
    }

    @GetMapping("/ordernotdelivery")
    public String ordernotdelivery(){
        return "page/admin/ordernotdelivery";
    }

    @GetMapping("/orderdelivery")
    public String orderdelivery(){
        return "page/admin/orderdelivery";
    }

    @GetMapping("/orderfinish")
    public String orderfinish(){
        return "page/admin/orderfinish";
    }

    @GetMapping("/ordercancel")
    public String ordercancel(){
        return "page/admin/ordercancel";
    }

    @GetMapping("/return_in_process")
    public String return_in_process(){
        return "page/admin/return_in_process";
    }

    @GetMapping("/return_success")
    public String return_success(){
        return "page/admin/return_success";
    }

    @GetMapping("/return_fail")
    public String return_fail(){
        return "page/admin/return_fail";
    }

    @GetMapping("/pets_type")
    public String pets_type(){
        return "page/admin/pets_type";
    }

    @GetMapping("/pets_info")
    public String pets_info(){
        return "page/admin/pets_info";
    }

    @GetMapping("/blog")
    public String blog(){
        return "page/admin/blog";
    }

    @GetMapping("/blog_comment")
    public String blog_comment(){
        return "page/admin/blog_comment";
    }

    @GetMapping("/agency")
    public String agency(){
        return "page/admin/agency";
    }

    @GetMapping("/help")
    public String help(){
        return "page/admin/help";
    }

    @GetMapping("/adopt")
    public String adopt(){
        return "page/admin/adopt";
    }

    @GetMapping("/adopt_request")
    public String adopt_request(){
        return "page/admin/adopt_request";
    }

    @GetMapping("/user")
    public String user(){
        return "page/admin/user";
    }

    @GetMapping("/comment")
    public String comment(){
        return "page/admin/comment";
    }

    // 宠物救助机构端
    @GetMapping("/agency/index")
    public String agency_index() {
        return "page/agency/agency_index";
    }

    @GetMapping("/agency/login")
    public String agency_login() {
        return "page/agency/agency_login";
    }

    @GetMapping("/agency/welcome")
    public String agency_console(Model model) {
        return "page/agency/agency_welcome";
    }

    @GetMapping("/agency/agency-info")
    public String agency_info(){
        return "page/agency/agency-info";
    }

    @GetMapping("/agency/help_not_process")
    public String agency_help_not_process(){
        return "page/agency/help_not_process";
    }

    @GetMapping("/agency/help_in_process")
    public String agency_help_in_process(){
        return "page/agency/help_in_process";
    }

    @GetMapping("/agency/help_finish")
    public String agency_help_finish(){
        return "page/agency/help_finish";
    }

    @GetMapping("/agency/help_cancel")
    public String agency_help_cancel(){
        return "page/agency/help_cancel";
    }

    @GetMapping("/agency/pets_adopt_seeking")
    public String agency_pets_adopt_seeking(){
        return "page/agency/pets_adopt_seeking";
    }

    @GetMapping("/agency/pets_adopt_processing")
    public String agency_pets_adopt_processing(){
        return "page/agency/pets_adopt_processing";
    }

    @GetMapping("/agency/pets_adopt_finish")
    public String agency_pets_adopt_finish(){
        return "page/agency/pets_adopt_finish";
    }

    @GetMapping("/agency/adopt_request_notprocess")
    public String agency_adopt_request_notprocess(){
        return "page/agency/adopt_request_notprocess";
    }

    @GetMapping("/agency/adopt_request_preagree")
    public String agency_adopt_request_preagree(){
        return "page/agency/adopt_request_preagree";
    }

    @GetMapping("/agency/adopt_request_finish")
    public String agency_adopt_request_finish(){
        return "page/agency/adopt_request_finish";
    }

    @GetMapping("/agency/adopt_request_disagree")
    public String agency_adopt_request_disagree(){
        return "page/agency/adopt_request_disagree";
    }

    @GetMapping("/agency/adopt_request_cancel")
    public String agency_adopt_request_cancel(){
        return "page/agency/adopt_request_cancel";
    }
}
