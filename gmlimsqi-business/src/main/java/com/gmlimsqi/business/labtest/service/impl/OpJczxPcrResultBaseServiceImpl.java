package com.gmlimsqi.business.labtest.service.impl;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.basicdata.domain.LabtestItems;
import com.gmlimsqi.business.basicdata.mapper.LabtestItemsMapper;
import com.gmlimsqi.business.labtest.bo.PcrResultImportBo;
import com.gmlimsqi.business.labtest.domain.*;
import com.gmlimsqi.business.labtest.mapper.*;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.enums.EntrustOrderStatusEnum;
import com.gmlimsqi.common.enums.JczxPcrResultStatusEnum;
import com.gmlimsqi.common.enums.YesNo2Enum;
import com.gmlimsqi.common.utils.CollectionUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.domain.SysUploadFile;
import com.gmlimsqi.system.service.ISysUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import com.gmlimsqi.business.labtest.service.IOpJczxPcrResultBaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 样品化验PCRService业务层处理
 * 
 * @author hhy
 * @date 2025-10-13
 */
@Service
public class OpJczxPcrResultBaseServiceImpl implements IOpJczxPcrResultBaseService 
{
    @Autowired
    private OpJczxPcrResultBaseMapper opJczxPcrResultBaseMapper;
    @Autowired
    private OpJczxPcrResultInfoMapper opJczxPcrResultInfoMapper;
    @Autowired
    private OpPcrEntrustOrderSampleMapper entrustOrderSampleMapper;
    @Autowired
    private OpPcrEntrustOrderItemMapper entrustOrderItemMapper;
    @Autowired
    private LabtestItemsMapper itemMapper;
    @Autowired
    private UserCacheService userCacheService;
     @Autowired
    private ISysUploadFileService sysUploadFileService;
    @Autowired
    private OpPcrEntrustOrderMapper pcrEntrustOrderMapper;
    @Autowired
    private OpPcrEntrustOrderSampleMapper pcrEntrustOrderSampleMapper;

    /**
     * 查询样品化验PCR
     * 
     * @param opJczxPcrResultBaseId 样品化验PCR主键
     * @return 样品化验PCR
     */
    @Override
    public OpJczxPcrResultBase selectOpJczxPcrResultBaseByOpJczxPcrResultBaseId(String opJczxPcrResultBaseId)
    {
        return opJczxPcrResultBaseMapper.selectOpJczxPcrResultBaseByOpJczxPcrResultBaseId(opJczxPcrResultBaseId);
    }

    /**
     * 查询样品化验PCR列表
     * 
     * @param opJczxPcrResultBase 样品化验PCR
     * @return 样品化验PCR
     */
    @Override
    public List<OpJczxPcrResultBase> selectOpJczxPcrResultBaseList(OpJczxPcrResultBase opJczxPcrResultBase)
    {
        List<OpJczxPcrResultBase> items = opJczxPcrResultBaseMapper.selectOpJczxPcrResultBaseList(opJczxPcrResultBase);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpJczxPcrResultBase::getUpdateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getUpdateBy())));
        }

        return items;
    }

    /**
     * 新增样品化验PCR
     * 
     * @param opJczxPcrResultBase 样品化验PCR
     * @return 结果
     */
    @Transactional
    @Override
    public int insertOpJczxPcrResultBase(OpJczxPcrResultBase opJczxPcrResultBase)
    {
        if (StringUtils.isEmpty(opJczxPcrResultBase.getOpJczxPcrResultBaseId())) {
            opJczxPcrResultBase.setOpJczxPcrResultBaseId(IdUtils.simpleUUID());
            for (OpJczxPcrResultInfo opJczxPcrResultInfo : opJczxPcrResultBase.getOpJczxPcrResultInfoList()){
                opJczxPcrResultInfo.setOpJczxPcrResultInfoId(IdUtils.simpleUUID());
                opJczxPcrResultInfo.setBaseId(opJczxPcrResultBase.getOpJczxPcrResultBaseId());
                opJczxPcrResultInfo.fillCreateInfo();
            }
        }
        // 自动填充创建/更新信息
        opJczxPcrResultBase.fillCreateInfo();
        int rows = opJczxPcrResultBaseMapper.insertOpJczxPcrResultBase(opJczxPcrResultBase);
        insertOpJczxPcrResultInfo(opJczxPcrResultBase);
        return rows;
    }

    /**
     * 修改样品化验PCR
     * 
     * @param opJczxPcrResultBase 样品化验PCR
     * @return 结果
     */
    @Transactional
    @Override
    public int updateOpJczxPcrResultBase(OpJczxPcrResultBase opJczxPcrResultBase)
    {
        // 自动填充更新信息
        opJczxPcrResultBase.fillUpdateInfo();
        //更新子表删除标志并插入
        opJczxPcrResultBaseMapper.updateDeleteFlagByBaseId(opJczxPcrResultBase.getOpJczxPcrResultBaseId(),String.valueOf(SecurityUtils.getUserId()));
        for (OpJczxPcrResultInfo opJczxPcrResultInfo : opJczxPcrResultBase.getOpJczxPcrResultInfoList()){
            opJczxPcrResultInfo.setOpJczxPcrResultInfoId(IdUtils.simpleUUID());
            opJczxPcrResultInfo.setBaseId(opJczxPcrResultBase.getOpJczxPcrResultBaseId());
            opJczxPcrResultInfo.fillCreateInfo();
        }
        insertOpJczxPcrResultInfo(opJczxPcrResultBase);
        return opJczxPcrResultBaseMapper.updateOpJczxPcrResultBase(opJczxPcrResultBase);
    }



    /**
     * 新增检测中心pce化验单子信息
     * 
     * @param opJczxPcrResultBase 样品化验PCR对象
     */
    public void insertOpJczxPcrResultInfo(OpJczxPcrResultBase opJczxPcrResultBase)
    {
        List<OpJczxPcrResultInfo> opJczxPcrResultInfoList = opJczxPcrResultBase.getOpJczxPcrResultInfoList();
        String opJczxPcrResultBaseId = opJczxPcrResultBase.getOpJczxPcrResultBaseId();
        if (StringUtils.isNotNull(opJczxPcrResultInfoList))
        {
            List<OpJczxPcrResultInfo> list = new ArrayList<OpJczxPcrResultInfo>();
            for (OpJczxPcrResultInfo opJczxPcrResultInfo : opJczxPcrResultInfoList)
            {
                opJczxPcrResultInfo.setBaseId(opJczxPcrResultBaseId);
                list.add(opJczxPcrResultInfo);
            }
            if (list.size() > 0)
            {
                opJczxPcrResultBaseMapper.batchOpJczxPcrResultInfo(list);
            }
        }
    }

    /**
     * 导入PCR检测结果数据
     */
    @Override
    @Transactional
    public String importPcrResult(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RuntimeException("导入文件不能为空！");
        }
        // 复制文件内容到字节数组
        byte[] fileBytes = file.getBytes();
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        try {
            // 1. 使用复制的数据读取Excel
            List<PcrResultImportBo> importList = readExcelDataFromBytes(fileBytes);

            if (importList == null || importList.isEmpty()) {
                importList = importList.stream()
                        .filter(item -> item != null)
                        .collect(Collectors.toList());
                if (importList.isEmpty()) {
                    throw new RuntimeException("Excel中没有有效数据！");
                }
            }

            // 2. 使用复制的数据保存文件
            String fileId = sysUploadFileService.uploadFile(fileBytes, originalFilename, contentType);
            // 3. 提取样品编号列表
            List<String> sampleNoList = importList.stream()
                    .map(PcrResultImportBo::getSampleNo)
                    .filter(StringUtils::isNotEmpty)
                    .distinct()
                    .collect(Collectors.toList());

            if (sampleNoList.isEmpty()) {
                sysUploadFileService.deleteFile(fileId);
                throw new RuntimeException("Excel中没有有效的样品编号！");
            }
            //去重
            sampleNoList  = sampleNoList.stream().distinct().collect(Collectors.toList());
            //去掉空白样
            sampleNoList  = sampleNoList.stream().filter(a -> !("空白样".equals(a) ||"阴性对照".equals(a) ||"阳性对照".equals(a) ) ).collect(Collectors.toList());
            //过滤样品 已审核通过的数据，并返回前端
            List<OpPcrEntrustOrderSample> examineSampleList = entrustOrderSampleMapper.selectExamineBySampleNoList(sampleNoList);
            List<String> exanmineSampleNoList = examineSampleList.stream().map(OpPcrEntrustOrderSample::getSampleNo).collect(Collectors.toList());
            sampleNoList.removeAll(exanmineSampleNoList);
            if (sampleNoList.isEmpty()) {
                sysUploadFileService.deleteFile(fileId);
                throw new RuntimeException("Excel中数据已全部审核，无法修改！");
            }
            importList = importList.stream().filter(a -> !exanmineSampleNoList.contains(a.getSampleNo())).collect(Collectors.toList());

            // 4. 查询已有的PCR检测结果数据
            List<OpJczxPcrResultBase> existingBaseList = opJczxPcrResultBaseMapper.selectOpJczxPcrResultBaseListBySampleNoList(sampleNoList);
            String testUserId = String.valueOf(SecurityUtils.getUserId());
            LabtestItems queryItem = new LabtestItems();
            //标签是pcr
            queryItem.setTag("3");
            queryItem.setIsDelete(YesNo2Enum.NO.getCode());
            queryItem.setIsEnable(YesNo2Enum.YES.getCode());
            List<LabtestItems> labtestItems = itemMapper.selectBsLabtestItemsList(queryItem);
            Map<String,String> itemNameIdSet = labtestItems.stream().collect(Collectors.toMap(
                    LabtestItems::getItemName,
                    LabtestItems::getLabtestItemsId,
                    (existing, replacement) -> existing  // 重复key处理策略
            ));
            String kzsjh = importList.get(0).getKzsjh();
            String tqsjh = importList.get(0).getTqsjh();
            PcrResultImportBo kbBo = new PcrResultImportBo();
            PcrResultImportBo yinBo = new PcrResultImportBo();
            PcrResultImportBo yangBo = new PcrResultImportBo();

            List<OpJczxPcrResultInfo> kbInfoList = new LinkedList<>();
            List<OpJczxPcrResultInfo> yinInfoList = new LinkedList<>();
            List<OpJczxPcrResultInfo> yangInfoList = new LinkedList<>();
            List<OpJczxPcrResultBase> opJczxPcrResultBases = new ArrayList<>();
            for (PcrResultImportBo pcrResultImportBo : importList) {
                // 跳过sampleNo=空白样和阴性阳性对照的数据
                if("空白样".equals(pcrResultImportBo.getSampleNo())){
                    kbBo = pcrResultImportBo;
                    continue;
                }
                if("阴性对照".equals(pcrResultImportBo.getSampleNo())){
                    yinBo = pcrResultImportBo;
                    continue;
                }
                if("阳性对照".equals(pcrResultImportBo.getSampleNo())){
                    yangBo = pcrResultImportBo;
                    continue;
                }
                updateInfoItemData( testUserId, itemNameIdSet , pcrResultImportBo, kzsjh, tqsjh, fileId);
                //查询空白样和阴性阳性对照的infoList
                if(CollectionUtils.isAnyEmpty(opJczxPcrResultBases)){
                    List<String> queryList = new ArrayList<>();
                    queryList.add(pcrResultImportBo.getSampleNo());
                    opJczxPcrResultBases = opJczxPcrResultBaseMapper.selectOpJczxPcrResultBaseListBySampleNoList(queryList);
                    //查询空白样和阴性阳性对照的info信息
                    OpJczxPcrResultInfo queryInfo = new OpJczxPcrResultInfo();
                    queryInfo.setSampleNo("空白样");
                    queryInfo.setBaseId(opJczxPcrResultBases.get(0).getOpJczxPcrResultBaseId());
                    kbInfoList = opJczxPcrResultInfoMapper.selectOpJczxPcrResultInfoList(queryInfo);

                    OpJczxPcrResultInfo queryInfo2 = new OpJczxPcrResultInfo();
                    queryInfo2.setSampleNo("阴性对照");
                    queryInfo2.setBaseId(opJczxPcrResultBases.get(0).getOpJczxPcrResultBaseId());
                    yinInfoList = opJczxPcrResultInfoMapper.selectOpJczxPcrResultInfoList(queryInfo2);
                    OpJczxPcrResultInfo queryInfo3 = new OpJczxPcrResultInfo();
                    queryInfo3.setSampleNo("阳性对照");
                    queryInfo3.setBaseId(opJczxPcrResultBases.get(0).getOpJczxPcrResultBaseId());
                    yangInfoList = opJczxPcrResultInfoMapper.selectOpJczxPcrResultInfoList(queryInfo3);
                }
            }
            //更新空白样和阴性阳性对照
            for (OpJczxPcrResultInfo info : kbInfoList) {
                info.setFileId(fileId);
                info.setUpdateBy(testUserId);
                if("空白样".equals(info.getSampleNo())){
                    updateInfoOtherData(info,kbBo);
                }
            }
            for (OpJczxPcrResultInfo info : yangInfoList) {
                info.setFileId(fileId);
                info.setUpdateBy(testUserId);
                if("阳性对照".equals(info.getSampleNo())){
                    updateInfoOtherData(info,yangBo);
                }
            }
            for (OpJczxPcrResultInfo info : yinInfoList) {
                info.setFileId(fileId);
                info.setUpdateBy(testUserId);
                if("阴性对照".equals(info.getSampleNo())){
                    updateInfoOtherData(info,yinBo);
                }
            }



            for (OpJczxPcrResultBase opJczxPcrResultBase : existingBaseList) {
                opJczxPcrResultBase.setFileId(fileId);
                opJczxPcrResultBase.setStatus(JczxPcrResultStatusEnum.HYWC.getCode());
                opJczxPcrResultBase.setTestEndTime(new Date());
                opJczxPcrResultBase.setTestUser(SecurityUtils.getLoginUser().getUser().getNickName());
                opJczxPcrResultBase.setTestUserId(testUserId);
                opJczxPcrResultBase.fillUpdateInfo();
                opJczxPcrResultBaseMapper.updateOpJczxPcrResultBase(opJczxPcrResultBase);
            }
            //更新委托单检测人
            Set<String> sampleNoSet = new HashSet<>(sampleNoList);


            Date testTime = new Date();
            //更新委托单状态
            // 检查该委托单下的所有项目是否都已检测
            for (String s : sampleNoSet) {
                int untestdCount = entrustOrderItemMapper.countUntestedItemsBySampleNo(s);

                if (untestdCount == 0) {
                    // 如果所有项目都已检测，更新父 OpPcrEntrustOrder 的状态为 "3" (已检测)
                    // 状态 3: 已检测
                    pcrEntrustOrderMapper.updateStatusById(s, EntrustOrderStatusEnum.JCWC.getCode(), null,null,null,testUserId, testTime);
                }
            }
            //更新样品表检测人检测时间
            pcrEntrustOrderSampleMapper.updateTestUserBySampleNoSet(sampleNoSet,testUserId,testTime);

            // 标记文件为正式文件
            sysUploadFileService.markFileAsPermanent(fileId);

            if(CollectionUtil.isEmpty(exanmineSampleNoList)){
                return "导入成功，共处理 " + importList.size() + " 条数据";
            }else {
                String result = String.join(",", exanmineSampleNoList);
                return "导入成功，共处理 " + importList.size() + " 条数据,失败条数："+exanmineSampleNoList.size()+",样品编号为："+result;
            }

        } catch (Exception e) {
            throw new RuntimeException("导入失败：" + e.getMessage());
        }
    }

    private void updateInfoOtherData(OpJczxPcrResultInfo info,PcrResultImportBo bo){
        //info.setRemark(bo.getRemark());
        if(StringUtils.isNotEmpty(bo.getItem1()) && "金黄色葡萄球菌".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem1());
            info.setItemName("金黄色葡萄球菌");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem2())&& "牛轮状病毒".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem2());
            info.setItemName("牛轮状病毒");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem3()) && "牛冠状病毒".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem3());
            info.setItemName("牛冠状病毒");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem4()) && "隐孢子虫".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem4());
            info.setItemName("隐孢子虫");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem5()) && "肠毒素型细菌-大肠杆菌".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem5());
            info.setItemName("肠毒素型细菌-大肠杆菌");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem6()) && "绿脓杆菌".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem6());
            info.setItemName("绿脓杆菌");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem7()) && "β-内酰胺酶抗性基因".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem7());
            info.setItemName("β-内酰胺酶抗性基因");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem8()) && "停乳链球菌".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem8());
            info.setItemName("停乳链球菌");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem9()) && "克雷伯氏菌属".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem9());
            info.setItemName("克雷伯氏菌属");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem10()) && "牛支原体".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem10());
            info.setItemName("牛支原体");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem11()) && "大肠杆菌".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem11());
            info.setItemName("大肠杆菌");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem12()) && "牛病毒性腹泻病毒".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem12());
            info.setItemName("牛病毒性腹泻病毒");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem13()) && "牛呼吸道合胞体病毒".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem13());
            info.setItemName("牛呼吸道合胞体病毒");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem14()) && "牛副流感病毒3型".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem14());
            info.setItemName("牛副流感病毒3型");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem15()) && "溶血性曼氏杆菌".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem15());
            info.setItemName("溶血性曼氏杆菌");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem16()) && "多杀巴斯德杆菌".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem16());
            info.setItemName("多杀巴斯德杆菌");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }
        if (StringUtils.isNotEmpty(bo.getItem17()) && "无乳链球菌".equals(info.getPcrEntrustOrderItemId())){
            info.setTestResult(bo.getItem17());
            info.setItemName("无乳链球菌");
            opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(info);
        }

    }

    private void updateInfoItemData(String testUserId,Map<String,String> itemNameIdSet ,PcrResultImportBo pcrResultImportBo,
                                    String kzsjh,String tqsjh,String fileId){
        OpPcrEntrustOrderItem item = new OpPcrEntrustOrderItem();
        item.setTestUserId(testUserId);
        item.setKzsjh(kzsjh);
        item.setTqsjh(tqsjh);
        item.setSampleNo(pcrResultImportBo.getSampleNo());
        item.setFileId(fileId);
        item.setDeptName(pcrResultImportBo.getDeptName());
        OpJczxPcrResultInfo info = new OpJczxPcrResultInfo();
        info.setKzsjh(kzsjh);
        info.setTqsjh(tqsjh);
        info.setSequence(pcrResultImportBo.getSequence());
        info.setSampleNo(pcrResultImportBo.getSampleNo());
        info.setUpdateBy(testUserId);
        info.setFileId(fileId);
        //info.setRemark(pcrResultImportBo.getRemark());
        if(StringUtils.isNotEmpty(pcrResultImportBo.getItem1())){
            info.setTestResult(pcrResultImportBo.getItem1());
            info.setItemName("金黄色葡萄球菌");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem1());
            String itemId = itemNameIdSet.get("金黄色葡萄球菌");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("金黄色葡萄球菌项目不存在");
            }
            item.setItemName("金黄色葡萄球菌");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem2())){
            info.setTestResult(pcrResultImportBo.getItem2());
            info.setItemName("牛轮状病毒");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem2());
            String itemId = itemNameIdSet.get("牛轮状病毒");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("牛轮状病毒项目不存在");
            }
            item.setItemName("牛轮状病毒");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem3())){
            info.setTestResult(pcrResultImportBo.getItem3());
            info.setItemName("牛冠状病毒");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem3());
            String itemId = itemNameIdSet.get("牛冠状病毒");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("牛冠状病毒项目不存在");
            }
            item.setItemName("牛冠状病毒");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem4())){
            info.setTestResult(pcrResultImportBo.getItem4());
            info.setItemName("隐孢子虫");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem4());
            String itemId = itemNameIdSet.get("隐孢子虫");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("隐孢子虫项目不存在");
            }
            item.setItemName("隐孢子虫");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem5())){
            info.setTestResult(pcrResultImportBo.getItem5());
            info.setItemName("肠毒素型细菌-大肠杆菌");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem5());
            String itemId = itemNameIdSet.get("肠毒素型细菌-大肠杆菌");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("肠毒素型细菌-大肠杆菌项目不存在");
            }
            item.setItemName("肠毒素型细菌-大肠杆菌");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem6())){
            info.setTestResult(pcrResultImportBo.getItem6());
            info.setItemName("绿脓杆菌");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem6());
            String itemId = itemNameIdSet.get("绿脓杆菌");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("绿脓杆菌项目不存在");
            }
            item.setItemName("绿脓杆菌");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem7())){
            info.setTestResult(pcrResultImportBo.getItem7());
            info.setItemName("β-内酰胺酶抗性基因");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem7());
            String itemId = itemNameIdSet.get("β-内酰胺酶抗性基因");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("β-内酰胺酶抗性基因项目不存在");
            }
            item.setItemName("β-内酰胺酶抗性基因");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem8())){
            info.setTestResult(pcrResultImportBo.getItem8());
            info.setItemName("停乳链球菌");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem8());
            String itemId = itemNameIdSet.get("停乳链球菌");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("停乳链球菌项目不存在");
            }
            item.setItemName("停乳链球菌");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem9())){
            info.setTestResult(pcrResultImportBo.getItem9());
            info.setItemName("克雷伯氏菌属");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem9());
            String itemId = itemNameIdSet.get("克雷伯氏菌属");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("克雷伯氏菌属项目不存在");
            }
            item.setItemName("克雷伯氏菌属");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem10())){
            info.setTestResult(pcrResultImportBo.getItem10());
            info.setItemName("牛支原体");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem10());
            String itemId = itemNameIdSet.get("牛支原体");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("牛支原体项目不存在");
            }
            item.setItemName("牛支原体");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem11())){
            info.setTestResult(pcrResultImportBo.getItem11());
            info.setItemName("大肠杆菌");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem11());
            String itemId = itemNameIdSet.get("大肠杆菌");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("大肠杆菌项目不存在");
            }
            item.setItemName("大肠杆菌");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem12())){
            info.setTestResult(pcrResultImportBo.getItem12());
            info.setItemName("牛病毒性腹泻病毒");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem12());
            String itemId = itemNameIdSet.get("牛病毒性腹泻病毒");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("牛病毒性腹泻病毒项目不存在");
            }
            item.setItemName("牛病毒性腹泻病毒");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem13())){
            info.setTestResult(pcrResultImportBo.getItem13());
            info.setItemName("牛呼吸道合胞体病毒");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem13());
            String itemId = itemNameIdSet.get("牛呼吸道合胞体病毒");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("牛呼吸道合胞体病毒项目不存在");
            }
            item.setItemName("牛呼吸道合胞体病毒");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem14())){
            info.setTestResult(pcrResultImportBo.getItem14());
            info.setItemName("牛副流感病毒3型");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem14());
            String itemId = itemNameIdSet.get("牛副流感病毒3型");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("牛副流感病毒3型项目不存在");
            }
            item.setItemName("牛副流感病毒3型");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem15())){
            info.setTestResult(pcrResultImportBo.getItem15());
            info.setItemName("溶血性曼氏杆菌");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem15());
            String itemId = itemNameIdSet.get("溶血性曼氏杆菌");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("溶血性曼氏杆菌项目不存在");
            }
            item.setItemName("溶血性曼氏杆菌");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem16())){
            info.setTestResult(pcrResultImportBo.getItem16());
            info.setItemName("多杀巴斯德杆菌");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem16());
            String itemId = itemNameIdSet.get("多杀巴斯德杆菌");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("多杀巴斯德杆菌项目不存在");
            }
            item.setItemName("多杀巴斯德杆菌");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
        if (StringUtils.isNotEmpty(pcrResultImportBo.getItem17())){
            info.setTestResult(pcrResultImportBo.getItem17());
            info.setItemName("无乳链球菌");
            opJczxPcrResultInfoMapper.updateResultBySampleNoItemName(info);
            item.setTestResult(pcrResultImportBo.getItem17());
            String itemId = itemNameIdSet.get("无乳链球菌");
            if(StringUtils.isEmpty(itemId)){
                throw new RuntimeException("无乳链球菌项目不存在");
            }
            item.setItemName("无乳链球菌");
            item.setItemId(itemId);
            entrustOrderItemMapper.updateResultBySampleNo(item);
        }
    }


    /**
     * 从字节数组读取Excel数据
     */
    private List<PcrResultImportBo> readExcelDataFromBytes(byte[] fileBytes) throws Exception {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            ExcelUtil<PcrResultImportBo> util = new ExcelUtil<>(PcrResultImportBo.class);
            return util.importExcel(inputStream);
        }
    }
    /**
     * 审核PCR项目结果（更新备注和流转状态）
     */
    @Override
    @Transactional
    public int examinePcrResultBase(List<OpPcrEntrustOrderItem> items,String examinePassFlag,String resultNo) {
        // 审核人信息
        String examineUserId = String.valueOf(SecurityUtils.getUserId());
        String examineUser = SecurityUtils.getLoginUser().getUser().getNickName();
        Date examineTime = new Date();
        int totalRows = 0;

        if (CollectionUtil.isEmpty(items)) {
            throw new RuntimeException("未接收到审核项目数据。");
        }

        // 1. 检查是否存在非空备注 (Determining the single condition)
        List<OpPcrEntrustOrderItem> remarksItems = items.stream()
                .filter(item -> StringUtils.isNotEmpty(item.getRemark()))
                .collect(Collectors.toList());

        //每个样品的审核异议
        Map<String,String> sampleExamineNote = remarksItems.stream().collect(Collectors.toMap(
                OpPcrEntrustOrderItem::getSampleNo,
                OpPcrEntrustOrderItem::getRemark,
                (existing, replacement) -> existing
        ));

        // --- Logic Split ---

        if ("1".equals(examinePassFlag)) {
            // Case 1: 审核通过 (所有 remarks 都是空的)
            List<String> passedIds = items.stream()
                    .map(OpPcrEntrustOrderItem::getOpPcrEntrustOrderItemId)
                    .collect(Collectors.toList());

            // a. 批量更新所有项目的审核信息
            totalRows = entrustOrderItemMapper.batchUpdateExamineFields(passedIds, examineUserId, examineUser, examineTime);

            //查询所有委托单
            Set<String> orderIdSet = new HashSet<>();
            for (OpPcrEntrustOrderItem item : items) {
                String opPcrEntrustOrderId = entrustOrderItemMapper.selectEntrustOrderIdByItemId(item.getOpPcrEntrustOrderItemId());
                orderIdSet.add(opPcrEntrustOrderId);

            }
            for (String orderId : orderIdSet) {
                // b. 检查该委托单下的所有项目是否都已审核
                int unexaminedCount = entrustOrderItemMapper.countUnexaminedItemsByOrderId(orderId);

                if (unexaminedCount == 0) {
                    // c. 如果所有项目都已审核，更新父 OpPcrEntrustOrder 的状态为 "4" (已审核)
                    // 状态 4: 已审核
                    pcrEntrustOrderMapper.updateStatusById(orderId, EntrustOrderStatusEnum.YSH.getCode(),examineUser, examineUserId, examineTime,examineUserId,examineTime);
                }
                //更新result_base为审核已通过
                opJczxPcrResultBaseMapper.updateExamineFlagByResultNo("1",resultNo);
            }
        } else {
            // Case 2: 不通过
            // 只更新有备注的项目的备注字段 (并设置更新人/时间)
            for (OpPcrEntrustOrderItem item : remarksItems) {
                item.setUpdateBy(examineUserId);
                // 备注内容不需要清除，直接更新为传入值（即异议内容）
                entrustOrderItemMapper.updateRemarkById(item);
            }
            // 更新info表的remark
            for (OpPcrEntrustOrderItem item : remarksItems) {
                totalRows += opJczxPcrResultInfoMapper.updateRemarkBySampleNoItemId(item.getRemark(),item.getSampleNo(),item.getOpPcrEntrustOrderItemId());
            }
            // 注意：不更新 examine_user/examine_time，也不流转父单据状态。
            //更新result_base为审核不通过
            opJczxPcrResultBaseMapper.updateExamineFlagByResultNo("0",resultNo);
            // 更新sample表审核异议
            for (Map.Entry<String, String> entry : sampleExamineNote.entrySet()) {
                pcrEntrustOrderSampleMapper.updateExamineNoteBySampleNo(entry.getKey(),entry.getValue());
            }
            //更新info表
        }

        return totalRows;
    }


    /**
     * 取消审核PCR项目结果
     * @param entrustOrderNo 委托单号
     * @param pcrTaskItemType PCR项目类型
     * @return 结果行数
     */
    @Override
    @Transactional
    public int cancelExaminePcrResult(String resultNo) {
        String updateUserId = String.valueOf(SecurityUtils.getUserId());
        Date updateTime = new Date();
        // 1. 获取委托单信息并校验状态
        OpJczxPcrResultBase base = opJczxPcrResultBaseMapper.selectByNo(resultNo);
        if (base == null) {
            throw new RuntimeException("未找到导入文件：" + resultNo);
        }
        if (!JczxPcrResultStatusEnum.SHWC.getCode().equals(base.getStatus())) {
            throw new RuntimeException("状态不是已审核，无法取消审核");
        }
        //查询所以info
        OpJczxPcrResultInfo query = new OpJczxPcrResultInfo();
        query.setBaseId(base.getOpJczxPcrResultBaseId());
        List<OpJczxPcrResultInfo> infoList = opJczxPcrResultInfoMapper.selectOpJczxPcrResultInfoList(query);

        //过滤空白样、阴性阳性
        infoList  = infoList.stream().filter(a -> !("空白样".equals(a) ||"阴性对照".equals(a) ||"阳性对照".equals(a) ) ).collect(Collectors.toList());
        Set<String> orderIdSet = new HashSet<>();
        for (OpJczxPcrResultInfo info : infoList) {
                String opPcrEntrustOrderId = entrustOrderItemMapper.selectEntrustOrderIdByItemId(info.getPcrEntrustOrderItemId());
                if(StringUtils.isNotEmpty(opPcrEntrustOrderId)){
                    orderIdSet.add(opPcrEntrustOrderId);
                }
        }

        //修改委托单状态
        for (String orderId : orderIdSet) {
            pcrEntrustOrderMapper.updateStatusById(orderId, EntrustOrderStatusEnum.JCWC.getCode(),null, null, null,updateUserId,updateTime);
        }

        //更新result_base
        opJczxPcrResultBaseMapper.updateCancelExamine(base.getOpJczxPcrResultBaseId());
        int updatedRows=0;
        // 4. 提取 item ID 列表
        List<String> itemIds = infoList.stream().map(OpJczxPcrResultInfo::getPcrEntrustOrderItemId).distinct().collect(Collectors.toList());

        // 5. 批量清除审核信息
        updatedRows += entrustOrderItemMapper.batchClearExamineFields(itemIds, updateUserId, updateTime);
        opJczxPcrResultInfoMapper.batchClearExamineFields(base.getOpJczxPcrResultBaseId());

        return updatedRows;
    }


}
