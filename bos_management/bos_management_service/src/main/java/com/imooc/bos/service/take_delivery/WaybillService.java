package com.imooc.bos.service.take_delivery;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.imooc.bos.domain.take_delivery.WayBill;

/**  
 * ClassName:WaybillService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月25日 下午9:21:59 <br/>       
 */

public interface WaybillService {
    
    /**
     * 保存运单
     * @param model
     */
    void save(WayBill model);


    Page<WayBill> findAll(Pageable pageable);

    void batchImport(List<WayBill> list);
}

