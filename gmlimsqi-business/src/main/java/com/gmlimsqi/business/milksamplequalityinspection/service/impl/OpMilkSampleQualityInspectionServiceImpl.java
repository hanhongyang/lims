package com.gmlimsqi.business.milksamplequalityinspection.service.impl;

import com.gmlimsqi.business.inspectionmilktankers.domain.OpInspectionMilkTankers;
import com.gmlimsqi.business.inspectionmilktankers.mapper.OpInspectionMilkTankersMapper;
import com.gmlimsqi.business.milksamplequalityinspection.domain.ExitInspectionReportDTO;
import com.gmlimsqi.business.milksamplequalityinspection.domain.ExitInspectionReportVO;
import com.gmlimsqi.business.milksamplequalityinspection.domain.OpMilkSampleQIException;
import com.gmlimsqi.business.milksamplequalityinspection.domain.OpMilkSampleQualityInspection;
import com.gmlimsqi.business.milksamplequalityinspection.mapper.OpMilkSampleQualityInspectionMapper;
import com.gmlimsqi.business.milksamplequalityinspection.service.IOpMilkSampleQualityInspectionService;
import com.gmlimsqi.business.milksamplequalityinspection.service.MilkSampleQualityInspectionChangeLogService;
import com.gmlimsqi.business.rmts.entity.dto.FactoryQualityDTO;
import com.gmlimsqi.business.rmts.entity.dto.PhotoSyncDTO;
import com.gmlimsqi.business.rmts.entity.dto.QualitySyncDTO;
import com.gmlimsqi.business.rmts.entity.vo.MilkTransportDetectionInfoVO;
import com.gmlimsqi.business.rmts.service.RmtsRanchLimsService;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.IdListHandler;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.change.constant.ChangeLogConstant;
import com.gmlimsqi.common.core.domain.R;
import com.gmlimsqi.common.core.domain.entity.SysDept;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.file.ImageUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.domain.SysUploadFile;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import com.gmlimsqi.system.mapper.SysUploadFileMapper;
import com.gmlimsqi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.gmlimsqi.business.util.CodeGeneratorUtil.CODE_TYPE_MILK_SAMPLE_QUALITY_INSPECTION;

/**
 * 奶样质检Service业务层处理
 * * @author hhy
 * @date 2025-11-10
 */
@Service
public class OpMilkSampleQualityInspectionServiceImpl implements IOpMilkSampleQualityInspectionService
{
    @Autowired
    private OpMilkSampleQualityInspectionMapper opMilkSampleQualityInspectionMapper;
    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;

    @Autowired
    private RmtsRanchLimsService rmtsRanchLimsService;

    @Autowired
    private SysUploadFileMapper sysUploadFileMapper;

    @Autowired
    private OpInspectionMilkTankersMapper opInspectionMilkTankersMapper;

    @Autowired
    private MilkSampleQualityInspectionChangeLogService changeLogService;

    /**
     * 查询奶样质检
     * * @param opMilkSampleQualityInspectionId 奶样质检主键
     * @return 奶样质检
     */
    @Override
    public OpMilkSampleQualityInspection selectOpMilkSampleQualityInspectionByOpMilkSampleQualityInspectionId(String opMilkSampleQualityInspectionId)
    {
        OpMilkSampleQualityInspection opMilkSampleQualityInspection = opMilkSampleQualityInspectionMapper.
                selectOpMilkSampleQualityInspectionByOpMilkSampleQualityInspectionId(opMilkSampleQualityInspectionId);

        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getMilkTempPhoto())){
            SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getMilkTempPhoto());
            opMilkSampleQualityInspection.setMilkTempPhotoUrl(sysUploadFile.getUrl());
        }

        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getAlcoholPhoto())){
            SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getAlcoholPhoto());
            opMilkSampleQualityInspection.setAlcoholPhotoUrl(sysUploadFile.getUrl());
        }

        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getPhosphatePhoto())){
            SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getPhosphatePhoto());
            opMilkSampleQualityInspection.setPhosphatePhotoUrl(sysUploadFile.getUrl());
        }

        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getBloodMilkTestPhoto())){
            SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getBloodMilkTestPhoto());
            opMilkSampleQualityInspection.setBloodMilkTestPhotoUrl(sysUploadFile.getUrl());
        }

        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getSensoryIndexPhoto())){
            SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getSensoryIndexPhoto());
            opMilkSampleQualityInspection.setSensoryIndexPhotoUrl(sysUploadFile.getUrl());
        }
        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getAflatoxinPhoto())){
            SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getAflatoxinPhoto());
            opMilkSampleQualityInspection.setAflatoxinPhotoUrl(sysUploadFile.getUrl());
        }

        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getAntibioticPhoto())){
            SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getAntibioticPhoto());
            opMilkSampleQualityInspection.setAntibioticPhotoUrl(sysUploadFile.getUrl());
        }

        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getAcidityPhoto())){
            SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getAcidityPhoto());
            opMilkSampleQualityInspection.setAcidityPhotoUrl(sysUploadFile.getUrl());
        }

        // 处理附件，按逗号分隔
        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getFile())){
            List<String> list = IdListHandler.parseIdStr(opMilkSampleQualityInspection.getFile());
            if (!list.isEmpty()){
                List<String> urlList = new ArrayList<>();
                for (String fileId : list){
                    SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(fileId);
                    urlList.add(sysUploadFile.getUrl());
                }
                opMilkSampleQualityInspection.setFileUrl(urlList);
            }
        }

        return opMilkSampleQualityInspection;
    }

    /**
     * 查询奶样质检列表
     * * @param opMilkSampleQualityInspection 奶样质检
     * @return 奶样质检
     */
    // +++ 新增：@DataScope 注解以激活数据权限切面 +++
//    @DataScope(deptAlias = "d")
    @Override
    public List<OpMilkSampleQualityInspection> selectOpMilkSampleQualityInspectionList(OpMilkSampleQualityInspection opMilkSampleQualityInspection)
    {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            opMilkSampleQualityInspection.setDeptId(SecurityUtils.getLoginUser().getDeptId().toString());
        }

        List<OpMilkSampleQualityInspection> items = opMilkSampleQualityInspectionMapper.selectOpMilkSampleQualityInspectionList(opMilkSampleQualityInspection);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpMilkSampleQualityInspection::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增奶样质检
     * * @param opMilkSampleQualityInspection 奶样质检
     * @return 结果
     */
    @Override
    public int insertOpMilkSampleQualityInspection(OpMilkSampleQualityInspection opMilkSampleQualityInspection)
    {
        if (StringUtils.isEmpty(opMilkSampleQualityInspection.getOpMilkSampleQualityInspectionId())) {
            opMilkSampleQualityInspection.setOpMilkSampleQualityInspectionId(IdUtils.simpleUUID());
        }

        // +++ 自动填充部门ID，用于数据权限 +++
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser))
        {
            // 假设您的实体类中有名为 setDeptId 的方法
            // (请确保您的 OpMilkSampleQualityInspection 实体类中有 setDeptId(String deptId) 方法)
            opMilkSampleQualityInspection.setDeptId(loginUser.getDeptId().toString());
        }

        try {
            String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_MILK_SAMPLE_QUALITY_INSPECTION);
            opMilkSampleQualityInspection.setMilkSampleQualityInspectionNumber(resultNo);
        } catch (BusinessException e) {
            throw new RuntimeException("生成奶样质检单编号失败: " + e.getMessage());
        }

        SysUser sysUser = sysUserMapper.selectUserById(loginUser.getUserId());
        opMilkSampleQualityInspection.setCreateByName(sysUser.getNickName());
        opMilkSampleQualityInspection.setStatus("0");
        opMilkSampleQualityInspection.setPreStepCompleted("0");
        opMilkSampleQualityInspection.setIsDelete("0");
        opMilkSampleQualityInspection.setIsPushMilkSource("0");
        opMilkSampleQualityInspection.setIsSampling("0");


        // 自动填充创建/更新信息
        opMilkSampleQualityInspection.fillCreateInfo();
        return opMilkSampleQualityInspectionMapper.insertOpMilkSampleQualityInspection(opMilkSampleQualityInspection);
    }

    /**
     * 修改奶样质检
     * * @param opMilkSampleQualityInspection 奶样质检
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateOpMilkSampleQualityInspection(OpMilkSampleQualityInspection opMilkSampleQualityInspection)
    {
        // 查询检查单是否存在
        OpMilkSampleQualityInspection status =
                selectOpMilkSampleQualityInspectionByOpMilkSampleQualityInspectionId(opMilkSampleQualityInspection.getOpMilkSampleQualityInspectionId());
        if (status == null) {
            throw new IllegalArgumentException("检查单不存在");
        }

        if ("2".equals(status.getStatus())){
            throw new IllegalArgumentException("已审核的质检单不能重复审核");
        }

        // 记录日志
//        changeLogService.recordUpdateLog(status, opMilkSampleQualityInspection, ChangeLogConstant.OP_TYPE_UPDATE);

        // 自动填充更新信息
        opMilkSampleQualityInspection.fillUpdateInfo();
        return opMilkSampleQualityInspectionMapper.updateOpMilkSampleQualityInspection(opMilkSampleQualityInspection);
    }

    @Override
    @Transactional
    public int auditOpMilkSampleQualityInspection(String inspectionMilkTankersId) {
        // 检查单是否存在
        OpMilkSampleQualityInspection opMilkSampleQualityInspection =
                selectOpMilkSampleQualityInspectionByOpMilkSampleQualityInspectionId(inspectionMilkTankersId);
        if (opMilkSampleQualityInspection == null) {
            throw new IllegalArgumentException("检查单不存在");
        }

        // 检查单是否已审核
        if ("2".equals(opMilkSampleQualityInspection.getStatus())){
            throw new IllegalArgumentException("已审核的质检单不能重复审核");
        }

        if ("0".equals(opMilkSampleQualityInspection.getStatus())){
            throw new IllegalArgumentException("未取样的质检单不能审核");
        }

        // 更新检查单状态为已审核
        opMilkSampleQualityInspection.setStatus("2");
        // 自动填充更新信息
        opMilkSampleQualityInspection.fillUpdateInfo();
        // 从登录用户获取审核人信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)){
            opMilkSampleQualityInspection.setReviewerId(loginUser.getUserId().toString());
        }

        // 从用户表获取审核人名称
        SysUser sysUser = sysUserMapper.selectUserById(Long.parseLong(opMilkSampleQualityInspection.getReviewerId()));
        if (StringUtils.isNotNull(sysUser)){
            opMilkSampleQualityInspection.setReviewer(sysUser.getNickName());
        }

        opMilkSampleQualityInspection.setReviewTime(new Date());

        this.pushMilkSource(inspectionMilkTankersId);

        // ------------推送奶源逻辑结束
        return opMilkSampleQualityInspectionMapper.updateOpMilkSampleQualityInspection(opMilkSampleQualityInspection);
    }

    @Override
    public int pushMilkSource(String opMilkSampleQualityInspectionId) {
        OpMilkSampleQualityInspection opMilkSampleQualityInspection = opMilkSampleQualityInspectionMapper.
                selectOpMilkSampleQualityInspectionByOpMilkSampleQualityInspectionId(opMilkSampleQualityInspectionId);
        if (opMilkSampleQualityInspection == null) {
            throw new IllegalArgumentException("推送奶源失败，未找到对应的质检单");
        }

        // 根据奶罐车id查询车辆数据
        OpInspectionMilkTankers opInspectionMilkTankers = opInspectionMilkTankersMapper.selectOpInspectionMilkTankersByInspectionMilkTankersId
                (opMilkSampleQualityInspection.getInspectionMilkTankersId());
        if (opInspectionMilkTankers == null) {
            throw new IllegalArgumentException("推送奶源失败，未找到对应的检查单");
        }

        QualitySyncDTO qualitySyncDTO = new QualitySyncDTO();
        qualitySyncDTO.setOrderNumber(opMilkSampleQualityInspection.getMilkSourcePlanOrderNumber());
        qualitySyncDTO.setCarId(Integer.valueOf(opInspectionMilkTankers.getCarId()));
        qualitySyncDTO.setCarNumber(opInspectionMilkTankers.getLicensePlateNumber());
        qualitySyncDTO.setDriverId(Integer.valueOf(opInspectionMilkTankers.getDriverId()));
        qualitySyncDTO.setDriverName(opInspectionMilkTankers.getDriverName());
        qualitySyncDTO.setTrailerId(Integer.valueOf(opInspectionMilkTankers.getTrailerId()));
        qualitySyncDTO.setTrailerNumber(opInspectionMilkTankers.getTrailerNumber());
        qualitySyncDTO.setTemperature(Double.parseDouble(opMilkSampleQualityInspection.getMilkTemperature()));
        qualitySyncDTO.setReasonType(opMilkSampleQualityInspection.getExceptionType() == null ?
                "无" : opMilkSampleQualityInspection.getExceptionType());
        qualitySyncDTO.setAlcohol(opMilkSampleQualityInspection.getAlcoholValue());

        // 整合抗生素，将四个字段逗号拼接为一个字段
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(opMilkSampleQualityInspection.getBetaLactam());
        stringBuilder.append(",");
        stringBuilder.append(opMilkSampleQualityInspection.getTetracyclines());
        stringBuilder.append(",");
        stringBuilder.append(opMilkSampleQualityInspection.getCeftiofur());
        stringBuilder.append(",");
        stringBuilder.append(opMilkSampleQualityInspection.getCephalexin());
        qualitySyncDTO.setAntibiotic(stringBuilder.toString());
        qualitySyncDTO.setAflatoxin(opMilkSampleQualityInspection.getAflatoxinM1());
        qualitySyncDTO.setExperiment(opMilkSampleQualityInspection.getAcidity());
        qualitySyncDTO.setTasteAndSmell(opMilkSampleQualityInspection.getSensoryIndex());
        qualitySyncDTO.setPhosphate(opMilkSampleQualityInspection.getPhosphateTest());
        qualitySyncDTO.setStatus("1");

        R r = rmtsRanchLimsService.qualityInfo(qualitySyncDTO);
        if (r.getCode() == 200){
            opInspectionMilkTankers.setIsPushMilkSource("1");
            opInspectionMilkTankersMapper.updateOpInspectionMilkTankers(opInspectionMilkTankers);
        }

        // 推送图片同步
        PhotoSyncDTO photoSyncDTO = new PhotoSyncDTO();
        photoSyncDTO.setOrderNumber(opMilkSampleQualityInspection.getMilkSourcePlanOrderNumber());
        // 出场温度图片
        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getMilkTempPhoto())){
            try {
                // 查询相对路径
                SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getMilkTempPhoto());
                String base64 = ImageUtils.imageUrlToBase64(sysUploadFile.getFilePath());
                photoSyncDTO.setTemperaturePhoto(base64);
            }catch (IOException e){
                throw new RuntimeException("转换内壁图片为Base64编码失败: " + e.getMessage());
            }
        }
        // 酒精图片
        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getAlcoholPhotoUrl())){
            try {
                // 查询相对路径
                SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getAlcoholPhoto());
                String base64 = ImageUtils.imageUrlToBase64(sysUploadFile.getFilePath());
                photoSyncDTO.setAlcoholPhoto(base64);
            }catch (IOException e){
                throw new RuntimeException("转换酒精图片为Base64编码失败: " + e.getMessage());
            }
        }
        // 抗生素图片
        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getAntibioticPhotoUrl())){
            try {
                // 查询相对路径
                SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getAntibioticPhoto());
                String base64 = ImageUtils.imageUrlToBase64(sysUploadFile.getFilePath());
                photoSyncDTO.setAntibioticPhoto(base64);
            }catch (IOException e){
                throw new RuntimeException("转换抗生素图片为Base64编码失败: " + e.getMessage());
            }
        }
        // 黄曲霉毒素M1图片
        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getAflatoxinPhotoUrl())){
            try {
                // 查询相对路径
                SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getAflatoxinPhoto());
                String base64 = ImageUtils.imageUrlToBase64(sysUploadFile.getFilePath());
                photoSyncDTO.setAflatoxinPhoto(base64);
            }catch (IOException e){
                throw new RuntimeException("转换黄曲霉毒素M1图片为Base64编码失败: " + e.getMessage());
            }
        }
        // 酸度照片（掺碱试验）
        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getAcidityPhotoUrl())){
            try {
                // 查询相对路径
                SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getAcidityPhoto());
                String base64 = ImageUtils.imageUrlToBase64(sysUploadFile.getFilePath());
                photoSyncDTO.setExperimentPhoto(base64);
            }catch (IOException e){
                throw new RuntimeException("转换酸度照片为Base64编码失败: " + e.getMessage());
            }
        }
        // 感官评级图片
        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getSensoryIndexPhotoUrl())){
            try {
                // 查询相对路径
                SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getSensoryIndexPhoto());
                String base64 = ImageUtils.imageUrlToBase64(sysUploadFile.getFilePath());
                photoSyncDTO.setTasteAndSmellPhoto(base64);
            }catch (IOException e){
                throw new RuntimeException("转换感官评级图片为Base64编码失败: " + e.getMessage());
            }
        }
        // 磷酸盐实验
        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getPhosphatePhotoUrl())){
            try {
                // 查询相对路径
                SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getPhosphatePhoto());
                String base64 = ImageUtils.imageUrlToBase64(sysUploadFile.getFilePath());
                photoSyncDTO.setPhosphatePhoto(base64);
            }catch (IOException e){
                throw new RuntimeException("转换磷酸盐实验图片为Base64编码失败: " + e.getMessage());
            }
        }
        // 血奶图片
        if (StringUtils.isNotEmpty(opMilkSampleQualityInspection.getBloodMilkTestPhotoUrl())){
            try {
                // 查询相对路径
                SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(opMilkSampleQualityInspection.getBloodMilkTestPhoto());
                String base64 = ImageUtils.imageUrlToBase64(sysUploadFile.getFilePath());
                photoSyncDTO.setBloodMilkPhoto(base64);
            }catch (IOException e){
                throw new RuntimeException("转换血奶图片为Base64编码失败: " + e.getMessage());
            }
        }

        R r1 = rmtsRanchLimsService.photoSync(photoSyncDTO);

        if (r1.getCode() == 200 && r.getCode() == 200){
            opMilkSampleQualityInspection.setIsPushMilkSource("1");
        }

        return opMilkSampleQualityInspectionMapper.updateOpMilkSampleQualityInspection(opMilkSampleQualityInspection);
    }

    @Override
    public int sampling(OpMilkSampleQualityInspection opMilkSampleQualityInspection) {
        if (StringUtils.isEmpty(opMilkSampleQualityInspection.getOpMilkSampleQualityInspectionId())) {
            throw new IllegalArgumentException("质检单不存在");
        }

        // 检查单是否已取样
        if ("1".equals(opMilkSampleQualityInspection.getStatus())){
            throw new IllegalArgumentException("已取样的检查单不能重复取样");
        }

        if ("2".equals(opMilkSampleQualityInspection.getStatus())){
            throw new IllegalArgumentException("已审核的质检单不能取样");
        }

        SysUser sysUser = sysUserMapper.selectUserById(SecurityUtils.getUserId());

        opMilkSampleQualityInspection.setSamplingTime(new Date());
        opMilkSampleQualityInspection.setSamplerId(SecurityUtils.getUserId().toString());
        opMilkSampleQualityInspection.setSampler(sysUser.getNickName());

        // 更新检查单状态为已取样
        opMilkSampleQualityInspection.setIsSampling("1");
        opMilkSampleQualityInspection.setStatus("1");
        // 自动填充更新信息
        opMilkSampleQualityInspection.fillUpdateInfo();
        return opMilkSampleQualityInspectionMapper.updateOpMilkSampleQualityInspection(opMilkSampleQualityInspection);
    }

    @Override
    public List<OpMilkSampleQIException> selectOpMilkSampleQIExceptionList(OpMilkSampleQIException opMilkSampleQIException) {
        Long deptId = SecurityUtils.getDeptId();

        boolean admin = SecurityUtils.isAdmin(SecurityUtils.getUserId());

        if (!admin){
            opMilkSampleQIException.setDeptId(deptId.toString());
        }

        // 查询质检单列表
        List<OpMilkSampleQIException> opMilkSampleQIExceptionList = opMilkSampleQualityInspectionMapper.selectOpMilkSampleQIExceptionList(opMilkSampleQIException);

        SysDept sysDept = sysDeptMapper.selectDeptById(deptId);
        String sapName = sysDept.getSapName();

        // 查询质检信息同步接口
        FactoryQualityDTO factoryQualityDTO = new FactoryQualityDTO();
        factoryQualityDTO.setSchedulingDate(opMilkSampleQIException.getSchedulingDate());
        factoryQualityDTO.setPastureCode(sapName);

        R r = rmtsRanchLimsService.factoryQuality(factoryQualityDTO);
        if (r.getData() != null){
            List<MilkTransportDetectionInfoVO> infoVOList = (List<MilkTransportDetectionInfoVO>) r.getData();

            // 根据查询出来的订单号，如果质检单内有重复数据，则排除质检单内的数据，并返回
            // 3. 执行过滤
            List<OpMilkSampleQIException> result = filterListSafe(opMilkSampleQIExceptionList, infoVOList);

            return result;
        }else {
            // 4. 如果接口数据为空，返回质检单列表
            return opMilkSampleQIExceptionList;
        }
    }

    @Override
    public int submitOpMilkSampleQIException(OpMilkSampleQIException opMilkSampleQIException) {
        // 查询异常是否已提交
        OpMilkSampleQualityInspection opMilkSampleQualityInspection = opMilkSampleQualityInspectionMapper.
                selectOpMilkSampleQualityInspectionByOpMilkSampleQualityInspectionId
                        (opMilkSampleQIException.getOpMilkSampleQualityInspectionId());
        if (opMilkSampleQualityInspection != null && "1".equals(opMilkSampleQualityInspection.getIsExceptionSubmit())){
            throw new IllegalArgumentException("已提交的异常不能重复提交");
        }

        opMilkSampleQualityInspection.setExceptionType(opMilkSampleQIException.getExceptionType());
        opMilkSampleQualityInspection.setExceptionDesc(opMilkSampleQIException.getExceptionDesc());
        opMilkSampleQualityInspection.setIsExceptionSubmit("1");
        // 自动填充更新信息
        opMilkSampleQualityInspection.fillUpdateInfo();
        return opMilkSampleQualityInspectionMapper.updateOpMilkSampleQualityInspection(opMilkSampleQualityInspection);
    }

    @Override
    public List<ExitInspectionReportVO> exitInspectionReport(ExitInspectionReportDTO exitInspectionReportDTO) {
        return opMilkSampleQualityInspectionMapper.exitInspectionReport(exitInspectionReportDTO);
    }

    /**
     * 增强版过滤：支持list1/list2为null或空列表，避免NPE
     * @param list1 待过滤列表（OpMilkSampleQIException）
     * @param list2 参考列表（MilkTransportDetectionInfo）
     * @return 过滤后的list1（若list1为null，返回空列表）
     */
    public static List<OpMilkSampleQIException> filterListSafe(
            List<OpMilkSampleQIException> list1,
            List<MilkTransportDetectionInfoVO> list2) {

        // 处理list1为null的情况：直接返回空列表
        if (list1 == null || list1.isEmpty()) {
            return new ArrayList<>();
        }

        // 处理list2为null/空的情况：直接返回原list1（无数据可过滤）
        if (list2 == null || list2.isEmpty()) {
            return new ArrayList<>(list1); // 返回拷贝，避免修改原列表
        }

        // 提取list2的orderNumber到Set（去重+高效查询）
        Set<String> orderNumberSet = list2.stream()
                .map(MilkTransportDetectionInfoVO::getOrderNumber)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 过滤list1：核心逻辑
        return list1.stream()
                .filter(item -> {
                    String milkSourcePlanNo = item.getMilkSourcePlanOrderNumber();
                    // 逻辑：如果milkSourcePlanNo不为null，且在Set中 → 剔除；否则保留
                    return !(milkSourcePlanNo != null && orderNumberSet.contains(milkSourcePlanNo));
                })
                .collect(Collectors.toList());
    }

}
