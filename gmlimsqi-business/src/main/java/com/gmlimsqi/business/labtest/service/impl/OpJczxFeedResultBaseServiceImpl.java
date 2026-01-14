package com.gmlimsqi.business.labtest.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.basicdata.domain.LabtestItems;
import com.gmlimsqi.business.basicdata.mapper.LabtestItemsMapper;
import com.gmlimsqi.business.labtest.domain.*;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedResultBaseDto;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedResultInfoDto;
import com.gmlimsqi.business.labtest.mapper.*;
import com.gmlimsqi.business.labtest.service.IOpJczxFeedResultBaseService;
import com.gmlimsqi.business.labtest.vo.OpJczxFeedResultInitVo;
import com.gmlimsqi.business.labtest.vo.OpJczxFeedResultVo;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.enums.EntrustOrderStatusEnum;
import com.gmlimsqi.common.enums.JczxFeedReportStatusEnum;
import com.gmlimsqi.common.enums.JczxFeedResultStatusEnum;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.utils.CollectionUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.domain.SysUploadFile;
import com.gmlimsqi.system.service.ISysUploadFileService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.gmlimsqi.business.util.CodeGeneratorUtil.CODE_TYPE_FEED_RESULT;

/**
 * 检测中心饲料检测结果基础Service业务层处理
 *
 * @author hhy
 * @date 2025-09-25
 */
@Service
public class OpJczxFeedResultBaseServiceImpl implements IOpJczxFeedResultBaseService
{
    @Autowired
    private OpJczxFeedResultBaseMapper opJczxFeedResultBaseMapper;
    @Autowired
    private OpJczxFeedReportBaseMapper opJczxFeedReportBaseMapper;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;
    @Autowired
    private OpJczxFeedResultInfoMapper infoMapper;
    @Autowired
    private OpFeedEntrustOrderItemMapper feedEntrustOrderItemMapper;
    @Autowired
    private OpFeedEntrustOrderSampleMapper feedEntrustOrderSampleMapper;
    @Autowired
    private OpFeedEntrustOrderMapper orderMapper;
    @Autowired
    private ISysUploadFileService sysUploadFileService;
    @Autowired
    OpJczxTestModelMapper jczxTestModelMapper;
    @Autowired
    LabtestItemsMapper itemMapper;
    @Autowired
    private OpEquipmentTempHumMonitorMapper opEquipmentTempHumMonitorMapper;

    /**
     * 查询检测中心饲料检测结果基础
     *
     * @param opJczxFeedResultBaseId 检测中心饲料检测结果基础主键
     * @return 检测中心饲料检测结果基础
     */
    @Override
    public OpJczxFeedResultVo selectOpJczxFeedResultBaseByOpJczxFeedResultBaseId(String opJczxFeedResultBaseId)
    {
        OpJczxFeedResultBase base = opJczxFeedResultBaseMapper.selectOpJczxFeedResultBaseByOpJczxFeedResultBaseId(opJczxFeedResultBaseId);
        OpJczxFeedResultVo vo = new OpJczxFeedResultVo();
        BeanUtils.copyProperties(base,vo);
        if(base!=null){
            List<OpJczxFeedResultInfo> infoList =  infoMapper.selectInfoListByBaseId(base.getOpJczxFeedResultBaseId());
            vo.setInfoList(infoList);
        }
//        //精度
//        LabtestItems labtestItems = itemMapper.selectBsLabtestItemsDetailByLabtestItemsId(base.getItemId());
//        if(labtestItems!=null ){
//            vo.setDecimalPlaces(labtestItems.getDecimalPlaces());
//            vo.setSignificantDigits(labtestItems.getSignificantDigits());
//        }
        return vo;
    }


    @Override
    public OpJczxFeedResultVo selectJhwInfoByBaseId(String opJczxFeedResultBaseId) {
        // 1. 查询基础信息
        OpJczxFeedResultBase base = opJczxFeedResultBaseMapper.selectOpJczxFeedResultBaseByOpJczxFeedResultBaseId(opJczxFeedResultBaseId);
        OpJczxFeedResultVo vo = new OpJczxFeedResultVo();
        BeanUtils.copyProperties(base, vo);

        if (base != null) {
            // 2. 查询所有详情记录 (扁平结构)
            List<OpJczxFeedResultInfo> rawInfoList = infoMapper.selectJhwInfoListByBaseId(base.getOpJczxFeedResultBaseId());

            if (CollectionUtil.isNotEmpty(rawInfoList)) {

                // 3. 按照样品ID (entrustOrderSampleId) 分组
                // 使用 sampleId 分组比 sampleNo 更安全，防止不同批次同名样品混淆
                Map<String, List<OpJczxFeedResultInfo>> groupBySample = rawInfoList.stream()
                        .filter(info -> StringUtils.isNotEmpty(info.getEntrustOrderSampleId()))
                        .collect(Collectors.groupingBy(OpJczxFeedResultInfo::getEntrustOrderSampleId));

                List<OpJczxFeedResultInfo> structuredList = new ArrayList<>();

                // 4. 遍历分组，构建父子结构
                for (Map.Entry<String, List<OpJczxFeedResultInfo>> entry : groupBySample.entrySet()) {
                    List<OpJczxFeedResultInfo> sameSampleItems = entry.getValue();

                    if (CollectionUtil.isNotEmpty(sameSampleItems)) {
                        // A. 构建父级对象 (样品层)
                        // 取第一条记录的样品信息作为父级基础
                        OpJczxFeedResultInfo firstItem = sameSampleItems.get(0);

                        OpJczxFeedResultInfo parentSample = new OpJczxFeedResultInfo();
                        // 复制样品共有属性
                        parentSample.setEntrustOrderSampleId(firstItem.getEntrustOrderSampleId()); // 注意VO里对应的字段名
                        parentSample.setSampleNo(firstItem.getSampleNo());
                        parentSample.setSampleName(firstItem.getSampleName());
                        parentSample.setEntrustOrderId(firstItem.getEntrustOrderId());
                        parentSample.setFileUrl(firstItem.getFileUrl());
                        parentSample.setFileId(firstItem.getFileId());
                        // ... 其他样品共有的字段 ...

                        // B. 构建子级列表 (项目层)
                        List<OpJczxFeedResultInfo> testItems = new ArrayList<>();
                        for (OpJczxFeedResultInfo item : sameSampleItems) {
                            OpJczxFeedResultInfo subItem = new OpJczxFeedResultInfo();

                            // 复制项目相关属性
                            subItem.setOpFeedEntrustOrderItemId(item.getEntrustOrderItemId()); // 注意VO字段映射
                            subItem.setItemId(item.getItemId());
                            subItem.setItemName(item.getItemName());
                            subItem.setItemCode(item.getItemCode());
                            subItem.setAverage(item.getAverage()); // 结果值
                            subItem.setRemark(item.getRemark());   // 备注

                            // 也可以保留主键ID方便更新
                            subItem.setOpJczxFeedResultInfoId(item.getOpJczxFeedResultInfoId());

                            testItems.add(subItem);
                        }

                        // C. 将子列表挂载到父对象
                        parentSample.setTestItem(testItems);

                        // D. 加入最终列表
                        structuredList.add(parentSample);
                    }
                }

                // 5. 排序 (可选: 按样品编号排序)
                structuredList.sort(Comparator.comparing(OpJczxFeedResultInfo::getSampleNo,
                        Comparator.nullsLast(String::compareTo)));

                // 6. 设置到VO的 infoList 中 (注意: 前端可能用 infoList 来接收这层结构)
                vo.setInfoList(structuredList);
                // 或者是 vo.setTestItem(structuredList); 取决于你前端想要那个字段
                // 根据你之前的JSON结构: "infoList":[{"testItem":[]}]，应该设到 infoList
            }
        }

        return vo;
    }

    @Override
    public void backToSubmit(OpJczxFeedResultBaseDto dto) {
        //更新状态
        OpJczxFeedResultBase update =  new OpJczxFeedResultBase();
        update.setOpJczxFeedResultBaseId(dto.getOpJczxFeedResultBaseId());
        update.setStatus(JczxFeedResultStatusEnum.TH.getCode());
        update.fillUpdateInfo();
        opJczxFeedResultBaseMapper.backToSubmit(update);
    }

    @Override
    public void jhwBackToSubmit(OpJczxFeedResultBaseDto dto) {
        //更新状态
        OpJczxFeedResultBase update =  new OpJczxFeedResultBase();
        update.setOpJczxFeedResultBaseId(dto.getOpJczxFeedResultBaseId());
        update.setStatus(JczxFeedResultStatusEnum.TH.getCode());
        update.fillUpdateInfo();
        opJczxFeedResultBaseMapper.backToSubmit(update);
    }

    @Override
    public OpJczxFeedResultVo getResultBySampleNo(OpJczxFeedResultBaseDto dto) {

        OpJczxFeedResultVo vo = new OpJczxFeedResultVo();
        List<OpJczxFeedResultInfo> infoList =  infoMapper.getResultBySampleNo(dto);
        // 过滤掉 isReset='1' 的数据
        List<OpJczxFeedResultInfo> filteredList = infoList.stream()
                .filter(info -> !"1".equals(info.getIsReset()))  // 排除 isReset='1'
                .collect(Collectors.toList());
        vo.setInfoList(filteredList);
        if(CollectionUtil.isNotEmpty(infoList)){
            OpJczxFeedResultBase base = opJczxFeedResultBaseMapper.selectOpJczxFeedResultBaseByOpJczxFeedResultBaseId(infoList.get(0).getBaseId());
            BeanUtils.copyProperties(base,vo);
            return vo;
        }else {
            return  null;
        }
    }

    @Override
    public OpJczxFeedResultVo getResultBySampleNo2(OpJczxFeedResultBaseDto dto) {

        OpJczxFeedResultVo vo = new OpJczxFeedResultVo();
        List<OpJczxFeedResultInfo> infoList =  infoMapper.getResultBySampleNo2(dto.getSampleNo());
        // 过滤掉 isReset='1' 的数据
        List<OpJczxFeedResultInfo> filteredList = infoList.stream()
                .filter(info -> !"1".equals(info.getIsReset()))  // 排除 isReset='1'
                .collect(Collectors.toList());
        vo.setInfoList(filteredList);
        if(CollectionUtil.isNotEmpty(infoList)){
            OpJczxFeedResultBase base = opJczxFeedResultBaseMapper.selectOpJczxFeedResultBaseByOpJczxFeedResultBaseId(infoList.get(0).getBaseId());
            BeanUtils.copyProperties(base,vo);
            return vo;
        }else {
            return  null;
        }
    }


    /**
     * 查询检测中心饲料检测结果基础列表
     *
     * @param dto 检测中心饲料检测结果基础
     * @return 检测中心饲料检测结果基础
     */
    @Override
    public List<OpJczxFeedResultBase> selectOpJczxFeedResultBaseList(OpJczxFeedResultBaseDto dto)
    {
        // 获取当前登录用户
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String nickName = user.getNickName();
        Long userId = user.getUserId();
        //待提交页签传StatusList=1，6
        if(!StringUtils.isEmpty(dto.getStatusList()) && dto.getStatusList().contains("1")){
            // 【待提交页签】
            // 逻辑：只看自己是检测人的数据
            // 状态过滤：保存(0/1) 和 退回(6)
            dto.setTestUser(nickName);
        }
        //待校对和已校对传参Status=2，Status=3
        if (JczxFeedResultStatusEnum.HYWC.getCode().equals(dto.getStatus())
                || JczxFeedResultStatusEnum.JDWC.getCode().equals(dto.getStatus())) {
            // 【待校对 / 已校对页签】
            // 逻辑：看自己检测的 OR 自己负责校对的项目

            // 1. 必须清空 testUser，否则上面的 SQL 中 `and b.test_user like` 会生效，导致只能看到自己检测的单据，看不到别人做完需要我校对的单据
            dto.setTestUser(null);

            // 2. 设置权限过滤参数
            dto.setPermissionType("check");       // 触发 XML 中的 permissionType == 'check'
            dto.setCurrentUserId(String.valueOf(userId)); // 用于查配置表
            dto.setCurrentUserNickName(nickName); // 用于匹配 b.test_user
        }
        List<OpJczxFeedResultBase> items = opJczxFeedResultBaseMapper.selectOpJczxFeedResultBaseList(dto);

        return items;
    }
    @Override
    public List<OpJczxFeedResultBase> selectJhwListList(OpJczxFeedResultBaseDto dto) {
        //待提交里只有检测人本人可以看到数据
        //待校对里谁都可以看到所有数据,只有近红外校对人才能看到校对按钮，详情按钮谁都可以看到.前端判断
        //已校对里谁都可以看到所有数据

        // 获取当前登录用户
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String nickName = user.getNickName();
        Long userId = user.getUserId();
        //待提交页签传StatusList=1，6
        if(!StringUtils.isEmpty(dto.getStatusList()) && dto.getStatusList().contains("1")){
            // 【待提交页签】
            // 逻辑：只看自己是检测人的数据
            // 状态过滤：保存(0/1) 和 退回(6)
            dto.setTestUser(nickName);
        }
        List<OpJczxFeedResultBase> items = opJczxFeedResultBaseMapper.selectJhwListList(dto);

        return items;
    }

    /**
     * 新增检测中心饲料检测结果基础
     *
     * @param dto 检测中心饲料检测结果基础
     * @return 结果
     */
    @Override
    @Transactional
    public String insertOpJczxFeedResultBase(OpJczxFeedResultBaseDto dto) throws Exception {
        OpJczxFeedResultBase base = new OpJczxFeedResultBase();
        BeanUtils.copyProperties(dto, base);
        if (StringUtils.isEmpty(dto.getOpJczxFeedResultBaseId())) {
            base.setOpJczxFeedResultBaseId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        base.fillCreateInfo();
        try {
            String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_FEED_RESULT);
            base.setResultNo(resultNo);
            base.setTestUser(SecurityUtils.getLoginUser().getUser().getNickName());
            base.setTestDate(new Date());
            base.setTestEndTime(new Date());
        } catch (BusinessException e) {
            throw new RuntimeException("生成化验单编号失败: " + e.getMessage());
        }

        List<OpJczxFeedResultInfo> infoList = new ArrayList<>();
        List<String> orderIdList = new ArrayList<>();
        // 2. 保存样品表
        for (OpJczxFeedResultInfoDto infoDto : dto.getInfoList()) {
            OpJczxFeedResultInfo info  = new OpJczxFeedResultInfo();
            BeanUtils.copyProperties(infoDto, info);
            if(StringUtils.isEmpty(info.getEntrustOrderItemId()) || StringUtils.isEmpty(info.getEntrustOrderSampleId())){
                throw new RuntimeException("缺少项目或样品");
            }
            OpFeedEntrustOrderItem orderItem = null;
            if(!StringUtils.isEmpty(dto.getModelNo()) && dto.getModelNo().contains("JJ-15")){

            }else {
                orderItem = feedEntrustOrderItemMapper.selectOpFeedEntrustOrderItemByOpFeedEntrustOrderItemId(info.getEntrustOrderItemId());
                if(orderItem == null){
                    throw new RuntimeException("待检项目不存在");
                }
            }

//            if(orderItem.getItemId()!=null && !orderItem.getItemId().equals(base.getItemId())){
//                throw new RuntimeException("项目不一致");
//            }
//            if(orderItem.getOpFeedEntrustOrderSampleId()!=null && !orderItem.getOpFeedEntrustOrderSampleId().equals(info.getEntrustOrderSampleId())){
//                throw new RuntimeException("样品不一致");
//            }
            OpFeedEntrustOrderSample orderSample = feedEntrustOrderSampleMapper.selectOpFeedEntrustOrderSampleByOpFeedEntrustOrderSampleId(info.getEntrustOrderSampleId());
            if(orderSample == null){
                throw new RuntimeException("待检样品不存在");
            }
            info.setEntrustOrderId(orderSample.getFeedEntrustOrderId());
            info.setBaseId(base.getOpJczxFeedResultBaseId());
            info.setIsReset("0");
            info.setSampleNo(orderSample.getSampleNo());
            info.setSampleName(orderSample.getName());
            info.setOpJczxFeedResultInfoId(IdUtils.simpleUUID());

            info.fillCreateInfo();
            infoList.add(info);
            orderIdList.add(orderSample.getFeedEntrustOrderId());

            //更新委托单项目表的检测人
            if(!StringUtils.isEmpty(dto.getModelNo()) && dto.getModelNo().contains("JJ-15")){
                //不更新项目表，因为初水分不在委托单项目之中，属于前置检测的，没有对应的orderItem信息。
            }else {
                OpFeedEntrustOrderItem orderItem1 = new OpFeedEntrustOrderItem();
                orderItem1.setOpFeedEntrustOrderItemId(orderItem.getOpFeedEntrustOrderItemId());
                orderItem1.setTestUserId(SecurityUtils.getUserId().toString());
                orderItem1.fillUpdateInfo();
                feedEntrustOrderItemMapper.updateOpFeedEntrustOrderItem(orderItem1);
            }
        }
        //把发起复检的info更新为
        infoMapper.batchInsertOpJczxFeedResultInfo(infoList);

        // 验证文件是否存在（如果传了文件ID）
        if (!StringUtils.isEmpty(dto.getTestUserSignature())) {
            SysUploadFile fileInfo = sysUploadFileService.getFileById(dto.getTestUserSignature());
            if (fileInfo == null) {
                throw new RuntimeException("签名文件不存在，文件ID: " + dto.getTestUserSignature());
            }
            // 标记文件为正式使用（从临时文件转为正式文件）
            sysUploadFileService.markFileAsPermanent(dto.getTestUserSignature());
            // 更新委托单状态
            //查询委托单下所有项目是否已完成
            if(!StringUtils.isEmpty(dto.getModelNo()) && dto.getModelNo().contains("JJ-15")){
                //不更新项目表
            }else {
                for (String orderId : orderIdList) {
                    int count = Integer.parseInt(orderMapper.selectNotEndCountById(orderId));
                    //全部完成
                    if (count == 0) {
                        orderMapper.updateStatusById(EntrustOrderStatusEnum.JCWC.getCode(), orderId);
                    }
                }
            }
        }
        int i = opJczxFeedResultBaseMapper.insertOpJczxFeedResultBase(base);
        if(i>0){
            return base.getOpJczxFeedResultBaseId();
        }else {
            return Integer.toString(i);
        }

    }

    /**
     * 修改检测中心饲料检测结果基础
     *
     * @param dto 检测中心饲料检测结果基础
     * @return 结果
     */
    @Override
    @Transactional
    public int updateOpJczxFeedResultBase (OpJczxFeedResultBaseDto dto) throws Exception
    {
        // 自动填充更新信息
        OpJczxFeedResultBase base = new OpJczxFeedResultBase();
        BeanUtils.copyProperties(dto, base);
        base.fillUpdateInfo();
        base.setTestEndTime(new Date());
        List<OpJczxFeedResultInfo> infoList = new ArrayList<>();
        List<String> orderIdList = new ArrayList<>();
        // 2. 保存样品表
        for (OpJczxFeedResultInfoDto infoDto : dto.getInfoList()) {
            OpJczxFeedResultInfo info  = new OpJczxFeedResultInfo();
            BeanUtils.copyProperties(infoDto, info);
            if(StringUtils.isEmpty(info.getEntrustOrderItemId()) || StringUtils.isEmpty(info.getEntrustOrderSampleId())){
                throw new RuntimeException("缺少项目或样品");
            }
            //OpFeedEntrustOrderItem orderItem = feedEntrustOrderItemMapper.selectOpFeedEntrustOrderItemByOpFeedEntrustOrderItemId(info.getEntrustOrderItemId());
//            if(orderItem == null){
//                throw new RuntimeException("待检项目不存在");
//            }
//            if(orderItem.getItemId()!=null && !orderItem.getItemId().equals(base.getItemId())){
//                throw new RuntimeException("项目不一致");
//            }
//            if(orderItem.getOpFeedEntrustOrderSampleId()!=null && !orderItem.getOpFeedEntrustOrderSampleId().equals(info.getEntrustOrderSampleId())){
//                throw new RuntimeException("样品不一致");
//            }
            OpFeedEntrustOrderSample orderSample = feedEntrustOrderSampleMapper.selectOpFeedEntrustOrderSampleByOpFeedEntrustOrderSampleId(info.getEntrustOrderSampleId());
            if(orderSample == null){
                throw new RuntimeException("待检样品不存在");
            }
            info.setEntrustOrderId(orderSample.getFeedEntrustOrderId());
            info.setBaseId(base.getOpJczxFeedResultBaseId());
            info.setSampleNo(orderSample.getSampleNo());
            info.setSampleName(orderSample.getName());
            info.fillCreateInfo();
            info.setOpJczxFeedResultInfoId(IdUtils.simpleUUID());
            infoList.add(info);
            orderIdList.add(orderSample.getFeedEntrustOrderId());
        }
        infoMapper.updateDeleteFlagByBaseId(SecurityUtils.getUserId().toString(),base.getOpJczxFeedResultBaseId());
        // 批量插入
        if (!infoList.isEmpty()) {
            infoMapper.batchInsertOpJczxFeedResultInfo(infoList);
        }
        // 验证文件是否存在（如果传了文件ID）
        if (!StringUtils.isEmpty(dto.getTestUserSignature())) {
            SysUploadFile fileInfo = sysUploadFileService.getFileById(dto.getTestUserSignature());
            if (fileInfo == null) {
                throw new RuntimeException("签名文件不存在，文件ID: " + dto.getTestUserSignature());
            }
            if(fileInfo.getStatus().equals("1")){
                // 标记文件为正式使用（从临时文件转为正式文件）
                sysUploadFileService.markFileAsPermanent(dto.getTestUserSignature());
            }
            // 更新委托单状态
            //查询委托单下所有项目是否已完成
            if(!StringUtils.isEmpty(dto.getModelNo()) && dto.getModelNo().contains("JJ-15")){
                //不更新项目表
            }else {
                for (String orderId : orderIdList) {
                    int count = Integer.parseInt(orderMapper.selectNotEndCountById(orderId));
                    //全部完成
                    if(count==0){
                        orderMapper.updateStatusById(EntrustOrderStatusEnum.JCWC.getCode(),orderId);
                    }
                }
            }

        }
        return opJczxFeedResultBaseMapper.updateOpJczxFeedResultBase(base);
    }


    /**
     * 新增检测中心饲料检测结果基础（近红外-层级结构拆分）
     *
     * @param dto 检测中心饲料检测结果基础
     * @return 结果
     */
    @Override
    @Transactional
    public String insertJhwResultBase(OpJczxFeedResultBaseDto dto) throws Exception {
        // 1. 保存基础表 (Base)
        OpJczxFeedResultBase base = new OpJczxFeedResultBase();
        BeanUtils.copyProperties(dto, base);
        if (StringUtils.isEmpty(dto.getOpJczxFeedResultBaseId())) {
            base.setOpJczxFeedResultBaseId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        base.fillCreateInfo();
        try {
            String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_FEED_RESULT);
            base.setResultNo(resultNo);
            base.setTestUser(SecurityUtils.getLoginUser().getUser().getNickName());
            // 如果DTO传了testDate用DTO的，没传用当前时间
            if (base.getTestDate() == null) base.setTestDate(new Date());
            base.setTestEndTime(new Date());
        } catch (BusinessException e) {
            throw new RuntimeException("生成化验单编号失败: " + e.getMessage());
        }

        List<OpJczxFeedResultInfo> flatInfoList = new ArrayList<>(); // 扁平化后的结果列表
        List<String> orderIdList = new ArrayList<>(); // 涉及的委托单ID集合（用于更新状态）

        // 2. 遍历外层 infoList (样品层级)
        if (CollectionUtil.isNotEmpty(dto.getInfoList())) {
            for (OpJczxFeedResultInfoDto sampleDto : dto.getInfoList()) {

                // 校验样品是否存在
                // JSON中的字段是 opFeedEntrustOrderSampleId
                String sampleId = sampleDto.getOpFeedEntrustOrderSampleId();
                if (StringUtils.isEmpty(sampleId)) {
                    throw new RuntimeException("样品ID不能为空");
                }

                OpFeedEntrustOrderSample orderSample = feedEntrustOrderSampleMapper.selectOpFeedEntrustOrderSampleByOpFeedEntrustOrderSampleId(sampleId);
                if (orderSample == null) {
                    throw new RuntimeException("待检样品不存在: " + sampleDto.getSampleNo());
                }

                // 收集委托单ID用于后续状态更新
                if (!orderIdList.contains(orderSample.getFeedEntrustOrderId())) {
                    orderIdList.add(orderSample.getFeedEntrustOrderId());
                }

                // 3. 遍历内层 testItem (项目层级)
                // 只有当样品下有项目结果时才保存
                if (CollectionUtil.isNotEmpty(sampleDto.getTestItem())) {
                    for (OpJczxFeedResultInfo itemDto : sampleDto.getTestItem()) {
                        OpJczxFeedResultInfo info = new OpJczxFeedResultInfo();

                        // --- A. 填充基础关联信息 ---
                        info.setOpJczxFeedResultInfoId(IdUtils.simpleUUID());
                        info.setBaseId(base.getOpJczxFeedResultBaseId()); // 关联主表
                        info.setEntrustOrderId(orderSample.getFeedEntrustOrderId()); // 关联委托单

                        // --- B. 填充样品信息 (来自外层 sampleDto 或 数据库查询) ---
                        info.setEntrustOrderSampleId(sampleId);
                        info.setSampleNo(orderSample.getSampleNo());
                        info.setSampleName(orderSample.getName());
                        // 如果前端传了sampleRemark等可以在这里set

                        // --- C. 填充项目信息 (来自内层 itemDto) ---
                        // JSON中的字段是 opFeedEntrustOrderItemId
                        String orderItemId = itemDto.getOpFeedEntrustOrderItemId();
                        if (StringUtils.isEmpty(orderItemId)) {
                            // 近红外可能存在没有对应委托单项目的情况（视业务而定），如果有则必须校验
                            // 这里假设必须有对应项目ID，或者根据itemCode匹配
                            // 按照你的描述，需要拆分保存，所以这里应该取itemDto里的数据
                        }

                        info.setEntrustOrderItemId(orderItemId);
                        //info.setItemId(itemDto.getItemId());
                        info.setItemName(itemDto.getItemName());
                        //info.setItemCode(itemDto.getItemCode());
                        // 核心结果数据：average (JSON中结果放在average字段)
                        info.setAverage(itemDto.getAverage());
                        // 备注
                        info.setRemark(itemDto.getRemark());
                        info.setFileId(sampleDto.getFileId());
                        info.setFileUrl(sampleDto.getFileUrl());
                        info.fillCreateInfo();
                        flatInfoList.add(info);

                        // --- D. 更新委托单项目表的检测人 (如果需要) ---
                        if (StringUtils.isNotEmpty(orderItemId)) {
                            OpFeedEntrustOrderItem updateItem = new OpFeedEntrustOrderItem();
                            updateItem.setOpFeedEntrustOrderItemId(orderItemId);
                            updateItem.setTestUserId(SecurityUtils.getUserId().toString());
                            updateItem.fillUpdateInfo();
                            feedEntrustOrderItemMapper.updateOpFeedEntrustOrderItem(updateItem);
                        }
                    }
                }
            }
        }

        // 4. 批量插入详情
        if (CollectionUtil.isNotEmpty(flatInfoList)) {
            infoMapper.batchInsertOpJczxFeedResultInfo(flatInfoList);
        }

        // 5. 处理签名文件和委托单状态
        if (!StringUtils.isEmpty(dto.getTestUserSignature())) {
            SysUploadFile fileInfo = sysUploadFileService.getFileById(dto.getTestUserSignature());
            if (fileInfo == null) {
                throw new RuntimeException("签名文件不存在，文件ID: " + dto.getTestUserSignature());
            }
            sysUploadFileService.markFileAsPermanent(dto.getTestUserSignature());

            // 更新委托单状态
            for (String orderId : orderIdList) {
                int count = Integer.parseInt(orderMapper.selectNotEndCountById(orderId));
                // 全部完成
                if (count == 0) {
                    orderMapper.updateStatusById(EntrustOrderStatusEnum.JCWC.getCode(), orderId);
                }
            }
        }

        int i = opJczxFeedResultBaseMapper.insertOpJczxFeedResultBase(base);
        if (i > 0) {
            return base.getOpJczxFeedResultBaseId();
        } else {
            return Integer.toString(i);
        }
    }

    /**
     * 修改检测中心饲料检测结果基础（近红外-层级结构拆分）
     *
     * @param dto 检测中心饲料检测结果基础
     * @return 结果
     */
    @Override
    @Transactional
    public int updateJhwResultBase(OpJczxFeedResultBaseDto dto) throws Exception {
        // 1. 更新基础表
        OpJczxFeedResultBase base = new OpJczxFeedResultBase();
        BeanUtils.copyProperties(dto, base);
        base.fillUpdateInfo();
        base.setTestEndTime(new Date());

        List<OpJczxFeedResultInfo> flatInfoList = new ArrayList<>();
        List<String> orderIdList = new ArrayList<>();

        // 2. 遍历外层 infoList (样品层级)
        if (CollectionUtil.isNotEmpty(dto.getInfoList())) {
            for (OpJczxFeedResultInfoDto sampleDto : dto.getInfoList()) {

                // 优先取 entrustOrderSampleId
                String sampleId = sampleDto.getEntrustOrderSampleId();

                // 如果为空，则取 opFeedEntrustOrderSampleId
                if (StringUtils.isEmpty(sampleId)) {
                    sampleId = sampleDto.getOpFeedEntrustOrderSampleId();
                }
                if (StringUtils.isEmpty(sampleId)) continue;

                OpFeedEntrustOrderSample orderSample = feedEntrustOrderSampleMapper.selectOpFeedEntrustOrderSampleByOpFeedEntrustOrderSampleId(sampleId);
                if (orderSample == null) {
                    throw new RuntimeException("待检样品不存在: " + sampleDto.getSampleNo());
                }

                if (!orderIdList.contains(orderSample.getFeedEntrustOrderId())) {
                    orderIdList.add(orderSample.getFeedEntrustOrderId());
                }

                // 3. 遍历内层 testItem (项目层级)
                if (CollectionUtil.isNotEmpty(sampleDto.getTestItem())) {
                    for (OpJczxFeedResultInfo itemDto : sampleDto.getTestItem()) {
                        OpJczxFeedResultInfo info = new OpJczxFeedResultInfo();

                        info.setOpJczxFeedResultInfoId(IdUtils.simpleUUID());
                        info.setBaseId(base.getOpJczxFeedResultBaseId());
                        info.setEntrustOrderId(orderSample.getFeedEntrustOrderId());

                        // 样品信息
                        info.setEntrustOrderSampleId(sampleId);
                        info.setSampleNo(orderSample.getSampleNo());
                        info.setSampleName(orderSample.getName());

                        // 项目信息
                        String orderItemId = itemDto.getOpFeedEntrustOrderItemId();
                        info.setEntrustOrderItemId(orderItemId);
                        //info.setItemId(itemDto.getItemId());
                        info.setItemName(itemDto.getItemName());
                        //info.setItemCode(itemDto.getItemCode());
                        // 结果值
                        info.setAverage(itemDto.getAverage());
                        // 备注
                        info.setRemark(itemDto.getRemark());
                        info.setFileId(sampleDto.getFileId());
                        info.setFileUrl(sampleDto.getFileUrl());

                        info.fillCreateInfo(); // 注意：虽然是修改Base，但Info是重新插入，所以是Create
                        flatInfoList.add(info);
                    }
                }
            }
        }

        // 4. 先逻辑删除旧详情，再插入新详情
        // 这里的逻辑是：全量覆盖。先将该BaseId下的所有Info标记为删除
        infoMapper.updateDeleteFlagByBaseId(SecurityUtils.getUserId().toString(), base.getOpJczxFeedResultBaseId());

        if (CollectionUtil.isNotEmpty(flatInfoList)) {
            infoMapper.batchInsertOpJczxFeedResultInfo(flatInfoList);
        }

        // 5. 处理文件和状态
        if (!StringUtils.isEmpty(dto.getTestUserSignature())) {
            SysUploadFile fileInfo = sysUploadFileService.getFileById(dto.getTestUserSignature());
            if (fileInfo != null && "1".equals(fileInfo.getStatus())) {
                sysUploadFileService.markFileAsPermanent(dto.getTestUserSignature());
            }

            for (String orderId : orderIdList) {
                int count = Integer.parseInt(orderMapper.selectNotEndCountById(orderId));
                if (count == 0) {
                    orderMapper.updateStatusById(EntrustOrderStatusEnum.JCWC.getCode(), orderId);
                }
            }
        }

        return opJczxFeedResultBaseMapper.updateOpJczxFeedResultBase(base);
    }


    @Override
    @Transactional
    public int jhwRetest(String sampleId) {
        //TODO 查询样品是否销毁，销毁不允许复测
        int count = 0;
        //String baseId = "";


        String updateUser = SecurityUtils.getUserId().toString();
        List<String> sampleIdList = new ArrayList<>();
        sampleIdList.add(sampleId);
        List<OpJczxFeedResultInfo> opJczxFeedResultInfos = infoMapper.selectByOrderSampleIdList(sampleIdList);
        if(CollectionUtil.isEmpty(opJczxFeedResultInfos)){
            throw new RuntimeException("复测样品不存在 ");
        }else {
            for (OpJczxFeedResultInfo opJczxFeedResultInfo : opJczxFeedResultInfos) {
                //如果存在报告，且报告状态不是作废，则不允许复测
                List<OpJczxFeedReportBase> reportBases = opJczxFeedReportBaseMapper.selectReportBaseBySampleId2(opJczxFeedResultInfo.getEntrustOrderSampleId());
                if(CollectionUtil.isNotEmpty(reportBases)){
                    for (OpJczxFeedReportBase reportBase:reportBases) {
                        if(!JczxFeedReportStatusEnum.ZF.getCode().equals(reportBase.getStatus())){
                            throw new RuntimeException("样品已生成报告，且报告状态不是作废，不允许复测 ");
                        }
                    }
                }
                // baseId = info.getBaseId();
                //将复测原info记录删除
                // count = count + infoMapper.updateDeleteFlagById(updateUser,opJczxFeedResultInfoId);
                count = count + infoMapper.updateIsResetById(updateUser,opJczxFeedResultInfo.getOpJczxFeedResultInfoId());
                //更新order表为检测中
                orderMapper.updateStatusById(EntrustOrderStatusEnum.JCZ.getCode(),opJczxFeedResultInfo.getEntrustOrderId());
                //更新委托项目表检测人、结果为空
                feedEntrustOrderItemMapper.updateTestToNullById(opJczxFeedResultInfo.getEntrustOrderItemId(),updateUser);
                //更新样品表
                feedEntrustOrderSampleMapper.updateRetestById(opJczxFeedResultInfo.getEntrustOrderSampleId());
            }
        }

        return count;
    }
    @Override
    public OpJczxFeedResultInitVo getInitInfo(String invbillCode, String itemId) {
        // 1. 查询初始化信息 (SQL已修改，包含 instrumentName, instrumentCode, locationName, locationId)
        OpJczxFeedResultInitVo initInfo = opJczxFeedResultBaseMapper.getInitInfo(invbillCode, itemId, SecurityUtils.getDeptId().toString());

        if(initInfo == null){
            throw new RuntimeException("请先在《物料项目标准》中配置该物料的项目，请联系管理员");
        }

        // 2. 查询化验单模板
        OpJczxTestModel model = jczxTestModelMapper.selectByItemIdInvbillCode(itemId,invbillCode);
        if(model == null || StringUtils.isEmpty(model.getTestModelNo())){
            throw new RuntimeException("化验单模板未配置，请联系管理员");
        } else {
            // 3. 查询上一单试剂批号 (保持不变)
            OpJczxFeedResultBase lastBase = opJczxFeedResultBaseMapper.selectLastBaseByModelNo(model.getTestModelNo());
            if(lastBase != null){
                initInfo.setZysysj(lastBase.getZysysj());
                initInfo.setSymph(lastBase.getSymph());
                initInfo.setXxryph(lastBase.getXxryph());
                initInfo.setEDTAbzddryph(lastBase.getEDTAbzddryph());
                initInfo.setBzrypzph(lastBase.getBzrypzph());
                initInfo.setFmsarypzph(lastBase.getFmsarypzph());
                initInfo.setSjhph(lastBase.getSjhph());
                initInfo.setXxspzpc(lastBase.getXxspzpc());
                initInfo.setDdgbh(lastBase.getDdgbh());
                //粗灰分单据使用上一单的箱式电炉时间
                initInfo.setXxFirstTime(lastBase.getXxFirstTime());
                initInfo.setXxSecondTime(lastBase.getXxSecondTime());
            }

            // 4. 处理温湿度 (逻辑调整)
            // 从 initInfo 中获取 locationId 字符串 (逗号分隔的监控点ID)
            String locationIdsStr = initInfo.getLabLocation();

            if(StringUtils.isEmpty(locationIdsStr)){
                initInfo.setTemperature("");
                initInfo.setHumidity("");
                // initInfo.setTestLocation(""); // locationName SQL已查出，这里不需要置空
            } else {
                String[] locationIds = locationIdsStr.split(",");
                if(locationIds.length > 0){
                    // 取第一个监控点的温湿度作为默认值
                    String firstLocationId = locationIds[0];
                    OpEquipmentTempHumMonitor monitor = opEquipmentTempHumMonitorMapper.selectOpEquipmentTempHumMonitorById(firstLocationId);
                    if (monitor != null) {
                        initInfo.setTemperature(monitor.getTemperature() + "℃");
                        initInfo.setHumidity(monitor.getHumidity() + "%");
                    }
                }

                // testLocation (检测地点名称) SQL 已经直接查出来了 (cfg.location_name)，赋值到了 initInfo.testLocation
                // 所以这里不需要再循环查询拼接名称了。
            }

            // testBasis SQL 已经查出
            initInfo.setModelNo(model.getTestModelNo());
            initInfo.setModelName(model.getTestModelName());
        }
        return initInfo;
    }

    @Override
    @Transactional
    public int saveCheck(OpJczxFeedResultBaseDto dto) {
        //TODO 更新委托单状态
        OpJczxFeedResultBase base = new OpJczxFeedResultBase();
        base.setCheckUser(SecurityUtils.getLoginUser().getUser().getNickName());
        base.setOpJczxFeedResultBaseId(dto.getOpJczxFeedResultBaseId());
        base.setCheckTime(new Date());
        base.setCheckUserSignature(dto.getCheckUserSignature());
        base.setStatus(JczxFeedResultStatusEnum.JDWC.getCode());
        base.fillUpdateInfo();
        // 验证文件是否存在（如果传了文件ID）
        if (dto.getCheckUserSignature() != null) {
            SysUploadFile fileInfo = sysUploadFileService.getFileById(dto.getCheckUserSignature());
            if (fileInfo == null) {
                throw new RuntimeException("签名文件不存在，文件ID: " + dto.getCheckUserSignature());
            }
            // 标记文件为正式使用（从临时文件转为正式文件）
            sysUploadFileService.markFileAsPermanent(dto.getCheckUserSignature());
        }
        int count =  opJczxFeedResultBaseMapper.saveCheck(base);
        infoMapper.updateCheckUserByBaseId(dto.getOpJczxFeedResultBaseId(),SecurityUtils.getLoginUser().getUser().getNickName());
        return count;
    }
    @Override
    @Transactional
    public int testSubmit(OpJczxFeedResultBaseDto dto) {
        //TODO 更新委托单状态
        OpJczxFeedResultBase base = new OpJczxFeedResultBase();
        base.setOpJczxFeedResultBaseId(dto.getOpJczxFeedResultBaseId());
        base.setStatus(JczxFeedResultStatusEnum.HYWC.getCode());
        base.setTestEndTime(new Date());
        base.fillUpdateInfo();
        int count =  opJczxFeedResultBaseMapper.updateOpJczxFeedResultBase(base);
        infoMapper.updateTestUserByBaseId(dto.getOpJczxFeedResultBaseId(),SecurityUtils.getLoginUser().getUser().getNickName());
        return count;
    }

    @Override
    @Transactional
    public int retest(List<String> opJczxFeedResultInfoIdList) {
        //TODO 查询样品是否销毁，销毁不允许复测
        int count = 0;
        //String baseId = "";


        String updateUser = SecurityUtils.getUserId().toString();
        for (String opJczxFeedResultInfoId : opJczxFeedResultInfoIdList) {

                OpJczxFeedResultInfo info = infoMapper.
                        selectOpJczxFeedResultInfoByOpJczxFeedResultInfoId(opJczxFeedResultInfoId);
            if(info==null){
                throw new RuntimeException("复测样品不存在 ");
            }else {
                //如果存在报告，且报告状态不是作废，则不允许复测
                List<OpJczxFeedReportBase> reportBases = opJczxFeedReportBaseMapper.selectReportBaseBySampleId2(info.getEntrustOrderSampleId());
                if(CollectionUtil.isNotEmpty(reportBases)){
                    for (OpJczxFeedReportBase reportBase:reportBases) {
                        if(!JczxFeedReportStatusEnum.ZF.getCode().equals(reportBase.getStatus())){
                            throw new RuntimeException("样品已生成报告，且报告状态不是作废，不允许复测 ");
                        }
                    }
                }
               // baseId = info.getBaseId();
                //将复测原info记录删除
              // count = count + infoMapper.updateDeleteFlagById(updateUser,opJczxFeedResultInfoId);
                count = count + infoMapper.updateIsResetById(updateUser,opJczxFeedResultInfoId);
                //更新order表为检测中
                orderMapper.updateStatusById(EntrustOrderStatusEnum.JCZ.getCode(),info.getEntrustOrderId());
                //更新委托项目表检测人、结果为空
                feedEntrustOrderItemMapper.updateTestToNullById(info.getEntrustOrderItemId(),updateUser);
                //更新样品表
                //feedEntrustOrderSampleMapper.updateRetestById(info.getEntrustOrderSampleId());
            }
        }
        //如果化验单中不存在info，则删除化验单
//        if(StringUtils.isEmpty(baseId)){
//            throw new RuntimeException("化验单不存在 ");
//        }else {
//            List<OpJczxFeedResultInfo> infoList = infoMapper.selectInfoListByBaseId(baseId);
//            if(CollectionUtils.isAnyEmpty(infoList)){
//                opJczxFeedResultBaseMapper.updateDeleteFlagById(baseId,updateUser);
//            }
//        }

        return count;
    }

    @Override
    public List<OpJczxFeedResultInfo> selectInfoListByBaseId(String baseId) {
        List<OpJczxFeedResultInfo> infoList = infoMapper.selectInfoListByBaseId(baseId);
        return infoList;
    }

    @Override
    public int saveExamine(OpJczxFeedResultBaseDto dto) {
        //TODO 更新委托单状态
        OpJczxFeedResultBase base = new OpJczxFeedResultBase();
        base.setExamineUser(SecurityUtils.getLoginUser().getUser().getNickName());
        base.setOpJczxFeedResultBaseId(dto.getOpJczxFeedResultBaseId());
        base.setExamineTime(new Date());
        base.setExamineUserSignature(dto.getExamineUserSignature());
        base.setStatus(JczxFeedResultStatusEnum.SHWC.getCode());
        base.fillUpdateInfo();
        // 验证文件是否存在（如果传了文件ID）
        if (dto.getExamineUserSignature() != null) {
            SysUploadFile fileInfo = sysUploadFileService.getFileById(dto.getExamineUserSignature());
            if (fileInfo == null) {
                throw new RuntimeException("签名文件不存在，文件ID: " + dto.getExamineUserSignature());
            }
            // 标记文件为正式使用（从临时文件转为正式文件）
            sysUploadFileService.markFileAsPermanent(dto.getExamineUserSignature());
        }
        int count =   opJczxFeedResultBaseMapper.saveExamine(base);
        return count;
    }

    @Override
    public OpJczxFeedResultVo selectCsfByParentId(String parentId) {

        OpJczxFeedResultBase base = opJczxFeedResultBaseMapper.selectCsfByParentId(parentId);
        if(base==null){
            return null;
        }
        OpJczxFeedResultVo vo = new OpJczxFeedResultVo();
        BeanUtils.copyProperties(base,vo);
        if(base!=null){
            List<OpJczxFeedResultInfo> infoList =  infoMapper.selectInfoListByBaseId(base.getOpJczxFeedResultBaseId());
            vo.setInfoList(infoList);
        }
        //精度
        LabtestItems labtestItems = itemMapper.selectBsLabtestItemsDetailByLabtestItemsId(base.getItemId());
        if(labtestItems!=null ){
            vo.setDecimalPlaces(labtestItems.getDecimalPlaces());
            vo.setSignificantDigits(labtestItems.getSignificantDigits());
        }
        return vo;
    }

    @Override
    public OpJczxFeedResultVo getCsfInfoBySampleList(List<String> sampleList) {

        //通过主单据id查询该单据下样品的初水分信息。
        List<OpJczxFeedResultInfo> infoList = infoMapper.selectCsfByOrderSampleIdList(sampleList);
        OpJczxFeedResultVo vo = new OpJczxFeedResultVo();
        vo.setInfoList(infoList);
        return vo;
    }

    @Override
    public OpJczxFeedResultVo getJhwCsfInfoBySampleList(List<String> sampleList) {

        List<OpJczxFeedResultInfo> infoList = new ArrayList<>();
        for (String s : sampleList) {
            List<String> list = new ArrayList<>();
            list.add(s);
            List<OpJczxFeedResultInfo> infoList2 = infoMapper.selectCsfByOrderSampleIdList(list);
            if(CollectionUtil.isNotEmpty(infoList2)){
                infoList.addAll(infoList2);
            }else {
                OpJczxFeedResultInfo empty = new OpJczxFeedResultInfo();
                empty.setEntrustOrderSampleId(s);
                infoList.add(empty);
            }

        }
        //通过主单据id查询该单据下样品的初水分信息。

        OpJczxFeedResultVo vo = new OpJczxFeedResultVo();
        vo.setInfoList(infoList);
        return vo;
    }

    /**
     * 获取当前用户饲料待提交数量
     *
     * @return 待提交数量
     */
    @Override
    public int countFeedWaitSubmitByCurrentUser() {
        // 1. 封装查询条件（状态为1 和 6）
        List<String> statusList = new ArrayList<>();
        statusList.add("1");
        statusList.add("6");
        String userName = SecurityUtils.getLoginUser().getUser().getNickName();
        // 2. 查询
        return opJczxFeedResultBaseMapper.countFeedByStatusListAndTestUser(
                statusList, // 状态列表
                userName // 当前用户名称
        );
    }

    /**
     * 获取当前用户饲料待校验数量
     *
     * @return 待校验数量
     */
    @Override
    public int countFeedWaitCheckByCurrentUser() {
        // 查询
        return opJczxFeedResultBaseMapper.countFeedByStatusAndTestUser(
                "2", // 状态
                SecurityUtils.getUserId().toString() // 当前用户id
        );
    }
}
