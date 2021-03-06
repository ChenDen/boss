package com.imooc.bos.web.action.base;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.imooc.bos.domain.base.Courier;
import com.imooc.bos.domain.base.Standard;
import com.imooc.bos.domain.base.SubArea;
import com.imooc.bos.service.base.CourierService;
import com.imooc.bos.web.action.CommonAction;
import com.imooc.crm.domain.Customer;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**  
 * ClassName:CourierAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午7:48:13 <br/>       
 */

@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class CourierAction extends CommonAction<Courier>{
    
    //通过构造方法传递模型驱动所需的对象类型
    public CourierAction() {
        super(Courier.class);
    }
    
    @Autowired
    private CourierService courierService;
    
    
    //################### 保存快递员信息  ####################
    @Action(value = "courierAction_save", 
       results = {@Result(name = "success",location="/pages/base/courier.html", type="redirect")})
    public String save(){
        courierService.save(getModel());
        return SUCCESS;
    }
    
    
    //################### 分页查询快递员信息  ####################
    @Action("courierAction_pageQuery")
    public String pageQuery() throws IOException{
        
        //查询条件处理, 匿名内部类
        Specification<Courier> specification = new Specification<Courier>() {

            /**
             * 创建一个查询的where语句
             * @param root : 根对象.可以简单的认为就是泛型对象
             * @param cb : 构建查询条件
             * @return a {@link Predicate}, must not be {@literal null}.
             */
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                //取出已封装到model对象中的查询参数
                String courierNum = getModel().getCourierNum();  
                String company = getModel().getCompany();
                String type = getModel().getType();
                Standard standard = getModel().getStandard();
                
                // 存储条件的集合
                List<Predicate> list = new ArrayList<>();
                //校验查询参数
                if(StringUtils.isNotEmpty(courierNum)){
                    //构建一个等值查询条件where courierNum = "001"
                    //参数一:属性名及类型,参数二:具体的要比较的值
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courierNum);
                    list.add(p1);
                }
                if(StringUtils.isNotEmpty(company)){
                    //构建一个模糊查询条件where company like "001"
                    //参数一:属性名及类型,参数二:具体的要比较的值
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%"+company+"%");
                    list.add(p2);
                }
                if(StringUtils.isNotEmpty(type)){
                    //构建一个等值查询条件where type = "001"
                    //参数一:属性名及类型,参数二:具体的要比较的值
                    Predicate p3 = cb.equal(root.get("type").as(String.class), type);
                    list.add(p3);
                }
                if(standard != null){
                    String name = standard.getName();
                    if(StringUtils.isNotEmpty(name)){
                        // 连表查询,查询标准的名字
                        Join<Object, Object> join = root.join("standard");
                        //构建一个等值查询条件where join.name = "001"
                        //参数一:属性名及类型,参数二:具体的要比较的值
                        Predicate p4 = cb.equal(join.get("name").as(String.class), name);
                        list.add(p4);
                    }
                }
                //若都为空,表示用户没有输入查询条件
                if(list.size() == 0){
                    return null;
                }
                
                //用户输入了查询条件,将多个条件进行组合
                Predicate[] arr = new Predicate[list.size()];
                list.toArray(arr);
                
                //用户输入了多少个条件,就让多少个条件同时都满足
                Predicate predicate = cb.and(arr);
                return predicate;
            }
            
        };
        
        
        Pageable pageable = new PageRequest(page - 1, rows);
        //是否使用带条件的分页查询取决于specification是否为null
        Page<Courier> page = courierService.findAll(specification, pageable);
        
        // 在实际开发的时候,为了提高服务器的性能,把前台页面不需要的数据都应该忽略掉
        // 灵活控制输出的内容
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"fixedAreas", "takeTime"});

        page2json(page, jsonConfig);
        
        return NONE;
    }
    
    
    //################### 批量删除快递员信息  ####################
    //使用属性驱动获取要删除的快递员的Id
    private String ids;
    public void setIds(String ids) {
        this.ids = ids;
    }
    
    @Action(value = "courierAction_batchDel",
        results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
    public String batchDel(){
        courierService.batchDel(ids);
        return SUCCESS;
    }
    @Action(value="courierAction_doRestore",results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
    public String doRestore(){
    	courierService.doRestore(ids);
    	return SUCCESS;
    }
    //################### 查询所有在职的快递员  ####################
    // 方法一
    @Action("courierAction_listajax")
    public String listajax() throws IOException{
        List<Courier> list = courierService.findAvaible();
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"fixedAreas","takeTime"});
        
        list2json(list, jsonConfig);
        
        return NONE;
    }
    
    // 方法二
    @Action("courierAction_listajax2")
    public String listajax2() throws IOException{
        //创建一个查询的where语句
        Specification<Courier> specification = new Specification<Courier>() {
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                //比较空值
                //Predicate predicate = cb.equal(root.get("deltag").as(Character.class), null);//错误的写法
                Predicate predicate = cb.isNull(root.get("deltag").as(Character.class));
                return predicate;
            }
        };
        Page<Courier> p = courierService.findAll(specification,null);
        List<Courier> list = p.getContent();
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"fixedAreas","takeTime"});
        
        list2json(list, jsonConfig);
        
        return NONE;
    }
    
    // 查看定区关联的快递员  
    @Action(value="courierAction_findAssociatedCourier") 
    public String findAssociatedCourier() throws IOException{
        List<Courier> list = courierService.findAssociatedCourier(getModel().getId());
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"fixedAreas"});
        list2json(list, jsonConfig);
        return NONE;
    }
    
    
}
  
