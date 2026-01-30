package com.gmlimsqi.business.milkbinqualityinspection.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.gmlimsqi.business.milkbinqualityinspection.domain.OpMilkBinQualityInspection;
import com.gmlimsqi.business.milkbinqualityinspection.mapper.OpMilkBinQualityInspectionMapper;
import com.gmlimsqi.business.milkbinqualityinspection.service.IOpMilkBinQualityInspectionService;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.IdListHandler;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.annotation.DataScope;
import com.gmlimsqi.system.domain.SysUploadFile;
import com.gmlimsqi.system.mapper.SysUploadFileMapper;
import com.gmlimsqi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.gmlimsqi.business.util.CodeGeneratorUtil.CODE_TYPE_MILKBIN_QUALITY_INSPECTION;

/**
 * 奶仓质检单Service业务层处理
 * * @author hhy
 * @date 2025-11-10
 */
@Service
public class OpMilkBinQualityInspectionServiceImpl implements IOpMilkBinQualityInspectionService
{
    @Autowired
    private OpMilkBinQualityInspectionMapper opMilkBinQualityInspectionMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUploadFileMapper sysUploadFileMapper;

    /**
     * 查询奶仓质检单
     * * @param id 奶仓质检单主键
     * @return 奶仓质检单
     */
    @Override
    public OpMilkBinQualityInspection selectOpMilkBinQualityInspectionById(String id)
    {
        OpMilkBinQualityInspection opMilkBinQualityInspection = opMilkBinQualityInspectionMapper.selectOpMilkBinQualityInspectionById(id);

        if (opMilkBinQualityInspection.getAttachmentList() != null) {
            // 处理附件，按逗号分隔
            if (StringUtils.isNotEmpty(opMilkBinQualityInspection.getAttachmentList())){
                List<String> list = IdListHandler.parseIdStr(opMilkBinQualityInspection.getAttachmentList());
                if (!list.isEmpty()){
                    List<String> urlList = new ArrayList<>();
                    for (String fileId : list){
                        SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(fileId);
                        urlList.add(sysUploadFile.getUrl());
                    }
                    opMilkBinQualityInspection.setAttachmentListUrl(urlList);
                }
            }

            if(!StringUtils.isEmpty(opMilkBinQualityInspection.getAlcoholPhoto())){
                SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkBinQualityInspection.getAlcoholPhoto());
                opMilkBinQualityInspection.setAlcoholPhotoUrl(sysUploadFile.getUrl());
            }
        }

        return opMilkBinQualityInspection;
    }

    /**
     * 查询奶仓质检单列表
     * @param opMilkBinQualityInspection 奶仓质检单
     * @return 奶仓质检单
     */
    // +++ 新增：@DataScope 注解以激活数据权限切面 +++
    @Override
    public List<OpMilkBinQualityInspection> selectOpMilkBinQualityInspectionList(OpMilkBinQualityInspection opMilkBinQualityInspection)
    {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            opMilkBinQualityInspection.setDeptId(SecurityUtils.getDeptId().toString());
        }

        List<OpMilkBinQualityInspection> items = opMilkBinQualityInspectionMapper.selectOpMilkBinQualityInspectionList(opMilkBinQualityInspection);

        // 批量处理用户名
        if (!items.isEmpty()) {
            // 提取所有创建人ID
            Set<String> userIds = items.stream()
                    .map(OpMilkBinQualityInspection::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));

            // 提取所有审核人ID
            Set<String> reviewerIds = items.stream()
                    .map(OpMilkBinQualityInspection::getReviewer)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> reviewerMap = userCacheService.batchGetUsernames(reviewerIds);

            items.forEach(vo ->
                    vo.setReviewer(reviewerMap.get(vo.getReviewer())));
        }

        return items;
    }

    /**
     * 新增奶仓质检单
     * * @param opMilkBinQualityInspection 奶仓质检单
     * @return 结果
     */
    @Override
    public String insertOpMilkBinQualityInspection(OpMilkBinQualityInspection opMilkBinQualityInspection)
    {
        if (StringUtils.isEmpty(opMilkBinQualityInspection.getId())) {
            opMilkBinQualityInspection.setId(IdUtils.simpleUUID());
        }

        try {
            String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_MILKBIN_QUALITY_INSPECTION);
            opMilkBinQualityInspection.setInspectionNumber(resultNo);
        } catch (BusinessException e) {
            throw new RuntimeException("生成奶仓质检单编号失败: " + e.getMessage());
        }

        // +++ 自动填充部门ID，用于数据权限 +++
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser))
        {
            // 假设您的实体类中有名为 setDeptId 的方法
            // (请确保您的 OpMilkBinQualityInspection 实体类中有 setDeptId(String deptId) 方法)
            opMilkBinQualityInspection.setDeptId(loginUser.getDeptId().toString());
        }

        opMilkBinQualityInspection.setStatus("0");
        opMilkBinQualityInspection.setIsDelete("0");

        // 自动填充创建/更新信息
        opMilkBinQualityInspection.fillCreateInfo();
        opMilkBinQualityInspectionMapper.insertOpMilkBinQualityInspection(opMilkBinQualityInspection);
        return opMilkBinQualityInspection.getId();
    }

    /**
     * 修改奶仓质检单
     * * @param opMilkBinQualityInspection 奶仓质检单
     * @return 结果
     */
    @Override
    public String updateOpMilkBinQualityInspection(OpMilkBinQualityInspection opMilkBinQualityInspection)
    {
        /*if ("1".equals(opMilkBinQualityInspection.getStatus())){
            throw new IllegalArgumentException("已审核的质检单不能修改");
        }*/

        // 自动填充更新信息
        opMilkBinQualityInspection.fillUpdateInfo();
        opMilkBinQualityInspectionMapper.updateOpMilkBinQualityInspection(opMilkBinQualityInspection);
        return opMilkBinQualityInspection.getId();
    }

    /**
     * 审核奶仓质检单
     * @param id
     * @return
     */
    @Override
    public int auditOpMilkBinQualityInspection(String id) {
        // 根据id查询质检单
        OpMilkBinQualityInspection opMilkBinQualityInspection = selectOpMilkBinQualityInspectionById(id);
        if (opMilkBinQualityInspection == null) {
            throw new IllegalArgumentException("质检单不存在");
        }

        if ("1".equals(opMilkBinQualityInspection.getStatus())){
            throw new IllegalArgumentException("已审核的质检单不能重复审核");
        }

        // 审核质检单
        opMilkBinQualityInspection.setStatus("1");

        // 根据用户id查询
        SysUser sysUser = sysUserMapper.selectUserById(SecurityUtils.getUserId());
        if (sysUser == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        opMilkBinQualityInspection.setReviewerId(SecurityUtils.getUserId().toString());
        opMilkBinQualityInspection.setReviewer(sysUser.getNickName());
        opMilkBinQualityInspection.setReviewTime(new Date());

        // 自动填充更新信息
        opMilkBinQualityInspection.fillUpdateInfo();
        return opMilkBinQualityInspectionMapper.updateOpMilkBinQualityInspection(opMilkBinQualityInspection);
    }

    @Override
    public List<OpMilkBinQualityInspection> listDay(OpMilkBinQualityInspection opMilkBinQualityInspection) {
        // 如果检测开始和结束时间为空，默认查询当前天的记录
        if (opMilkBinQualityInspection.getTestStartTime() == null && opMilkBinQualityInspection.getTestEndTime() == null) {
            // 1. 获取Calendar实例（设置默认时区，避免时区偏移）
            Calendar cal = Calendar.getInstance();
            // 重置时分秒毫秒为0（当天00:00:00.000）
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date todayStart = cal.getTime();

            // 2. 设置为当天23:59:59.999
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date todayEnd = cal.getTime();

            // 给查询条件赋值
            opMilkBinQualityInspection.setTestStartTime(todayStart);
            opMilkBinQualityInspection.setTestEndTime(todayEnd);
        }

        if (SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            opMilkBinQualityInspection.setDeptId(SecurityUtils.getDeptId().toString());
        }

        List<OpMilkBinQualityInspection> items = opMilkBinQualityInspectionMapper.selectOpMilkBinQualityInspectionList(opMilkBinQualityInspection);

        // 批量处理用户名
        if (!items.isEmpty()) {
            // 提取所有创建人ID
            Set<String> userIds = items.stream()
                    .map(OpMilkBinQualityInspection::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));

            // 提取所有审核人ID
            Set<String> reviewerIds = items.stream()
                    .map(OpMilkBinQualityInspection::getReviewer)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> reviewerMap = userCacheService.batchGetUsernames(reviewerIds);

            items.forEach(vo ->
                    vo.setReviewer(reviewerMap.get(vo.getReviewer())));
        }

        return items;
    }


}
