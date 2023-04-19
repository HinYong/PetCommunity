package com.lxy.petcommunity.dao;

import com.lxy.petcommunity.bean.Adopt;
import com.lxy.petcommunity.bean.Agency;
import com.lxy.petcommunity.bean.PetsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class AdoptDao {
    @Autowired
    JdbcTemplate template;
    @Autowired
    AgencyDao agencyDao;

    // 管理员端
    // 返回所有领养需求数量
    public int getAllAdoptsCount() {
        int count = template.queryForObject("select count(*) from `adopt`", Integer.class);
        return count;
    }

    // 按照机构名称查找领养需求数量
    public int getAdoptsCountByAgencyName(String agency_name) {
        List<Agency> agency_list = template.query("select * from agency where agency_name like '%" + agency_name + "%'", new BeanPropertyRowMapper(Agency.class));
        int count = 0;
        for(Agency agency:agency_list){
            count += template.queryForObject("select count(*) from `adopt` where `agency_id` = " + agency.getAgency_id(), Integer.class);
        }
        return count;
    }

    // 按照关键字查找领养需求数量
    public int getAdoptsCountByKeyword(String Keyword) {
        int count = template.queryForObject("select count(*) from adopt where pet_name like '%" + Keyword + "%' " +
                "or pet_description like '%" + Keyword + "%' " +
                "or adopt_requirement like '%" + Keyword + "%'", Integer.class);
        return count;
    }

    // 管理员端获得所有领养需求，分页取数据
    public List<Adopt> getAllAdopts(int page, int limit) {
        List<Adopt> list;
        list = template.query("select * from `adopt` limit ?,?" ,new Object[]{(page-1)*limit,limit} ,
                    new BeanPropertyRowMapper(Adopt.class));
        if (list!=null&&list.size()!=0){
            for (Adopt adopt:list){
                // 设置宠物种类
                List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + adopt.getPet_type_id(),
                        new BeanPropertyRowMapper(PetsType.class));
                if(!petsTypes.isEmpty()){
                    adopt.setPetsType(petsTypes.get(0));
                }
                // 设置救助机构对象
                List<Agency> agencies = template.query("select * from `agency` where agency_id = "+adopt.getAgency_id()  ,
                        new BeanPropertyRowMapper(Agency.class));
                adopt.setAgency(agencies.get(0));
            }
            return list;
        }else{
            return null;
        }
    }

    // 根据机构名称查找领养需求返回到管理员端，分页取数据
    public List<Adopt> findAdoptsByAgencyName(int page, int limit, String agency_name) {
        List<Agency> agency_list = template.query("select * from agency where agency_name like '%" + agency_name + "%'", new BeanPropertyRowMapper(Agency.class));
        if (agency_list != null && agency_list.size() != 0) {
            List<Adopt> list = new ArrayList<>();
            for (Agency agency : agency_list) {
                List<Adopt> adopts = template.query("select * from `adopt` where agency_id = ? limit ?,?", new Object[]{agency.getAgency_id(), (page - 1) * limit, limit},
                        new BeanPropertyRowMapper(Adopt.class));
                if (adopts != null && adopts.size() != 0) {
                    for (Adopt adopt : adopts) {
                        // 设置宠物种类
                        List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + adopt.getPet_type_id(),
                                new BeanPropertyRowMapper(PetsType.class));
                        if(!petsTypes.isEmpty()){
                            adopt.setPetsType(petsTypes.get(0));
                        }
                        // 设置救助机构对象
                        adopt.setAgency(agency);
                        list.add(adopt);
                    }
                }
            }
            return list;
        }
        else{
            return null;
        }
    }

    // 根据关键字查找领养需求返回到管理员端，分页取数据
    public List<Adopt> findAdoptsByKeyword(int page, int limit, String Keyword) {
        List<Adopt> list = template.query("select * from adopt where pet_name like '%" + Keyword + "%' " +
                "or pet_description like '%" + Keyword + "%' " +
                "or adopt_requirement like '%" + Keyword + "%' limit ?,?", new Object[]{(page - 1) * limit, limit}, new BeanPropertyRowMapper(Adopt.class));
        if (!list.isEmpty()) {
            for (Adopt adopt : list) {
                // 设置宠物种类
                List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + adopt.getPet_type_id(),
                        new BeanPropertyRowMapper(PetsType.class));
                if(!petsTypes.isEmpty()){
                    adopt.setPetsType(petsTypes.get(0));
                }
                // 设置救助机构对象
                List<Agency> agencies = template.query("select * from `agency` where agency_id = " + adopt.getAgency_id(),
                        new BeanPropertyRowMapper(Agency.class));
                adopt.setAgency(agencies.get(0));
            }
            return list;
        } else {
            return null;
        }
    }

    // 管理员端和宠物救助机构端删除领养需求，需要删除文件夹的图片
    public int delAdopt(int adopt_id) {
        // 获取上传图片路径
        String adopt_images = template.queryForObject("select images from `adopt` where adopt_id = "+ adopt_id, String.class);
        if(adopt_images!=null){
            String[] adopt_images_List = adopt_images.split(",");
            // 删除之前上传的轮播图
            for(String s:adopt_images_List){
                s = s.replace("http://localhost:8081/", "src/main/resources/static/");
                File file1 = new File(s);
                file1.delete();
            }
        }
        return template.update("DELETE from `adopt` where adopt_id = ?",adopt_id);
    }

    // 宠物保护机构端
    // 返回指定状态的所有领养需求数量到宠物保护机构端
    public int getAllAdoptsCountByAgencyIdAndStatus(int agency_id, int status) {
        int count = template.queryForObject("select count(*) from `adopt` where agency_id=" + agency_id + " and status = "+status, Integer.class);
        return count;
    }

    // 宠物保护机构端获得指定状态的所有领养需求，分页取数据
    public List<Adopt> getAllAdoptsByAgencyIdAndStatus(int page, int limit, int agency_id, int status) {
        List<Adopt> list;
        list = template.query("select * from `adopt` where agency_id = ? and status = ? limit ?,?" ,new Object[]{agency_id,status,(page-1)*limit,limit} ,
                new BeanPropertyRowMapper(Adopt.class));
        if (list!=null&&list.size()!=0){
            for(Adopt adopt:list){
                // 设置宠物种类
                List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + adopt.getPet_type_id(),
                        new BeanPropertyRowMapper(PetsType.class));
                if(!petsTypes.isEmpty()){
                    adopt.setPetsType(petsTypes.get(0));
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 返回指定状态与含有关键字的领养需求数量到宠物保护机构端
    public int getAdoptsCountByKeywordAndStatus(String Keyword, int agency_id, int status) {
        int count = template.queryForObject("select count(*) from adopt where (pet_name like '%" + Keyword + "%' " +
                "or pet_description like '%" + Keyword + "%' " +
                "or adopt_requirement like '%" + Keyword + "%') and agency_id = " + agency_id + " and status = " + status, Integer.class);
        return count;
    }

    // 宠物保护机构端获得指定状态与含有关键字的领养需求，分页取数据
    public List<Adopt> findAdoptsByKeywordAndStatus(int page, int limit, String Keyword, int agency_id, int status) {
        List<Adopt> list = template.query("select * from adopt where (pet_name like '%" + Keyword + "%' " +
                "or pet_description like '%" + Keyword + "%' " +
                "or adopt_requirement like '%" + Keyword + "%') and agency_id = ? and status = ? limit ?,?", new Object[]{agency_id, status, (page - 1) * limit, limit}, new BeanPropertyRowMapper(Adopt.class));
        if (!list.isEmpty()) {
            for(Adopt adopt:list){
                // 设置宠物种类
                List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + adopt.getPet_type_id(),
                        new BeanPropertyRowMapper(PetsType.class));
                if(!petsTypes.isEmpty()){
                    adopt.setPetsType(petsTypes.get(0));
                }
            }
            return list;
        } else {
            return null;
        }
    }

    // 根据领养号和状态查找领养需求返回到宠物保护机构端
    public List<Adopt> findAdoptByAdoptIdAndStatus(int page, int limit, int adopt_id, int agency_id, int status) {
        List<Adopt> list = template.query("select * from `adopt` where `adopt_id` = ? and agency_id = ? and status = ? limit ?,?" ,new Object[]{adopt_id,agency_id,status,(page-1)*limit,limit},
                new BeanPropertyRowMapper(Adopt.class));
        if (list!=null&&list.size()!=0){
            for(Adopt adopt:list){
                // 设置宠物种类
                List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + adopt.getPet_type_id(),
                        new BeanPropertyRowMapper(PetsType.class));
                if(!petsTypes.isEmpty()){
                    adopt.setPetsType(petsTypes.get(0));
                }
            }
            return list;
        }else{
            return null;
        }
    }

    // 发布领养需求
    public int publishAdopt(Adopt adopt) {
        return template.update("insert into adopt values(null,?,?,?,?,null,?,?,?,null,0)",
                adopt.getAgency_id(), adopt.getPet_type_id(), adopt.getPet_name(), adopt.getPet_age(),
                adopt.getPet_description(), adopt.getAdopt_requirement(), new Date());
    }

    // 更新领养需求
    public int updateAdopt(Adopt adopt) {
        return template.update("update adopt set pet_type_id = ?, `pet_name` = ?, pet_age = ?, " +
                        "pet_description = ?, adopt_requirement = ?" +
                        "where adopt_id = ?",adopt.getPet_type_id(), adopt.getPet_name(), adopt.getPet_age(),
                        adopt.getPet_description(), adopt.getAdopt_requirement(), adopt.getAdopt_id());
    }

    // 更新图片，需要将原来的轮播图从服务器上删除
    public int updateAdoptImages(int adopt_id, String images) {
        // 获取之前的轮播图路径
        String old_images = template.queryForObject("select images from adopt where adopt_id = "+ adopt_id, String.class);
        if(old_images!=null){
            String[] old_Images_List = old_images.split(",");
            // 删除之前上传的旧的轮播图
            for(String s:old_Images_List){
                s = s.replace("http://localhost:8081/", "src/main/resources/static/");
                File file = new File(s);
                file.delete();
            }
        }
        // 更新图片路径
        return template.update("update adopt set `images` = ? where adopt_id = ?", images, adopt_id);
    }

    // 若领养申请人条件不符，则会将该需求重新发布，状态置为0
    public int republishAdopt(int adopt_id){
        return template.update("update `adopt` set status = ? where adopt_id = ?",0, adopt_id);
    }

    // 领养申请人符合领养资格，并且到线下完成了所有手续，领养成功，需求结束，状态置为2
    public int finishAdopt(int adopt_id){
        return template.update("update `adopt` set status = ?, finish_time = ? where adopt_id = ?",2, new Date(), adopt_id);
    }


    // 用户端
    // 按宠物种类分页返回所有发布的领养需求，若有搜索内容，则对于搜索内容进行查询
    public List getAllPublishAdoptsByPetType(int page, int limit, int pet_type_id, String searchContent) {
        List<Adopt> list;
        // 无搜索内容
        if (searchContent==null||searchContent.equals("")||searchContent.equals("null")){
            list = template.query("select * from adopt where status = 0 and pet_type_id = ? limit ?,?" ,
                    new Object[]{pet_type_id,(page-1)*limit,limit}, new BeanPropertyRowMapper(Adopt.class));
            if (!list.isEmpty()) {
                for (Adopt adopt : list) {
                    // 设置救助机构对象
                    List<Agency> agencies = template.query("select * from `agency` where agency_id = " + adopt.getAgency_id(),
                            new BeanPropertyRowMapper(Agency.class));
                    adopt.setAgency(agencies.get(0));
                    // 设置宠物种类
                    List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + adopt.getPet_type_id(),
                            new BeanPropertyRowMapper(PetsType.class));
                    if (!petsTypes.isEmpty()) {
                        adopt.setPetsType(petsTypes.get(0));
                    }
                    // 设置图片列表
                    String images = adopt.getImages();
                    if (images != null) {
                        String[] images_List = images.split(",");
                        adopt.setImages_List(Arrays.asList(images_List));
                    }
                }
            }
        }
        // 有搜索内容
        else {
            // 先从领养描述内容进行搜索
            list = template.query("select * from adopt where (pet_name like '%" + searchContent + "%' " +
                            "or pet_description like '%" + searchContent + "%' " +
                            "or adopt_requirement like '%" + searchContent + "%') and pet_type_id = ? and status = 0 limit ?,?",
                    new Object[]{pet_type_id, (page - 1) * limit, limit}, new BeanPropertyRowMapper(Adopt.class));
            if (!list.isEmpty()) {
                for (Adopt adopt : list) {
                    // 设置救助机构对象
                    List<Agency> agencies = template.query("select * from `agency` where agency_id = " + adopt.getAgency_id(),
                            new BeanPropertyRowMapper(Agency.class));
                    adopt.setAgency(agencies.get(0));
                    // 设置宠物种类
                    List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + adopt.getPet_type_id(),
                            new BeanPropertyRowMapper(PetsType.class));
                    if (!petsTypes.isEmpty()) {
                        adopt.setPetsType(petsTypes.get(0));
                    }
                    // 设置图片列表
                    String images = adopt.getImages();
                    if (images != null) {
                        String[] images_List = images.split(",");
                        adopt.setImages_List(Arrays.asList(images_List));
                    }
                }
            }
            // 再从发布机构所在地区进行搜索
            List<Agency> agencyList = agencyDao.findAgencyByKeyword(searchContent);
            if (agencyList!=null) {
                for (Agency agency : agencyList) {
                    List<Adopt> list2 = template.query("select * from adopt where pet_type_id = ? and agency_id = ? and status = 0 limit ?,?", new Object[]{pet_type_id, agency.getAgency_id(), (page - 1) * limit, limit}, new BeanPropertyRowMapper(Adopt.class));
                    if (!list2.isEmpty()) {
                        for (Adopt adopt : list2) {
                            boolean is_exist = false;  // adopt是否已经在list中存在的标志
                            // 检索list中是否含有adopt
                            if(!list.isEmpty()){
                                for(Adopt a:list){
                                    if (a.getAdopt_id()==adopt.getAdopt_id()){
                                        is_exist = true;
                                        break;
                                    }
                                }
                            }
                            // list中没有该条目才添加
                            if(is_exist==false){
                                // 设置救助机构对象
                                adopt.setAgency(agency);
                                // 设置宠物种类
                                List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + adopt.getPet_type_id(),
                                        new BeanPropertyRowMapper(PetsType.class));
                                if (!petsTypes.isEmpty()) {
                                    adopt.setPetsType(petsTypes.get(0));
                                }
                                // 设置图片列表
                                String images = adopt.getImages();
                                if (images != null) {
                                    String[] images_List = images.split(",");
                                    adopt.setImages_List(Arrays.asList(images_List));
                                }
                                list.add(adopt);
                            }
                        }
                    }
                }
            }
        }
        // 返回列表
        if(!list.isEmpty()){
            return list;
        }
        else {
            return null;
        }
    }

    // 根据领养号查找领养需求
    public Adopt findAdoptByAdoptId(int adopt_id) {
        List<Adopt> list = template.query("select * from `adopt` where `adopt_id` = " + adopt_id, new BeanPropertyRowMapper(Adopt.class));
        if (list!=null&&list.size()!=0){
            Adopt adopt=list.get(0);
            // 设置救助机构对象
            List<Agency> agencies = template.query("select * from `agency` where agency_id = "+adopt.getAgency_id()  ,
                    new BeanPropertyRowMapper(Agency.class));
            adopt.setAgency(agencies.get(0));
            // 设置宠物种类
            List<PetsType> petsTypes = template.query("select * from pets_type where pet_type_id = " + adopt.getPet_type_id(),
                    new BeanPropertyRowMapper(PetsType.class));
            if(!petsTypes.isEmpty()){
                adopt.setPetsType(petsTypes.get(0));
            }
            // 设置图片列表
            String images = adopt.getImages();
            if(images!=null){
                String[] images_List = images.split(",");
                adopt.setImages_List(Arrays.asList(images_List));
            }
            return adopt;
        }else{
            return null;
        }
    }
}
