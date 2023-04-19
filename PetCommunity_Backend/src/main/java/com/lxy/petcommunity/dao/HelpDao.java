package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.Agency;
import com.lxy.petcommunity.bean.Help;
import com.lxy.petcommunity.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Repository
public class HelpDao {
    @Autowired
    JdbcTemplate template;
    @Autowired
    AgencyDao agencyDao;

    // 管理员端
    // 返回所有求助请求数量
    public int getAllHelpsCount() {
        int count = template.queryForObject("select count(*) from `help`", Integer.class);
        return count;
    }

    // 返回含有查找关键字的求助请求数量
    public int getHelpsCountByContent(String help_content) {
        int count = template.queryForObject("select count(*) from `help` where `help_content` like '%" + help_content + "%'", Integer.class);
        return count;
    }

    // 管理员端获得所有求助请求，分页取数据
    public List<Help> getAllHelps(int page,int limit) {
        List<Help> list;
        list = template.query("select * from `help` limit ?,?" ,new Object[]{(page-1)*limit,limit} ,
                    new BeanPropertyRowMapper(Help.class));
        if (list!=null&&list.size()!=0){
            for (Help help:list){
                // 获取User对象
                List<User> users = template.query("select * from `user` where open_id = '"+help.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                help.setUser(users.get(0));
                // 设置救助机构对象
                List<Agency> agencies = template.query("select * from `agency` where agency_id = "+help.getAgency_id()  ,
                        new BeanPropertyRowMapper(Agency.class));
                help.setAgency(agencies.get(0));
                // 设置地址字符串
                String address = help.getProvince()+" "+help.getCity()+" "+help.getDetail_address();
                help.setAddress(address);
            }
            return list;
        }else{
            return null;
        }
    }

    // 根据关键字查找求助请求返回到管理员端
    public List<Help> findHelpByContent(int page, int limit, String help_content) {
        List<Help> list = template.query("select * from `help` where `help_content` like '%"+help_content+"%' limit ?,?" ,new Object[]{(page-1)*limit,limit},
                new BeanPropertyRowMapper(Help.class));
        if (!list.isEmpty()){
            for (Help help:list){
                // 获取User对象
                List<User> users = template.query("select * from `user` where open_id = '"+help.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                help.setUser(users.get(0));
                // 设置救助机构对象
                List<Agency> agencies = template.query("select * from `agency` where agency_id = "+help.getAgency_id()  ,
                        new BeanPropertyRowMapper(Agency.class));
                help.setAgency(agencies.get(0));
                // 设置地址字符串
                String address = help.getProvince()+" "+help.getCity()+" "+help.getDetail_address();
                help.setAddress(address);
            }
            return list;
        }else{
            return null;
        }
    }

    // 管理员端删除求助请求，需要删除文件夹的图片
    public int delHelp(int help_id) {
        // 获取上传图片路径
        String help_images = template.queryForObject("select images from `help` where help_id = "+ help_id, String.class);
        if(help_images!=null){
            String[] help_images_List = help_images.split(",");
            // 删除之前上传的轮播图
            for(String s:help_images_List){
                s = s.replace("http://localhost:8081/", "src/main/resources/static/");
                File file1 = new File(s);
                file1.delete();
            }
        }
        return template.update("DELETE from `help` where help_id = ?",help_id);
    }

    // 宠物保护机构端
    // 返回指定状态的所有求助请求数量到宠物保护机构端
    public int getAllHelpsCountByAgencyIdAndStatus(int agency_id, int status) {
        int count = template.queryForObject("select count(*) from `help` where agency_id=" + agency_id + " and status = "+status, Integer.class);
        return count;
    }

    // 宠物保护机构端获得所有求助请求，分页取数据
    public List<Help> getAllHelpsByAgencyIdAndStatus(int agency_id, int status, int page, int limit) {
        List<Help> list;
        list = template.query("select * from `help` where agency_id = ? and status = ? limit ?,?" ,new Object[]{agency_id,status,(page-1)*limit,limit} ,
                new BeanPropertyRowMapper(Help.class));
        if (list!=null&&list.size()!=0){
            for (Help help:list){
                // 获取User对象
                List<User> users = template.query("select * from `user` where open_id = '"+help.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                help.setUser(users.get(0));
                // 设置地址字符串
                String address = help.getProvince()+" "+help.getCity()+" "+help.getDetail_address();
                help.setAddress(address);
            }
            return list;
        }else{
            return null;
        }
    }

    // 根据单号和状态查找求助请求返回到宠物保护机构端，分页取数据
    public List<Help> findHelpByHelpId(int page, int limit, int help_id, int agency_id, int status) {
        List<Help> list = template.query("select * from `help` where `help_id` = ? and agency_id = ? and status = ? limit ?,?" ,new Object[]{help_id,agency_id,status,(page-1)*limit,limit},
                new BeanPropertyRowMapper(Help.class));
        if (list!=null&&list.size()!=0){
            for (Help help:list){
                // 获取User对象
                List<User> users = template.query("select * from `user` where open_id = '"+help.getUser_openid()+"'"  ,
                        new BeanPropertyRowMapper(User.class));
                help.setUser(users.get(0));
                // 设置地址字符串
                String address = help.getProvince()+" "+help.getCity()+" "+help.getDetail_address();
                help.setAddress(address);

            }
            return list;
        }else{
            return null;
        }
    }

    // 处理请求
    public int processHelp(int help_id, String staff_name, String staff_phone){
        return template.update("update `help` set status = ?, process_time = ?, staff_name = ?, staff_phone = ? where help_id = ?",1, new Date(), staff_name, staff_phone, help_id);
    }

    // 处理结束
    public int finishHelp(int help_id){
        return template.update("update `help` set status = ?, finish_time = ? where help_id = ?",2, new Date(), help_id);
    }

    // 用户端
    // 获取用户发布的指定状态的求助请求
    public List<Help> getPublishHelpsByUserOpenIdAndStatus(String user_openid, int status, int limit, int page) {
        List<Help> list = template.query("select * from `help` where user_openid = ? and status = ? limit ?,?", new Object[]{user_openid,status,(page-1)*limit,limit},
                new BeanPropertyRowMapper(Help.class));
        if (list!=null&&list.size()!=0){
            for (Help help:list){
                // 设置救助机构对象
                List<Agency> agencies = template.query("select * from `agency` where agency_id = "+help.getAgency_id()  ,
                        new BeanPropertyRowMapper(Agency.class));
                help.setAgency(agencies.get(0));
                // 设置图片集
                String images = help.getImages();
                if(images!=null){
                    String[] images_List = images.split(",");
                    help.setImages_List(Arrays.asList(images_List));
                }
                // 设置地址字符串
                String address = help.getProvince()+" "+help.getCity()+" "+help.getDetail_address();
                help.setAddress(address);
            }
            return list;
        }else{
            return null;
        }
    }

    // 用户发布求助请求
    public boolean publishHelp(String user_openid, String contact_name, String contact_phone, String province, String city, String detail_address, String help_content, String help_images){
        List<Agency> list = agencyDao.findAgencyByCity(city);
        if(!list.isEmpty()){
            Agency agency = new Agency();
            if(list.size()==1){
                agency = list.get(0);
            }
            else {
                Random r = new Random();
                agency = list.get(r.nextInt(list.size()-1));
            }
            int agency_id = agency.getAgency_id();
            template.update("insert into `help` values (null,?,?,?,?,?,?,?,?,?,null,null,null,?,0,null,null)",
                    user_openid, contact_name, contact_phone, province, city, detail_address, help_content, help_images, new Date(), agency_id);
            return true;
        }
        else {
            return false;
        }
    }

    // 用户取消请求
    public int cancelHelp(String user_openid, int help_id){
        return template.update("update `help` set status = 3, cancel_time = ? where user_openid = ? and help_id = ?", new Date(), user_openid, help_id);
    }
}
