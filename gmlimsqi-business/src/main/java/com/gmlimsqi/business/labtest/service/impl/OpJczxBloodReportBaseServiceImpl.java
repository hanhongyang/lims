package com.gmlimsqi.business.labtest.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.labtest.domain.OpJczxBloodResultInfo;
import com.gmlimsqi.business.basicdata.mapper.BsContactMapper;
import com.gmlimsqi.business.labtest.domain.*;
import com.gmlimsqi.business.labtest.dto.BiochemistryItem;
import com.gmlimsqi.business.labtest.dto.BloodEmailDTO;
import com.gmlimsqi.business.labtest.mapper.*;
import com.gmlimsqi.business.labtest.service.IOpJczxBloodReportBaseService;
import com.gmlimsqi.business.labtest.vo.OpBloodJHReportSampleVo;
import com.gmlimsqi.business.labtest.vo.ReportEmailVo;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.FileConvertUtils;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.core.domain.entity.SysDept;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.enums.EntrustOrderStatusEnum;
import com.gmlimsqi.common.enums.JczxFeedReportStatusEnum;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.DictUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.email.EmailUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.framework.web.service.TokenService;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import com.gmlimsqi.system.mapper.SysUserMapper;
import com.gmlimsqi.system.service.ISysConfigService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 检测中心血样报告主Service业务层处理
 *
 * @author hhy
 * @date 2025-10-22
 */
@Service
public class OpJczxBloodReportBaseServiceImpl implements IOpJczxBloodReportBaseService {
    private static final Logger logger = LoggerFactory.getLogger(OpJczxBloodReportBaseServiceImpl.class);
    @Autowired
    private OpJczxBloodReportBaseMapper opJczxBloodReportBaseMapper;
    @Autowired
    private OpJczxBloodResultBaseMapper opJczxBloodResultBaseMapper;
    @Autowired
    private OpJczxBloodReportInfoMapper opJczxBloodReportInfoMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private OpBloodEntrustOrderMapper opBloodEntrustOrderMapper;
    @Autowired
    private OpBloodEntrustOrderSampleMapper opBloodEntrustOrderSampleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private BsContactMapper bsContactMapper;

    @Value("${ruoyi.profile}")
    private String profile;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 查询检测中心血样报告主
     *
     * @param opJczxBloodReportBase 检测中心血样报告主主键
     * @return 检测中心血样报告主
     */
    @Override
    public OpJczxBloodReportBase selectOpJczxBloodReportBaseByOpJczxBloodReportBaseId(OpJczxBloodReportBase opJczxBloodReportBase) {

        String status = opJczxBloodReportBase.getStatus();
        if (ObjectUtils.isEmpty(status)) {
            throw new RuntimeException("缺少参数，请联系管理员");
        }

        OpJczxBloodReportBase reportBase = new OpJczxBloodReportBase();
        List<OpJczxBloodReportInfo> yinList = new ArrayList<>();//阴性
        List<OpJczxBloodReportInfo> yangList = new ArrayList<>();//阳性
        List<OpJczxBloodReportInfo> ktyList = new ArrayList<>();//口蹄疫
        List<OpJczxBloodReportInfo> zaoyunList = new ArrayList<>();//早孕
        List<OpJczxBloodReportInfo> shList = new ArrayList<>();//生化
        List<OpJczxBloodReportInfo> fjhList = new ArrayList<>();//副结核
        List<OpJczxBloodReportInfo> bvdvkyList = new ArrayList<>();//BVDV抗原
        OpBloodJHReportSampleVo opBloodJHReportSampleVo = new OpBloodJHReportSampleVo();


        String bvdvkySampleName = "";
        String bvdvkySampleState = "";

        String itemType = opJczxBloodReportBase.getBloodTaskItemType();
        List<OpBloodEntrustOrderSample> sampleList = new ArrayList<>();
        StringBuilder testEvaluation = new StringBuilder();
        StringBuilder testType = new StringBuilder();//检测类型

        //待制作
        if (status.equals(JczxFeedReportStatusEnum.DZZ.getCode())) {
            String bloodEntrustOrderId = opJczxBloodReportBase.getBloodEntrustOrderId();
            if (StringUtils.isEmpty(bloodEntrustOrderId)) {
                throw new RuntimeException("缺少参数，请联系管理员");
            }

            //查询委托单
            OpBloodEntrustOrder opBloodEntrustOrder = opBloodEntrustOrderMapper.selectOpBloodEntrustOrderByOpBloodEntrustOrderId(bloodEntrustOrderId);
            BeanUtils.copyProperties(opBloodEntrustOrder, reportBase);
            reportBase.setBloodEntrustOrderId(opBloodEntrustOrder.getOpBloodEntrustOrderId());
            reportBase.setSampleAmount(String.valueOf(opBloodEntrustOrder.getTotalSampleQuantity()));
            // [!code ++] // 补充委托单中的接收时间，用于早孕报告
            reportBase.setReceiveTime(opBloodEntrustOrder.getReceiveTime());

            //明细表
            OpBloodEntrustOrderSample sample = new OpBloodEntrustOrderSample();
            sample.setOpBloodEntrustOrderId(opJczxBloodReportBase.getBloodEntrustOrderId());
            sampleList = opBloodEntrustOrderSampleMapper.selectOpBloodEntrustOrderSampleList(sample);


            if (itemType.equals("2") || itemType.equals("6")) {//结核（牛分枝杆菌PPD）,结核抗体
                //根据阳性，阴性组装
                for (OpBloodEntrustOrderSample opBloodEntrustOrderSample : sampleList) {
                    String testResult = opBloodEntrustOrderSample.getTestResult();
                    //使用字典转换
                    testResult = DictUtils.getDictValue("yinyang", testResult);
                    OpJczxBloodReportInfo vo = new OpJczxBloodReportInfo();
                    vo.setSampleName(opBloodEntrustOrderSample.getSampleName());
                    vo.setSampleNo(opBloodEntrustOrderSample.getSampleNo());
                    vo.setGh(opBloodEntrustOrderSample.getGh());
                    vo.setSp(opBloodEntrustOrderSample.getSp());
                    vo.setSequence(opBloodEntrustOrderSample.getSequence());
                    if (testResult.equals("1")) {
                        vo.setTestResult("1");
                        yangList.add(vo);
                    } else if (testResult.equals("0")) {
                        vo.setTestResult("0");
                        yinList.add(vo);
                    }
                }
                if (itemType.equals("2")) {
                    reportBase.setTestEvaluation("牛分枝杆菌PPD皮内变态反应试验：检测" + yangList.size() + yinList.size() + "份，阴性" + yinList.size() + "份 ");
                    reportBase.setTestType("牛分枝杆菌PPD皮内变态反应试验");
                } else if(itemType.equals("6")){
                    int sum = yangList.size() + yinList.size();
                    int yangCount = yangList.size();
                    BigDecimal yangRate = BigDecimal.ZERO;
                    if (sum != 0) {
                        BigDecimal total = new BigDecimal(sum);
                        yangRate = new BigDecimal(yangCount)
                                .divide(total, 2, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100)); // 转换为百分比
                    }
                    reportBase.setTestEvaluation("牛结核病抗体ELISA检测：检测" + sum + "份，阳性" + yangList.size() + "份，阳性率" + yangRate + "%");
                    reportBase.setTestType("牛结核抗体检测");

                }
            } else if (itemType.equals("0")) {//口蹄疫
                BigDecimal AyangRate = BigDecimal.ZERO;
                BigDecimal OyangRate = BigDecimal.ZERO;

                int AYangCount = 0;
                int OYangCount = 0;
                //是否显示A型/O型
                boolean AisShow = false;
                boolean OisShow = false;

                // 计算阳性份数
                AYangCount = (int) sampleList.stream()
                        .map(OpBloodEntrustOrderSample::getATestResult)
                        .filter(str -> str != null && "+".equals(str.trim()))
                        .count();

                OYangCount = (int) sampleList.stream()
                        .map(OpBloodEntrustOrderSample::getOTestResult)
                        .filter(str -> str != null && "+".equals(str.trim()))
                        .count();

                int totalCount = sampleList.size();
                // 计算百分比（乘以100）
                if (totalCount > 0) {
                    BigDecimal total = new BigDecimal(totalCount);
                    if (AYangCount > 0) {
                        AisShow = true;
                        AyangRate = new BigDecimal(AYangCount)
                                .divide(total, 2, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100)); // 转换为百分比
                        testEvaluation.append("A型口蹄疫抗体：检测" + totalCount + "份，阳性" + AYangCount + "份，阳性率" + AyangRate + "%");
                        testType.append("口蹄疫A型抗体");

                    }

                    if (OYangCount > 0) {
                        OisShow = true;
                        OyangRate = new BigDecimal(OYangCount)
                                .divide(total, 2, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100)); // 转换为百分比
                        testEvaluation.append("$$O型口蹄疫抗体：检测" + totalCount + "份，阳性" + OYangCount + "份，阳性率" + OyangRate + "%");

                        testType.append(StringUtils.isEmpty(testType) ? "口蹄疫O型抗体" : "、口蹄疫O型抗体");
                    }
                }
                ktyList = sampleList.stream()
                        .map(itme -> {
                            OpJczxBloodReportInfo kty = new OpJczxBloodReportInfo();
                            // 复制字段
                            kty.setAYpxj(itme.getAYpxj());
                            kty.setATestResult(itme.getATestResult());
                            kty.setOYpxj(itme.getOYpxj());
                            kty.setOTestResult(itme.getOTestResult());
                            kty.setGh(itme.getGh());
                            kty.setSequence(itme.getSequence());
                            kty.setSampleName(itme.getSampleName());
                            kty.setSampleNo(itme.getSampleNo());
                            return kty;
                        })
                        .collect(Collectors.toList());
                reportBase.setAIsShow(AisShow);
                reportBase.setOIsShow(OisShow);
                reportBase.setTestType(String.valueOf(testType));
                reportBase.setTestEvaluation(String.valueOf(testEvaluation));
            } else if (itemType.equals("9")) {//早孕
                zaoyunList = sampleList.stream()
                        .map(itme -> {
                            OpJczxBloodReportInfo zaoyun = new OpJczxBloodReportInfo();
                            // 复制字段
                            zaoyun.setOd(itme.getOd());
                            zaoyun.setSequence(itme.getSequence());
                            zaoyun.setGh(itme.getGh());
                            zaoyun.setTestRemark(itme.getTestRemark());
                            zaoyun.setZaoyunTestResult(itme.getZaoyunTestResult());
                            zaoyun.setSampleName(itme.getSampleName());
                            zaoyun.setSampleNo(itme.getSampleNo());
                            return zaoyun;
                        })
                        .collect(Collectors.toList());

            } else if (itemType.equals("8")) {//生化
                shList = sampleList.stream()
                        .map(itme -> {
                            OpJczxBloodReportInfo sh = new OpJczxBloodReportInfo();
                            // 复制字段
                            BeanUtils.copyProperties(itme, sh);
                            return sh;
                        })
                        .collect(Collectors.toList());

            }else if ( itemType.equals("3") || itemType.equals("4")) {//副结核,BVDV抗原


                if(itemType.equals("3")){
                    int sum = yangList.size() + yinList.size();
                    int yangCount = yangList.size();
                    BigDecimal yangRate = BigDecimal.ZERO;
                    if (sum != 0) {
                        BigDecimal total = new BigDecimal(sum);
                        yangRate = new BigDecimal(yangCount)
                                .divide(total, 2, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100)); // 转换为百分比
                    }
                    reportBase.setTestEvaluation("副结核抗体：检测" + sum + "份，阳性" + yangList.size() + "份，阳性率" + yangRate + "%");
                    reportBase.setTestType("副结核抗体检测");

                    //根据阳性，阴性组装
                    for (OpBloodEntrustOrderSample opBloodEntrustOrderSample : sampleList) {
                        String pdjg = opBloodEntrustOrderSample.getPdjg();
                        //使用字典转换
                        pdjg = DictUtils.getDictValue("yinyang", pdjg);
                        OpJczxBloodReportInfo vo = new OpJczxBloodReportInfo();
                        vo.setSampleName(opBloodEntrustOrderSample.getSampleName());
                        String sampleType =opBloodEntrustOrderSample.getSampleType();
                        sampleType = DictUtils.getDictLabel("cattle_type", sampleType);
                        vo.setSampleType(sampleType);
                        vo.setSampleNo(opBloodEntrustOrderSample.getSampleNo());
                        vo.setGh(opBloodEntrustOrderSample.getGh());
                        vo.setSp(opBloodEntrustOrderSample.getSp());
                        vo.setSequence(opBloodEntrustOrderSample.getSequence());
                        vo.setPdjg(opBloodEntrustOrderSample.getPdjg());
                        fjhList.add(vo);
                        if (!StringUtils.isEmpty(pdjg)) {
                            if (pdjg.equals("1")) {
                                yangList.add(vo);
                            } else if (pdjg.equals("0")) {
                                yinList.add(vo);
                            }
                        }
                    }

                }else if(itemType.equals("4")){
                    // 1. 根据 bw (部位) 动态分组
                    // 过滤掉 bw 为空的如果不统计，或者用 orElse("未知")
                    Map<String, List<OpBloodEntrustOrderSample>> groupedMap = sampleList.stream()
                            .filter(s -> StringUtils.isNotEmpty(s.getBw()))
                            .collect(Collectors.groupingBy(OpBloodEntrustOrderSample::getBw));
                    for (OpBloodEntrustOrderSample opBloodEntrustOrderSample : sampleList) {
                        String pdjg = opBloodEntrustOrderSample.getPdjg();
                        //使用字典转换
                        pdjg = DictUtils.getDictValue("yinyang", pdjg);
                        OpJczxBloodReportInfo vo = new OpJczxBloodReportInfo();
                        vo.setSampleName(opBloodEntrustOrderSample.getSampleName());
                        String sampleType =opBloodEntrustOrderSample.getSampleType();
                        sampleType = DictUtils.getDictLabel("cattle_type", sampleType);
                        vo.setSampleType(sampleType);
                        vo.setSampleNo(opBloodEntrustOrderSample.getSampleNo());
                        vo.setGh(opBloodEntrustOrderSample.getGh());
                        vo.setSequence(opBloodEntrustOrderSample.getSequence());
                        vo.setDnh(opBloodEntrustOrderSample.getDnh());
                        vo.setMnh(opBloodEntrustOrderSample.getMnh());
                        vo.setBw(opBloodEntrustOrderSample.getBw());
                        vo.setTestRemark(opBloodEntrustOrderSample.getTestRemark());
                        vo.setSn(opBloodEntrustOrderSample.getSn());
                        vo.setPdjg(opBloodEntrustOrderSample.getPdjg());
                        bvdvkyList.add(vo);
                    }
                    StringBuilder sb = new StringBuilder("BVDV抗原：");
                    boolean isFirst = true;

                    bvdvkySampleName = groupedMap.keySet().stream()
                            .collect(Collectors.joining(","));
                    bvdvkySampleState = groupedMap.keySet().stream()
                            .collect(Collectors.joining("/"));
                    // 2. 遍历分组进行统计
                    for (Map.Entry<String, List<OpBloodEntrustOrderSample>> entry : groupedMap.entrySet()) {
                        String bwName = entry.getKey(); // 获取部位名称 (如: 耳组织, 血清, 全血等)
                        List<OpBloodEntrustOrderSample> groupSamples = entry.getValue();

                        int totalCount = groupSamples.size();
                        int yangCount = 0;

                        // 统计该部位下的阳性数
                        for (OpBloodEntrustOrderSample sampleBw : groupSamples) {
                            String pdjg = sampleBw.getPdjg();
                            // 使用字典转换判断是否为阳性 ("1"为阳性)
                            String resultVal = DictUtils.getDictValue("yinyang", pdjg);
                            if ("1".equals(resultVal)) {
                                yangCount++;
                            }
                        }

                        // 计算阳性率 (保留1位小数)
                        BigDecimal rate = BigDecimal.ZERO;
                        if (totalCount > 0 && yangCount > 0) {
                            rate = new BigDecimal(yangCount)
                                    .divide(new BigDecimal(totalCount), 3, BigDecimal.ROUND_HALF_UP)
                                    .multiply(new BigDecimal(100))
                                    .setScale(1, BigDecimal.ROUND_HALF_UP);
                        }
                        // 格式化阳性率：如果是0则显示"0"，否则显示如"6.1"
                        String rateStr = (rate.compareTo(BigDecimal.ZERO) == 0) ? "0" : rate.toString();

                        // 3. 组装文案
                        // 如果不是第一组，前面加换行符
                        if (!isFirst) {
                            sb.append("\n");
                        }
                        sb.append(bwName).append("检测").append(totalCount).append("份，阳性")
                                .append(yangCount).append("份，阳性率").append(rateStr).append("%。");

                        isFirst = false;
                    }

                    reportBase.setTestEvaluation(sb.toString());
                    reportBase.setTestType(" BVDV抗原检测 ");
                }


            }
            // 处理检测时间
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String testTime = calculateAndFormatTimeRange(sampleList, format);
            reportBase.setTestTime(testTime);
            reportBase.setRemark(sampleList.get(0).getTestRemark());
        } else {
            String opJczxBloodReportBaseId = opJczxBloodReportBase.getOpJczxBloodReportBaseId();
            if (ObjectUtils.isEmpty(opJczxBloodReportBaseId)) {
                throw new RuntimeException("缺少参数，请联系管理员");
            }
            List<OpJczxBloodReportInfo> opJczxBloodReportInfos = new ArrayList<>();
            reportBase = opJczxBloodReportBaseMapper.selectOpJczxBloodReportBaseByOpJczxBloodReportBaseId(opJczxBloodReportBaseId);
            OpJczxBloodReportInfo info = new OpJczxBloodReportInfo();
            info.setBaseId(opJczxBloodReportBaseId);
            //查询报告子表
            opJczxBloodReportInfos = opJczxBloodReportInfoMapper.selectOpJczxBloodReportInfoList(info);
            if (itemType.equals("2") || itemType.equals("6") ) {//结核（牛分枝杆菌PPD）,结核抗体
                for (OpJczxBloodReportInfo opJczxBloodReportInfo : opJczxBloodReportInfos) {
                    //子表结果组装
                    String testResult = opJczxBloodReportInfo.getTestResult();
                    OpJczxBloodReportInfo vo = new OpJczxBloodReportInfo();
                    vo.setSampleName(opJczxBloodReportInfo.getSampleName());
                    vo.setSampleNo(opJczxBloodReportInfo.getSampleNo());
                    vo.setSp(opJczxBloodReportInfo.getSp());
                    vo.setGh(opJczxBloodReportInfo.getGh());
                    vo.setSequence(opJczxBloodReportInfo.getSequence());
                    vo.setDnh(opJczxBloodReportInfo.getDnh());
                    vo.setBw(opJczxBloodReportInfo.getBw());
                    vo.setSn(opJczxBloodReportInfo.getSn());
                    vo.setPdjg(opJczxBloodReportInfo.getPdjg());
                    vo.setSampleType(opJczxBloodReportInfo.getSampleType());
                    if (testResult.equals("1")) {
                        vo.setTestResult("1");
                        yangList.add(vo);
                    } else if (testResult.equals("0")) {
                        vo.setTestResult("0");
                        yinList.add(vo);
                    }
                }
            } else if (itemType.equals("0")) {//口蹄疫
                ktyList = opJczxBloodReportInfos;
            } else if (itemType.equals("9")) {//早孕
                zaoyunList = opJczxBloodReportInfos;
            } else if (itemType.equals("8")) {//生化
                shList = opJczxBloodReportInfos;
            }else if (itemType.equals("3")) {//副结核
                fjhList = opJczxBloodReportInfos;
            }else if (itemType.equals("4")) {//BVDV抗原
                bvdvkyList = opJczxBloodReportInfos;
            }

        }

        //装填报告内容

        //     ,         检测方法及依据,   检测结果与评价         判定标准               标准对照
        String testBasis = null, testCriterion = null, testReferStandard = null;
        String sampleBatchNo = null;


        if (itemType.equals("2")) {//结核（牛分枝杆菌PPD）

            opBloodJHReportSampleVo.setYangList(yangList);
            opBloodJHReportSampleVo.setYinList(yinList);
            testBasis = "GB/T18645--2020 动物结核病诊断技术";
            testCriterion = "牛分枝杆菌PPD皮内变态反应试验：局部有明显的炎性反应，皮厚差大于或等于4.0mm判为阳性反应；局部炎性反应不明显，" +
                    "皮厚差大于或等于2.0mm，小于4.0mm判为疑似反应；无炎性反应，皮厚差在2.0mm以下，判为阴性。";
            testReferStandard = "以上实验标准对照均成立";
            sampleBatchNo = "哈药集团生物疫苗有限公司 202401";
            reportBase.setSampleName("奶牛");
            reportBase.setAnimalType("牛");
        } else if (itemType.equals("6")) {//结核抗体

            opBloodJHReportSampleVo.setYangList(yangList);
            opBloodJHReportSampleVo.setYinList(yinList);
            reportBase.setSampleState("血清清亮");
            testBasis = "牛分支杆菌抗体检测技术规范";
            reportBase.setInsturment("FC酶标仪");
            testCriterion = "牛结核病抗体ELISA检测：S/P=（样品OD值-阴性对照的平均OD值）/（阳性对照的平均OD值-阴性对照的平均OD值); " +
                    "标准阴性血清的OD值必须在小于0.150。标准阳性血清的OD值必须在1.500以上。判定：S/P值≥0.5判为结核抗体阳性，用+表示；S/P值<0.5判为结核抗体阴性，用-表示。";
            testReferStandard = "以上实验标准对照均成立";
            sampleBatchNo = "牛结核病抗体ELISA检测：安世佳合（4304P056）";
            reportBase.setSampleName("奶牛");
            reportBase.setAnimalType("牛");
        } else if (itemType.equals("0")) {//口蹄疫
            opBloodJHReportSampleVo.setKtyList(ktyList);
            testBasis = "GB/T 18935-2018 口蹄疫诊断技术";
            reportBase.setInsturment("FC酶标仪");
            testCriterion = "A型口蹄疫抗体：效价≥1:128判为阳性，用+表示；效价1:64-1:128判为可疑，用+/-表示；效价<1:64判为阴性，用-表示；" +
                    " $$O型口蹄疫抗体：效价≥1:128判为阳性，用+表示；效价1:64-1:128判为可疑，用+/-表示；效价<1:64判为阴性，用-表示";
            testReferStandard = "以上实验标准对照均成立";
            reportBase.setSampleState("血清清亮");
            reportBase.setSampleName("血清");
            reportBase.setAnimalType("牛");

        }else if (itemType.equals("3")) {//副结核
            opBloodJHReportSampleVo.setFjhList(fjhList);
            testBasis = "样品内的抗体能与板上包被抗原特异性结合形成抗原抗体复合物，并与后面加入的酶标二抗结合，二抗上的酶再催化底物显色，显色深度与样品中的抗体含量成正相关。";
            reportBase.setInsturment("FC酶标仪");
            testCriterion = "样品S/P值=（样品OD值-阴性性对照OD值）/（阳性对照OD值-阴性对照OD值）*100\n" +
                    "有效性：阴性对照的OD值必须小于0.2，阳性对照OD值的平均值必须≥0.75.\n" +
                    "血清（单个）：\n" +
                    "阴性：样品的S/P值小于100%，样品血清中抗体阴性\n" +
                    "阳性：样品的s/p值大于或等于100%，样品血清中抗体阳性\n";
            testReferStandard = "以上实验标准对照均成立";
            reportBase.setSampleState("血清清亮");
            reportBase.setSampleName("血清");
            reportBase.setAnimalType("牛");

        }
        else if (itemType.equals("4")) {//BVDV抗原
            opBloodJHReportSampleVo.setBvdvkyList(bvdvkyList);
            testBasis = "试剂盒采用双抗体夹心ELISA法：微孔板包被BVDV结构蛋白Erns的特异性单抗。样品与酶标结合物（HRP标记的BVDV抗体）同时加入ELISA板中，若样品中含BVDV抗原，则抗原将会被两个抗体捕获。孵育，清洗后，加入底物TMB，根据显色结果进行判定。";
            reportBase.setInsturment("FC酶标仪");
            testCriterion = "结果有效性：阳性对照OD值>1，且阴性对照OD值<0.25。结果判定：S-N值=样品OD值-阴性对照OD值。血清：样品S-N≥0.25，样品呈BVDV抗原阳性，样品S-N值<0.25，样品呈BVDV抗原阴性；耳组织：样品S-N值≥0.4，样品呈BVDV抗原阳性；样品S-N值<0.4，样品呈BVDV抗原阴性。";
            testReferStandard = "以上实验标准对照均成立";
            reportBase.setSampleState(bvdvkySampleState);
            reportBase.setSampleName(bvdvkySampleName);
            reportBase.setAnimalType("牛");

        }
        if (itemType.equals("0")) {//口蹄疫用A型O型试剂批号
            if(CollectionUtil.isNotEmpty(sampleList)){
                // 1. 提取、过滤空值、去重、拼接
                String aSjph = sampleList.stream()
                        .map(OpBloodEntrustOrderSample::getASjph)       // 提取 sjph 字段
                        .filter(s -> !StringUtils.isEmpty(s))          // 过滤掉空字符串和 null
                        .distinct()                                    // 去重关键步骤
                        .collect(Collectors.joining(","));             // 用逗号拼接
                aSjph = "A型试剂批号:"+aSjph;

                String oSjph = sampleList.stream()
                        .map(OpBloodEntrustOrderSample::getOSjph)       // 提取 sjph 字段
                        .filter(s -> !StringUtils.isEmpty(s))          // 过滤掉空字符串和 null
                        .distinct()                                    // 去重关键步骤
                        .collect(Collectors.joining(","));             // 用逗号拼接
                oSjph = "$$O型试剂批号:"+oSjph;
                String sjph = aSjph + oSjph;
                // 2. 赋值
                if (!StringUtils.isEmpty(sjph)) {
                    reportBase.setSjph(sjph);
                }
            }else {
                //sjph 取base的sjph
            }

        }else {
            if(CollectionUtil.isNotEmpty(sampleList)){
                // 1. 提取、过滤空值、去重、拼接
                String sjph = sampleList.stream()
                        .map(OpBloodEntrustOrderSample::getSjph)       // 提取 sjph 字段
                        .filter(s -> !StringUtils.isEmpty(s))          // 过滤掉空字符串和 null
                        .distinct()                                    // 去重关键步骤
                        .collect(Collectors.joining(","));             // 用逗号拼接

                // 2. 赋值
                if (!StringUtils.isEmpty(sjph)) {
                    reportBase.setSjph(sjph);
                }
            }else {
                //sjph 取base的sjph
            }

        }

        reportBase.setTestBasis(testBasis);
        reportBase.setTestCriterion(testCriterion);
        reportBase.setTestReferStandard(testReferStandard);
        reportBase.setSampleBatchNo(sampleBatchNo);

        // 获取当前日期
        LocalDate today = LocalDate.now();
        Date d = java.sql.Date.valueOf(today);
        reportBase.setReportDate(d);
        reportBase.setOpBloodJHReportSampleVo(opBloodJHReportSampleVo);

        //早孕
        opBloodJHReportSampleVo.setZaoyunList(zaoyunList);
        //生化
        opBloodJHReportSampleVo.setShList(shList);
        //查询签名
        String editUserId = reportBase.getEditUserId();
        String checkUserId = reportBase.getCheckUserId();
        String approveUserId = reportBase.getApproveUserId();
        if (!ObjectUtils.isEmpty(editUserId)) {
            String sign = sysUserMapper.selectSignByUserId(editUserId);
            reportBase.setEditSign(sign);
        }
        if (!ObjectUtils.isEmpty(checkUserId)) {
            String sign = sysUserMapper.selectSignByUserId(checkUserId);
            reportBase.setCheckSign(sign);
        }
        if (!ObjectUtils.isEmpty(approveUserId)) {
            String sign = sysUserMapper.selectSignByUserId(approveUserId);
            reportBase.setApproveSign(sign);
        }
        return reportBase;
    }

    /**
     * 查询检测中心血样报告主列表
     *
     * @param opJczxBloodReportBase 检测中心血样报告主
     * @return 检测中心血样报告主
     */
    @Override
    public List<OpJczxBloodReportBase> selectOpJczxBloodReportBaseList(OpJczxBloodReportBase opJczxBloodReportBase) {
        String status = opJczxBloodReportBase.getStatus();
        if (StringUtils.isEmpty(status)) {
            throw new RuntimeException("缺失参数，请联系管理员");
        }
        List<OpJczxBloodReportBase> opJczxBloodReportBaseList = new ArrayList<>();
        if (status.equals((JczxFeedReportStatusEnum.DZZ.getCode()))) {
            opJczxBloodReportBaseList = opJczxBloodReportBaseMapper.selectOpJczxBloodReportBaseListStatus0(opJczxBloodReportBase);
        } else {
            opJczxBloodReportBaseList = opJczxBloodReportBaseMapper.selectOpJczxBloodReportBaseList(opJczxBloodReportBase);
        }
        return opJczxBloodReportBaseList;
    }

    /**
     * 新增检测中心血样报告主
     *
     * @param opJczxBloodReportBase 检测中心血样报告主
     * @return 结果
     */
    @Override
    @Transactional
    public String insertOpJczxBloodReportBase(OpJczxBloodReportBase opJczxBloodReportBase) {
//        if (StringUtils.isEmpty(opJczxBloodReportBase.getOpJczxBloodReportBaseId())) {
//            opJczxBloodReportBase.setOpJczxBloodReportBaseId(IdUtils.simpleUUID());
//        }

        String status = opJczxBloodReportBase.getStatus();
        if (ObjectUtils.isEmpty(status)) {
            throw new RuntimeException("缺失参数，请联系管理员");
        }
        Long userId = SecurityUtils.getUserId();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        String username = sysUser.getNickName();
        String baseId = opJczxBloodReportBase.getOpJczxBloodReportBaseId();

        //提交
        if (status.equals((JczxFeedReportStatusEnum.DSH.getCode()))) {

            opJczxBloodReportBase.setEditUserId(String.valueOf(userId));
            opJczxBloodReportBase.setEditTime(new Date());
//            String sign = sysUserMapper.selectSignByUserId(String.valueOf(userId));
            opJczxBloodReportBase.setEditUser(username);
        }
        //首次保存
        if (ObjectUtils.isEmpty(baseId)) {
            // 自动填充创建/更新信息
            opJczxBloodReportBase.fillCreateInfo();
            baseId = IdUtils.simpleUUID();
            opJczxBloodReportBase.setReportNo(codeGeneratorUtil.generateBLOODReportNo());
            opJczxBloodReportBase.setOpJczxBloodReportBaseId(baseId);
            opJczxBloodReportBaseMapper.insertOpJczxBloodReportBase(opJczxBloodReportBase);
        } else {
            opJczxBloodReportBase.fillUpdateInfo();
            opJczxBloodReportBaseMapper.updateOpJczxBloodReportBase(opJczxBloodReportBase);
            //子表，先删除，再插入
            opJczxBloodReportInfoMapper.deleteByBaseId(String.valueOf(userId), baseId);
        }
        OpBloodJHReportSampleVo opBloodJHReportSampleVo = opJczxBloodReportBase.getOpBloodJHReportSampleVo();
        List<OpJczxBloodReportInfo> yangList = opBloodJHReportSampleVo.getYangList();
        List<OpJczxBloodReportInfo> yinList = opBloodJHReportSampleVo.getYinList();
        List<OpJczxBloodReportInfo> ktyList = opBloodJHReportSampleVo.getKtyList();
        List<OpJczxBloodReportInfo> zaoyunList = opBloodJHReportSampleVo.getZaoyunList();
        List<OpJczxBloodReportInfo> bvdvkyList = opBloodJHReportSampleVo.getBvdvkyList();
        List<OpJczxBloodReportInfo> fjhList = opBloodJHReportSampleVo.getFjhList();
        List<OpJczxBloodReportInfo> shList = opBloodJHReportSampleVo.getShList();
        if (!ObjectUtils.isEmpty(yangList)) {
            for (OpJczxBloodReportInfo yang : yangList) {
                OpJczxBloodReportInfo info = new OpJczxBloodReportInfo();


                BeanUtils.copyProperties(yang, info);
                info.setBaseId(baseId);
                info.setOpJczxBloodReportInfoId(IdUtils.simpleUUID());
                info.fillCreateInfo();
                opJczxBloodReportInfoMapper.insertOpJczxBloodReportInfo(info);
            }
        }
        if (!ObjectUtils.isEmpty(yinList)) {
            for (OpJczxBloodReportInfo yin : yinList) {
                OpJczxBloodReportInfo info = new OpJczxBloodReportInfo();
                BeanUtils.copyProperties(yin, info);
                info.fillCreateInfo();
                info.setBaseId(baseId);
                info.setOpJczxBloodReportInfoId(IdUtils.simpleUUID());
                opJczxBloodReportInfoMapper.insertOpJczxBloodReportInfo(info);
            }
        }
        if (!ObjectUtils.isEmpty(ktyList)) {
            for (OpJczxBloodReportInfo kty : ktyList) {
                OpJczxBloodReportInfo info = new OpJczxBloodReportInfo();
                BeanUtils.copyProperties(kty, info);
                info.fillCreateInfo();
                info.setBaseId(baseId);
                info.setOpJczxBloodReportInfoId(IdUtils.simpleUUID());
                opJczxBloodReportInfoMapper.insertOpJczxBloodReportInfo(info);
            }
        }
        if (!ObjectUtils.isEmpty(zaoyunList)) {
            for (OpJczxBloodReportInfo zaoyun : zaoyunList) {
                OpJczxBloodReportInfo info = new OpJczxBloodReportInfo();
                BeanUtils.copyProperties(zaoyun, info);
                info.fillCreateInfo();
                info.setBaseId(baseId);
                info.setOpJczxBloodReportInfoId(IdUtils.simpleUUID());
                opJczxBloodReportInfoMapper.insertOpJczxBloodReportInfo(info);
            }
        }
        if (!ObjectUtils.isEmpty(shList)) {
            for (OpJczxBloodReportInfo sh : shList) {
                OpJczxBloodReportInfo info = new OpJczxBloodReportInfo();
                BeanUtils.copyProperties(sh, info);
                info.fillCreateInfo();
                info.setBaseId(baseId);
                info.setOpJczxBloodReportInfoId(IdUtils.simpleUUID());
                opJczxBloodReportInfoMapper.insertOpJczxBloodReportInfo(info);
            }
        }
        if (!ObjectUtils.isEmpty(fjhList)) {
            for (OpJczxBloodReportInfo sh : fjhList) {
                OpJczxBloodReportInfo info = new OpJczxBloodReportInfo();
                BeanUtils.copyProperties(sh, info);
                info.fillCreateInfo();
                info.setBaseId(baseId);
                info.setOpJczxBloodReportInfoId(IdUtils.simpleUUID());
                opJczxBloodReportInfoMapper.insertOpJczxBloodReportInfo(info);
            }
        }
        if (!ObjectUtils.isEmpty(bvdvkyList)) {
            for (OpJczxBloodReportInfo sh : bvdvkyList) {
                OpJczxBloodReportInfo info = new OpJczxBloodReportInfo();
                BeanUtils.copyProperties(sh, info);
                info.fillCreateInfo();
                info.setBaseId(baseId);
                info.setOpJczxBloodReportInfoId(IdUtils.simpleUUID());
                opJczxBloodReportInfoMapper.insertOpJczxBloodReportInfo(info);
            }
        }
        return baseId;
    }

    /**
     * 修改检测中心血样报告主
     *
     * @param opJczxBloodReportBase 检测中心血样报告主
     * @return 结果
     */
    @Override
    public int updateOpJczxBloodReportBase(OpJczxBloodReportBase opJczxBloodReportBase) {
        // 自动填充更新信息
        opJczxBloodReportBase.fillUpdateInfo();
        return opJczxBloodReportBaseMapper.updateOpJczxBloodReportBase(opJczxBloodReportBase);
    }

    /**
     * 批量删除检测中心血样报告主
     *
     * @param opJczxBloodReportBaseIds 需要删除的检测中心血样报告主主键
     * @return 结果
     */
    @Override
    public int deleteOpJczxBloodReportBaseByOpJczxBloodReportBaseIds(String[] opJczxBloodReportBaseIds) {
        return opJczxBloodReportBaseMapper.deleteOpJczxBloodReportBaseByOpJczxBloodReportBaseIds(opJczxBloodReportBaseIds);
    }

    /**
     * 删除检测中心血样报告主信息
     *
     * @param opJczxBloodReportBaseId 检测中心血样报告主主键
     * @return 结果
     */
    @Override
    public int deleteOpJczxBloodReportBaseByOpJczxBloodReportBaseId(String opJczxBloodReportBaseId) {
        return opJczxBloodReportBaseMapper.deleteOpJczxBloodReportBaseByOpJczxBloodReportBaseId(opJczxBloodReportBaseId);
    }

    @Override
    public int verifyOpJczxBloodReportBase(OpJczxBloodReportBase opJczxBloodReportBase) {
        String status = opJczxBloodReportBase.getStatus();
        if (ObjectUtils.isEmpty(status)) {
            throw new RuntimeException("缺失参数，请联系管理员");
        }
        if (status.equals(JczxFeedReportStatusEnum.YSH.getCode())) {
            Long userId = SecurityUtils.getUserId();
            SysUser sysUser = sysUserMapper.selectUserById(userId);
            String username = sysUser.getNickName();
            opJczxBloodReportBase.setCheckTime(DateUtils.getNowDate());
            opJczxBloodReportBase.setCheckUserId(String.valueOf(userId));
//            String sign = sysUserMapper.selectSignByUserId(String.valueOf(userId));
            opJczxBloodReportBase.setCheckUser(username);
        }
        opJczxBloodReportBase.fillUpdateInfo();
        //如果退回到制作中。把result表已审核改为待审核
        if(status.equals(JczxFeedReportStatusEnum.ZZZ.getCode())) {
            String updateUserId = String.valueOf(SecurityUtils.getUserId());
            Date updateTime = new Date();
            List<OpJczxBloodReportInfo> infoList =opJczxBloodReportInfoMapper.selectOpJczxBloodReportInfoListByBaseId(opJczxBloodReportBase.getOpJczxBloodReportBaseId());
            List<String> sampleNoList = infoList.stream().map(OpJczxBloodReportInfo::getSampleNo).collect(Collectors.toList());
            List<OpBloodEntrustOrderSample> examineSampleList = opBloodEntrustOrderSampleMapper.selectExamineBySampleNoList(sampleNoList);
            List<String> sampleIdList = examineSampleList.stream().map(OpBloodEntrustOrderSample::getOpBloodEntrustOrderSampleId).collect(Collectors.toList());
            opBloodEntrustOrderSampleMapper.batchClearExamineFields(sampleIdList, updateUserId, updateTime);
        }


        return opJczxBloodReportBaseMapper.updateOpJczxBloodReportBase(opJczxBloodReportBase);
    }

    @Override
    public int commitOpJczxBloodReportBase(OpJczxBloodReportBase opJczxBloodReportBase) {
        String status = opJczxBloodReportBase.getStatus();
        if (ObjectUtils.isEmpty(status)) {
            throw new RuntimeException("缺失参数，请联系管理员");
        }
        String orderId = opJczxBloodReportBase.getBloodEntrustOrderId();
        if (ObjectUtils.isEmpty(orderId)) {
            throw new RuntimeException("缺少必要参数，请联系管理员");
        }
        if (status.equals(JczxFeedReportStatusEnum.YPZ.getCode())) {

            Long userId = SecurityUtils.getUserId();
            SysUser sysUser = sysUserMapper.selectUserById(userId);
            String username = sysUser.getNickName();
            opJczxBloodReportBase.setApproveTime(DateUtils.getNowDate());
            opJczxBloodReportBase.setApproveUserId(String.valueOf(userId));
//            String sign = sysUserMapper.selectSignByUserId(String.valueOf(userId));
            opJczxBloodReportBase.setApproveUser(username);


            OpBloodEntrustOrder opBloodEntrustOrder = new OpBloodEntrustOrder();
            opBloodEntrustOrder.setOpBloodEntrustOrderId(orderId);
            opBloodEntrustOrder.setReportId(opJczxBloodReportBase.getOpJczxBloodReportBaseId());
            opBloodEntrustOrderMapper.updateOpBloodEntrustOrder(opBloodEntrustOrder);
        }
        opJczxBloodReportBase.fillUpdateInfo();
        //如果退回到制作中。把result表已审核改为待审核
        if(status.equals(JczxFeedReportStatusEnum.ZZZ.getCode())) {
            String updateUserId = String.valueOf(SecurityUtils.getUserId());
            Date updateTime = new Date();
            List<OpJczxBloodReportInfo> infoList =opJczxBloodReportInfoMapper.selectOpJczxBloodReportInfoListByBaseId(opJczxBloodReportBase.getOpJczxBloodReportBaseId());
            List<String> sampleNoList = infoList.stream().map(OpJczxBloodReportInfo::getSampleNo).collect(Collectors.toList());
            List<OpBloodEntrustOrderSample> examineSampleList = opBloodEntrustOrderSampleMapper.selectExamineBySampleNoList(sampleNoList);
            List<String> sampleIdList = examineSampleList.stream().map(OpBloodEntrustOrderSample::getOpBloodEntrustOrderSampleId).collect(Collectors.toList());
            opBloodEntrustOrderSampleMapper.batchClearExamineFields(sampleIdList, updateUserId, updateTime);
        }
        return opJczxBloodReportBaseMapper.updateOpJczxBloodReportBase(opJczxBloodReportBase);

    }

    // [!code focus:start]
    @Override
    public void downloadZaoyunReportExcel(HttpServletResponse response, OpJczxBloodReportBase opJczxBloodReportBase) throws Exception {
        //根据委托单查询所有结果
        List<OpBloodEntrustOrderSample> sampleList = opBloodEntrustOrderSampleMapper.
                selectAllByOpBloodEntrustOrderId(opJczxBloodReportBase.getBloodEntrustOrderId());

        // 5. 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = StringUtils.format("早孕检测报告_{}.xlsx", opJczxBloodReportBase.getEntrustDeptName());
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);


        // 6. 读取模板并填充数据
        fillZaoyunExcelTemplate(response, opJczxBloodReportBase, sampleList);

    }

    @Override
    public void downloadBiochemistryReportExcel(HttpServletResponse response, OpJczxBloodReportBase opJczxBloodReportBase) throws Exception {
        //根据委托单查询所有结果
        List<OpBloodEntrustOrderSample> sampleList = opBloodEntrustOrderSampleMapper.
                selectAllByOpBloodEntrustOrderId(opJczxBloodReportBase.getBloodEntrustOrderId());

        // 5. 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = StringUtils.format("生化检测报告_{}.xlsx", opJczxBloodReportBase.getEntrustDeptName());
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);


        // 6. 读取模板并填充数据
        fillBiochemistryExcelTemplate(response, opJczxBloodReportBase, sampleList);

    }
    // [!code focus:end]


    @Override
    public List<ReportEmailVo> selectOpJczxBloodReportBaseEmailList(String bloodEntrustOrderId) {
        // 获取当前登录人的部门
        Long deptId = SecurityUtils.getDeptId();
        if (Objects.isNull(deptId)) {
            throw new ServiceException("部门为空");
        }

        // 获取检测中心通讯方式
        List<ReportEmailVo> jczxEmailList = new LinkedList<>();

        // 查询委托单下样品
        List<OpBloodEntrustOrderSample> sampleList = opBloodEntrustOrderSampleMapper.
                selectAllByOpBloodEntrustOrderId(bloodEntrustOrderId);

        // 是否有生化样品
        boolean hasBiochemistrySample = sampleList.stream()
                .anyMatch(sample -> "8".equals(sample.getBloodTaskItemType()));
        // 是否有早孕样品
        boolean hasZy = sampleList.stream()
                .anyMatch(sample -> "9".equals(sample.getBloodTaskItemType()));
        // 是否有疫病样品
        boolean hasYb = sampleList.stream()
                .anyMatch(sample -> !"9".equals(sample.getBloodTaskItemType()) && !"8".equals(sample.getBloodTaskItemType()));

        // 获取检测中心对应类型的邮箱
        if (hasBiochemistrySample) {
            List<ReportEmailVo> shEmailByDeptId = bsContactMapper.selectShEmailByDeptId(deptId);
            jczxEmailList.addAll(shEmailByDeptId);
        }
        if (hasZy) {
            List<ReportEmailVo> zyEmailByDeptIdForZy = bsContactMapper.selectZyEmailByDeptId(deptId);
            jczxEmailList.addAll(zyEmailByDeptIdForZy);
        }
        if (hasYb) {
            List<ReportEmailVo> ybEmailByDeptIdForYb = bsContactMapper.selectYbEmailByDeptId(deptId);
            jczxEmailList.addAll(ybEmailByDeptIdForYb);
        }

        // 获取委托部门通讯方式表邮箱列表
        List<ReportEmailVo> toEmailByContact = new LinkedList<>();
        // 获取委托部门ID
        OpBloodEntrustOrder opBloodEntrustOrder = opBloodEntrustOrderMapper.selectOpBloodEntrustOrderByOpBloodEntrustOrderId(bloodEntrustOrderId);
        Long entrustDeptId = opBloodEntrustOrder.getEntrustDeptId();

        // 获取委托部门对应类型的邮箱
        if (hasBiochemistrySample) {
            List<ReportEmailVo> shEmailByDeptId = bsContactMapper.selectShEmailByDeptId(entrustDeptId);
            toEmailByContact.addAll(shEmailByDeptId);
        }
        if (hasZy) {
            List<ReportEmailVo> zyEmailByDeptIdForZy = bsContactMapper.selectZyEmailByDeptId(entrustDeptId);
            toEmailByContact.addAll(zyEmailByDeptIdForZy);
        }
        if (hasYb) {
            List<ReportEmailVo> ybEmailByDeptIdForYb = bsContactMapper.selectYbEmailByDeptId(entrustDeptId);
            toEmailByContact.addAll(ybEmailByDeptIdForYb);
        }

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
    public int sendOpJczxBloodReport(List<BloodEmailDTO> emailDTOList) throws IOException {
        if (emailDTOList.isEmpty()) {
            throw new ServiceException("请选择要发送的报告");
        }
        for (BloodEmailDTO emailDTO : emailDTOList) {
            //获取委托单号id
            String bloodEntrustOrderId = emailDTO.getBloodEntrustOrderId();
            if (StringUtils.isEmpty(bloodEntrustOrderId)) {
                throw new ServiceException("缺少必要参数");
            }
            List<OpJczxBloodReportBase> baseList = opJczxBloodReportBaseMapper.selectReportBaseByOrderId(bloodEntrustOrderId);
            logger.info("Fetched {} report bases for order ID {}", baseList.size(), bloodEntrustOrderId);
            if (ObjectUtils.isEmpty(baseList)) {
                throw new ServiceException("获取报告数据失败");
            }
            //遍历报告，发送
            for (OpJczxBloodReportBase opJczxBloodReportBase : baseList) {

                if (StringUtils.isEmpty(opJczxBloodReportBase.getPdfFileInfo())) {
                    throw new ServiceException(opJczxBloodReportBase.getReportNo() + " 报告文件不存在");
                }

                // 查询文件
                MultipartFile file = FileConvertUtils.getMultipartFileByAbsolutePath(profile, opJczxBloodReportBase.getPdfFileInfo());
                if (ObjectUtils.isEmpty(file)) {
                    throw new ServiceException("请上传文件");
                }

                MultipartFile[] files = {file};

                if (ObjectUtils.isEmpty(opJczxBloodReportBase)) {
                    throw new ServiceException("获取报告数据失败");
                }

                if (!"4".equals(opJczxBloodReportBase.getStatus()) && !"5".equals(opJczxBloodReportBase.getStatus())) {
                    throw new ServiceException("状态错误，无法发送");
                }

                if (StringUtils.isEmpty(bloodEntrustOrderId)) {
                    throw new ServiceException("获取样品表数据失败");
                }

                // 根据委托单主表id查询邮箱
                OpBloodEntrustOrder opBloodEntrustOrder = opBloodEntrustOrderMapper.selectOpBloodEntrustOrderByOpBloodEntrustOrderId(bloodEntrustOrderId);

                String bloodTaskItemType = opBloodEntrustOrder.getBloodTaskItemType();
                String[] sender = null;
                if("8".equals(bloodTaskItemType)){
                    //生化标题内容改为取前端输入
//                    emailDTO.setTitle("生化检测结果");
                    sender = configService.selectConfigByKey("email.sh").split(";");
//                    emailDTO.setContent("各位领导好：生化结果烦请见附件\n" +
//                            "--------------------------\n" +
//                            "\n" +
//                            "光明牧业有限公司 奶研所\n"
//                            +sender[0]+"\n" +
//                            "手机："+sender[3]+"\n" +
//                            "邮箱："+sender[1]+"\n" +
//                            "地址：上海市闵行区万源路2729号309室");
                }else if("9".equals(bloodTaskItemType)){
                    //早孕
                    emailDTO.setTitle("早孕检测结果");
                     sender = configService.selectConfigByKey("email.zy").split(";");
                    emailDTO.setContent("各位领导好：早孕结果烦请见附件\n" +
                            "--------------------------\n" +
                            "\n" +
                            "光明牧业有限公司 奶研所\n"
                            +sender[0]+"\n" +
                            "手机："+sender[3]+"\n" +
                            "邮箱："+sender[1]+"\n" +
                            "地址：上海市闵行区万源路2729号309室");
                }else {
                    sender = configService.selectConfigByKey("email.jb").split(";");
                    emailDTO.setContent("各位领导好：疫病结果烦请见附件\n" +
                            "--------------------------\n" +
                            "\n" +
                            "光明牧业有限公司 奶研所\n"
                            +sender[0]+"\n" +
                            "手机："+sender[3]+"\n" +
                            "邮箱："+sender[1]+"\n" +
                            "地址：上海市闵行区万源路2729号309室");
                }

                if (ObjectUtils.isEmpty(opBloodEntrustOrder)) {
                    throw new ServiceException("获取邮箱数据失败");
                }

                // 从委托单主表中获取邮箱
                String toEmail = opBloodEntrustOrder.getEntrustContactEmail();
                if (StringUtils.isEmpty(toEmail)) {
                    throw new ServiceException("获取邮箱失败");
                }

                // 获取当前登录人的部门
                Long deptId = SecurityUtils.getDeptId();
                if (Objects.isNull(deptId)) {
                    throw new ServiceException("部门为空");
                }

                //获取通讯方式表邮箱
                String toEmailUser = emailDTO.getEmails();
                Set<String> emailSet = new LinkedHashSet<>();

                // 统一处理逻辑
                String[] emailSources = {toEmail, toEmailUser};
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
                System.out.println("Final Emails: " + finalEmails); // 调试输出，查看最终的邮箱列表
                logger.info("Final Emails: {}", finalEmails); // 使用日志记录最终的邮箱列表


                // 获取邮箱和授权码
                // 获取邮箱
                String deptEmail = sender[1];
                if (StringUtils.isEmpty(deptEmail)) {
                    throw new ServiceException("获取部门邮箱失败");
                }

                // 获取授权码
                String emailAuthCode = sender[2];
                if (StringUtils.isEmpty(emailAuthCode)) {
                    throw new ServiceException("邮箱未授权，请联系管理员");
                }

                // 发送邮件
                String[] toEmails = finalEmails.split(",");

                try {
                    EmailUtils.sendEmail(deptEmail, emailAuthCode, toEmails,
                            emailDTO.getTitle(),
                            emailDTO.getContent(),
                            false,
                            files);
                } catch (MessagingException | IOException e) {
                    throw new ServiceException("发送邮件失败");
                }

                opJczxBloodReportBase.setStatus("5");
                opJczxBloodReportBaseMapper.updateOpJczxBloodReportBase(opJczxBloodReportBase);
            }
        }
        return 1;
    }


    private String calculateAndFormatTimeRange(List<OpBloodEntrustOrderSample> reports, DateTimeFormatter formatter) {
        List<LocalDateTime> times = reports.stream()
                .map(OpBloodEntrustOrderSample::getTestTime)
                .filter(Objects::nonNull)
                .map(date -> LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()))
                .collect(Collectors.toList());

        if (times.isEmpty()) {
            return ""; // 修改这里：未查询到有效检测时间时返回空字符串
        }

        LocalDateTime startTime = Collections.min(times);
        LocalDateTime endTime = Collections.max(times);

        if (startTime.equals(endTime)) {
            return startTime.format(formatter);
        } else {
            return String.format("%s-%s",
                    startTime.format(formatter),
                    endTime.format(formatter));
        }
    }

    /**
     * 填充生化检测Excel模板
     */
    private void fillBiochemistryExcelTemplate(HttpServletResponse response, OpJczxBloodReportBase opJczxBloodReportBase, List<OpBloodEntrustOrderSample> sampleList)
            throws Exception {
        // 日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 查询这些样品所选的生化项目 (这段代码你已经有了，保持不变)
        List<String> sampleIdList = sampleList.stream().map(OpBloodEntrustOrderSample::getOpBloodEntrustOrderSampleId).collect(Collectors.toList());
        List<String> biochemistryItemTypeList = opBloodEntrustOrderMapper.selectBiochemistryItemTypeBySampleIdList(sampleIdList);

        Set<String> biochemistryItemTypeSet = new TreeSet<>(Comparator.comparingInt(Integer::parseInt)); // 使用 TreeSet 并指定比较器按数字排序
        for (String s : biochemistryItemTypeList) {
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

        // 该委托单所选的生化项目及单位参考范围 (这段代码你已经有了，保持不变)
        List<BiochemistryItem> biochemistryItemList = new ArrayList<>();
        for (String itemType : biochemistryItemTypeSet) { // 使用排序后的 Set
            String name = DictUtils.getDictLabel("biochemistry_item_type", itemType);
            String unit = DictUtils.getDictLabel("biochemistry_item_unit", itemType);
            String range = DictUtils.getDictLabel("biochemistry_item_range", itemType);
            BiochemistryItem biochemistryItem = new BiochemistryItem();
            biochemistryItem.setRange(range);
            biochemistryItem.setName(name);
            biochemistryItem.setUnit(unit);
            biochemistryItem.setBiochemistryItemType(itemType); // 存储字典值，用于后续取值
            biochemistryItemList.add(biochemistryItem);
        }

        // 送检单位
        String entrustDeptName = opJczxBloodReportBase.getEntrustDeptName();

        // 动态创建Excel
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             OutputStream outputStream = response.getOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("生化检测报告");

            // --- 创建样式 ---
            // (复用 fillZaoyunExcelTemplate 中的样式创建代码，这里省略重复代码，假设样式已创建好)
            // 标题样式: titleStyle
            // 副标题样式: subTitleStyle
            // 标签样式: labelStyle
            // 数据样式 (左对齐): dataStyle
            // 表头样式 (居中加粗带边框): headerStyle
            // 列表数据样式 (居中带边框): listDataStyle

            // --- 复制样式创建代码 (从 fillZaoyunExcelTemplate 复制过来) ---
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setFontName("宋体");
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setBold(true);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle subTitleStyle = workbook.createCellStyle();
            Font subTitleFont = workbook.createFont();
            subTitleFont.setFontName("宋体");
            subTitleFont.setFontHeightInPoints((short) 14);
            subTitleFont.setBold(true);
            subTitleStyle.setFont(subTitleFont);
            subTitleStyle.setAlignment(HorizontalAlignment.CENTER);
            subTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle labelStyle = workbook.createCellStyle();
            Font labelFont = workbook.createFont();
            labelFont.setFontName("宋体");
            labelFont.setFontHeightInPoints((short) 11);
            labelStyle.setFont(labelFont);
            labelStyle.setAlignment(HorizontalAlignment.RIGHT); // 标签右对齐
            labelStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle dataStyle = workbook.createCellStyle();
            Font dataFont = workbook.createFont();
            dataFont.setFontName("宋体");
            dataFont.setFontHeightInPoints((short) 11);
            dataStyle.setFont(dataFont);
            dataStyle.setAlignment(HorizontalAlignment.LEFT); // 数据左对齐
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setFontName("宋体");
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setWrapText(true); // 允许换行

            CellStyle listDataStyle = workbook.createCellStyle();
            listDataStyle.setFont(dataFont);
            listDataStyle.setAlignment(HorizontalAlignment.CENTER);
            listDataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            listDataStyle.setBorderBottom(BorderStyle.THIN);
            listDataStyle.setBorderTop(BorderStyle.THIN);
            listDataStyle.setBorderLeft(BorderStyle.THIN);
            listDataStyle.setBorderRight(BorderStyle.THIN);
            // --- 样式创建结束 ---


            // --- 开始填充 ---
            int rowIndex = 0;
            int baseColNum = 2; // 固定列（序号、牛号）的数量
            int dynamicColNum = biochemistryItemList.size(); // 动态列的数量
            int totalColNum = baseColNum + dynamicColNum; // 总列数

            // 第1行: 标题
            Row row0 = sheet.createRow(rowIndex++);
            row0.setHeightInPoints(30);
            createCell(row0, 0, "光明牧业有限公司检验测试中心", titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, totalColNum - 1)); // 合并整个宽度

            // 第2行: 副标题
            Row row1 = sheet.createRow(rowIndex++);
            row1.setHeightInPoints(25);
            createCell(row1, 0, "生化检测报告", subTitleStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, totalColNum - 1)); // 合并整个宽度

            // 第3行: 送检单位
            Row row2 = sheet.createRow(rowIndex++);
            row2.setHeightInPoints(20);
            createCell(row2, 0, "送检单位：", labelStyle);
            createCell(row2, 1, entrustDeptName, dataStyle);
            // 可以根据需要合并送检单位的值单元格，如果动态列很多的话
            if (totalColNum > 2) {
                sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, totalColNum - 1));
            }


            // --- 创建动态表头 (三行) ---
            int headerStartRow = rowIndex;
            Row headerRow1 = sheet.createRow(rowIndex++); // 第4行: 项目名称
            Row headerRow2 = sheet.createRow(rowIndex++); // 第5行: 单位
            Row headerRow3 = sheet.createRow(rowIndex++); // 第6行: 参考范围

            headerRow1.setHeightInPoints(22);
            headerRow2.setHeightInPoints(22);
            headerRow3.setHeightInPoints(22);

            // 固定表头列 (序号, 牛号) - 创建并合并
            createCell(headerRow1, 0, "序号", headerStyle);
            createCell(headerRow1, 1, "牛号", headerStyle);
            // 为合并单元格创建边框 (因为合并后只有左上角单元格有样式)
            for (int i = headerStartRow + 1; i < headerStartRow + 3; i++) {
                createCell(sheet.getRow(i), 0, "", headerStyle);
                createCell(sheet.getRow(i), 1, "", headerStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(headerStartRow, headerStartRow + 2, 0, 0)); // 合并序号列 (A4:A6)
            sheet.addMergedRegion(new CellRangeAddress(headerStartRow, headerStartRow + 2, 1, 1)); // 合并牛号列 (B4:B6)


            // 动态表头列
            for (int i = 0; i < dynamicColNum; i++) {
                BiochemistryItem item = biochemistryItemList.get(i);
                int currentCol = baseColNum + i; // 当前列索引

                // 第4行: 项目名称
                createCell(headerRow1, currentCol, item.getName(), headerStyle);
                // 第5行: 单位
                createCell(headerRow2, currentCol, item.getUnit(), headerStyle);
                // 第6行: 参考范围
                createCell(headerRow3, currentCol, item.getRange(), headerStyle);
            }

            // --- 填充样品数据 ---
            int dataRowIndex = rowIndex; // 从第7行开始
            if (sampleList != null) {
                for (int i = 0; i < sampleList.size(); i++) {
                    OpBloodEntrustOrderSample sample = sampleList.get(i);
                    Row dataRow = sheet.createRow(dataRowIndex++);
                    dataRow.setHeightInPoints(20);

                    // 固定列
                    createCell(dataRow, 0, String.valueOf(i + 1), listDataStyle); // 序号
                    createCell(dataRow, 1, sample.getSampleName(), listDataStyle); // 牛号

                    // 动态列
                    for (int j = 0; j < dynamicColNum; j++) {
                        BiochemistryItem item = biochemistryItemList.get(j);
                        int currentCol = baseColNum + j; // 当前列索引

                        // 根据 item 的 biochemistryItemType 获取 sample 中对应的字段值
                        String value = getFieldValueByItemType(sample, item.getBiochemistryItemType());
                        createCell(dataRow, currentCol, value, listDataStyle);
                    }
                }
            }


            // 签发日期 (数据行之后)
            Row footerRow = sheet.createRow(dataRowIndex + 1); // 在最后一行数据后再空一行添加
            footerRow.setHeightInPoints(20);
            // 将签发日期放在最后几列，使其靠右
            createCell(footerRow, totalColNum - 2, "签发日期：", labelStyle); // 倒数第二列放标签
            String reportDate = opJczxBloodReportBase.getReportDate() != null ? sdf.format(opJczxBloodReportBase.getReportDate()) : "";
            createCell(footerRow, totalColNum - 1, reportDate, dataStyle); // 最后一列放日期


            // --- 设置列宽 ---
            sheet.setColumnWidth(0, 8 * 256); // A 序号
            sheet.setColumnWidth(1, 15 * 256); // B 牛号
            // 动态列宽
            for (int i = 0; i < dynamicColNum; i++) {
                int currentCol = baseColNum + i;
                // 可以根据项目名称长度稍微调整，或者给一个统一宽度
                sheet.setColumnWidth(currentCol, 15 * 256); // 统一宽度15
            }


            workbook.write(outputStream);
        }
    }


    /**
     * 辅助方法：根据生化项目类型(字典值)获取OpBloodEntrustOrderSample对象中对应的字段值
     *
     * @param sample   样品对象
     * @param itemType 生化项目的字典值 (e.g., "1", "2", ...)
     * @return 对应的字段值，如果找不到或为null则返回空字符串
     */
    private String getFieldValueByItemType(OpBloodEntrustOrderSample sample, String itemType) {
        if (sample == null || itemType == null) {
            return "";
        }
        String value = null;
        switch (itemType) {
            case "1":
                value = sample.getShZdb();
                break;     // 总蛋白
            case "2":
                value = sample.getShBdb();
                break;     // 白蛋白
            case "3":
                value = sample.getShZg();
                break;      // 总钙
            case "4":
                value = sample.getShLzg();
                break;     // 离子钙
            case "5":
                value = sample.getShMlz();
                break;     // 镁离子
            case "6":
                value = sample.getShWjl();
                break;     // 无机磷
            case "7":
                value = sample.getShNlz();
                break;     // 钠离子
            case "8":
                value = sample.getShJlz();
                break;     // 钾离子
            case "9":
                value = sample.getShLlz();
                break;     // 氯离子
            case "10":
                value = sample.getShTlz();
                break;    // 铜离子
            case "11":
                value = sample.getShTielz();
                break;  // 铁离子
            case "12":
                value = sample.getShXlz();
                break;    // 锌离子
            case "13":
                value = sample.getShGysz();
                break;   // 甘油三酯
            case "14":
                value = sample.getShQds();
                break;    // β-羟丁酸
            case "15":
                value = sample.getShFzhx();
                break;   // 非酯化性脂肪酸
            case "16":
                value = sample.getShPpt();
                break;    // 葡萄糖
            case "17":
                value = sample.getShXynsd();
                break;   // 血液尿素氮
            case "18":
                value = sample.getShGczam();
                break;  // 谷草转氨酶
            case "19":
                value = sample.getShGbzam();
                break;  // 谷丙转氨酶
            case "20":
                value = sample.getShJxlsm();
                break;  // 碱性磷酸酶
            // 如果有更多项目，继续添加 case
            default:
                // 如果字典值没有匹配的字段，可以选择返回空字符串或记录日志
                System.err.println("未找到与生化项目类型 '" + itemType + "' 对应的字段");
                break;
        }
        return value != null ? value : ""; // 如果获取到的值为 null，也返回空字符串
    }

    /**
     * 填充Excel模板
     */
    private void fillZaoyunExcelTemplate(HttpServletResponse response, OpJczxBloodReportBase
            opJczxBloodReportBase, List<OpBloodEntrustOrderSample> sampleList)
            throws Exception {

        // 日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 动态创建Excel，不使用模板
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             OutputStream outputStream = response.getOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("早孕检测报告");

            // --- 创建样式 ---
            // 标题样式
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setFontName("宋体");
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setBold(true);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // 副标题样式
            CellStyle subTitleStyle = workbook.createCellStyle();
            Font subTitleFont = workbook.createFont();
            subTitleFont.setFontName("宋体");
            subTitleFont.setFontHeightInPoints((short) 14);
            subTitleFont.setBold(true);
            subTitleStyle.setFont(subTitleFont);
            subTitleStyle.setAlignment(HorizontalAlignment.CENTER);
            subTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // 英文标题
            CellStyle enTitleStyle = workbook.createCellStyle();
            Font enFont = workbook.createFont();
            enFont.setFontName("Arial");
            enFont.setFontHeightInPoints((short) 12);
            enFont.setItalic(true);
            enTitleStyle.setFont(enFont);
            enTitleStyle.setAlignment(HorizontalAlignment.CENTER);
            enTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // 标签样式 (如：送样单位：)
            CellStyle labelStyle = workbook.createCellStyle();
            Font labelFont = workbook.createFont();
            labelFont.setFontName("宋体");
            labelFont.setFontHeightInPoints((short) 11);
            labelStyle.setFont(labelFont);
            labelStyle.setAlignment(HorizontalAlignment.RIGHT);
            labelStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // 数据样式 (左对齐)
            CellStyle dataStyle = workbook.createCellStyle();
            Font dataFont = workbook.createFont();
            dataFont.setFontName("宋体");
            dataFont.setFontHeightInPoints((short) 11);
            dataStyle.setFont(dataFont);
            dataStyle.setAlignment(HorizontalAlignment.LEFT);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // ：数据样式 (居中对齐)
            CellStyle dataStyleCenter = workbook.createCellStyle();
            dataStyleCenter.cloneStyleFrom(dataStyle); // 基于 dataStyle 复制
            dataStyleCenter.setAlignment(HorizontalAlignment.CENTER); // 设置居中

            // 表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setFontName("宋体");
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // 列表数据样式
            CellStyle listDataStyle = workbook.createCellStyle();
            listDataStyle.setFont(dataFont);
            listDataStyle.setAlignment(HorizontalAlignment.CENTER);
            listDataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            listDataStyle.setBorderBottom(BorderStyle.THIN);
            listDataStyle.setBorderTop(BorderStyle.THIN);
            listDataStyle.setBorderLeft(BorderStyle.THIN);
            listDataStyle.setBorderRight(BorderStyle.THIN);

            // --- 开始填充 ---

            // 第1行: 标题
            Row row0 = sheet.createRow(0);
            row0.setHeightInPoints(30);
            createCell(row0, 0, "光明牧业有限公司检验测试中心", titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4)); // 合并 A1:E1

            // 第2行: 副标题
            Row row1 = sheet.createRow(1);
            row1.setHeightInPoints(25);
            createCell(row1, 0, "早孕检测报告", subTitleStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4)); // 合并 A2:E2

            // 第3行: 英文
            Row row2 = sheet.createRow(2);
            row2.setHeightInPoints(20);
            createCell(row2, 2, "BioPRYN PSPB Pregnancy Report", enTitleStyle);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 4)); // 合并 C3:E3

            // 第4行: 送样单位, 收样日期, 样品数量
            Row row3 = sheet.createRow(3);
            row3.setHeightInPoints(20);
            createCell(row3, 0, "送样单位：", labelStyle);
            createCell(row3, 1, opJczxBloodReportBase.getEntrustDeptName(), dataStyle);
            createCell(row3, 2, "收样日期：", labelStyle);
            String receiveTime = opJczxBloodReportBase.getReceiveTime() != null ? sdf.format(opJczxBloodReportBase.getReceiveTime()) : "";
            createCell(row3, 3, receiveTime, dataStyle);
            createCell(row3, 4, opJczxBloodReportBase.getSampleAmount() + "个", dataStyle);

            // 第5行: 空行 (跳过)

            // 第6行: 固定文本
            Row row5 = sheet.createRow(5);
            createCell(row5, 1, "BioPRYN OD", headerStyle);
            createCell(row5, 3, "BioPRYN OD", headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 2)); // 合并 B6:C6
            sheet.addMergedRegion(new CellRangeAddress(5, 5, 3, 4)); // 合并 D6:E6

            // 第7行: 固定文本
            Row row6 = sheet.createRow(6);
            createCell(row6, 0, "空怀<", labelStyle);
            createCell(row6, 1, "< 0.135", dataStyle);
            // 使用居中样式
            createCell(row6, 2, "< 复查 <", dataStyleCenter);
            createCell(row6, 3, "< 0.21", dataStyleCenter);
            createCell(row6, 4, "< 怀孕", dataStyle);


            // 第8行: 空行 (跳过)

            // 第9行: 列表表头
            Row row8 = sheet.createRow(8);
            row8.setHeightInPoints(22);
            createCell(row8, 0, "管号", headerStyle);
            createCell(row8, 1, "牛号", headerStyle);
            createCell(row8, 2, "BioPRYN OD", headerStyle);
            createCell(row8, 3, "结果", headerStyle);
            createCell(row8, 4, "备注", headerStyle);

            // 第10行开始: 填充样品数据
            int dataRowIndex = 9;
            if (sampleList != null) {  // 添加空值检查
                for (OpBloodEntrustOrderSample sample : sampleList) {
                    Row dataRow = sheet.createRow(dataRowIndex);
                    dataRow.setHeightInPoints(20);

                    // 管号 (OpBloodEntrustOrderSample)
                    createCell(dataRow, 0, sample.getGh(), listDataStyle);
                    // 牛号 (OpBloodEntrustOrderSample)
                    createCell(dataRow, 1, sample.getSampleName(), listDataStyle);
                    // BioPRYN OD (假设字段为 od, OpBloodEntrustOrderSample)
                    createCell(dataRow, 2, sample.getOd(), listDataStyle);
                    // 结果 (假设字段为 zaoyunTestResult, OpBloodEntrustOrderSample)
                    createCell(dataRow, 3, sample.getZaoyunTestResult(), listDataStyle);
                    // 备注 (假设字段为 testRemark, OpBloodEntrustOrderSample)
                    createCell(dataRow, 4, sample.getTestRemark(), listDataStyle);

                    dataRowIndex++;
                }
            }

            // 签发日期 (数据行之后)
            Row footerRow = sheet.createRow(dataRowIndex + 1); // 行号基于 dataRowIndex
            footerRow.setHeightInPoints(20);
            createCell(footerRow, 3, "签发日期：", labelStyle);
            String reportDate = opJczxBloodReportBase.getReportDate() != null ? sdf.format(opJczxBloodReportBase.getReportDate()) : "";
            createCell(footerRow, 4, reportDate, dataStyle);


            // --- 优化点：设置列宽 (根据模板大致调整) ---
            sheet.setColumnWidth(0, 15 * 256); // A 管号
            sheet.setColumnWidth(1, 20 * 256); // B 牛号
            sheet.setColumnWidth(2, 20 * 256); // C BioPRYN OD
            sheet.setColumnWidth(3, 15 * 256); // D 结果
            sheet.setColumnWidth(4, 25 * 256); // E 备注
            // ---------------------------------

            workbook.write(outputStream);
        }

    }

    /**
     * 辅助方法：创建单元格并设置值和样式
     */
    private void createCell(Row row, int colIndex, String value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }


    @Override
    public Map<String, Integer> getStatusCount(OpJczxBloodReportBase base) {

        Integer status_0 = opJczxBloodReportBaseMapper.getStatus0Count(base);

        List<Map<String, Object>> rows = opJczxBloodReportBaseMapper.getStatusCount(base);
        Map<String, Integer> countsMap = rows.stream()
                .collect(Collectors.toMap(
                        row -> String.valueOf(row.get("status")),
                        row -> ((Number) row.get("count")).intValue()
                ));

        Map<String, Integer> statusCount = new HashMap<>();
        statusCount.put("status_1", countsMap.getOrDefault("1", 0));
        statusCount.put("status_2", countsMap.getOrDefault("2", 0));
        statusCount.put("status_3", countsMap.getOrDefault("3", 0));
        statusCount.put("status_4", countsMap.getOrDefault("4", 0));
        statusCount.put("status_5", countsMap.getOrDefault("5", 0));
        statusCount.put("status_0", status_0 != null ? status_0 : 0);

        return statusCount;

    }

    @Override
    public OpJczxBloodReportBase getReport( String opJczxBloodReportBaseId) {

        if (ObjectUtils.isEmpty(opJczxBloodReportBaseId)) {
            throw new RuntimeException("缺少必要参数，请联系管理员");
        }
        OpJczxBloodReportBase reportBase = opJczxBloodReportBaseMapper.selectOpJczxBloodReportBaseByOpJczxBloodReportBaseId(opJczxBloodReportBaseId);
        if (ObjectUtils.isEmpty(reportBase)) {
            throw new RuntimeException("未获取到报告信息");
        }
        String status = reportBase.getStatus();
        if (ObjectUtils.isEmpty(status)) {
            throw new RuntimeException("未获取到报告信息");
        }
        if (!status.equals(JczxFeedReportStatusEnum.YPZ.getCode())) {
            throw new RuntimeException("报告未批准，无法查询");
        }
        List<OpJczxBloodReportInfo> yinList = new ArrayList<>();//阴性
        List<OpJczxBloodReportInfo> yangList = new ArrayList<>();//阳性
        List<OpJczxBloodReportInfo> ktyList = new ArrayList<>();//口蹄疫
        List<OpJczxBloodReportInfo> zaoyunList = new ArrayList<>();//早孕
        List<OpJczxBloodReportInfo> shList = new ArrayList<>();//生化
        String itemType = reportBase.getBloodTaskItemType();
        List<OpJczxBloodReportInfo> opJczxBloodReportInfos = new ArrayList<>();
        OpJczxBloodReportInfo info = new OpJczxBloodReportInfo();
        info.setBaseId(opJczxBloodReportBaseId);
        //查询报告子表
        opJczxBloodReportInfos = opJczxBloodReportInfoMapper.selectOpJczxBloodReportInfoList(info);
        if (itemType.equals("2") || itemType.equals("6")) {//结核（牛分枝杆菌PPD）,结核抗体
            for (OpJczxBloodReportInfo opJczxBloodReportInfo : opJczxBloodReportInfos) {
                //子表结果组装
                String testResult = opJczxBloodReportInfo.getTestResult();
                OpJczxBloodReportInfo vo = new OpJczxBloodReportInfo();
                vo.setSampleName(opJczxBloodReportInfo.getSampleName());
                vo.setSp(opJczxBloodReportInfo.getSp());
                if (testResult.equals("1")) {
                    vo.setTestResult("1");
                    yangList.add(vo);
                } else if (testResult.equals("0")) {
                    vo.setTestResult("0");
                    yinList.add(vo);
                }
            }
        } else if (itemType.equals("0")) {//口蹄疫
            ktyList = opJczxBloodReportInfos;
        } else if (itemType.equals("9")) {//早孕
            zaoyunList = opJczxBloodReportInfos;
        } else if (itemType.equals("8")) {//生化
            shList = opJczxBloodReportInfos;
        }
        // 获取当前日期
        LocalDate today = LocalDate.now();
        Date d = java.sql.Date.valueOf(today);
        reportBase.setReportDate(d);
        //结核（牛分枝杆菌PPD）,结核抗体 列表
        OpBloodJHReportSampleVo opBloodJHReportSampleVo = new OpBloodJHReportSampleVo();
        opBloodJHReportSampleVo.setYangList(yangList);
        opBloodJHReportSampleVo.setYinList(yinList);
        //口蹄疫列表
        opBloodJHReportSampleVo.setKtyList(ktyList);
        reportBase.setOpBloodJHReportSampleVo(opBloodJHReportSampleVo);

        //早孕
        opBloodJHReportSampleVo.setZaoyunList(zaoyunList);
        //生化
        opBloodJHReportSampleVo.setShList(shList);
        //查询签名
        String editUserId = reportBase.getEditUserId();
        String checkUserId = reportBase.getCheckUserId();
        String approveUserId = reportBase.getApproveUserId();
        if (!ObjectUtils.isEmpty(editUserId)) {
            String sign = sysUserMapper.selectSignByUserId(editUserId);
            reportBase.setEditSign(sign);
        }
        if (!ObjectUtils.isEmpty(checkUserId)) {
            String sign = sysUserMapper.selectSignByUserId(checkUserId);
            reportBase.setCheckSign(sign);
        }
        if (!ObjectUtils.isEmpty(approveUserId)) {
            String sign = sysUserMapper.selectSignByUserId(approveUserId);
            reportBase.setApproveSign(sign);
        }
        return reportBase;


    }

    @Override
    @Transactional
    public void addAll(OpJczxBloodReportBase opJczxBloodReportBase) {
        List<String> bloodEntrustOrderIdList = opJczxBloodReportBase.getBloodEntrustOrderIdList();
        //流转到待发送
        for (String orderId : bloodEntrustOrderIdList) {
            OpBloodEntrustOrder opBloodEntrustOrder = opBloodEntrustOrderMapper.selectOpBloodEntrustOrderByOpBloodEntrustOrderId(orderId);
            //制作
            OpJczxBloodReportBase query = new OpJczxBloodReportBase();
            query.setStatus(JczxFeedReportStatusEnum.DZZ.getCode());
            query.setBloodTaskItemType(opBloodEntrustOrder.getBloodTaskItemType());
            query.setBloodEntrustOrderId(orderId);
            OpJczxBloodReportBase reportBase =  this.selectOpJczxBloodReportBaseByOpJczxBloodReportBaseId(query);
            //不要删这行
            reportBase.setStatus(JczxFeedReportStatusEnum.ZZZ.getCode());
            String baseId = this.insertOpJczxBloodReportBase(reportBase);
            System.out.println(baseId);
            //提交
            reportBase.setStatus(JczxFeedReportStatusEnum.DSH.getCode());
            reportBase.setOpJczxBloodReportBaseId(baseId);
            this.insertOpJczxBloodReportBase(reportBase);

        }
    }

    /**
     * 疾病报告
     * @param opJczxBloodReportBase 获取条件
     * @return 疾病报告列表
     */
    @Override
    public List<OpJczxBloodReportBase> queryDiseaseReport(OpJczxBloodReportBase opJczxBloodReportBase) {
        opJczxBloodReportBase.setBloodTaskItemTypeNotIn("8,9");
        return opJczxBloodReportBaseMapper.selectOpJczxBloodReportBaseList(opJczxBloodReportBase);
    }

    /**
     * 获取早产报告
     * @param opJczxBloodReportBase 获取条件
     * @return 早产报告集合
     */
    @Override
    public List<OpJczxBloodReportBase> queryEarlyPregnancyReport(OpJczxBloodReportBase opJczxBloodReportBase) {
        opJczxBloodReportBase.setBloodTaskItemType("9");
        return opJczxBloodReportBaseMapper.selectOpJczxBloodReportBaseList(opJczxBloodReportBase);
    }

    /**
     * 查询生化报告
     * @param opJczxBloodReportBase 查询条件
     * @return 生化报告集合
     */
    @Override
    public List<OpJczxBloodReportBase> queryBiochemistryReport(OpJczxBloodReportBase opJczxBloodReportBase) {
        opJczxBloodReportBase.setBloodTaskItemType("8");
        return opJczxBloodReportBaseMapper.selectOpJczxBloodReportBaseList(opJczxBloodReportBase);
    }

    //早孕退回，作废reportBase,并把resultBase的examine_pass_flag改完不通过
    @Override
    public void zyBack(OpJczxBloodReportBase opJczxBloodReportBase) {
        opJczxBloodReportBase.setStatus(JczxFeedReportStatusEnum.ZF.getCode());
        opJczxBloodReportBase.fillUpdateInfo();
        opJczxBloodReportBaseMapper.updateStatus(opJczxBloodReportBase);


        String orderId = opJczxBloodReportBase.getBloodEntrustOrderId();
        List<OpBloodEntrustOrderSample> opBloodEntrustOrderSamples = opBloodEntrustOrderSampleMapper.selectAllByOpBloodEntrustOrderId(orderId);
        List<String> sampleNos = opBloodEntrustOrderSamples.stream()
                .map(OpBloodEntrustOrderSample::getSampleNo)
                .distinct()// 根据实际属性名调整
                .collect(Collectors.toList());
        List<OpJczxBloodResultBase> opJczxBloodResultBases = opJczxBloodResultBaseMapper.selectOpJczxBloodResultBaseListBySampleNoList(sampleNos);
        if(CollectionUtil.isNotEmpty(opJczxBloodResultBases)){
            for (OpJczxBloodResultBase opJczxBloodResultBase : opJczxBloodResultBases) {
                OpJczxBloodResultBase resultBase = new OpJczxBloodResultBase();
                resultBase.setExaminePassFlag("0");
                opJczxBloodResultBaseMapper.updateExamineFlagByResultNo("0",opJczxBloodResultBase.getResultNo());
            }

        }


        //opBloodEntrustOrderMapper.updateStatusById(orderId, EntrustOrderStatusEnum.YSH.getCode(),examineUser, examineUserId, examineTime,examineUserId,examineTime);


    }
}