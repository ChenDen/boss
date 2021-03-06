package com.imooc.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.imooc.crm.domain.Customer;

/**  
 * ClassName:CustomerService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月19日 下午3:41:08 <br/>       
 */

@Consumes(MediaType.APPLICATION_JSON)   //指定参数的数据传输格式,若写在接口上,对所有方法都有效
@Produces(MediaType.APPLICATION_JSON)   //指定返回值的数据传输格式,若写在接口上,对所有方法都有效
public interface CustomerService {
    
    
    /**  
     * 查询所有客户
     */
    @GET
    @Path("/findAll")
    List<Customer> findAll();
    
    
    /**  
     * 查询未关联定区的客户
     */
    @GET
    @Path("/findCustomersUnAssociated")
    List<Customer> findCustomersUnAssociated();
    
    
    /**  
     * 查询已关联定区的客户
     */
    @GET
    @Path("/findCustomersAssociated2FixedArea")
    List<Customer> findCustomersAssociated2FixedArea(@QueryParam("fixedAreaId") String fixedAreaId);
    
    
    /**
     * 定区ID,要关联的数据
     * 根据定区ID,把关联到指定定区的所有客户全部解绑,将要关联的数据和定区ID进行绑定
     */
    @PUT
    @Path("/assignCustomers2FixedArea")
    void assignCustomers2FixedArea(@QueryParam("fixedAreaId") String fixedAreaId,
            @QueryParam("customerIds") Long[] customerIds);
    
    /**
     * 定区ID,要关联的数据
     * 当要关联的数据为空时,根据定区ID,把关联到指定定区的所有客户全部解绑
     */
    @PUT
    @Path("/assignCustomers2FixedArea2")
    void assignCustomers2FixedArea2(@QueryParam("fixedAreaId") String fixedAreaId);
    
    
    /**
     * 注册保存客户
     */
    @POST
    @Path("/save")
    void save(Customer customer);
    
    
    /**
     * 激活用户
     */
    @PUT
    @Path("/active")
    void active(@QueryParam("telephone") String telephone);
    
    
    /**
     * 检查用户是否激活
     */
    @GET
    @Path("/isActived")
    Customer isActived(@QueryParam("telephone") String telephone);
    
    
    /**
     * 登录
     */
    @GET
    @Path("/login")
    Customer login(@QueryParam("telephone") String telephone,@QueryParam("password") String password);
   
    
    /**
     * 根据地址查询定区ID
     */
    @GET
    @Path("/findFixedAreaIdByAdddress")
    String findFixedAreaIdByAdddress(@QueryParam("address") String address);
}
  
