package com.gmlimsqi.business.labtest.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.basicdata.domain.OpJczxPcrReportInfo;
import com.gmlimsqi.business.basicdata.mapper.BsContactMapper;
import com.gmlimsqi.business.labtest.domain.OpJczxPcrReportBase;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrder;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrderItem;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrderSample;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.dto.PcrReportExcelResult;
import com.gmlimsqi.business.labtest.dto.PcrReportSendDTO;
import com.gmlimsqi.business.labtest.mapper.*;
import com.gmlimsqi.business.labtest.service.IOpJczxPcrReportBaseService;
import com.gmlimsqi.business.labtest.vo.OpPcrReportItemListVo;
import com.gmlimsqi.business.labtest.vo.OpPcrReportListVo;
import com.gmlimsqi.business.labtest.vo.ReportEmailVo;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.core.domain.entity.SysDept;
import com.gmlimsqi.common.enums.FeedReportEnum;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.DictUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.email.EmailUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import com.gmlimsqi.system.mapper.SysUserMapper;
import com.gmlimsqi.system.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * pcr报告主Service业务层处理
 *
 * @author hhy
 * @date 2025-10-20
 */
@Slf4j
@Service
public class OpJczxPcrReportBaseServiceImpl implements IOpJczxPcrReportBaseService {
    @Autowired
    private OpJczxPcrReportBaseMapper opJczxPcrReportBaseMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private OpPcrEntrustOrderMapper opPcrEntrustOrderMapper;
    @Autowired
    private OpPcrEntrustOrderItemMapper opPcrEntrustOrderItemMapper;
    @Autowired
    private OpPcrEntrustOrderSampleMapper opPcrEntrustOrderSampleMapper;
    @Autowired
    private OpJczxPcrReportInfoMapper opJczxPcrReportInfoMapper;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private BsContactMapper bsContactMapper;
    @Autowired
    private ISysConfigService configService;
    /**
     * 查询pcr报告主
     *
     * @return pcr报告主
     */
    @Override
    public  List<OpPcrReportItemListVo>  selectOpJczxPcrReportBase(OpJczxPcrReportBase opJczxPcrReportBase) {
//        OpJczxPcrReportBase opJczxPcrReportBase = opJczxPcrReportBaseMapper.selectOpJczxPcrReportBaseByOpJczxPcrReportBaseId(opJczxPcrReportBaseId);
//        String status = opJczxPcrReportBase.getStatus();
        String status ="0";
//        if (StringUtils.isEmpty(status)) {
//            throw new RuntimeException("服务器网络异常");
//        }
        List<OpPcrReportItemListVo> opPcrReportItemLists = new ArrayList<>();

        if (status.equals("0")){
            //保存前
            //查询检测项目
            String pcrEntrustOrderId = opJczxPcrReportBase.getPcrEntrustOrderId();
            String orderNo = opJczxPcrReportBase.getOrderNo();
            if (ObjectUtils.isEmpty(orderNo)) {
                log.error("orderNo is empty");
                throw new RuntimeException("服务器网络异常");
            }
            if (ObjectUtils.isEmpty(pcrEntrustOrderId)) {
                log.error("pcrEntrustOrderId is empty");
                throw new RuntimeException("服务器网络异常");
            }

            List<String> itemType = opPcrEntrustOrderMapper.selectItemTypeByEntrustOrderId(pcrEntrustOrderId);
            //组装数据
            for (String item : itemType) {
                OpPcrReportItemListVo opPcrReportItemListVo = new OpPcrReportItemListVo();
                List<OpPcrEntrustOrderItem> baseByEntrustOrderNo = opPcrEntrustOrderItemMapper.getBaseByEntrustOrderNo(item,orderNo );
                opPcrReportItemListVo.setOpPcrEntrustOrderItemList(baseByEntrustOrderNo);
                opPcrReportItemListVo.setItemType(item);
                opPcrReportItemLists.add(opPcrReportItemListVo);
            }
        }else {
            //保存后
            String opJczxPcrReportBaseId = opJczxPcrReportBase.getOpJczxPcrReportBaseId();
            if (StringUtils.isEmpty(opJczxPcrReportBaseId)) {
                log.error("opJczxPcrReportBaseId is empty");
                throw  new RuntimeException("服务器网络异常");
            }

            List<String> itemType = opJczxPcrReportInfoMapper.selectItemTypeByBaseId(opJczxPcrReportBaseId);
            //组装数据
            for (String item : itemType) {
                OpPcrReportItemListVo opPcrReportItemListVo = new OpPcrReportItemListVo();
                opPcrReportItemListVo.setItemType(item);
            }
//            opJczxPcrReportBaseMapper.selectOpJczxPcrReportBaseByOpJczxPcrReportBaseId()
        }
        return opPcrReportItemLists;
    }

    /**
     * 查询pcr报告主列表
     *
     * @return pcr报告主
     */
    @Override
    public List<OpPcrReportListVo> selectOpJczxPcrReportBaseList(OpJczxTestTaskDto opJczxTestTaskDto) {
        if (opJczxTestTaskDto.getEndtestTime() != null) {
            setEndOfDay(opJczxTestTaskDto.getEndtestTime());
        }
        if (opJczxTestTaskDto.getEndExamineTime() != null) {
            setEndOfDay(opJczxTestTaskDto.getEndExamineTime());
        }
        return opPcrEntrustOrderMapper.selectPcrAuditedList(opJczxTestTaskDto);
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
    /**
     * 新增pcr报告主
     *
     * @param opJczxPcrReportBase pcr报告主
     * @return 结果
     */
    @Override
    @Transactional
    public int insertOpJczxPcrReportBase(OpJczxPcrReportBase opJczxPcrReportBase) {

        String userId = String.valueOf(SecurityUtils.getUserId());
        String status = opJczxPcrReportBase.getStatus();
        if (ObjectUtils.isEmpty(status)) {
            throw new RuntimeException("服务器网络异常");
        }
        List<OpPcrReportItemListVo> pcrReportInfoList = opJczxPcrReportBase.getPcrReportItemListVoList();

        if (ObjectUtils.isEmpty(pcrReportInfoList)) {
            throw new RuntimeException("服务器网络异常");
        }

        String baseId = opJczxPcrReportBase.getOpJczxPcrReportBaseId();
        if (ObjectUtils.isEmpty(baseId)) {
            //首次保存
            opJczxPcrReportBase.fillCreateInfo();
            baseId = IdUtils.simpleUUID();
            opJczxPcrReportBase.setReportNo(codeGeneratorUtil.generatePCRReportNo());
            opJczxPcrReportBaseMapper.insertOpJczxPcrReportBase(opJczxPcrReportBase);
        } else {
            // 自动填充创建/更新信息
            opJczxPcrReportBaseMapper.updateOpJczxPcrReportBase(opJczxPcrReportBase);
        }

        //子表先删除，再插入
        opJczxPcrReportInfoMapper.updateDeleteFlagByBaseId(opJczxPcrReportBase.getOpJczxPcrReportBaseId(), userId);
        for (OpPcrReportItemListVo opPcrReportItemListVo : pcrReportInfoList) {
            String itemType = opPcrReportItemListVo.getItemType();
            List<OpPcrEntrustOrderItem> opPcrEntrustOrderItemList = opPcrReportItemListVo.getOpPcrEntrustOrderItemList();
            for (OpPcrEntrustOrderItem opPcrEntrustOrderItem : opPcrEntrustOrderItemList) {
                OpJczxPcrReportInfo opJczxPcrReportInfo = new OpJczxPcrReportInfo();
                BeanUtils.copyProperties(opPcrEntrustOrderItem, opJczxPcrReportInfo);
                opJczxPcrReportInfo.setPcrTaskItemType(itemType);
                opJczxPcrReportInfo.setBaseId(baseId);
                opJczxPcrReportInfo.setOpJczxPcrReportInfoId(IdUtils.simpleUUID());
                opJczxPcrReportInfoMapper.insertOpJczxPcrReportInfo(opJczxPcrReportInfo);
            }
        }
        return 1;
    }

    /**
     * 修改pcr报告主
     *
     * @param opJczxPcrReportBase pcr报告主
     * @return 结果
     */
    @Override
    public int updateOpJczxPcrReportBase(OpJczxPcrReportBase opJczxPcrReportBase) {
        // 自动填充更新信息
        opJczxPcrReportBase.fillUpdateInfo();
        return opJczxPcrReportBaseMapper.updateOpJczxPcrReportBase(opJczxPcrReportBase);
    }

    /**
     * 批量删除pcr报告主
     *
     * @param opJczxPcrReportBaseIds 需要删除的pcr报告主主键
     * @return 结果
     */
    @Override
    public int deleteOpJczxPcrReportBaseByOpJczxPcrReportBaseIds(String[] opJczxPcrReportBaseIds) {
        return opJczxPcrReportBaseMapper.deleteOpJczxPcrReportBaseByOpJczxPcrReportBaseIds(opJczxPcrReportBaseIds);
    }

    /**
     * 删除pcr报告主信息
     *
     * @param opJczxPcrReportBaseId pcr报告主主键
     * @return 结果
     */
    @Override
    public int deleteOpJczxPcrReportBaseByOpJczxPcrReportBaseId(String opJczxPcrReportBaseId) {
        return opJczxPcrReportBaseMapper.deleteOpJczxPcrReportBaseByOpJczxPcrReportBaseId(opJczxPcrReportBaseId);
    }

    /**
     * 审核
     * @param opJczxPcrReportBase
     * @return
     */
    @Override
    public int verifyOpJczxPcrReportBase(OpJczxPcrReportBase opJczxPcrReportBase) {
        String status = opJczxPcrReportBase.getStatus();
        if (ObjectUtils.isEmpty(status)) {
            throw new RuntimeException("服务器网络异常");
        }
        if (status.equals(FeedReportEnum.Veriy.getCode())) {
            Long userIdL = SecurityUtils.getUserId();
            String userId = String.valueOf(userIdL);
            opJczxPcrReportBase.setCheckUser(userId);
            opJczxPcrReportBase.setCheckTime(DateUtils.getNowDate());
        }
        return opJczxPcrReportBaseMapper.updateOpJczxPcrReportBase(opJczxPcrReportBase);
    }

    /**
     * 校准
     * @param opJczxPcrReportBase
     * @return
     */
    @Override
    public int commitOpJczxPcrReportBase(OpJczxPcrReportBase opJczxPcrReportBase) {
        String status = opJczxPcrReportBase.getStatus();
        if (ObjectUtils.isEmpty(status)) {
            throw new RuntimeException("服务器网络异常");
        }
        if (status.equals(FeedReportEnum.Commit.getCode())) {
            Long userIdL = SecurityUtils.getUserId();
            String username = SecurityUtils.getUsername();
            String userId = String.valueOf(userIdL);
            opJczxPcrReportBase.setApproveUserId(userId);
            opJczxPcrReportBase.setApproveUser(username);
            opJczxPcrReportBase.setApproveTime(DateUtils.getNowDate());
        }
        return opJczxPcrReportBaseMapper.updateOpJczxPcrReportBase(opJczxPcrReportBase);


    }


    @Override
    public void generatePcrReportExcel(HttpServletResponse response, OpPcrEntrustOrder dto) {
        log.info("开始生成 PCR 报告 Excel (HttpServletResponse 方式)，委托单 ID: {}", dto.getOpPcrEntrustOrderId());
        OpPcrEntrustOrder order = null;
        try {
            order = opPcrEntrustOrderMapper.selectOrderDetailById(dto.getOpPcrEntrustOrderId());

            if (order == null) {
                throw new RuntimeException("未找到委托单数据");
            }
            if (CollectionUtil.isEmpty(order.getSampleList())) {
                log.warn("委托单 {} 下没有样品数据", order.getEntrustOrderNo());
            }

            Map<String, List<OpPcrEntrustOrderSample>> groupedSamples = order.getSampleList().stream()
                    .filter(s -> StringUtils.isNotEmpty(s.getPcrTaskItemType()))
                    .filter(s -> s.getPcrTaskItemType().equals(dto.getPcrTaskItemType()))
                    .collect(Collectors.groupingBy(OpPcrEntrustOrderSample::getPcrTaskItemType));

            String fileName = "PCR检测报告_" + order.getEntrustOrderNo() + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            try (XSSFWorkbook workbook = new XSSFWorkbook();
                 OutputStream outputStream = response.getOutputStream()) {

                XSSFCellStyle onePlusStyle = createCellStyleWithBackground(workbook, IndexedColors.YELLOW);
                XSSFCellStyle twoPlusStyle = createCellStyleWithBackground(workbook, IndexedColors.DARK_RED, IndexedColors.WHITE);
                XSSFCellStyle threePlusStyle = createCellStyleWithBackground(workbook, IndexedColors.RED);

                for (Map.Entry<String, List<OpPcrEntrustOrderSample>> entry : groupedSamples.entrySet()) {
                    String pcrTaskItemType = entry.getKey();
                    List<OpPcrEntrustOrderSample> samplesInGroup = entry.getValue();

                    String sheetName = DictUtils.getDictLabel("pcr_task_item_type", pcrTaskItemType);
                    if (StringUtils.isEmpty(sheetName)) {
                        sheetName = "Type_" + pcrTaskItemType;
                    }
                    sheetName = sheetName.replaceAll("[\\\\/*?\\[\\]:]", "_").substring(0, Math.min(sheetName.length(), 30));
                    XSSFSheet sheet = workbook.createSheet(sheetName);

                    List<String> sortedItemNames = getSortedItemNamesByType(samplesInGroup, pcrTaskItemType);

                    // 1. 提取全局批号
                    String globalTqsjh = "";
                    String globalKzsjh = "";
                    for (OpPcrEntrustOrderSample sample : samplesInGroup) {
                        if (CollectionUtil.isNotEmpty(sample.getTestItem())) {
                            for (OpPcrEntrustOrderItem item : sample.getTestItem()) {
                                if (!Arrays.asList("空白样", "阳性对照", "阴性对照").contains(item.getSampleNo())) {
                                    if (StringUtils.isEmpty(globalTqsjh) && StringUtils.isNotEmpty(item.getTqsjh())) globalTqsjh = item.getTqsjh();
                                    if (StringUtils.isEmpty(globalKzsjh) && StringUtils.isNotEmpty(item.getKzsjh())) globalKzsjh = item.getKzsjh();
                                }
                            }
                        }
                        if (StringUtils.isNotEmpty(globalTqsjh) && StringUtils.isNotEmpty(globalKzsjh)) break;
                    }

                    // 2. 构建表头
                    Row headerRow = sheet.createRow(0);
                    int colIndex = 0;
                    setCellValue(headerRow, colIndex++, "序号");
                    setCellValue(headerRow, colIndex++, "所属牧场");
                    setCellValue(headerRow, colIndex++, "样品名称");
                    int sampleDescColIndex = colIndex; // 记录样品描述列索引
                    setCellValue(headerRow, colIndex++, "样品描述");

                    Map<String, Integer> itemColIndexMap = new HashMap<>();
                    for (String itemName : sortedItemNames) {
                        setCellValue(headerRow, colIndex, itemName);
                        itemColIndexMap.put(itemName, colIndex++);
                    }

                    int remarkColIndex = colIndex++;
                    setCellValue(headerRow, remarkColIndex, "委托备注");
                    int sampleNoColIndex = colIndex++;
                    setCellValue(headerRow, sampleNoColIndex, "样品编号");

                    // 3. 填充数据
                    int rowIndex = 1;
                    int sampleIndex = 0;
                    for (OpPcrEntrustOrderSample sample : samplesInGroup) {
                        Row dataRow = sheet.createRow(rowIndex++);
                        int curCol = 0;
                        setCellValue(dataRow, curCol++, ++sampleIndex);
                        setCellValue(dataRow, curCol++, order.getEntrustDeptName());
                        setCellValue(dataRow, curCol++, sample.getInvbillName());
                        setCellValue(dataRow, curCol++, sample.getName());

                        if(CollectionUtil.isNotEmpty(sample.getTestItem())){
                            for (OpPcrEntrustOrderItem item : sample.getTestItem()) {
                                if (!Arrays.asList("空白样", "阳性对照", "阴性对照").contains(item.getSampleNo())) {
                                    Integer itemCol = itemColIndexMap.get(item.getItemName());
                                    if (itemCol != null) {
                                        setCellValueWithMultipleConditions(dataRow, itemCol, item.getTestResult(), onePlusStyle, twoPlusStyle, threePlusStyle);
                                    }
                                }
                            }
                        }
                        setCellValue(dataRow, remarkColIndex, sample.getRemark());
                        setCellValue(dataRow, sampleNoColIndex, sample.getSampleNo());
                    }

                    // 4. 添加固定行
                    addFixedRows(sheet, rowIndex, sampleDescColIndex, sampleNoColIndex, samplesInGroup, itemColIndexMap, onePlusStyle, twoPlusStyle, threePlusStyle);
                    rowIndex += 3;

                    // 5. 底部追加批号
                    Row tqsjhRow = sheet.createRow(rowIndex++);
                    setCellValue(tqsjhRow, 0, "提取试剂盒批号:" + globalTqsjh);
                    Row kzsjhRow = sheet.createRow(rowIndex++);
                    setCellValue(kzsjhRow, 0, "扩增试剂盒批号:" + globalKzsjh);

                    // 自动列宽调整 (可选)
                    for (int i = 0; i < colIndex; i++) {
                        sheet.autoSizeColumn(i);
                        sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 5 * 256);
                    }
                }
                workbook.write(outputStream);
                outputStream.flush();
            } catch (IOException e) {
                log.error("IO 异常", e);
                throw new RuntimeException("Excel 写入 IO 异常", e);
            }
        } catch (Exception e) {
            log.error("生成 PCR 报告 Excel 失败", e);
            throw new ServiceException("生成报告失败: " + e.getMessage());
        }
    }

    /**
     * 发送PCR报告（每个报告单独发送邮件）
     *
     * @param pcrReportSendDTO PCR报告主对象列表
     * @return 成功发送的数量
     */
    @Override
    public int sendOpPcrReportBase(PcrReportSendDTO pcrReportSendDTO) {
        List<OpPcrEntrustOrder> opJczxPcrReportBase = pcrReportSendDTO.getOpJczxPcrReportBase();
        if (CollectionUtils.isEmpty(opJczxPcrReportBase)) {
            throw new ServiceException("发送报告失败: 报告列表为空");
        }

        // 获取当前登录用户部门信息（只需一次）
        Long deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        SysDept sysDept = sysDeptMapper.selectDeptById(deptId);
        if (sysDept == null) {
            throw new ServiceException("发送报告失败: 无法获取部门信息");
        }
        String[] sender = null;
        sender = configService.selectConfigByKey("email.pcr").split(";");
        String deptEmail = sender[1];
        // 获取授权码
        String emailAuthCode = sender[2];

        if (StringUtils.isEmpty(deptEmail)) {
            throw new ServiceException("发送报告失败: 部门邮箱未配置");
        }
        if (StringUtils.isEmpty(emailAuthCode)) {
            throw new ServiceException("发送报告失败: 部门邮箱授权码未配置");
        }

        // 批量生成Excel报告
        List<PcrReportExcelResult> pcrReportExcelResults = batchGeneratePcrReportExcels(opJczxPcrReportBase);
        if (CollectionUtils.isEmpty(pcrReportExcelResults)) {
            throw new ServiceException("发送报告失败: 生成报告Excel失败");
        }

        int successCount = 0; // 记录成功发送数量
        List<String> successId = new ArrayList<>();

        // 逐个发送邮件
        for (PcrReportExcelResult result : pcrReportExcelResults) {
            String opPcrEntrustOrderId = result.getOpPcrEntrustOrderId();
            if (StringUtils.isEmpty(opPcrEntrustOrderId)) {
                log.warn("跳过发送：报告ID为空");
                continue;
            }

            // 查询委托单以获取联系人邮箱
            OpPcrEntrustOrder order = opPcrEntrustOrderMapper.selectOpPcrEntrustOrderByOpPcrEntrustOrderId(opPcrEntrustOrderId);
            if (order == null) {
                log.warn("跳过发送：未找到委托单，ID={}", opPcrEntrustOrderId);
                continue;
            }

            String toEmail = order.getEntrustContactEmail();
            if (StringUtils.isEmpty(toEmail)) {
                log.warn("跳过发送：邮箱为空，报告ID={}", opPcrEntrustOrderId);
                continue;
            }
            //获取用户表固定邮箱
            String toEmailByUser = pcrReportSendDTO.getEmails();
            Set<String> emailSet = new LinkedHashSet<>();

            // 统一处理逻辑
            String[] emailSources = {toEmail, toEmailByUser};
            for (String emailSource : emailSources) {
                if (StringUtils.isNotEmpty(emailSource)) {
                    String[] emails = emailSource.split(",");
                    for (String email : emails) {
                        if (StringUtils.isNotEmpty(email.trim())) {
                            emailSet.add(email.trim());
                        }
                    }
                }
            }

            String finalEmails = String.join(",", emailSet);

            MultipartFile excelFile = result.getExcelFile();
            if (excelFile == null || excelFile.isEmpty()) {
                log.warn("跳过发送：Excel文件为空，报告ID={}", opPcrEntrustOrderId);
                continue;
            }
            try {
                // 将finalEmails按逗号分割成单个邮箱
                String[] emailArray = finalEmails.split(",");

                for (String singleEmail : emailArray) {
                    if (StringUtils.isNotEmpty(singleEmail.trim())) {
                        try {
                            EmailUtils.sendEmail(
                                    deptEmail,
                                    emailAuthCode,
                                    new String[]{singleEmail.trim()},
                                    "检测中心PCR报告",
                                    "请查收您的PCR检测报告。",
                                    false,
                                    new MultipartFile[]{excelFile}
                            );
                            successCount++;
                            successId.add(opPcrEntrustOrderId);

                            log.info("邮件发送成功：报告ID={}, 收件人={}", opPcrEntrustOrderId, singleEmail.trim());

                        } catch (MessagingException | IOException e) {
                            // 单个邮箱发送失败，记录日志但不中断其他邮箱发送
                            log.error("邮件发送失败：报告ID={}, 收件人={}, 错误信息={}",
                                    opPcrEntrustOrderId, singleEmail.trim(), e.getMessage());
                            // 可以选择记录失败计数
                            // failCount++;
                        }
                    }
                }

                // 检查是否有成功发送的邮件
                if (successCount == 0) {
                    throw new ServiceException("所有邮件发送都失败：报告ID=" + opPcrEntrustOrderId);
                }

            } catch (Exception e) {
                log.error("邮件发送过程出现异常：报告ID={}", opPcrEntrustOrderId, e);
                throw new ServiceException("发送邮件过程失败: 报告ID=" + opPcrEntrustOrderId + ", 错误信息: " + e.getMessage());
            }
//            try {
//                EmailUtils.sendEmail(
//                        deptEmail,
//                        emailAuthCode,
//                        new String[]{finalEmails},
//                        "检测中心饲料报告",
//                        "请查收您的PCR检测报告。",
//                        false,
//                        new MultipartFile[]{excelFile}
//                );
//                successCount++;
//                log.info("邮件发送成功：报告ID={}, 收件人={}", opPcrEntrustOrderId, toEmail);
//
//            } catch (MessagingException | IOException e) {
//                log.error("邮件发送失败：报告ID={}, 收件人={}", opPcrEntrustOrderId, toEmail, e);
//                throw new ServiceException("发送邮件失败: 报告ID=" + opPcrEntrustOrderId + ", 错误信息: " + e.getMessage());
//            }
        }

        if (successCount == 0) {
            throw new ServiceException("发送报告失败: 所有邮件均未成功发送");
        }
        //更新为已发送状态
        opPcrEntrustOrderMapper.updateSendStatusByIdList(successId);

        opPcrEntrustOrderSampleMapper.updateSendStatusByOrderIdAndPcrTaskItemType(successId.get(0),opJczxPcrReportBase.get(0).getPcrTaskItemType());

        return successCount;
    }

    /**
     * 添加固定行（空白样、阳性对照、阴性对照）- *** 已修改 ***
     * 去掉了 tqsjhCol, kzsjhCol 参数，新增 sampleDescCol 参数
     */
    private void addFixedRows(XSSFSheet sheet, int startRow, int sampleDescCol, int sampleNoCol,
                              List<OpPcrEntrustOrderSample> samplesInGroup,
                              Map<String, Integer> itemColIndexMap,
                              XSSFCellStyle onePlusStyle,
                              XSSFCellStyle twoPlusStyle,
                              XSSFCellStyle threePlusStyle) {

        if (CollectionUtil.isEmpty(samplesInGroup) || CollectionUtil.isEmpty(samplesInGroup.get(0).getTestItem())) {
            return;
        }

        List<OpPcrEntrustOrderItem> allItems = samplesInGroup.get(0).getTestItem();

        List<OpPcrEntrustOrderItem> blankItems = allItems.stream().filter(item -> "空白样".equals(item.getSampleNo())).collect(Collectors.toList());
        List<OpPcrEntrustOrderItem> positiveItems = allItems.stream().filter(item -> "阳性对照".equals(item.getSampleNo())).collect(Collectors.toList());
        List<OpPcrEntrustOrderItem> negativeItems = allItems.stream().filter(item -> "阴性对照".equals(item.getSampleNo())).collect(Collectors.toList());

        // --- 1. 空白样 ---
        Row blankRow = sheet.createRow(startRow);
        setCellValue(blankRow, sampleDescCol, "空白样"); // 样品描述
        setCellValue(blankRow, sampleNoCol, "空白样");   // 样品编号
        fillControlItems(blankRow, blankItems, itemColIndexMap, onePlusStyle, twoPlusStyle, threePlusStyle);

        // --- 2. 阳性对照 ---
        Row positiveRow = sheet.createRow(startRow + 1);
        setCellValue(positiveRow, sampleDescCol, "阳性对照"); // 样品描述
        setCellValue(positiveRow, sampleNoCol, "阳性对照");   // 样品编号
        fillControlItems(positiveRow, positiveItems, itemColIndexMap, onePlusStyle, twoPlusStyle, threePlusStyle);

        // --- 3. 阴性对照 ---
        Row negativeRow = sheet.createRow(startRow + 2);
        setCellValue(negativeRow, sampleDescCol, "阴性对照"); // 样品描述
        setCellValue(negativeRow, sampleNoCol, "阴性对照");   // 样品编号
        fillControlItems(negativeRow, negativeItems, itemColIndexMap, onePlusStyle, twoPlusStyle, threePlusStyle);
    }

    // 提取公共填充逻辑
    private void fillControlItems(Row row, List<OpPcrEntrustOrderItem> items, Map<String, Integer> itemColIndexMap,
                                  XSSFCellStyle s1, XSSFCellStyle s2, XSSFCellStyle s3) {
        if (CollectionUtil.isNotEmpty(items)) {
            for (OpPcrEntrustOrderItem item : items) {
                Integer itemCol = itemColIndexMap.get(item.getItemName());
                if (itemCol != null) {
                    setCellValueWithMultipleConditions(row, itemCol, item.getTestResult(), s1, s2, s3);
                }
            }
        }
    }

    // --- 确保 getSortedItemNamesByType 和 setCellValue 方法存在且正确 ---
    /**
     * 根据固定排序规则对项目名称进行排序 (根据样品列表和类型)
     */
    private List<String> getSortedItemNamesByType(List<OpPcrEntrustOrderSample> samplesInGroup, String pcrTaskItemType) {
        //补充空白样、阴性对照、阳性对照数据
        for (OpPcrEntrustOrderSample pcrEntrustOrderSample : samplesInGroup) {
            List<OpPcrEntrustOrderItem> kby = opPcrEntrustOrderItemMapper.seletKbyBySampleId(pcrEntrustOrderSample.getOpPcrEntrustOrderSampleId());
            if (kby != null) { // 添加空指针检查
                pcrEntrustOrderSample.getTestItem().addAll(kby);
            }
        }
        if (samplesInGroup.isEmpty()) {
            return new ArrayList<>();
        }
        OpPcrEntrustOrderSample firstSample = samplesInGroup.get(0);
        if (CollectionUtil.isEmpty(firstSample.getTestItem())) {
            return new ArrayList<>();
        }

        List<String> originalItemList = firstSample.getTestItem().stream()
                .map(OpPcrEntrustOrderItem::getItemName)
                .filter(StringUtils::isNotEmpty)
                .distinct()
                .collect(Collectors.toList());

        Map<String, List<String>> sortRulesMap = new HashMap<>();
        sortRulesMap.put("4", Arrays.asList("金黄色葡萄球菌"));
        sortRulesMap.put("5", Arrays.asList("牛冠状病毒", "牛轮状病毒", "隐孢子虫", "肠毒素型细菌-大肠杆菌"));
        sortRulesMap.put("6", Arrays.asList("牛冠状病毒", "牛病毒性腹泻病毒", "牛呼吸道合胞体病毒", "牛副流感病毒3型", "牛支原体", "溶血性曼氏杆菌", "多杀巴斯德杆菌"));
        sortRulesMap.put("7", Arrays.asList("绿脓杆菌", "无乳链球菌", "β-内酰胺酶抗性基因", "停乳链球菌", "克雷伯氏菌属", "牛支原体", "大肠杆菌", "金黄色葡萄球菌"));
        sortRulesMap.put("8", Arrays.asList("牛支原体"));
        sortRulesMap.put("9", Arrays.asList("无乳链球菌"));

        List<String> rule = sortRulesMap.get(pcrTaskItemType);

        if (rule != null) {
            // (这里的逻辑基于你的原代码，如果对照组的项目名称与真实项目名称有重叠，这是能正常工作的)
            if (originalItemList.containsAll(rule) && rule.containsAll(originalItemList)) {
                return rule.stream()
                        .filter(originalItemList::contains)
                        .collect(Collectors.toList());
            } else {
                List<String> sortedKnownItems = rule.stream()
                        .filter(originalItemList::contains)
                        .collect(Collectors.toList());
                List<String> newItems = originalItemList.stream()
                        .filter(item -> !rule.contains(item))
                        .collect(Collectors.toList());
                sortedKnownItems.addAll(newItems);
                return sortedKnownItems;
            }
        }
        return originalItemList;
    }


    /**
     * 设置单元格值 (Helper Method)
     */
    private void setCellValue(Row row, int cellIndex, Object value) {
        Cell cell = row.createCell(cellIndex);
        if (value != null) {
            if (value instanceof Number) {
                cell.setCellValue(((Number) value).doubleValue());
            } else if (value instanceof Date) {
                cell.setCellValue((Date) value);
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            } else {
                cell.setCellValue(value.toString());
            }
        } else {
            cell.setCellValue("");
        }
    }

    /**
     * 创建带背景色的单元格样式
     */
    private XSSFCellStyle createCellStyleWithBackground(XSSFWorkbook workbook, IndexedColors color) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(color.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }

    /**
     * 创建带背景色的单元格样式 (增加字体颜色支持)
     */
    private XSSFCellStyle createCellStyleWithBackground(XSSFWorkbook workbook, IndexedColors backgroundColor, IndexedColors fontColor) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(backgroundColor.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 如果指定了字体颜色，则创建并设置字体
        if (fontColor != null) {
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setColor(fontColor.getIndex());
            style.setFont(font);
        }

        return style;
    }

    /**
     * 设置单元格值并根据加号数量应用不同样式
     */
    private void setCellValueWithMultipleConditions(Row row, int cellIndex, Object value,
                                                    XSSFCellStyle onePlusStyle,
                                                    XSSFCellStyle twoPlusStyle,
                                                    XSSFCellStyle threePlusStyle) {
        Cell cell = row.createCell(cellIndex);

        // 设置单元格值（复用原有逻辑）
        if (value != null) {
            if (value instanceof Number) {
                cell.setCellValue(((Number) value).doubleValue());
            } else if (value instanceof Date) {
                cell.setCellValue((Date) value);
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            } else {
                cell.setCellValue(value.toString());
            }
        } else {
            cell.setCellValue("");
        }

        // 根据加号数量应用不同的背景色
        if (value != null) {
            String stringValue = value.toString().trim();
            if ("+".equals(stringValue)) {
                cell.setCellStyle(onePlusStyle);        // 一个加号：黄色
            } else if ("++".equals(stringValue)) {
                cell.setCellStyle(twoPlusStyle);        // 两个加号：深红
            } else if ("+++".equals(stringValue)) {
                cell.setCellStyle(threePlusStyle);      // 三个加号：红色
            }
            // 其他值不设置特殊样式，使用默认样式
        }
    }
    /**
     * 批量生成PCR报告Excel（返回MultipartFile）
     */
    public List<PcrReportExcelResult> batchGeneratePcrReportExcels(List<OpPcrEntrustOrder> orders) {
        log.info("开始批量生成PCR报告Excel，共 {} 个委托单", orders.size());
        List<PcrReportExcelResult> results = new ArrayList<>(orders.size());

        for (OpPcrEntrustOrder orderDTO : orders) {
            String orderId = orderDTO.getOpPcrEntrustOrderId();
            String targetTaskItemType = orderDTO.getPcrTaskItemType();

            try {
                OpPcrEntrustOrder dbOrder = opPcrEntrustOrderMapper.selectOrderDetailById(orderId);
                if (dbOrder == null) throw new RuntimeException("未找到委托单数据");
                String orderNo = dbOrder.getEntrustOrderNo();

                if (StringUtils.isEmpty(targetTaskItemType)) continue;

                Map<String, List<OpPcrEntrustOrderSample>> groupedSamples = dbOrder.getSampleList().stream()
                        .filter(s -> StringUtils.isNotEmpty(s.getPcrTaskItemType()))
                        .filter(s -> s.getPcrTaskItemType().equals(targetTaskItemType))
                        .collect(Collectors.groupingBy(OpPcrEntrustOrderSample::getPcrTaskItemType));

                if (groupedSamples.isEmpty()) continue;

                try (XSSFWorkbook workbook = new XSSFWorkbook();
                     ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {

                    XSSFCellStyle onePlusStyle = createCellStyleWithBackground(workbook, IndexedColors.YELLOW);
                    XSSFCellStyle twoPlusStyle = createCellStyleWithBackground(workbook, IndexedColors.DARK_RED, IndexedColors.WHITE);
                    XSSFCellStyle threePlusStyle = createCellStyleWithBackground(workbook, IndexedColors.RED);

                    for (Map.Entry<String, List<OpPcrEntrustOrderSample>> entry : groupedSamples.entrySet()) {
                        String pcrTaskItemType = entry.getKey();
                        List<OpPcrEntrustOrderSample> samplesInGroup = entry.getValue();

                        String sheetName = DictUtils.getDictLabel("pcr_task_item_type", pcrTaskItemType);
                        if (StringUtils.isEmpty(sheetName)) sheetName = "Type_" + pcrTaskItemType;
                        sheetName = sheetName.replaceAll("[\\\\/*?\\[\\]:]", "_").substring(0, Math.min(sheetName.length(), 30));
                        XSSFSheet sheet = workbook.createSheet(sheetName);

                        List<String> sortedItemNames = getSortedItemNamesByType(samplesInGroup, pcrTaskItemType);

                        // 1. 提取全局批号
                        String globalTqsjh = "";
                        String globalKzsjh = "";
                        for (OpPcrEntrustOrderSample sample : samplesInGroup) {
                            if (CollectionUtil.isNotEmpty(sample.getTestItem())) {
                                for (OpPcrEntrustOrderItem item : sample.getTestItem()) {
                                    if (!Arrays.asList("空白样", "阳性对照", "阴性对照").contains(item.getSampleNo())) {
                                        if (StringUtils.isEmpty(globalTqsjh) && StringUtils.isNotEmpty(item.getTqsjh())) globalTqsjh = item.getTqsjh();
                                        if (StringUtils.isEmpty(globalKzsjh) && StringUtils.isNotEmpty(item.getKzsjh())) globalKzsjh = item.getKzsjh();
                                    }
                                }
                            }
                            if (StringUtils.isNotEmpty(globalTqsjh) && StringUtils.isNotEmpty(globalKzsjh)) break;
                        }

                        // 2. 构建表头
                        Row headerRow = sheet.createRow(0);
                        int colIndex = 0;
                        setCellValue(headerRow, colIndex++, "序号");
                        setCellValue(headerRow, colIndex++, "所属牧场");
                        setCellValue(headerRow, colIndex++, "样品名称");
                        int sampleDescColIndex = colIndex;
                        setCellValue(headerRow, colIndex++, "样品描述");

                        Map<String, Integer> itemColIndexMap = new HashMap<>();
                        for (String itemName : sortedItemNames) {
                            setCellValue(headerRow, colIndex, itemName);
                            itemColIndexMap.put(itemName, colIndex++);
                        }

                        int remarkColIndex = colIndex++;
                        setCellValue(headerRow, remarkColIndex, "委托备注");
                        int sampleNoColIndex = colIndex++;
                        setCellValue(headerRow, sampleNoColIndex, "样品编号");

                        // 3. 填充数据
                        int rowIndex = 1;
                        int sampleIndex = 0;
                        for (OpPcrEntrustOrderSample sample : samplesInGroup) {
                            Row dataRow = sheet.createRow(rowIndex++);
                            int curCol = 0;
                            setCellValue(dataRow, curCol++, ++sampleIndex);
                            setCellValue(dataRow, curCol++, dbOrder.getEntrustDeptName());
                            setCellValue(dataRow, curCol++, sample.getInvbillName());
                            setCellValue(dataRow, curCol++, sample.getName());

                            if (CollectionUtil.isNotEmpty(sample.getTestItem())) {
                                for (OpPcrEntrustOrderItem item : sample.getTestItem()) {
                                    if (!Arrays.asList("空白样", "阳性对照", "阴性对照").contains(item.getSampleNo())) {
                                        Integer itemCol = itemColIndexMap.get(item.getItemName());
                                        if (itemCol != null) {
                                            setCellValueWithMultipleConditions(dataRow, itemCol, item.getTestResult(), onePlusStyle, twoPlusStyle, threePlusStyle);
                                        }
                                    }
                                }
                            }
                            setCellValue(dataRow, remarkColIndex, sample.getRemark());
                            setCellValue(dataRow, sampleNoColIndex, sample.getSampleNo());
                        }

                        // 4. 添加固定行
                        addFixedRows(sheet, rowIndex, sampleDescColIndex, sampleNoColIndex, samplesInGroup, itemColIndexMap, onePlusStyle, twoPlusStyle, threePlusStyle);
                        rowIndex += 3;

                        // 5. 底部追加批号
                        Row tqsjhRow = sheet.createRow(rowIndex++);
                        setCellValue(tqsjhRow, 0, "提取试剂盒批号:" + globalTqsjh);
                        Row kzsjhRow = sheet.createRow(rowIndex++);
                        setCellValue(kzsjhRow, 0, "扩增试剂盒批号:" + globalKzsjh);

                        // 自动列宽调整 (可选)
                        for (int i = 0; i < colIndex; i++) {
                            sheet.autoSizeColumn(i);
                            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 5 * 256);
                        }
                    }

                    workbook.write(byteOut);
                    byte[] excelBytes = byteOut.toByteArray();
                    String fileName = "PCR检测报告_" + orderNo + ".xlsx";
                    MultipartFile multipartFile = new MockMultipartFile("file", fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelBytes);
                    results.add(new PcrReportExcelResult(orderId, multipartFile));

                } catch (IOException e) {
                    log.error("IO Exception", e);
                }
            } catch (Exception e) {
                log.error("处理委托单 {} 异常", orderId, e);
            }
        }
        return results;
    }
    /**
     * 查询发送报告邮箱
     */
    @Override
    public List<ReportEmailVo> selectOpJczxPcrReportBaseEmailList(String opPcrEntrustOrderId) {

        OpPcrEntrustOrder opPcrEntrustOrder = opPcrEntrustOrderMapper.selectOpPcrEntrustOrderByOpPcrEntrustOrderId(opPcrEntrustOrderId);

        //获取检测中心通讯方式
        List<ReportEmailVo> jczxEmailList = bsContactMapper.selectPcrEmailByDeptId(SecurityUtils.getDeptId());

        // 获取通讯方式表邮箱列表（委托部门）
        List<ReportEmailVo> toEmailByContact = bsContactMapper.selectPcrEmailByDeptId(opPcrEntrustOrder.getEntrustDeptId());

        // 合并两个邮箱列表并去重（检测中心邮箱 + 委托部门邮箱）
        List<ReportEmailVo> emailList = Stream.concat(
                        // 检测中心邮箱流
                        Optional.ofNullable(jczxEmailList).orElse(Collections.emptyList()).stream()
                                .filter(vo -> vo != null && StringUtils.isNotBlank(vo.getEmail())), // 过滤空对象和空邮箱
                        // 委托部门邮箱流
                        Optional.ofNullable(toEmailByContact).orElse(Collections.emptyList()).stream()
                                .filter(vo -> vo != null && StringUtils.isNotBlank(vo.getEmail())) // 过滤空对象和空邮箱
                )
                // 收集并去重：根据邮箱地址去重，保留第一个出现的重复项
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                vo -> vo.getEmail().trim().toLowerCase(), // 去重键：去除空格并转为小写
                                Function.identity(),                       // 值：对象本身
                                (first, second) -> first                   // 合并函数：重复时保留第一个
                        ),
                        map -> new ArrayList<>(map.values())           // 将Map的值转换为List
                ));

        return emailList;
    }
    @Override
    public Map<String, Object> selectPcrReportDetail(OpPcrEntrustOrder dto) {
        Map<String, Object> result = new HashMap<>();

        // 1. 查询基础详情
        OpPcrEntrustOrder order = opPcrEntrustOrderMapper.selectOrderDetailById(dto.getOpPcrEntrustOrderId());
        if (order == null) {
            return result;
        }

        // 2. 过滤样品：只保留与入参类型一致的样品
        if (CollectionUtil.isNotEmpty(order.getSampleList())) {
            List<OpPcrEntrustOrderSample> filteredSamples = order.getSampleList().stream()
                    .filter(s -> StringUtils.isNotEmpty(s.getPcrTaskItemType()))
                    .filter(s -> s.getPcrTaskItemType().equals(dto.getPcrTaskItemType()))
                    .collect(Collectors.toList());
            order.setSampleList(filteredSamples);
        }

        if (CollectionUtil.isEmpty(order.getSampleList())) {
            result.put("orderData", order);
            result.put("headerList", new ArrayList<>());
            return result;
        }

        // 3. 获取排序后的表头
        // 关键点：此方法会副作用修改 order.getSampleList()，将对照组数据(kby) addAll 到每个样品的 testItem 中
        // 这正是我们需要的，因为后面要从中提取对照组数据
        List<String> sortedHeader = getSortedItemNamesByType(order.getSampleList(), dto.getPcrTaskItemType());
        result.put("headerList", sortedHeader);

        // 4. 构造虚拟的对照组行 (空白样、阳性对照、阴性对照)
        // 从第一个真实样品中提取对照组数据 (因为 getSortedItemNamesByType 已经把 kby 加进去了)
        if (CollectionUtil.isNotEmpty(order.getSampleList())) {
            OpPcrEntrustOrderSample firstSample = order.getSampleList().get(0);
            List<OpPcrEntrustOrderItem> allItems = firstSample.getTestItem();

            if (CollectionUtil.isNotEmpty(allItems)) {
                // 构造三个虚拟样品对象，分别对应三行
                OpPcrEntrustOrderSample blankSample = buildControlSample("空白样", allItems);
                OpPcrEntrustOrderSample positiveSample = buildControlSample("阳性对照", allItems);
                OpPcrEntrustOrderSample negativeSample = buildControlSample("阴性对照", allItems);

                // 5. 清理真实样品数据
                // 此时真实样品的 testItem 里混杂了 sampleNo="空白样" 等对照数据，
                // 必须移除，否则前端渲染真实样品行时可能会被对照数据覆盖
                cleanRealSamples(order.getSampleList());

                // 6. 将虚拟行追加到列表末尾
                order.getSampleList().add(blankSample);
                order.getSampleList().add(positiveSample);
                order.getSampleList().add(negativeSample);
            }
        }

        result.put("orderData", order);
        return result;
    }

    @Override
    public Map<String, Object> selectPcrReportDetail2(OpPcrEntrustOrder dto) {
        Map<String, Object> result = new HashMap<>();

        // 1. 查询基础详情
        OpPcrEntrustOrder order = opPcrEntrustOrderMapper.selectOrderDetailById(dto.getOpPcrEntrustOrderId());
        if (order == null) {
            return result;
        }

        // 保存原始的全量样品列表，用于后续查找跨类型的对照数据
        List<OpPcrEntrustOrderSample> allSamples = new ArrayList<>(order.getSampleList());

        // 2. 过滤样品：只保留与入参类型一致的样品 (用于界面展示)
        List<OpPcrEntrustOrderSample> targetSamples = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(allSamples)) {
            targetSamples = allSamples.stream()
                    .filter(s -> StringUtils.isNotEmpty(s.getPcrTaskItemType()))
                    .filter(s -> s.getPcrTaskItemType().equals(dto.getPcrTaskItemType()))
                    .collect(Collectors.toList());
        }

        if (CollectionUtil.isEmpty(targetSamples)) {
            order.setSampleList(new ArrayList<>());
            result.put("orderData", order);
            result.put("headerList", new ArrayList<>());
            return result;
        }

        // 3. 获取排序后的表头 (基于目标类型的样品)
        // 注意：getSortedItemNamesByType 会副作用修改 targetSamples，给它们填充kby数据，但这仅限于targetSamples自己的关联文件
        List<String> sortedHeader = getSortedItemNamesByType(targetSamples, dto.getPcrTaskItemType());
        result.put("headerList", sortedHeader);

        // ===========================================================================================
        // 4. 【关键修复】从“所有样品”中汇总对照组数据
        // 即使某个对照数据关联在 Type 4 的样品上，只要它属于当前 Type 7 的表头项目，也应该被显示
        // ===========================================================================================
        List<OpPcrEntrustOrderItem> allControlItems = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(allSamples)) {
            for (OpPcrEntrustOrderSample sample : allSamples) {
                // 主动查询每个样品的对照数据 (因为 allSamples 里包含未经过 getSortedItemNamesByType 处理的非目标类型样品)
                List<OpPcrEntrustOrderItem> kby = opPcrEntrustOrderItemMapper.seletKbyBySampleId(sample.getOpPcrEntrustOrderSampleId());
                if (CollectionUtil.isNotEmpty(kby)) {
                    allControlItems.addAll(kby);
                }
                // 同时也要把可能已经存在于 testItem 里的对照数据加进来 (防守性编程)
                if (CollectionUtil.isNotEmpty(sample.getTestItem())) {
                    List<OpPcrEntrustOrderItem> existingControls = sample.getTestItem().stream()
                            .filter(item -> Arrays.asList("空白样", "阳性对照", "阴性对照").contains(item.getSampleNo()))
                            .collect(Collectors.toList());
                    allControlItems.addAll(existingControls);
                }
            }
        }

        // 5. 过滤和去重对照组数据
        // 规则1：只保留 "项目名称" 存在于当前报表表头 (sortedHeader) 中的对照数据
        // 规则2：根据 "对照类型(sampleNo) + 项目名称(itemName)" 去重
        List<OpPcrEntrustOrderItem> distinctControlItems = allControlItems.stream()
                .filter(item -> sortedHeader.contains(item.getItemName())) // 只保留属于当前报表的项目
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(
                                item -> item.getSampleNo() + "_" + item.getItemName()))),
                        ArrayList::new
                ));

        // 6. 构造虚拟的对照组行
        if (CollectionUtil.isNotEmpty(distinctControlItems)) {
            OpPcrEntrustOrderSample blankSample = buildControlSample("空白样", distinctControlItems);
            OpPcrEntrustOrderSample positiveSample = buildControlSample("阳性对照", distinctControlItems);
            OpPcrEntrustOrderSample negativeSample = buildControlSample("阴性对照", distinctControlItems);

            // 7. 清理真实样品数据 (移除混在 targetSamples 里的对照数据，避免前端显示重复)
            cleanRealSamples(targetSamples);

            // 8. 将虚拟行追加到列表末尾
            targetSamples.add(blankSample);
            targetSamples.add(positiveSample);
            targetSamples.add(negativeSample);
        } else {
            cleanRealSamples(targetSamples);
        }

        // 9. 更新 Order 的样品列表为最终处理好的列表
        order.setSampleList(targetSamples);

        result.put("orderData", order);
        return result;
    }

    @Override
    public List<OpPcrReportListVo> queryPcrReport(OpJczxTestTaskDto opJczxTestTaskDto) {
        return opPcrEntrustOrderMapper.selectPcrAuditedList(opJczxTestTaskDto);
    }

    @Override
    public int batchSendPcrReport(List<PcrReportSendDTO> pcrReportSendDTOList) {
        int successCount = 0; // 记录成功发送数量

        for (PcrReportSendDTO pcrReportSendDTO : pcrReportSendDTOList) {
            List<OpPcrEntrustOrder> opJczxPcrReportBase = pcrReportSendDTO.getOpJczxPcrReportBase();
            if (CollectionUtils.isEmpty(opJczxPcrReportBase)) {
                throw new ServiceException("发送报告失败: 报告列表为空");
            }

            // 获取当前登录用户部门信息（只需一次）
            Long deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
            SysDept sysDept = sysDeptMapper.selectDeptById(deptId);
            if (sysDept == null) {
                throw new ServiceException("发送报告失败: 无法获取部门信息");
            }
            String deptEmail = sysDept.getEmail();
            String emailAuthCode = sysDept.getEmailAuthorizationCode();

            if (StringUtils.isEmpty(deptEmail)) {
                throw new ServiceException("发送报告失败: 部门邮箱未配置");
            }
            if (StringUtils.isEmpty(emailAuthCode)) {
                throw new ServiceException("发送报告失败: 部门邮箱授权码未配置");
            }

            // 批量生成Excel报告
            List<PcrReportExcelResult> pcrReportExcelResults = batchGeneratePcrReportExcels(opJczxPcrReportBase);
            if (CollectionUtils.isEmpty(pcrReportExcelResults)) {
                throw new ServiceException("发送报告失败: 生成报告Excel失败");
            }

            List<String> successId = new ArrayList<>();

            // 逐个发送邮件
            for (PcrReportExcelResult result : pcrReportExcelResults) {
                String opPcrEntrustOrderId = result.getOpPcrEntrustOrderId();
                if (StringUtils.isEmpty(opPcrEntrustOrderId)) {
                    log.warn("跳过发送：报告ID为空");
                    continue;
                }

                // 查询委托单以获取联系人邮箱
                OpPcrEntrustOrder order = opPcrEntrustOrderMapper.selectOpPcrEntrustOrderByOpPcrEntrustOrderId(opPcrEntrustOrderId);
                if (order == null) {
                    log.warn("跳过发送：未找到委托单，ID={}", opPcrEntrustOrderId);
                    continue;
                }

                String toEmail = order.getEntrustContactEmail();
                if (StringUtils.isEmpty(toEmail)) {
                    log.warn("跳过发送：邮箱为空，报告ID={}", opPcrEntrustOrderId);
                    continue;
                }
                //获取用户表固定邮箱
                String toEmailByUser = pcrReportSendDTO.getEmails();
                Set<String> emailSet = new LinkedHashSet<>();

                // 统一处理逻辑
                String[] emailSources = {toEmail, toEmailByUser};
                for (String emailSource : emailSources) {
                    if (StringUtils.isNotEmpty(emailSource)) {
                        String[] emails = emailSource.split(",");
                        for (String email : emails) {
                            if (StringUtils.isNotEmpty(email.trim())) {
                                emailSet.add(email.trim());
                            }
                        }
                    }
                }

                String finalEmails = String.join(",", emailSet);

                MultipartFile excelFile = result.getExcelFile();
                if (excelFile == null || excelFile.isEmpty()) {
                    log.warn("跳过发送：Excel文件为空，报告ID={}", opPcrEntrustOrderId);
                    continue;
                }
                try {
                    // 将finalEmails按逗号分割成单个邮箱
                    String[] emailArray = finalEmails.split(",");

                    for (String singleEmail : emailArray) {
                        if (StringUtils.isNotEmpty(singleEmail.trim())) {
                            try {
                                EmailUtils.sendEmail(
                                        deptEmail,
                                        emailAuthCode,
                                        new String[]{singleEmail.trim()},
                                        "检测中心PCR报告",
                                        "请查收您的PCR检测报告。",
                                        false,
                                        new MultipartFile[]{excelFile}
                                );
                                successCount++;
                                successId.add(opPcrEntrustOrderId);

                                log.info("邮件发送成功：报告ID={}, 收件人={}", opPcrEntrustOrderId, singleEmail.trim());

                            } catch (MessagingException | IOException e) {
                                // 单个邮箱发送失败，记录日志但不中断其他邮箱发送
                                log.error("邮件发送失败：报告ID={}, 收件人={}, 错误信息={}",
                                        opPcrEntrustOrderId, singleEmail.trim(), e.getMessage());
                                // 可以选择记录失败计数
                                // failCount++;
                            }
                        }
                    }

                    // 检查是否有成功发送的邮件
                    if (successCount == 0) {
                        throw new ServiceException("所有邮件发送都失败：报告ID=" + opPcrEntrustOrderId);
                    }

                } catch (Exception e) {
                    log.error("邮件发送过程出现异常：报告ID={}", opPcrEntrustOrderId, e);
                    throw new ServiceException("发送邮件过程失败: 报告ID=" + opPcrEntrustOrderId + ", 错误信息: " + e.getMessage());
                }
//            try {
//                EmailUtils.sendEmail(
//                        deptEmail,
//                        emailAuthCode,
//                        new String[]{finalEmails},
//                        "检测中心饲料报告",
//                        "请查收您的PCR检测报告。",
//                        false,
//                        new MultipartFile[]{excelFile}
//                );
//                successCount++;
//                log.info("邮件发送成功：报告ID={}, 收件人={}", opPcrEntrustOrderId, toEmail);
//
//            } catch (MessagingException | IOException e) {
//                log.error("邮件发送失败：报告ID={}, 收件人={}", opPcrEntrustOrderId, toEmail, e);
//                throw new ServiceException("发送邮件失败: 报告ID=" + opPcrEntrustOrderId + ", 错误信息: " + e.getMessage());
//            }
            }

            if (successCount == 0) {
                throw new ServiceException("发送报告失败: 所有邮件均未成功发送");
            }
            //更新为已发送状态
            opPcrEntrustOrderMapper.updateSendStatusByIdList(successId);

            opPcrEntrustOrderSampleMapper.updateSendStatusByOrderIdAndPcrTaskItemType(successId.get(0),opJczxPcrReportBase.get(0).getPcrTaskItemType());
        }
        return successCount;
    }

    /**
     * 辅助方法：构造虚拟对照样品对象
     */
    private OpPcrEntrustOrderSample buildControlSample(String controlName, List<OpPcrEntrustOrderItem> sourceItems) {
        OpPcrEntrustOrderSample sample = new OpPcrEntrustOrderSample();
        // 设置“样品编号”列显示的内容
        sample.setSampleNo(controlName);
        // 其他描述列置空
        sample.setInvbillName("");
        sample.setName("");
        sample.setRemark("");

        // 筛选出属于该对照组的检测项（sampleNo 等于 "空白样"/"阳性对照" 等）
        // Debug信息显示：对照组的 item.getSampleNo() 是有值的，而真实数据的 sampleNo 是 null
        List<OpPcrEntrustOrderItem> controlItems = sourceItems.stream()
                .filter(item -> controlName.equals(item.getSampleNo()))
                .collect(Collectors.toList());

        sample.setTestItem(controlItems);
        return sample;
    }

    /**
     * 辅助方法：清理真实样品中的对照组数据
     */
    private void cleanRealSamples(List<OpPcrEntrustOrderSample> sampleList) {
        for (OpPcrEntrustOrderSample sample : sampleList) {
            if (CollectionUtil.isNotEmpty(sample.getTestItem())) {
                // 只保留 sampleNo 为 null 的项（根据Debug信息，真实检测项的 sampleNo 字段为 null）
                // 过滤掉 sampleNo 为 "空白样", "阳性对照" 等的项
                List<OpPcrEntrustOrderItem> realItems = sample.getTestItem().stream()
                        .filter(item -> item.getSampleNo() == null)
                        .collect(Collectors.toList());
                sample.setTestItem(realItems);
            }
        }
    }
}
