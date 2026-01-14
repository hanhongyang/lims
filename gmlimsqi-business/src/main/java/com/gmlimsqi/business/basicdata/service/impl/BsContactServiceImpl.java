package com.gmlimsqi.business.basicdata.service.impl;

import com.gmlimsqi.business.basicdata.domain.BsContact;
import com.gmlimsqi.business.basicdata.domain.BsContactInfo;
import com.gmlimsqi.business.basicdata.dto.BsContactDto;
import com.gmlimsqi.business.basicdata.mapper.BsContactInfoMapper;
import com.gmlimsqi.business.basicdata.mapper.BsContactMapper;
import com.gmlimsqi.business.basicdata.service.IBsContactService;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.enums.ReceiveEnum;
import com.gmlimsqi.common.enums.YesNo2Enum;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.DesensitizedUtil;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 通讯方式Service业务层处理
 *
 * @author wgq
 * @date 2025-09-15
 */
@Slf4j
@Service
public class BsContactServiceImpl implements IBsContactService {
    @Autowired
    private BsContactMapper bsContactMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private BsContactInfoMapper bsContactInfoMapper;

    /**
     * 查询通讯方式
     *
     * @param id 通讯方式主键
     * @return 通讯方式
     */
    @Override
    public BsContactDto selectBsContactById(String id) {
        BsContact bsContact = bsContactMapper.selectBsContactById(id);
        if (ObjectUtils.isEmpty(bsContact)) {
            throw new RuntimeException("服务器网络异常!");
        }
        //bsContact.setEmail(DesensitizedUtil.emailDesensitization(bsContact.getEmail()));
        BsContactDto bsContactDto = new BsContactDto();
        BeanUtils.copyProperties(bsContact, bsContactDto);
        List<BsContactInfo> bsContactInfos = bsContactInfoMapper.selectWithParentId(id);
        bsContactDto.setBsContactInfoList(bsContactInfos);
        return bsContactDto;
    }

    /**
     * 查询通讯方式列表
     *
     * @param bsContact 通讯方式
     * @return 通讯方式
     */
    @Override
    public List<BsContact> selectBsContactList(BsContact bsContact) {
        List<BsContact> items = bsContactMapper.selectBsContactList(bsContact);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(BsContact::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo -> {
                vo.setCreateBy(usernameMap.get(vo.getCreateBy()));  // 设置 createBy 字段
                //vo.setEmail(DesensitizedUtil.emailDesensitization(vo.getEmail()));
            });
        }
        return items;
    }

    /**
     * 新增通讯方式
     *
     * @param bsContactDto 通讯方式
     * @return 结果
     */
    @Override
    @Transactional
    public int insertBsContact(BsContactDto bsContactDto) {
        String username = SecurityUtils.getUsername();
        String bsContactId = bsContactDto.getId();

        if (StringUtils.isEmpty(bsContactId)) {
            bsContactId = IdUtils.simpleUUID();
            bsContactDto.setId(bsContactId);
        }
        //批量插入子表信息
        List<BsContactInfo> bsContactInfoList = bsContactDto.getBsContactInfoList();
        int flagCount = 0;
        if (!ObjectUtils.isEmpty(bsContactInfoList)) {
            for (BsContactInfo bsContactInfo : bsContactInfoList) {
                bsContactInfo.setBsContactId(bsContactId);
                bsContactInfo.setBsContactInfoId(IdUtils.simpleUUID());
                bsContactInfo.fillCreateInfo();
                String isReceive = bsContactInfo.getIsReceive();
                if (!ObjectUtils.isEmpty(isReceive) ) {
                    flagCount++;
                }
            }
//            if (flagCount > 1) {
//                throw new RuntimeException("只允许一人接收报告");
//            }
            if (flagCount == 0) {
                throw new RuntimeException("请设置接收报告的操作人员");
            }
            //批量插入子表
            bsContactInfoMapper.batchInsertBsContactInfo(bsContactInfoList);
        }

        BsContact bsContact = new BsContact();
        BeanUtils.copyProperties(bsContactDto, bsContact);
        bsContact.fillCreateInfo();
        try {
            return bsContactMapper.insertBsContact(bsContact);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("不允许对同一部门重复添加记录");

        }

    }

    /**
     * 修改通讯方式
     *
     * @param bsContactDto 通讯方式
     * @return 结果
     */
    @Override
    @Transactional
    public int updateBsContact(BsContactDto bsContactDto) {
        String userId = String.valueOf(SecurityUtils.getUserId());
        String bsContactId = bsContactDto.getId();
        if (ObjectUtils.isEmpty(bsContactId)) {
            throw new RuntimeException("服务器网络错误");
        }
        BsContact bsContact = new BsContact();
        BeanUtils.copyProperties(bsContactDto, bsContact);
        // 自动填充更新信息
        bsContact.fillUpdateInfo();
        List<BsContactInfo> bsContactInfoList = bsContactDto.getBsContactInfoList();
        int flagCount = 0;
        Date now = DateUtils.getNowDate();
        //先删除，在批量插入
//        if (!ObjectUtils.isEmpty(bsContactInfoList)) {
            bsContactInfoMapper.deleteWithParentId(now,userId, bsContactId);
            String userName = SecurityUtils.getUsername();

            for (BsContactInfo bsContactInfo : bsContactInfoList) {
                bsContactInfo.setBsContactId(bsContactId);
                bsContactInfo.setBsContactInfoId(IdUtils.simpleUUID());
                bsContactInfo.fillCreateInfo();
                String isReceive = bsContactInfo.getIsReceive();
                if (!ObjectUtils.isEmpty(isReceive) ) {
                    flagCount++;
                }
            }
            if (!ObjectUtils.isEmpty(bsContactInfoList)) {
//                if (flagCount > 1) {
//                    throw new RuntimeException("只允许一人接收报告");
//                }
                if (flagCount == 0) {
                    throw new RuntimeException("请设置接收报告的操作人员");
                }
                bsContactInfoMapper.batchInsertBsContactInfo(bsContactInfoList);
            }

//        }
        return bsContactMapper.updateBsContact(bsContact);
    }

    /**
     * 批量删除通讯方式
     *
     * @param ids 需要删除的通讯方式主键
     * @return 结果
     */
    @Override
    public int deleteBsContactByIds(String[] ids) {
        String userId = String.valueOf(SecurityUtils.getLoginUser().getUserId());
        Date now = DateUtils.getNowDate();
        if (ids.length > 0) {
            for (String id : ids) {
                bsContactInfoMapper.deleteWithParentId(now, userId,id);
                bsContactMapper.updateDeleteFlagById(id);
            }
        }
        return ids.length;
    }

    /**
     * 删除通讯方式信息
     *
     * @param id 通讯方式主键
     * @return 结果
     */
    @Override
    public int deleteBsContactById(String id) {
        String userId = String.valueOf(SecurityUtils.getLoginUser().getUserId());
        Date now = DateUtils.getNowDate();
        bsContactInfoMapper.deleteWithParentId(now,userId,id);
        return bsContactMapper.updateDeleteFlagById(id);
    }

    /**
     * 启用/弃用通讯方式
     *
     * @param bsContact 通讯方式
     * @return
     */
    @Override
    public int enableBsContact(BsContact bsContact) {
        String id = bsContact.getId();
        if (ObjectUtils.isEmpty(id)) {
            log.error("未获取到单据id");
            throw new RuntimeException("服务器网络异常");
        }
        String isEnable = bsContact.getIsEnable();
        if (ObjectUtils.isEmpty(isEnable)) {
            log.error("未获取到启用/弃用的按钮状态");
            throw new RuntimeException("服务器网络异常");
        }
        BsContact bsc = bsContactMapper.selectBsContactById(id);
        bsc.setIsEnable(isEnable);
        return bsContactMapper.updateBsContact(bsc);
    }

    @Override
    public BsContactDto getInfoByDeptId(String deptId) {
        BsContact query = new BsContact();
        query.setDeptId(deptId);
        query.setIsEnable(YesNo2Enum.YES.getCode());
        List<BsContact> bsContacts = selectBsContactList(query);
        BsContact result = new BsContact();
        BsContactDto bsContactDto = new BsContactDto();
        if (!CollectionUtils.isEmpty(bsContacts)) {
            result = bsContacts.get(0);
            List<BsContactInfo> bsContactInfos = bsContactInfoMapper.selectWithParentId(result.getId());

            BeanUtils.copyProperties(result, bsContactDto);
            bsContactDto.setBsContactInfoList(bsContactInfos);

        }

        return bsContactDto;
    }
}
