package com.gmlimsqi.business.milktankinspection.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.gmlimsqi.business.milktankinspection.domain.OpMilkTankInspection;
import com.gmlimsqi.business.milktankinspection.mapper.OpMilkTankInspectionMapper;
import com.gmlimsqi.business.milktankinspection.service.IOpMilkTankInspectionService;
import com.gmlimsqi.business.util.IdListHandler;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.system.domain.SysUploadFile;
import com.gmlimsqi.system.mapper.SysUploadFileMapper;
import com.gmlimsqi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 奶罐车质检Service业务层处理
 * * @author hhy
 * @date 2025-11-12
 */
@Service
public class OpMilkTankInspectionServiceImpl implements IOpMilkTankInspectionService
{
    @Autowired
    private OpMilkTankInspectionMapper opMilkTankInspectionMapper;
    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUploadFileMapper sysUploadFileMapper;

    /**
     * 查询奶罐车质检
     * * @param opMilkTankInspectionId 奶罐车质检主键
     * @return 奶罐车质检
     */
    @Override
    public OpMilkTankInspection selectOpMilkTankInspectionByOpMilkTankInspectionId(String opMilkTankInspectionId)
    {
        OpMilkTankInspection opMilkTankInspection = opMilkTankInspectionMapper.
                selectOpMilkTankInspectionByOpMilkTankInspectionId(opMilkTankInspectionId);

        // 处理附件，按逗号分隔
        if (StringUtils.isNotEmpty(opMilkTankInspection.getFile())){
            List<String> list = IdListHandler.parseIdStr(opMilkTankInspection.getFile());
            if (!list.isEmpty()){
                List<String> urlList = new ArrayList<>();
                for (String fileId : list){
                    SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(fileId);
                    urlList.add(sysUploadFile.getUrl());
                }
                opMilkTankInspection.setFileUrl(urlList);
            }
        }

        return opMilkTankInspection;
    }

    /**
     * 查询奶罐车质检列表
     * * @param opMilkTankInspection 奶罐车质检
     * @return 奶罐车质检
     */
    // +++ 新增：@DataScope 注解以激活数据权限切面 +++
//    @DataScope(deptAlias = "d")
    @Override
    public List<OpMilkTankInspection> selectOpMilkTankInspectionList(OpMilkTankInspection opMilkTankInspection)
    {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            opMilkTankInspection.setDeptId(SecurityUtils.getLoginUser().getDeptId().toString());
        }

        List<OpMilkTankInspection> items = opMilkTankInspectionMapper.selectOpMilkTankInspectionList(opMilkTankInspection);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpMilkTankInspection::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增奶罐车质检
     * * @param opMilkTankInspection 奶罐车质检
     * @return 结果
     */
    @Override
    public int insertOpMilkTankInspection(OpMilkTankInspection opMilkTankInspection)
    {
        if (StringUtils.isEmpty(opMilkTankInspection.getOpMilkTankInspectionId())) {
            opMilkTankInspection.setOpMilkTankInspectionId(IdUtils.simpleUUID());
        }

        // +++ 自动填充部门ID，用于数据权限 +++
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser))
        {
            // 假设您的实体类中有名为 setDeptId 的方法
            // (请确保您的 OpMilkTankInspection 实体类中有 setDeptId(String deptId) 方法)
            opMilkTankInspection.setDeptId(loginUser.getDeptId().toString());
        }

        SysUser sysUser = sysUserMapper.selectUserById(loginUser.getUserId());
        opMilkTankInspection.setCreateByName(sysUser.getNickName());

        opMilkTankInspection.setIsDelete("0");
        opMilkTankInspection.setStatus("0");
        opMilkTankInspection.setDeptId(loginUser.getDeptId().toString());

        // 自动填充创建/更新信息
        opMilkTankInspection.fillCreateInfo();
        return opMilkTankInspectionMapper.insertOpMilkTankInspection(opMilkTankInspection);
    }

    /**
     * 修改奶罐车质检
     * * @param opMilkTankInspection 奶罐车质检
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateOpMilkTankInspection(OpMilkTankInspection opMilkTankInspection)
    {
        OpMilkTankInspection old = opMilkTankInspectionMapper.
                selectOpMilkTankInspectionByOpMilkTankInspectionId(opMilkTankInspection.getOpMilkTankInspectionId());

        if ("1".equals(old.getStatus())) {
            throw new IllegalArgumentException("已审核的记录不能修改");
        }

        // 自动填充更新信息
        opMilkTankInspection.fillUpdateInfo();
        return opMilkTankInspectionMapper.updateOpMilkTankInspection(opMilkTankInspection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int audit(String opMilkTankInspectionId) {

        OpMilkTankInspection old = selectOpMilkTankInspectionByOpMilkTankInspectionId(opMilkTankInspectionId);
        if (old == null) {
            throw new IllegalArgumentException("奶罐车质检单不存在");
        }
        if ("1".equals(old.getStatus())) {
            throw new IllegalArgumentException("已审核的奶罐车质检单不能重复审核");
        }

        // 拷贝一份 new（避免直接改 old，导致对比不出来差异）
        OpMilkTankInspection newDO = new OpMilkTankInspection();
        // 方式1：手动复制主键（建议你用 BeanUtils.copyProperties）
        org.springframework.beans.BeanUtils.copyProperties(old, newDO);

        // ======= 设置审核信息 =======
        newDO.setStatus("1");
        newDO.fillUpdateInfo();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            newDO.setReviewerId(loginUser.getUserId().toString());
        }
        SysUser sysUser = sysUserMapper.selectUserById(Long.parseLong(newDO.getReviewerId()));
        if (StringUtils.isNotNull(sysUser)) {
            newDO.setReviewer(sysUser.getNickName());
        }
        newDO.setReviewTime(new Date());

        // 入库更新
        return opMilkTankInspectionMapper.updateOpMilkTankInspection(newDO);
    }

}