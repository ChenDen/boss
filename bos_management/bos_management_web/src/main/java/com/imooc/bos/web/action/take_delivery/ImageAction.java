package com.imooc.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

/**  
 * ClassName:ImageAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月26日 下午3:22:24 <br/>       
 */

@Namespace("/") // 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default") // 等价于struts.xml文件中package节点extends属性
@Scope("prototype")
@Controller // spring 的注解,控制层代码
public class ImageAction extends ActionSupport{
    
    //使用属性驱动获取用户上传的文件
    private File imgFile;
    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }
    
    
    //########################### 上传图片 #############################
    //使用属性驱动获取用户上传的文件名
    private String imgFileFileName;
    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }
    
    @Action("imageAction_upload")
    public String upload() throws IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        
        try {
            //指定保存图片的文件夹
            String dirPath = "/upload";
            
            //获取保存图片的文件夹的绝对磁盘路径
            //C:/javasoftware/apache-tomcat/wtpwebapps/bos_management_web/upload
            ServletContext servletContext = ServletActionContext.getServletContext();
            String dirRealPath = servletContext.getRealPath(dirPath);
            
            //获取文件名的后缀,a.jpg =>不加1 .jpg ,加1 jpg
            String suffix = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
            
            //使用UUID生成文件名
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
            
            //上传的目标文件
            File destFile = new File(dirRealPath + "/" + fileName);
      
            //保存文件
            FileUtils.copyFile(imgFile, destFile );
            
            //上传成功,返回json数据
            //http://localhost:8080/bos_management_web/upload/a.jpg
            String contextPath = servletContext.getContextPath();  //本项目路径
            map.put("error", 0);
            map.put("url", contextPath + "/upload/" +fileName);  
        } catch (IOException e) {
            e.printStackTrace();  
            //上传失败,返回json数据
            map.put("error", 1);
            map.put("message", e.getMessage());
        }
        
        String json = JSONObject.fromObject(map).toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        return NONE;
    }
    
    
    //########################### 开启图片空间 #############################
    @Action(value="imageAction_manager")
    public String manager() throws IOException{
        //指定保存图片的文件夹
        String dirPath = "/upload";
        
        //指定图片扩展名
        String [] fileTypes = new String[] {"gif", "jpg", "jpeg", "png", "bmp"};
        
        //获取保存文件的文件夹的绝对磁盘路径
        ServletContext servletContext = ServletActionContext.getServletContext();
        String dirRealPath = servletContext.getRealPath(dirPath);
        //保存文件的文件夹对象
        File currentPathFile = new File(dirRealPath);
        
        //遍历目录获取文件信息
        List<Hashtable> fileList = new ArrayList<Hashtable>();
        if(currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {
                Hashtable<String, Object> hash = new Hashtable<String, Object>();
                String fileName = file.getName();
                if(file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if(file.isFile()){
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
                fileList.add(hash);
            }
        }
        
        //封装写回客户端的数据
        JSONObject result = new JSONObject();
        
        //封装保存文件的文件夹的路径 : 本项目路径 + 文件夹名
        String contextPath = servletContext.getContextPath();
        result.put("current_url", contextPath + "/upload/");
        
        //封装所有的图片信息
        result.put("file_list", fileList);
        
        //返回数据
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(result.toString());
        
        return NONE;
    }
}
  
