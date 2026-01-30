package com.gmlimsqi.business.labtest.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.labtest.domain.OpJczxBloodResultInfo;
import com.gmlimsqi.business.labtest.bo.OpJczxBloodTestModelBo;
import com.gmlimsqi.business.labtest.bo.OpJczxPcrTestModelBo;
import com.gmlimsqi.business.labtest.domain.*;
import com.gmlimsqi.business.labtest.dto.BiochemistryItem;
import com.gmlimsqi.business.labtest.dto.OpJczxBloodResultDto;
import com.gmlimsqi.business.labtest.dto.OpJczxPcrResultDto;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.mapper.*;
import com.gmlimsqi.business.labtest.service.*;
import com.gmlimsqi.business.labtest.vo.OpJczxTestTaskVo;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.common.enums.EntrustOrderStatusEnum;
import com.gmlimsqi.common.enums.EntrustOrderTypeEnum;
import com.gmlimsqi.common.enums.JczxFeedReportStatusEnum;
import com.gmlimsqi.common.enums.JczxPcrResultStatusEnum;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.utils.DictUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.system.service.ISysConfigService;
import com.gmlimsqi.system.service.ISysUploadFileService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 检测中心检测任务Service业务层处理
 * * @author hhy
 * @date 2025-09-22
 */
@Service
public class OpJczxTestTaskServiceImpl implements IOpJczxTestTaskService
{
    @Autowired
    private OpJczxTestTaskMapper opJczxTestTaskMapper;
    @Autowired
    private OpPcrEntrustOrderItemMapper pcrEntrustOrderItemMapper;
    @Autowired
    private OpPcrEntrustOrderSampleMapper pcrEntrustOrderSampleMapper;
    @Autowired
    private OpPcrEntrustOrderMapper pcrEntrustOrderMapper;
    @Autowired
    private IOpJczxPcrResultBaseService pcrResultBaseService;
    @Autowired
    private OpJczxPcrResultInfoMapper pcrResultInfoMapper;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;
    @Autowired
    private OpBloodEntrustOrderSampleMapper bloodEntrustOrderSampleMapper;
    @Autowired
    private OpBloodEntrustOrderMapper bloodEntrustOrderMapper;
    @Autowired
    private IOpJczxBloodResultBaseService opJczxBloodResultBaseService;
    @Autowired
    private OpFeedEntrustOrderSampleMapper feedEntrustOrderSampleMapper;
    @Autowired
    private OpJczxPcrResultBaseMapper opJczxPcrResultBaseMapper;
    @Autowired
    private OpJczxBloodResultBaseMapper opJczxBloodResultBaseMapper;
    @Autowired
    private OpJczxBloodResultInfoMapper bloodResultInfoMapper;
    @Autowired
    private ISysUploadFileService sysUploadFileService;
    @Autowired
    private IOpJczxBloodReportBaseService opJczxBloodReportBaseService;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private OpJczxBloodReportBaseMapper opJczxBloodReportBaseMapper;
    @Override
    public OpJczxTestTaskVo selectOpJczxTestTaskByOpJczxTestTaskId(String entrustOrderItemId)
    {
        return opJczxTestTaskMapper.selectTestTaskByEntrustOrderItemId(entrustOrderItemId);
    }

    /**
     * 查询检测任务列表（统一入口）
     * <p>
     * 根据【委托类型 + 是否已化验 + 当前Tab给定的状态 + 检测方法】
     * 决定最终调用哪一条 Mapper 查询 SQL
     */
    @Override
    public List<OpJczxTestTaskVo> selectOpJczxTestTaskList(OpJczxTestTaskDto opJczxTestTaskDto) {

        /* ===================== ① 通用前置处理 ===================== */

        // 如果前端传了“接收结束时间”，统一处理为当天 23:59:59
        // 目的：避免只查到当天 00:00:00，导致漏数据
        if (opJczxTestTaskDto.getEndReceiveTime() != null) {
            setEndOfDay(opJczxTestTaskDto.getEndReceiveTime());
        }
        // 设置当前登录用户 ID（用于数据权限 / 人员过滤）
        opJczxTestTaskDto.setUserId(SecurityUtils.getUserId().toString());

        /* ===================== ② 血样 / PCR 的时间字段特殊处理 ===================== */
        // 血样和 PCR 在不同 Tab 下，查询的“时间字段”不一样
        // 待审核 Tab → 按检测时间查
        // 已审核 Tab → 按审核时间查
        if (opJczxTestTaskDto.getEntrustOrderType().equals(EntrustOrderTypeEnum.BLOOD.getCode())) {
            // 血样
            if ("2".equals(opJczxTestTaskDto.getStatus())) {
                // Tab：待审核（按检测时间查询）
                if (opJczxTestTaskDto.getEndTestDate() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndTestDate());
                }
                if (opJczxTestTaskDto.getEndTestEndTime() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndTestEndTime());
                }
            } else if ("3".equals(opJczxTestTaskDto.getStatus())) {
                // Tab：已审核（按审核时间查询）
                if (opJczxTestTaskDto.getEndExamineTime() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndExamineTime());
                }
            }
        } else if (opJczxTestTaskDto.getEntrustOrderType().equals(EntrustOrderTypeEnum.PCR.getCode())) {
            // PCR（逻辑与血样一致）
            if ("2".equals(opJczxTestTaskDto.getStatus())) {
                // 待审核 → 查检测时间
                if (opJczxTestTaskDto.getEndTestDate() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndTestDate());
                }
                if (opJczxTestTaskDto.getEndTestEndTime() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndTestEndTime());
                }
            } else if ("3".equals(opJczxTestTaskDto.getStatus())) {
                // 已审核 → 查审核时间
                if (opJczxTestTaskDto.getEndExamineTime() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndExamineTime());
                }
            }
        }

        /* ===================== ③ 提取常用判断字段 ===================== */
        String type = opJczxTestTaskDto.getEntrustOrderType(); // 委托类型（饲料 / 血样 / PCR）
        String status = opJczxTestTaskDto.getStatus();         // Tab 状态（0/1/2/3）
        String testMethod = opJczxTestTaskDto.getTestMethod(); // 检测方法（饲料专用）
        String isTest = opJczxTestTaskDto.getIsTest();         // 是否已化验
        String queryCsf = opJczxTestTaskDto.getQueryCsf();     // 是否查询初水分
        String bloodTaskItemType = opJczxTestTaskDto.getBloodTaskItemType(); // 血样项目类型

        Set<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        /* ===================== ④ 按委托类型分流 ===================== */
        /* ========== 饲料检测 ========== */
        if (EntrustOrderTypeEnum.FEED.getCode().equals(type)) {
            // 饲料检测需要先区分检测方法
            // 1 = 近红外，其他 = 化学法
            if ("1".equals(testMethod)) {
                // 近红外：使用新的“分组查询”逻辑
                return opJczxTestTaskMapper.selectFeedTestTaskListGrouped(opJczxTestTaskDto);
            } else {
                // 化学法
                if ("0".equals(queryCsf)) {
                    // 普通化学法（非初水分）
                    opJczxTestTaskDto.setIsTest("0");
                    return opJczxTestTaskMapper.selectFeedTestTaskList(opJczxTestTaskDto);
                } else {
                    // 初水分查询
                    // 业务含义：查询“尚未生成水分化验单”的样品
                    String csfItemId = configService
                            .selectConfigByKey("csfItemId")
                            .trim();
                    opJczxTestTaskDto.setCsfItemId(csfItemId);
                    return opJczxTestTaskMapper.selectCsfTestTaskList(opJczxTestTaskDto);
                }
            }
            /* ========== 血样检测 ========== */
        } else if (EntrustOrderTypeEnum.BLOOD.getCode().equals(type)) {
            // 先按“是否已化验”区分
            if ("0".equals(isTest)) {
                // 待化验
                if (opJczxTestTaskDto.getEndReceiveTime() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndReceiveTime());
                }
                // 特殊血样项目（如 8 = 送检后审核类）
                if ("8".equals(bloodTaskItemType)) {
                    return opJczxTestTaskMapper.selectShTestTaskList(opJczxTestTaskDto);
                } else {
                    return opJczxTestTaskMapper.selectBloodTestTaskList(opJczxTestTaskDto);
                }
            } else if ("1".equals(isTest)) {
                // 已化验
                if (opJczxTestTaskDto.getEndTestDate() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndTestDate());
                }
                return opJczxTestTaskMapper.selectBloodTestTaskListIsTest(opJczxTestTaskDto);
            }else if ("2".equals(isTest)) {
                // 化验中
                if (opJczxTestTaskDto.getEndTestDate() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndTestDate());
                }
                return opJczxTestTaskMapper.selectBloodTestTaskListIsTest(opJczxTestTaskDto);
            }
            // 再按 Tab 状态区分（审核维度）
            if ("2".equals(status)) {
                // 待审核
                if (opJczxTestTaskDto.getEndTestDate() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndTestDate());
                }
                return opJczxTestTaskMapper.selectBloodExamine(opJczxTestTaskDto);
            } else if ("3".equals(status)) {
                // 已审核
                return opJczxTestTaskMapper.selectBloodAuditedList(opJczxTestTaskDto);
            }
            return null;

            /* ========== PCR 检测 ========== */
        } else if (EntrustOrderTypeEnum.PCR.getCode().equals(type)) {
            if ("0".equals(isTest)) {
                // 待化验
                if (opJczxTestTaskDto.getEndReceiveTime() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndReceiveTime());
                }
                return opJczxTestTaskMapper.selectPcrTestTaskList(opJczxTestTaskDto);
            } else if ("1".equals(isTest)) {
                // 已化验
                if (opJczxTestTaskDto.getEndTestDate() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndTestDate());
                }
                if (opJczxTestTaskDto.getEndTestEndTime() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndTestEndTime());
                }
                return opJczxTestTaskMapper.selectPcrTestTaskListIsTest(opJczxTestTaskDto);
            }else if ("2".equals(isTest)) {
                // 化验中
                if (opJczxTestTaskDto.getEndTestDate() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndTestDate());
                }
                if (opJczxTestTaskDto.getEndTestEndTime() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndTestEndTime());
                }
                return opJczxTestTaskMapper.selectPcrTestTaskListIsTest(opJczxTestTaskDto);
            }
            if ("2".equals(status)) {
                // 待审核
                if (opJczxTestTaskDto.getEndTestDate() != null) {
                    setEndOfDay(opJczxTestTaskDto.getEndTestDate());
                }
                return opJczxTestTaskMapper.selectPcrExamine(opJczxTestTaskDto);
            } else if ("3".equals(status)) {
                // 已审核
                return opJczxTestTaskMapper.selectPcrAuditedList(opJczxTestTaskDto);
            }
            return null;
        }
        return null;
    }

    // 辅助方法，设置时间为当天的23:59:59
    private void setEndOfDay(Date date) {
        if (date == null) return;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999); // 设置毫秒确保覆盖全天
        // 注意：这里修改的是传入的Date对象本身，如果需要返回新的Date对象，需要clone
        date.setTime(calendar.getTimeInMillis());
    }
        @Override
        public void generatePcrTestExcel(HttpServletResponse response, OpJczxTestTaskDto dto) {
            try {
                List<OpJczxPcrTestModelBo> sampleList; // 1. 声明 sampleList

                //已化验页面传resultNo下载。待化验传委托单号项目类别
                if(StringUtils.isNotEmpty(dto.getResultNo())){
                    // 2. (已化验) - 直接调用 selectSampleModelByResultNo
                    //    (我们将在 XML 中修改这个查询，让它自动按 info.sequence 排序)
                    sampleList = pcrEntrustOrderSampleMapper.
                            selectSampleModelByResultNo(dto.getResultNo());
                }else {
                    // 3. (待化验) - 必须在这里使用 Java 重新排序

                    // 3a. 从数据库获取无序的样品列表
                    List<OpJczxPcrTestModelBo> unsortedSampleList = pcrEntrustOrderSampleMapper.
                            selectSampleModelByNoList(dto.getEntrustOrderNoList(),dto.getPcrTaskItemType());

                    // 3b. 获取前端传递的、我们期望的“有序”委托单号列表
                    List<String> orderedEntrustOrderNos = dto.getEntrustOrderNoList();

                    // 3c. 将无序的样品列表按委托单号分组，放入 Map 中
                    // (!! 关键假设: OpJczxPcrTestModelBo 必须有一个 getEntrustOrderNo() 方法 !!)
                    // (您需要确保 selectSampleModelByNoList 查询返回了 entrustOrderNo 字段)
                    Map<String, List<OpJczxPcrTestModelBo>> sampleMap = unsortedSampleList.stream()
                            .collect(Collectors.groupingBy(OpJczxPcrTestModelBo::getEntrustOrderNo)); // 假设 getEntrustOrderNo() 存在

                    // 3d. 根据“有序”的委托单号列表，遍历 Map，构建新的“有序”样品列表
                    sampleList = new ArrayList<>(); // 赋值给 sampleList
                    for (String orderNo : orderedEntrustOrderNos) {
                        List<OpJczxPcrTestModelBo> samplesForThisOrder = sampleMap.get(orderNo);
                        if (samplesForThisOrder != null) {
                            // (推荐) 在委托单内部，再按样品本身的序号排序
                            samplesForThisOrder.sort(Comparator.comparing(OpJczxPcrTestModelBo::getSequence,
                                    Comparator.nullsLast(Comparator.naturalOrder())));
                            sampleList.addAll(samplesForThisOrder);
                        }
                    }
                }


                // 4. (检查项目是否相同 - 逻辑不变)
                if (sampleList.size() > 0) {
                    String firstItemType = sampleList.get(0).getPcrTaskItemType();
                    boolean allSame = sampleList.stream()
                            .allMatch(sample -> firstItemType.equals(sample.getPcrTaskItemType()));
                    if (!allSame) {
                        throw new RuntimeException("样本的项目必须相同");
                    }
                }

                // 5. (设置响应头 - 逻辑不变)
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setCharacterEncoding("utf-8");

                // 6. (读取模板并填充数据 - 逻辑不变)
                //    此时传入的 sampleList 已经是排序好的
                // 1. 获取 pcrTaskItemType
                String pcrTaskItemType = sampleList.isEmpty() ? dto.getPcrTaskItemType() : sampleList.get(0).getPcrTaskItemType();
                // 2. 传入 dto.getBlTemplateType()
                fillExcelTemplate(response, sampleList, pcrTaskItemType, dto.getBlTemplateType());

            } catch (Exception e) {
                throw new RuntimeException("生成Excel失败: " + e.getMessage());
            }
        }

    @Override
    public void generateBloodTestExcel(HttpServletResponse response, OpJczxTestTaskDto dto) {
        try {

            List<OpJczxBloodTestModelBo> bsampleList; // 1. 声明 bsampleList

            //已化验页面传resultNo下载。待化验传委托单号
            if(StringUtils.isNotEmpty(dto.getResultNo())){
                // 2. (已化验) - 直接调用 selectSampleModelByResultNo
                //    我们将在 XML 中修改这个查询，让它自动按 info.sequence 排序
                bsampleList = bloodEntrustOrderSampleMapper.
                        selectSampleModelByResultNo(dto.getResultNo());
            }else {
                // 3. (待化验) - 必须在这里使用 Java 重新排序

                // 3a. 从数据库获取无序的样品列表
                List<OpJczxBloodTestModelBo> unsortedBsampleList = bloodEntrustOrderSampleMapper.selectSampleModelByIdList(dto.getEntrustOrderIdList());

                // 3b. 获取前端传递的、我们期望的“有序”委托单ID列表
                List<String> orderedEntrustOrderIds = dto.getEntrustOrderIdList();

                // 3c. 将无序的样品列表按委托单ID分组，放入 Map 中
                // (根据 selectSampleModelByIdList 的
                //  LEFT JOIN op_blood_entrust_order_sample opeos ON opbeo.op_blood_entrust_order_id = opeos.op_blood_entrust_order_id
                Map<String, List<OpJczxBloodTestModelBo>> sampleMap = unsortedBsampleList.stream()
                        .collect(Collectors.groupingBy(OpJczxBloodTestModelBo::getEntrustOrderId));

                // 3d. 根据“有序”的委托单ID列表，遍历 Map，构建新的“有序”样品列表
                bsampleList = new ArrayList<>(); // 赋值给 bsampleList
                for (String orderId : orderedEntrustOrderIds) {
                    List<OpJczxBloodTestModelBo> samplesForThisOrder = sampleMap.get(orderId);
                    if (samplesForThisOrder != null) {
                        // (推荐) 在委托单内部，再按样品本身的序号排序
                        samplesForThisOrder.sort(Comparator.comparing(OpJczxBloodTestModelBo::getSequence,
                                Comparator.nullsLast(Comparator.naturalOrder())));
                        bsampleList.addAll(samplesForThisOrder);
                    }
                }
            }

            // 4. (检查项目是否相同 - 逻辑不变)
            if (bsampleList.size() > 0) {
                String bloodTaskItemType = bsampleList.get(0).getBloodTaskItemType();
                boolean allSame = bsampleList.stream()
                        .allMatch(sample -> bloodTaskItemType.equals(sample.getBloodTaskItemType()));
                if (!allSame) {
                    throw new RuntimeException("样本的项目必须相同");
                }
            }

            // 5. (设置响应头 - 逻辑不变)
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");

            // 6. (读取模板并填充数据 - 逻辑不变)
            //    此时传入的 bsampleList 已经是排序好的
            fillExcelBloodTemplate(response, bsampleList, bsampleList.get(0).getBloodTaskItemType());

        } catch (Exception e) {
            throw new RuntimeException("生成Excel失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public String beginPcrTask(OpJczxTestTaskDto dto) {

        // 1. (待化验) - 在这里使用 Java 重新排序

        // 1a. 从数据库获取无序的样品列表
        List<OpJczxPcrTestModelBo> unsortedSampleList = pcrEntrustOrderSampleMapper.
                selectSampleModelByNoList(dto.getEntrustOrderNoList(),dto.getPcrTaskItemType());

        // 1b. 获取前端传递的、我们期望的“有序”委托单号列表
        List<String> orderedEntrustOrderNos = dto.getEntrustOrderNoList();

        Map<String, List<OpJczxPcrTestModelBo>> sampleMap = unsortedSampleList.stream()
                .collect(Collectors.groupingBy(OpJczxPcrTestModelBo::getEntrustOrderNo));

        // 1d. 根据“有序”的委托单号列表，构建新的“有序”样品列表
        List<OpJczxPcrTestModelBo> sampleList = new ArrayList<>(); // 这是排序后的列表
        for (String orderNo : orderedEntrustOrderNos) {
            List<OpJczxPcrTestModelBo> samplesForThisOrder = sampleMap.get(orderNo);
            if (samplesForThisOrder != null) {
                // (推荐) 在委托单内部，再按样品本身的序号排序
                samplesForThisOrder.sort(Comparator.comparing(OpJczxPcrTestModelBo::getSequence,
                        Comparator.nullsLast(Comparator.naturalOrder())));
                sampleList.addAll(samplesForThisOrder);
            }
        }


        // 2. (检查项目是否相同 - 逻辑不变)
        if (sampleList.size() > 0) {
            String firstItemType = sampleList.get(0).getPcrTaskItemType();
            boolean allSame = sampleList.stream()
                    .allMatch(sample -> firstItemType.equals(sample.getPcrTaskItemType()));
            if (!allSame) {
                throw new RuntimeException("样本的项目必须相同");
            }
        }else {
            throw new RuntimeException("不存在有效样品，请检查样品编号");
        }

        // 3. (更新状态 - 逻辑不变)
        pcrEntrustOrderMapper.updateOrderStatusByNoList(dto.getEntrustOrderNoList(),dto.getPcrTaskItemType());

        // 4. (检查是否已存在 - 逻辑不变)
        List<String> sampleNoList = sampleList.stream().map(OpJczxPcrTestModelBo::getSampleNo).collect(Collectors.toList());
        List<OpJczxPcrResultBase> opJczxPcrResultBases = opJczxPcrResultBaseMapper.selectOpJczxPcrResultBaseListBySampleNoList(sampleNoList);

        if(CollectionUtil.isEmpty(opJczxPcrResultBases)){
            // 5. (!! 关键 !!)
            //    调用 buildResultInfo，传入我们“排序后”的 sampleList
            String resultNo = buildResultInfo(sampleList, dto.getBlTemplateType());
            String itemName = DictUtils.getDictLabel("pcr_task_item_type",sampleList.get(0).getPcrTaskItemType());
            String fileName = "PCR"+itemName+"检测表格_" + resultNo.substring(resultNo.length() - 9) + ".xlsx";
            opJczxPcrResultBaseMapper.updateFileNameByResultNo(resultNo,fileName);
            return resultNo;
        }else {
            return opJczxPcrResultBases.get(0).getResultNo();
        }

    }

    @Override
    @Transactional
    public String beginBloodTask(List<String> entrustOrderIdList) {

        // 1. (待化验) - 同样在这里使用 Java 重新排序

        // 1a. 从数据库获取无序的样品列表
        List<OpJczxBloodTestModelBo> unsortedBsampleList = bloodEntrustOrderSampleMapper.
                selectSampleModelByIdList(entrustOrderIdList);

        //     请检查你的 OpJczxBloodTestModelBo 定义，并确保 selectSampleModelByIdList 查询填充了
        //     opeos.op_blood_entrust_order_id 字段 (它在XML里，但BO里可能没有)
        //     如果
        Map<String, List<OpJczxBloodTestModelBo>> sampleMap = unsortedBsampleList.stream()
                .collect(Collectors.groupingBy(OpJczxBloodTestModelBo::getEntrustOrderId)); // 假设 getEntrustOrderId() 存在

        // 1c. 根据“有序”的委托单ID列表，构建新的“有序”样品列表
        List<OpJczxBloodTestModelBo> bsampleList = new ArrayList<>(); // 这是排序后的列表
        for (String orderId : entrustOrderIdList) {
            List<OpJczxBloodTestModelBo> samplesForThisOrder = sampleMap.get(orderId);
            if (samplesForThisOrder != null) {
                // (推荐) 在委托单内部，再按样品本身的序号排序
                samplesForThisOrder.sort(Comparator.comparing(OpJczxBloodTestModelBo::getSequence,
                        Comparator.nullsLast(Comparator.naturalOrder())));
                bsampleList.addAll(samplesForThisOrder);
            }
        }


        // 2. (检查项目是否相同 - 逻辑不变)
        if (bsampleList.size() > 0) {
            String firstItemType = bsampleList.get(0).getBloodTaskItemType();
            boolean allSame = bsampleList.stream()
                    .allMatch(sample -> firstItemType.equals(sample.getBloodTaskItemType()));
            if (!allSame) {
                throw new RuntimeException("样本的项目必须相同");
            }
        }else {
            throw new RuntimeException("不存在有效样品，请检查样品编号");
        }

        // 3. (更新状态 - 逻辑不变)
        bloodEntrustOrderMapper.updateOrderStatusByOrderIdList(entrustOrderIdList);


        // 4. (检查是否已存在 - 逻辑不变)
        List<String> sampleNoList = bsampleList.stream().map(OpJczxBloodTestModelBo::getSampleNo).collect(Collectors.toList());
        List<OpJczxBloodResultBase> opJczxBloodResultBases = opJczxBloodResultBaseMapper.selectOpJczxBloodResultBaseListBySampleNoList(sampleNoList);

        if(CollectionUtil.isEmpty(opJczxBloodResultBases)){
            // 5. (!! 关键 !!)
            //    调用 buildBloodResultInfo，传入我们“排序后”的 bsampleList
            String resultNo = buildBloodResultInfo(bsampleList);
            String itemName = DictUtils.getDictLabel("blood_task_item_type",bsampleList.get(0).getBloodTaskItemType());
            String fileName = "血样"+itemName+"检测表格_" + resultNo.substring(resultNo.length() - 9) + ".xlsx";
            opJczxBloodResultBaseMapper.updateFileNameByResultNo(resultNo,fileName);
            return resultNo;
        }else {
            return opJczxBloodResultBases.get(0).getResultNo();
        }

    }

    @Override
    @Transactional
    public void savePcrResultInfoList(OpJczxPcrResultDto param) {
        List<OpPcrEntrustOrderItem> itemList = param.getTestItem();
        String resultNo = param.getResultNo();
        //判断是否已审核
        List<String> collect = itemList.stream().map(OpPcrEntrustOrderItem::getOpPcrEntrustOrderItemId).collect(Collectors.toList());
//        int count = pcrEntrustOrderItemMapper.selectExaminCountByIdList(collect);
//        if(count>0){
//            throw new RuntimeException("存在已审核数据,无法修改");
//        }

        for (OpPcrEntrustOrderItem item : itemList) {
            pcrEntrustOrderItemMapper.updateOpPcrEntrustOrderItem(item);
            OpJczxPcrResultInfo opJczxPcrResultInfo = new OpJczxPcrResultInfo();
            BeanUtils.copyProperties(item,opJczxPcrResultInfo);
            pcrResultInfoMapper.updateResultBySampleNoItemName(opJczxPcrResultInfo);
        }
        // 审核人信息
        String examineUserId = String.valueOf(SecurityUtils.getUserId());
        String examineUser = SecurityUtils.getLoginUser().getUser().getNickName();
        Date examineTime = new Date();
        //更新为已审核

            // Case 1: 审核通过 (所有 remarks 都是空的)
            List<String> passedIds = itemList.stream()
                    .map(OpPcrEntrustOrderItem::getOpPcrEntrustOrderItemId)
                    .collect(Collectors.toList());

            // a. 批量更新所有项目的审核信息
            pcrEntrustOrderItemMapper.batchUpdateExamineFields(passedIds, examineUserId, examineUser, examineTime);

            //查询所有委托单
            Set<String> orderIdSet = new HashSet<>();
            for (OpPcrEntrustOrderItem item : itemList) {
                String opPcrEntrustOrderId = pcrEntrustOrderItemMapper.selectEntrustOrderIdByItemId(item.getOpPcrEntrustOrderItemId());
                orderIdSet.add(opPcrEntrustOrderId);

            }
            for (String orderId : orderIdSet) {
                // b. 检查该委托单下的所有项目是否都已审核
                int unexaminedCount = pcrEntrustOrderItemMapper.countUnexaminedItemsByOrderId(orderId);

                if (unexaminedCount == 0) {
                    // c. 如果所有项目都已审核，更新父 OpPcrEntrustOrder 的状态为 "4" (已审核)
                    // 状态 4: 已审核
                    pcrEntrustOrderMapper.updateStatusById(orderId, EntrustOrderStatusEnum.YSH.getCode(),examineUser, examineUserId, examineTime,examineUserId,examineTime);
                }
                //更新result_base为审核已通过
                opJczxPcrResultBaseMapper.updateExamineFlagByResultNo("1",resultNo);
                pcrEntrustOrderSampleMapper.batchUpdateExamineFields(orderId, examineUserId, examineUser, examineTime);
            }

    }


    @Override
    @Transactional
    public void saveBloodResultInfoList(OpJczxBloodResultDto param) {
        List<OpBloodEntrustOrderSample> opBloodEntrustOrderSamples = param.getSampleList();
        String resultNo = param.getResultNo();
        //判断是否已审核
//        List<String> collect = opBloodEntrustOrderSamples.stream().map(OpBloodEntrustOrderSample::getOpBloodEntrustOrderSampleId).collect(Collectors.toList());
//        int count = bloodEntrustOrderSampleMapper.selectExaminCountByIdList(collect);
//        if(count>0){
//            throw new RuntimeException("存在已审核数据,无法修改");
//        }

        for (OpBloodEntrustOrderSample sample : opBloodEntrustOrderSamples) {
            bloodEntrustOrderSampleMapper.updateOpBloodEntrustOrderSample(sample);
            OpJczxBloodResultInfo bloodResultInfo = new OpJczxBloodResultInfo();
            BeanUtils.copyProperties(sample,bloodResultInfo);
            bloodResultInfoMapper.updateResultBySampleNo(bloodResultInfo);
        }

        //生化、早孕的报告直接流转到待发送.疾病流转到待编制

        Set<String> orderIdList = new HashSet<>();
        for (OpBloodEntrustOrderSample sample : opBloodEntrustOrderSamples) {
            String orderId = bloodEntrustOrderSampleMapper.selectOrderIdById(sample.getOpBloodEntrustOrderSampleId());
            orderIdList.add(orderId);

        }
        // 审核人信息
        String examineUserId = String.valueOf(SecurityUtils.getUserId());
        String examineUser = SecurityUtils.getLoginUser().getUser().getNickName();
        Date examineTime = new Date();
        // 批量更新所有样品的审核信息
        List<String> passedIds = opBloodEntrustOrderSamples.stream()
                .map(OpBloodEntrustOrderSample::getOpBloodEntrustOrderSampleId)
                .collect(Collectors.toList());
        bloodEntrustOrderSampleMapper.batchUpdateExamineFields(passedIds, examineUserId, examineUser, examineTime);
        OpJczxBloodResultBase base = opJczxBloodResultBaseMapper.selectByNo(resultNo);
        for (String orderId : orderIdList) {
            // b. 检查该委托单下的所有项目是否都已审核
            int unexaminedCount = bloodEntrustOrderSampleMapper.countUnexaminedItemsByOrderId(orderId);

            if (unexaminedCount == 0) {
                // c. 如果所有项目都已审核，更新委托单 的状态为 "4" (已审核)
                // 状态 4: 已审核
                bloodEntrustOrderMapper.updateStatusById(orderId, EntrustOrderStatusEnum.YSH.getCode(),examineUser, examineUserId, examineTime,examineUserId,examineTime);

                if(base!=null){
                    //如果是生化、早孕流转到待发送
                    if("8".equals(base.getBloodTaskItemType()) || "9".equals(base.getBloodTaskItemType())){
                        // 校验：检查该委托单是否已存在有效报告，防止重复生成
                        // 查询该委托单最新的一条报告记录
                        List<OpJczxBloodReportBase> existingReport = opJczxBloodReportBaseMapper.selectReportBaseByOrderIdLimit1(orderId);

                        if(CollectionUtil.isNotEmpty(existingReport)){
                            // 如果存在报告，且状态不是"6"(作废/已退回)，则视为有效报告，跳过新报告生成
                            // 注意：状态 '6' 对应 JczxFeedReportStatusEnum.ZF (作废)，退回操作会置为 6
                            if (!"6".equals(existingReport.get(0).getStatus())) {
                                // 日志记录（可选）
                                 System.out.println("委托单 " + orderId + " 已存在有效报告(状态" + existingReport.get(0).getStatus() + ")，跳过生成。");
                                continue;
                            }
                        }


                        //制作
                        OpJczxBloodReportBase opJczxBloodReportBase = new OpJczxBloodReportBase();
                        opJczxBloodReportBase.setStatus(JczxFeedReportStatusEnum.DZZ.getCode());
                        opJczxBloodReportBase.setBloodTaskItemType(base.getBloodTaskItemType());
                        opJczxBloodReportBase.setBloodEntrustOrderId(orderId);
                        OpJczxBloodReportBase reportBase =  opJczxBloodReportBaseService.selectOpJczxBloodReportBaseByOpJczxBloodReportBaseId(opJczxBloodReportBase);
                        //不要删这行
                        reportBase.setStatus(JczxFeedReportStatusEnum.ZZZ.getCode());
                        String baseId = opJczxBloodReportBaseService.insertOpJczxBloodReportBase(reportBase);
                        System.out.println(baseId);
                        //提交
                        reportBase.setStatus(JczxFeedReportStatusEnum.DSH.getCode());
                        reportBase.setOpJczxBloodReportBaseId(baseId);
                        opJczxBloodReportBaseService.insertOpJczxBloodReportBase(reportBase);

                        //审核
                        opJczxBloodReportBase.setOpJczxBloodReportBaseId(baseId);
                        opJczxBloodReportBase.setStatus(JczxFeedReportStatusEnum.YSH.getCode());
                        opJczxBloodReportBaseService.verifyOpJczxBloodReportBase(opJczxBloodReportBase);
                        //批准
                        opJczxBloodReportBase.setStatus(JczxFeedReportStatusEnum.YPZ.getCode());
                        opJczxBloodReportBaseService.commitOpJczxBloodReportBase(opJczxBloodReportBase);
                    }
                }
            }
        }

        opJczxBloodResultBaseMapper.updateExamineFlagByResultNo("1",resultNo);



    }

    private String buildResultInfo(List<OpJczxPcrTestModelBo> sampleList, String blTemplateType) { // 1. 确认有入参

        String pcrTaskItemType = sampleList.get(0).getPcrTaskItemType();
        // 2. 确认 getSortedItemNames 被调用时传入了 blTemplateType
        List<String> sortedItemNames = getSortedItemNames(sampleList, pcrTaskItemType, blTemplateType);
        List<OpJczxPcrResultInfo> infoList = new LinkedList<>();
        // 用于查找 item id 的 Map <sampleNo, Map<itemName, itemId>>
        Map<String, Map<String, String>> sampleItemMap = new HashMap<>();
        // 预处理 sampleList，构建查找 Map
        for (OpJczxPcrTestModelBo bo : sampleList) {
            String[] itemNames = bo.getItemName() != null ? bo.getItemName().split("@@") : new String[0];
            String[] itemIds = bo.getEntrustOrderItemIds() != null ? bo.getEntrustOrderItemIds().split("@@") : new String[0]; // 获取 itemIds

            if (itemNames.length == itemIds.length) { // 确保数量匹配
                Map<String, String> itemMap = new HashMap<>();
                for (int i = 0; i < itemNames.length; i++) {
                    itemMap.put(itemNames[i].trim(), itemIds[i].trim()); // 存储 itemName -> itemId 的映射
                }
                sampleItemMap.put(bo.getSampleNo(), itemMap);
            } else {
                // 记录日志或抛出异常，因为 item name 和 id 数量不匹配
                System.err.println("Warning: Item names and IDs count mismatch for sampleNo: " + bo.getSampleNo());
            }
        }


        // 遍历我们“排序后”的 sampleList，并使用索引 i 作为 sequence
        for (int i = 0; i < sampleList.size(); i++) {
            OpJczxPcrTestModelBo bo = sampleList.get(i); // 1. 按索引获取

            // (内部循环，为该样品的 *所有项目* 赋予 *相同的* 样品顺序号)
            for (String itemName : sortedItemNames) {
                OpJczxPcrResultInfo info = new OpJczxPcrResultInfo();
                String itemId = sampleItemMap.getOrDefault(bo.getSampleNo(), Collections.emptyMap())
                        .getOrDefault(itemName, "");
                info.setPcrEntrustOrderItemId(itemId);
                info.setItemName(itemName);
                info.setSampleNo(bo.getSampleNo());
                info.setSampleName(bo.getSampleName());
                info.setInvbillName(bo.getInvbillName());
                info.setPcrEntrustOrderSampleId(bo.getEntrustOrderSampleId());
                info.setDeptName(bo.getEntrustDeptName());
                info.setKzsjh("");
                info.setTqsjh("");

                // (!! 关键修改 !!)
                // 2. 将 (i + 1) 作为 1-based 的顺序存入 sequence 字段
                // (确保 OpJczxPcrResultInfo 实体类中有 sequence 字段 (String类型))
                info.setSequence(String.valueOf(i + 1));

                infoList.add(info);
            }
        }

        // --- 添加 空白样、阳性对照、阴性对照 的逻辑保持不变 ---
        // (注意：这些固定行不需要 sequence，它们在 Excel 模板中是固定的)
        for (String itemName : sortedItemNames) {
            OpJczxPcrResultInfo info = new OpJczxPcrResultInfo();
            info.setItemName(itemName);
            info.setSampleNo("空白样");
            info.setPcrEntrustOrderItemId(itemName); // 这些固定行的 ID 设为空
            infoList.add(info);
        }
        for (String itemName : sortedItemNames) {
            OpJczxPcrResultInfo info = new OpJczxPcrResultInfo();
            info.setItemName(itemName);
            info.setSampleNo("阳性对照");
            info.setPcrEntrustOrderItemId(itemName); // 这些固定行的 ID 设为空
            infoList.add(info);
        }
        for (String itemName : sortedItemNames) {
            OpJczxPcrResultInfo info = new OpJczxPcrResultInfo();
            info.setItemName(itemName);
            info.setSampleNo("阴性对照");
            info.setPcrEntrustOrderItemId(itemName); // 这些固定行的 ID 设为空
            infoList.add(info);
        }

        OpJczxPcrResultBase opJczxPcrResultBase = new OpJczxPcrResultBase();
        String resultNo = "";
        try {
            resultNo = codeGeneratorUtil.generatePcrResultNo();
            opJczxPcrResultBase.setResultNo(resultNo);
        } catch (BusinessException e) {
            throw new BusinessException("生成化验单编号失败: " + e.getMessage());
        }
        opJczxPcrResultBase.setTestDate(new Date());
        opJczxPcrResultBase.setPcrTaskItemType(pcrTaskItemType);
        opJczxPcrResultBase.setBlTemplateType(blTemplateType);
        opJczxPcrResultBase.setStatus(JczxPcrResultStatusEnum.KSHY.getCode());
        opJczxPcrResultBase.setOpJczxPcrResultInfoList(infoList);
        pcrResultBaseService.insertOpJczxPcrResultBase(opJczxPcrResultBase);
        return resultNo;
    }
    private String buildBloodResultInfo(List<OpJczxBloodTestModelBo> sampleList) {
        String bloodTaskItemType = sampleList.get(0).getBloodTaskItemType();
        List<OpJczxBloodResultInfo> infoList = new LinkedList<>();

        // (!! 关键修改 !!)
        // 遍历我们“排序后”的 sampleList，并使用索引 i 作为 sequence
        for (int i = 0; i < sampleList.size(); i++) {
            OpJczxBloodTestModelBo bo = sampleList.get(i);
            OpJczxBloodResultInfo info = new OpJczxBloodResultInfo();
            //ItemId、PcrEntrustOrderItemId没有实际使用，先不赋值
            info.setItemId("");
            info.setBloodEntrustOrderItemId("");
            info.setSampleNo(bo.getSampleNo());
            info.setSampleName(bo.getSampleName());
            info.setGh(bo.getGh());
            info.setBloodEntrustOrderSampleId(bo.getEntrustOrderSampleId());
            info.setDeptName(bo.getEntrustDeptName());
            info.setItemName(DictUtils.getDictLabel("blood_task_item_type",bloodTaskItemType));
            // (!! 关键修改 !!)
            // 将 (i + 1) 作为 1-based 的顺序存入 sequence 字段
            // (确保 OpJczxBloodResultInfo 实体类中有 sequence 字段 (String类型))
            info.setSequence(i + 1);

            infoList.add(info);
        }
        OpJczxBloodResultBase opJczxBloodResultBase = new OpJczxBloodResultBase();
        String resultNo = "";
        try {
            resultNo = codeGeneratorUtil.generateBloodResultNo();
            opJczxBloodResultBase.setResultNo(resultNo);
        } catch (BusinessException e) {
            throw new BusinessException("生成化验单编号失败: " + e.getMessage());
        }
        opJczxBloodResultBase.setTestDate(new Date());
        opJczxBloodResultBase.setBloodTaskItemType(bloodTaskItemType);
        opJczxBloodResultBase.setStatus(JczxPcrResultStatusEnum.KSHY.getCode());
        opJczxBloodResultBase.setOpJczxBloodResultInfoList(infoList);
        opJczxBloodResultBaseService.insertOpJczxBloodResultBase(opJczxBloodResultBase);
        return resultNo;
    }
    /**
     * 填充Excel模板
     */
    private void fillExcelTemplate(HttpServletResponse response, List<OpJczxPcrTestModelBo> sampleList, String pcrTaskItemType, String blTemplateType)
            throws Exception {

        // 动态创建Excel，不使用模板
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             OutputStream outputStream = response.getOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("PCR检测");

            // 获取项目名称列表并按照固定顺序排序
            List<String> itemNames = getSortedItemNames(sampleList, pcrTaskItemType, blTemplateType);

            // 创建表头行
            Row headerRow = sheet.createRow(0);
            int colIndex = 0;

            // 设置基础列标题（调整顺序：试剂盒批号放在最前面）
            setCellValue(headerRow, colIndex++, "提取试剂盒批号");
            setCellValue(headerRow, colIndex++, "扩增试剂盒批号");
            setCellValue(headerRow, colIndex++, "序号");
            setCellValue(headerRow, colIndex++, "所属牧场");
            setCellValue(headerRow, colIndex++, "样品名称");
            setCellValue(headerRow, colIndex++, "样品描述");


            // 添加排序后的项目名称作为列标题
            for (String itemName : itemNames) {
                setCellValue(headerRow, colIndex++, itemName);
            }

            int remarkColIndex = colIndex++;    // 记录 委托备注 列索引
            int sampleNoColIndex = colIndex++;  // 记录 样品编号 列索引

            setCellValue(headerRow, remarkColIndex, "委托备注");
            setCellValue(headerRow, sampleNoColIndex, "样品编号");

            // 记录试剂盒批号列的索引（现在在最前面）
            int tqsjhCol = 0; // 提取试剂盒批号列索引
            int kzsjhCol = 1; // 扩增试剂盒批号列索引

            // 填充样品数据
            int startRow = 1; // 从第2行开始填充数据
            for (int i = 0; i < sampleList.size(); i++) {
                OpJczxPcrTestModelBo sample = sampleList.get(i);
                Row row = sheet.createRow(startRow + i);

                // 填充数据（按照新的列顺序）
                setCellValue(row, tqsjhCol, sample.getTqsjh()); // 提取试剂盒批号
                setCellValue(row, kzsjhCol, sample.getKzsjh()); // 扩增试剂盒批号
                setCellValue(row, 2, i + 1); // 序号
                setCellValue(row, 3, sample.getEntrustDeptName()); // 所属牧场
                setCellValue(row, 4, sample.getInvbillName()); // 样品名称
                setCellValue(row, 5, sample.getSampleName()); // 样品描述
                setCellValue(row, remarkColIndex, sample.getRemark()); // 委托备注 (放到后面)
                setCellValue(row, sampleNoColIndex, sample.getSampleNo()); // 样品编号 (放到最后)
                // 项目列留空（用于后续填写结果）
            }

            // 在样品数据后添加固定行（空白样、阳性对照、阴性对照）
            int fixedRowStart = startRow + sampleList.size();
            //addFixedRows(sheet, fixedRowStart, tqsjhCol, kzsjhCol, sampleNoColIndex);
            addFixedRows(sheet, fixedRowStart, tqsjhCol, kzsjhCol, sampleNoColIndex, 5);
            // --- 优化点：自动调整所有列的宽度，并增加额外边距 ---
            int totalCols = headerRow.getLastCellNum();
            for (int i = 0; i < totalCols; i++) {
                sheet.autoSizeColumn(i); // 1. 先自动调整
                int currentWidth = sheet.getColumnWidth(i); // 2. 获取自动调整后的宽度
                // 3. 在原宽度的基础上，增加 5 个字符的额外宽度 (1/256)
                sheet.setColumnWidth(i, currentWidth + (5 * 256));
            }
            // ---------------------------------

            workbook.write(outputStream);
        }
    }
    /**
     * 根据固定排序规则对项目名称进行排序
     * 修改：增加4联、7联的中英文排序逻辑
     */
    private List<String> getSortedItemNames(List<OpJczxPcrTestModelBo> sampleList, String pcrTaskItemType, String blTemplateType) {
        // 1. 现有的 8联 (Type="7") 逻辑保持不变
        if ("7".equals(pcrTaskItemType)) {
            if ("1".equals(blTemplateType)) { // 中文
                return Arrays.asList("β-内酰胺酶抗性基因", "无乳链球菌", "绿脓杆菌", "克雷伯氏菌属", "停乳链球菌", "金黄色葡萄球菌", "大肠杆菌", "牛支原体");
            } else { // 英文 (或默认 '2')
                return Arrays.asList("绿脓杆菌", "无乳链球菌", "β-内酰胺酶抗性基因", "停乳链球菌", "克雷伯氏菌属", "牛支原体", "大肠杆菌", "金黄色葡萄球菌");
            }
        }

        if (sampleList.isEmpty() || StringUtils.isEmpty(sampleList.get(0).getItemName())) {
            return new ArrayList<>();
        }

        // 2. 获取当前样品中的所有项目名称（去重）
        String[] originalItems = sampleList.get(0).getItemName().split("@@");
        Set<String> distinctSet = new LinkedHashSet<>();
        for (String item : originalItems) {
            if (StringUtils.isNotEmpty(item)) {
                distinctSet.add(item.trim());
            }
        }

        // 3. 定义4联和7联的项目集合（用于匹配）
        Set<String> fourChainItems = new HashSet<>(Arrays.asList("牛冠状病毒", "牛轮状病毒", "隐孢子虫", "肠毒素型细菌-大肠杆菌","产气荚膜梭菌"));
        Set<String> sevenChainItems = new HashSet<>(Arrays.asList("牛冠状病毒", "牛病毒性腹泻病毒", "牛呼吸道合胞体病毒", "牛副流感病毒3型", "牛支原体", "溶血性曼氏杆菌", "多杀巴斯德杆菌"));

        // 4. 判断逻辑：如果是 4联
        if (distinctSet.size() == 4 && distinctSet.containsAll(fourChainItems)) {
            if ("1".equals(blTemplateType)) {
                // 4联中文顺序
                return Arrays.asList("牛轮状病毒", "牛冠状病毒", "肠毒素型细菌-大肠杆菌", "隐孢子虫","产气荚膜梭菌");
            } else {
                // 4联英文顺序 (默认)
                return Arrays.asList("牛冠状病毒", "牛轮状病毒", "隐孢子虫", "肠毒素型细菌-大肠杆菌","产气荚膜梭菌");
            }
        }

        // 5. 判断逻辑：如果是 7联
        if (distinctSet.size() == 7 && distinctSet.containsAll(sevenChainItems)) {
            if ("1".equals(blTemplateType)) {
                // 7联中文顺序
                return Arrays.asList("牛病毒性腹泻病毒", "牛冠状病毒", "牛副流感病毒3型", "牛呼吸道合胞体病毒", "多杀巴斯德杆菌", "溶血性曼氏杆菌", "牛支原体");
            } else {
                // 7联英文顺序 (默认)
                return Arrays.asList("牛冠状病毒", "牛病毒性腹泻病毒", "牛呼吸道合胞体病毒", "牛副流感病毒3型", "牛支原体", "溶血性曼氏杆菌", "多杀巴斯德杆菌");
            }
        }

        // 6. 兜底逻辑：如果都不是上述情况，使用旧的 sortRules 进行匹配，或者返回原始顺序
        List<String> originalItemList = new ArrayList<>(distinctSet);

        // 定义其他可能的固定排序规则 (保留旧代码中的兜底规则，防止其他少见的组合失效)
        List<List<String>> sortRules = Arrays.asList(
                Arrays.asList("金黄色葡萄球菌"),
                // 旧的4联规则作为兜底（虽然上面已经处理了，但保留无害）
                Arrays.asList("牛冠状病毒", "牛轮状病毒", "隐孢子虫", "肠毒素型细菌-大肠杆菌","产气荚膜梭菌"),
                // 旧的7联规则作为兜底
                Arrays.asList("牛冠状病毒", "牛病毒性腹泻病毒", "牛呼吸道合胞体病毒", "牛副流感病毒3型", "牛支原体", "溶血性曼氏杆菌", "多杀巴斯德杆菌"),
                Arrays.asList("绿脓杆菌", "无乳链球菌", "β-内酰胺酶抗性基因", "停乳链球菌", "克雷伯氏菌属", "牛支原体", "大肠杆菌", "金黄色葡萄球菌"),
                Arrays.asList("牛支原体"),
                Arrays.asList("无乳链球菌")
        );

        // 查找匹配的排序规则
        for (List<String> rule : sortRules) {
            if (originalItemList.containsAll(rule) && rule.containsAll(originalItemList)) {
                return new ArrayList<>(rule); // 完全匹配，返回该排序规则
            }
        }

        // 如果没有完全匹配的规则，按原始顺序返回
        return originalItemList;
    }
    /**
     * 根据固定排序规则对项目名称进行排序
     */
    private List<String> getSortedItemNames2(List<OpJczxPcrTestModelBo> sampleList, String pcrTaskItemType, String blTemplateType) {
        if ("7".equals(pcrTaskItemType)) {
            if ("1".equals(blTemplateType)) { // 中文
                return Arrays.asList("β-内酰胺酶抗性基因", "无乳链球菌", "绿脓杆菌", "克雷伯氏菌属", "停乳链球菌", "金黄色葡萄球菌", "大肠杆菌", "牛支原体");
            } else { // 英文 (或默认 '2')
                return Arrays.asList("绿脓杆菌", "无乳链球菌", "β-内酰胺酶抗性基因", "停乳链球菌", "克雷伯氏菌属", "牛支原体", "大肠杆菌", "金黄色葡萄球菌");
            }
        }
        if (sampleList.isEmpty() || StringUtils.isEmpty(sampleList.get(0).getItemName())) {
            return new ArrayList<>();
        }

        // 获取原始项目名称
        String[] originalItems = sampleList.get(0).getItemName().split("@@");

        // 使用 LinkedHashSet 进行去重，同时保留原始顺序
        Set<String> distinctSet = new LinkedHashSet<>();
        for (String item : originalItems) {
            if (StringUtils.isNotEmpty(item)) {
                distinctSet.add(item.trim());
            }
        }
        List<String> originalItemList = new ArrayList<>(distinctSet);
        // 定义六种固定排序规则
        List<List<String>> sortRules = Arrays.asList(
                Arrays.asList("金黄色葡萄球菌"),
                Arrays.asList("牛冠状病毒", "牛轮状病毒", "隐孢子虫", "肠毒素型细菌-大肠杆菌","产气荚膜梭菌"),
                Arrays.asList("牛冠状病毒", "牛病毒性腹泻病毒", "牛呼吸道合胞体病毒", "牛副流感病毒3型", "牛支原体", "溶血性曼氏杆菌", "多杀巴斯德杆菌"),
                Arrays.asList("绿脓杆菌", "无乳链球菌", "β-内酰胺酶抗性基因", "停乳链球菌", "克雷伯氏菌属", "牛支原体", "大肠杆菌", "金黄色葡萄球菌"),
                Arrays.asList("牛支原体"),
                Arrays.asList("无乳链球菌")
        );

        // 查找匹配的排序规则
        for (List<String> rule : sortRules) {
            if (originalItemList.containsAll(rule) && rule.containsAll(originalItemList)) {
                return new ArrayList<>(rule); // 完全匹配，返回该排序规则
            }
        }

        // 如果没有完全匹配的规则，按原始顺序返回
        return originalItemList;
    }
    /**
     * 添加固定行（空白样、阳性对照、阴性对照）- 更新方法签名
     * 【修改点】：增加了 int sampleDescColIndex 参数
     */
    private void addFixedRows(XSSFSheet sheet, int startRow, int tqsjhCol, int kzsjhCol, int sampleNoColIndex, int sampleDescColIndex) {
        // 空白样
        Row blankRow = sheet.createRow(startRow);
        setCellValue(blankRow, sampleNoColIndex, "空白样"); // 样品编号列
        setCellValue(blankRow, sampleDescColIndex, "空白样"); // 【新增】：样品描述列
        setCellValue(blankRow, tqsjhCol, ""); // 提取试剂盒批号
        setCellValue(blankRow, kzsjhCol, ""); // 扩增试剂盒批号

        // 阳性对照
        Row positiveRow = sheet.createRow(startRow + 1);
        setCellValue(positiveRow, sampleNoColIndex, "阳性对照"); // 样品编号列
        setCellValue(positiveRow, sampleDescColIndex, "阳性对照"); // 【新增】：样品描述列
        setCellValue(positiveRow, tqsjhCol, ""); // 提取试剂盒批号
        setCellValue(positiveRow, kzsjhCol, ""); // 扩增试剂盒批号

        // 阴性对照
        Row negativeRow = sheet.createRow(startRow + 2);
        setCellValue(negativeRow, sampleNoColIndex, "阴性对照"); // 样品编号列
        setCellValue(negativeRow, sampleDescColIndex, "阴性对照"); // 【新增】：样品描述列
        setCellValue(negativeRow, tqsjhCol, ""); // 提取试剂盒批号
        setCellValue(negativeRow, kzsjhCol, ""); // 扩增试剂盒批号
    }
    /**
     * 添加固定行（空白样、阳性对照、阴性对照）- 更新方法签名
     */
//    private void addFixedRows(XSSFSheet sheet, int startRow, int tqsjhCol, int kzsjhCol, int sampleNoColIndex) {
//        // 空白样
//        Row blankRow = sheet.createRow(startRow);
//        // --- [修改] --- 将 7 改为 sampleNoColIndex
//        setCellValue(blankRow, sampleNoColIndex, "空白样");
//        setCellValue(blankRow, tqsjhCol, ""); // 提取试剂盒批号
//        setCellValue(blankRow, kzsjhCol, ""); // 扩增试剂盒批号
//
//        // 阳性对照
//        Row positiveRow = sheet.createRow(startRow + 1);
//        // --- [修改] --- 将 7 改为 sampleNoColIndex
//        setCellValue(positiveRow, sampleNoColIndex, "阳性对照");
//        setCellValue(positiveRow, tqsjhCol, ""); // 提取试剂盒批号
//        setCellValue(positiveRow, kzsjhCol, ""); // 扩增试剂盒批号
//
//        // 阴性对照
//        Row negativeRow = sheet.createRow(startRow + 2);
//        // --- [修改] --- 将 7 改为 sampleNoColIndex
//        setCellValue(negativeRow, sampleNoColIndex, "阴性对照");
//        setCellValue(negativeRow, tqsjhCol, ""); // 提取试剂盒批号
//        setCellValue(negativeRow, kzsjhCol, ""); // 扩增试剂盒批号
//    }

    /**
     * 填充Excel模板
     */
    private void fillExcelBloodTemplate(HttpServletResponse response, List<OpJczxBloodTestModelBo> sampleList,String bloodTaskItemType)
            throws Exception {

        // 动态创建Excel，不使用模板
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             OutputStream outputStream = response.getOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("血样检测");

            // 创建表头行
            Row headerRow = sheet.createRow(0);
            int colIndex = 0;

            // 设置基础列标题（调整顺序：试剂盒批号放在最前面）
            setCellValue(headerRow, colIndex++, "委托单位");
            setCellValue(headerRow, colIndex++, "管号");
            setCellValue(headerRow, colIndex++, "牛号");
            //setCellValue(headerRow, colIndex++, "样品编号");
            //口蹄疫
            if("0".equals(bloodTaskItemType)){
                setCellValue(headerRow, colIndex++, "A型口蹄疫样品效价");
                setCellValue(headerRow, colIndex++, "A型口蹄疫结果判定");
                setCellValue(headerRow, colIndex++, "O型口蹄疫样品效价");
                setCellValue(headerRow, colIndex++, "O型口蹄疫结果判定");
            }else if("1".equals(bloodTaskItemType)){//布病（虎红）

            }else if("2".equals(bloodTaskItemType)){//结核（牛分枝杆菌PPD）
                setCellValue(headerRow, colIndex++, "结果判定");
            }else if("3".equals(bloodTaskItemType)){//副结核抗体
                setCellValue(headerRow, colIndex++, "S/P值");
                setCellValue(headerRow, colIndex++, "判定结果");
            }else if("4".equals(bloodTaskItemType)){//BVDV抗原
                setCellValue(headerRow, colIndex++, "母牛号");
                setCellValue(headerRow, colIndex++, "S-N值");
                setCellValue(headerRow, colIndex++, "判定结果");
                setCellValue(headerRow, colIndex++, "备注");
            }else if("5".equals(bloodTaskItemType)){//布病抗体

            }else if("6".equals(bloodTaskItemType)){//结核抗体
                setCellValue(headerRow, colIndex++, "S/P值");
                setCellValue(headerRow, colIndex++, "结果判定");
            }else if("7".equals(bloodTaskItemType)){//BVDV抗体

            }else if("8".equals(bloodTaskItemType)){//生化
                //查询这些样品所选的生化项目
                List<String> sampleIdList = sampleList.stream().map(OpJczxBloodTestModelBo::getEntrustOrderSampleId).collect(Collectors.toList());
                List<String> biochemistryItemTypeList = bloodEntrustOrderMapper.selectBiochemistryItemTypeBySampleIdList(sampleIdList);

                Set<String> biochemistryItemTypeSet = new TreeSet<>();
                for (String s : biochemistryItemTypeList) {
                    // 根据逗号分隔
                    if (s != null && !s.trim().isEmpty()) {
                        String[] items = s.split(",");
                        for (String item : items) {
                            String trimmedItem = item.trim();
                            if (!trimmedItem.isEmpty()) {
                                biochemistryItemTypeSet.add(trimmedItem);
                            }
                        }
                    }
                }
                for (String item : biochemistryItemTypeSet) {
                    String name = DictUtils.getDictLabel("biochemistry_item_type", item);
                    setCellValue(headerRow, colIndex++, name);
                }
            }else if("9".equals(bloodTaskItemType)){//早孕
                setCellValue(headerRow, colIndex++, "导入牛号");
                setCellValue(headerRow, colIndex++, "BioPRYN OD");
                setCellValue(headerRow, colIndex++, "结果");
                setCellValue(headerRow, colIndex++, "备注");
            }

            // 1. 如果是口蹄疫(0)，添加 A型试剂批号 和 O型试剂批号
            // 2. 如果不是口蹄疫(0) 且 不是早孕(9)，则添加通用的 试剂批号
            if ("0".equals(bloodTaskItemType)) {
                setCellValue(headerRow, colIndex++, "A型试剂批号");
                setCellValue(headerRow, colIndex++, "O型试剂批号");
            } else if (!"9".equals(bloodTaskItemType)) {
                setCellValue(headerRow, colIndex++, "试剂批号");
            }
            // 在所有列逻辑走完后，将“样品编号”放在最后
            setCellValue(headerRow, colIndex++, "样品编号");

            // 记录“样品编号”所在的列索引（因为 colIndex++ 此时已经指向下一列了，所以要减1）
            int sampleNoColIndex = colIndex - 1;

            // 填充样品数据
            int startRow = 1; // 从第2行开始填充数据
            for (int i = 0; i < sampleList.size(); i++) {
                OpJczxBloodTestModelBo sample = sampleList.get(i);
                Row row = sheet.createRow(startRow + i);

                // 填充数据
                setCellValue(row, 0, sample.getEntrustDeptName()); // 委托单位
                setCellValue(row, 1, sample.getGh()); // 序号
                setCellValue(row, 2, sample.getSampleName()); // 牛号
                // 针对 BVDV抗原 (type=4) 填充特定列数据
                if ("4".equals(bloodTaskItemType)) {
                    setCellValue(row, 3, sample.getMnh());
                    setCellValue(row, 6, sample.getRemark());
                }
                setCellValue(row, sampleNoColIndex, sample.getSampleNo()); // 样品编号放在最后

            }

            // --- ：自动调整所有列的宽度，并增加额外边距 ---
            int totalCols = headerRow.getLastCellNum();
            for (int i = 0; i < totalCols; i++) {
                sheet.autoSizeColumn(i); // 1. 先自动调整
                int currentWidth = sheet.getColumnWidth(i); // 2. 获取自动调整后的宽度
                // 修改：早孕(type=9)的结果列(索引6)宽度改为双倍
                if ("9".equals(bloodTaskItemType) && i == 6) {
                    sheet.setColumnWidth(i, (currentWidth + (5 * 256)) * 2);
                } else {
                    // 3. 在原宽度的基础上，增加 5 个字符的额外宽度 (1/256)
                    sheet.setColumnWidth(i, currentWidth + (5 * 256));
                }
            }
            // ---------------------------------

            workbook.write(outputStream);
        }
    }

    /**
     * 设置单元格值
     */
    private void setCellValue(Row row, int cellIndex, Object value) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            cell = row.createCell(cellIndex);
        }
        if (value != null) {
            cell.setCellValue(value.toString());
        } else {
            cell.setCellValue("");
        }
    }

    @Override
    public void generateNirTestExcel(HttpServletResponse response, List<String> entrustOrderIds) {
        if (entrustOrderIds == null || entrustOrderIds.isEmpty()) {
            throw new RuntimeException("未选择委托单");
        }

        try {
            // 1. 查询所有相关样品
            List<OpFeedEntrustOrderSample> samples = feedEntrustOrderSampleMapper.selectSamplesByOrderIds(entrustOrderIds);

            // 2. 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = "近红外检测表格_" + System.currentTimeMillis() + ".xlsx";
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            // 3. 动态创建Excel
            try (XSSFWorkbook workbook = new XSSFWorkbook();
                 OutputStream outputStream = response.getOutputStream()) {

                XSSFSheet sheet = workbook.createSheet("样品");

                // 4. 创建表头行
                Row headerRow = sheet.createRow(0);
                setCellValue(headerRow, 0, "样品编号");
                setCellValue(headerRow, 1, "样品名称"); // 或 "样品描述"

                // 5. 填充样品数据
                int startRow = 1;
                for (int i = 0; i < samples.size(); i++) {
                    OpFeedEntrustOrderSample sample = samples.get(i);
                    Row row = sheet.createRow(startRow + i);
                    setCellValue(row, 0, sample.getSampleNo());
                    setCellValue(row, 1, sample.getName()); // 对应 "样品名称"
                }

                // 6. (可选) 设置列宽 -> 优化为自动列宽
                // sheet.setColumnWidth(0, 30 * 256); // 样品编号
                // sheet.setColumnWidth(1, 30 * 256); // 样品名称
                // --- 优化点：自动调整所有列的宽度，并增加额外边距 ---
                int totalCols = headerRow.getLastCellNum();
                for (int i = 0; i < totalCols; i++) {
                    sheet.autoSizeColumn(i); // 1. 先自动调整
                    int currentWidth = sheet.getColumnWidth(i); // 2. 获取自动调整后的宽度
                    // 3. 在原宽度的基础上，增加 5 个字符的额外宽度 (1/256)
                    sheet.setColumnWidth(i, currentWidth + (5 * 256));
                }
                // ---------------------------------

                workbook.write(outputStream);
            }

        } catch (Exception e) {
            throw new RuntimeException("生成近红外Excel失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void uploadNirReportForOrder(String entrustOrderId, String fileId) {
        OpFeedEntrustOrderSample query = new OpFeedEntrustOrderSample();
        query.setFeedEntrustOrderId(entrustOrderId);
        query.setIsDelete("0"); // 确保只查询未删除的
        List<OpFeedEntrustOrderSample> samples = feedEntrustOrderSampleMapper.selectOpFeedEntrustOrderSampleList(query);

        if (samples.isEmpty()) {
            throw new RuntimeException("未找到委托单 " + entrustOrderId + " 对应的样品");
        }

        int affectedRows = 0;
        for (OpFeedEntrustOrderSample sample : samples) {
            OpFeedEntrustOrderSample updateSample = new OpFeedEntrustOrderSample();
            updateSample.setOpFeedEntrustOrderSampleId(sample.getOpFeedEntrustOrderSampleId());
            updateSample.setFileId(fileId);
            updateSample.fillUpdateInfo(); // 设置 updateBy 和 updateTime
            affectedRows += feedEntrustOrderSampleMapper.updateOpFeedEntrustOrderSample(updateSample);
        }

        if (affectedRows > 0) {
            // 标记文件为正式文件
            sysUploadFileService.markFileAsPermanent(fileId);
        }
    }

    @Override
    public List<OpJczxTestTaskVo> selectJhwTaskList(OpJczxTestTaskDto opJczxTestTaskDto) {

        // 处理结束日期
        if (opJczxTestTaskDto.getEndReceiveTime() != null) {
            setEndOfDay(opJczxTestTaskDto.getEndReceiveTime());
        }
        opJczxTestTaskDto.setUserId(SecurityUtils.getUserId().toString());
        return opJczxTestTaskMapper.selectJhwTaskList(opJczxTestTaskDto);
    }

    /**
     * 根据当前用户统计饲料检测【待化验 - 化学法】数量
     *
     * @return Feed 待检测项目数
     */
    @Override
    public int countFeedWaitTestChemistryByCurrentUser() {
        return opJczxTestTaskMapper.countFeedWaitTestChemistryByUserId(
                SecurityUtils.getUserId().toString() // 当前用户
        );
    }

    /**
     * 根据当前用户统计饲料检测【待化验 - 出水分】数量
     *
     * @return 待化验初水分数量
     */
    @Override
    public int countFeedWaitTestCsfByCurrentUser() {
        // 1. 获取配置项 csfItemId 的值
        String csfItemId = configService
                .selectConfigByKey("csfItemId")
                .trim();
        // 2. 查询
        return opJczxTestTaskMapper.countFeedWaitTestCsfByUserId(
                SecurityUtils.getUserId().toString(), // 当前用户
                csfItemId // 出水分项目ID
        );
    }

    /**
     * 统计pcr待检测数量
     * @return 数量
     */
    @Override
    public int countPcrWaitTest() {
        return opJczxTestTaskMapper.countPcrWaitTest();
    }

    /**
     * 统计病害待检测数量
     * @return 数量
     */
    @Override
    public int countDiseaseWaitTest() {
        return opJczxTestTaskMapper.countDiseaseWaitTest();
    }

    /**
     * 统计生化待检测数量
     * @return 数量
     */
    @Override
    public int countBiochemistryWaitTest() {
        return opJczxTestTaskMapper.countBiochemistryWaitTest();
    }

    /**
     * 早孕待检测数量
     * @return  数量
     */
    @Override
    public int countEarlyPregnancyWaitTest() {
        return opJczxTestTaskMapper.countEarlyPregnancyWaitTest();
    }

    @Override
    public int countPcrWaitAccept() {
        return opJczxTestTaskMapper.countPcrWaitAccept();
    }

    @Override
    public int countDiseaseWaitAccept() {
        return opJczxTestTaskMapper.countDiseaseWaitAccept();
    }

    @Override
    public int countBiochemistryWaitAccept() {
        return opJczxTestTaskMapper.countBiochemistryWaitAccept();
    }

    @Override
    public int countEarlyPregnancyWaitAccept() {
        return opJczxTestTaskMapper.countEarlyPregnancyWaitAccept();
    }
}