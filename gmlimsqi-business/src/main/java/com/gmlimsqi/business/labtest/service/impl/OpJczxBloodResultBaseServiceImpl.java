package com.gmlimsqi.business.labtest.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.basicdata.domain.LabtestItems;
import com.gmlimsqi.business.labtest.domain.OpJczxBloodResultInfo;
import com.gmlimsqi.business.labtest.bo.BloodResultImportBo;
import com.gmlimsqi.business.labtest.domain.*;
import com.gmlimsqi.business.labtest.mapper.OpBloodEntrustOrderMapper;
import com.gmlimsqi.business.labtest.mapper.OpBloodEntrustOrderSampleMapper;
import com.gmlimsqi.business.labtest.mapper.OpJczxBloodResultBaseMapper;
import com.gmlimsqi.business.labtest.mapper.OpJczxBloodResultInfoMapper;
import com.gmlimsqi.business.labtest.service.IOpJczxBloodResultBaseService;
import com.gmlimsqi.business.util.BloodReportParser;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.enums.EntrustOrderStatusEnum;
import com.gmlimsqi.common.enums.JczxBloodResultStatusEnum;
import com.gmlimsqi.common.enums.JczxPcrResultStatusEnum;
import com.gmlimsqi.common.enums.YesNo2Enum;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.service.ISysUploadFileService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.gmlimsqi.framework.datasource.DynamicDataSourceContextHolder.log;

/**
 * 检测中心血样化验单主Service业务层处理
 * 
 * @author hhy
 * @date 2025-10-14
 */
@Service
public class OpJczxBloodResultBaseServiceImpl implements IOpJczxBloodResultBaseService
{
    @Autowired
    private OpJczxBloodResultBaseMapper opJczxBloodResultBaseMapper;
    @Autowired
    private OpJczxBloodResultInfoMapper opJczxBloodResultInfoMapper;
    @Autowired
    private OpBloodEntrustOrderSampleMapper entrustOrderSampleMapper;
    @Autowired
    private OpBloodEntrustOrderMapper bloodEntrustOrderMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private ISysUploadFileService sysUploadFileService;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;
    // 缓存：避免同一个 Excel 处理同一单时反复查询数据库 Map<委托单ID, 化验单主表BaseID>
    private Map<String, String> orderIdToBaseIdCache = new HashMap<>();


    /**
     * 查询检测中心血样化验单主列表
     * 
     * @param opJczxBloodResultBase 检测中心血样化验单主
     * @return 检测中心血样化验单主
     */
    @Override
    public List<OpJczxBloodResultBase> selectOpJczxBloodResultBaseList(OpJczxBloodResultBase opJczxBloodResultBase)
    {
        List<OpJczxBloodResultBase> items = opJczxBloodResultBaseMapper.selectOpJczxBloodResultBaseList(opJczxBloodResultBase);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpJczxBloodResultBase::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增检测中心血样化验单主
     * 
     * @param opJczxBloodResultBase 检测中心血样化验单主
     * @return 结果
     */
    @Override
    public int insertOpJczxBloodResultBase(OpJczxBloodResultBase opJczxBloodResultBase)
    {
        if (StringUtils.isEmpty(opJczxBloodResultBase.getOpJczxBloodResultBaseId())) {
            opJczxBloodResultBase.setOpJczxBloodResultBaseId(IdUtils.simpleUUID());
            for (OpJczxBloodResultInfo opJczxBloodResultInfo : opJczxBloodResultBase.getOpJczxBloodResultInfoList()) {
                opJczxBloodResultInfo.setOpJczxBloodResultInfoId(IdUtils.simpleUUID());
                opJczxBloodResultInfo.setBaseId(opJczxBloodResultBase.getOpJczxBloodResultBaseId());
                opJczxBloodResultInfo.fillCreateInfo();
            }
        }
        // 自动填充创建/更新信息
        opJczxBloodResultBase.fillCreateInfo();
        int rows = opJczxBloodResultBaseMapper.insertOpJczxBloodResultBase(opJczxBloodResultBase);
          insertOpJczxBloodResultInfo(opJczxBloodResultBase);
        return rows;
    }
    /**
     * 新增检测中心pce化验单子信息
     *
     * @param opJczxBloodResultBase 样品化验Blood对象
     */
    public void insertOpJczxBloodResultInfo(OpJczxBloodResultBase opJczxBloodResultBase)
    {
        List<OpJczxBloodResultInfo> opJczxBloodResultInfoList = opJczxBloodResultBase.getOpJczxBloodResultInfoList();
        String opJczxBloodResultBaseId = opJczxBloodResultBase.getOpJczxBloodResultBaseId();
        if (StringUtils.isNotNull(opJczxBloodResultInfoList))
        {
            List<OpJczxBloodResultInfo> list = new ArrayList<OpJczxBloodResultInfo>();
            for (OpJczxBloodResultInfo opJczxBloodResultInfo : opJczxBloodResultInfoList)
            {
                opJczxBloodResultInfo.setBaseId(opJczxBloodResultBaseId);
                list.add(opJczxBloodResultInfo);
            }
            if (list.size() > 0)
            {
                opJczxBloodResultBaseMapper.batchOpJczxBloodResultInfo(list);
            }
        }
    }

    /**
     * 修改检测中心血样化验单主
     * 
     * @param opJczxBloodResultBase 检测中心血样化验单主
     * @return 结果
     */
    @Override
    public int updateOpJczxBloodResultBase(OpJczxBloodResultBase opJczxBloodResultBase)
    {
        // 自动填充更新信息
        opJczxBloodResultBase.fillUpdateInfo();
        return opJczxBloodResultBaseMapper.updateOpJczxBloodResultBase(opJczxBloodResultBase);
    }

    @Override
    public int examineBloodResultBase(List<OpBloodEntrustOrderSample> sampleList,String examinePassFlag,String resultNo) {
        // 审核人信息
        String examineUserId = String.valueOf(SecurityUtils.getUserId());
        String examineUser = SecurityUtils.getLoginUser().getUser().getNickName();
        Date examineTime = new Date();
        int totalRows = 0;

        if (CollectionUtil.isEmpty(sampleList)) {
            throw new RuntimeException("未接收到审核项目数据。");
        }
        // --- Logic Split ---

        if ("1".equals(examinePassFlag)) {
            Set<String> orderIdList = new HashSet<>();
            for (OpBloodEntrustOrderSample sample : sampleList) {
                String orderId = entrustOrderSampleMapper.selectOrderIdById(sample.getOpBloodEntrustOrderSampleId());
                orderIdList.add(orderId);

            }
            // 批量更新所有样品的审核信息
            List<String> passedIds = sampleList.stream()
                    .map(OpBloodEntrustOrderSample::getOpBloodEntrustOrderSampleId)
                    .collect(Collectors.toList());
            totalRows = entrustOrderSampleMapper.batchUpdateExamineFields(passedIds, examineUserId, examineUser, examineTime);

            for (String orderId : orderIdList) {
                // b. 检查该委托单下的所有项目是否都已审核
                int unexaminedCount = entrustOrderSampleMapper.countUnexaminedItemsByOrderId(orderId);

                if (unexaminedCount == 0) {
                    // c. 如果所有项目都已审核，更新委托单 的状态为 "4" (已审核)
                    // 状态 4: 已审核
                    bloodEntrustOrderMapper.updateStatusById(orderId, EntrustOrderStatusEnum.YSH.getCode(),examineUser, examineUserId, examineTime,examineUserId,examineTime);
                }
            }

            opJczxBloodResultBaseMapper.updateExamineFlagByResultNo("1",resultNo);

        } else {
            // 1. 检查是否存在非空审核异议 (Determining the single condition)
            List<OpBloodEntrustOrderSample> unPassSamples = sampleList.stream()
                    .filter(sample -> StringUtils.isNotEmpty(sample.getExamineNote()))
                    .collect(Collectors.toList());
            // 更新审核异议 (并设置更新人/时间)
            for (OpBloodEntrustOrderSample sample : unPassSamples) {
                sample.setUpdateBy(examineUserId);
                // 审核异议内容不需要清除，直接更新为传入值（即异议内容）
                totalRows += entrustOrderSampleMapper.updateExamineNoteById(sample);
            }
            //每个样品的审核异议
            Map<String,String> sampleExamineNote = unPassSamples.stream().collect(Collectors.toMap(
                    OpBloodEntrustOrderSample::getSampleNo,
                    OpBloodEntrustOrderSample::getExamineNote,
                    (existing, replacement) -> existing
            ));
            // 更新sample表审核异议
            for (Map.Entry<String, String> entry : sampleExamineNote.entrySet()) {
                opJczxBloodResultInfoMapper.updateExamineNoteBySampleNo(entry.getKey(),entry.getValue());
            }

            // 注意：不更新 examine_user/examine_time，也不流转父单据状态。
            //更新result_base为审核不通过
            opJczxBloodResultBaseMapper.updateExamineFlagByResultNo("0",resultNo);

        }

        return totalRows;

    }

    @Override
    public int cancelExamineBloodResult(String resultNo) {
        String updateUserId = String.valueOf(SecurityUtils.getUserId());
        Date updateTime = new Date();
        // 1. 获取委托单信息并校验状态
        OpJczxBloodResultBase base = opJczxBloodResultBaseMapper.selectByNo(resultNo);
        if (base == null) {
            throw new RuntimeException("未找到导入文件：" + resultNo);
        }
        if (!JczxPcrResultStatusEnum.SHWC.getCode().equals(base.getStatus())) {
            throw new RuntimeException("状态不是已审核，无法取消审核");
        }
        //查询所以info
        OpJczxBloodResultInfo query = new OpJczxBloodResultInfo();
        query.setBaseId(base.getOpJczxBloodResultBaseId());
        List<OpJczxBloodResultInfo> infoList = opJczxBloodResultInfoMapper.selectOpJczxBloodResultInfoList(query);

        Set<String> orderIdSet = new HashSet<>();
        Set<String> orderSampleIdSet = new HashSet<>();
        for (OpJczxBloodResultInfo info : infoList) {
            String orderId = entrustOrderSampleMapper.selectOrderIdById(info.getBloodEntrustOrderSampleId());
            if(StringUtils.isNotEmpty(orderId)){
                orderIdSet.add(orderId);
            }
            orderSampleIdSet.add(info.getBloodEntrustOrderSampleId());
        }
        //将委托单状态改回 "检测完成" (状态码 '3')
        for (String orderId : orderIdSet) {
            bloodEntrustOrderMapper.updateStatusById(orderId, EntrustOrderStatusEnum.JCWC.getCode(),null, null, null,updateUserId,updateTime);
        }


        //更新result_base
        opJczxBloodResultBaseMapper.updateCancelExamine(base.getOpJczxBloodResultBaseId());
        int updatedRows=0;


        // 5. 批量清除审核信息
        List<String> sampleIdList = new ArrayList<>(orderSampleIdSet);
        updatedRows += entrustOrderSampleMapper.batchClearExamineFields(sampleIdList, updateUserId, updateTime);

        return updatedRows;
    }

    @Override
    public String importBloodResult(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("导入文件不能为空！");
        }
        // 复制文件内容到字节数组
        byte[] fileBytes = file.getBytes();
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        String fileId = null; // 在 try 块之外声明
        try {
            // 1. 使用复制的数据读取Excel
            List<BloodResultImportBo> importList = readExcelDataFromBytes(fileBytes);

            // 首先检查是否为null
            if (importList == null) {
                throw new RuntimeException("读取Excel数据失败！");
            }

            // 然后进行过滤操作
            importList = importList.stream()
                    .filter(item -> item != null)
                    .filter(sample -> StringUtils.isNotEmpty(sample.getSampleNo()))
                    .collect(Collectors.toList());

            // 最后检查过滤后的结果是否为空
            if (importList.isEmpty()) {
                throw new RuntimeException("Excel中没有有效数据！");
            }

            // 2. 使用复制的数据保存文件（暂存）
            fileId = sysUploadFileService.uploadFile(fileBytes, originalFilename, contentType);
            // 3. 提取样品编号列表
            List<String> sampleNoList = importList.stream()
                    .map(BloodResultImportBo::getSampleNo)
                    .filter(StringUtils::isNotEmpty)
                    .distinct()
                    .collect(Collectors.toList());

            if (sampleNoList.isEmpty()) {
                sysUploadFileService.deleteFile(fileId);
                throw new RuntimeException("Excel中没有有效的样品编号！");
            }
            //去重
            sampleNoList  = sampleNoList.stream().distinct().collect(Collectors.toList());
            //过滤样品 已审核通过的数据，并返回前端
            List<OpBloodEntrustOrderSample> examineSampleList = entrustOrderSampleMapper.selectExamineBySampleNoList(sampleNoList);
            List<String> exanmineSampleNoList = examineSampleList.stream().map(OpBloodEntrustOrderSample::getSampleNo).collect(Collectors.toList());

            // --- 从 importList 中移除已审核的样品 ---
            importList = importList.stream().filter(a -> !exanmineSampleNoList.contains(a.getSampleNo())).collect(Collectors.toList());
            // --- 从 sampleNoList 中也移除 ---
            sampleNoList.removeAll(exanmineSampleNoList);

            if (importList.isEmpty()) {
                sysUploadFileService.deleteFile(fileId);
                throw new RuntimeException("Excel中的样品均已审核，无可导入数据！");
            }


            // --- 新增：导入前预校验 ---
            List<String> validationErrors = new ArrayList<>();
            Map<String, OpBloodEntrustOrderSample> sampleCache = new HashMap<>(); // 缓存查询结果

            for (int i = 0; i < importList.size(); i++) {
                BloodResultImportBo bo = importList.get(i);

                OpBloodEntrustOrderSample sample = entrustOrderSampleMapper.selectBySampleNo(bo.getSampleNo());
                if (sample == null) {
                    validationErrors.add("Excel行 " + (i + 2) + ": 样品编号 " + bo.getSampleNo() + " 在数据库中不存在。");
                    continue;
                }

                // (已审核的已在 importList 层面被过滤，无需在此重复检查 sample.getExamineUser())

                sampleCache.put(bo.getSampleNo(), sample); // 缓存样品信息，供主循环使用

                // *** 早孕牛号校验 ***
                if("9".equals(sample.getBloodTaskItemType())){ // 9 = 早孕
                    String excelCowNo = bo.getSampleName(); // Excel中的 "牛号"
                    String excelImportCowNo = bo.getImportCowNo(); // Excel中的 "导入牛号"

                    // 将 null 和空字符串视为相同
                    String normalizedCowNo = StringUtils.isEmpty(excelCowNo) ? "" : excelCowNo;
                    String normalizedImportCowNo = StringUtils.isEmpty(excelImportCowNo) ? "" : excelImportCowNo;

                    if (!normalizedCowNo.equals(normalizedImportCowNo)) {
                        validationErrors.add("Excel行 " + (i + 2) + " (样品 " + sample.getSampleNo() + "): '导入牛号' (" + (excelImportCowNo != null ? excelImportCowNo : "") + ") 与 '牛号' (" + (excelCowNo != null ? excelCowNo : "") + ") 不匹配。");
                    }
                }
            }

            // --- 检查校验结果 ---
            if (!validationErrors.isEmpty()) {
                sysUploadFileService.deleteFile(fileId); // 校验失败，删除上传的文件
                throw new RuntimeException("导入失败，数据校验未通过: " + String.join("; ", validationErrors));
            }
            // --- 预校验结束 ---


            String testUserId = String.valueOf(SecurityUtils.getUserId());
            String testUser = SecurityUtils.getLoginUser().getUser().getNickName();
            Set<String> orderIdSet = new HashSet<>();
            Date testTime = new Date();
            // List<String> exanminSampleList = new LinkedList<>(); // (这个变量现在只用于最后的消息返回)

            for (int i = 0; i < importList.size(); i++) {
                BloodResultImportBo bloodResultImportBo = importList.get(i);

                // 行序号，从 1 开始
                int sequence = i + 1;

                // --- 使用预校验中缓存的 sample ---
                OpBloodEntrustOrderSample sample = sampleCache.get(bloodResultImportBo.getSampleNo());

                if(sample==null){
                    // 理论上讲，预校验已覆盖此情况，但保留此防护
                    throw new RuntimeException("Excel中存在异常数据行！");
                }
                // (已审核的检查已在顶层过滤，此处无需重复)
                // if(StringUtils.isNotEmpty(sample.getExamineUser())){...}

                orderIdSet.add(sample.getOpBloodEntrustOrderId());
                String bloodTaskItemType = sample.getBloodTaskItemType();
                OpBloodEntrustOrderSample sampleUpdate = new OpBloodEntrustOrderSample();
                sampleUpdate.setTestUserId(testUserId);
                sampleUpdate.setTestUser(testUser);
                sampleUpdate.setTestTime(testTime);
                sampleUpdate.setFileId(fileId);
                sampleUpdate.setSjph(bloodResultImportBo.getSjph());
                sampleUpdate.setGh(bloodResultImportBo.getGh());
                sampleUpdate.fillUpdateInfo();
                sampleUpdate.setSampleNo(bloodResultImportBo.getSampleNo());

                OpJczxBloodResultInfo info = new OpJczxBloodResultInfo();
                info.setSampleNo(bloodResultImportBo.getSampleNo());
                info.setSampleName(bloodResultImportBo.getSampleName());
                info.setGh(bloodResultImportBo.getGh());
                info.setSjph(bloodResultImportBo.getSjph());
                info.setSequence(sequence); // 赋值为当前行序号
                info.setUpdateBy(testUserId);
                info.setFileId(fileId);

                //口蹄疫
                if("0".equals(bloodTaskItemType)){
                    info.setATestResult(bloodResultImportBo.getATestResult());
                    info.setAYpxj(bloodResultImportBo.getAYpxj());
                    info.setOTestResult(bloodResultImportBo.getOTestResult());
                    info.setOYpxj(bloodResultImportBo.getOYpxj());
                    info.setASjph(bloodResultImportBo.getASjph());
                    info.setOSjph(bloodResultImportBo.getOSjph());
                    opJczxBloodResultInfoMapper.updateResultBySampleNo(info);

                    sampleUpdate.setATestResult(bloodResultImportBo.getATestResult());
                    sampleUpdate.setAYpxj(bloodResultImportBo.getAYpxj());
                    sampleUpdate.setOTestResult(bloodResultImportBo.getOTestResult());
                    sampleUpdate.setOYpxj(bloodResultImportBo.getOYpxj());
                    sampleUpdate.setASjph(bloodResultImportBo.getASjph());
                    sampleUpdate.setOSjph(bloodResultImportBo.getOSjph());
                    entrustOrderSampleMapper.updateResultBySampleNo(sampleUpdate);
                }else if("1".equals(bloodTaskItemType)){//布病（虎红）

                }else if("2".equals(bloodTaskItemType)){//结核（牛分枝杆菌PPD）
                    //结果判定
                    info.setTestResult(bloodResultImportBo.getTestResult());
                    opJczxBloodResultInfoMapper.updateResultBySampleNo(info);

                    sampleUpdate.setTestResult(bloodResultImportBo.getTestResult());
                    entrustOrderSampleMapper.updateResultBySampleNo(sampleUpdate);
                }else if("3".equals(bloodTaskItemType)){//副结核抗体
                    //结果判定
                    info.setPdjg(bloodResultImportBo.getPdjg());
                    info.setSp(bloodResultImportBo.getSp());
                    opJczxBloodResultInfoMapper.updateResultBySampleNo(info);

                    sampleUpdate.setSp(bloodResultImportBo.getSp());
                    sampleUpdate.setPdjg(bloodResultImportBo.getPdjg());
                    entrustOrderSampleMapper.updateResultBySampleNo(sampleUpdate);
                }else if("4".equals(bloodTaskItemType)){//BVDV抗原
                    String sn = bloodResultImportBo.getSn();
                    if (StringUtils.isNotBlank(sn)) {
                        try {
                            // 使用 BigDecimal 处理精度，保留 5 位小数(根据实际需求调整)，四舍五入
                            // stripTrailingZeros() 去除末尾多余的0 (可选)
                            // toPlainString() 防止变成科学计数法
                            String formattedSn = new BigDecimal(sn)
                                    .setScale(5, RoundingMode.HALF_UP)
                                    .stripTrailingZeros()
                                    .toPlainString();
                            info.setSn(formattedSn);
                            sampleUpdate.setSn(formattedSn);
                        } catch (NumberFormatException e) {
                            // 如果导入的不是数字（比如填了文字），则原样保存或报错
                            info.setSn(sn);
                            sampleUpdate.setSn(sn);
                        }
                    }

                    info.setDnh(bloodResultImportBo.getDnh());
                    info.setMnh(bloodResultImportBo.getMnh());
                    info.setPdjg(bloodResultImportBo.getPdjg());
                    info.setTestRemark(bloodResultImportBo.getTestRemark());
                    info.setBw(bloodResultImportBo.getTestRemark());
                    opJczxBloodResultInfoMapper.updateResultBySampleNo(info);


                    sampleUpdate.setDnh(bloodResultImportBo.getDnh());
                    sampleUpdate.setMnh(bloodResultImportBo.getMnh());
                    sampleUpdate.setPdjg(bloodResultImportBo.getPdjg());
                    sampleUpdate.setBw(bloodResultImportBo.getTestRemark());
                    sampleUpdate.setTestRemark(bloodResultImportBo.getTestRemark());
                    entrustOrderSampleMapper.updateResultBySampleNo(sampleUpdate);
                }else if("5".equals(bloodTaskItemType)){//布病抗体

                }else if("6".equals(bloodTaskItemType)){//结核抗体
                    //结果判定
                    info.setTestResult(bloodResultImportBo.getTestResult());
                    info.setSp(bloodResultImportBo.getSp());
                    opJczxBloodResultInfoMapper.updateResultBySampleNo(info);

                    sampleUpdate.setSp(bloodResultImportBo.getSp());
                    sampleUpdate.setTestResult(bloodResultImportBo.getTestResult());
                    entrustOrderSampleMapper.updateResultBySampleNo(sampleUpdate);
                }else if("7".equals(bloodTaskItemType)){//BVDV抗体

                }else if("8".equals(bloodTaskItemType)){//生化
                    //查询生化
                    info.setShZdb(bloodResultImportBo.getShZdb());
                    info.setShBdb(bloodResultImportBo.getShBdb());
                    info.setShZg(bloodResultImportBo.getShZg());
                    info.setShLzg(bloodResultImportBo.getShLzg());
                    info.setShMlz(bloodResultImportBo.getShMlz());
                    info.setShWjl(bloodResultImportBo.getShWjl());
                    info.setShNlz(bloodResultImportBo.getShNlz());
                    info.setShJlz(bloodResultImportBo.getShJlz());
                    info.setShLlz(bloodResultImportBo.getShLlz());
                    info.setShTlz(bloodResultImportBo.getShTlz());
                    info.setShTielz(bloodResultImportBo.getShTielz());
                    info.setShXlz(bloodResultImportBo.getShXlz());
                    info.setShGysz(bloodResultImportBo.getShGysz());
                    info.setShQds(bloodResultImportBo.getShQds());
                    info.setShFzhx(bloodResultImportBo.getShFzhx());
                    info.setShPpt(bloodResultImportBo.getShPpt());
                    info.setShXynsd(bloodResultImportBo.getShXynsd());
                    info.setShGczam(bloodResultImportBo.getShGczam());
                    info.setShGbzam(bloodResultImportBo.getShGbzam());
                    info.setShJxlsm(bloodResultImportBo.getShJxlsm());
                    opJczxBloodResultInfoMapper.updateResultBySampleNo(info);
                    sampleUpdate.setShZdb(bloodResultImportBo.getShZdb());
                    sampleUpdate.setShBdb(bloodResultImportBo.getShBdb());
                    sampleUpdate.setShZg(bloodResultImportBo.getShZg());
                    sampleUpdate.setShLzg(bloodResultImportBo.getShLzg());
                    sampleUpdate.setShMlz(bloodResultImportBo.getShMlz());
                    sampleUpdate.setShWjl(bloodResultImportBo.getShWjl());
                    sampleUpdate.setShNlz(bloodResultImportBo.getShNlz());
                    sampleUpdate.setShJlz(bloodResultImportBo.getShJlz());
                    sampleUpdate.setShLlz(bloodResultImportBo.getShLlz());
                    sampleUpdate.setShTlz(bloodResultImportBo.getShTlz());
                    sampleUpdate.setShTielz(bloodResultImportBo.getShTielz());
                    sampleUpdate.setShXlz(bloodResultImportBo.getShXlz());
                    sampleUpdate.setShGysz(bloodResultImportBo.getShGysz());
                    sampleUpdate.setShQds(bloodResultImportBo.getShQds());
                    sampleUpdate.setShFzhx(bloodResultImportBo.getShFzhx());
                    sampleUpdate.setShPpt(bloodResultImportBo.getShPpt());
                    sampleUpdate.setShXynsd(bloodResultImportBo.getShXynsd());
                    sampleUpdate.setShGczam(bloodResultImportBo.getShGczam());
                    sampleUpdate.setShGbzam(bloodResultImportBo.getShGbzam());
                    sampleUpdate.setShJxlsm(bloodResultImportBo.getShJxlsm());
                    entrustOrderSampleMapper.updateResultBySampleNo(sampleUpdate);
                }else if("9".equals(bloodTaskItemType)){//早孕
                    if("NOT PREGNANT".equals(bloodResultImportBo.getZaoyunTestResult().trim())){
                        sampleUpdate.setZaoyunTestResult("空怀");
                        info.setZaoyunTestResult("空怀");
                    }else if ("PREGNANT".equals(bloodResultImportBo.getZaoyunTestResult().trim())) {
                        sampleUpdate.setZaoyunTestResult("怀孕");
                        info.setZaoyunTestResult("怀孕");
                    }else if ("RECHECK".equals(bloodResultImportBo.getZaoyunTestResult().trim())) {
                        sampleUpdate.setZaoyunTestResult("可疑");
                        info.setZaoyunTestResult("可疑");
                    }else {
                        sampleUpdate.setZaoyunTestResult(bloodResultImportBo.getZaoyunTestResult());
                        info.setZaoyunTestResult(bloodResultImportBo.getZaoyunTestResult());
                    }
                    // (校验已在循环外完成)
                    StringBuilder remark = new StringBuilder();
                    // od值保留三位，四舍五入
                    String odStr = bloodResultImportBo.getOd();
                    if (odStr == null || odStr.trim().isEmpty()) {
                        // 允许空值情况，实验人员跳过该样品
                    }else {
                        try {
                            Double od = Double.parseDouble(odStr.trim());

                            // 可选：添加范围校验
                            if (od < 0) {
                                throw new IllegalArgumentException("OD值不能为负数");
                            }
                            if (Double.isNaN(od) || Double.isInfinite(od)) {
                                throw new IllegalArgumentException("OD值不合法");
                            }
                        } catch (NumberFormatException e) {
                            // 处理格式错误
                             throw new BusinessException("OD值格式错误，请输入有效数字");
                            // 或使用默认值：od = defaultValue;
                        }
                        Double od = Double.parseDouble(bloodResultImportBo.getOd());
                        od = Math.round(od * 1000.0) / 1000.0;

                        // 判断od值，如果od值大于0.100小于0.135，备注就是接近临界点，建议复查

                        if(od>0.100 && od<0.135){
                            remark.append("接近临界点，建议复查");
                        }
                        // 合并备注
                        if(!remark.isEmpty()){
                            remark.append("；");
                        }
                        remark.append(bloodResultImportBo.getTestRemark());
                        info.setOd(od.toString());
                    }
                    info.setImportCowNo(bloodResultImportBo.getImportCowNo());
                    info.setTestRemark(remark.toString());
                    opJczxBloodResultInfoMapper.updateResultBySampleNo(info);

                    sampleUpdate.setOd(bloodResultImportBo.getOd());
                    sampleUpdate.setTestRemark(bloodResultImportBo.getTestRemark());
                    entrustOrderSampleMapper.updateResultBySampleNo(sampleUpdate);
                }
            }

            // (不再需要这个过滤，因为 importList 已经是过滤后的)
            // sampleNoList.removeAll(exanminSampleList);
            // if (sampleNoList.isEmpty()) { ... }

            // 4. 查询已有的Blood检测结果数据
            // (注意: sampleNoList 此时只包含未审核的样品编号)
            List<OpJczxBloodResultBase> existingBaseList = opJczxBloodResultBaseMapper.selectOpJczxBloodResultBaseListBySampleNoList(sampleNoList);
            for (OpJczxBloodResultBase opJczxBloodResultBase : existingBaseList) {
                opJczxBloodResultBase.setFileId(fileId);
                opJczxBloodResultBase.setStatus(JczxBloodResultStatusEnum.HYWC.getCode());
                opJczxBloodResultBase.setTestEndTime(testTime);
                opJczxBloodResultBase.setTestUser(SecurityUtils.getLoginUser().getUser().getNickName());
                opJczxBloodResultBase.setTestUserId(testUserId);
                opJczxBloodResultBase.fillUpdateInfo();
                opJczxBloodResultBaseMapper.updateOpJczxBloodResultBase(opJczxBloodResultBase);
            }

            //更新委托单状态
            // 检查该委托单下的所有样品是否都已检测

            for (String s : orderIdSet) {
                int untestdCount = entrustOrderSampleMapper.countUntestedSamplesByOrderId(s);

                if (untestdCount == 0) {
                    // 如果所有样品都已检测，更新父 OpBloodEntrustOrder 的状态为 "3" (已检测)
                    // 状态 3: 已检测
                    bloodEntrustOrderMapper.updateStatusById(s, EntrustOrderStatusEnum.JCWC.getCode(), null,null,null,testUserId, testTime);
                }
            }

            // 标记文件为正式文件
            sysUploadFileService.markFileAsPermanent(fileId);

            // (exanmineSampleNoList 是最初被过滤掉的已审核列表)
            if(CollectionUtil.isEmpty(exanmineSampleNoList)){
                return "导入成功，共处理 " + importList.size() + " 条数据";
            }else {
                String result = String.join(",", exanmineSampleNoList);
                return "导入成功，共处理 " + importList.size() + " 条数据。另有 " + exanmineSampleNoList.size() + " 条已审核数据被跳过，样品编号为：" + result;
            }

        } catch (Exception e) {
            // 如果文件上传了但后续失败，删除它
            if (fileId != null) {
                sysUploadFileService.deleteFile(fileId);
            }
            throw new RuntimeException("导入失败：" + e.getMessage());
        }
    }


    /**
     * 从字节数组读取Excel数据
     */
    private List<BloodResultImportBo> readExcelDataFromBytes(byte[] fileBytes) throws Exception {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            ExcelUtil<BloodResultImportBo> util = new ExcelUtil<>(BloodResultImportBo.class);
            return util.importExcel(inputStream);
        }
    }


    /**
     * 新版生化结果导入逻辑 (修复版：包含文件上传与关联 fileId 逻辑)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importBloodResultNew(MultipartFile file, String operName) throws Exception {
        if (file.isEmpty()) {
            throw new RuntimeException("导入文件不能为空！");
        }

        // 1. 上传文件获取 fileId (这一步必须在解析前做，或者并发做，确保拿到ID)
        // 为了数据安全，先读取字节流，避免流被关闭
        byte[] fileBytes = file.getBytes();
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();

        // 保存文件到服务器
        String fileId = sysUploadFileService.uploadFile(fileBytes, originalFilename, contentType);

        // 2. 解析 Excel 数据
        List<BloodResultImportBo> importList;
        try (ByteArrayInputStream is = new ByteArrayInputStream(fileBytes)) {
            // 使用自定义解析器 (BloodReportParser)
            importList = BloodReportParser.parse(is);
        }

        if (CollectionUtils.isEmpty(importList)) {
            // 如果解析为空，删除刚刚上传的文件，避免垃圾数据
            sysUploadFileService.deleteFile(fileId);
            throw new RuntimeException("导入数据为空或格式不正确");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder failureMsg = new StringBuilder();
        StringBuilder successMsg = new StringBuilder();

        // 清空缓存
        orderIdToBaseIdCache.clear();
        Map<String, String> orderNoToIdCache = new HashMap<>();

        for (BloodResultImportBo bo : importList) {
            try {
                // 3. 基础校验
                if (StringUtils.isEmpty(bo.getEntrustOrderNo())) {
                    continue; // 跳过无效行
                }
                if (StringUtils.isEmpty(bo.getSampleName())) {
                    failureNum++;
                    failureMsg.append("<br/>委托单[").append(bo.getEntrustOrderNo()).append("]存在缺少牛号的数据，已跳过");
                    continue;
                }

                // 4. 获取委托单ID (带缓存)
                String orderId = orderNoToIdCache.get(bo.getEntrustOrderNo());
                if (orderId == null) {
                    OpBloodEntrustOrder order = bloodEntrustOrderMapper.selectByNo(bo.getEntrustOrderNo());
                    if (order == null) {
                        failureNum++;
                        failureMsg.append("<br/>委托单[").append(bo.getEntrustOrderNo()).append("]不存在");
                        continue;
                    }
                    orderId = order.getOpBloodEntrustOrderId();
                    orderNoToIdCache.put(bo.getEntrustOrderNo(), orderId);
                }

                // 5. 查找样品
                OpBloodEntrustOrderSample targetSample = findSampleByOrderAndName(orderId, bo.getSampleName());
                if (targetSample == null) {
                    failureNum++;
                    failureMsg.append("<br/>委托单[").append(bo.getEntrustOrderNo()).append("]下未找到牛号[").append(bo.getSampleName()).append("]的样品");
                    continue;
                }

                // 6. 保存或更新结果 (传入 fileId)
                saveOrUpdateBloodResultInfo(targetSample, bo, operName, fileId);

                successNum++;
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>牛号[" + bo.getSampleName() + "] 导入异常：" + e.getMessage();
                failureMsg.append(msg);
                log.error(msg, e);
            }
        }

        // 7. 处理上传文件的最终状态
        if (successNum > 0) {
            sysUploadFileService.markFileAsPermanent(fileId);
        } else {
            sysUploadFileService.deleteFile(fileId);
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入出现错误！共 " + failureNum + " 条数据不正确，错误如下：");
            throw new RuntimeException(failureMsg.toString());
        } else {
            successMsg.append("恭喜您，数据已全部导入成功！共 " + successNum + " 条");
        }
        return successMsg.toString();
    }

    /**
     * 辅助方法：查找样品 (保持不变)
     */
    private OpBloodEntrustOrderSample findSampleByOrderAndName(String orderId, String sampleName) {
        List<OpBloodEntrustOrderSample> samples = entrustOrderSampleMapper.selectAllByOpBloodEntrustOrderId(orderId);
        if (CollectionUtil.isEmpty(samples)) return null;
        return samples.stream()
                .filter(s -> s.getSampleName() != null && s.getSampleName().trim().equals(sampleName.trim()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 核心方法：保存或更新结果信息
     * 同时更新 Sample表、Info表、Base表 的 fileId 和检测结果
     */
    private void saveOrUpdateBloodResultInfo(OpBloodEntrustOrderSample sample, BloodResultImportBo bo, String operName, String fileId) {
        // --- 1. 更新 Sample 表 (同步结果 + 关联文件) ---
        // 无论 Info 是否存在，Sample 表都需要更新最新的检测数据和文件ID
        OpBloodEntrustOrderSample sampleUpdate = new OpBloodEntrustOrderSample();
        sampleUpdate.setOpBloodEntrustOrderSampleId(sample.getOpBloodEntrustOrderSampleId());
        sampleUpdate.setFileId(fileId);
        sampleUpdate.setTestUser(operName);
        sampleUpdate.setTestUserId(String.valueOf(SecurityUtils.getUserId()));
        sampleUpdate.setTestTime(new Date());
        sampleUpdate.setTestRemark(bo.getTestRemark());
        // 将 Excel 解析出的生化结果映射到 Sample 对象
        mapBoToSample(bo, sampleUpdate);

        entrustOrderSampleMapper.updateOpBloodEntrustOrderSample(sampleUpdate);


        // --- 2. 处理 Info 表 (Base + Info) ---
        OpJczxBloodResultInfo infoQuery = new OpJczxBloodResultInfo();
        infoQuery.setSampleNo(sample.getSampleNo());
        List<OpJczxBloodResultInfo> infos = opJczxBloodResultInfoMapper.selectOpJczxBloodResultInfoList(infoQuery);

        OpJczxBloodResultInfo info = null;

        if (CollectionUtil.isNotEmpty(infos)) {
            // === 情况A：Info 已存在，执行更新 ===
            info = infos.get(0);
            info.setUpdateBy(operName);
            info.setUpdateTime(DateUtils.getNowDate());
            info.setFileId(fileId); // 更新文件ID

            // 映射结果到 Info 对象
            mapBoToInfo(bo, info);

            opJczxBloodResultInfoMapper.updateOpJczxBloodResultInfo(info);

            // 同时也更新关联的 Base 表的文件ID (保证 Base 指向最新的导入文件)
            updateBaseFileId(info.getBaseId(), fileId, operName);
        } else {
            // === 情况B：Info 不存在，执行新增 (自动创建 Base) ===

            String entrustOrderId = sample.getOpBloodEntrustOrderId();
            OpBloodEntrustOrder order = bloodEntrustOrderMapper.selectOpBloodEntrustOrderByOpBloodEntrustOrderId(entrustOrderId);

            // 获取或创建 BaseID (传入 fileId)
            String baseId = getOrCreateBaseId(entrustOrderId, operName, order, fileId);

            // 创建 Info
            info = new OpJczxBloodResultInfo();
            info.setOpJczxBloodResultInfoId(IdUtils.simpleUUID());
            info.setBaseId(baseId);
            info.setSampleNo(sample.getSampleNo());
            info.setSampleName(sample.getSampleName());
            info.setGh(sample.getGh());
            info.setSequence(sample.getSequence());
            info.setBloodEntrustOrderSampleId(sample.getOpBloodEntrustOrderSampleId());
            info.setFileId(fileId); // 设置文件ID
            if(order != null) {
                info.setDeptName(order.getEntrustDeptName());
            }

            info.setCreateBy(operName);
            info.setCreateTime(DateUtils.getNowDate());
            info.setDeleteId("0");

            // 映射结果到 Info 对象
            mapBoToInfo(bo, info);

            opJczxBloodResultInfoMapper.insertOpJczxBloodResultInfo(info);
        }
    }

    /**
     * 辅助方法：获取或创建化验单主表ID (带 FileId 更新)
     */
    private String getOrCreateBaseId(String orderId, String operName, OpBloodEntrustOrder order, String fileId) {
        // 1. 查缓存
        if (orderIdToBaseIdCache.containsKey(orderId)) {
            String baseId = orderIdToBaseIdCache.get(orderId);
            // 顺便更新一下这个 Base 的 fileId (覆盖为最新上传的)
            updateBaseFileId(baseId, fileId, operName);
            return baseId;
        }

        // 2. 查数据库：该委托单是否已有样品生成了结果记录
        List<OpBloodEntrustOrderSample> samples = entrustOrderSampleMapper.selectAllByOpBloodEntrustOrderId(orderId);
        if (CollectionUtil.isNotEmpty(samples)) {
            for (OpBloodEntrustOrderSample s : samples) {
                OpJczxBloodResultInfo q = new OpJczxBloodResultInfo();
                q.setSampleNo(s.getSampleNo());
                List<OpJczxBloodResultInfo> res = opJczxBloodResultInfoMapper.selectOpJczxBloodResultInfoList(q);
                if (CollectionUtil.isNotEmpty(res)) {
                    String existingBaseId = res.get(0).getBaseId();
                    orderIdToBaseIdCache.put(orderId, existingBaseId);

                    // 找到已存在的 Base，更新其 fileId
                    updateBaseFileId(existingBaseId, fileId, operName);
                    return existingBaseId;
                }
            }
        }

        // 3. 创建新 Base
        OpJczxBloodResultBase newBase = new OpJczxBloodResultBase();
        newBase.setOpJczxBloodResultBaseId(IdUtils.simpleUUID());

        try {
            newBase.setResultNo(codeGeneratorUtil.generateBloodResultNo());
        } catch (Exception e) {
            newBase.setResultNo("HY" + DateUtils.dateTimeNow() + (int)(Math.random() * 100));
        }

        if (order != null) {
            newBase.setBloodTaskItemType(order.getBloodTaskItemType());
        }

        newBase.setStatus("2"); // 默认状态：化验完成
        newBase.setTestUser(operName);
        newBase.setTestUserId(String.valueOf(SecurityUtils.getUserId()));
        newBase.setTestDate(new Date());
        newBase.setTestEndTime(new Date());
        newBase.setFileId(fileId); // **记录 fileId**

        newBase.setCreateBy(operName);
        newBase.setCreateTime(new Date());
        newBase.setDeleteId("0");

        opJczxBloodResultBaseMapper.insertOpJczxBloodResultBase(newBase);

        orderIdToBaseIdCache.put(orderId, newBase.getOpJczxBloodResultBaseId());
        return newBase.getOpJczxBloodResultBaseId();
    }

    /**
     * 辅助方法：单独更新 Base 表的文件ID
     */
    private void updateBaseFileId(String baseId, String fileId, String operName) {
        OpJczxBloodResultBase baseUpdate = new OpJczxBloodResultBase();
        baseUpdate.setOpJczxBloodResultBaseId(baseId);
        baseUpdate.setFileId(fileId);
        baseUpdate.setUpdateBy(operName);
        baseUpdate.setUpdateTime(new Date());
        opJczxBloodResultBaseMapper.updateOpJczxBloodResultBase(baseUpdate);
    }

    /**
     * 辅助方法：BO -> Info 映射
     */
    private void mapBoToInfo(BloodResultImportBo bo, OpJczxBloodResultInfo info) {
        if (StringUtils.isNotEmpty(bo.getShZdb())) info.setShZdb(bo.getShZdb());
        if (StringUtils.isNotEmpty(bo.getShBdb())) info.setShBdb(bo.getShBdb());
        if (StringUtils.isNotEmpty(bo.getShZg())) info.setShZg(bo.getShZg());
        if (StringUtils.isNotEmpty(bo.getShLzg())) info.setShLzg(bo.getShLzg());
        if (StringUtils.isNotEmpty(bo.getShMlz())) info.setShMlz(bo.getShMlz());
        if (StringUtils.isNotEmpty(bo.getShWjl())) info.setShWjl(bo.getShWjl());
        if (StringUtils.isNotEmpty(bo.getShNlz())) info.setShNlz(bo.getShNlz());
        if (StringUtils.isNotEmpty(bo.getShJlz())) info.setShJlz(bo.getShJlz());
        if (StringUtils.isNotEmpty(bo.getShLlz())) info.setShLlz(bo.getShLlz());
        if (StringUtils.isNotEmpty(bo.getShTlz())) info.setShTlz(bo.getShTlz());
        if (StringUtils.isNotEmpty(bo.getShTielz())) info.setShTielz(bo.getShTielz());
        if (StringUtils.isNotEmpty(bo.getShXlz())) info.setShXlz(bo.getShXlz());
        if (StringUtils.isNotEmpty(bo.getShGysz())) info.setShGysz(bo.getShGysz());
        if (StringUtils.isNotEmpty(bo.getShQds())) info.setShQds(bo.getShQds());
        if (StringUtils.isNotEmpty(bo.getShFzhx())) info.setShFzhx(bo.getShFzhx());
        if (StringUtils.isNotEmpty(bo.getShPpt())) info.setShPpt(bo.getShPpt());
        if (StringUtils.isNotEmpty(bo.getShXynsd())) info.setShXynsd(bo.getShXynsd());
        if (StringUtils.isNotEmpty(bo.getShGczam())) info.setShGczam(bo.getShGczam());
        if (StringUtils.isNotEmpty(bo.getShGbzam())) info.setShGbzam(bo.getShGbzam());
        if (StringUtils.isNotEmpty(bo.getShJxlsm())) info.setShJxlsm(bo.getShJxlsm());
        if (StringUtils.isNotEmpty(bo.getTestRemark())) {
            info.setTestRemark(bo.getTestRemark());
        }
    }

    /**
     * 辅助方法：BO -> Sample 映射 (新增，用于同步更新样品表数据)
     */
    private void mapBoToSample(BloodResultImportBo bo, OpBloodEntrustOrderSample sample) {
        if (StringUtils.isNotEmpty(bo.getShZdb())) sample.setShZdb(bo.getShZdb());
        if (StringUtils.isNotEmpty(bo.getShBdb())) sample.setShBdb(bo.getShBdb());
        if (StringUtils.isNotEmpty(bo.getShZg())) sample.setShZg(bo.getShZg());
        if (StringUtils.isNotEmpty(bo.getShLzg())) sample.setShLzg(bo.getShLzg());
        if (StringUtils.isNotEmpty(bo.getShMlz())) sample.setShMlz(bo.getShMlz());
        if (StringUtils.isNotEmpty(bo.getShWjl())) sample.setShWjl(bo.getShWjl());
        if (StringUtils.isNotEmpty(bo.getShNlz())) sample.setShNlz(bo.getShNlz());
        if (StringUtils.isNotEmpty(bo.getShJlz())) sample.setShJlz(bo.getShJlz());
        if (StringUtils.isNotEmpty(bo.getShLlz())) sample.setShLlz(bo.getShLlz());
        if (StringUtils.isNotEmpty(bo.getShTlz())) sample.setShTlz(bo.getShTlz());
        if (StringUtils.isNotEmpty(bo.getShTielz())) sample.setShTielz(bo.getShTielz());
        if (StringUtils.isNotEmpty(bo.getShXlz())) sample.setShXlz(bo.getShXlz());
        if (StringUtils.isNotEmpty(bo.getShGysz())) sample.setShGysz(bo.getShGysz());
        if (StringUtils.isNotEmpty(bo.getShQds())) sample.setShQds(bo.getShQds());
        if (StringUtils.isNotEmpty(bo.getShFzhx())) sample.setShFzhx(bo.getShFzhx());
        if (StringUtils.isNotEmpty(bo.getShPpt())) sample.setShPpt(bo.getShPpt());
        if (StringUtils.isNotEmpty(bo.getShXynsd())) sample.setShXynsd(bo.getShXynsd());
        if (StringUtils.isNotEmpty(bo.getShGczam())) sample.setShGczam(bo.getShGczam());
        if (StringUtils.isNotEmpty(bo.getShGbzam())) sample.setShGbzam(bo.getShGbzam());
        if (StringUtils.isNotEmpty(bo.getShJxlsm())) sample.setShJxlsm(bo.getShJxlsm());
        if (StringUtils.isNotEmpty(bo.getTestRemark())) {
            sample.setTestRemark(bo.getTestRemark());
        }
    }
}
