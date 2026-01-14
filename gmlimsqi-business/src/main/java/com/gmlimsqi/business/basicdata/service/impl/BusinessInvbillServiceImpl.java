package com.gmlimsqi.business.basicdata.service.impl;

import com.gmlimsqi.business.basicdata.domain.BsInvbillInfo;
import com.gmlimsqi.business.basicdata.domain.BusinessInvbill;
import com.gmlimsqi.business.basicdata.mapper.BsInvbillInfoMapper;
import com.gmlimsqi.business.basicdata.service.IBusinessInvbillService;
import com.gmlimsqi.business.basicdata.vo.BusinessInvbillVo;
import com.gmlimsqi.business.basicdata.mapper.BusinessInvbillMapper;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.gmlimsqi.business.util.CodeGeneratorUtil.*;

/**
 * 物料档案Service业务层处理
 *
 * @author gmlimsqi
 * @date 2024-12-16
 */
@Service
public class BusinessInvbillServiceImpl implements IBusinessInvbillService {
    
    private static final int BATCH_SIZE = 100;
    private static final int NUM_THREADS = 5; // 线程池大小
    @Autowired
    private BusinessInvbillMapper businessInvbillMapper;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;

    @Autowired
    private BsInvbillInfoMapper infoMapper;

    /**
     * 查询物料档案
     *
     * @param id 物料档案主键
     * @return 物料档案
     */
    @Override
    public BusinessInvbill selectBusinessInvbillById(String id) {
        return businessInvbillMapper.selectBusinessInvbillById(id);
    }
    
    /**
     * 查询物料档案列表
     *
     * @param businessInvbill 物料档案
     * @return 物料档案
     */
    @Override
    public List<BusinessInvbill> selectBusinessInvbillList(BusinessInvbill businessInvbill) {
        return businessInvbillMapper.selectBusinessInvbillList(businessInvbill);
    }
    
    /**
     * 新增物料档案
     *
     * @param businessInvbill 物料档案
     * @return 结果
     */
    @Override
    public int insertBusinessInvbill(BusinessInvbill businessInvbill) {
        businessInvbill.setCreateTime(DateUtils.getNowDate());
        return businessInvbillMapper.insertBusinessInvbill(businessInvbill);
    }
    
    /**
     * 修改物料档案
     *
     * @param businessInvbill 物料档案
     * @return 结果
     */
    @Override
    public int updateBusinessInvbill(BusinessInvbill businessInvbill) {
        businessInvbill.setUpdateTime(DateUtils.getNowDate());
        return businessInvbillMapper.updateBusinessInvbill(businessInvbill);
    }

    
    @Override
    @Transactional
    public void insertBusinessInvbillBatch(List<BusinessInvbillVo> noMatch) {
        List<BusinessInvbill> businessInvbills = new ArrayList<>();
        if (!ObjectUtils.isEmpty(noMatch)) {
            for (BusinessInvbillVo match : noMatch) {
                BusinessInvbill businessInvbill = new BusinessInvbill();
                businessInvbill
                        .setId(IdUtils.fastUUID())
                        .setInvbillName(match.getCinvname())
                        .setSapCode(match.getCinvcode())
                        .setCremark(match.getCremark())
                        .setCclasscode(match.getCclasscode())
                        .setCclassname(match.getCclassname())
                        .setIkz("是".equals(match.getIkz()) ? "是" : "否")
                        .setDeptId(match.getDeptId())
                ;
                
                businessInvbills.add(businessInvbill);
            }
        }
        
        ExecutorService executorService2 =
                Executors.newFixedThreadPool(NUM_THREADS);
        
        if (!ObjectUtils.isEmpty(businessInvbills)) {
            int totalBatches2 =
                    (int) Math.ceil((double) businessInvbills.size() / BATCH_SIZE);
            for (int i = 0; i < totalBatches2; i++) {
                int start = i * BATCH_SIZE;
                int end = Math.min(start + BATCH_SIZE,
                        businessInvbills.size());
                List<BusinessInvbill> batchData2 =
                        businessInvbills.subList(start, end);
                
                executorService2.submit(() ->
                        businessInvbillMapper.insertBusinessInvbillBatch(batchData2));
            }
        }
        shutdownAndAwaitTermination(executorService2);
    }
    
    @Override
    public String selectInfoByMaterialCode(String materialCode) {
        return businessInvbillMapper.selectInfoByMaterialCode(materialCode);
    }
    
    @Override
    public BusinessInvbill selectInfoWithCodeAndSap(String matnr, String werks) {
        return businessInvbillMapper.selectInfoWithCodeAndSap(matnr, werks);
    }
    @Override
    public BusinessInvbill selectInfoBySapCode(String matnr) {
        return businessInvbillMapper.selectInfoBySapCode(matnr);
    }

    @Override
    public int insertInvbill(BusinessInvbill baseInvbill) {
        baseInvbill.setId(IdUtils.fastUUID());
        baseInvbill.setCreateBy(String.valueOf(SecurityUtils.getLoginUser().getUserId()));
        baseInvbill.setCreateTime(DateUtils.getNowDate());
        baseInvbill.setIsSapType("1");

        // 生成物料编码
        String resultNo = null;
        try {
            resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_MANUAL_INVBILL);

            baseInvbill.setInvbillCode(resultNo);
            baseInvbill.setSapCode(resultNo);
        } catch (BusinessException e) {
            throw new RuntimeException("生成物料编码失败: " + e.getMessage());
        }

        BsInvbillInfo invbillInfo = new BsInvbillInfo();
        invbillInfo.setBsInvbillInfoId(IdUtils.simpleUUID());
        invbillInfo.setSapCode(baseInvbill.getSapCode());
        invbillInfo.setTag(baseInvbill.getTag());
        invbillInfo.setIsUploadReport(baseInvbill.getIsUploadReport());
        invbillInfo.fillCreateInfo();
        infoMapper.insertBsInvbillInfo(invbillInfo);

        return businessInvbillMapper.insertBusinessInvbill(baseInvbill);
    }

    // 等待线程池任务完成并优雅关闭线程池
    private void shutdownAndAwaitTermination(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
