package com.gmlimsqi.business.labtest.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.gmlimsqi.business.basicdata.domain.BsWorkdayConfig;
import com.gmlimsqi.business.basicdata.mapper.BsWorkdayConfigMapper;
import com.gmlimsqi.business.labtest.domain.*;
import com.gmlimsqi.business.labtest.dto.OpSampleReceiveDto;
import com.gmlimsqi.business.labtest.mapper.*;
import com.gmlimsqi.business.labtest.service.IOpFeedEntrustOrderService;
import com.gmlimsqi.business.labtest.service.IOpSampleReceiveService;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.UserInfoProcessor;
import com.gmlimsqi.common.annotation.DataScope;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.enums.EntrustOrderStatusEnum;
import com.gmlimsqi.common.enums.EntrustOrderTypeEnum;
import com.gmlimsqi.common.enums.YesNo2Enum;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.service.ISysConfigService;
import com.gmlimsqi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 饲料样品委托单Service业务层处理
 * 
 * @author hhy
 * @date 2025-09-13
 */
@Service
public class OpFeedEntrustOrderServiceImpl implements IOpFeedEntrustOrderService 
{
    @Autowired
    private OpFeedEntrustOrderMapper opFeedEntrustOrderMapper;
    @Autowired
    private UserInfoProcessor userInfoProcessor;
    @Autowired
    private OpFeedEntrustOrderSampleMapper feedEntrustOrderSampleMapper;
    @Autowired
    private OpFeedEntrustOrderItemMapper opFeedEntrustOrderItemMapper;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;
    @Autowired
    private IOpSampleReceiveService sampleReceiveService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private BsWorkdayConfigMapper bsWorkdayConfigMapper;

    // 服务等级与所需工作日的映射（替代switch）
    private static final Map<String, Integer> SERVICE_LEVEL_DAYS = new HashMap<>();
    static {
        SERVICE_LEVEL_DAYS.put("1", 6); // 普通件
        SERVICE_LEVEL_DAYS.put("2", 4); // 加快件
        SERVICE_LEVEL_DAYS.put("3", 3); // 特快件
        SERVICE_LEVEL_DAYS.put("4", 2); // 特急件
    }

    // 日期格式器（统一处理"yyyyMMdd"格式）
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    @Autowired
    private OpJczxFeedReportBaseMapper feedReportBaseMapper;
    @Autowired
    private OpJczxFeedResultInfoMapper feedResultInfoMapper;
    @Autowired
    private OpFeedEntrustOrderChangeLogMapper changeLogMapper;
    @Autowired
    private  OpFeedEntrustOrderSampleMapper opFeedEntrustOrderSampleMapper;
    @Autowired
    private ISysConfigService configService;
    /**
     * 查询饲料样品委托单
     * 
     * @param opFeedEntrustOrderId 饲料样品委托单主键
     * @return 饲料样品委托单
     */
    @Override
    public OpFeedEntrustOrder selectOpFeedEntrustOrderByOpFeedEntrustOrderId(String opFeedEntrustOrderId)
    {
        // 1. 查询主表和正常的样品/项目
        OpFeedEntrustOrder order = opFeedEntrustOrderMapper.selectOrderDetailById(opFeedEntrustOrderId);

        if (order != null) {
            // 2. 查询所有“已删除”状态的样品
            List<OpFeedEntrustOrderSample> deletedSamples = opFeedEntrustOrderSampleMapper.selectDeletedSamplesByOrderId(opFeedEntrustOrderId);

            // 【核心修改开始】：过滤掉非接收方删除的样品
            if (deletedSamples != null && !deletedSamples.isEmpty()) {
                // 获取检测中心（接收方）的部门ID
                String jczxDeptIdStr = configService.selectConfigByKey("jczx.deptId");

                // 使用迭代器安全删除元素
                Iterator<OpFeedEntrustOrderSample> iterator = deletedSamples.iterator();
                while (iterator.hasNext()) {
                    OpFeedEntrustOrderSample sample = iterator.next();
                    String deleteBy = sample.getUpdateBy(); // 获取执行删除的人

                    // 如果没有删除人信息，默认不显示，直接移除
                    if (StringUtils.isEmpty(deleteBy)) {
                        iterator.remove();
                        continue;
                    }

                    // 查询该用户的部门信息
                    // 注意：为了性能，如果有大量删除数据，建议批量查询用户。但通常删除数据量很少，循环查问题不大。
                    SysUser user = sysUserService.selectUserById(Long.valueOf(deleteBy));

                    // 判断逻辑：
                    // 如果查不到用户 OR 用户部门为空 OR 用户部门 != 检测中心部门ID
                    // 则认为是委托方（非接收方）删除的，从列表中移除，不给看
                    if (user == null || user.getDeptId() == null ||
                            !String.valueOf(user.getDeptId()).equals(jczxDeptIdStr)) {
                        iterator.remove();
                    }
                }
            }
            // 【核心修改结束】

            // 设置过滤后的列表
            order.setDeletedSampleList(deletedSamples);

            // 3. 填充已删除样品的检测项目（针对过滤后剩下的样品）
            if (deletedSamples != null && !deletedSamples.isEmpty()) {
                for (OpFeedEntrustOrderSample delSample : deletedSamples) {
                    List<OpFeedEntrustOrderItem> delItems = opFeedEntrustOrderItemMapper.selectItemsBySampleIdIncludeDeleted(delSample.getOpFeedEntrustOrderSampleId());
                    delSample.setTestItem(delItems);
                }
            }

            // 4. 查询修改日志 (保持原有逻辑)
            List<String> allIds = new ArrayList<>();
            allIds.add(order.getOpFeedEntrustOrderId());
            if (order.getSampleList() != null) {
                order.getSampleList().forEach(s -> allIds.add(s.getOpFeedEntrustOrderSampleId()));
            }
            // 这里也只添加过滤后剩下的已删除样品ID查日志
            if (deletedSamples != null) {
                deletedSamples.forEach(s -> allIds.add(s.getOpFeedEntrustOrderSampleId()));
            }

            if (!allIds.isEmpty()) {
                List<OpFeedEntrustOrderChangeLog> logs = changeLogMapper.selectLogsByBusinessIds(allIds);
                order.setChangeLogs(logs);
            }
        }
        return order;
    }

    /**
     * 查询饲料样品委托单列表
     * 
     * @param opFeedEntrustOrder 饲料样品委托单
     * @return 饲料样品委托单
     */
    @Override
    public List<OpFeedEntrustOrder> selectOpFeedEntrustOrderList(OpFeedEntrustOrder opFeedEntrustOrder)
    {
        //检测中心看到所有委托单，牧场只看自己委托单
        String jczxDeptId = configService.selectConfigByKey("jczx.deptId");
        if(jczxDeptId.equals(SecurityUtils.getDeptId().toString())) {

        }else {
            opFeedEntrustOrder.setEntrustDeptId(SecurityUtils.getDeptId());
        }
        // 处理结束日期，设置为当天的23:59:59
        if (opFeedEntrustOrder.getSendSampleDateEnd() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(opFeedEntrustOrder.getSendSampleDateEnd());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            opFeedEntrustOrder.setSendSampleDateEnd(calendar.getTime());
        }


        if(SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            //超管不过滤部门，显示全部
            opFeedEntrustOrder.setEntrustDeptId(null);
        }else {
            Long deptId = SecurityUtils.getDeptId();
            if (deptId != null){
                opFeedEntrustOrder.setEntrustDeptId(deptId);
            }
        }
        List<OpFeedEntrustOrder> items = opFeedEntrustOrderMapper.selectOpFeedEntrustOrderList(opFeedEntrustOrder);

        // 批量处理用户名（创建人和更新人）
        userInfoProcessor.processBaseEntityUserInfo(items);

        return items;
    }

    /**
     * 新增饲料样品委托单
     * 
     * @param opFeedEntrustOrder 饲料样品委托单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertOpFeedEntrustOrder(OpFeedEntrustOrder opFeedEntrustOrder)
    {
        if (StringUtils.isEmpty(opFeedEntrustOrder.getOpFeedEntrustOrderId())) {
            opFeedEntrustOrder.setOpFeedEntrustOrderId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opFeedEntrustOrder.fillCreateInfo();
        // 根据 isSubmit 决定初始状态
        if (Boolean.TRUE.equals(opFeedEntrustOrder.getIsSubmit())) {
            opFeedEntrustOrder.setStatus(EntrustOrderStatusEnum.DSL.getCode()); // 1
        } else {
            opFeedEntrustOrder.setStatus(EntrustOrderStatusEnum.DTJ.getCode()); // 0
        }
        try {
            String orderNo = codeGeneratorUtil.generateFeedEntrustOrderNo();
            opFeedEntrustOrder.setEntrustOrderNo(orderNo);
        } catch (BusinessException e) {
            throw new BusinessException("生成委托单编号失败: " + e.getMessage());
        }

        // 计算执行期限
//        calculateExecutionPeriod(opFeedEntrustOrder, bsWorkdayConfigMapper);

        int count = opFeedEntrustOrderMapper.insertOpFeedEntrustOrder(opFeedEntrustOrder);

        // 2. 保存样品表
        if (opFeedEntrustOrder.getSampleList() != null && !opFeedEntrustOrder.getSampleList().isEmpty()) {
            for (OpFeedEntrustOrderSample sample : opFeedEntrustOrder.getSampleList()) {
                try {
//                    String sampleNo = codeGeneratorUtil.generateFeedSampleNo();
//                    sample.setSampleNo(sampleNo);
                    sample.setFeedEntrustOrderId(opFeedEntrustOrder.getOpFeedEntrustOrderId());
                    sample.setOpFeedEntrustOrderSampleId(IdUtils.simpleUUID());
                    sample.fillCreateInfo();
                    feedEntrustOrderSampleMapper.insertOpFeedEntrustOrderSample(sample);

                    // 3. 保存样品-项目关联
                    if (sample.getTestItem() != null && !sample.getTestItem().isEmpty()) {
                        List<OpFeedEntrustOrderItem> itemList = new ArrayList<>();
                        for (OpFeedEntrustOrderItem testItem : sample.getTestItem()) {
                            OpFeedEntrustOrderItem item = new OpFeedEntrustOrderItem();
                            item.setOpFeedEntrustOrderItemId(IdUtils.simpleUUID());
                            item.setOpFeedEntrustOrderSampleId(sample.getOpFeedEntrustOrderSampleId());
                            item.setItemId(testItem.getItemId());
                            item.fillCreateInfo();
                            itemList.add(item);
                        }
                        if (!itemList.isEmpty()) {
                            opFeedEntrustOrderItemMapper.insertBatch(itemList);
                        }
                    }
                } catch (BusinessException e) {
                    throw new BusinessException("生成样品编号失败: " + e.getMessage());
                }

            }
        }
        return count;
    }
    @Override
    @Transactional
    public int updateOpFeedEntrustOrder3(OpFeedEntrustOrder opFeedEntrustOrder) {
        String orderId = opFeedEntrustOrder.getOpFeedEntrustOrderId();

        // 1. 获取数据库中的旧数据全貌 (包含样品和项目)
        OpFeedEntrustOrder oldFullOrder = opFeedEntrustOrderMapper.selectOrderDetailById(orderId);
        if (oldFullOrder == null) {
            throw new RuntimeException("委托单不存在");
        }
        // 如果是“提交/撤回”操作（isSubmit != null），则严格校验状态；
        // 如果是“仅保存”操作（isSubmit == null，通常是接收方修改），则允许修改。
        if (opFeedEntrustOrder.getIsSubmit() != null) {
            // 如果试图改变状态（提交或存草稿），必须确保当前是 待提交(0) 或 已驳回(6)
            if (!EntrustOrderStatusEnum.DTJ.getCode().equals(oldFullOrder.getStatus()) &&
                    !EntrustOrderStatusEnum.YBH.getCode().equals(oldFullOrder.getStatus())) {
                throw new RuntimeException("当前委托单已提交或受理，不允许修改状态，请先撤回");
            }
        }// else: 如果 isSubmit 为 null，说明是接收方或管理员在修改业务数据，不做状态拦截（

        // 将旧样品转为 Map，方便按 ID 快速查找 (Key: sampleId, Value: sample对象)
        Map<String, OpFeedEntrustOrderSample> oldSampleMap = new HashMap<>();
        if (oldFullOrder.getSampleList() != null) {
            for (OpFeedEntrustOrderSample s : oldFullOrder.getSampleList()) {
                oldSampleMap.put(s.getOpFeedEntrustOrderSampleId(), s);
            }
        }

        // 2. 准备日志集合与当前用户
        List<OpFeedEntrustOrderChangeLog> logList = new ArrayList<>();
        String currentUsername = SecurityUtils.getUsername(); // 或 opFeedEntrustOrder.getUpdateBy()

        // 3. 对比并更新主表字段 (委托单信息)
        compareAndLog(orderId, "ORDER", "producerUnitName", "生产企业",oldFullOrder.getProducerUnitName(), opFeedEntrustOrder.getProducerUnitName(), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "reportMailingAddress", "报告寄送地址",
                oldFullOrder.getReportMailingAddress(), opFeedEntrustOrder.getReportMailingAddress(), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "entrustContact", "联系人",
                oldFullOrder.getEntrustContact(), opFeedEntrustOrder.getEntrustContact(), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "entrustContactPhone", "联系电话",
                oldFullOrder.getEntrustContactPhone(), opFeedEntrustOrder.getEntrustContactPhone(), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "sendSampleDate", "送样日期",
                formatDate(oldFullOrder.getSendSampleDate()), formatDate(opFeedEntrustOrder.getSendSampleDate()), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "reportReceiver", "取报告人",
                oldFullOrder.getReportReceiver(), opFeedEntrustOrder.getReportReceiver(), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "invoiceTitle", "发票抬头",
                oldFullOrder.getInvoiceTitle(), opFeedEntrustOrder.getInvoiceTitle(), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "invoiceType", "发票类型",
                oldFullOrder.getInvoiceType(), opFeedEntrustOrder.getInvoiceType(), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "paymentStatus", "付款状态",
                toString(oldFullOrder.getPaymentStatus()), toString(opFeedEntrustOrder.getPaymentStatus()), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "paymentMethod", "付款方式",
                toString(oldFullOrder.getPaymentMethod()), toString(opFeedEntrustOrder.getPaymentMethod()), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "remark", "委托单备注",
                oldFullOrder.getRemark(), opFeedEntrustOrder.getRemark(), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "sampleReturnPolicy", "样品要求",
                oldFullOrder.getSampleReturnPolicy(), opFeedEntrustOrder.getSampleReturnPolicy(), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "requiresJudgement", "是否判定",
                oldFullOrder.getRequiresJudgement(), opFeedEntrustOrder.getRequiresJudgement(), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "allowsSubcontracting", "是否同意分包",
                oldFullOrder.getAllowsSubcontracting(), opFeedEntrustOrder.getAllowsSubcontracting(), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "reportReceiveType", "报告领取方式",
                oldFullOrder.getReportReceiveType(), opFeedEntrustOrder.getReportReceiveType(), logList, currentUsername);
        // 如果是接收操作（isReceive=Y），计算本次操作删除的样品数量并记录
        if (YesNo2Enum.YES.getCode().equals(opFeedEntrustOrder.getIsReceive())) {
            long deleteCount = 0;
            // 确保旧数据存在 (oldSampleMap 在方法开头已构建)
            if (oldSampleMap != null && !oldSampleMap.isEmpty()) {
                // 1. 获取前端本次提交的所有有效样品ID集合
                Set<String> inputIds = new HashSet<>();
                List<OpFeedEntrustOrderSample> inputSampleList = opFeedEntrustOrder.getSampleList();
                if (inputSampleList != null) {
                    inputSampleList.stream()
                            .map(OpFeedEntrustOrderSample::getOpFeedEntrustOrderSampleId)
                            .filter(StringUtils::isNotEmpty) // 过滤掉没有ID的新增样品
                            .forEach(inputIds::add);
                }

                // 2. 遍历旧数据，如果在提交的ID集合中不存在，则视为本次被删除
                for (String oldId : oldSampleMap.keySet()) {
                    if (!inputIds.contains(oldId)) {
                        deleteCount++;
                    }
                }
            }
            // 3. 设置删除数量字段 (注意类型转换，这里假设数据库是 int/Integer)
            opFeedEntrustOrder.setScypsl((int)deleteCount);
        }
        opFeedEntrustOrder.fillUpdateInfo();
        // 更新主表
        opFeedEntrustOrderMapper.updateOpFeedEntrustOrder(opFeedEntrustOrder);


        // ================== 4. 核心：样品处理 (Upsert 逻辑) ==================

        // 用来记录本次操作涉及到的样品ID，用于最后找出被删除的样品
        Set<String> processedIds = new HashSet<>();

        if (opFeedEntrustOrder.getSampleList() != null) {
            for (OpFeedEntrustOrderSample sample : opFeedEntrustOrder.getSampleList()) {

                String inputId = sample.getOpFeedEntrustOrderSampleId();

                // --- 情况 A: 修改 (Update) ---
                if (StringUtils.isNotEmpty(inputId) && oldSampleMap.containsKey(inputId)) {
                    OpFeedEntrustOrderSample oldSample = oldSampleMap.get(inputId);
                    processedIds.add(inputId); // 标记已处理

                    // A1. 记录修改日志 (ID 不变，直接关联)
                    compareAndLog(inputId, "SAMPLE", "materialName", "物料",
                            oldSample.getMaterialName(), sample.getMaterialName(), logList, currentUsername);
                    compareAndLog(inputId, "SAMPLE", "name", "类别",
                            oldSample.getName(), sample.getName(), logList, currentUsername);
                    compareAndLog(inputId, "SAMPLE", "model", "样品数量",
                            oldSample.getModel(), sample.getModel(), logList, currentUsername);
                    compareAndLog(inputId, "SAMPLE", "batchNo", "批号",
                            oldSample.getBatchNo(), sample.getBatchNo(), logList, currentUsername);
                    compareAndLog(inputId, "SAMPLE", "packaging", "包装",
                            oldSample.getPackaging(), sample.getPackaging(), logList, currentUsername);
                    compareAndLog(inputId, "SAMPLE", "status", "样品状况", oldSample.getStatus(), sample.getStatus(), logList, currentUsername);
                    compareAndLog(inputId, "SAMPLE", "storageRequirement", "存储要求", oldSample.getStorageRequirement(), sample.getStorageRequirement(), logList, currentUsername);
                    compareAndLog(inputId, "SAMPLE", "testMethod", "检测方法", oldSample.getTestMethod(), sample.getTestMethod(), logList, currentUsername);
                    compareAndLog(inputId, "SAMPLE", "producerUnit", "供应商", oldSample.getProducerUnit(), sample.getProducerUnit(), logList, currentUsername);
                    compareAndLog(inputId, "SAMPLE", "sampleRemark", "样品备注", oldSample.getSampleRemark(), sample.getSampleRemark(), logList, currentUsername);
                    // A2. 对比检测项目 (将 List 转 String 对比)
                    String oldItemsStr = formatItems(oldSample.getTestItem());
                    String newItemsStr = formatItems(sample.getTestItem());
                    compareAndLog(inputId, "SAMPLE", "testItem", "检测项目",
                            oldItemsStr, newItemsStr, logList, currentUsername);

                    // A3. 执行更新
                    sample.fillUpdateInfo();
                    feedEntrustOrderSampleMapper.updateOpFeedEntrustOrderSample(sample);

                    // A4. 处理该样品下的项目 (依然采用删旧插新，因为项目表ID不重要，且逻辑简单)
                    opFeedEntrustOrderItemMapper.updateDeleteBySampleId(opFeedEntrustOrder.getUpdateBy(), inputId);
                    insertItemsForSample(sample.getTestItem(), inputId);

                }
                // --- 情况 B: 新增 (Insert) ---
                else {
                    sample.setFeedEntrustOrderId(orderId);
                    if (StringUtils.isEmpty(sample.getOpFeedEntrustOrderSampleId())) {
                        sample.setOpFeedEntrustOrderSampleId(IdUtils.simpleUUID());
                    }
                    sample.fillCreateInfo();
                    feedEntrustOrderSampleMapper.insertOpFeedEntrustOrderSample(sample);

                    // 插入新项目
                    insertItemsForSample(sample.getTestItem(), sample.getOpFeedEntrustOrderSampleId());
                }
            }
        }

        // ================== 5. 处理真删除的样品 ==================
        // 遍历数据库里的旧ID，如果不在本次处理的 processedIds 集合里，说明被前端删除了
        for (String oldId : oldSampleMap.keySet()) {
            if (!processedIds.contains(oldId)) {
                // 执行软删除 is_delete = '1'
                OpFeedEntrustOrderSample delSample = new OpFeedEntrustOrderSample();
                delSample.setOpFeedEntrustOrderSampleId(oldId);
                delSample.setIsDelete("1"); // 真正删除
                delSample.setUpdateBy(opFeedEntrustOrder.getUpdateBy());
                delSample.setUpdateTime(new Date());
                feedEntrustOrderSampleMapper.updateOpFeedEntrustOrderSample(delSample);

                // 同时软删除其关联的项目
                opFeedEntrustOrderItemMapper.updateDeleteStatusBySampleId(oldId, "1", opFeedEntrustOrder.getUpdateBy());
            }
        }

        // 6. 批量插入日志
        if (!logList.isEmpty()) {
            changeLogMapper.insertBatch(logList);
        }

        // 注意：这里不需要再 updateOpFeedEntrustOrder 了，因为第3步已经更新过主表了
        // 如果你需要更新状态，可以单独写一个 updateStatus 或者复用
        // 为了保险起见，或者如果 updateOpFeedEntrustOrder 包含状态更新逻辑，可以再调用一次，或者手动更新状态对象
        // 建议：直接构建一个只更新状态的对象，避免覆盖上面的字段
        OpFeedEntrustOrder statusUpdater = new OpFeedEntrustOrder();
        statusUpdater.setOpFeedEntrustOrderId(orderId);
        statusUpdater.setUpdateBy(opFeedEntrustOrder.getUpdateBy());
        statusUpdater.setUpdateTime(new Date());
        boolean statusChanged = false;

        // 只有当 isSubmit 明确不为 null 时，才进行状态流转
        if (opFeedEntrustOrder.getIsSubmit() != null) {
            if (Boolean.TRUE.equals(opFeedEntrustOrder.getIsSubmit())) {
                // 动作：提交 -> 变更为 待受理(1)
                statusUpdater.setStatus(EntrustOrderStatusEnum.DSL.getCode());
                // 提交时重置退回状态
                statusUpdater.setIsReturn("0");
            } else {
                // 动作：存草稿 -> 变更为 待提交(0)
                statusUpdater.setStatus(EntrustOrderStatusEnum.DTJ.getCode());
            }
            statusChanged = true;
        }
        // 如果 isSubmit 为 null，则完全不设置 status 字段，MyBatis 会忽略该字段的更新，从而保持原状态（如检测中、已受理等）

        // 如果状态有变更，执行更新
        if (statusChanged) {
             opFeedEntrustOrderMapper.updateOpFeedEntrustOrder(statusUpdater);
        }


        List<String> newSampleIds = new ArrayList<>();
        List<String> orderIds = new ArrayList<>();

        //接收
        if(YesNo2Enum.YES.getCode().equals(opFeedEntrustOrder.getIsReceive())){
            OpSampleReceiveDto dto = new OpSampleReceiveDto();
            dto.setType(EntrustOrderTypeEnum.FEED.getCode());
            orderIds.add(orderId);
            List<OpFeedEntrustOrderSample> opFeedEntrustOrderSamples = feedEntrustOrderSampleMapper.selectSamplesByOrderIds(orderIds);
            if(!opFeedEntrustOrderSamples.isEmpty()){
                newSampleIds = opFeedEntrustOrderSamples.stream().map(OpFeedEntrustOrderSample::getOpFeedEntrustOrderSampleId).collect(Collectors.toList());
            }
            String[] sampleIds = newSampleIds.toArray(String[]::new);

            dto.setSampleIds(sampleIds);
            sampleReceiveService.add(dto);
            //如果状态不是待处理，则不允许修改
            OpFeedEntrustOrder calculate = opFeedEntrustOrderMapper.selectOpFeedEntrustOrderByOpFeedEntrustOrderId(opFeedEntrustOrder.getOpFeedEntrustOrderId());
            // 计算执行期限
            calculateExecutionPeriod(calculate, bsWorkdayConfigMapper);
            // 修改
            opFeedEntrustOrderMapper.updateExecutionPeriod(calculate.getExecutionPeriod(),calculate.getOpFeedEntrustOrderId());
        }
        return 1;
    }
    // 简单的转字符串防空指针
    private String toString(Object obj) {
        return obj == null ? "" : String.valueOf(obj);
    }

    /**
     * 辅助方法：插入项目
     */
    private void insertItemsForSample(List<OpFeedEntrustOrderItem> items, String sampleId) {
        if (items != null && !items.isEmpty()) {
            List<OpFeedEntrustOrderItem> itemList = new ArrayList<>();

            for (OpFeedEntrustOrderItem testItem : items) {
                OpFeedEntrustOrderItem item = new OpFeedEntrustOrderItem();

                // 1. 生成新 ID
                String newItemId = IdUtils.simpleUUID();
                item.setOpFeedEntrustOrderItemId(newItemId);

                item.setOpFeedEntrustOrderSampleId(sampleId);
                item.setItemId(testItem.getItemId()); // 这是字典ID（如：钙）
                item.fillCreateInfo();
                itemList.add(item);

                // 2. 【核心新增】尝试迁移旧结果到这个新ID上
                // 逻辑：如果结果表里已经有属于这个样品且是“钙”的结果，把它指给这个新ID
                opFeedEntrustOrderMapper.migrateResultToNewItem(sampleId, testItem.getItemId(), newItemId);
            }

            // 3. 批量插入新项目
            if (!itemList.isEmpty()) {
                opFeedEntrustOrderItemMapper.insertBatch(itemList);
            }
        }
    }


    @Override
    public void withdrawOrder(String orderId) {
        OpFeedEntrustOrder order = opFeedEntrustOrderMapper.selectOpFeedEntrustOrderByOpFeedEntrustOrderId(orderId);
        if (order == null) throw new BusinessException("订单不存在");

        // 只有 待受理(1) 的单子可以撤回 (如果已经检测中，就不允许撤回了)
        if (!EntrustOrderStatusEnum.DSL.getCode().equals(order.getStatus())) {
            throw new BusinessException("当前状态不允许撤回（仅待受理状态可撤回）");
        }

        OpFeedEntrustOrder update = new OpFeedEntrustOrder();
        update.setOpFeedEntrustOrderId(orderId);
        update.setStatus(EntrustOrderStatusEnum.DTJ.getCode()); // 变回 0
        update.setUpdateBy(SecurityUtils.getUserId().toString());
        update.setUpdateTime(new Date());

        opFeedEntrustOrderMapper.updateOpFeedEntrustOrder(update);
    }

//    @Override
//    @Transactional
//    public int updateOpFeedEntrustOrder(OpFeedEntrustOrder opFeedEntrustOrder)
//    {
//        //如果状态不是待处理，则不允许修改
//        OpFeedEntrustOrder selectOrder = opFeedEntrustOrderMapper.selectOpFeedEntrustOrderByOpFeedEntrustOrderId(opFeedEntrustOrder.getOpFeedEntrustOrderId());
//        if(selectOrder==null){
//            throw new RuntimeException("委托单不存在");
//        }
////        if(!EntrustOrderStatusEnum.DSL.getCode().equals(selectOrder.getStatus()) &&
////                !EntrustOrderStatusEnum.YBH.getCode().equals(selectOrder.getStatus())){
////            throw new RuntimeException("委托单已受理，不允许修改");
////        }
//
//        // 自动填充更新信息
//        opFeedEntrustOrder.fillUpdateInfo();
//        //先删除再插入
//        feedEntrustOrderSampleMapper.updateDeleteByOrderId(opFeedEntrustOrder.getUpdateBy(), opFeedEntrustOrder.getOpFeedEntrustOrderId());
//        opFeedEntrustOrderItemMapper.updateDeleteByOrderId(opFeedEntrustOrder.getUpdateBy(),opFeedEntrustOrder.getOpFeedEntrustOrderId());
//        List<String> newSampleIds = new ArrayList<>();
//        if (opFeedEntrustOrder.getSampleList() != null && !opFeedEntrustOrder.getSampleList().isEmpty()) {
//            for (OpFeedEntrustOrderSample sample : opFeedEntrustOrder.getSampleList()) {
//                String oldSampleId = sample.getOpFeedEntrustOrderSampleId();
//                sample.setFeedEntrustOrderId(opFeedEntrustOrder.getOpFeedEntrustOrderId());
//                sample.setOpFeedEntrustOrderSampleId(IdUtils.simpleUUID());
//                sample.fillCreateInfo();
//                newSampleIds.add(sample.getOpFeedEntrustOrderSampleId());
//                feedEntrustOrderSampleMapper.insertOpFeedEntrustOrderSample(sample);
//                if (sample.getTestItem() != null && !sample.getTestItem().isEmpty()) {
//                    List<OpFeedEntrustOrderItem> itemList = new ArrayList<>();
//                    for (OpFeedEntrustOrderItem testItem : sample.getTestItem()) {
//                        String oldItemId = testItem.getOpFeedEntrustOrderItemId();
//                        OpFeedEntrustOrderItem item = new OpFeedEntrustOrderItem();
//                        item.setOpFeedEntrustOrderItemId(IdUtils.simpleUUID());
//                        item.setOpFeedEntrustOrderSampleId(sample.getOpFeedEntrustOrderSampleId());
//                        item.setItemId(testItem.getItemId());
//                        item.fillCreateInfo();
//                        itemList.add(item);
//                        updateBussinessTableItemId(oldItemId,item.getOpFeedEntrustOrderItemId());
//                    }
//                    if (!itemList.isEmpty()) {
//                        opFeedEntrustOrderItemMapper.insertBatch(itemList);
//                    }
//                }
//
//                //更新result_info表和report_info、op_jczx_feed_report_base表的id
//                String newSampleId = sample.getOpFeedEntrustOrderSampleId();
//
//                updateBussinessTableSampleId(oldSampleId,newSampleId);
//
//            }
//        }
//
//        // 提交时修改为未退回状态
//        opFeedEntrustOrder.setIsReturn("0");
//        opFeedEntrustOrder.setStatus(EntrustOrderStatusEnum.DSL.getCode());
//        int count = opFeedEntrustOrderMapper.updateOpFeedEntrustOrder(opFeedEntrustOrder);
//
//        //接收
//        if(YesNo2Enum.YES.getCode().equals(opFeedEntrustOrder.getIsReceive())){
//            OpSampleReceiveDto dto = new OpSampleReceiveDto();
//            dto.setType(EntrustOrderTypeEnum.FEED.getCode());
//            String[] sampleIds = newSampleIds.toArray(String[]::new);
//            dto.setSampleIds(sampleIds);
//            sampleReceiveService.add(dto);
//            //如果状态不是待处理，则不允许修改
//            OpFeedEntrustOrder calculate = opFeedEntrustOrderMapper.selectOpFeedEntrustOrderByOpFeedEntrustOrderId(opFeedEntrustOrder.getOpFeedEntrustOrderId());
//            // 计算执行期限
//            calculateExecutionPeriod(calculate, bsWorkdayConfigMapper);
//            // 修改
//            opFeedEntrustOrderMapper.updateExecutionPeriod(calculate.getExecutionPeriod(),calculate.getOpFeedEntrustOrderId());
//        }
//
//
//
//
//
//
//        return count;
//    }

    private void updateBussinessTableSampleId(String oldSampleId, String newSampleId) {
        feedReportBaseMapper.updateSampleId(oldSampleId,newSampleId);
        feedResultInfoMapper.updateSampleId(oldSampleId,newSampleId);

    }
    private String formatDate(Date date) {
        if (date == null) return "";
        // 使用 Hutool 或 SimpleDateFormat
        return DateUtils.parseDateToStr("yyyy-MM-dd", date);
    }
    /**
     * 通用对比并记录日志方法 (优化版：支持数值智能比对)
     */
    private void compareAndLog(String businessId, String type, String fieldKey, String fieldName,
                               String oldVal, String newVal, List<OpFeedEntrustOrderChangeLog> list, String user) {
        // 1. 处理 null 为空字符串，并去空格
        String o = oldVal == null ? "" : oldVal.trim();
        String n = newVal == null ? "" : newVal.trim();

        // 2. 尝试进行数值比对 (忽略精度差异，如 0.00 == 0)
        boolean isNumericEqual = false;
        // 简单判断是否像数字 (包含负号、数字、小数点)
        if (o.matches("-?\\d+(\\.\\d+)?") && n.matches("-?\\d+(\\.\\d+)?")) {
            try {
                BigDecimal b1 = new BigDecimal(o);
                BigDecimal b2 = new BigDecimal(n);
                if (b1.compareTo(b2) == 0) {
                    isNumericEqual = true;
                } else {
                    // 如果数值不等，为了日志显示好看，可以选择去除尾随0 (可选)
                    // o = b1.stripTrailingZeros().toPlainString();
                    // n = b2.stripTrailingZeros().toPlainString();
                }
            } catch (NumberFormatException e) {
                // 转换失败，说明不是纯数字，忽略异常继续走字符串比对
            }
        }

        // 3. 如果数值相等，直接返回；否则进行字符串比对
        if (isNumericEqual) {
            return;
        }

        if (!o.equals(n)) {
            OpFeedEntrustOrderChangeLog log = new OpFeedEntrustOrderChangeLog();
            log.setLogId(IdUtils.simpleUUID());
            log.setBusinessId(businessId);
            log.setBusinessType(type);
            log.setFieldKey(fieldKey);
            log.setFieldName(fieldName);
            log.setOldValue(o);
            log.setNewValue(n);
            log.setCreateBy(user);
            list.add(log);
        }
    }

    /**
     * 将检测项目列表格式化为字符串用于对比
     */
    private String formatItems(List<OpFeedEntrustOrderItem> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }
        // 提取名称并排序，避免顺序不同导致误判
        return items.stream()
                .map(OpFeedEntrustOrderItem::getItemName) // 假设Item对象里有itemName字段，如果没有需要联查或者前端传
                .filter(StringUtils::isNotEmpty)
                .sorted()
                .collect(Collectors.joining(","));
    }

    private void updateBussinessTableItemId(String oldItemId, String newItemId) {
        feedResultInfoMapper.updateItemId(oldItemId,newItemId);

    }

    @Override
    public OpFeedEntrustOrder selectPrintOpFeedEntrustOrderByOpFeedEntrustOrderId(String opFeedEntrustOrderId) {
        OpFeedEntrustOrder opFeedEntrustOrder = opFeedEntrustOrderMapper.selectPrintOrderDetailById(opFeedEntrustOrderId);
        if (ObjectUtils.isEmpty(opFeedEntrustOrder)) {
            throw new RuntimeException("该委托单未查到检测方法为【化学法】物料");
        }

        return opFeedEntrustOrder;
    }

    /**
     * 判断字符串日期是否为当月最后一天
     *
     * @param dateStr  日期字符串（如："2023-12-31"）
     * @param pattern  日期格式（如："yyyy-MM-dd"）
     * @return true：是当月最后一天；false：不是；解析失败返回false
     */
    public static boolean isLastDayOfMonth(String dateStr, String pattern) {
        try {
            // 1. 解析字符串为LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate date = LocalDate.parse(dateStr, formatter);

            // 2. 计算当月最后一天
            int daysInMonth = date.lengthOfMonth(); // 当月总天数
            LocalDate lastDayOfMonth = date.withDayOfMonth(daysInMonth); // 当月最后一天

            // 3. 比较原日期与最后一天
            return date.equals(lastDayOfMonth);
        } catch (DateTimeParseException e) {
            // 日期格式解析失败（如字符串与pattern不匹配）
            System.err.println("日期解析失败：" + e.getMessage());
            return false;
        }
    }

    /**
     * 按原逻辑计算执行期限（送样日期当天不算，从下一天开始）
     */
    public void calculateExecutionPeriod(OpFeedEntrustOrder opFeedEntrustOrder, BsWorkdayConfigMapper bsWorkdayConfigMapper) {
        // 1. 校验测试服务等级
        String testingServiceLevel = opFeedEntrustOrder.getTestingServiceLevel();
        if (StringUtils.isEmpty(testingServiceLevel)) {
            throw new BusinessException("测试服务要求不能为空");
        }
        Integer requiredWorkdays = SERVICE_LEVEL_DAYS.get(testingServiceLevel);
        if (requiredWorkdays == null) {
            throw new BusinessException("未知的测试服务要求");
        }

        // TODO 开发暂时先写死送样日期，需要商议收养日期怎么弄
        // 2. 转换送样日期为LocalDate（原逻辑：送样日期当天不算，从下一天开始）
        Date receiveTime = opFeedEntrustOrder.getReceiveTime();
        LocalDate startDate = receiveTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .plusDays(1); // 关键：从收样日期的下一天开始计算

        // 3. 定义工作日配置提供者（按年月查配置）
        Function<LocalDate, Map<String, BsWorkdayConfig>> workdayConfigProvider = date -> {
            int year = date.getYear();
            int month = date.getMonthValue();
            List<BsWorkdayConfig> configs = bsWorkdayConfigMapper.selectWorkdayConfigByYearMonth(year, month);
            if (ObjectUtils.isEmpty(configs)) {
                throw new BusinessException("未配置" + year + "年" + month + "月的工作日信息");
            }
            return configs.stream()
                    .collect(Collectors.toMap(BsWorkdayConfig::getDateStr, config -> config));
        };

        // 4. 核心计算：按原逻辑推进日期（先+1天，再判断工作日）
        LocalDate executionDate = calculateTargetDate(startDate, requiredWorkdays, workdayConfigProvider);

        // 5. 转换回Date并设置
        opFeedEntrustOrder.setExecutionPeriod(
                Date.from(executionDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
    }

    /**
     * 严格按原逻辑计算目标日期：
     * 1. 从起始日期（送样日期的下一天）开始
     * 2. 每次循环先推进日期（原逻辑的addDays(1)）
     * 3. 判断当前日期是否为工作日，若是则减剩余天数
     * 4. 剩余天数为0时，当前日期即为结果
     */
    private LocalDate calculateTargetDate(LocalDate startDate, int requiredWorkdays,
                                          Function<LocalDate, Map<String, BsWorkdayConfig>> configProvider) {
        LocalDate currentDate = startDate; // 起始日期是送样日期的下一天
        int remainingDays = requiredWorkdays;
        Map<String, Map<String, BsWorkdayConfig>> monthConfigCache = new HashMap<>();

        while (remainingDays > 0) {
            // 获取当前日期所在月的配置（缓存避免重复查库）
            String yearMonth = currentDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
            LocalDate current = currentDate; // 解决lambda变量问题
            Map<String, BsWorkdayConfig> dayConfigs = monthConfigCache.computeIfAbsent(
                    yearMonth,
                    k -> configProvider.apply(current)
            );

            // 检查当前日期的配置是否存在
            String currentDateStr = currentDate.format(DATE_FORMATTER);
            BsWorkdayConfig currentConfig = dayConfigs.get(currentDateStr);
            if (currentConfig == null) {
                throw new BusinessException("工作日配置缺失：" + currentDateStr);
            }

            // 原逻辑：如果是工作日，剩余天数减1
            if (currentConfig.getIsWorkday() == 1) {
                remainingDays--;
                // 剩余天数为0时，当前日期即为执行期限
                if (remainingDays == 0) {
                    return currentDate;
                }
            }

            // 原逻辑：无论是否为工作日，推进到下一天（对应原代码的addDays(1)）
            currentDate = currentDate.plusDays(1);
        }

        return currentDate;
    }
    @Override
    public List<OpFeedEntrustOrderSample> selectJhwItemList(List<String> sampleIdList) {
        return feedEntrustOrderSampleMapper.selectJhwItemList(sampleIdList);
    }


}
