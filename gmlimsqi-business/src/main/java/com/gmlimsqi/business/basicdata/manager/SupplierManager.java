package com.gmlimsqi.business.basicdata.manager;

import com.gmlimsqi.business.basicdata.domain.BusinessSupplier;
import com.gmlimsqi.business.basicdata.service.IBusinessSupplierService;
import com.gmlimsqi.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 供应商数据获取
 */
@Service
@Slf4j
public class SupplierManager {
    
    public final static AtomicBoolean CREATE = new AtomicBoolean(false);
    @Autowired
    private IBusinessSupplierService businessSupplierService;
    
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void createSupplier(List<BusinessSupplier> businessSuppliers,
                               SecurityContext context) {
        SecurityContextHolder.setContext(context);
        try {
            long l = System.currentTimeMillis();
            businessSupplierService.saveBatch(businessSuppliers);
            long l1 = System.currentTimeMillis();
            log.info("========================数据耗时=>" + (l1 - l));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据获取失败");
        } finally {
            CREATE.set(false);
            log.info("==========================数据+" + CREATE.get());
        }
        
    }
}
