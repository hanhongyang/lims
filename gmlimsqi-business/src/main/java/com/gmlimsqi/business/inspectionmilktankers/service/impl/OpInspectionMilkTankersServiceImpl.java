package com.gmlimsqi.business.inspectionmilktankers.service.impl;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.util.ObjectUtil;
import com.gmlimsqi.business.inspectionmilktankers.domain.OpInspectionMilkTankers;
import com.gmlimsqi.business.inspectionmilktankers.mapper.OpInspectionMilkTankersMapper;
import com.gmlimsqi.business.inspectionmilktankers.service.IOpInspectionMilkTankersService;
import com.gmlimsqi.business.leadsealsheet.domain.OpLeadSealSheet;
import com.gmlimsqi.business.leadsealsheet.mapper.OpLeadSealSheetMapper;
import com.gmlimsqi.business.leadsealsheet.service.IOpLeadSealSheetService;
import com.gmlimsqi.business.milkfillingorder.domain.OpMilkFillingOrder;
import com.gmlimsqi.business.milkfillingorder.mapper.OpMilkFillingOrderMapper;
import com.gmlimsqi.business.milkfillingorder.service.IOpMilkFillingOrderService;
import com.gmlimsqi.business.milksamplequalityinspection.domain.OpMilkSampleQualityInspection;
import com.gmlimsqi.business.milksamplequalityinspection.mapper.OpMilkSampleQualityInspectionMapper;
import com.gmlimsqi.business.milksamplequalityinspection.service.IOpMilkSampleQualityInspectionService;
import com.gmlimsqi.business.rmts.entity.dto.PhotoSyncDTO;
import com.gmlimsqi.business.rmts.service.RmtsRanchLimsService;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.core.domain.R;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.file.ImageUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.annotation.DataScope;

import com.gmlimsqi.system.domain.SysUploadFile;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import com.gmlimsqi.system.mapper.SysUploadFileMapper;
import com.gmlimsqi.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gmlimsqi.business.util.CodeGeneratorUtil.CODE_TYPE_INSPECTION_MILK_TANKERS;

/**
 * 奶罐车检查Service业务层处理
 * * @author hhy
 * @date 2025-11-10
 */
@Slf4j
@Service
public class OpInspectionMilkTankersServiceImpl implements IOpInspectionMilkTankersService
{
    @Autowired
    private OpInspectionMilkTankersMapper opInspectionMilkTankersMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private IOpMilkFillingOrderService opMilkFillingOrderService;

    @Autowired
    private OpMilkFillingOrderMapper opMilkFillingOrderMapper;

    @Autowired
    private IOpLeadSealSheetService opLeadSealSheetService;

    @Autowired
    private OpLeadSealSheetMapper opLeadSealSheetMapper;

    @Autowired
    private IOpMilkSampleQualityInspectionService opMilkSampleQualityInspectionService;

    @Autowired
    private OpMilkSampleQualityInspectionMapper opMilkSampleQualityInspectionMapper;

    @Autowired
    private SysUploadFileMapper sysUploadFileMapper;

    @Autowired
    private RmtsRanchLimsService rmtsRanchLimsService;

    /**
     * 查询奶罐车检查
     * * @param inspectionMilkTankersId 奶罐车检查主键
     * @return 奶罐车检查
     */
    @Override
    public OpInspectionMilkTankers selectOpInspectionMilkTankersByInspectionMilkTankersId(String inspectionMilkTankersId)
    {
        OpInspectionMilkTankers opInspectionMilkTankers = opInspectionMilkTankersMapper.
                selectOpInspectionMilkTankersByInspectionMilkTankersId(inspectionMilkTankersId);

        if (StringUtils.isNotEmpty(opInspectionMilkTankers.getWasherPhoto())){
            SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opInspectionMilkTankers.getWasherPhoto());
            opInspectionMilkTankers.setWasherPhotoUrl(sysUploadFile.getUrl());
        }

        if (StringUtils.isNotEmpty(opInspectionMilkTankers.getCylinderHeadExhaustHolePhoto())){
            SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opInspectionMilkTankers.getCylinderHeadExhaustHolePhoto());
            opInspectionMilkTankers.setCylinderHeadExhaustHolePhotoUrl(sysUploadFile.getUrl());
        }

        if (StringUtils.isNotEmpty(opInspectionMilkTankers.getMilkCanSprayHeadPhoto())){
            SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opInspectionMilkTankers.getMilkCanSprayHeadPhoto());
            opInspectionMilkTankers.setMilkCanSprayHeadPhotoUrl(sysUploadFile.getUrl());
        }

        /*if (StringUtils.isNotEmpty(opInspectionMilkTankers.getCylinderHeadGasketPhoto())){
            SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opInspectionMilkTankers.getCylinderHeadGasketPhoto());
            opInspectionMilkTankers.setCylinderHeadGasketPhotoUrl(sysUploadFile.getFilePath());
        }*/

        return opInspectionMilkTankers;
    }

    /**
     * 查询奶罐车检查列表
     * * @param opInspectionMilkTankers 奶罐车检查
     * @return 奶罐车检查
     */
    // +++ 新增：@DataScope 注解以激活数据权限切面 +++
    @Override
    public List<OpInspectionMilkTankers> selectOpInspectionMilkTankersList(OpInspectionMilkTankers opInspectionMilkTankers)
    {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            opInspectionMilkTankers.setDeptId(SecurityUtils.getDeptId().toString());
        }
        List<OpInspectionMilkTankers> items = opInspectionMilkTankersMapper.selectOpInspectionMilkTankersList(opInspectionMilkTankers);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpInspectionMilkTankers::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增奶罐车检查
     * * @param opInspectionMilkTankers 奶罐车检查
     * @return 结果
     */
    @Override
    @Transactional
    public String insertOpInspectionMilkTankers(OpInspectionMilkTankers opInspectionMilkTankers)
    {
        if (StringUtils.isEmpty(opInspectionMilkTankers.getInspectionMilkTankersId())) {
            opInspectionMilkTankers.setInspectionMilkTankersId(IdUtils.simpleUUID());
        }

        Long deptId = SecurityUtils.getDeptId();

        // 生成装奶单
        OpMilkFillingOrder opMilkFillingOrder = new OpMilkFillingOrder();
        opMilkFillingOrder.setLicensePlateNumber(opInspectionMilkTankers.getLicensePlateNumber());
        opMilkFillingOrder.setInspectionMilkTankersId(opInspectionMilkTankers.getInspectionMilkTankersId());
        opMilkFillingOrder.setMilkSourcePlanOrderNumber(opInspectionMilkTankers.getMilkSourcePlanOrderNumber());
        opMilkFillingOrder.setDeptId(deptId.toString());
        String opMilkFillingOrderId = opMilkFillingOrderService.insertOpMilkFillingOrder(opMilkFillingOrder);

        // 生成铅封单
        OpLeadSealSheet opLeadSealSheet = new OpLeadSealSheet();
        opLeadSealSheet.setInspectionMilkTankersId(opInspectionMilkTankers.getInspectionMilkTankersId());
        opLeadSealSheet.setMilkSourcePlanOrderNumber(opInspectionMilkTankers.getMilkSourcePlanOrderNumber());
        opLeadSealSheet.setLicensePlateNumber(opInspectionMilkTankers.getLicensePlateNumber());
        opLeadSealSheet.setOpMilkFillingOrderId(opMilkFillingOrderId);
        opLeadSealSheet.setDeptId(deptId.toString());
        opLeadSealSheetService.insertOpLeadSealSheet(opLeadSealSheet);

        // 生成奶样质检
        OpMilkSampleQualityInspection opMilkSampleQualityInspection = new OpMilkSampleQualityInspection();
        opMilkSampleQualityInspection.setInspectionMilkTankersId(opInspectionMilkTankers.getInspectionMilkTankersId());
        opMilkSampleQualityInspection.setMilkSourcePlanOrderNumber(opInspectionMilkTankers.getMilkSourcePlanOrderNumber());
        opMilkSampleQualityInspection.setLicensePlateNumber(opInspectionMilkTankers.getLicensePlateNumber());
        opMilkSampleQualityInspection.setOpMilkFillingOrderId(opMilkFillingOrderId);
        opMilkSampleQualityInspection.setDeptId(deptId.toString());
        opMilkSampleQualityInspection.setEntryTime(opInspectionMilkTankers.getEntryTime());
        opMilkSampleQualityInspectionService.insertOpMilkSampleQualityInspection(opMilkSampleQualityInspection);

        // 新增时默认设置删除标志为 '0'
        opInspectionMilkTankers.setIsDelete("0");
        opInspectionMilkTankers.setStatus("0");
        opInspectionMilkTankers.setIsPushMilkSource("0");
        opInspectionMilkTankers.setOrderNumber(opInspectionMilkTankers.getMilkSourcePlanOrderNumber());

        try {
            String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_INSPECTION_MILK_TANKERS);
            opInspectionMilkTankers.setInspectionMilkTankersNumber(resultNo);
        } catch (BusinessException e) {
            throw new RuntimeException("生成奶罐车检查单编号失败: " + e.getMessage());
        }

        // +++ 自动填充部门ID，用于数据权限 +++
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser))
        {
            // 假设您的实体类中有名为 setDeptId 的方法
            // (请确保您的 OpInspectionMilkTankers 实体类中有 setDeptId(String deptId) 方法)
            opInspectionMilkTankers.setDeptId(loginUser.getDeptId().toString());
        }

        Long userId = SecurityUtils.getUserId();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        if (StringUtils.isNotNull(sysUser)){
            opInspectionMilkTankers.setCreateByName(sysUser.getNickName());
        }

        // 自动填充创建/更新信息
        opInspectionMilkTankers.fillCreateInfo();
        opInspectionMilkTankersMapper.insertOpInspectionMilkTankers(opInspectionMilkTankers);
        return opInspectionMilkTankers.getInspectionMilkTankersId();
    }

    /**
     * 修改奶罐车检查
     * * @param opInspectionMilkTankers 奶罐车检查
     * @return 结果
     */
    @Override
    public int updateOpInspectionMilkTankers(OpInspectionMilkTankers opInspectionMilkTankers)
    {
        // 查询检查单是否存在
        OpInspectionMilkTankers status =
                selectOpInspectionMilkTankersByInspectionMilkTankersId(opInspectionMilkTankers.getInspectionMilkTankersId());
        if (status == null) {
            throw new IllegalArgumentException("检查单不存在");
        }

        if ("1".equals(status.getStatus())){
            throw new IllegalArgumentException("已审核的检查单不能修改");
        }

        // 自动填充更新信息
        opInspectionMilkTankers.fillUpdateInfo();
        return opInspectionMilkTankersMapper.updateOpInspectionMilkTankers(opInspectionMilkTankers);
    }

    @Override
    @Transactional
    public int auditOpInspectionMilkTankers(String inspectionMilkTankersId){
        // 检查单是否存在
        OpInspectionMilkTankers inspectionMilkTankers = selectOpInspectionMilkTankersByInspectionMilkTankersId(inspectionMilkTankersId);
        if (inspectionMilkTankers == null) {
            throw new IllegalArgumentException("检查单不存在");
        }

        // 检查单是否已审核
        if ("1".equals(inspectionMilkTankers.getStatus())){
            throw new IllegalArgumentException("已审核的检查单不能重复审核");
        }

        // 更新检查单状态为已审核
        inspectionMilkTankers.setStatus("1");
        // 自动填充更新信息
        inspectionMilkTankers.fillUpdateInfo();
        // 从登录用户获取审核人信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)){
            inspectionMilkTankers.setReviewerId(loginUser.getUserId().toString());
        }

        // 从用户表获取审核人名称
        SysUser sysUser = sysUserMapper.selectUserById(Long.parseLong(inspectionMilkTankers.getReviewerId()));
        if (StringUtils.isNotNull(sysUser)){
            inspectionMilkTankers.setReviewer(sysUser.getNickName());
        }

        inspectionMilkTankers.setReviewTime(new Date());

        // 查询装奶单
        OpMilkFillingOrder opMilkFillingOrder = opMilkFillingOrderMapper.selectOpMilkFillingOrderByMilkTankersId(inspectionMilkTankersId);
        if (opMilkFillingOrder == null) {
            throw new IllegalArgumentException("审核检查单失败，未找到对应的装奶单");
        }

        opMilkFillingOrder.setPreStepCompleted("1");
        // 更新装奶单
        opMilkFillingOrderMapper.updateOpMilkFillingOrder(opMilkFillingOrder);

        int i = opInspectionMilkTankersMapper.updateOpInspectionMilkTankers(inspectionMilkTankers);

        int i1 = this.pushMilkSource(inspectionMilkTankersId);

        System.out.println("修改奶罐车检查状态为1，推送奶源状态为1" + i1);

        return i;
    }

    @Override
    public int pushMilkSource(String inspectionMilkTankersId) {
        OpInspectionMilkTankers inspectionMilkTankers = opInspectionMilkTankersMapper.selectOpInspectionMilkTankersByInspectionMilkTankersId(inspectionMilkTankersId);
        if (inspectionMilkTankers == null) {
            throw new RuntimeException("推送奶源失败，未找到对应的检查单");
        }

        // ---------------------------推送奶源逻辑开始
        PhotoSyncDTO photoSyncDTO = new PhotoSyncDTO();

        // 获取缸盖图片
        String cylinderHeadExhaustHolePhoto = inspectionMilkTankers.getCylinderHeadExhaustHolePhoto();
        if (StringUtils.isNotEmpty(cylinderHeadExhaustHolePhoto)){
            try {
                // 查询相对路径
                SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(cylinderHeadExhaustHolePhoto);
                if (sysUploadFile != null && StringUtils.isNotEmpty(sysUploadFile.getFilePath())) {
                    String base64 = ImageUtils.imageUrlToBase64(sysUploadFile.getFilePath());
                    photoSyncDTO.setCylinderHeadPhoto(base64);
                }
            }catch (IOException e){
                throw new RuntimeException("转换缸盖图片为Base64编码失败: " + e.getMessage());
            }
        }
        // 获取奶罐喷淋头图片
        String milkTankSprayHeadPhoto = inspectionMilkTankers.getMilkCanSprayHeadPhoto();
        if (StringUtils.isNotEmpty(milkTankSprayHeadPhoto)){
            try {
                SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(milkTankSprayHeadPhoto);
                if (sysUploadFile != null && StringUtils.isNotEmpty(sysUploadFile.getFilePath())) {
                    String base64 = ImageUtils.imageUrlToBase64(sysUploadFile.getFilePath());
                    photoSyncDTO.setSprinklerHeadPhoto(base64);
                }
            }catch (IOException e){
                throw new RuntimeException("转换奶罐喷淋头图片为Base64编码失败: " + e.getMessage());
            }
        }
        // 获取内壁，缸盖，垫圈图片
        // 获取内壁图片
        String innerWallPhoto = inspectionMilkTankers.getWasherPhoto();
        if (StringUtils.isNotEmpty(innerWallPhoto)){
            try {
                SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(innerWallPhoto);
                if (sysUploadFile != null && StringUtils.isNotEmpty(sysUploadFile.getFilePath())) {
                    String base64 = ImageUtils.imageUrlToBase64(sysUploadFile.getFilePath());
                    photoSyncDTO.setWasherPhoto(base64);
                }
            }catch (IOException e){
                throw new RuntimeException("转换内壁图片为Base64编码失败: " + e.getMessage());
            }
        }

        if (StringUtils.isEmpty(inspectionMilkTankers.getMilkSourcePlanOrderNumber())) {
            throw new RuntimeException("推送奶源失败，奶源计划单号为空");
        }
        photoSyncDTO.setOrderNumber(inspectionMilkTankers.getMilkSourcePlanOrderNumber());

        R r = rmtsRanchLimsService.photoSync(photoSyncDTO);
        if (r.getCode() == 200){
            System.out.println("修改奶罐车检查推送奶源状态为1");
            inspectionMilkTankers.setIsPushMilkSource("1");
        }
        // ---------------------------推送奶源逻辑结束
        return opInspectionMilkTankersMapper.updateOpInspectionMilkTankers(inspectionMilkTankers);
    }

    @Override
    @Transactional
    public int changePlan(OpInspectionMilkTankers opInspectionMilkTankers) {
        // 变更奶罐车检查，装奶单，奶样质检，铅封单计划
        if (StringUtils.isEmpty(opInspectionMilkTankers.getMilkSourcePlanOrderNumber())) {
            throw new IllegalArgumentException("请选择计划单号");
        }

        // 根据奶罐车id查询装奶单
        OpMilkFillingOrder opMilkFillingOrder = opMilkFillingOrderMapper.selectOpMilkFillingOrderByMilkTankersId(opInspectionMilkTankers.getInspectionMilkTankersId());
        if (opMilkFillingOrder == null) {
            throw new IllegalArgumentException("审核检查单失败，未找到对应的装奶单");
        }

        opInspectionMilkTankers.setIsPushMilkSource("0");
        opInspectionMilkTankers.setOrderNumber(opInspectionMilkTankers.getInspectionMilkTankersNumber());

        // 更新装奶单计划
        opMilkFillingOrder.setMilkSourcePlanOrderNumber(opInspectionMilkTankers.getMilkSourcePlanOrderNumber());
        opMilkFillingOrder.setLicensePlateNumber(opInspectionMilkTankers.getLicensePlateNumber());
        opMilkFillingOrder.setIsPushMilkSource("0");
        opMilkFillingOrderMapper.updateOpMilkFillingOrder(opMilkFillingOrder);

        // 根据奶罐车id查询铅封单
        OpLeadSealSheet opLeadSealSheet = opLeadSealSheetMapper.selectOpLeadSealSheetByMilkTankersId(opInspectionMilkTankers.getInspectionMilkTankersId());
        if (opLeadSealSheet == null) {
            throw new IllegalArgumentException("审核检查单失败，未找到对应的铅封单");
        }

        // 更新铅封单计划
        opLeadSealSheet.setMilkSourcePlanOrderNumber(opInspectionMilkTankers.getMilkSourcePlanOrderNumber());
        opLeadSealSheet.setLicensePlateNumber(opInspectionMilkTankers.getLicensePlateNumber());
        opLeadSealSheet.setIsPushMilkSource("0");
        opLeadSealSheetMapper.updateOpLeadSealSheet(opLeadSealSheet);

        // 根据奶罐车id查询奶样质检单
        OpMilkSampleQualityInspection opMilkSampleQualityInspection = opMilkSampleQualityInspectionMapper.selectOpMilkSampleQualityInspectionByInspectionMilkTankersId(opInspectionMilkTankers.getInspectionMilkTankersId());
        if (opMilkSampleQualityInspection == null) {
            throw new IllegalArgumentException("审核检查单失败，未找到对应的奶样质检单");
        }

        // 更新奶样质检单计划
        opMilkSampleQualityInspection.setMilkSourcePlanOrderNumber(opInspectionMilkTankers.getMilkSourcePlanOrderNumber());
        opMilkSampleQualityInspection.setLicensePlateNumber(opInspectionMilkTankers.getLicensePlateNumber());
        opMilkSampleQualityInspection.setIsPushMilkSource("0");
        opMilkSampleQualityInspectionMapper.updateOpMilkSampleQualityInspection(opMilkSampleQualityInspection);

        int i = opInspectionMilkTankersMapper.updateOpInspectionMilkTankers(opInspectionMilkTankers);

        try{
            // 重新推送奶源系统-奶罐车检查
            this.pushMilkSource(opInspectionMilkTankers.getInspectionMilkTankersId());
            // 重新推送奶源系统-装奶单
            opMilkFillingOrderService.pushMilkSource(opMilkFillingOrder.getOpMilkFillingOrderId());
            // 重新推送奶源系统-铅封单
            opLeadSealSheetService.pushMilkSource(opLeadSealSheet.getOpLeadSealSheetId());
            // 重新推送奶源系统-奶样质检单
            opMilkSampleQualityInspectionService.pushMilkSource(opMilkSampleQualityInspection.getOpMilkSampleQualityInspectionId());
        }catch (Exception e){
            log.error("变更奶罐车计划失败，异常信息：{}", e.getMessage());
        }

        // 更新奶罐车计划
        return i;
    }
}
