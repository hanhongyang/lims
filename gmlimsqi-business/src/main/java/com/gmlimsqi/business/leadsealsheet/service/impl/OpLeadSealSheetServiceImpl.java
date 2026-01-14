package com.gmlimsqi.business.leadsealsheet.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.gmlimsqi.business.inspectionmilktankers.mapper.OpInspectionMilkTankersMapper;
import com.gmlimsqi.business.leadsealsheet.domain.OpLeadSealSheet;
import com.gmlimsqi.business.leadsealsheet.mapper.OpLeadSealSheetMapper;
import com.gmlimsqi.business.leadsealsheet.service.IOpLeadSealSheetService;
import com.gmlimsqi.business.rmts.entity.dto.QualitySyncDTO;
import com.gmlimsqi.business.rmts.service.RmtsRanchLimsService;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.IdListHandler;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.core.domain.R;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.annotation.DataScope;
import com.gmlimsqi.system.domain.SysUploadFile;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import com.gmlimsqi.system.mapper.SysUploadFileMapper;
import com.gmlimsqi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.gmlimsqi.business.util.CodeGeneratorUtil.CODE_TYPE_LEAD_SEAL_SHEET;

/**
 * 铅封单Service业务层处理
 * * @author hhy
 * @date 2025-11-10
 */
@Service
public class OpLeadSealSheetServiceImpl implements IOpLeadSealSheetService
{
    @Autowired
    private OpLeadSealSheetMapper opLeadSealSheetMapper;
    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;

     @Autowired
    private SysUploadFileMapper sysUploadFileMapper;

    @Autowired
    private OpInspectionMilkTankersMapper opInspectionMilkTankersMapper;

    @Autowired
    private RmtsRanchLimsService rmtsRanchLimsService;

    /**
     * 查询铅封单
     * * @param opLeadSealSheetId 铅封单主键
     * @return 铅封单
     */
    @Override
    public OpLeadSealSheet selectOpLeadSealSheetByOpLeadSealSheetId(String opLeadSealSheetId)
    {
        OpLeadSealSheet opLeadSealSheet = opLeadSealSheetMapper.selectOpLeadSealSheetByOpLeadSealSheetId(opLeadSealSheetId);

        // 处理附件，按逗号分隔
        if (StringUtils.isNotEmpty(opLeadSealSheet.getSealedPhoto())){
            List<String> list = IdListHandler.parseIdStr(opLeadSealSheet.getSealedPhoto());
            if (!list.isEmpty()){
                List<String> urlList = new ArrayList<>();
                for (String fileId : list){
                    SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(fileId);
                    urlList.add(sysUploadFile.getUrl());
                }
                opLeadSealSheet.setSealedPhotoUrl(urlList);
            }
        }

        return opLeadSealSheet;
    }

    /**
     * 查询铅封单列表
     * * @param opLeadSealSheet 铅封单
     * @return 铅封单
     */
    // +++ 新增：@DataScope 注解以激活数据权限切面 +++
//    @DataScope(deptAlias = "a")
    @Override
    public List<OpLeadSealSheet> selectOpLeadSealSheetList(OpLeadSealSheet opLeadSealSheet)
    {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            opLeadSealSheet.setDeptId(SecurityUtils.getLoginUser().getDeptId().toString());
        }
        List<OpLeadSealSheet> items = opLeadSealSheetMapper.selectOpLeadSealSheetList(opLeadSealSheet);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpLeadSealSheet::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增铅封单
     * * @param opLeadSealSheet 铅封单
     * @return 结果
     */
    @Override
    public int insertOpLeadSealSheet(OpLeadSealSheet opLeadSealSheet)
    {
        if (StringUtils.isEmpty(opLeadSealSheet.getOpLeadSealSheetId())) {
            opLeadSealSheet.setOpLeadSealSheetId(IdUtils.simpleUUID());
        }

        // +++ 自动填充部门ID，用于数据权限 +++
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser))
        {
            // 假设您的实体类中有名为 setDeptId 的方法
            // (请确保您的 OpLeadSealSheet 实体类中有 setDeptId(String deptId) 方法)
            opLeadSealSheet.setDeptId(loginUser.getDeptId().toString());
        }

        try {
            String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_LEAD_SEAL_SHEET);
            opLeadSealSheet.setLeadSealNumber(resultNo);
        } catch (BusinessException e) {
            throw new RuntimeException("生成铅封单编号失败: " + e.getMessage());
        }

        SysUser sysUser = sysUserMapper.selectUserById(loginUser.getUserId());
        opLeadSealSheet.setCreateByName(sysUser.getNickName());
        opLeadSealSheet.setPreStepCompleted("0");
        opLeadSealSheet.setIsDelete("0");
        opLeadSealSheet.setStatus("0");
        opLeadSealSheet.setIsPushMilkSource("0");

        // 自动填充创建/更新信息
        opLeadSealSheet.fillCreateInfo();
        return opLeadSealSheetMapper.insertOpLeadSealSheet(opLeadSealSheet);
    }

    /**
     * 修改铅封单
     * * @param opLeadSealSheet 铅封单
     * @return 结果
     */
    @Override
    public int updateOpLeadSealSheet(OpLeadSealSheet opLeadSealSheet)
    {
        // 查询检查单是否存在
        OpLeadSealSheet status =
                selectOpLeadSealSheetByOpLeadSealSheetId(opLeadSealSheet.getOpLeadSealSheetId());
        if (status == null) {
            throw new IllegalArgumentException("铅封单不存在");
        }

        if ("1".equals(status.getStatus())){
            throw new IllegalArgumentException("已审核的铅封单不能重复审核");
        }

        Long userId = SecurityUtils.getUserId();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        opLeadSealSheet.setSealedMan(sysUser.getNickName());
        opLeadSealSheet.setSealedManId(userId.toString());

        // 自动填充更新信息
        opLeadSealSheet.fillUpdateInfo();
        return opLeadSealSheetMapper.updateOpLeadSealSheet(opLeadSealSheet);
    }

    @Override
    public int audit(String opLeadSealSheetId) {
        // 检查单是否存在
        OpLeadSealSheet opLeadSealSheet = selectOpLeadSealSheetByOpLeadSealSheetId(opLeadSealSheetId);
        if (opLeadSealSheet == null) {
            throw new IllegalArgumentException("铅封单不存在");
        }

        // 检查单是否已审核
        if ("1".equals(opLeadSealSheet.getStatus())){
            throw new IllegalArgumentException("已审核的铅封单不能重复审核");
        }

        // 更新检查单状态为已审核
        opLeadSealSheet.setStatus("1");
        // 自动填充更新信息
        opLeadSealSheet.fillUpdateInfo();
        // 从登录用户获取审核人信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)){
            opLeadSealSheet.setReviewerId(loginUser.getUserId().toString());
        }

        // 从用户表获取审核人名称
        SysUser sysUser = sysUserMapper.selectUserById(loginUser.getUserId());
        if (StringUtils.isNotNull(sysUser)){
            opLeadSealSheet.setReviewer(sysUser.getNickName());
        }
        opLeadSealSheet.setReviewTime(new Date());

        this.pushMilkSource(opLeadSealSheetId);

        return opLeadSealSheetMapper.updateOpLeadSealSheet(opLeadSealSheet);
    }

    @Override
    public int pushMilkSource(String opLeadSealSheetId) {
        OpLeadSealSheet opLeadSealSheet = opLeadSealSheetMapper.selectOpLeadSealSheetByOpLeadSealSheetId(opLeadSealSheetId);
        if (opLeadSealSheet == null) {
            throw new IllegalArgumentException("推送奶源失败，未找到对应的铅封单");
        }

        // ---------------------------推送奶源逻辑开始
        QualitySyncDTO qualitySyncDTO = new QualitySyncDTO();
        qualitySyncDTO.setOrderNumber(opLeadSealSheet.getMilkSourcePlanOrderNumber());

        if (StringUtils.isEmpty(opLeadSealSheet.getQfNumber())){
            throw new IllegalArgumentException("推送奶源失败，铅封号为空 ");
        }

        qualitySyncDTO.setLeadSealNum(opLeadSealSheet.getQfNumber());
        qualitySyncDTO.setStatus("0");

        // 调用 RMTS 接口推送奶源信息
        R r = rmtsRanchLimsService.qualityInfo(qualitySyncDTO);
        if (r.getCode() == 200){
            opLeadSealSheet.setIsPushMilkSource("1");
        }
        // ---------------------------推送奶源逻辑结束

        return opLeadSealSheetMapper.updateOpLeadSealSheet(opLeadSealSheet);
    }
}
