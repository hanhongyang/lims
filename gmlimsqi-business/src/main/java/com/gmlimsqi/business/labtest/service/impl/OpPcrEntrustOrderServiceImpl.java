package com.gmlimsqi.business.labtest.service.impl;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.basicdata.domain.LabtestItems;
import com.gmlimsqi.business.basicdata.mapper.LabtestItemsMapper;
import com.gmlimsqi.business.labtest.domain.*;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.dto.OpSampleReceiveDto;
import com.gmlimsqi.business.labtest.mapper.OpPcrEntrustOrderChangeLogMapper;
import com.gmlimsqi.business.labtest.mapper.OpPcrEntrustOrderItemMapper;
import com.gmlimsqi.business.labtest.mapper.OpPcrEntrustOrderSampleMapper;
import com.gmlimsqi.business.labtest.service.IOpSampleReceiveService;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.UserInfoProcessor;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.enums.EntrustOrderStatusEnum;
import com.gmlimsqi.common.enums.EntrustOrderTypeEnum;
import com.gmlimsqi.common.enums.YesNo2Enum;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.*;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.service.ISysConfigService;
import com.gmlimsqi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import com.gmlimsqi.business.labtest.mapper.OpPcrEntrustOrderMapper;
import com.gmlimsqi.business.labtest.service.IOpPcrEntrustOrderService;

import javax.servlet.http.HttpServletResponse;

/**
 * PCR样品委托单Service业务层处理
 * 
 * @author hhy
 * @date 2025-09-17
 */
@Service
public class OpPcrEntrustOrderServiceImpl implements IOpPcrEntrustOrderService 
{
    @Autowired
    private OpPcrEntrustOrderMapper opPcrEntrustOrderMapper;
    @Autowired
    private UserInfoProcessor userInfoProcessor;
    @Autowired
    private OpPcrEntrustOrderSampleMapper sampleMapper;
    @Autowired
    private OpPcrEntrustOrderItemMapper orderItemMapper;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;
    @Autowired
    private IOpSampleReceiveService sampleReceiveService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private LabtestItemsMapper itemMapper;
    @Value("${ruoyi.profile}/templates")
    private String templateBasePath;
    @Autowired
    private OpPcrEntrustOrderChangeLogMapper changeLogMapper;
    @Autowired
    private ISysConfigService configService;
    /**
     * 查询PCR样品委托单
     * 
     * @param opPcrEntrustOrderId PCR样品委托单主键
     * @return PCR样品委托单
     */

    public OpPcrEntrustOrder selectOpPcrEntrustOrderByOpPcrEntrustOrderId2(String opPcrEntrustOrderId)
    {
        return opPcrEntrustOrderMapper.selectOrderDetailById(opPcrEntrustOrderId);
    }

    /**
     * 通用对比并记录日志方法
     */
    private void compareAndLog(String businessId, String type, String fieldKey, String fieldName,
                               String oldVal, String newVal, List<OpPcrEntrustOrderChangeLog> list, String user) {
        String o = oldVal == null ? "" : oldVal.trim();
        String n = newVal == null ? "" : newVal.trim();

        // 简单数值比对兼容 (如 "1.00" == "1")
        if (o.matches("-?\\d+(\\.\\d+)?") && n.matches("-?\\d+(\\.\\d+)?")) {
            try {
                BigDecimal b1 = new BigDecimal(o);
                BigDecimal b2 = new BigDecimal(n);
                if (b1.compareTo(b2) == 0) return;
            } catch (Exception ignored) {}
        }

        if (!o.equals(n)) {
            OpPcrEntrustOrderChangeLog log = new OpPcrEntrustOrderChangeLog();
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

    private String formatDate(Date date) {
        if (date == null) return "";
        return DateUtils.parseDateToStr("yyyy-MM-dd", date);
    }


    /**
     * 查询PCR委托单详情（包含正常样品、删除样品、修改记录）
     */
    @Override
    public OpPcrEntrustOrder selectOpPcrEntrustOrderByOpPcrEntrustOrderId(String opPcrEntrustOrderId)
    {
        // 1. 查询主表和正常的样品 (is_delete = 0)
        OpPcrEntrustOrder order = opPcrEntrustOrderMapper.selectOrderDetailById(opPcrEntrustOrderId);

        if (order != null) {
            // 2. 查询已删除样品
            List<OpPcrEntrustOrderSample> deletedSamples = sampleMapper.selectDeletedSamplesByOrderId(opPcrEntrustOrderId);

            // ================== 核心过滤逻辑 Start ==================
            if (deletedSamples != null && !deletedSamples.isEmpty()) {
                // 获取配置的检测中心（接收方）部门ID
                String jczxDeptIdStr = configService.selectConfigByKey("jczx.deptId");

                Iterator<OpPcrEntrustOrderSample> iterator = deletedSamples.iterator();
                while (iterator.hasNext()) {
                    OpPcrEntrustOrderSample sample = iterator.next();
                    String deleteBy = sample.getUpdateBy(); // 获取执行删除操作的人

                    // 如果没有删除人信息，默认不显示，移除
                    if (StringUtils.isEmpty(deleteBy)) {
                        iterator.remove();
                        continue;
                    }

                    // 查询该用户的部门信息
                    SysUser user = sysUserService.selectUserById(Long.valueOf(deleteBy));

                    // 过滤逻辑：
                    // 如果查不到用户 OR 用户部门为空 OR 用户部门 != 检测中心部门ID
                    // 则认为是委托方（客户）自己删的，从列表中移除
                    if (user == null || user.getDeptId() == null ||
                            !String.valueOf(user.getDeptId()).equals(jczxDeptIdStr)) {
                        iterator.remove();
                    }
                }
            }
            // ================== 核心过滤逻辑 End ==================

            order.setDeletedSampleList(deletedSamples);

            // 2.1 填充已删除样品的项目信息 (只处理过滤后剩下的)
            if (deletedSamples != null && !deletedSamples.isEmpty()) {
                for (OpPcrEntrustOrderSample delSample : deletedSamples) {
                    List<OpPcrEntrustOrderItem> delItems = orderItemMapper.selectItemsBySampleIdIncludeDeleted(delSample.getOpPcrEntrustOrderSampleId());
                    delSample.setTestItem(delItems);
                }
            }

            // 3. 查询修改日志
            List<String> allIds = new ArrayList<>();
            allIds.add(order.getOpPcrEntrustOrderId()); // 订单ID
            // 正常样品
            if (order.getSampleList() != null) {
                order.getSampleList().forEach(s -> allIds.add(s.getOpPcrEntrustOrderSampleId()));
            }
            // 过滤后的已删除样品
            if (deletedSamples != null) {
                deletedSamples.forEach(s -> allIds.add(s.getOpPcrEntrustOrderSampleId()));
            }

            if (!allIds.isEmpty()) {
                List<OpPcrEntrustOrderChangeLog> logs = changeLogMapper.selectLogsByBusinessIds(allIds);
                order.setChangeLogs(logs);
            }
        }
        return order;
    }
    /**
     * 查询PCR样品委托单列表
     * 
     * @param opPcrEntrustOrder PCR样品委托单
     * @return PCR样品委托单
     */
    @Override
    public List<OpPcrEntrustOrder> selectOpPcrEntrustOrderList(OpPcrEntrustOrder opPcrEntrustOrder)
    {
        //检测中心看到所有委托单，牧场只看自己委托单
        String jczxDeptId = configService.selectConfigByKey("jczx.deptId");
        if(jczxDeptId.equals(SecurityUtils.getDeptId().toString())) {

        }else {
            opPcrEntrustOrder.setEntrustDeptId(SecurityUtils.getDeptId());
        }
        // 处理结束日期，设置为当天的23:59:59
        if (opPcrEntrustOrder.getSendSampleDateEnd() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(opPcrEntrustOrder.getSendSampleDateEnd());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            opPcrEntrustOrder.setSendSampleDateEnd(calendar.getTime());
        }

        List<OpPcrEntrustOrder> items = opPcrEntrustOrderMapper.selectOpPcrEntrustOrderList(opPcrEntrustOrder);

        // 批量处理用户名（创建人和更新人）
        userInfoProcessor.processBaseEntityUserInfo(items);

        return items;
    }

    /**
     * 新增PCR样品委托单
     * 
     * @param opPcrEntrustOrder PCR样品委托单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertOpPcrEntrustOrder(OpPcrEntrustOrder opPcrEntrustOrder){
        opPcrEntrustOrder.setOpPcrEntrustOrderId(IdUtils.simpleUUID());
        // 自动填充创建/更新信息
        opPcrEntrustOrder.fillCreateInfo();
        // 根据 isSubmit 决定初始状态
        if (Boolean.TRUE.equals(opPcrEntrustOrder.getIsSubmit())) {
            opPcrEntrustOrder.setStatus(EntrustOrderStatusEnum.DSL.getCode()); // 1
        } else {
            opPcrEntrustOrder.setStatus(EntrustOrderStatusEnum.DTJ.getCode()); // 0
        }
        try {
            String orderNo = codeGeneratorUtil.generatePcrEntrustOrderNo();
            opPcrEntrustOrder.setEntrustOrderNo(orderNo);
        } catch (BusinessException e) {
            throw new BusinessException("生成委托单编号失败: " + e.getMessage());
        }
        if(!CollectionUtil.isEmpty(opPcrEntrustOrder.getSampleList())){
            for (OpPcrEntrustOrderSample opPcrEntrustOrderSample : opPcrEntrustOrder.getSampleList()){

                try {
//                    String sampleNo = codeGeneratorUtil.generatePcrSampleNo();
//                    opPcrEntrustOrderSample.setSampleNo(sampleNo);
                    opPcrEntrustOrderSample.setOpPcrEntrustOrderSampleId(IdUtils.simpleUUID());
                    opPcrEntrustOrderSample.setPcrEntrustOrderId(opPcrEntrustOrder.getOpPcrEntrustOrderId());

                    opPcrEntrustOrderSample.fillCreateInfo();
                    sampleMapper.insertOpPcrEntrustOrderSample(opPcrEntrustOrderSample);

                    //根据检测项目类型匹配项目
                    if(StringUtils.isEmpty(opPcrEntrustOrderSample.getPcrTaskItemType())){
                        throw new BusinessException(opPcrEntrustOrderSample.getName()+"检测项目为空:");
                    }else {
                        List<OpPcrEntrustOrderItem> itemList = new ArrayList<>();
                        LabtestItems queryItem = new LabtestItems();
                        queryItem.setTag(opPcrEntrustOrderSample.getPcrTaskItemType());
                        queryItem.setIsDelete(YesNo2Enum.NO.getCode());
                        queryItem.setIsEnable(YesNo2Enum.YES.getCode());
                        List<LabtestItems> labtestItems = itemMapper.selectBsLabtestItemsList(queryItem);
                        for (LabtestItems labtestItem : labtestItems) {
                            OpPcrEntrustOrderItem item = new OpPcrEntrustOrderItem();
                            item.setOpPcrEntrustOrderItemId(IdUtils.simpleUUID());
                            item.setPcrEntrustOrderSampleId(opPcrEntrustOrderSample.getOpPcrEntrustOrderSampleId());
                            item.setItemId(labtestItem.getLabtestItemsId());
                            item.fillCreateInfo();
                            itemList.add(item);
                        }

                        if (!itemList.isEmpty()) {
                            orderItemMapper.insertBatch(itemList);
                        }
                    }

                } catch (BusinessException e) {
                    throw new BusinessException("生成样品编号失败: " + e.getMessage());
                }

            }
        }


        // 自动填充创建/更新信息
        opPcrEntrustOrder.fillCreateInfo();
        int rows = opPcrEntrustOrderMapper.insertOpPcrEntrustOrder(opPcrEntrustOrder);
        return rows;
    }



    /**
     * 修改PCR样品委托单
     * 
     * @param opPcrEntrustOrder PCR样品委托单
     * @return 结果
     */
//    @Transactional
//    //@Override
//    public int updateOpPcrEntrustOrder2(OpPcrEntrustOrder opPcrEntrustOrder)
//    {
//        //如果状态不是待处理，则不允许修改
//        OpPcrEntrustOrder selectOrder = opPcrEntrustOrderMapper.selectOpPcrEntrustOrderByOpPcrEntrustOrderId(opPcrEntrustOrder.getOpPcrEntrustOrderId());
//        if(selectOrder==null){
//            throw new RuntimeException("委托单不存在");
//        }
//        if(!EntrustOrderStatusEnum.DSL.getCode().equals(selectOrder.getStatus()) &&
//                !EntrustOrderStatusEnum.YBH.getCode().equals(selectOrder.getStatus())){
//            throw new RuntimeException("委托单已受理，不允许修改");
//        }
//        // 自动填充更新信息
//        opPcrEntrustOrder.fillUpdateInfo();
//        //更新子表删除标志并插入
//        orderItemMapper.updateDeleteByOrderId(opPcrEntrustOrder.getUpdateBy(),opPcrEntrustOrder.getOpPcrEntrustOrderId());
//        sampleMapper.updateDeleteByOrderId(opPcrEntrustOrder.getUpdateBy(),opPcrEntrustOrder.getOpPcrEntrustOrderId());
//        if(!CollectionUtil.isEmpty(opPcrEntrustOrder.getSampleList())){
//            for (OpPcrEntrustOrderSample opPcrEntrustOrderSample : opPcrEntrustOrder.getSampleList()){
////                String sampleNo = codeGeneratorUtil.generatePcrSampleNo();
////                opPcrEntrustOrderSample.setSampleNo(sampleNo);
//                opPcrEntrustOrderSample.setOpPcrEntrustOrderSampleId(IdUtils.simpleUUID());
//                opPcrEntrustOrderSample.setPcrEntrustOrderId(opPcrEntrustOrder.getOpPcrEntrustOrderId());
//                opPcrEntrustOrderSample.fillCreateInfo();
//                sampleMapper.insertOpPcrEntrustOrderSample(opPcrEntrustOrderSample);
//                if(!CollectionUtil.isEmpty(opPcrEntrustOrderSample.getTestItem())){
//                    List<OpPcrEntrustOrderItem> itemList = new ArrayList<>();
//                    for (OpPcrEntrustOrderItem testItem : opPcrEntrustOrderSample.getTestItem()) {
//                        OpPcrEntrustOrderItem item = new OpPcrEntrustOrderItem();
//                        item.setOpPcrEntrustOrderItemId(IdUtils.simpleUUID());
//                        item.setPcrEntrustOrderSampleId(opPcrEntrustOrderSample.getOpPcrEntrustOrderSampleId());
//                        item.setItemId(testItem.getItemId());
//                        item.setItemName(testItem.getItemName());
//                        item.fillCreateInfo();
//                        itemList.add(item);
//                    }
//                    if (!itemList.isEmpty()) {
//                        orderItemMapper.insertBatch(itemList);
//                    }
//                }
//            }
//        }
//        // 提交时修改为未退回状态
//        opPcrEntrustOrder.setIsReturn("0");
//        opPcrEntrustOrder.setStatus(EntrustOrderStatusEnum.DSL.getCode());
//        int count =  opPcrEntrustOrderMapper.updateOpPcrEntrustOrder(opPcrEntrustOrder);
//        //接收
//        if(YesNo2Enum.YES.getCode().equals(opPcrEntrustOrder.getIsReceive())){
//            OpSampleReceiveDto dto = new OpSampleReceiveDto();
//            dto.setType(EntrustOrderTypeEnum.PCR.getCode());
//            String[] sampleIds = opPcrEntrustOrder.getSampleList().
//                    stream().map(OpPcrEntrustOrderSample :: getOpPcrEntrustOrderSampleId).toArray(String[]::new);
//            dto.setSampleIds(sampleIds);
//            sampleReceiveService.add(dto);
//        }
//        return count;
//    }

    /**
     * 修改PCR委托单（带日志记录和软删除）
     */
    @Override
    @Transactional
    public int updateOpPcrEntrustOrder(OpPcrEntrustOrder opPcrEntrustOrder) {
        String orderId = opPcrEntrustOrder.getOpPcrEntrustOrderId();

        // 1. 获取数据库中的旧数据全貌 (包含样品和项目)
        OpPcrEntrustOrder oldFullOrder = opPcrEntrustOrderMapper.selectOrderDetailById(orderId);
        if (oldFullOrder == null) {
            throw new RuntimeException("委托单不存在");
        }

        // 如果是“提交/撤回”操作（isSubmit != null），则严格校验状态；
        // 如果是“仅保存”操作（isSubmit == null，通常是接收方修改），则允许修改。
        if (opPcrEntrustOrder.getIsSubmit() != null) {
            // 如果试图改变状态（提交或存草稿），必须确保当前是 待提交(0) 或 已驳回(6)
            if (!EntrustOrderStatusEnum.DTJ.getCode().equals(oldFullOrder.getStatus()) &&
                    !EntrustOrderStatusEnum.YBH.getCode().equals(oldFullOrder.getStatus())) {
                throw new RuntimeException("当前委托单已提交或受理，不允许修改状态，请先撤回");
            }
        }// else: 如果 isSubmit 为 null，说明是接收方或管理员在修改业务数据，不做状态拦截（

        // 将旧样品转为 Map，方便按 ID 快速查找
        Map<String, OpPcrEntrustOrderSample> oldSampleMap = new HashMap<>();
        if (oldFullOrder.getSampleList() != null) {
            for (OpPcrEntrustOrderSample s : oldFullOrder.getSampleList()) {
                oldSampleMap.put(s.getOpPcrEntrustOrderSampleId(), s);
            }
        }

        // 2. 准备日志集合与当前用户
        List<OpPcrEntrustOrderChangeLog> logList = new ArrayList<>();
        String currentUsername = SecurityUtils.getUsername();

        // 3. 对比并更新主表字段
        // 送检单位
        compareAndLog(orderId, "ORDER", "entrustDeptName", "送检单位",
                oldFullOrder.getEntrustDeptName(), opPcrEntrustOrder.getEntrustDeptName(), logList, currentUsername);

        // 送检时间 (送样日期)
        compareAndLog(orderId, "ORDER", "sendSampleDate", "送检时间",
                formatDate(oldFullOrder.getSendSampleDate()), formatDate(opPcrEntrustOrder.getSendSampleDate()), logList, currentUsername);

        // 地址 (报告寄送地址)
        compareAndLog(orderId, "ORDER", "address", "地址",
                oldFullOrder.getAddress(), opPcrEntrustOrder.getAddress(), logList, currentUsername);

        // 送样人 (联系人/委托人)
        compareAndLog(orderId, "ORDER", "entrustContact", "送样人",
                oldFullOrder.getEntrustContact(), opPcrEntrustOrder.getEntrustContact(), logList, currentUsername);
        // 如果是接收操作（isReceive=Y），计算本次操作删除的样品数量并记录
        if (YesNo2Enum.YES.getCode().equals(opPcrEntrustOrder.getIsReceive())) {
            long deleteCount = 0;
            // 确保旧数据存在 (oldSampleMap 在方法开头已构建)
            if (oldSampleMap != null && !oldSampleMap.isEmpty()) {
                // 1. 获取前端本次提交的所有有效样品ID集合
                Set<String> inputIds = new HashSet<>();
                List<OpPcrEntrustOrderSample> inputSampleList = opPcrEntrustOrder.getSampleList();
                if (inputSampleList != null) {
                    inputSampleList.stream()
                            .map(OpPcrEntrustOrderSample::getOpPcrEntrustOrderSampleId)
                            .filter(StringUtils::isNotEmpty)
                            .forEach(inputIds::add);
                }

                // 2. 遍历旧数据，如果在提交的ID集合中不存在，则视为本次被删除
                for (String oldId : oldSampleMap.keySet()) {
                    if (!inputIds.contains(oldId)) {
                        deleteCount++;
                    }
                }
            }
            // 3. 设置删除数量字段
            opPcrEntrustOrder.setScypsl((int)deleteCount);
        }

        opPcrEntrustOrder.fillUpdateInfo();

        // 只有当 isSubmit 明确不为 null 时，才进行状态流转
        if (opPcrEntrustOrder.getIsSubmit() != null) {
            if (Boolean.TRUE.equals(opPcrEntrustOrder.getIsSubmit())) {
                // 动作：提交 -> 变更为 待受理(1)
                opPcrEntrustOrder.setStatus(EntrustOrderStatusEnum.DSL.getCode());
                // 提交时重置退回状态
                opPcrEntrustOrder.setIsReturn("0");
            } else {
                // 动作：存草稿 -> 变更为 待提交(0)
                opPcrEntrustOrder.setStatus(EntrustOrderStatusEnum.DTJ.getCode());
            }
        }
        // 如果 isSubmit 为 null，则完全不设置 status 字段，MyBatis 会忽略该字段的更新，从而保持原状态（如检测中、已受理等）


        int count = opPcrEntrustOrderMapper.updateOpPcrEntrustOrder(opPcrEntrustOrder);

        // ================== 4. 样品处理 ==================
        Set<String> processedIds = new HashSet<>();

        if (opPcrEntrustOrder.getSampleList() != null) {
            for (OpPcrEntrustOrderSample sample : opPcrEntrustOrder.getSampleList()) {
                String inputId = sample.getOpPcrEntrustOrderSampleId();

                // --- Case A: 修改 (Update) ---
                if (StringUtils.isNotEmpty(inputId) && oldSampleMap.containsKey(inputId)) {
                    OpPcrEntrustOrderSample oldSample = oldSampleMap.get(inputId);
                    processedIds.add(inputId);

                    // A1. 记录修改日志
                    // 物料 (对应 invbillName)
                    compareAndLog(inputId, "SAMPLE", "invbillName", "物料",
                            oldSample.getInvbillName(), sample.getInvbillName(), logList, currentUsername);

                    // 样品描述 (对应 sampleName)
                    compareAndLog(inputId, "SAMPLE", "name", "样品描述",
                            oldSample.getName(), sample.getName(), logList, currentUsername);

                    // 备注
                    compareAndLog(inputId, "SAMPLE", "remark", "备注",
                            oldSample.getRemark(), sample.getRemark(), logList, currentUsername);

                    // 检测项目比对 (List 转 String)
                    String oldItemsStr = DictUtils.getDictLabel("pcr_task_item_type",oldSample.getPcrTaskItemType());
                    String newItemsStr = DictUtils.getDictLabel("pcr_task_item_type",sample.getPcrTaskItemType());
                    compareAndLog(inputId, "SAMPLE", "pcrTaskItemType", "检测项目",
                            oldItemsStr, newItemsStr, logList, currentUsername);

                    // A2. 更新样品
                    sample.fillUpdateInfo();
                    sampleMapper.updateOpPcrEntrustOrderSample(sample);

                    // A3. 处理检测项目 (简单策略：删旧插新)
                    // 假设你有 pcrEntrustOrderItemMapper
                    if (sample.getTestItem() != null) {
                        orderItemMapper.deleteBySampleId(inputId,SecurityUtils.getUserId().toString()); // 物理删或软删均可，视业务定
                        for (OpPcrEntrustOrderItem item : sample.getTestItem()) {
                            item.setOpPcrEntrustOrderItemId(IdUtils.simpleUUID());
                            item.setPcrEntrustOrderSampleId(inputId);
                            item.fillCreateInfo();
                            orderItemMapper.insertOpPcrEntrustOrderItem(item);
                        }
                    }
                }
                // --- Case B: 新增 (Insert) ---
                else {
                    sample.setPcrEntrustOrderId(orderId);
                    if (StringUtils.isEmpty(sample.getOpPcrEntrustOrderSampleId())) {
                        sample.setOpPcrEntrustOrderSampleId(IdUtils.simpleUUID());
                    }
                    sample.fillCreateInfo();
                    sampleMapper.insertOpPcrEntrustOrderSample(sample);

                    // 插入新项目
                    if (sample.getTestItem() != null) {
                        for (OpPcrEntrustOrderItem item : sample.getTestItem()) {
                            item.setOpPcrEntrustOrderItemId(IdUtils.simpleUUID());
                            item.setPcrEntrustOrderSampleId(sample.getOpPcrEntrustOrderSampleId());
                            item.fillCreateInfo();
                            orderItemMapper.insertOpPcrEntrustOrderItem(item);
                        }
                    }
                }
            }
        }

        // ================== 5. 处理软删除的样品 ==================
        for (String oldId : oldSampleMap.keySet()) {
            if (!processedIds.contains(oldId)) {
                // 执行软删除
                OpPcrEntrustOrderSample delSample = new OpPcrEntrustOrderSample();
                delSample.setOpPcrEntrustOrderSampleId(oldId);
                delSample.setIsDelete("1");
                delSample.setUpdateBy(opPcrEntrustOrder.getUpdateBy());
                delSample.setUpdateTime(new Date());
                sampleMapper.updateOpPcrEntrustOrderSample(delSample);

                // 同时软删除项目 (如果需要)
                // pcrEntrustOrderItemMapper.updateDeleteBySampleId(oldId);
            }
        }

        // 6. 批量插入日志
        if (!logList.isEmpty()) {
            changeLogMapper.insertBatch(logList);
        }

        //接收
        if(YesNo2Enum.YES.getCode().equals(opPcrEntrustOrder.getIsReceive())){
            OpSampleReceiveDto dto = new OpSampleReceiveDto();
            dto.setType(EntrustOrderTypeEnum.PCR.getCode());
            String[] sampleIds = opPcrEntrustOrder.getSampleList().
                    stream().map(OpPcrEntrustOrderSample :: getOpPcrEntrustOrderSampleId).toArray(String[]::new);
            dto.setSampleIds(sampleIds);
            sampleReceiveService.add(dto);
        }
        return count;
    }
    /**
     * 根据类型下载导入模板
     * (此版本不依赖 ServletUtils.writeAttachment)
     *
     * @param response HttpServletResponse
     * @param dto      包含 bloodTaskItemType 的 DTO
     * @return null (因为响应是直接写入的)
     */
    @Override
    public String downloadImportModel(HttpServletResponse response) {
        String fileName = "PCR样品导入模板.xlsx";
        // 2. 构造完整的文件路径
        String filePath = templateBasePath + File.separator + fileName;
        File file = new File(filePath);

        // 3. 检查文件是否存在
        if (!file.exists() || !file.canRead()) {
            //log.error("模板文件不存在或无法读取: {}", filePath);
            throw new ServiceException("指定的模板文件不存在: " + fileName);
        }

        // 4. 【核心修改】手动实现文件下载
        try {
            // A. 替换 FileUtils.readBytes(file)
            // 使用 Java 7+ (NIO) 的标准方法读取文件为字节数组
            byte[] bytes = Files.readAllBytes(file.toPath());

            // B. 替换 ServletUtils.writeAttachment(...)
            //    (I) 重置响应
            response.reset();

            //    (II) 设置响应头 (针对 .xlsx 文件)
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");

            //    (III) 使用您已有的 ServletUtils.urlEncode 方法来处理中文文件名
            //          (您贴出的 ServletUtils.java 源码中包含了这个方法)
            String encodedFileName = ServletUtils.urlEncode(fileName);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

            //    (IV) 手动将字节流写入响应
            //         使用 try-with-resources 语法，确保流自动关闭
            try (OutputStream os = response.getOutputStream()) {
                os.write(bytes);
                os.flush();
            }

        } catch (Exception e) {
            //log.error("下载模板文件失败: {}", filePath, e);
            // 如果异常发生在流写入之后，重置响应以防万一
            if (response.isCommitted()) {
                response.reset();
            }
            // 抛出业务异常，让全局异常处理器捕获
            throw new ServiceException("下载模板文件失败: " + e.getMessage());
        }

        // 5. 返回 null，因为响应已手动处理
        return null;
    }

    @Override
    public void withdrawOrder(String orderId) {
        OpPcrEntrustOrder opPcrEntrustOrder = opPcrEntrustOrderMapper.selectOpPcrEntrustOrderByOpPcrEntrustOrderId(orderId);
        if (opPcrEntrustOrder == null) throw new BusinessException("订单不存在");

        // 只有 待受理(1) 的单子可以撤回 (如果已经检测中，就不允许撤回了)
        if (!EntrustOrderStatusEnum.DSL.getCode().equals(opPcrEntrustOrder.getStatus())) {
            throw new BusinessException("当前状态不允许撤回（仅待受理状态可撤回）");
        }

        OpPcrEntrustOrder update = new OpPcrEntrustOrder();
        update.setOpPcrEntrustOrderId(orderId);
        update.setStatus(EntrustOrderStatusEnum.DTJ.getCode()); // 变回 0
        update.setUpdateBy(SecurityUtils.getUserId().toString());
        update.setUpdateTime(new Date());

        opPcrEntrustOrderMapper.updateOpPcrEntrustOrder(update);
    }
}
