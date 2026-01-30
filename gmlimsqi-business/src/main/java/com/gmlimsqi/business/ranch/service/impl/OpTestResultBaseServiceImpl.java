// (文件: com/gmlimsqi/business/ranch/service/impl/OpTestResultBaseServiceImpl.java)
// 【请替换您已有的 OpTestResultBaseServiceImpl.java】
package com.gmlimsqi.business.ranch.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import com.gmlimsqi.business.bsweighbridge.domain.BsWeighBridge;
import com.gmlimsqi.business.bsweighbridge.mapper.BsWeighBridgeMapper;
import com.gmlimsqi.business.ranch.domain.*;
import com.gmlimsqi.business.ranch.dto.GetJCKCTestDTO;
import com.gmlimsqi.business.ranch.dto.OpSamplingPlanSamplePushSapDTO;
import com.gmlimsqi.business.ranch.dto.changelog.ResultChangeSaveDTO;
import com.gmlimsqi.business.ranch.mapper.*;
import com.gmlimsqi.business.ranch.service.IOpTestResultChangeLogService;
import com.gmlimsqi.business.ranch.vo.*;
import com.gmlimsqi.business.unquality.domain.OpSampleUnquality;
import com.gmlimsqi.business.unquality.domain.OpSampleUnqualityDetail;
import com.gmlimsqi.business.unquality.service.IOpSampleUnqualityService;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.SpringStoredProcCaller;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.core.domain.entity.SysDept;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.exception.ServiceException; // (新) 引入
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.github.pagehelper.Page;
import com.gmlimsqi.common.core.page.TableSupport;
import com.gmlimsqi.common.core.page.PageDomain;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.annotation.DataScope;
import com.gmlimsqi.sap.accept.domain.dto.CheckResRetJudgeDTO;
import com.gmlimsqi.sap.accept.domain.dto.CheckResRetJudgeItemDTO;
import com.gmlimsqi.sap.accept.domain.dto.InboundInspectionDTO;
import com.gmlimsqi.sap.accept.domain.vo.CheckResRetJudgeVO;
import com.gmlimsqi.sap.accept.domain.vo.InboundInspectionDetailVO;
import com.gmlimsqi.sap.util.SapUtils;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import com.gmlimsqi.system.service.ISysUploadFileService; // (新) 引入
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // (新) 引入
import org.springframework.util.CollectionUtils; // (新) 引入
import com.gmlimsqi.business.ranch.service.IOpTestResultBaseService;

import javax.annotation.Resource;

@Service
@Slf4j
public class OpTestResultBaseServiceImpl implements IOpTestResultBaseService {
    @Autowired
    private OpTestResultBaseMapper opTestResultBaseMapper;
    @Autowired
    private OpTestResultInfoMapper opTestResultInfoMapper;
    @Autowired
    private OpSamplingPlanItemMapper opSamplingPlanItemMapper;
    @Autowired
    private OpSamplingPlanSampleMapper opSamplingPlanSampleMapper;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired(required = false)
    private ISysUploadFileService sysUploadFileService;

    @Autowired
    private SapUtils sapUtils;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private BsWeighBridgeMapper bsWeighBridgeMapper;

    @Autowired
    private OpSamplingPlanMapper opSamplingPlanMapper;

    @Autowired
    private SpringStoredProcCaller springStoredProcCaller;

    // 注入Spring Boot自动配置的JdbcTemplate（无需手动配置DataSource）
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IOpSampleUnqualityService opSampleUnqualityService;

    @Autowired
    private IOpTestResultChangeLogService opTestResultChangeLogService;
    @Autowired
    private OpTestResultInfoSubMapper opTestResultInfoSubMapper;

    /**
     * 查询样品化验 (修改：同时查询子表)
     */
    @Override
    public OpTestResultBase selectOpTestResultBaseById(String id) {
        OpTestResultBase base = opTestResultBaseMapper.selectOpTestResultBaseById(id);
        if (base != null) {
            OpTestResultInfo queryInfo = new OpTestResultInfo();
            queryInfo.setBaseId(id);
            List<OpTestResultInfo> infoList = opTestResultInfoMapper.selectOpTestResultInfoList(queryInfo);
            base.setOpTestResultInfoList(infoList);

            for (OpTestResultInfo info : infoList) {
                // 查询是否有子表数据
                List<OpTestResultInfoSub> subList = opTestResultInfoSubMapper.selectOpTestResultInfoByBaseId(info.getId());

                if (!CollectionUtils.isEmpty(subList)) {
                    info.setSubList(subList);
                }

                if (StringUtils.isEmpty(info.getUpperLimit())) {
                    String upperLimit = "";
                    info.setUpperLimit(upperLimit);
                }

                if (StringUtils.isEmpty(info.getLowerLimit())) {
                    String lowerLimit = "";
                    info.setLowerLimit(lowerLimit);
                }

                // 查询样品化验项目信息
                if(StringUtils.isNotEmpty(info.getPlanItemId())) {
                    OpSamplingPlanItem item = opSamplingPlanItemMapper.selectOpSamplingPlanItemByOpSamplingPlanItemId(info.getPlanItemId());
                    info.setOpSamplingPlanItem(item);

                    if (StringUtils.isEmpty(item.getUpperLimit())) {
                        String upperLimit = "";
                        item.setUpperLimit(upperLimit);
                    }

                    if (StringUtils.isEmpty(item.getLowerLimit())) {
                        String lowerLimit = "";
                        item.setLowerLimit(lowerLimit);
                    }
                }

                // 查询取样人
                OpSamplingPlanSample sample = opSamplingPlanSampleMapper.selectOpSamplingPlanSampleByOpSamplingPlanSampleId(info.getPlanSampleId());
                if (sample != null) {
                    if (!StringUtils.isEmpty(sample.getCreateBy())) {
                        String username = userCacheService.getUsername(sample.getCreateBy());
                        base.setSamplerName(username);
                    }
                }
            }
        }
        return base;
    }

    /**
     * 查询样品化验列表 (Tab 2, 3, 4)
     */
//    @DataScope(deptAlias = "d")
    @Override
    public List<OpTestResultBase> selectOpTestResultBaseList(OpTestResultBase opTestResultBase) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            Long deptId = SecurityUtils.getDeptId();
            opTestResultBase.setDeptId(deptId.toString());
        }

        List<OpTestResultBase> items = opTestResultBaseMapper.selectOpTestResultBaseList(opTestResultBase);
        if ("4".equals(opTestResultBase.getStatus())){
            if (!items.isEmpty()){
                items.forEach(vo->{
                    vo.setExamineTime(vo.getUpdateTime());
                });
            }
        }
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpTestResultBase::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);
            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }
        return items;
    }

    /**
     * (新) 查询 "待化验" (Tab 1) 列表
     */
    @Override
//    @DataScope(deptAlias = "d", permission = "ranch:testResult:list")
    public List<SampleTaskVo> selectPendingTaskList(SampleTaskVo queryParams) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            Long deptId = SecurityUtils.getDeptId();
            queryParams.setDeptId(deptId.toString());
        }

        return opTestResultBaseMapper.selectPendingTaskList(queryParams);
    }

    /**
     * (新) 查询所有Tab的角标数量
     */
    @Override
    @DataScope(deptAlias = "d", permission = "ranch:testResult:list")
    public Map<String, Long> getTestResultCounts(SampleTaskVo pendingParams, OpTestResultBase listParams) {
        Map<String, Long> counts = new HashMap<>();

        // 1. 查询 Tab '1' (待化验)
        Long pendingCount = opTestResultBaseMapper.countPendingTaskList(pendingParams);
        counts.put("status_1", pendingCount); // 对应前端 Tab '1'

        // 2. 查询 Tab '2','3','4' (状态 2, 3, 4)
        List<Map<String, Object>> statusCounts = opTestResultBaseMapper.countByStatus(listParams);

        counts.put("status_2", 0L);
        counts.put("status_3", 0L);
        counts.put("status_4", 0L);

        for (Map<String, Object> row : statusCounts) {
            String status = (String) row.get("status");
            Long count = ((Number) row.get("count")).longValue();
            if (status != null && count != null) {
                counts.put("status_" + status, count); // 对应前端 Tab '2','3','4'
            }
        }

        return counts;
    }

    @Override
    @Transactional
    public int retest(List<String> infoId) {
        int count = 0;
        String updateUser = SecurityUtils.getUserId().toString();
        for (String s : infoId) {
            OpTestResultInfo info = opTestResultInfoMapper.selectOpTestResultInfoById(s);
            if (info != null) {
                OpSamplingPlanSample sample = opSamplingPlanSampleMapper.
                        selectOpSamplingPlanSampleByOpSamplingPlanSampleId(info.getPlanSampleId());
                if (!"0".equals(sample.getIsDestroy())) {
                    throw new ServiceException("已销毁的样品不能复检");
                }
                count = count + opTestResultInfoMapper.updateRetestFlagById(updateUser, s, "1");
            }
        }
        return count;
    }

    @Override
    @Transactional
    public int retestSingle(String infoId) {
        String updateUser = SecurityUtils.getUserId().toString();
        if (StringUtils.isEmpty(infoId)) {
            throw new RuntimeException("参数错误");
        }

        OpTestResultInfo info = opTestResultInfoMapper.selectOpTestResultInfoById(infoId);
        if (info == null) {
            throw new ServiceException("检测详情不存在");
        }

        // 校验该单据下的样品是否已销毁
        OpSamplingPlanSample sample = opSamplingPlanSampleMapper.
                selectOpSamplingPlanSampleByOpSamplingPlanSampleId(info.getPlanSampleId());
        if (sample != null && !"0".equals(sample.getIsDestroy())) {
            throw new ServiceException("样品[" + info.getSampleNo() + "]已销毁，不能复检");
        }

        // 更新子项的复检标志为 2 (复检待审核)
        opTestResultInfoMapper.updateRetestFlagById(updateUser, infoId, "2");

        // 更新涉及到的父表状态为待审核 (3)
        OpTestResultBase base = new OpTestResultBase();
        base.setId(info.getBaseId());
        base.setStatus("3"); // 待审核
        base.fillUpdateInfo();
        opTestResultBaseMapper.updateOpTestResultBase(base);

        return 1;
    }

    @Override
    public List<OpTestResultBase> selectRetestPendingList(OpTestResultBase opTestResultBase) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            Long deptId = SecurityUtils.getDeptId();
            opTestResultBase.setDeptId(deptId.toString());
        }
        return opTestResultBaseMapper.selectRetestPendingList(opTestResultBase);
    }

    @Override
    @Transactional
    public int approveRetestSingle(String id) {
        OpTestResultBase base = opTestResultBaseMapper.selectOpTestResultBaseById(id);
        if (base == null) {
            throw new ServiceException("化验单不存在");
        }
        if (!"3".equals(base.getStatus())) {
            throw new ServiceException("当前化验单状态不允许复检审核");
        }

        base.fillUpdateInfo();
        base.setStatus("1"); // 审核通过后进入待化验
        base.setExamineTime(new java.util.Date());
        base.setExamineUserId(SecurityUtils.getUserId().toString());
        base.setExamineUser(SecurityUtils.getLoginUser().getUser().getNickName());
        
        List<OpTestResultInfo> infoList = opTestResultInfoMapper.selectOpTestResultInfoByBaseId(id);
        if (infoList != null && !infoList.isEmpty()) {
            for (OpTestResultInfo info : infoList) {
                if ("2".equals(info.getRetestFlag())) {
                    // 作废之前生成的不合格处理单
                    opSampleUnqualityService.deleteOpSampleUnqualityBySourceId(info.getPlanSampleId());
                    // 清空子表结果
                    opTestResultInfoMapper.clearResultById(info.getId());
                    // 设置复检标志为 1 (复检中)
                    opTestResultInfoMapper.updateRetestFlag(info.getId(), "1");
                    // 更新项目表检测人、结果为空
                    opSamplingPlanItemMapper.updateTestToNullById(info.getPlanItemId(), SecurityUtils.getUserId().toString());
                }
            }
        }
        
        return opTestResultBaseMapper.updateOpTestResultBase(base);
    }

    @Override
    public List<OpSamplingPlanSample> selectUnPushSapList(OpSamplingPlanSample opSamplingPlanSample) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            Long deptId = SecurityUtils.getDeptId();
            opSamplingPlanSample.setDeptId(deptId.toString());
        }

        return opSamplingPlanSampleMapper.selectUnPushSapList(opSamplingPlanSample);
    }

    @Override
    public int pushSap(OpSamplingPlanSamplePushSapDTO opSamplingPlanSample) {
        // 根据id查询数据
        OpSamplingPlanSample sample = opSamplingPlanSampleMapper.
                selectOpSamplingPlanSampleByOpSamplingPlanSampleId(opSamplingPlanSample.getOpSamplingPlanSampleId());

        if (StringUtils.isEmpty(sample.getIsMainSample()) || "0".equals(sample.getIsMainSample())) {

            // 根据取样id查询化验结果信息表
            OpTestResultInfo queryInfo = new OpTestResultInfo();
            queryInfo.setPlanSampleId(opSamplingPlanSample.getOpSamplingPlanSampleId());
            List<OpTestResultInfo> opTestResultInfoList = opTestResultInfoMapper.selectOpTestResultInfoList(queryInfo);

            if (CollectionUtils.isEmpty(opTestResultInfoList)) {
                throw new ServiceException("获取化验结果失败，请联系管理员");
            }

            String deptId = SecurityUtils.getDeptId().toString();

            if (StringUtils.isEmpty(deptId)) {
                throw new ServiceException("获取部门失败，请联系管理员");
            }

            // 查询部门sap编码
            SysDept dept = sysDeptMapper.selectDeptById(Long.parseLong(deptId));
            if (dept == null) {
                throw new ServiceException("部门不存在，请联系管理员");
            }
            String sapCode = dept.getSapName();
            if (StringUtils.isEmpty(sapCode)) {
                throw new ServiceException("部门未配置SAP编码，请联系管理员");
            }

            // 推送SAP
            List<CheckResRetJudgeDTO> checkResRetJudgeDTOList = new ArrayList<>();

            // 如果已选择了批次则不再进行查询
            if (StringUtils.isEmpty(opSamplingPlanSample.getPRUEFLOS())) {
                throw new ServiceException("请先绑定批次");
            }

            CheckResRetJudgeDTO checkResRetJudgeDTO = new CheckResRetJudgeDTO();
            checkResRetJudgeDTO.setWERK(sapCode);
            checkResRetJudgeDTO.setPRUEFLOS(opSamplingPlanSample.getPRUEFLOS());
            checkResRetJudgeDTO.setVCODE(opSamplingPlanSample.getWhetherQualified());
            // 查询是否回传过检验批
            if (StringUtils.isNotEmpty(sample.getIsPushSap()) && "0".equals(sample.getIsPushSap())) {
                checkResRetJudgeDTO.setZTYPE("H");

                checkResRetJudgeDTOList.add(checkResRetJudgeDTO);
            }

            if (StringUtils.isNotEmpty(sample.getIsPushSap()) && "1".equals(sample.getIsPushSap())) {
                checkResRetJudgeDTO.setZTYPE("I");

                List<CheckResRetJudgeItemDTO> items = new ArrayList<>();

                // 整合参数
                for (OpTestResultInfo info : opTestResultInfoList) {
                    CheckResRetJudgeItemDTO checkResRetJudgeItemDTO = new CheckResRetJudgeItemDTO();
                    checkResRetJudgeItemDTO.setVERWMERKM(info.getItemName());
                    checkResRetJudgeItemDTO.setORIGINAL_INPUT(info.getResult());
                    items.add(checkResRetJudgeItemDTO);
                }
                checkResRetJudgeDTO.setITEMS(items);
                checkResRetJudgeDTOList.add(checkResRetJudgeDTO);
            }

            try {
                CheckResRetJudgeVO checkResRetJudgeVO = sapUtils.checkResRetJudge(checkResRetJudgeDTOList);

                if (StringUtils.isNotEmpty(checkResRetJudgeVO.getReturnInfo().getType())
                        && "S".equals(checkResRetJudgeVO.getReturnInfo().getType())
                        && "0".equals(sample.getIsPushSap())) {
                    opSamplingPlanSample.setIsPushSap("1");
                }

                if (StringUtils.isNotEmpty(checkResRetJudgeVO.getReturnInfo().getType())
                        && "S".equals(checkResRetJudgeVO.getReturnInfo().getType())
                        && "1".equals(sample.getIsPushSap())) {
                    opSamplingPlanSample.setIsPushSap("2");
                }
            } catch (Exception e) {
                log.error("检验批结果回传推送失败", e);
            }
        }

        return opSamplingPlanSampleMapper.updateOpSamplingPlanSampleDTO(opSamplingPlanSample);
    }

    @Override
    public int judgePass(OpSamplingPlanSample opSamplingPlanSample) {
        // 根据id查询数据
        OpSamplingPlanSample sample = opSamplingPlanSampleMapper.
                selectOpSamplingPlanSampleByOpSamplingPlanSampleId(opSamplingPlanSample.getOpSamplingPlanSampleId());

        if (sample == null) {
            throw new ServiceException("样品不存在，请联系管理员");
        }

        if (StringUtils.isNotEmpty(sample.getWhetherQualified())) {
            throw new ServiceException("样品合格已判定");
        }

        return opSamplingPlanSampleMapper.updateOpSamplingPlanSample(opSamplingPlanSample);
    }

    @Override
    public SampleInfoVO sampleTestDetail(String opSamplingPlanSampleId) {
        // 查询样品表
        SampleInfoVO sampleInfoVO = opSamplingPlanSampleMapper.selectOpSamplingPlanSampleId(opSamplingPlanSampleId);
        // 查询检测项目表
        OpTestResultInfo query = new OpTestResultInfo();
        query.setPlanSampleId(opSamplingPlanSampleId);
        List<OpTestResultInfo> opTestResultInfoList = opTestResultInfoMapper.selectOpTestResultInfoList(query);
        sampleInfoVO.setOpTestResultInfoList(opTestResultInfoList);
        return sampleInfoVO;
    }

    @Override
    public int autoBindBatch(String opSamplingPlanSampleId) {
        // 查询数据
        // 查询样品表
        OpSamplingPlanSample opSamplingPlanSample = opSamplingPlanSampleMapper.
                selectOpSamplingPlanSampleByOpSamplingPlanSampleId(opSamplingPlanSampleId);
        if (opSamplingPlanSample == null) {
            throw new ServiceException("样品不存在，请联系管理员");
        }
        // 根据签到id和物料查询地磅是否有物料凭证
        // 获取签到id-根据取样id查询取样主表
        OpSamplingPlan samplingPlan = opSamplingPlanMapper.selectOpSamplingPlanByOpSamplingPlanId(opSamplingPlanSample.getSamplingPlanId());

        BsWeighBridge weighBridge = bsWeighBridgeMapper.selectBsWeighBridgeBySignIdAndMaterialVoucher(
                samplingPlan.getSignInId(),
                opSamplingPlanSample.getInvbillCode());

        if (weighBridge == null) {
            throw new ServiceException("地磅未上传物料凭证，请手动查询检验批次并选择");
        }

        // 查询检验批次
        List<InboundInspectionDTO> inboundInspectionDTOList = new ArrayList<>();
        InboundInspectionDTO inboundInspectionDTO = new InboundInspectionDTO();
        inboundInspectionDTO.setWERK(SecurityUtils.getSapCode());
        inboundInspectionDTO.setMBLNR(weighBridge.getMaterialVoucher());
        inboundInspectionDTOList.add(inboundInspectionDTO);

        List<InboundInspectionDetailVO> inboundInspectionDetailVOS = sapUtils.queryInboundInspection(inboundInspectionDTOList);

        if (CollectionUtils.isEmpty(inboundInspectionDetailVOS)) {
            throw new ServiceException("地磅未上传检验批次，请手动查询检验批次并选择");
        }

        opSamplingPlanSample.setPRUEFLOS(inboundInspectionDetailVOS.get(0).getPRUEFLOS());

        return opSamplingPlanSampleMapper.updateOpSamplingPlanSample(opSamplingPlanSample);
    }

    @Override
    public int updateSampleInfo(OpSamplingPlanSample opSamplingPlanSample) {
        return opSamplingPlanSampleMapper.updateOpSamplingPlanSample(opSamplingPlanSample);
    }

    @Override
    public SamplingPlanReportVO printReport(String opSamplingPlanSampleId) {
        // 查询数据库获取扁平数据
        List<SamplingPlanItemVO> items = opTestResultBaseMapper.selectReportBySampleId(opSamplingPlanSampleId);

        if (items.isEmpty()) {
            return null;
        }

        // 转换为结构化的报表DTO
        return convertToReportDTO(items);
    }

    /**
     * 将扁平数据转换为结构化的报表DTO
     */
    private SamplingPlanReportVO convertToReportDTO(List<SamplingPlanItemVO> items) {
        // 取第一个项目获取样品基本信息
        SamplingPlanItemVO firstItem = items.get(0);

        SamplingPlanReportVO reportDTO = new SamplingPlanReportVO();
        reportDTO.setOpSamplingPlanSampleId(firstItem.getOpSamplingPlanSampleId());
        reportDTO.setInvbillCode(firstItem.getInvbillCode());
        reportDTO.setInvbillName(firstItem.getInvbillName());
        reportDTO.setTestTime(firstItem.getTestTime());
        reportDTO.setStatus(firstItem.getStatus());
        reportDTO.setProductionDate(firstItem.getProductionDate());
        reportDTO.setDeptId(firstItem.getDeptId());

        // 设置项目列表
        List<SamplingPlanReportVO.ReportItemDTO> itemDTOs = items.stream()
                .map(this::convertToReportItemDTO)
                .collect(Collectors.toList());
        reportDTO.setItems(itemDTOs);

        return reportDTO;
    }

    /**
     * 转换为报表项目DTO
     */
    private SamplingPlanReportVO.ReportItemDTO convertToReportItemDTO(SamplingPlanItemVO item) {
        SamplingPlanReportVO.ReportItemDTO itemDTO = new SamplingPlanReportVO.ReportItemDTO();
        itemDTO.setItemId(item.getItemId());
        itemDTO.setItemCode(item.getItemCode());
        itemDTO.setItemName(item.getItemName());
        itemDTO.setResult(item.getResult());
        itemDTO.setCheckResult(item.getCheckResult());

        // 设置特性
        SamplingPlanReportVO.ReportFeatureDTO featureDTO = new SamplingPlanReportVO.ReportFeatureDTO();
        featureDTO.setName(item.getFeatureName());
        featureDTO.setInfo(item.getFeatureInfo());
        featureDTO.setUpperLimit(item.getUpperLimit());
        featureDTO.setLowerLimit(item.getLowerLimit());
        featureDTO.setUnitOfMeasurement(item.getUnitOfMeasurement());
        featureDTO.setQualitativeOrQuantitative(item.getQualitativeOrQuantitative());
        featureDTO.setQualitativeType(item.getQualitativeType());
        featureDTO.setStandardValue(item.getStandardValue());
        featureDTO.setJudgeValue(item.getJudgeValue());

        itemDTO.setFeature(featureDTO);
        return itemDTO;
    }

    /**
     * (修改) 从待办任务开始化验
     *
     * @param opSamplingPlanItemIds 样品-项目ID列表
     */
    @Override
    @Transactional
    public String startTest(List<String> opSamplingPlanItemIds) throws Exception {
        if (CollectionUtils.isEmpty(opSamplingPlanItemIds)) {
            throw new ServiceException("请选择要化验的任务");
        }

        List<OpSamplingPlanItem> items = opSamplingPlanItemMapper.selectOpSamplingPlanItemListByIds(opSamplingPlanItemIds);
        if (items.size() != opSamplingPlanItemIds.size()) {
            throw new ServiceException("选择的任务数据异常，请刷新重试");
        }

        String firstItemId = items.get(0).getItemId();
        String firstItemName = items.get(0).getItemName();
        for (OpSamplingPlanItem item : items) {
            if (!firstItemId.equals(item.getItemId())) {
                throw new ServiceException("批量开始化验必须选择相同的检测项目");
            }
        }

        List<String> sampleIds = items.stream()
                .map(OpSamplingPlanItem::getSamplingPlanSampleId)
                .distinct()
                .collect(Collectors.toList());
        List<OpSamplingPlanSample> samples = opSamplingPlanSampleMapper.selectOpSamplingPlanSampleListByIds(sampleIds);
        Map<String, OpSamplingPlanSample> sampleMap = samples.stream()
                .collect(Collectors.toMap(OpSamplingPlanSample::getOpSamplingPlanSampleId, s -> s));
        String deptId = SecurityUtils.getDeptId().toString();
        OpTestResultBase base = new OpTestResultBase();
        base.setId(IdUtils.simpleUUID());
        base.fillCreateInfo();
        base.setDeptId(deptId);
        base.setResultNo(codeGeneratorUtil.generateHydAssayOrderNo(deptId)); // 临时单号
        base.setItemName(firstItemName);
        base.setItemId(firstItemId);
        base.setTestUser(SecurityUtils.getLoginUser().getUser().getNickName());
        base.setTestUserId(SecurityUtils.getUserId().toString());
        base.setTestTime(new java.util.Date());
        base.setResultAddType("1");
        base.setStatus("2"); // 状态：待提交
        opTestResultBaseMapper.insertOpTestResultBase(base);

        for (OpSamplingPlanItem item : items) {
            OpSamplingPlanSample sample = sampleMap.get(item.getSamplingPlanSampleId());
            if (sample == null) {
                throw new ServiceException("数据关联失败：找不到样品 " + item.getSamplingPlanSampleId());
            }

            OpTestResultInfo info = new OpTestResultInfo();
            info.setId(IdUtils.simpleUUID());
            info.fillCreateInfo();
            info.setBaseId(base.getId());
            info.setDeptId(base.getDeptId());
            info.setSampleNo(sample.getSampleNo());
            info.setSampleName(sample.getInvbillName());
            info.setInvbillId(sample.getInvillId());
            info.setInvbillCode(sample.getInvbillCode());
            info.setInvbillName(sample.getInvbillName());
            info.setPlanId(sample.getSamplingPlanId());
            info.setPlanSampleId(sample.getOpSamplingPlanSampleId());
            info.setItemName(item.getItemName());
            info.setItemId(item.getItemId());
            info.setPlanItemId(item.getOpSamplingPlanItemId());
            info.setIsPushSap("0");
            info.setFeatureId(item.getFeatureId());
            info.setFeatureName(item.getFeatureName());
            info.setUpperLimit(item.getUpperLimit());
            info.setLowerLimit(item.getLowerLimit());
            info.setQualitativeOrQuantitative(item.getQualitativeOrQuantitative());


            opTestResultInfoMapper.insertOpTestResultInfo(info);
        }

        return base.getId();
    }

    /**
     * 新增样品化验 (修改: 插入子表)
     */
    @Override
    @Transactional
    public int insertOpTestResultBase(OpTestResultBase opTestResultBase) {
        if (StringUtils.isEmpty(opTestResultBase.getId())) {
            opTestResultBase.setId(IdUtils.simpleUUID());
        }

        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            opTestResultBase.setDeptId(loginUser.getDeptId().toString());
        }

        opTestResultBase.fillCreateInfo();

        // 插入子表
        insertOpTestResultInfo(opTestResultBase);

        return opTestResultBaseMapper.insertOpTestResultBase(opTestResultBase);
    }

    /**
     * 修改样品化验 (修改: 更新子表)
     */
    @Override
    @Transactional
    public int updateOpTestResultBase(OpTestResultBase opTestResultBase) {
        opTestResultBase.fillUpdateInfo();
        String updateBy = SecurityUtils.getUsername();

        // 1. 软删除旧的子表
        opTestResultInfoMapper.updateDeleteFlagByBaseId(opTestResultBase.getId(), updateBy);

        // 2. 插入新的子表
        insertOpTestResultInfo(opTestResultBase);

        // 3. 更新主表
        return opTestResultBaseMapper.updateOpTestResultBase(opTestResultBase);
    }

    /**
     * (新) 提交化验单
     */
    @Override
    @Transactional
    public int submitTestResult(OpTestResultBase opTestResultBase) {
        opTestResultBase.setStatus("3"); // 待审核
        opTestResultBase.setTestTime(new java.util.Date());
        opTestResultBase.setTestUserId(SecurityUtils.getUserId().toString());
        opTestResultBase.setTestUser(SecurityUtils.getLoginUser().getUser().getNickName());
        // 调用存储过程
//        springStoredProcCaller.callProTransCreateUnquality("HY", opTestResultBase.getId());
        return updateOpTestResultBase(opTestResultBase); // 复用更新逻辑
    }

    /**
     * (新) 审核通过
     */
    @Override
    @Transactional
    public int approveTestResult(String id) {
        OpTestResultBase base = opTestResultBaseMapper.selectOpTestResultBaseById(id);

        if ("4".equals(base.getStatus())) {
            throw new ServiceException("化验单已审核");
        }

        base.fillUpdateInfo();
        base.setStatus("4");  // 已审核
        base.setExamineTime(new java.util.Date());
        base.setExamineUserId(SecurityUtils.getUserId().toString());
        base.setExamineUser(SecurityUtils.getLoginUser().getUser().getNickName());
        base.setReturnReason(null);

        // 化验不合格处理
        if (!base.getOpTestResultInfoList().isEmpty()) {
            for (OpTestResultInfo info : base.getOpTestResultInfoList()) {
                if ("2".equals(info.getCheckResult())) {
                    handleUnquality(info);
                }
            }
        }

        // 复检
        List<OpTestResultInfo> opTestResultInfoList = base.getOpTestResultInfoList();
        if (opTestResultInfoList != null && !opTestResultInfoList.isEmpty()) {
            List<String> idList = opTestResultInfoList.stream()
                    .map(OpTestResultInfo::getId)
                    .toList();

            if (!idList.isEmpty()) {
                retest(idList);
            }
        }

        return opTestResultBaseMapper.updateOpTestResultBase(base);
    }

    private void handleUnquality(OpTestResultInfo info) {
        // 查询是否有签到id
        OpSamplingPlan opSamplingPlan = opSamplingPlanMapper.selectOpSamplingPlanByOpSamplingPlanId(info.getPlanId());
        // 查询样品子表数据
        List<OpSamplingPlanSample> sample = opSamplingPlanSampleMapper.selectOpSamplingPlanSampleListByPlanId(info.getPlanId());

        // 整合不合格数据
        OpSampleUnquality opSampleUnquality = new OpSampleUnquality();
        opSampleUnquality.setSourceId(info.getPlanSampleId());
        opSampleUnquality.setMaterialId(info.getInvbillId());
        opSampleUnquality.setMaterialCode(info.getInvbillCode());
        opSampleUnquality.setMaterialName(info.getInvbillName());
        opSampleUnquality.setIsDelete("0");
        opSampleUnquality.setTestUserId(String.valueOf(SecurityUtils.getUserId()));
        opSampleUnquality.setTestResult(info.getResult());
        opSampleUnquality.setCtype("化验不合格");
        opSampleUnquality.setProcessingType("1");
        opSampleUnquality.setStatus("0");
        opSampleUnquality.setCfilepath(info.getSjtFileUrl());
        if (sample != null) {
            if (!sample.isEmpty()) {
                OpSamplingPlanSample firstSample = sample.get(0);
                if (StringUtils.isNotEmpty(firstSample.getSupplierCode())) {
                    opSampleUnquality.setSupplierCode(firstSample.getSupplierCode());
                }
                if (StringUtils.isNotEmpty(firstSample.getSupplierName())) {
                    opSampleUnquality.setSupplierName(firstSample.getSupplierName());
                }
            }
        }
        if (StringUtils.isNotEmpty(opSamplingPlan.getDriverName())) {
            opSampleUnquality.setDriverName(opSamplingPlan.getDriverName());
        }
        if (StringUtils.isNotEmpty(opSamplingPlan.getDriverCode())) {
            opSampleUnquality.setDriverCode(opSamplingPlan.getDriverCode());
        }

        if (opSamplingPlan != null) {
            if (StringUtils.isNotEmpty(opSamplingPlan.getSignInId())) {
                opSampleUnquality.setSignInId(opSamplingPlan.getSignInId());
            }
        }

        // 新增子表
        List<OpSampleUnqualityDetail> list = new ArrayList<>();
        OpSampleUnqualityDetail opSampleUnqualityDetail = new OpSampleUnqualityDetail();
        opSampleUnquality.setOpSampleUnqualityId(IdUtils.simpleUUID());
        opSampleUnqualityDetail.setOpSampleUnqualityDetailId(IdUtils.simpleUUID());
        opSampleUnqualityDetail.setOpSampleUnqualityId(opSampleUnquality.getOpSampleUnqualityId());
        opSampleUnqualityDetail.setItemId(info.getItemId());
        opSampleUnqualityDetail.setItemName(info.getItemName());
        opSampleUnqualityDetail.setCtx(info.getFeatureName());
        opSampleUnqualityDetail.setCtestresult(info.getResult());
        opSampleUnqualityDetail.setChg(info.getCheckResult());

        list.add(opSampleUnqualityDetail);
        opSampleUnquality.setOpSampleUnqualityDetailList(list);
        opSampleUnqualityService.insertOpSampleUnquality(opSampleUnquality);
    }

    /**
     * (新) 审核退回
     */
    @Override
    public int rejectTestResult(String id, String reason) {
        OpTestResultBase base = opTestResultBaseMapper.selectOpTestResultBaseById(id);
        base.fillUpdateInfo();
        base.setStatus("2"); // 待提交
        base.setReturnReason(reason);
        return opTestResultBaseMapper.updateOpTestResultBase(base);
    }

    /**
     * (新) 取消审核
     */
    @Override
    public int cancelApprove(String id) {
        OpTestResultBase base = opTestResultBaseMapper.selectOpTestResultBaseById(id);
        base.fillUpdateInfo();
        base.setStatus("3"); // 待审核
        base.setExamineTime(null);
        base.setExamineUserId(null);
        base.setExamineUser(null);
        return opTestResultBaseMapper.updateOpTestResultBase(base);
    }

    /**
     * 插入子表信息 (内部方法)
     */
    private void insertOpTestResultInfo(OpTestResultBase opTestResultBase) {
        List<OpTestResultInfo> opTestResultInfoList = opTestResultBase.getOpTestResultInfoList();
        if (StringUtils.isNotEmpty(opTestResultInfoList)) {
            for (OpTestResultInfo info : opTestResultInfoList) {
                info.setId(IdUtils.simpleUUID());
                info.fillCreateInfo();
                info.setBaseId(opTestResultBase.getId());
                info.setDeptId(opTestResultBase.getDeptId());
                info.setIsPushSap("0"); // 默认不推送
                opTestResultInfoMapper.insertOpTestResultInfo(info);

                // 标记文件为永久
                if (sysUploadFileService != null) {
                    if (StringUtils.isNotEmpty(info.getSjtFileId())) {
                        sysUploadFileService.markFileAsPermanent(info.getSjtFileId());
                    }
                }
            }
        }
    }

    /**
     * 调用饲料进厂库存检测存储过程
     *
     * @param ccorpcode 部门编码（dept_id），空字符串表示不限
     * @param cinvname  物料名称，模糊查询（空字符串表示不限）
     * @param starttime 开始时间（存储过程暂未启用过滤，传空或具体日期字符串均可）
     * @param endtime   结束时间（同上）
     * @param type      取样类型（空字符串表示不限）
     * @return 检测结果列表
     */
    @Override
    public List<JckcTestVO> callProTransGetJCKCTest(GetJCKCTestDTO getJCKCTestDTO) {
        // 1. 参数清洗：确保 null 变为空字符串，确保存储过程逻辑生效
        String ccorpcode = StringUtils.nvl(getJCKCTestDTO.getCcorpcode(), "");
        String cinvname = StringUtils.nvl(getJCKCTestDTO.getCinvname(), "");
        String starttime = StringUtils.nvl(getJCKCTestDTO.getStarttime(), "");
        String endtime = StringUtils.nvl(getJCKCTestDTO.getEndtime(), "");
        String type = StringUtils.nvl(getJCKCTestDTO.getType(), "");

        // 2. 时间格式处理：截取为 YYYY-MM-DD，防止 MySQL 报 Data truncation 错误
        if (starttime.length() > 10) {
            starttime = starttime.substring(0, 10);
        }
        if (endtime.length() > 10) {
            endtime = endtime.substring(0, 10);
        }

        // 存储过程调用语法
        String procSql = "{call Pro_Trans_GetJCKCTest(?, ?, ?, ?, ?)}";

        // 3. 执行存储过程获取全量结果 (JdbcTemplate 不支持 PageHelper 自动分页)
        List<JckcTestVO> allList = jdbcTemplate.query(
                procSql,
                new Object[]{ccorpcode, cinvname, starttime, endtime, type},
                new RowMapper<JckcTestVO>() {
                    @Override
                    public JckcTestVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                        JckcTestVO dto = new JckcTestVO();
                        dto.setOp_sampling_plan_sample_id(rs.getString("op_sampling_plan_sample_id"));
                        dto.setSample_no(rs.getString("sample_no"));
                        dto.setSampling_type(rs.getString("sampling_type"));
                        dto.setInvbill_name(rs.getString("invbill_name"));
                        dto.setCar_in_time(rs.getString("car_in_time"));
                        dto.setDriver_code(rs.getString("driver_code"));
                        dto.setTest_time(rs.getString("test_time"));
                        dto.setIday(rs.getInt("iday"));
                        dto.setHqm(rs.getString("hqm"));
                        dto.setOts(rs.getString("ots"));
                        dto.setCmxt(rs.getString("cmxt"));
                        dto.setSjqa(rs.getString("sjqa"));
                        dto.setWhether_qualified(rs.getString("whether_qualified"));
                        dto.setQyr(rs.getString("qyr"));
                        dto.setJcr(rs.getString("jcr"));
                        return dto;
                    }
                }
        );

        // 4. 处理姓名转换：将取样人(qyr)和检测人(jcr)的 ID 转换为真实姓名
        if (!allList.isEmpty()) {
            Set<String> userIds = new HashSet<>();
            allList.forEach(item -> {
                if (StringUtils.isNotEmpty(item.getQyr())) userIds.add(item.getQyr());
                if (StringUtils.isNotEmpty(item.getJcr())) userIds.add(item.getJcr());
            });

            if (!userIds.isEmpty()) {
                Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);
                allList.forEach(item -> {
                    if (StringUtils.isNotEmpty(item.getQyr())) {
                        item.setQyr(usernameMap.getOrDefault(item.getQyr(), item.getQyr()));
                    }
                    if (StringUtils.isNotEmpty(item.getJcr())) {
                        item.setJcr(usernameMap.getOrDefault(item.getJcr(), item.getJcr()));
                    }
                });
            }
        }

        // 5. 手动实现内存分页
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            int total = allList.size();
            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);

            // 越界处理
            if (fromIndex > total) {
                fromIndex = total;
                toIndex = total;
            }

            List<JckcTestVO> pageList = allList.subList(fromIndex, toIndex);

            // 关键：必须封装成 Page 对象，否则 Controller 的 getDataTable 无法获取正确的 total
            Page<JckcTestVO> resultPage = new Page<>(pageNum, pageSize);
            resultPage.setTotal(total);
            resultPage.addAll(pageList);
            return resultPage;
        }

        return allList;
    }

    /**
     * 检测结果变更
     *
     * @param dto 变更信息
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeResult(ResultChangeSaveDTO dto) {

        // 1. 查询详细信息
        OpTestResultInfo opTestResultInfo = opTestResultInfoMapper.selectOpTestResultInfoById(dto.getResultId());
        if (opTestResultInfo == null) {
            throw new BusinessException("检测结果不存在");
        }

        // 2. 保存原值（关键）
        String originResult = opTestResultInfo.getResult();
        String originCheckResult = opTestResultInfo.getCheckResult();

        // 3. 更新检测结果
        opTestResultInfo.setResult(dto.getResult());
        opTestResultInfo.setCheckResult(dto.getCheckResult());
        opTestResultInfo.fillUpdateInfo();
        int update = opTestResultInfoMapper.updateOpTestResultInfo(opTestResultInfo);

        // 化验不合格处理
        if ("2".equals(opTestResultInfo.getCheckResult())) {
            // 查询是否有签到id
            OpSamplingPlan opSamplingPlan = opSamplingPlanMapper.selectOpSamplingPlanByOpSamplingPlanId(opTestResultInfo.getPlanId());
            // 查询样品子表数据
            List<OpSamplingPlanSample> sample = opSamplingPlanSampleMapper.selectOpSamplingPlanSampleListByPlanId(opTestResultInfo.getPlanId());

            // 整合不合格数据
            OpSampleUnquality opSampleUnquality = new OpSampleUnquality();
            opSampleUnquality.setSourceId(opTestResultInfo.getPlanSampleId());
            opSampleUnquality.setMaterialId(opTestResultInfo.getInvbillId());
            opSampleUnquality.setMaterialCode(opTestResultInfo.getInvbillCode());
            opSampleUnquality.setMaterialName(opTestResultInfo.getInvbillName());
            opSampleUnquality.setIsDelete("0");
            opSampleUnquality.setTestUserId(String.valueOf(SecurityUtils.getUserId()));
            opSampleUnquality.setTestResult(opTestResultInfo.getResult());
            opSampleUnquality.setCtype("化验不合格");
            opSampleUnquality.setProcessingType("1");
            opSampleUnquality.setStatus("0");
            opSampleUnquality.setCfilepath(opTestResultInfo.getSjtFileUrl());
            if (sample != null) {
                if (!sample.isEmpty()) {
                    OpSamplingPlanSample firstSample = sample.get(0);
                    if (StringUtils.isNotEmpty(firstSample.getSupplierCode())) {
                        opSampleUnquality.setSupplierCode(firstSample.getSupplierCode());
                    }
                    if (StringUtils.isNotEmpty(firstSample.getSupplierName())) {
                        opSampleUnquality.setSupplierName(firstSample.getSupplierName());
                    }
                }
            }
            if (StringUtils.isNotEmpty(opSamplingPlan.getDriverName())) {
                opSampleUnquality.setDriverName(opSamplingPlan.getDriverName());
            }
            if (StringUtils.isNotEmpty(opSamplingPlan.getDriverCode())) {
                opSampleUnquality.setDriverCode(opSamplingPlan.getDriverCode());
            }

            if (opSamplingPlan != null) {
                if (StringUtils.isNotEmpty(opSamplingPlan.getSignInId())) {
                    opSampleUnquality.setSignInId(opSamplingPlan.getSignInId());
                }
            }

            // 新增子表
            List<OpSampleUnqualityDetail> list = new ArrayList<>();
            OpSampleUnqualityDetail opSampleUnqualityDetail = new OpSampleUnqualityDetail();
            opSampleUnquality.setOpSampleUnqualityId(IdUtils.simpleUUID());
            opSampleUnqualityDetail.setOpSampleUnqualityDetailId(IdUtils.simpleUUID());
            opSampleUnqualityDetail.setOpSampleUnqualityId(opSampleUnquality.getOpSampleUnqualityId());
            opSampleUnqualityDetail.setItemId(opTestResultInfo.getItemId());
            opSampleUnqualityDetail.setItemName(opTestResultInfo.getItemName());
            opSampleUnqualityDetail.setCtx(opTestResultInfo.getFeatureName());
            opSampleUnqualityDetail.setCtestresult(opTestResultInfo.getResult());
            opSampleUnqualityDetail.setChg(opTestResultInfo.getCheckResult());

            list.add(opSampleUnqualityDetail);
            opSampleUnquality.setOpSampleUnqualityDetailList(list);
            opSampleUnqualityService.insertOpSampleUnquality(opSampleUnquality);
        }

        // 4. 写变更日志（用原值）
        OpTestResultChangeLog log = OpTestResultChangeLog.builder()
                .resultId(dto.getResultId())
                .originResult(originResult)
                .originCheckResult(originCheckResult)
                .newResult(dto.getResult())
                .newCheckResult(dto.getCheckResult())
                .changeReason(dto.getChangeReason())
                .build();
        log.fillCreateInfo();
        opTestResultChangeLogService.save(log);

        return update;
    }

    @Override
    @Transactional
    public int approveTest(OpTestResultBase opTestResultBase) {
        if ("4".equals(opTestResultBase.getStatus())) {
            throw new ServiceException("化验单已审核");
        }

        opTestResultBase.fillUpdateInfo();
        opTestResultBase.setStatus("4");  // 已审核
        opTestResultBase.setExamineTime(new java.util.Date());
        opTestResultBase.setExamineUserId(SecurityUtils.getUserId().toString());
        opTestResultBase.setExamineUser(SecurityUtils.getLoginUser().getUser().getNickName());
        opTestResultBase.setReturnReason(null);

        // 化验不合格处理
        if (!opTestResultBase.getOpTestResultInfoList().isEmpty()) {
            for (OpTestResultInfo info : opTestResultBase.getOpTestResultInfoList()) {
                if ("2".equals(info.getCheckResult())) {
                    handleUnquality(info);
                }
            }
        }

        // 复检 (原本就有这段逻辑)
        List<OpTestResultInfo> opTestResultInfoList = opTestResultBase.getOpTestResultInfoList();
        if (opTestResultInfoList != null && !opTestResultInfoList.isEmpty()) {
            List<String> idList = opTestResultInfoList.stream()
                    .map(OpTestResultInfo::getId)
                    .toList();

            if (!idList.isEmpty()) {
                retest(idList);
            }
        }
        return updateOpTestResultBase(opTestResultBase);
    }

    @Override
    public int jczxAdd(OpSamplingPlanSample opSamplingPlanSample) {
        String sampleNo = opSamplingPlanSample.getSampleNo();
        OpTestResultBase base = new OpTestResultBase();
        insertOpTestResultBase(base);
        return 0;
    }
}
