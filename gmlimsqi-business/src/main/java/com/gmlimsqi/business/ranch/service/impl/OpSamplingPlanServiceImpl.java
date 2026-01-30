package com.gmlimsqi.business.ranch.service.impl;


import com.gmlimsqi.business.basicdata.domain.BsInvbillItemStandard;
import com.gmlimsqi.business.basicdata.mapper.BsInvbillItemStandardMapper;
import com.gmlimsqi.business.basicdata.service.IBsInvbillItemStandardService;
import com.gmlimsqi.business.basicdata.vo.BsInvbillItemStandardVo;
import com.gmlimsqi.business.disinfectionmanagement.domain.OpDisinfectionManagement;
import com.gmlimsqi.business.disinfectionmanagement.service.IOpDisinfectionManagementService;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlan;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlanItem;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;
import com.gmlimsqi.business.ranch.dto.SignInNotificationDTO;
import com.gmlimsqi.business.ranch.mapper.OpSamplingPlanItemMapper;
import com.gmlimsqi.business.ranch.mapper.OpSamplingPlanMapper;
import com.gmlimsqi.business.ranch.mapper.OpSamplingPlanSampleMapper;
import com.gmlimsqi.business.ranch.service.IOpSamplingPlanService;
import com.gmlimsqi.business.ranch.vo.OpSamplingPlanSampleMonitorVO;
import com.gmlimsqi.business.ranch.vo.QRCodeResult;
import com.gmlimsqi.business.ranch.vo.SamplingReceiveListVo;
import com.gmlimsqi.business.samplingplan.mapper.OpFinishedProductSamplingPlanMapper;
import com.gmlimsqi.business.samplingplan.pojo.entity.OpFinishedProductSamplingPlan;
import com.gmlimsqi.business.unquality.domain.OpSampleUnquality;
import com.gmlimsqi.business.unquality.mapper.OpSampleUnqualityMapper;
import com.gmlimsqi.business.unquality.service.IOpSampleUnqualityService;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.SpringStoredProcCaller;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.core.domain.entity.SysDept;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.enums.SamplePlanStatusEnum;
import com.gmlimsqi.common.enums.YesNo2Enum;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.CollectionUtils;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.domain.SysUploadFile;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import com.gmlimsqi.system.mapper.SysUploadFileMapper;
import com.gmlimsqi.system.mapper.SysUserMapper;
import com.gmlimsqi.system.service.ISysUploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.gmlimsqi.business.util.CodeGeneratorUtil.*;

/**
 * 取样计划主Service业务层处理
 *
 * @author hhy
 * @date 2025-11-04
 */
@Service
public class OpSamplingPlanServiceImpl implements IOpSamplingPlanService {
    @Autowired
    private OpSamplingPlanMapper opSamplingPlanMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private OpSamplingPlanSampleMapper opSamplingPlanSampleMapper;
    @Autowired
    private OpSamplingPlanItemMapper opSamplingPlanItemMapper;
    /*@Autowired
    private OpSampleUnqualityMapper opSampleUnqualityMapper;*/
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;
    @Autowired
    private OpSampleUnqualityMapper opSampleUnqualityMapper;
    @Autowired
    private SysUploadFileMapper sysUploadFileMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private ISysUploadFileService sysUploadFileService;

    @Autowired
    private SpringStoredProcCaller springStoredProcCaller;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private OpFinishedProductSamplingPlanMapper opFinishedProductSamplingPlanMapper;

    @Autowired
    private IOpSampleUnqualityService opSampleUnqualityService;

    @Autowired
    private IOpDisinfectionManagementService opDisinfectionManagementService;

    @Autowired
    private BsInvbillItemStandardMapper bsInvbillItemStandardMapper;

    @Autowired
    private IBsInvbillItemStandardService iBsInvbillItemStandardService;


    private static final Logger logger = LoggerFactory.getLogger(OpSamplingPlanServiceImpl.class);

    /**
     * 根据取样方案 ID 查询取样方案详情
     * 包含：
     * 1. 取样方案基本信息
     * 2. 车辆附件 URL 处理
     * 3. 取样单列表
     * 4. 每个取样单的检测项目、附件信息
     *
     * @param opSamplingPlanId 取样方案主键 ID
     * @return 取样方案完整对象
     */
    @Override
    public OpSamplingPlan selectOpSamplingPlanByOpSamplingPlanId(String opSamplingPlanId) {
        /* ======================= 1. 参数校验 ======================= */
        // 判断取样方案 ID 是否为空
        if (ObjectUtils.isEmpty(opSamplingPlanId)) {
            // 参数缺失，直接抛出业务异常
            throw new BusinessException("缺少参数，请联系管理员");
        }

        /* ======================= 2. 查询取样方案主表 ======================= */
        // 根据取样方案 ID 查询方案基本信息
        OpSamplingPlan samplingPlan =
                opSamplingPlanMapper.selectOpSamplingPlanByOpSamplingPlanId(opSamplingPlanId);
        if (ObjectUtils.isEmpty(samplingPlan)){
            throw new RuntimeException("参数错误,请联系管理员");
        }

        /* ======================= 3. 处理车辆附件（carFileId → URL） ======================= */
        // 判断车辆附件 ID 是否为空（多个 ID 以逗号分隔）
        if (StringUtils.isNotEmpty(samplingPlan.getCarFileId())) {
            // 将逗号分隔的文件 ID 字符串拆分成 List
            List<String> carFileIds =
                    Arrays.asList(samplingPlan.getCarFileId().split(","));
            // 确保附件 ID 集合不为空
            if (!carFileIds.isEmpty()) {
                // 用于拼接附件 URL 的 StringBuilder
                StringBuilder carFileUrlBuilder = new StringBuilder();
                // 遍历每一个附件 ID
                for (String carFileId : carFileIds) {
                    // 根据附件 ID 查询附件信息
                    SysUploadFile sysUploadFile =
                            sysUploadFileMapper.selectFileById(carFileId);
                    // 拼接附件访问 URL，并以逗号分隔
                    carFileUrlBuilder.append(sysUploadFile.getUrl()).append(",");
                }
                // 去掉最后一个多余的逗号，并设置到方案对象中
                samplingPlan.setCarFileUrl(
                        carFileUrlBuilder.toString()
                                .substring(0, carFileUrlBuilder.length() - 1)
                );
            }
        }

        /* ======================= 4. 查询取样单列表 ======================= */
        // 根据取样方案 ID 查询对应的取样单列表
        List<OpSamplingPlanSample> opSamplingPlanSamples =
                opSamplingPlanSampleMapper
                        .selectOpSamplingPlanSampleListByPlanId(opSamplingPlanId);
        // 判断取样单列表是否为空
        if (!ObjectUtils.isEmpty(opSamplingPlanSamples)) {
            // 遍历每一个取样单
            for (OpSamplingPlanSample sample : opSamplingPlanSamples) {
                /* ---------- 4.1 查询取样单对应的检测项目 ---------- */
                // 根据取样单 ID 查询检测项目列表
                List<OpSamplingPlanItem> items =
                        opSamplingPlanItemMapper
                                .selectOpSamplingPlanItemBySampleId(
                                        sample.getOpSamplingPlanSampleId()
                                );

                // 将检测项目列表设置到取样单对象中
                sample.setOpSamplingPlanItemList(items);
                /* ---------- 4.2 查询取样单附件（sourceId 关联） ---------- */
                // 根据取样单 ID 查询附件 ID（逗号分隔）
                String fileIds =
                        sysUploadFileMapper
                                .selectFileBySourceId(
                                        sample.getOpSamplingPlanSampleId()
                                );
                // 判断附件 ID 是否为空
                if (StringUtils.isNotEmpty(fileIds)) {
                    // 设置附件 URL 列表
                    sample.setGgQualityFileUrls(
                            sysUploadFileMapper
                                    .selectFileUrlBySourceId(
                                            sample.getOpSamplingPlanSampleId()
                                    )
                    );
                    // 设置附件 ID 列表
                    sample.setGgQualityFileIds(fileIds);
                }
                /* ---------- 4.3 查询取样单详情（补充字段） ---------- */
                // 再次根据取样单 ID 查询完整取样单信息
                OpSamplingPlanSample samplingPlanSample =
                        opSamplingPlanSampleMapper
                                .selectOpSamplingPlanSampleByOpSamplingPlanSampleId(
                                        sample.getOpSamplingPlanSampleId()
                                );

                /* ---------- 4.4 处理取样单质量附件 ID → URL ---------- */
                // 判断质量附件 ID 是否为空
                if (!StringUtils.isEmpty(samplingPlanSample.getGgQualityFileIds())) {
                    // 将逗号分隔的附件 ID 拆分为 List
                    List<String> carFileIds =
                            Arrays.asList(
                                    sample.getGgQualityFileIds().split(",")
                            );
                    // 用于拼接质量附件 URL
                    StringBuilder carFileUrlBuilder = new StringBuilder();
                    // 遍历每一个质量附件 ID
                    for (String carFileId : carFileIds) {
                        // 根据附件 ID 查询附件信息
                        SysUploadFile sysUploadFile =
                                sysUploadFileMapper.selectFileById(carFileId);
                        // 拼接附件 URL
                        carFileUrlBuilder.append(sysUploadFile.getUrl()).append(",");
                    }
                    // 去掉最后一个逗号，并设置到取样单对象中
                    sample.setGgQualityFileUrls(
                            carFileUrlBuilder
                                    .substring(0, carFileUrlBuilder.length() - 1)
                    );
                }

                /* ---------- 4.5 不合格处理单（已注释逻辑） ---------- */
            /*
            String ggQualityResult = sample.getGgQualityResult();
            if (!StringUtils.isEmpty(ggQualityResult)) {
                if (sample.getGgQualityResult().equals(YesNo2Enum.NO.getCode())) {
                    OpSampleUnquality opSampleUnquality =
                            opSampleUnqualityMapper
                                    .selectOpSampleUnqualityBySourceId(
                                            sample.getOpSamplingPlanSampleId()
                                    );
                    sample.setDebitAmount(opSampleUnquality.getDebitAmount());
                    sample.setDebitWeight(opSampleUnquality.getDebitWeight());
                    sample.setDebitResult(opSampleUnquality.getDebitResult());
                    sample.setQualityDescribe(opSampleUnquality.getQualityDescribe());
                    sample.setQualityFileIds(opSampleUnquality.getQualityFileIds());
                }
            }
            */
            }
            // 将取样单列表设置到取样方案对象中
            samplingPlan.setOpSamplingPlanSampleList(opSamplingPlanSamples);
        }
        /* ---------- 4.6 补充最新的消毒信息 ---------- */
        if ("0".equals(samplingPlan.getStatus())) { // 只有待取样需要补充目前最新的消毒信息
            OpDisinfectionManagement dm = opDisinfectionManagementService.selectOnePassedByDeptId(SecurityUtils.getDeptId());
            if (dm == null) return samplingPlan;
            // 只有原来的值不存在才设置
            if (samplingPlan.getToxicide() == null) samplingPlan.setToxicide(dm.getDisinfectant()); // 消毒药
            if (samplingPlan.getDensity() == null) samplingPlan.setDensity(dm.getConcentration()); // 浓度
            if (samplingPlan.getDisinfection() == null)
                samplingPlan.setDisinfection(dm.getDisinfectionMethod()); // 消毒方式
        }

        /* ======================= 5. 返回结果 ======================= */
        // 返回包含所有子数据的取样方案对象
        return samplingPlan;
    }

    /**
     * 查询取样计划主列表
     *
     * @param opSamplingPlan 取样计划主
     * @return 取样计划主
     */
    @Override
    public List<OpSamplingPlan> selectOpSamplingPlanList(OpSamplingPlan opSamplingPlan) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            opSamplingPlan.setDeptId(String.valueOf(SecurityUtils.getDeptId()));
        }

        // 时间处理核心逻辑（直接操作Date字段）
        if (opSamplingPlan.getCreateStartTime() != null && opSamplingPlan.getCreateEndTime() != null) {
            // 1. 获取原始Date字段
            Date start = opSamplingPlan.getCreateStartTime();
            Date end = opSamplingPlan.getCreateEndTime();

            // 2. 判断是否为同一天
            if (isSameDay(start, end)) {
                // 3. 修正为当天00:00:00 和 23:59:59.999
                Date startOfDay = getStartOfDay(start);  // 当天0点
                Date endOfDay = getEndOfDay(end);        // 当天23点59分59秒

                // 4. 直接回写字段（重点：现在字段是Date类型，类型匹配）
                opSamplingPlan.setCreateStartTime(startOfDay);
                opSamplingPlan.setCreateEndTime(endOfDay);
            }
            // 非同一天：保留原时间，无需处理
        }

        List<OpSamplingPlan> items = opSamplingPlanMapper.selectOpSamplingPlanList(opSamplingPlan);

        // 批量处理用户名
        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpSamplingPlan::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo -> {
                String createById = vo.getCreateBy();
                if (createById != null) {
                    // 检查是否是其他系统推送的ID，如果是则统一返回"一卡通签到推送"
                    if (isSystemPushedId(createById)) {
                        vo.setCreateBy("一卡通签到推送");
                    } else {
                        // 正常查询用户，如果查询不到也返回"一卡通签到推送"
                        String username = usernameMap.get(createById);
                        vo.setCreateBy(username != null ? username : "一卡通签到推送");
                    }
                } else {
                    // 创建人ID为空时也返回"一卡通签到推送"
                    vo.setCreateBy("一卡通签到推送");
                }
            });

            for (OpSamplingPlan item : items) {
                String carFileUrl = "";
                // 查询随车检验报告
                if (!StringUtils.isEmpty(item.getCarFileId())) {
                    // 逗号分隔id
                    List<String> carFileIds = Arrays.asList(item.getCarFileId().split(","));
                    if (!StringUtils.isEmpty(carFileIds)) {
                        for (String carFileId : carFileIds) {
                            // 查询附件
                            SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(carFileId);
                            if (sysUploadFile != null && StringUtils.isNotEmpty(sysUploadFile.getUrl())) {
                                // 存在则逗号拼接url
                                if (StringUtils.isEmpty(carFileUrl)) {
                                    carFileUrl = sysUploadFile.getUrl();
                                } else {
                                    carFileUrl += "," + sysUploadFile.getUrl();
                                }
                            }
                        }
                    }
                }
                if (StringUtils.isNotEmpty(carFileUrl)) {
                    item.setCarFileUrl(carFileUrl);
                }
                /* ---------- 4.6 补充最新的消毒信息 ---------- */
                if ("0".equals(item.getStatus())) { // 只有待取样需要补充目前最新的消毒信息
                    OpDisinfectionManagement dm = opDisinfectionManagementService.selectOnePassedByDeptId(SecurityUtils.getDeptId());
                    if (dm == null) continue;
                    // 只有原来的值不存在才设置
                    if (item.getToxicide() == null) item.setToxicide(dm.getDisinfectant()); // 消毒药
                    if (item.getDensity() == null) item.setDensity(dm.getConcentration()); // 浓度
                    if (item.getDisinfection() == null)
                        item.setDisinfection(dm.getDisinfectionMethod()); // 消毒方式
                }
            }
        }

        return items;
    }

    /**
     * 判断是否是其他系统推送的ID
     * 可以根据具体规则判断，比如以特定前缀开头或包含特定字符串
     */
    private boolean isSystemPushedId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return false;
        }

        // 示例：判断是否为其他系统推送的ID
        // 您可以根据实际规则调整这里
        String[] systemIds = {
                "EgapSystemApi",  // 示例系统ID
                "SystemApi",      // 其他可能的系统ID
                "AutoPush"        // 自动推送的系统ID
        };

        for (String systemId : systemIds) {
            if (systemId.equalsIgnoreCase(userId)) {
                return true;
            }
        }

        // 或者根据ID的特征判断，比如包含特定关键词
        String lowerUserId = userId.toLowerCase();
        return lowerUserId.contains("system") ||
                lowerUserId.contains("api") ||
                lowerUserId.contains("push") ||
                lowerUserId.contains("auto");
    }

    /**
     * 新增取样计划主
     *
     * @param opSamplingPlan 取样计划主
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public String insertOpSamplingPlan(OpSamplingPlan opSamplingPlan) {
        // 判断是否有主表id
        if (!StringUtils.isEmpty(opSamplingPlan.getOpSamplingPlanId())
                && "3".equals(opSamplingPlan.getSamplingType())) {
            // 判断是否提交
            // 查询数据
            OpSamplingPlan samplingPlan = opSamplingPlanMapper.
                    selectOpSamplingPlanByOpSamplingPlanId(opSamplingPlan.getOpSamplingPlanId());
            if (!"0".equals(samplingPlan.getStatus())) {
                throw new RuntimeException("已提交的取样计划不能修改");
            }
        }
        List<String> oldSampleNos = new ArrayList<>();
        if (opSamplingPlan.getOpSamplingPlanId() != null) {
            List<OpSamplingPlanSample> opSamplingPlanSamples = opSamplingPlanSampleMapper.selectOpSamplingPlanSampleListByPlanId(opSamplingPlan.getOpSamplingPlanId());
            for (OpSamplingPlanSample oldSample : opSamplingPlanSamples) {
                if (StringUtils.isNotEmpty(oldSample.getSampleNo())) {
                    oldSampleNos.add(oldSample.getSampleNo());
                }
            }
        }
        int row = 0;
        // 手动新增样品计数
        int sampleCount = 0;

        // 新增
        String opSamplingPlanId = opSamplingPlan.getOpSamplingPlanId();
        // 判断 新增的类型 3 为原料类新增，有主表数据
        if (!StringUtils.isEmpty(opSamplingPlan.getSamplingType()) &&
                "3".equals(opSamplingPlan.getSamplingType())) {
            String status = opSamplingPlan.getStatus();
            if (ObjectUtils.isEmpty(status)) {
                throw new RuntimeException("缺少必要参数");
            }
            // 提交时，保存取样人、取样时间
            if (status.equals(SamplePlanStatusEnum.WSAPLE.getCode())) {
                opSamplingPlan.setSampleTime(DateUtils.getNowDate());
                opSamplingPlan.setSamplerId(String.valueOf(SecurityUtils.getUserId()));
                opSamplingPlan.setSamplerName(SecurityUtils.getLoginUser().getUser().getNickName());
            }

            if (StringUtils.isEmpty(opSamplingPlanId)) {

                opSamplingPlanId = IdUtils.simpleUUID();
                opSamplingPlan.setOpSamplingPlanId(opSamplingPlanId);
                opSamplingPlan.setSamplingPlanNo(codeGeneratorUtil.generateSAMPLEPlanNo());
                opSamplingPlan.setDeptId(String.valueOf(SecurityUtils.getDeptId()));
                // 填充消毒信息（只有传递过来的数据没有填写，才会覆盖最新的）
                OpDisinfectionManagement dm = opDisinfectionManagementService.selectOnePassedByDeptId(SecurityUtils.getDeptId());
                if (dm != null) {
                    if (StringUtils.isEmpty(opSamplingPlan.getToxicide())) opSamplingPlan.setToxicide(dm.getDisinfectant()); // 消毒药
                    if (StringUtils.isEmpty(opSamplingPlan.getDensity())) opSamplingPlan.setDensity(dm.getConcentration()); // 浓度
                    if (StringUtils.isEmpty(opSamplingPlan.getPersonLiable())) opSamplingPlan.setDisinfection(dm.getDisinfectionMethod()); // 消毒方式
                }
                // 自动填充创建/更新信息
                opSamplingPlan.fillCreateInfo();
                row = opSamplingPlanMapper.insertOpSamplingPlan(opSamplingPlan);
            } else {
                // 更新
                opSamplingPlan.fillUpdateInfo();
                row = opSamplingPlanMapper.updateOpSamplingPlan(opSamplingPlan);
                // 子表先删除
                opSamplingPlanMapper.updateSampleDeleteFlagByPlanId(String.valueOf(SecurityUtils.getUserId()), opSamplingPlanId);
                opSamplingPlanMapper.updateItemDeleteFlagByPlanId(String.valueOf(SecurityUtils.getUserId()), opSamplingPlanId);
            }
        }

        List<OpSamplingPlanSample> sampleList = opSamplingPlan.getOpSamplingPlanSampleList();
        if (!ObjectUtils.isEmpty(sampleList)) {
            List<String> sampleNos = new ArrayList<>();
            for (OpSamplingPlanSample sample : sampleList) {
                if (sampleNos.contains(sample.getSampleNo())) {
                    throw new RuntimeException(sample.getSampleNo() + "编号重复");
                }
                sampleNos.add(sample.getSampleNo());
            }
            for (OpSamplingPlanSample sample : sampleList) {
                // 非原料类的删除，再此新增
                if (!"3".equals(sample.getSamplingType())) {
                    opSamplingPlanSampleMapper.updateDeleteFlagById(sample.getOpSamplingPlanSampleId());
                }

                // 判断取样单号是否有值
                if (StringUtils.isEmpty(sample.getSampleNo())) {
                    try {
                        if ("0".equals(sample.getSamplingType())) {
                            String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_FINISHED_PRODUCT_SAMPLING_ORDER);
                            sample.setSampleNo(resultNo);
                        }
                        if ("1".equals(sample.getSamplingType())) {
                            String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_INVENTORY_SAMPLING_ORDER);
                            sample.setSampleNo(resultNo);
                        }
                        if ("2".equals(sample.getSamplingType())) {
                            String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_FEED_SAMPLING_ORDER);
                            sample.setSampleNo(resultNo);
                        }
                        if ("3".equals(sample.getSamplingType())) {
                            String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_YLQYD);
                            sample.setSampleNo(resultNo);
                        }

                    } catch (BusinessException e) {
                        throw new RuntimeException("生成装奶单编号失败: " + e.getMessage());
                    }
                } else {
                    int i = opSamplingPlanSampleMapper.checkSampleNoUnique(sample.getSampleNo());
                    if (!oldSampleNos.contains(sample.getSampleNo()) && i != 0) {
                        throw new RuntimeException("样品编号" + sample.getSampleNo() + "已存在");
                    }
                }

                // 如果主表id为空就表示为牧场，饲料厂采购，产品手动新增，没有主表数据，直接删除原数据再新增
                // 判断
                if (StringUtils.isEmpty(opSamplingPlan.getOpSamplingPlanId()) &&
                        StringUtils.isNotEmpty(sample.getOpSamplingPlanSampleId())) {
                    // 删除样品表
                    opSamplingPlanSampleMapper.updateDeleteFlagById(sample.getOpSamplingPlanSampleId());
                }

                if (!StringUtils.isEmpty(opSamplingPlan.getSupplierName())) {
                    sample.setSupplierId(opSamplingPlan.getSupplierId());
                    sample.setSupplierName(opSamplingPlan.getSupplierName());
                }

                if (!StringUtils.isEmpty(opSamplingPlan.getDriverName())) {
                    sample.setDriverName(opSamplingPlan.getDriverName());
                }

                if (opSamplingPlan.getCarInTime() != null) {
                    sample.setCarInTime(opSamplingPlan.getCarInTime());
                }

//                opSampleUnqualityMapper.updateDeleteFlagBySourceId(String.valueOf(SecurityUtils.getUserId()), sample.getOpSamplingPlanSampleId());
                sample.fillCreateInfo();
                String sampleId = IdUtils.simpleUUID();
                sample.setSamplingPlanId(opSamplingPlanId);
                sample.setOpSamplingPlanSampleId(sampleId);
                sample.setIsDelete(YesNo2Enum.NO.getCode());
                sample.setIsPushSap("0");
                sample.setIsDestroy("0");

                sample.setDeptId(String.valueOf(SecurityUtils.getDeptId()));

                // 处理检测项目
                List<OpSamplingPlanItem> itemList = sample.getOpSamplingPlanItemList();
                if (ObjectUtils.isEmpty(itemList)) {
                    throw new RuntimeException("未选择检验项目");
                } else {
                    for (OpSamplingPlanItem item : itemList) {
                        if (StringUtils.isEmpty(opSamplingPlanId)
                                && StringUtils.isNotEmpty(item.getOpSamplingPlanItemId())) {
                            // 删除化验表
                            opSamplingPlanItemMapper.updateDeleteFlagById(item.getOpSamplingPlanItemId());
                        }

                        item.fillCreateInfo();
                        item.setIsDelete("0");
                        item.setOpSamplingPlanItemId(IdUtils.simpleUUID());
                        item.setSamplingPlanSampleId(sampleId);
                    }
                }
                opSamplingPlanItemMapper.batchInsertOpSamplingPlanItem(itemList);


                sampleCount = opSamplingPlanSampleMapper.insertOpSamplingPlanSample(sample);

                // 调用存储过程
                /*if ("0".equals(sample.getGgQualityResult())){
                    // 调用存储过程
                    StoredProcedureVO gg = springStoredProcCaller.callProTransCreateUnquality("GG", sampleId);
                    if (gg != null) {
                        String code = gg.getCode();
                        String msg = gg.getMsg();

                        if (!"1".equals(code)) {
                            throw new ServiceException("生成不合格单失败：" + msg);
                        }
                    }
                }*/
                // 如果有不合格数据，发起不合格单,
                String ggQualityResult = sample.getGgQualityResult();
                if ("0".equals(ggQualityResult)) {
                    // 删除重新插入
                    opSampleUnqualityMapper.deleteOpSampleUnqualityBySourceId(sampleId, new Date(), String.valueOf(SecurityUtils.getUserId()));
                    // 整合感官不合格数据
                        OpSampleUnquality opSampleUnquality = new OpSampleUnquality();
                        opSampleUnquality.setSourceId(sampleId);
                        opSampleUnquality.setMaterialId(sample.getInvillId());
                        opSampleUnquality.setMaterialCode(sample.getInvbillCode());
                        opSampleUnquality.setMaterialName(sample.getInvbillName());
                        opSampleUnquality.setIsDelete("0");
                        opSampleUnquality.setTestUserId(String.valueOf(SecurityUtils.getUserId()));
                        opSampleUnquality.setTestResult(sample.getGgQualityResult());
                        opSampleUnquality.setCtype("感官不合格");
                        opSampleUnquality.setCunqualityinfo(sample.getGgQualityDescribe());
                        opSampleUnquality.setProcessingType("1");
                        opSampleUnquality.setStatus("0");
                        opSampleUnquality.setCfilepath(sample.getGgQualityFileIds());
                        if (StringUtils.isNotEmpty(sample.getSupplierCode())) {
                            opSampleUnquality.setSupplierCode(sample.getSupplierCode());
                        }
                        if (StringUtils.isNotEmpty(sample.getSupplierName())) {
                            opSampleUnquality.setSupplierName(sample.getSupplierName());
                        }
                        if (StringUtils.isNotEmpty(opSamplingPlan.getDriverName())) {
                            opSampleUnquality.setDriverName(opSamplingPlan.getDriverName());
                        }
                        if (StringUtils.isNotEmpty(opSamplingPlan.getDriverCode())) {
                            opSampleUnquality.setDriverCode(opSamplingPlan.getDriverCode());
                        }

                        if (StringUtils.isNotEmpty(opSamplingPlan.getSignInId())) {
                            opSampleUnquality.setSignInId(opSamplingPlan.getSignInId());
                        }

                        opSampleUnqualityService.insertOpSampleUnquality(opSampleUnquality);
                }
                if (!StringUtils.isEmpty(ggQualityResult)) {
                    // 处理文件
                    String ggQualityFileIds = sample.getGgQualityFileIds();
                    if (!ObjectUtils.isEmpty(ggQualityFileIds)) {
                        Arrays.stream(ggQualityFileIds.split(","))
                                .map(String::trim)
                                .forEach(fileId -> {
                                    // 来源表id赋值
                                    sysUploadFileMapper.updateByFileId(fileId);
                                });
                    }
                }
            }
//            sampleCount = opSamplingPlanSampleMapper.batchInsertOpSamplingPlanSample(sampleList);
        }
        if (row > 0) {
//            return row;
            return opSamplingPlanId;
        }
        if (sampleCount > 0) {
//            return sampleCount;
            return opSamplingPlanId;
        } else {
            throw new RuntimeException("新增取样计划主失败");
        }

    }

    /**
     * 修改取样计划主
     *
     * @param opSamplingPlan 取样计划主
     * @return 结果
     */
    @Override
    public int updateOpSamplingPlan(OpSamplingPlan opSamplingPlan) {
        // 自动填充更新信息
        opSamplingPlan.fillUpdateInfo();
        return opSamplingPlanMapper.updateOpSamplingPlan(opSamplingPlan);
    }

    /**
     * [API 专用] 处理 [一卡通系统] 接收到的司机签到通知
     *
     * @param notifyDTO 来自 egap 系统的通知数据
     */
    @Override
    @Transactional
    public void processSignInNotification(SignInNotificationDTO notifyDTO, String urlPrefix) {

        // 1. 创建一个新的 LIMS 取样计划实体
        OpSamplingPlan newPlan = new OpSamplingPlan();

        newPlan.setDriverName(notifyDTO.getDriverName());
        newPlan.setDriverPhone(notifyDTO.getDriverPhone());
        newPlan.setDriverCode(notifyDTO.getDriverCode());
        newPlan.setCarInTime(notifyDTO.getSignInTime());

        // --- 3. 填充 LIMS 系统自身的业务字段 ---

        // A. [!! 关键 !!] 根据 DTO 的 destinationCode 查询 LIMS 的 deptId
        String egapDeptCode = notifyDTO.getDestinationCode();
        if (StringUtils.isEmpty(egapDeptCode)) {
            logger.error("接收签到通知失败: egap 系统未提供 destinationCode");
            throw new ServiceException("处理失败: 目标部门编码(destinationCode)为空");
        }

        // 在 LIMS 系统中查找这个部门
        List<SysDept> limsDept = sysDeptMapper.selectBySapCode(egapDeptCode);

        if (CollectionUtils.isAnyEmpty(limsDept)) {
            logger.error("接收签到通知失败: 无法在 LIMS 中找到编码为 {} 的部门", egapDeptCode);
            throw new ServiceException("处理失败: 未能在 LIMS 中找到匹配的部门编码: " + egapDeptCode);
        }

        // 设置 LIMS 的部门 ID
        newPlan.setDeptId(limsDept.get(0).getDeptId().toString());

        // --- 处理Base64附件列表 ---
        List<String> fileIds = new ArrayList<>();
        List<String> fileUrls = new ArrayList<>();

        // 检查是否有Base64附件
        if (notifyDTO.getFileBase64() != null && !notifyDTO.getFileBase64().isEmpty()) {
            // 获取Base64列表
            List<String> base64List = notifyDTO.getFileBase64();

            // 先生成送检单号，用于文件名
            String samplingPlanNo = codeGeneratorUtil.generateSAMPLEPlanNo();
            String timestamp = DateUtils.dateTimeNow("yyyyMMddHHmmss");

            logger.info("开始处理Base64附件，数量: {}", base64List.size());

            // 遍历所有Base64文件
            for (int i = 0; i < base64List.size(); i++) {
                String fileBase64 = base64List.get(i);

                if (StringUtils.isNotEmpty(fileBase64)) {
                    try {
                        // 1. 生成统一的文件名
                        String originalFilename = generateUnifiedFilename(
                                samplingPlanNo,
                                timestamp,
                                i,
                                determineFileExtension(fileBase64)
                        );

                        logger.debug("开始上传第 {} 个附件，文件名: {}", i + 1, originalFilename);

                        // 2. 调用上传接口（调用base64上传方法）
                        SysUploadFile fileInfo = sysUploadFileService.uploadBase64File(
                                fileBase64,
                                originalFilename,
                                urlPrefix
                        );

                        // 3. 收集文件信息
                        fileIds.add(fileInfo.getFileId());
                        fileUrls.add(fileInfo.getUrl());

                        logger.info("成功上传第 {} 个附件: {}, 文件ID: {}",
                                i + 1, originalFilename, fileInfo.getFileId());

                    } catch (Exception e) {
                        logger.error("上传Base64文件失败，序号 {}: {}", i, e.getMessage(), e);
                        // 根据业务需求决定：继续处理其他文件 或 抛出异常
                        // 这里选择记录日志但继续处理其他文件，避免一个文件失败导致整个流程失败
                    }
                }
            }
        }

        // 将文件ID和URL用逗号分隔存储
        if (!fileIds.isEmpty()) {
            // 将List转换为逗号分隔的String
            String fileIdStr = String.join(",", fileIds);
            String fileUrlStr = String.join(",", fileUrls);

            // 存储到实体中
            newPlan.setCarFileId(fileIdStr);
            newPlan.setCarFileUrl(fileUrlStr);

            logger.info("保存附件信息 - 文件ID数量: {}, 文件URL数量: {}",
                    fileIds.size(), fileUrls.size());
            logger.debug("文件ID列表: {}", fileIdStr);
            logger.debug("文件URL列表: {}", fileUrlStr);
        } else {
            logger.info("没有附件需要保存");
        }

        // B. 设置主键
        newPlan.setOpSamplingPlanId(IdUtils.simpleUUID());

        // C. 生成唯一的"送检单号"
        newPlan.setSamplingPlanNo(codeGeneratorUtil.generateSAMPLEPlanNo());

        // D. 设置取样计划状态为 "待取样"
        newPlan.setStatus(SamplePlanStatusEnum.WSAPLE.getCode());

        // E. 设置其他默认值
        newPlan.setIsReceive(YesNo2Enum.NO.getCode());

        // --- 4. 关键: 手动填充审计字段 ---
        newPlan.setCreateTime(DateUtils.getNowDate());
        newPlan.setCreateBy("EgapSystemApi");
        newPlan.setSignInId(notifyDTO.getId());

        // --- 5. 插入数据库 ---
        int rows = opSamplingPlanMapper.insertOpSamplingPlan(newPlan);

        if (rows == 0) {
            throw new ServiceException("自动创建取样计划失败，数据库未受影响");
        }

        logger.info("已成功处理 egap 签到通知，并创建了 LIMS 取样计划: {}, 附件数量: {}",
                newPlan.getSamplingPlanNo(), fileIds.size());
    }

    /**
     * 生成统一的文件名
     */
    private String generateUnifiedFilename(String samplingPlanNo, String timestamp, int index, String fileExtension) {
        // 确保序号是两位数，保持文件名长度一致
        String formattedIndex = String.format("%02d", index);

        // 替换可能存在的非法字符
        String safeSamplingPlanNo = samplingPlanNo.replaceAll("[^a-zA-Z0-9]", "_");

        return String.format("%s_%s_%s.%s",
                safeSamplingPlanNo,
                timestamp,
                formattedIndex,
                fileExtension);
    }

    /**
     * 根据Base64字符串判断文件类型
     */
    private String determineFileExtension(String base64) {
        if (StringUtils.isEmpty(base64)) {
            return "jpg"; // 默认
        }

        // 检查Base64前缀来判断文件类型
        if (base64.startsWith("/9j/") || base64.contains("data:image/jpeg")) {
            return "jpg";
        } else if (base64.startsWith("iVBORw0KGgo") || base64.contains("data:image/png")) {
            return "png";
        } else if (base64.startsWith("R0lGOD") || base64.contains("data:image/gif")) {
            return "gif";
        } else if (base64.startsWith("JVBER") || base64.contains("data:application/pdf")) {
            return "pdf";
        } else {
            // 默认返回jpg
            return "jpg";
        }
    }

    /**
     * 批量删除取样计划主
     *
     * @param opSamplingPlanIds 需要删除的取样计划主主键
     * @return 结果
     */
    @Override
    public int deleteOpSamplingPlanByOpSamplingPlanIds(String[] opSamplingPlanIds) {
        return opSamplingPlanMapper.deleteOpSamplingPlanByOpSamplingPlanIds(opSamplingPlanIds);
    }

    /**
     * 删除取样计划主信息
     *
     * @param opSamplingPlanId 取样计划主主键
     * @return 结果
     */
    @Override
    public int deleteOpSamplingPlanByOpSamplingPlanId(String opSamplingPlanId) {
        return opSamplingPlanMapper.deleteOpSamplingPlanByOpSamplingPlanId(opSamplingPlanId);
    }

    @Override
    public List<SamplingReceiveListVo> selectOpSamplingPlanReceiveList(OpSamplingPlan opSamplingPlan) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            opSamplingPlan.setDeptId(String.valueOf(SecurityUtils.getDeptId()));
        }
        List<SamplingReceiveListVo> receiveListVos = opSamplingPlanMapper.selectOpSamplingPlanReceiveList(opSamplingPlan);

        if (receiveListVos != null && !receiveListVos.isEmpty()) {
            for (SamplingReceiveListVo samplingReceiveListVo : receiveListVos) {
                // 查询检测项目列表
                List<OpSamplingPlanItem> itemList = opSamplingPlanItemMapper.selectOpSamplingPlanItemBySampleId(samplingReceiveListVo.getSamplingPlanSampleId());
                samplingReceiveListVo.setOpSamplingPlanItemList(itemList);

                /** 如果取样人为空 那么把创建人设置为取样人 */
                if (StringUtils.isNotBlank(samplingReceiveListVo.getCreateBy())&&StringUtils.isEmpty(samplingReceiveListVo.getSamplerName())){
                    if (samplingReceiveListVo.getCreateBy().matches("^\\d+$")){
                        Long userId = Long.valueOf(samplingReceiveListVo.getCreateBy());
                        SysUser user = sysUserMapper.selectUserById(userId);
                        if (user != null) {
                            String nickName = user.getNickName(); // 获取用户姓名
                            samplingReceiveListVo.setSamplerName(nickName);
                        }
                    }else{
                        samplingReceiveListVo.setSamplerName(samplingReceiveListVo.getCreateBy());
                    }
                }
                if (samplingReceiveListVo.getSampleTime()==null&&samplingReceiveListVo.getCreateTime()!=null){
                    samplingReceiveListVo.setSampleTime(samplingReceiveListVo.getCreateTime());
                }
            }
        }
        return receiveListVos;
    }


    //查询物料已接受，不带项目
    @Override
    public List<SamplingReceiveListVo> selectOpSamplingPlanReceiveList2(OpSamplingPlan opSamplingPlan) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            opSamplingPlan.setDeptId(String.valueOf(SecurityUtils.getDeptId()));
        }
        List<SamplingReceiveListVo> receiveListVos = opSamplingPlanMapper.selectOpSamplingPlanReceiveList2(opSamplingPlan);
        return receiveListVos;
    }

    /**
     * 修改取样物料
     * @param opSamplingPlanSample
     * @return
     */
    @Override
    public int editOpSamplingPlanSampleMaterial(OpSamplingPlanSample opSamplingPlanSample) {
        // 查询物料项目标准
        BsInvbillItemStandard bsInvbillItemStandard = new BsInvbillItemStandard();
        bsInvbillItemStandard.setInvbillCode(opSamplingPlanSample.getInvbillCode());
        bsInvbillItemStandard.setDeptId(opSamplingPlanSample.getDeptId());
        List<BsInvbillItemStandardVo> standardList = iBsInvbillItemStandardService.selectListGroupByInvbill(bsInvbillItemStandard);
        if (standardList == null || standardList.isEmpty()) {
            throw new ServiceException("该物料没有配置检测项目标准,无法修改");
        }

        // 判断查询出来的检测项目是否在标准中
        if (opSamplingPlanSample.getOpSamplingPlanItemList() == null || opSamplingPlanSample.getOpSamplingPlanItemList().isEmpty()) {
            throw new ServiceException("获取检测项目失败");
        }

        for (OpSamplingPlanItem item : opSamplingPlanSample.getOpSamplingPlanItemList()) {
            if (!standardList.stream().anyMatch(standard ->
                    standard.getStandardList().stream().
                            anyMatch(std -> std.getItemId().equals(item.getItemId())))) {
                throw new ServiceException("该物料的检测项目" + item.getItemName() + "不在标准中,无法修改");
            }
        }

        // 删除该取样id的化验项目
        opSamplingPlanItemMapper.updateItemDeleteFlagByPlanId(opSamplingPlanSample.getOpSamplingPlanSampleId(), SecurityUtils.getUserId().toString());

        return opSamplingPlanSampleMapper.updateOpSamplingPlanSample(opSamplingPlanSample);
    }

    @Override
    public int editOpSamplingPlanSampleItem(OpSamplingPlanSample opSamplingPlanSample) {
        // 查询原检测项目
        List<OpSamplingPlanItem> oldItemList = opSamplingPlanItemMapper.selectOpSamplingPlanItemBySampleId(opSamplingPlanSample.getOpSamplingPlanSampleId());

        if (oldItemList != null && !oldItemList.isEmpty()) {
            // 删除原检测项目
            opSamplingPlanItemMapper.updateItemDeleteFlagByPlanId(opSamplingPlanSample.getOpSamplingPlanSampleId(), SecurityUtils.getUserId().toString());
        }

        // 处理检测项目
        List<OpSamplingPlanItem> itemList = opSamplingPlanSample.getOpSamplingPlanItemList();
        if (ObjectUtils.isEmpty(itemList)) {
            throw new RuntimeException("未选择检验项目");
        } else {
            for (OpSamplingPlanItem item : itemList) {
                if (StringUtils.isEmpty(opSamplingPlanSample.getOpSamplingPlanSampleId())
                        && StringUtils.isNotEmpty(item.getOpSamplingPlanItemId())) {
                    // 删除化验表
                    opSamplingPlanItemMapper.updateDeleteFlagById(item.getOpSamplingPlanItemId());
                }

                item.fillCreateInfo();
                item.setIsDelete("0");
                item.setOpSamplingPlanItemId(IdUtils.simpleUUID());
                item.setSamplingPlanSampleId(opSamplingPlanSample.getOpSamplingPlanSampleId());
            }
        }

        return opSamplingPlanItemMapper.batchInsertOpSamplingPlanItem(itemList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int receiveOpSamplingPlan(List<String> ids) {
        for (String id : ids) {
            // 查询是否有化验项目
            List<OpSamplingPlanItem> itemList = opSamplingPlanItemMapper.selectOpSamplingPlanItemBySampleId(id);
            if (itemList == null || itemList.isEmpty()) {
                throw new ServiceException("该取样计划没有化验项目,无法接收");
            }

            OpSamplingPlanSample sample = new OpSamplingPlanSample();
            sample.setOpSamplingPlanSampleId(id);
            sample.fillUpdateInfo();
            sample.setIsReceive("1");
            sample.setReceiverId(String.valueOf(SecurityUtils.getUserId()));
            sample.setReceiveTime(new Date());
            sample.setReceiverName(SecurityUtils.getLoginUser().getUser().getNickName());
            opSamplingPlanSampleMapper.updateOpSamplingPlanSample(sample);
        }

        return ids.size();
    }

    /**
     * 获取取样二维码
     */
    @Override
    public List<QRCodeResult> getSampleQRCode(String count) {
        try {
            int num = Integer.parseInt(count);
            List<QRCodeResult> qrCodes = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                String qrCode = codeGeneratorUtil.generateSamoleSLQRNo();
                QRCodeResult qrCodeResult = new QRCodeResult();
                qrCodeResult.setQrCode(qrCode);
                qrCodes.add(qrCodeResult);
            }
//            QRCodeResult qrCodeResult = new QRCodeResult();
//            qrCodeResult.setQrCode(String.join(",", qrCodes));
//            return qrCodeResult;
            return qrCodes;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("参数count必须是有效的数字");
        }
    }

    /**
     * 更新取样计划状态（用于 "取样完成" 和 "无需取样" 按钮）
     *
     * @param opSamplingPlanId 计划ID
     * @param status           新的状态值 ("1" 或 "2")
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStatus(String opSamplingPlanId, String status) {

        // 1. 校验
        if (StringUtils.isEmpty(opSamplingPlanId) || StringUtils.isEmpty(status)) {
            throw new ServiceException("关键参数缺失，无法更新状态");
        }

        // 2. 查询原始单据，确保它存在
        OpSamplingPlan plan = opSamplingPlanMapper.selectOpSamplingPlanByOpSamplingPlanId(opSamplingPlanId);
        if (plan == null) {
            throw new ServiceException("未找到该计划，请刷新后重试");
        }

        // (可选: 检查状态是否允许变更)
        // if(!plan.getStatus().equals("0")){ // 假设只有 "0" (草稿) 才能变更
        //     throw new ServiceException("只有未提交的计划才能操作");
        // }

        // 3. 创建一个只包含需要更新字段的实体
        OpSamplingPlan planToUpdate = new OpSamplingPlan();
        planToUpdate.setOpSamplingPlanId(opSamplingPlanId);
        planToUpdate.setStatus(status);

        // 4. [关键业务逻辑]
        // 复用你 "insertOpSamplingPlan" 方法中的逻辑：
        // 如果状态是 "取样完成" (status='1')，则记录取样人信息。
        // (我假设 '1' 对应 SamplePlanStatusEnum.YSAPLE.getCode())
        if (SamplePlanStatusEnum.YSAPLE.getCode().equals(status)) {
            planToUpdate.setSampleTime(DateUtils.getNowDate()); // 记录取样时间
            planToUpdate.setSamplerId(String.valueOf(SecurityUtils.getUserId()));
            planToUpdate.setSamplerName(SecurityUtils.getLoginUser().getUser().getNickName());
        }

        // 5. [关键审计] 自动填充 update_by 和 update_time
        planToUpdate.fillUpdateInfo();

        // 6. 调用已有的 update 方法
        // mapper 中的 updateOpSamplingPlan 方法会动态更新非 null 字段
        return opSamplingPlanMapper.updateOpSamplingPlan(planToUpdate);
    }

    @Override
    public String isRelease(String signInId) {
        List<OpSamplingPlan> opSamplingPlan = opSamplingPlanMapper.selectBySignInId(signInId);
        if(CollectionUtils.isAnyEmpty(opSamplingPlan)){
            return null;
        }
        if("1".equals(opSamplingPlan.get(0).getIsRelease())){
            return "1";
        }
        return "0";
    }

    @Override
    public int cancelOpSamplingPlan(String opSamplingPlanId) {
        // 1. 校验
        if (StringUtils.isEmpty(opSamplingPlanId)) {
            throw new ServiceException("关键参数缺失，无法作废计划");
        }

        // 2. 查询原始单据，确保它存在
        OpSamplingPlan plan = opSamplingPlanMapper.selectOpSamplingPlanByOpSamplingPlanId(opSamplingPlanId);
        if (plan == null) {
            throw new ServiceException("未找到该计划");
        }

        // 3. 创建一个只包含需要更新字段的实体
        OpSamplingPlan planToUpdate = new OpSamplingPlan();
        planToUpdate.setOpSamplingPlanId(opSamplingPlanId);
        planToUpdate.setStatus("3");

        // 4. [关键审计] 自动填充 update_by 和 update_time
        planToUpdate.fillUpdateInfo();

        // 5. 调用已有的 update 方法
        // mapper 中的 updateOpSamplingPlan 方法会动态更新非 null 字段
        return opSamplingPlanMapper.updateOpSamplingPlan(planToUpdate);
    }

    @Override
    public List<OpSamplingPlanSample> selectOpSamplingPlanSampleListByPlanId(OpSamplingPlanSample opSamplingPlanSample) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            opSamplingPlanSample.setDeptId(SecurityUtils.getDeptId().toString());
        }
        return opSamplingPlanSampleMapper.selectDestroyList(opSamplingPlanSample);
    }

    @Override
    public int destroyOpSamplingPlanSample(String opSamplingPlanSampleId) {
        // 查询是否存在
        OpSamplingPlanSample sample = opSamplingPlanSampleMapper.selectOpSamplingPlanSampleByOpSamplingPlanSampleId(opSamplingPlanSampleId);
        if (sample == null) {
            throw new ServiceException("未找到该样品");
        }

        // 检查是否已销毁
        if ("1".equals(sample.getIsDestroy())) {
            throw new ServiceException("该样品已被销毁");
        }

        // 设置销毁状态
        sample.setIsDestroy("1");
        sample.fillUpdateInfo();
        return opSamplingPlanSampleMapper.updateOpSamplingPlanSample(sample);
    }

    @Override
    public int linkOpSamplingPlanSample(OpSamplingPlanSample opSamplingPlanSample) {
        if (StringUtils.isEmpty(opSamplingPlanSample.getOpSamplingPlanSampleId())
                || StringUtils.isEmpty(opSamplingPlanSample.getSamplingPlanId())) {
            throw new ServiceException("关键参数缺失，无法关联计划");
        }

        return opSamplingPlanSampleMapper.updateOpSamplingPlanSample(opSamplingPlanSample);
    }

    @Override
    @Transactional
    public int addKCDL(OpSamplingPlan opSamplingPlan) {
        if (CollectionUtils.isAnyEmpty(opSamplingPlan.getOpSamplingPlanSampleList())) {
            throw new ServiceException("关键参数缺失，无法新增");
        }

        for (OpSamplingPlanSample sample : opSamplingPlan.getOpSamplingPlanSampleList()) {
            // 如果关联了计划修改计划状态
            if (!StringUtils.isEmpty(sample.getSamplingPlanId())) {
                // 更新计划状态
                OpFinishedProductSamplingPlan opFinishedProductSamplingPlan = opFinishedProductSamplingPlanMapper.
                        selectOpFinishedProductSamplingPlanByFinishedProductSamplingPlanId(sample.getSamplingPlanId());
                if (opFinishedProductSamplingPlan == null) {
                    throw new ServiceException("未找到该计划");
                }

                if (!"0".equals(opFinishedProductSamplingPlan.getStatus())) {
                    throw new ServiceException("该计划已提交无法审核");
                }

                opFinishedProductSamplingPlan.setStatus("2");
                opFinishedProductSamplingPlanMapper.updateOpFinishedProductSamplingPlan(opFinishedProductSamplingPlan);
            }

            if (StringUtils.isNotEmpty(sample.getIsReceive()) && "1".equals(sample.getIsReceive())){
                throw new ServiceException("已接收的样品不能修改");
            }

            if (!StringUtils.isEmpty(sample.getOpSamplingPlanSampleId())) {
                opSamplingPlanSampleMapper.deleteOpSamplingPlanSampleByOpSamplingPlanSampleId(sample.getOpSamplingPlanSampleId());
            }

            sample.fillCreateInfo();
            String sampleId = IdUtils.simpleUUID();
            sample.setOpSamplingPlanSampleId(sampleId);
            sample.setIsDelete(YesNo2Enum.NO.getCode());
            sample.setIsPushSap("0");
            sample.setIsDestroy("0");

            // 判断取样单号是否有值
            if (StringUtils.isEmpty(sample.getSampleNo())) {
                try {
                    if (sample.getSamplingType().equals("0")) {
                        String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_FINISHED_PRODUCT_SAMPLING_ORDER);
                        sample.setSampleNo(resultNo);
                    }
                    if (sample.getSamplingType().equals("1")) {
                        String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_INVENTORY_SAMPLING_ORDER);
                        sample.setSampleNo(resultNo);
                    }
                    if (sample.getSamplingType().equals("2")) {
                        String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_FEED_SAMPLING_ORDER);
                        sample.setSampleNo(resultNo);
                    }
                    if (sample.getSamplingType().equals("3")) {
                        String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_YLQYD);
                        sample.setSampleNo(resultNo);
                    }

                } catch (BusinessException e) {
                    throw new RuntimeException("生成编号失败: " + e.getMessage());
                }
            }

            sample.setDeptId(String.valueOf(SecurityUtils.getDeptId()));

            // 处理检测项目
            List<OpSamplingPlanItem> itemList = sample.getOpSamplingPlanItemList();
            if (ObjectUtils.isEmpty(itemList)) {
                throw new RuntimeException("未选择检验项目");
            } else {
                for (OpSamplingPlanItem item : itemList) {

                    // 删除化验表
                    opSamplingPlanItemMapper.updateDeleteFlagById(item.getOpSamplingPlanItemId());

                    item.fillCreateInfo();
                    item.setIsDelete("0");
                    item.setOpSamplingPlanItemId(IdUtils.simpleUUID());
                    item.setSamplingPlanSampleId(sampleId);
                }
            }
            opSamplingPlanItemMapper.batchInsertOpSamplingPlanItem(itemList);
        }

        return opSamplingPlanSampleMapper.batchInsertOpSamplingPlanSample(opSamplingPlan.getOpSamplingPlanSampleList());
    }

    /**
     * 管理员全流程监控：查询样品平铺列表（含检测项目统计进度）
     */
    @Override
    public List<OpSamplingPlanSampleMonitorVO> selectSampleMonitorList(OpSamplingPlanSample opSamplingPlanSample) {
        // 1. 数据权限校验
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            opSamplingPlanSample.setDeptId(String.valueOf(SecurityUtils.getDeptId()));
        }

        // 2. 获取样品平铺列表（含基本统计及计划冗余字段）
        List<OpSamplingPlanSampleMonitorVO> samples = opSamplingPlanSampleMapper.selectSampleMonitorList(opSamplingPlanSample);

        // 3. 循环填充每个样品下的具体检测项目详情
        for (OpSamplingPlanSampleMonitorVO sample : samples) {
            // 穿透：查询每个物料下所有检测项目的实时结果
            List<OpSamplingPlanItem> items = opSamplingPlanItemMapper.selectItemMonitorDetail(sample.getOpSamplingPlanSampleId());
            sample.setItemList(items);
        }
        return samples;
    }

    @Override
    public SamplingReceiveListVo selectopSamplingPlanSampleInfoById(String opSamplingPlanSampleId) {
        if (StringUtils.isBlank(opSamplingPlanSampleId)){
            throw new RuntimeException("参数缺失 请联系管理员");
        }
        SamplingReceiveListVo sampleInfo = opSamplingPlanMapper.selectopSamplingPlanSampleInfoById(opSamplingPlanSampleId);
        if (ObjectUtils.isEmpty(sampleInfo)) {
            throw new BusinessException(String.format("未查询到取样样品ID【%s】对应的数据", opSamplingPlanSampleId));
        }
        String samplingPlanSampleId = sampleInfo.getSamplingPlanSampleId();
        if (StringUtils.isNotBlank(samplingPlanSampleId)) {
                // 查询检测项目列表
                List<OpSamplingPlanItem> itemList = opSamplingPlanItemMapper.selectOpSamplingPlanItemBySampleId(sampleInfo.getSamplingPlanSampleId());
                sampleInfo.setOpSamplingPlanItemList(itemList);

        }

        return sampleInfo;
    }

    /**
     * 判断两个Date是否为同一天
     */
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取指定日期的当天00:00:00
     */
    private Date getStartOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);   // 小时设为0
        cal.set(Calendar.MINUTE, 0);        // 分钟设为0
        cal.set(Calendar.SECOND, 0);        // 秒设为0
        cal.set(Calendar.MILLISECOND, 0);   // 毫秒设为0
        return cal.getTime();
    }

    /**
     * 获取指定日期的当天23:59:59.999
     */
    private Date getEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);  // 小时设为23
        cal.set(Calendar.MINUTE, 59);       // 分钟设为59
        cal.set(Calendar.SECOND, 59);       // 秒设为59
        cal.set(Calendar.MILLISECOND, 999); // 毫秒设为999（避免漏数据）
        return cal.getTime();
    }

}
