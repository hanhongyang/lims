package com.gmlimsqi.task;


import com.gmlimsqi.sap.accept.service.CustomerService;
import com.gmlimsqi.sap.accept.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;

/**
 * 对接sap基础信息定时任务
 */
@Slf4j
@Component("SapTask")
public class SapTask {
    @Autowired
    private MaterialService materialService;
    @Autowired
    private CustomerService customerService;

    /**
     * 同步物料档案
     */
    public void syncBaseInvbill() throws ParseException {
        log.info("定时任务：同步物料档案");
        materialService.syncMateical();
        log.info("定时任务结束：同步物料档案");
    }
    
    /**
     * 同步供应商数据
     */
    public void syncSupplier() throws ParseException, IOException {
        log.info("定时任务：同步供应商、客商数据");
//        businessSupplierService.info();
        customerService.quartzSupplier();
        log.info("定时任务结束：同步供应商、客商数据");
    }
    
}
