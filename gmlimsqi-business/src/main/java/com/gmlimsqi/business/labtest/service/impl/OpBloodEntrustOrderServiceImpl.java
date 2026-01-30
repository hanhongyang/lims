package com.gmlimsqi.business.labtest.service.impl;


import com.gmlimsqi.business.basicdata.domain.LabtestItems;
import com.gmlimsqi.business.basicdata.mapper.LabtestItemsMapper;
import com.gmlimsqi.business.labtest.domain.*;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.dto.OpSampleReceiveDto;
import com.gmlimsqi.business.labtest.mapper.OpBloodEntrustOrderChangeLogMapper;
import com.gmlimsqi.business.labtest.mapper.OpBloodEntrustOrderItemMapper;
import com.gmlimsqi.business.labtest.mapper.OpBloodEntrustOrderMapper;
import com.gmlimsqi.business.labtest.mapper.OpBloodEntrustOrderSampleMapper;
import com.gmlimsqi.business.labtest.service.IOpBloodEntrustOrderService;
import com.gmlimsqi.business.labtest.service.IOpSampleReceiveService;
import com.gmlimsqi.business.labtest.vo.OpBloodEntrustVo;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.UserCacheService;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 血样样品委托单Service业务层处理
 *
 * @author hhy
 * @date 2025-09-20
 */
@Service
public class OpBloodEntrustOrderServiceImpl implements IOpBloodEntrustOrderService {
    @Autowired
    private OpBloodEntrustOrderMapper opBloodEntrustOrderMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private OpBloodEntrustOrderItemMapper opBloodEntrustOrderItemMapper;
    @Autowired
    private OpBloodEntrustOrderSampleMapper opBloodEntrustOrderSampleMapper;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;
    @Autowired
    private IOpSampleReceiveService sampleReceiveService;
    @Autowired
    private LabtestItemsMapper labtestItemsMapper;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private OpBloodEntrustOrderChangeLogMapper changeLogMapper;
    @Value("${ruoyi.profile}/templates")
    private String templateBasePath;

    /**
     * 查询血样样品委托单
     *
     * @param opBloodEntrustOrderId 血样样品委托单主键
     * @return 血样样品委托单
     */
    /**
     * 查询血样样品委托单
     *
     * @param opBloodEntrustOrderId 血样样品委托单主键
     * @return 血样样品委托单
     */
    @Override
    public OpBloodEntrustVo selectOpBloodEntrustOrderByOpBloodEntrustOrderId(String opBloodEntrustOrderId) {
        // 1. 查询主单信息
        OpBloodEntrustOrder opBloodEntrustOrder = opBloodEntrustOrderMapper.selectOpBloodEntrustOrderByOpBloodEntrustOrderId(opBloodEntrustOrderId);
        OpBloodEntrustVo opBloodEntrustVo = new OpBloodEntrustVo();

        if (!ObjectUtils.isEmpty(opBloodEntrustOrder)) {
            // 2. 查询正常数据 (未删除的)
            List<OpBloodEntrustOrderItem> opboi = opBloodEntrustOrderItemMapper.selectAllByOpBloodEntrustOrderId(opBloodEntrustOrderId);
            List<OpBloodEntrustOrderSample> opbos = opBloodEntrustOrderSampleMapper.selectAllByOpBloodEntrustOrderId(opBloodEntrustOrderId);

            BeanUtils.copyProperties(opBloodEntrustOrder, opBloodEntrustVo);
            if (!ObjectUtils.isEmpty(opboi)) {
                String itemCodeStr = opboi.stream()
                        .map(OpBloodEntrustOrderItem::getItemCode)
                        .filter(code -> code != null && !code.isEmpty())
                        .collect(Collectors.joining(","));
                // 这里的 get(0) 逻辑是你原有的，保持不变
                opBloodEntrustVo.setImmunityTime(opboi.get(0).getImmunityTime());
                opBloodEntrustVo.setItemTy(opboi.get(0).getItemTy());
                opBloodEntrustVo.setItemBbkt(opboi.get(0).getItemBbkt());
                opBloodEntrustVo.setItemCode(itemCodeStr);
            }
            opBloodEntrustVo.setSampleList(opbos);

            // ================== 新增逻辑 Start ==================

            // 3. 查询已删除样品
            List<OpBloodEntrustOrderSample> deletedSamples = opBloodEntrustOrderSampleMapper.selectDeletedSamplesByOrderId(opBloodEntrustOrderId);

            // 【核心修改】：过滤掉非接收方（检测中心）删除的样品
            if (deletedSamples != null && !deletedSamples.isEmpty()) {
                // 获取配置的检测中心部门ID
                String jczxDeptIdStr = configService.selectConfigByKey("jczx.deptId");

                Iterator<OpBloodEntrustOrderSample> iterator = deletedSamples.iterator();
                while (iterator.hasNext()) {
                    OpBloodEntrustOrderSample sample = iterator.next();
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
                    // 则认为是委托方自己删的，不显示在详情里
                    if (user == null || user.getDeptId() == null ||
                            !String.valueOf(user.getDeptId()).equals(jczxDeptIdStr)) {
                        iterator.remove();
                    }
                }
            }

            // 设置过滤后的已删除列表
            opBloodEntrustVo.setDeletedSampleList(deletedSamples);

            // 4. 查询修改日志
            List<String> allIds = new ArrayList<>();
            allIds.add(opBloodEntrustOrder.getOpBloodEntrustOrderId()); // 订单ID

            // 收集所有正常样品ID
            if (opbos != null) {
                opbos.forEach(s -> allIds.add(s.getOpBloodEntrustOrderSampleId()));
            }
            // 收集所有已删除样品ID (只查过滤后剩下的)
            if (deletedSamples != null) {
                deletedSamples.forEach(s -> allIds.add(s.getOpBloodEntrustOrderSampleId()));
            }

            if (!allIds.isEmpty()) {
                // 注意：这里需要确保 changeLogMapper 已经注入
                List<OpBloodEntrustOrderChangeLog> logs = changeLogMapper.selectLogsByBusinessIds(allIds);
                opBloodEntrustVo.setChangeLogs(logs);
            }
            // ================== 新增逻辑 End ==================
        }
        return opBloodEntrustVo;
    }

    /**
     * 查询血样样品委托单列表
     *
     * @param opBloodEntrustOrder 血样样品委托单
     * @return 血样样品委托单
     */
    @Override
    public List<OpBloodEntrustOrder> selectOpBloodEntrustOrderList(OpBloodEntrustOrder opBloodEntrustOrder) {
        //检测中心看到所有委托单，牧场只看自己委托单
        String jczxDeptId = configService.selectConfigByKey("jczx.deptId");
        if(jczxDeptId.equals(SecurityUtils.getDeptId().toString())) {

        }else {
            opBloodEntrustOrder.setEntrustDeptId(SecurityUtils.getDeptId());
        }

        List<OpBloodEntrustOrder> items = opBloodEntrustOrderMapper.selectOpBloodEntrustOrderList(opBloodEntrustOrder);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpBloodEntrustOrder::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增血样样品委托单
     *
     * @param opBloodEntrustVo 血样样品委托单
     * @return 结果
     */
    @Override
    @Transactional
    public int insertOpBloodEntrustOrder(OpBloodEntrustVo opBloodEntrustVo) {
        String id = IdUtils.simpleUUID();
        OpBloodEntrustOrder opblo = new OpBloodEntrustOrder();
        BeanUtils.copyProperties(opBloodEntrustVo, opblo);
        String itemCode = opBloodEntrustVo.getItemCode();
        if (ObjectUtils.isEmpty(itemCode)) {
            throw new RuntimeException("请选择检测项目");
        }
        // 根据 isSubmit 决定初始状态
        if (Boolean.TRUE.equals(opBloodEntrustVo.getIsSubmit())) {
            opblo.setStatus(EntrustOrderStatusEnum.DSL.getCode()); // 1
        } else {
            opblo.setStatus(EntrustOrderStatusEnum.DTJ.getCode()); // 0
        }
        opblo.setOpBloodEntrustOrderId(id);
        opblo.setEntrustOrderNo(codeGeneratorUtil.generateBloodEntrustOrderNo());
        opblo.setBloodTaskItemType(itemCode);

        //免疫时间
        String immunityTime = opBloodEntrustVo.getImmunityTime();
        //检测口蹄疫
        String itemTy = opBloodEntrustVo.getItemTy();
        //检测病原体
        String itemBbkt = opBloodEntrustVo.getItemBbkt();

        OpBloodEntrustOrderItem opbeitem = new OpBloodEntrustOrderItem();
        //保存字典值
        opbeitem.setBloodTaskItemType(itemCode);
        //字典值与检测项目项目匹配
        String diseaseTypeXy = DictUtils.getDictLabel("blood_task_item_type", itemCode);
        LabtestItems labtestItems = labtestItemsMapper.selectBsLabtestItemsByLabtestItemsName(diseaseTypeXy);
        opbeitem.setItemId(labtestItems.getLabtestItemsId());
        opbeitem.setItemCode(labtestItems.getItemCode());
        opbeitem.setItemName(labtestItems.getItemName());
        opbeitem.setOpBloodEntrustOrderItemId(IdUtils.simpleUUID());
        opbeitem.setOpBloodEntrustOrderId(id);
        opbeitem.setIsDelete("0");
        opbeitem.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        opbeitem.setCreateTime(DateUtils.getNowDate());
        opbeitem.setImmunityTime(immunityTime);
        opbeitem.setItemTy(itemTy);
        opbeitem.setItemBbkt(itemBbkt);
        opBloodEntrustOrderItemMapper.insertOpBloodEntrustOrderItem(opbeitem);


        //保存样品子表
        List<OpBloodEntrustOrderSample> opbeosampleList = opBloodEntrustVo.getSampleList();
        if (!ObjectUtils.isEmpty(opbeosampleList)) {
            for (OpBloodEntrustOrderSample opbeoSample : opbeosampleList) {
                if("4".equals(itemCode)){
                    opbeoSample.setBw(opbeoSample.getRemark());
                }
                opbeoSample.setOpBloodEntrustOrderId(id);
                opbeoSample.setBloodTaskItemType(itemCode);
                opbeoSample.setOpBloodEntrustOrderSampleId(IdUtils.simpleUUID());
                opbeoSample.fillCreateInfo();
                opBloodEntrustOrderSampleMapper.insertOpBloodEntrustOrderSample(opbeoSample);
            }
        }

        // 自动填充创建/更新信息
        opblo.fillCreateInfo();
        return opBloodEntrustOrderMapper.insertOpBloodEntrustOrder(opblo);
    }

    /**
     * 修改血样样品委托单
     *
     * @param opBloodEntrustVo 血样样品委托单
     * @return 结果
     */
   // @Override
//    @Transactional
//    public int updateOpBloodEntrustOrder2(OpBloodEntrustVo opBloodEntrustVo) {
//
//        String opBloodEntrustOrderId = opBloodEntrustVo.getOpBloodEntrustOrderId();
//        //如果状态不是待处理，则不允许修改
//        OpBloodEntrustOrder opBloodEntrustOrder = opBloodEntrustOrderMapper.selectOpBloodEntrustOrderByOpBloodEntrustOrderId(opBloodEntrustOrderId);
//        if(opBloodEntrustOrder==null){
//            throw new RuntimeException("委托单不存在");
//        }
//        if(!EntrustOrderStatusEnum.DSL.getCode().equals(opBloodEntrustOrder.getStatus()) &&
//                !EntrustOrderStatusEnum.YBH.getCode().equals(opBloodEntrustOrder.getStatus())){
//            throw new RuntimeException("委托单已受理，不允许修改");
//        }
//
//        OpBloodEntrustOrder opblo = new OpBloodEntrustOrder();
//        BeanUtils.copyProperties(opBloodEntrustVo, opblo);
//        opblo.setOpBloodEntrustOrderId(opBloodEntrustOrderId);
//        opblo.setEntrustOrderNo(opBloodEntrustVo.getEntrustOrderNo());
//        opblo.fillUpdateInfo();
//
//        // 提交时修改为未退回状态
//        opBloodEntrustOrder.setIsReturn("0");
//        opBloodEntrustOrder.setStatus(EntrustOrderStatusEnum.DSL.getCode());
//        int count = opBloodEntrustOrderMapper.updateOpBloodEntrustOrder(opblo);
//        //删除
//        opBloodEntrustOrderItemMapper.updateDeleteFlagByEntrustOrderId(opBloodEntrustOrderId, DateUtils.getNowDate(), SecurityUtils.getUserId().toString());
//        opBloodEntrustOrderSampleMapper.updateDeleteFlagByOrderSample(opBloodEntrustOrderId, DateUtils.getNowDate(), SecurityUtils.getUserId().toString());
//
//
//       // String itemCode = opBloodEntrustVo.getItemCode();
//        String itemCode = opBloodEntrustVo.getBloodTaskItemType();
//        //免疫时间
//        String immunityTime = opBloodEntrustVo.getImmunityTime();
//        //检测口蹄疫
//        String itemTy = opBloodEntrustVo.getItemTy();
//        //检测病原体
//        String itemBbkt = opBloodEntrustVo.getItemBbkt();
//
//        if (ObjectUtils.isEmpty(itemCode)) {
//            throw new RuntimeException("请选择检测项目");
//        }
//
//        OpBloodEntrustOrderItem opbeitem = new OpBloodEntrustOrderItem();
//        //保存字典值
//        opbeitem.setBloodTaskItemType(itemCode);
//        //字典值与检测项目项目匹配
//        String diseaseTypeXy = DictUtils.getDictLabel("blood_task_item_type", itemCode);
//        LabtestItems labtestItems = labtestItemsMapper.selectBsLabtestItemsByLabtestItemsName(diseaseTypeXy);
//        opbeitem.setItemId(labtestItems.getLabtestItemsId());
//        opbeitem.setItemCode(labtestItems.getItemCode());
//        opbeitem.setItemName(labtestItems.getItemName());
//        opbeitem.setOpBloodEntrustOrderItemId(IdUtils.simpleUUID());
//        opbeitem.setOpBloodEntrustOrderId(opBloodEntrustOrderId);
//        opbeitem.setIsDelete("0");
//        opbeitem.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
//        opbeitem.setCreateTime(DateUtils.getNowDate());
//        opbeitem.setImmunityTime(immunityTime);
//        opbeitem.setItemTy(itemTy);
//        opbeitem.setItemBbkt(itemBbkt);
//        opBloodEntrustOrderItemMapper.insertOpBloodEntrustOrderItem(opbeitem);
//
//
////        //保存检测项目子表
////        String[] itemCodes = itemCode.split(",");
////        for (String code : itemCodes) {
////            OpBloodEntrustOrderItem opbeitem = new OpBloodEntrustOrderItem();
////            opbeitem.setItemCode(code);
////            opbeitem.setOpBloodEntrustOrderItemId(IdUtils.simpleUUID());
////            opbeitem.setOpBloodEntrustOrderId(opBloodEntrustOrderId);
////            opbeitem.setImmunityTime(immunityTime);
////            opbeitem.setItemTy(itemTy);
////            opbeitem.setItemBbkt(itemBbkt);
////            opbeitem.fillCreateInfo();
////            opBloodEntrustOrderItemMapper.insertOpBloodEntrustOrderItem(opbeitem);
////        }
//
//        //保存样品子表
//        List<OpBloodEntrustOrderSample> opbeosampleList = opBloodEntrustVo.getSampleList();
//        if (!ObjectUtils.isEmpty(opbeosampleList)) {
//            for (OpBloodEntrustOrderSample opbeoSample : opbeosampleList) {
//                if("4".equals(itemCode)){
//                    opbeoSample.setBw(opbeoSample.getRemark());
//                }
//                opbeoSample.setBloodTaskItemType(itemCode);
//                opbeoSample.setOpBloodEntrustOrderId(opBloodEntrustOrderId);
//                opbeoSample.setOpBloodEntrustOrderSampleId(IdUtils.simpleUUID());
//                opbeoSample.fillCreateInfo();
//                opBloodEntrustOrderSampleMapper.insertOpBloodEntrustOrderSample(opbeoSample);
//            }
//        }
//        //接收
//        if (YesNo2Enum.YES.getCode().equals(opBloodEntrustVo.getIsReceive())) {
//            OpSampleReceiveDto dto = new OpSampleReceiveDto();
//            dto.setType(EntrustOrderTypeEnum.BLOOD.getCode());
//            String[] sampleIds = opBloodEntrustVo.getSampleList().
//                    stream().map(OpBloodEntrustOrderSample::getOpBloodEntrustOrderSampleId).toArray(String[]::new);
//            dto.setSampleIds(sampleIds);
//            sampleReceiveService.add(dto);
//        }
//
//        return count;
//    }
    /**
     * 修改血样样品委托单（带日志记录和软删除）
     */
    @Override
    @Transactional
    public int updateOpBloodEntrustOrder(OpBloodEntrustVo opBloodEntrustVo) {
        String orderId = opBloodEntrustVo.getOpBloodEntrustOrderId();

        // 1. 获取数据库中的旧数据全貌 (包含样品)
        OpBloodEntrustOrder oldFullOrder = opBloodEntrustOrderMapper.selectOpBloodEntrustOrderByOpBloodEntrustOrderId(orderId);
        if (oldFullOrder == null) {
            throw new RuntimeException("委托单不存在");
        }
        // 如果是“提交/撤回”操作（isSubmit != null），则严格校验状态；
        // 如果是“仅保存”操作（isSubmit == null，通常是接收方修改），则允许修改。
        if (opBloodEntrustVo.getIsSubmit() != null) {
            // 如果试图改变状态（提交或存草稿），必须确保当前是 待提交(0) 或 已驳回(6)
            if (!EntrustOrderStatusEnum.DTJ.getCode().equals(oldFullOrder.getStatus()) &&
                    !EntrustOrderStatusEnum.YBH.getCode().equals(oldFullOrder.getStatus())) {
                throw new RuntimeException("当前委托单已提交或受理，不允许修改状态，请先撤回");
            }
        }// else: 如果 isSubmit 为 null，说明是接收方或管理员在修改业务数据，不做状态拦截（

        // 查询旧的样品列表并转为 Map
        List<OpBloodEntrustOrderSample> oldSamples = opBloodEntrustOrderSampleMapper.selectAllByOpBloodEntrustOrderId(orderId);
        Map<String, OpBloodEntrustOrderSample> oldSampleMap = new HashMap<>();
        if (oldSamples != null) {
            for (OpBloodEntrustOrderSample s : oldSamples) {
                oldSampleMap.put(s.getOpBloodEntrustOrderSampleId(), s);
            }
        }

        // 2. 准备日志集合与当前用户
        List<OpBloodEntrustOrderChangeLog> logList = new ArrayList<>();
        String currentUsername = SecurityUtils.getUsername();

        // 3. 对比主表字段 (ORDER)
        // 检测项目 (bloodTaskItemType 对应前端 itemCode)
        String oldItemLabel = DictUtils.getDictLabel("blood_task_item_type", oldFullOrder.getBloodTaskItemType());
        String newItemLabel = DictUtils.getDictLabel("blood_task_item_type", opBloodEntrustVo.getBloodTaskItemType());
        compareAndLog(orderId, "ORDER", "itemCode", "检测项目", oldItemLabel, newItemLabel, logList, currentUsername);

        compareAndLog(orderId, "ORDER", "entrustDeptName", "送检单位", oldFullOrder.getEntrustDeptName(), opBloodEntrustVo.getEntrustDeptName(), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "sendSampleDate", "送检时间", formatDate(oldFullOrder.getSendSampleDate()), formatDate(opBloodEntrustVo.getSendSampleDate()), logList, currentUsername);
        compareAndLog(orderId, "ORDER", "address", "地址", oldFullOrder.getAddress(), opBloodEntrustVo.getAddress(), logList, currentUsername);

        // 送样人 (这里比较 ID 或 名字均可，建议前端传名字或后端转义，这里简单比对ID变化)
        compareAndLog(orderId, "ORDER", "sendSampleUserName", "送样人", oldFullOrder.getSendSampleUserName(), opBloodEntrustVo.getSendSampleUserName(), logList, currentUsername);

        // 更新主表数据
        OpBloodEntrustOrder opblo = new OpBloodEntrustOrder();
        BeanUtils.copyProperties(opBloodEntrustVo, opblo);
        opblo.setOpBloodEntrustOrderId(orderId);
        opblo.setBloodTaskItemType(opBloodEntrustVo.getBloodTaskItemType()); // 确保项目类型被更新
        opblo.setIsReturn("0");
        opblo.setStatus(EntrustOrderStatusEnum.DSL.getCode());
        opblo.fillUpdateInfo();
        // 只有当 isSubmit 明确不为 null 时，才进行状态流转
        if (opBloodEntrustVo.getIsSubmit() != null) {
            if (Boolean.TRUE.equals(opBloodEntrustVo.getIsSubmit())) {
                // 动作：提交 -> 变更为 待受理(1)
                opblo.setStatus(EntrustOrderStatusEnum.DSL.getCode());
                // 提交时重置退回状态
                opblo.setIsReturn("0");
            } else {
                // 动作：存草稿 -> 变更为 待提交(0)
                opblo.setStatus(EntrustOrderStatusEnum.DTJ.getCode());
            }
        }
        // 如果 isSubmit 为 null，则完全不设置 status 字段，MyBatis 会忽略该字段的更新，从而保持原状态（如检测中、已受理等）

        // 如果是接收操作（isReceive=Y），计算本次操作删除的样品数量并记录
        if (YesNo2Enum.YES.getCode().equals(opBloodEntrustVo.getIsReceive())) {
            int deleteCount = 0;
            // 确保旧数据存在
            if (oldSamples != null && !oldSamples.isEmpty()) {
                // 1. 获取前端本次提交的所有有效样品ID集合
                Set<String> inputIds = new HashSet<>();
                List<OpBloodEntrustOrderSample> inputSampleList = opBloodEntrustVo.getSampleList();
                if (inputSampleList != null) {
                    inputSampleList.stream()
                            .map(OpBloodEntrustOrderSample::getOpBloodEntrustOrderSampleId)
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
            // 3. 设置删除数量字段 (请确保实体类 OpBloodEntrustOrder 中已添加 scypsl 字段)
            opblo.setScypsl(deleteCount);
        }
        int count = opBloodEntrustOrderMapper.updateOpBloodEntrustOrder(opblo);

        // 4. 更新检测项目附表 (Item表) - 这里简化为删旧插新，因为Item表ID不关联业务日志
        opBloodEntrustOrderItemMapper.updateDeleteFlagByEntrustOrderId(orderId, DateUtils.getNowDate(), SecurityUtils.getUserId().toString());
        // 插入新项目逻辑 (保持原逻辑不变)
        insertOrderItems(opBloodEntrustVo, orderId);

        // ================== 5. 样品处理 (Upsert + Log) ==================
        Set<String> processedIds = new HashSet<>();
        List<OpBloodEntrustOrderSample> newSamples = opBloodEntrustVo.getSampleList();
        String newItemCode = opBloodEntrustVo.getBloodTaskItemType();

        if (newSamples != null) {
            for (OpBloodEntrustOrderSample sample : newSamples) {
                String inputId = sample.getOpBloodEntrustOrderSampleId();

                // --- Case A: 修改 (Update) ---
                if (StringUtils.isNotEmpty(inputId) && oldSampleMap.containsKey(inputId)) {
                    OpBloodEntrustOrderSample oldSample = oldSampleMap.get(inputId);
                    processedIds.add(inputId);

                    // A1. 记录样品修改日志
                    compareAndLog(inputId, "SAMPLE", "gh", "管号", oldSample.getGh(), sample.getGh(), logList, currentUsername);
                    compareAndLog(inputId, "SAMPLE", "sampleName", "牛号", oldSample.getSampleName(), sample.getSampleName(), logList, currentUsername);

                    // 类别 (需转字典 Label)
                    String oldType = DictUtils.getDictLabel("cattle_type", oldSample.getSampleType());
                    String newType = DictUtils.getDictLabel("cattle_type", sample.getSampleType());
                    compareAndLog(inputId, "SAMPLE", "sampleType", "类别", oldType, newType, logList, currentUsername);

                    compareAndLog(inputId, "SAMPLE", "remark", "备注", oldSample.getRemark(), sample.getRemark(), logList, currentUsername);

                    // 特殊字段：配种天数(早孕)、母牛号(BVDV)
                    compareAndLog(inputId, "SAMPLE", "pzts", "配种天数", oldSample.getPzts(), sample.getPzts(), logList, currentUsername);
                    compareAndLog(inputId, "SAMPLE", "mnh", "母牛号", oldSample.getMnh(), sample.getMnh(), logList, currentUsername);

                    // A2. 更新样品
                    if ("4".equals(newItemCode)) {
                        sample.setBw(sample.getRemark());
                    }
                    sample.setBloodTaskItemType(newItemCode);
                    sample.fillUpdateInfo();
                    opBloodEntrustOrderSampleMapper.updateOpBloodEntrustOrderSample(sample);
                }
                // --- Case B: 新增 (Insert) ---
                else {
                    if ("4".equals(newItemCode)) {
                        sample.setBw(sample.getRemark());
                    }
                    sample.setOpBloodEntrustOrderId(orderId);
                    sample.setBloodTaskItemType(newItemCode);
                    // 如果ID为空生成新ID，否则使用前端传的(如果有)
                    if(StringUtils.isEmpty(sample.getOpBloodEntrustOrderSampleId())) {
                        sample.setOpBloodEntrustOrderSampleId(IdUtils.simpleUUID());
                    }
                    sample.fillCreateInfo();
                    opBloodEntrustOrderSampleMapper.insertOpBloodEntrustOrderSample(sample);
                }
            }
        }

        // ================== 6. 处理软删除的样品 ==================
        for (String oldId : oldSampleMap.keySet()) {
            if (!processedIds.contains(oldId)) {
                // 执行软删除
                OpBloodEntrustOrderSample delSample = new OpBloodEntrustOrderSample();
                delSample.setOpBloodEntrustOrderSampleId(oldId);
                delSample.setIsDelete("1");
                delSample.setUpdateBy(SecurityUtils.getUserId().toString());
                delSample.setUpdateTime(new Date());
                opBloodEntrustOrderSampleMapper.updateOpBloodEntrustOrderSample(delSample);
            }
        }

        // 7. 批量插入日志
        if (!logList.isEmpty()) {
            changeLogMapper.insertBatch(logList);
        }

        // 8. 接收逻辑 (保持不变)
        if (YesNo2Enum.YES.getCode().equals(opBloodEntrustVo.getIsReceive())) {
            OpSampleReceiveDto dto = new OpSampleReceiveDto();
            dto.setType(EntrustOrderTypeEnum.BLOOD.getCode());
            // 重新获取该订单下所有正常的样品ID
            List<OpBloodEntrustOrderSample> currentSamples = opBloodEntrustOrderSampleMapper.selectAllByOpBloodEntrustOrderId(orderId);
            String[] sampleIds = currentSamples.stream()
                    .map(OpBloodEntrustOrderSample::getOpBloodEntrustOrderSampleId)
                    .toArray(String[]::new);
            dto.setSampleIds(sampleIds);
            sampleReceiveService.add(dto);
        }

        return count;
    }

    // 辅助方法：插入Order Items (提取自原代码)
    private void insertOrderItems(OpBloodEntrustVo vo, String orderId) {
        String itemCode = vo.getItemCode();
        String immunityTime = vo.getImmunityTime();
        String itemTy = vo.getItemTy();
        String itemBbkt = vo.getItemBbkt();

        OpBloodEntrustOrderItem opbeitem = new OpBloodEntrustOrderItem();
        opbeitem.setBloodTaskItemType(vo.getBloodTaskItemType());
        String diseaseTypeXy = DictUtils.getDictLabel("blood_task_item_type", itemCode);
        LabtestItems labtestItems = labtestItemsMapper.selectBsLabtestItemsByLabtestItemsName(diseaseTypeXy);

        if (labtestItems != null) {
            opbeitem.setItemId(labtestItems.getLabtestItemsId());
            opbeitem.setItemCode(labtestItems.getItemCode());
            opbeitem.setItemName(labtestItems.getItemName());
        }

        opbeitem.setOpBloodEntrustOrderItemId(IdUtils.simpleUUID());
        opbeitem.setOpBloodEntrustOrderId(orderId);
        opbeitem.setIsDelete("0");
        opbeitem.setItemCode(itemCode);
        opbeitem.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        opbeitem.setCreateTime(DateUtils.getNowDate());
        opbeitem.setImmunityTime(immunityTime);
        opbeitem.setItemTy(itemTy);
        opbeitem.setItemBbkt(itemBbkt);
        opBloodEntrustOrderItemMapper.insertOpBloodEntrustOrderItem(opbeitem);
    }

    // 辅助方法：通用比对
    private void compareAndLog(String businessId, String type, String fieldKey, String fieldName,
                               String oldVal, String newVal, List<OpBloodEntrustOrderChangeLog> list, String user) {
        String o = oldVal == null ? "" : oldVal.trim();
        String n = newVal == null ? "" : newVal.trim();
        if (!o.equals(n)) {
            OpBloodEntrustOrderChangeLog log = new OpBloodEntrustOrderChangeLog();
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
        return date == null ? "" : DateUtils.parseDateToStr("yyyy-MM-dd", date);
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
    public String downloadImportModel(HttpServletResponse response, OpJczxTestTaskDto dto) {
        String bloodTaskItemType = dto.getBloodTaskItemType();
        String fileName;

        // 1. 根据类型选择文件名
         if ("8".equals(bloodTaskItemType)) {
            fileName = "生化样品导入模板.xlsx";
        } else if ("9".equals(bloodTaskItemType)) {
            fileName = "早孕样品导入模板.xlsx";
        } else if ("4".equals(bloodTaskItemType)) {
             fileName = "BVDV抗原样品导入模板.xlsx";
        } else {
            fileName = "疫病样品导入模板.xlsx";
        }

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
        OpBloodEntrustOrder order = opBloodEntrustOrderMapper.selectOpBloodEntrustOrderByOpBloodEntrustOrderId(orderId);
        if (order == null) throw new BusinessException("订单不存在");

        // 只有 待受理(1) 的单子可以撤回 (如果已经检测中，就不允许撤回了)
        if (!EntrustOrderStatusEnum.DSL.getCode().equals(order.getStatus())) {
            throw new BusinessException("当前状态不允许撤回（仅待受理状态可撤回）");
        }

        OpBloodEntrustOrder update = new OpBloodEntrustOrder();
        update.setOpBloodEntrustOrderId(orderId);
        update.setStatus(EntrustOrderStatusEnum.DTJ.getCode()); // 变回 0
        update.setUpdateBy(SecurityUtils.getUserId().toString());
        update.setUpdateTime(new Date());

        opBloodEntrustOrderMapper.updateOpBloodEntrustOrder(update);
    }
}
