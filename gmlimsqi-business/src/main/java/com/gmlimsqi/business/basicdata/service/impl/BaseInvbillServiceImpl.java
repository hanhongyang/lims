package com.gmlimsqi.business.basicdata.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.basicdata.domain.*;
import com.gmlimsqi.business.basicdata.mapper.*;
import com.gmlimsqi.business.basicdata.service.IBaseInvbillService;
import com.gmlimsqi.business.basicdata.service.IBusinessInvbillService;
import com.gmlimsqi.business.basicdata.vo.BusinessInvbillVo;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class BaseInvbillServiceImpl implements IBaseInvbillService {
    
    @Autowired
    private BaseInvbillMapper baseInvbillMapper;
    @Autowired
    private IBusinessInvbillService invbillService;
    @Autowired
    private BusinessInvbillMapper businessInvbillMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private BusinessInvbillInfoMapper businessInvbillInfoMapper;
    @Autowired
    private BsInvbillDeptMapper bsInvbillDeptMapper;
    @Autowired
    private BsInvbillInfoMapper infoMapper;
    
    @Override
    public List<BaseInvbill> selectInvbillList(BaseInvbill baseInvbill) {

        /*if(StringUtils.isEmpty(baseInvbill.getDeptId())){

            if(SecurityUtils.isAdmin(SecurityUtils.getUserId())){
                //超管不过滤部门，显示全部
                //baseInvbill.setOwnDeptId("");
                baseInvbill.setDeptId("");
            }else {
                String sapCode = SecurityUtils.getSapCode();
                if (ObjectUtils.isNotEmpty(sapCode)){
                    baseInvbill.setDeptId(sapCode);
                }
            }

        }*/
        List<BaseInvbill> baseInvbills = baseInvbillMapper.selectInvbillList(baseInvbill);
        // 批量处理用户名（创建人和更新人）
        if (!baseInvbills.isEmpty()) {
            // 收集所有用户ID（创建人和更新人）
            Set<String> userIds = new HashSet<>();

            baseInvbills.forEach(item -> {
                if (StringUtils.isNotEmpty(item.getCreateBy())) {
                    userIds.add(item.getCreateBy());
                }
                if (StringUtils.isNotEmpty(item.getUpdateBy())) {
                    userIds.add(item.getUpdateBy());
                }
            });

            // 批量获取用户名
            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            // 设置用户名到对应的字段
            baseInvbills.forEach(vo -> {
                vo.setCreateBy(usernameMap.getOrDefault(vo.getCreateBy(), vo.getCreateBy()));
                vo.setUpdateBy(usernameMap.getOrDefault(vo.getUpdateBy(), vo.getUpdateBy()));
            });
        }
        for (BaseInvbill invbill : baseInvbills) {
            String sapCodeWith0 = invbill.getSapCode().replaceFirst("^0+", "");
            invbill.setSapCode(sapCodeWith0);
        }

        return baseInvbills;
    }
    
    @Override
    public void syncBaseInvbill() {
    
//        获取一卡通所有物料档案
        List<BusinessInvbill> invbillList =
                invbillService.selectBusinessInvbillList(new BusinessInvbill());
//        获取从库所有物料档案
        List<BusinessInvbillVo> invbillVoList =
                baseInvbillMapper.selectBusinessInvbillList();
//        将一卡通物料档案和从库物料档案进行比对
//        相同sapcode物料做修改
//        List<BusinessInvbillVo> match = invbillVoList.stream()
//                .filter(vo -> invbillList.stream().
//                        anyMatch(invbill ->
//                                invbill.getSapCode().equals(vo.getCinvcode())))
//                .toList();
        
//        不同sapcode物料做新增
        if (!ObjectUtils.isEmpty(invbillVoList)){
            List<BusinessInvbillVo> noMatch;
            if (ObjectUtils.isEmpty(invbillList)) {
                noMatch = invbillVoList;
            } else {
                noMatch = invbillVoList.stream()
                        .filter(vo -> invbillList.stream().
                                noneMatch(invbill ->
                                        invbill.getSapCode().equals(vo.getCinvcode())))
                        .toList();
            }
            invbillService.insertBusinessInvbillBatch(noMatch);
        }
        
//        invbillService.updateBusinessInvbillBatch(match);

    }
    
    @Override
    @Transactional
    public int updateInvbill(BusinessInvbill baseInvbill) {

        String userId = String.valueOf(SecurityUtils.getUserId());
        Date now = DateUtils.getNowDate();
        baseInvbill.setUpdateBy(userId);
        baseInvbill.setUpdateTime(now);
        int count =  businessInvbillMapper.updateBusinessInvbill(baseInvbill);
        if (!CollectionUtil.isEmpty(baseInvbill.getInvbillDepts())) {
            bsInvbillDeptMapper.updateDeleteFlagBySapCode(baseInvbill.getUpdateBy(),baseInvbill.getSapCode());
            for (BsInvbillDept bsInvbillDept : baseInvbill.getInvbillDepts()) {
                bsInvbillDept.setBsInvbillDeptId(IdUtils.simpleUUID());
                bsInvbillDept.fillCreateInfo();
                bsInvbillDeptMapper.insertBsInvbillDept(bsInvbillDept);
            }
        }
        //更新子表
        infoMapper.updateDeleteFlagBySapCode(baseInvbill.getUpdateBy(),baseInvbill.getSapCode());
        BsInvbillInfo invbillInfo = new BsInvbillInfo();
        invbillInfo.setBsInvbillInfoId(IdUtils.simpleUUID());
        invbillInfo.setSapCode(baseInvbill.getSapCode());
        invbillInfo.setTag(baseInvbill.getTag());
        invbillInfo.setIsUploadReport(baseInvbill.getIsUploadReport());
        invbillInfo.fillCreateInfo();
        infoMapper.insertBsInvbillInfo(invbillInfo);
        return count;
    }

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    @Override
    public BusinessInvbill selectInvbillById(String id) {
        BusinessInvbillInfoDTO query = new BusinessInvbillInfoDTO();
        query.setId(id);
        return businessInvbillMapper.getBusinessInvbillWithInfoById(query);
    }

    @Override
    public List<BaseInvbill> selectInvbillListSap(BaseInvbill baseInvbill) {
        if(StringUtils.isEmpty(baseInvbill.getDeptId())){

            if(SecurityUtils.isAdmin(SecurityUtils.getUserId())){
                //超管不过滤部门，显示全部
                //baseInvbill.setOwnDeptId("");
                baseInvbill.setDeptId("");
            }else {
                String sapCode = SecurityUtils.getSapCode();
                if (ObjectUtils.isNotEmpty(sapCode)){
                    baseInvbill.setDeptId(sapCode);
                }
            }
        }

        List<BaseInvbill> baseInvbills = baseInvbillMapper.selectInvbillListSap(baseInvbill);
        // 批量处理用户名（创建人和更新人）
        if (!baseInvbills.isEmpty()) {
            // 收集所有用户ID（创建人和更新人）
            Set<String> userIds = new HashSet<>();

            baseInvbills.forEach(item -> {
                if (StringUtils.isNotEmpty(item.getCreateBy())) {
                    userIds.add(item.getCreateBy());
                }
                if (StringUtils.isNotEmpty(item.getUpdateBy())) {
                    userIds.add(item.getUpdateBy());
                }
            });

            // 批量获取用户名
            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            // 设置用户名到对应的字段
            baseInvbills.forEach(vo -> {
                vo.setCreateBy(usernameMap.getOrDefault(vo.getCreateBy(), vo.getCreateBy()));
                vo.setUpdateBy(usernameMap.getOrDefault(vo.getUpdateBy(), vo.getUpdateBy()));
            });
        }
        for (BaseInvbill invbill : baseInvbills) {
            String sapCodeWith0 = invbill.getSapCode().replaceFirst("^0+", "");
            invbill.setSapCode(sapCodeWith0);
        }

        return baseInvbills;
    }

    /**
     * 新增子表
     */
    public void insertBusinessInvbill(BusinessInvbill businessInvbill) {
        List<BusinessInvbillInfo> businessInvbillInfo = businessInvbill.getBusinessInvbillInfoList();
        String id = businessInvbill.getId();

        if (ObjectUtils.isNotEmpty(businessInvbillInfo))
        {
            List<BusinessInvbillInfo> list = new ArrayList<>();
            for ( BusinessInvbillInfo item : businessInvbillInfo){
                item.setId(IdUtils.fastUUID());
                item.setInvbillId(id);
                item.setCreateBy(SecurityUtils.getUserId().toString());
                item.setCreateTime(DateUtils.getNowDate());
                // 子表有值就为跳过质检
                item.setIzj("1");
                list.add(item);
            }

            if (!list.isEmpty()){
                businessInvbillInfoMapper.batchInsertBusinessInvbillInfo(list);
            }
        }
    }

}
