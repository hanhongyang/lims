package com.gmlimsqi.business.samplingplan.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.gmlimsqi.business.ranch.domain.OpSamplingPlan;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlanItem;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;
import com.gmlimsqi.business.ranch.domain.OpTestResultBase;
import com.gmlimsqi.business.ranch.mapper.OpSamplingPlanItemMapper;
import com.gmlimsqi.business.ranch.mapper.OpSamplingPlanMapper;
import com.gmlimsqi.business.ranch.mapper.OpSamplingPlanSampleMapper;
import com.gmlimsqi.business.samplingplan.mapper.OpFinishedProductSamplingPlanMapper;
import com.gmlimsqi.business.samplingplan.pojo.entity.OpFinishedProductSamplingPlan;
import com.gmlimsqi.business.samplingplan.pojo.entity.OpFinishedProductSamplingPlanDetail;
import com.gmlimsqi.business.samplingplan.pojo.vo.OpSamplingPlanSampleInfoVO;
import com.gmlimsqi.business.samplingplan.pojo.vo.OpSamplingPlanSampleVO;
import com.gmlimsqi.business.samplingplan.service.IOpFinishedProductSamplingPlanService;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.IdListHandler;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.enums.SamplePlanStatusEnum;
import com.gmlimsqi.common.enums.UnqualityMethodEnum;
import com.gmlimsqi.common.enums.YesNo2Enum;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.annotation.DataScope;
import com.gmlimsqi.system.domain.SysUploadFile;
import com.gmlimsqi.system.mapper.SysUploadFileMapper;
import com.gmlimsqi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import static com.gmlimsqi.business.util.CodeGeneratorUtil.*;


/**
 * 成品，库存，垫料取样计划Service业务层处理
 * * @author hhy
 * @date 2025-11-24
 */
@Service
public class OpFinishedProductSamplingPlanServiceImpl implements IOpFinishedProductSamplingPlanService
{
    @Autowired
    private OpFinishedProductSamplingPlanMapper opFinishedProductSamplingPlanMapper;
    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private SysUploadFileMapper sysUploadFileMapper;

    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;

    @Autowired
    private OpSamplingPlanSampleMapper opSamplingPlanSampleMapper;

    @Autowired
    private OpSamplingPlanItemMapper opSamplingPlanItemMapper;

    @Autowired
    private OpSamplingPlanMapper opSamplingPlanMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询成品，库存，垫料取样计划
     * * @param finishedProductSamplingPlanId 成品，库存，垫料取样计划主键
     * @return 成品，库存，垫料取样计划
     */
    @Override
    public OpFinishedProductSamplingPlan selectOpFinishedProductSamplingPlanByFinishedProductSamplingPlanId(String finishedProductSamplingPlanId)
    {
        OpFinishedProductSamplingPlan opFinishedProductSamplingPlan = opFinishedProductSamplingPlanMapper.
                selectOpFinishedProductSamplingPlanByFinishedProductSamplingPlanId(finishedProductSamplingPlanId);

        List<OpFinishedProductSamplingPlanDetail> opFinishedProductSamplingPlanDetailList =
                opFinishedProductSamplingPlan.getOpFinishedProductSamplingPlanDetailList();


        if (opFinishedProductSamplingPlanDetailList!= null){
            for (OpFinishedProductSamplingPlanDetail opFinishedProductSamplingPlanDetail : opFinishedProductSamplingPlanDetailList){
                // 处理附件，按逗号分隔
                if (StringUtils.isNotEmpty(opFinishedProductSamplingPlanDetail.getFile())){
                    List<String> list = IdListHandler.parseIdStr(opFinishedProductSamplingPlanDetail.getFile());
                    if (!list.isEmpty()){
                        List<String> urlList = new ArrayList<>();
                        for (String fileId : list){
                            SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(fileId);
                            urlList.add(sysUploadFile.getUrl());
                        }
                        opFinishedProductSamplingPlanDetail.setFileUrl(urlList);
                    }
                }
            }
        }

        return opFinishedProductSamplingPlan;
    }

    /**
     * 查询成品，库存，垫料取样计划列表
     * * @param opFinishedProductSamplingPlan 成品，库存，垫料取样计划
     * @return 成品，库存，垫料取样计划
     */
    // +++ 新增：@DataScope 注解以激活数据权限切面 +++
    @Override
    public List<OpFinishedProductSamplingPlan> selectOpFinishedProductSamplingPlanList(OpFinishedProductSamplingPlan opFinishedProductSamplingPlan)
    {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            opFinishedProductSamplingPlan.setDeptId(SecurityUtils.getDeptId().toString());
        }

        // 处理planType，将字符串转换为整数列表
        if (opFinishedProductSamplingPlan.getPlanType() != null){
            opFinishedProductSamplingPlan.setPlanTypeList(convertToStringList(opFinishedProductSamplingPlan.getPlanType()));
        }

        List<OpFinishedProductSamplingPlan> items = opFinishedProductSamplingPlanMapper.selectOpFinishedProductSamplingPlanList(opFinishedProductSamplingPlan);

        for (OpFinishedProductSamplingPlan item : items){
            item.setOpFinishedProductSamplingPlanDetailList(
                    opFinishedProductSamplingPlanMapper.selectPlanDetailListBySamplingPlanId(item.getFinishedProductSamplingPlanId()));
        }

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpFinishedProductSamplingPlan::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 前端参数（如"1,0"）转为String类型List（适配字符串字段）
     */
    public static List<String> convertToStringList(String input) {
        if (StringUtils.isBlank(input)) {
            return List.of();
        }
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    /**
     * 新增成品，库存，垫料取样计划
     * * @param opFinishedProductSamplingPlan 成品，库存，垫料取样计划
     * @return 结果
     */
    @Transactional
    @Override
    public int insertOpFinishedProductSamplingPlan(OpFinishedProductSamplingPlan opFinishedProductSamplingPlan)
    {
        if (StringUtils.isEmpty(opFinishedProductSamplingPlan.getFinishedProductSamplingPlanId())) {
        opFinishedProductSamplingPlan.setFinishedProductSamplingPlanId(IdUtils.simpleUUID());
            for (OpFinishedProductSamplingPlanDetail opFinishedProductSamplingPlanDetail :
                    opFinishedProductSamplingPlan.getOpFinishedProductSamplingPlanDetailList()){
            opFinishedProductSamplingPlanDetail.setFinishedProductSamplingPlanDetailId(IdUtils.simpleUUID());
            opFinishedProductSamplingPlanDetail.setFinishedProductSamplingPlanId(opFinishedProductSamplingPlan.getFinishedProductSamplingPlanId());
            opFinishedProductSamplingPlanDetail.fillCreateInfo();
            }
        }

        String resultNo = null;

        if (StringUtils.isNotEmpty(opFinishedProductSamplingPlan.getFinishedProductSamplingPlanId())){
            // 生成计划单号
            try {
                // 判断类型，是成品取样，还是库存取样，还是垫料取样
                if ("0".equals(opFinishedProductSamplingPlan.getPlanType())){
                    // 成品取样计划
                    resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_FINISHED_PRODUCT_SAMPLING_PLAN);
                } else if ("1".equals(opFinishedProductSamplingPlan.getPlanType())){
                    // 库存取样计划
                    resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_INVENTORY_SAMPLING_PLAN);
                } else if ("2".equals(opFinishedProductSamplingPlan.getPlanType())){
                    // 垫料取样计划
                    resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_FEED_SAMPLING_PLAN);
                }else if ("3".equals(opFinishedProductSamplingPlan.getPlanType())){
                    // 原料取样计划
                    resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_YL_SAMPLING_PLAN);
                }

                opFinishedProductSamplingPlan.setSamplingOrderNumber(resultNo);
            } catch (BusinessException e) {
                throw new RuntimeException("生成取样计划编号失败: " + e.getMessage());
            }
        }

        // +++ 自动填充部门ID，用于数据权限 +++
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser))
        {
            // 假设您的实体类中有名为 setDeptId 的方法
            // (请确保您的 OpFinishedProductSamplingPlan 实体类中有 setDeptId(String deptId) 方法)
            opFinishedProductSamplingPlan.setDeptId(loginUser.getDeptId().toString());
        }

        // 自动填充创建/更新信息
        opFinishedProductSamplingPlan.fillCreateInfo();
//        opFinishedProductSamplingPlan.setStatus("0");
        int rows = opFinishedProductSamplingPlanMapper.insertOpFinishedProductSamplingPlan(opFinishedProductSamplingPlan);
        insertOpFinishedProductSamplingPlanDetail(opFinishedProductSamplingPlan);
        return rows;
    }

    /**
     * 修改成品，库存，垫料取样计划
     * * @param opFinishedProductSamplingPlan 成品，库存，垫料取样计划
     * @return 结果
     */
    @Transactional
    @Override
    public int updateOpFinishedProductSamplingPlan(OpFinishedProductSamplingPlan opFinishedProductSamplingPlan)
    {
        // 1. 查询取样计划是否存在
        OpFinishedProductSamplingPlan status = opFinishedProductSamplingPlanMapper.
                selectOpFinishedProductSamplingPlanByFinishedProductSamplingPlanId(opFinishedProductSamplingPlan.getFinishedProductSamplingPlanId());
        if (StringUtils.isNull(status)){
            throw new ServiceException("取样计划不存在");
        }

        // 2. 检查是否为待审核状态
        if (!StringUtils.equals("0", status.getStatus())){
            throw new ServiceException("只能修改未关联状态的取样计划");
        }

        // 自动填充更新信息
        opFinishedProductSamplingPlan.fillUpdateInfo();
        //删除子表
        opFinishedProductSamplingPlanMapper.
                deleteOpFinishedProductSamplingPlanDetailByFinishedProductSamplingPlanId
                        (opFinishedProductSamplingPlan.getFinishedProductSamplingPlanId());

        for (OpFinishedProductSamplingPlanDetail opFinishedProductSamplingPlanDetail :
                opFinishedProductSamplingPlan.getOpFinishedProductSamplingPlanDetailList()){
            opFinishedProductSamplingPlanDetail.setFinishedProductSamplingPlanDetailId(IdUtils.simpleUUID());
            opFinishedProductSamplingPlanDetail.setFinishedProductSamplingPlanId(opFinishedProductSamplingPlan.getFinishedProductSamplingPlanId());
            opFinishedProductSamplingPlanDetail.fillCreateInfo();
        }
        insertOpFinishedProductSamplingPlanDetail(opFinishedProductSamplingPlan);
        return opFinishedProductSamplingPlanMapper.updateOpFinishedProductSamplingPlan(opFinishedProductSamplingPlan);
    }

    @Override
    @Transactional
    public void batchSaveMainAndSub(List<OpFinishedProductSamplingPlan> planList) {
        if (!planList.isEmpty()){
           for (OpFinishedProductSamplingPlan opFinishedProductSamplingPlan : planList){
               if (StringUtils.isEmpty(opFinishedProductSamplingPlan.getFinishedProductSamplingPlanId())) {
                   opFinishedProductSamplingPlan.setFinishedProductSamplingPlanId(IdUtils.simpleUUID());
                   for (OpFinishedProductSamplingPlanDetail opFinishedProductSamplingPlanDetail :
                           opFinishedProductSamplingPlan.getOpFinishedProductSamplingPlanDetailList()){
                       opFinishedProductSamplingPlanDetail.setFinishedProductSamplingPlanDetailId(IdUtils.simpleUUID());
                       opFinishedProductSamplingPlanDetail.setFinishedProductSamplingPlanId(opFinishedProductSamplingPlan.getFinishedProductSamplingPlanId());
                       opFinishedProductSamplingPlanDetail.fillCreateInfo();
                   }
               }

               String resultNo = null;

               // 生成计划单号
               try {
                   // 判断类型，是成品取样，还是库存取样，还是垫料取样
                   if ("0".equals(opFinishedProductSamplingPlan.getPlanType())){
                       // 成品取样计划
                       resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_FINISHED_PRODUCT_SAMPLING_PLAN);
                   } else if ("1".equals(opFinishedProductSamplingPlan.getPlanType())){
                       // 库存取样计划
                       resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_INVENTORY_SAMPLING_PLAN);
                   } else if ("2".equals(opFinishedProductSamplingPlan.getPlanType())){
                       // 垫料取样计划
                       resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_FEED_SAMPLING_PLAN);
                   } else if ("3".equals(opFinishedProductSamplingPlan.getPlanType())){
                       // 原料取样计划
                       resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_YL_SAMPLING_PLAN);
                   }

                   opFinishedProductSamplingPlan.setSamplingOrderNumber(resultNo);
               } catch (BusinessException e) {
                   throw new RuntimeException("生成取样计划编号失败: " + e.getMessage());
               }

               // +++ 自动填充部门ID，用于数据权限 +++
               LoginUser loginUser = SecurityUtils.getLoginUser();
               if (StringUtils.isNotNull(loginUser))
               {
                   opFinishedProductSamplingPlan.setDeptId(loginUser.getDeptId().toString());
               }

               // 自动填充创建/更新信息
               opFinishedProductSamplingPlan.fillCreateInfo();
               int rows = opFinishedProductSamplingPlanMapper.insertOpFinishedProductSamplingPlan(opFinishedProductSamplingPlan);
               insertOpFinishedProductSamplingPlanDetail(opFinishedProductSamplingPlan);
           }
        }
    }

    @Override
    public void audit(String finishedProductSamplingPlanId) {
        // 1. 查询取样计划是否存在
        OpFinishedProductSamplingPlan opFinishedProductSamplingPlan = opFinishedProductSamplingPlanMapper.
                selectOpFinishedProductSamplingPlanByFinishedProductSamplingPlanId(finishedProductSamplingPlanId);
        if (StringUtils.isNull(opFinishedProductSamplingPlan)){
            throw new ServiceException("取样计划不存在");
        }

        // 2. 更新状态为已审核
        opFinishedProductSamplingPlan.setStatus("1");
        opFinishedProductSamplingPlanMapper.updateOpFinishedProductSamplingPlan(opFinishedProductSamplingPlan);
    }

    @Override
    public List<OpFinishedProductSamplingPlanDetail> selectOpFinishedProductSamplingPlanDetailList(OpFinishedProductSamplingPlanDetail detail) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            detail.setDeptId(SecurityUtils.getDeptId().toString());
        }

        List<OpFinishedProductSamplingPlanDetail> opFinishedProductSamplingPlanDetails = opFinishedProductSamplingPlanMapper.selectDetailList(detail);

        if (opFinishedProductSamplingPlanDetails != null && !opFinishedProductSamplingPlanDetails.isEmpty()){
            for (OpFinishedProductSamplingPlanDetail opFinishedProductSamplingPlanDetail : opFinishedProductSamplingPlanDetails){
                // 查询主表
                OpFinishedProductSamplingPlan opFinishedProductSamplingPlan = opFinishedProductSamplingPlanMapper.selectOpFinishedProductSamplingPlanByFinishedProductSamplingPlanId(opFinishedProductSamplingPlanDetail.getFinishedProductSamplingPlanId());

                SysUser sysUser = sysUserMapper.selectUserById(Long.parseLong(opFinishedProductSamplingPlan.getCreateBy()));
                opFinishedProductSamplingPlanDetail.setImportBy(sysUser.getNickName());
            }
        }

        return opFinishedProductSamplingPlanDetails;
    }

    @Override
    public List<OpSamplingPlanSampleVO> selectOpSamplingPlanSampleList(OpSamplingPlanSample sample) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            sample.setDeptId(SecurityUtils.getDeptId().toString());
        }

        // 处理planType，将字符串转换为整数列表
        if (sample.getSamplingType() != null){
            sample.setSamplingTypeList(convertToStringList(sample.getSamplingType()));
        }
        List<OpSamplingPlanSampleVO> opSamplingPlanSampleVOS = opSamplingPlanSampleMapper.selectOpSamplingPlanSampleListLinkOFPSP(sample);
        if (!opSamplingPlanSampleVOS.isEmpty()) {
            opSamplingPlanSampleVOS.forEach(vo -> {
                if (StringUtils.isNotEmpty(vo.getCreateBy())) {
                    SysUser sysUser = sysUserMapper.selectUserById(Long.parseLong(vo.getCreateBy()));
                    vo.setSamplerName(sysUser.getNickName());
                }
                // 根据样品ID查询关联的检测项目列表
                List<OpSamplingPlanItem> itemList = opSamplingPlanItemMapper.selectOpSamplingPlanItemBySampleId(vo.getOpSamplingPlanSampleId());
                vo.setOpSamplingPlanItemList(itemList);
            });
        }
        return opSamplingPlanSampleVOS;
    }

    @Override
    public int updateOpFinishedProductSamplingPlanDetail(OpFinishedProductSamplingPlanDetail detail) {

        // 1. 查询取样计划详情是否存在
        OpFinishedProductSamplingPlanDetail opFinishedProductSamplingPlanDetail = opFinishedProductSamplingPlanMapper.
                selectOpFinishedProductSamplingPlanDetailByFinishedProductSamplingPlanDetailId(detail.getFinishedProductSamplingPlanDetailId());
        if (StringUtils.isNull(opFinishedProductSamplingPlanDetail)){
            throw new ServiceException("取样计划详情不存在");
        }

        // 查询主表状态
        OpFinishedProductSamplingPlan opFinishedProductSamplingPlan = opFinishedProductSamplingPlanMapper.
                selectOpFinishedProductSamplingPlanByFinishedProductSamplingPlanId(detail.getFinishedProductSamplingPlanId());
        if (StringUtils.isNull(opFinishedProductSamplingPlan)){
            throw new ServiceException("取样计划不存在");
        }

        // 2. 检查是否为待审核状态
        if (!StringUtils.equals("0", opFinishedProductSamplingPlan.getStatus())){
            throw new ServiceException("只能修改未关联状态的取样计划详情");
        }

        return opFinishedProductSamplingPlanMapper.updateOpFinishedProductSamplingPlanDetail(detail);
    }

    @Override
    public OpFinishedProductSamplingPlanDetail selectOpFinishedProductSamplingPlanDetailByFinishedProductSamplingPlanDetailId(String finishedProductSamplingPlanDetailId) {
        return opFinishedProductSamplingPlanMapper.selectOpFinishedProductSamplingPlanDetailByFinishedProductSamplingPlanDetailId(finishedProductSamplingPlanDetailId);
    }

    @Override
    public int addOpSamplingPlanSample(OpSamplingPlanSample sample) {
        sample.fillCreateInfo();
        String sampleId = IdUtils.simpleUUID();

        sample.setOpSamplingPlanSampleId(sampleId);
        sample.setIsDelete(YesNo2Enum.NO.getCode());
        sample.setIsPushSap("0");

        Long deptId = SecurityUtils.getDeptId();
        sample.setDeptId(deptId.toString());

        String resultNo = "";

        // 生成计划单号
        try {
            // 判断类型，是成品取样，还是库存取样，还是垫料取样
            if (StringUtils.equals("0", sample.getSamplingType())){
                // 成品取样计划
                resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_FINISHED_PRODUCT_SAMPLING_ORDER);
            } else if (StringUtils.equals("1", sample.getSamplingType())){
                // 库存取样计划
                resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_INVENTORY_SAMPLING_ORDER);
            } else if (StringUtils.equals("2", sample.getSamplingType())){
                // 垫料取样计划
                resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_FEED_SAMPLING_ORDER);
            }

            sample.setSampleNo(resultNo);
        } catch (BusinessException e) {
            throw new RuntimeException("生成取样单号失败: " + e.getMessage());
        }

        //处理检测项目
        List<OpSamplingPlanItem> itemList = sample.getOpSamplingPlanItemList();
        if (ObjectUtils.isEmpty(itemList)) {
            throw new RuntimeException("未选择检验项目");
        } else {
            for (OpSamplingPlanItem item : itemList) {
                item.fillCreateInfo();
                item.setIsDelete("0");
                item.setOpSamplingPlanItemId(IdUtils.simpleUUID());
                item.setSamplingPlanSampleId(sampleId);
            }
        }
        opSamplingPlanItemMapper.batchInsertOpSamplingPlanItem(itemList);

        return opSamplingPlanSampleMapper.insertOpSamplingPlanSample(sample);
    }

    @Override
    public int updateOpSamplingPlanSample(OpSamplingPlanSample sample) {
        //更新
        sample.fillUpdateInfo();
        //子表先删除
        opSamplingPlanItemMapper.updateItemDeleteFlagByPlanId(sample.getOpSamplingPlanSampleId(),
                String.valueOf(SecurityUtils.getUserId()));

        //处理检测项目
        List<OpSamplingPlanItem> itemList = sample.getOpSamplingPlanItemList();
        if (ObjectUtils.isEmpty(itemList)) {
            throw new RuntimeException("未选择检验项目");
        } else {
            for (OpSamplingPlanItem item : itemList) {
                item.fillCreateInfo();
                item.setIsDelete("0");
                item.setOpSamplingPlanItemId(IdUtils.simpleUUID());
                item.setSamplingPlanSampleId(sample.getOpSamplingPlanSampleId());
            }
        }
        opSamplingPlanItemMapper.batchInsertOpSamplingPlanItem(itemList);

        if (StringUtils.isNotEmpty(sample.getSamplingPlanId())){
            // 根据id查询计划子表数据
            OpFinishedProductSamplingPlanDetail opFinishedProductSamplingPlanDetail = opFinishedProductSamplingPlanMapper.
                    selectOpFinishedProductSamplingPlanDetailByFinishedProductSamplingPlanIdAndMaterialCode(
                            sample.getSamplingPlanId(), sample.getInvbillCode());

            if (opFinishedProductSamplingPlanDetail == null){
                throw new ServiceException("未找到相关取样计划详情");
            }

            opFinishedProductSamplingPlanDetail.setHaveSampleCopies(
                    opFinishedProductSamplingPlanDetail.getHaveSampleCopies() + 1);

            // 更新子表数据
            opFinishedProductSamplingPlanMapper.updateOpFinishedProductSamplingPlanDetail(opFinishedProductSamplingPlanDetail);
        }

        sample.fillUpdateInfo();
        return opSamplingPlanSampleMapper.updateOpSamplingPlanSample(sample);
    }

    @Override
    public OpSamplingPlanSample selectOpSamplingPlanSampleDetail(String opSamplingPlanSampleId) {
        OpSamplingPlanSample opSamplingPlanSample = opSamplingPlanSampleMapper.selectOpSamplingPlanSampleDetail(opSamplingPlanSampleId);

        if (opSamplingPlanSample == null){
            throw new ServiceException("未找到相关取样计划详情");
        }

        SysUser sysUser = sysUserMapper.selectUserById(Long.parseLong(opSamplingPlanSample.getCreateBy()));

        if (sysUser == null){
            throw new ServiceException("未找到相关取样人");
        }

        opSamplingPlanSample.setSamplerName(sysUser.getNickName());

        List<OpSamplingPlanItem> opSamplingPlanItemList = opSamplingPlanItemMapper.selectOpSamplingPlanItemBySampleId(opSamplingPlanSampleId);

        if (ObjectUtils.isEmpty(opSamplingPlanItemList)){
            throw new ServiceException("未找到相关检验项目");
        }

        opSamplingPlanSample.setOpSamplingPlanItemList(opSamplingPlanItemList);

        return opSamplingPlanSample;
    }

    @Override
    public int cancelOpSamplingPlan(String finishedProductSamplingPlanId) {
        // 1. 校验
        if (StringUtils.isEmpty(finishedProductSamplingPlanId)) {
            throw new ServiceException("关键参数缺失，无法作废计划");
        }

        // 2. 查询原始单据，确保它存在
        OpFinishedProductSamplingPlan plan = opFinishedProductSamplingPlanMapper.selectOpSamplingPlanByOpSamplingPlanId(finishedProductSamplingPlanId);
        if(plan == null){
            throw new ServiceException("未找到该计划");
        }

        // 3. 创建一个只包含需要更新字段的实体
        OpFinishedProductSamplingPlan planToUpdate = new OpFinishedProductSamplingPlan();
        planToUpdate.setFinishedProductSamplingPlanId(finishedProductSamplingPlanId);
        planToUpdate.setStatus("3");

        // 4. [关键审计] 自动填充 update_by 和 update_time
        planToUpdate.fillUpdateInfo();

        return opFinishedProductSamplingPlanMapper.updateOpFinishedProductSamplingPlan(planToUpdate);
    }

    /**
     * 新增成品，库存，垫料取样计划详情信息
     * * @param opFinishedProductSamplingPlan 成品，库存，垫料取样计划对象
     */
    public void insertOpFinishedProductSamplingPlanDetail(OpFinishedProductSamplingPlan opFinishedProductSamplingPlan)
    {
        List<OpFinishedProductSamplingPlanDetail> opFinishedProductSamplingPlanDetailList = opFinishedProductSamplingPlan.getOpFinishedProductSamplingPlanDetailList();
        String finishedProductSamplingPlanId = opFinishedProductSamplingPlan.getFinishedProductSamplingPlanId();
        if (StringUtils.isNotNull(opFinishedProductSamplingPlanDetailList))
        {
            List<OpFinishedProductSamplingPlanDetail> list = new ArrayList<>();
            for (OpFinishedProductSamplingPlanDetail opFinishedProductSamplingPlanDetail : opFinishedProductSamplingPlanDetailList)
            {
                opFinishedProductSamplingPlanDetail.setFinishedProductSamplingPlanId(finishedProductSamplingPlanId);
                list.add(opFinishedProductSamplingPlanDetail);
            }
            if (!list.isEmpty())
            {
                    opFinishedProductSamplingPlanMapper.batchOpFinishedProductSamplingPlanDetail(list);
            }
        }
    }
}
