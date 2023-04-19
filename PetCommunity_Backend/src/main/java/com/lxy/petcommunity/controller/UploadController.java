package com.lxy.petcommunity.controller;

import com.lxy.petcommunity.bean.ResBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@RestController
public class UploadController {

    public void copyFile(String src,String destDir,String fileName) throws IOException {
        FileInputStream in=new FileInputStream(src);
        File fileDir = new File(destDir);
        if(!fileDir.isDirectory()){
            fileDir.mkdirs();
        }
        File file = new File(fileDir,fileName);

        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream out=new FileOutputStream(file);
        int c;
        byte buffer[]=new byte[1024];
        while((c=in.read(buffer))!=-1){
            for(int i=0;i<c;i++){
                out.write(buffer[i]);
            }

        }
        in.close();
        out.close();
    }

    // 管理员端上传图片接口
    @PostMapping(value = "/upload", produces = "application/json;charset=UTF-8")
    public ResBody singleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        ResBody resBody = new ResBody();
        File targetFile=null;
        String url="";
        if (file.isEmpty()) {
            resBody.setCode(500);
            resBody.setMsg("文件为空");
        }
        else {
            String fileName = file.getOriginalFilename();
            //图片访问的虚拟文件夹路径
            String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/upload/imgs/";
            //文件临时存储位置
            String path = request.getSession().getServletContext().getRealPath("") + "upload" + File.separator + "imgs";
            //文件后缀
            String fileSuffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            //新的文件名
            fileName = System.currentTimeMillis() + "_" + new Random().nextInt(1000) + fileSuffix;
            //先判断文件是否存在
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //文件创建日期
            String fileAdd = sdf.format(new Date());
            //获取文件夹路径
            path = path + File.separator + fileAdd + File.separator;
            File file1 = new File(path);
            //如果文件夹不存在则创建
            if (!file1.exists() && !file1.isDirectory()) {
                file1.mkdirs();
            }
            //将图片存入文件夹
            targetFile = new File(file1, fileName);
            try {
                //将上传的文件写到服务器上指定的文件。
                file.transferTo(targetFile);
                String projectPath = System.getProperty("user.dir");
                //文件复制
                String src = path + fileName;
                //文件所在文件夹的实际路径
                String destDir = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "upload" + File.separator + "imgs" + File.separator + fileAdd + File.separator;
                //将图片到指定的实际路径
                copyFile(src, destDir, fileName);
                //将文件在服务器上的虚拟路径返回前端，用于管理员预览图片
                url = returnUrl + fileAdd + "/" + fileName;
                resBody.setCode(200);
                resBody.setMsg(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resBody;
    }

    // 宠物救助机构端上传图片接口
    @PostMapping(value = "/agency_upload", produces = "application/json;charset=UTF-8")
    public ResBody singleFileUpload_Agency(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        ResBody resBody = new ResBody();
        File targetFile=null;
        String url="";
        if (file.isEmpty()) {
            resBody.setCode(500);
            resBody.setMsg("文件为空");
        }
        else {
            String fileName = file.getOriginalFilename();
            //图片访问的虚拟文件夹路径
            String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/agency_upload/imgs/";
            //文件临时存储位置
            String path = request.getSession().getServletContext().getRealPath("") + "agency_upload" + File.separator + "imgs";
            //文件后缀
            String fileSuffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            //新的文件名
            fileName = System.currentTimeMillis() + "_" + new Random().nextInt(1000) + fileSuffix;
            //先判断文件是否存在
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //文件创建日期
            String fileAdd = sdf.format(new Date());
            //获取文件夹路径
            path = path + File.separator + fileAdd + File.separator;
            File file1 = new File(path);
            //如果文件夹不存在则创建
            if (!file1.exists() && !file1.isDirectory()) {
                file1.mkdirs();
            }
            //将图片存入文件夹
            targetFile = new File(file1, fileName);
            try {
                //将上传的文件写到服务器上指定的文件。
                file.transferTo(targetFile);
                String projectPath = System.getProperty("user.dir");
                //文件复制
                String src = path + fileName;
                //文件所在文件夹的实际路径
                String destDir = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "agency_upload" + File.separator + "imgs" + File.separator + fileAdd + File.separator;
                //将图片到指定的实际路径
                copyFile(src, destDir, fileName);
                //将文件在服务器上的虚拟路径返回前端，用于管理员预览图片
                url = returnUrl + fileAdd + "/" + fileName;
                resBody.setCode(200);
                resBody.setMsg(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resBody;
    }

    // 用户端上传图片接口
    @PostMapping(value = "/wxuser_upload", produces = "application/json;charset=UTF-8")
    public ResBody WXFileUpload(@RequestParam("file") MultipartFile file, @RequestParam String user_openid, HttpServletRequest request) {
        ResBody resBody = new ResBody();
        File targetFile=null;
        String url="";
        if (file.isEmpty()) {
            resBody.setCode(500);
            resBody.setMsg("文件为空");
        }
        else {
            String fileName = file.getOriginalFilename();
            //图片访问的虚拟文件夹路径
            String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/wxuser_upload/" + user_openid + "/imgs/";
            //文件临时存储位置
            String path = request.getSession().getServletContext().getRealPath("") + "wxuser_upload" + File.separator + user_openid + File.separator + "imgs";
            //文件后缀
            String fileSuffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            //新的文件名
            fileName = System.currentTimeMillis() + "_" + new Random().nextInt(1000) + fileSuffix;
            //先判断文件是否存在
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //文件创建日期
            String fileAdd = sdf.format(new Date());
            //获取文件夹路径
            path = path + File.separator + fileAdd + File.separator;
            File file1 = new File(path);
            //如果文件夹不存在则创建
            if (!file1.exists() && !file1.isDirectory()) {
                file1.mkdirs();
            }
            //将图片存入文件夹
            targetFile = new File(file1, fileName);
            try {
                //将上传的文件写到服务器上指定的文件。
                file.transferTo(targetFile);
                String projectPath = System.getProperty("user.dir");
                //文件复制
                String src = path + fileName;
                //文件所在文件夹的实际路径
                String destDir = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "wxuser_upload" + File.separator + user_openid + File.separator + "imgs" + File.separator + fileAdd + File.separator;
                //将图片到指定的实际路径
                copyFile(src, destDir, fileName);
                //将文件在服务器上的虚拟路径返回前端
                url = returnUrl + fileAdd + "/" + fileName;
                resBody.setCode(200);
                resBody.setMsg(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resBody;
    }


}
