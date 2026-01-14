package com.gmlimsqi.business.util;

import com.gmlimsqi.business.sequence.domain.SysSequence;
import com.gmlimsqi.business.sequence.service.ISysSequenceService;
import com.gmlimsqi.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * 编码生成工具类（处理各类业务编码生成）
 * 注意：非静态工具类，需通过Spring注入使用（避免静态方法依赖注入问题）
 *
 * @author ruoyi
 */
@Component
public class CodeGeneratorUtil {

    // 编码类型常量定义
    public static final String CODE_TYPE_SAMPLE = "SAMPLE";
    public static final String CODE_TYPE_FEED_ENTRUST_ORDER = "FEED_ENTRUST_ORDER";
    public static final String CODE_TYPE_PCR_RESULT = "PCR_RESULT";
    public static final String CODE_TYPE_FEED_SAMPLE = "FEED_SAMPLE";
    public static final String CODE_TYPE_PCR_ENTRUST_ORDER = "PCR_ENTRUST_ORDER";
    public static final String CODE_TYPE_PCR_SAMPLE = "PCR_SAMPLE";
    public static final String CODE_TYPE_BLOOD_ENTRUST_ORDER = "BLOOD_ENTRUST_ORDER";
    public static final String CODE_TYPE_BLOOD_SAMPLE = "BLOOD_SAMPLE";
    public static final String CODE_TYPE_BLOOD_RESULT = "BLOOD_RESULT";
    public static final String CODE_TYPE_FEED_RESULT = "FEED_RESULT";
    public static final String CODE_TYPE_FEED_REPORT = "FEED_REPORT";
    public static final String CODE_TYPE_PCR_REPORT = "PCR_REPORT";
    public static final String CODE_TYPE_BLOOD_REPORT = "BLOOD_REPORT";
    public static final String CODE_TYPE_SAMPLE_PLAN = "SAMPLE_PLAN";
    public static final String CODE_TYPE_UNQUALITY = "UNQUALITY";
    // 感官不合格处理单
    public static final String CODE_TYPE_UNQUALITY_SENSORY = "UNQUALITY_SENSORY";
    public static final String CODE_TYPE_SAMPLE_SL_QRCODE = "SL";
    // 奶仓质检单编码
    public static final String CODE_TYPE_MILKBIN_QUALITY_INSPECTION = "MILKBIN_QUALITY_INSPECTION";
    // 奶罐车检查编码
    public static final String CODE_TYPE_INSPECTION_MILK_TANKERS = "INSPECTION_MILK_TANKERS";
    // 装奶单编码
    public static final String CODE_TYPE_MILK_FILLING_ORDER = "MILK_FILLING_ORDER";
    // 铅封单编码
    public static final String CODE_TYPE_LEAD_SEAL_SHEET = "LEAD_SEAL_SHEET";
    // 奶样质量单编码
    public static final String CODE_TYPE_MILK_SAMPLE_QUALITY_INSPECTION = "MILK_SAMPLE_QUALITY_INSPECTION";

     // 成品取样计划编码
    public static final String CODE_TYPE_FINISHED_PRODUCT_SAMPLING_PLAN = "FINISHED_PRODUCT_SAMPLING_PLAN";

    // 库存取样计划编码
    public static final String CODE_TYPE_INVENTORY_SAMPLING_PLAN = "INVENTORY_SAMPLING_PLAN";

    // 垫料取样计划编码
    public static final String CODE_TYPE_FEED_SAMPLING_PLAN = "FEED_SAMPLING_PLAN";
    // 原料取样计划编码
    public static final String CODE_TYPE_YL_SAMPLING_PLAN = "YL_SAMPLING_PLAN";



    // 成品取样单编码
    public static final String CODE_TYPE_FINISHED_PRODUCT_SAMPLING_ORDER = "FINISHED_PRODUCT_SAMPLING_ORDER";
    // 库存取样单编码
    public static final String CODE_TYPE_INVENTORY_SAMPLING_ORDER = "INVENTORY_SAMPLING_ORDER";
    // 垫料取样单编码
    public static final String CODE_TYPE_FEED_SAMPLING_ORDER = "FEED_SAMPLING_ORDER";
    // 原料取样单编码
    public static final String CODE_TYPE_YLQYD = "YLQYD";



    // 手动新增的物料编码
    public static final String CODE_TYPE_MANUAL_INVBILL = "MANUAL_INVBILL";

    // 检验报告模板编码
    public static final String CODE_TYPE_INSPECTION_REPORT_TEMPLATE = "INSPECTION_REPORT_TEMPLATE";



    // === 新增：按公司隔离的化验单编码 ===
    /**
     * 按公司隔离的化验单编码
     * 规则: MC + yyMMdd + 3位流水号 (按公司隔离)
     */
    public static final String CODE_TYPE_HYD_ASSAY_ORDER = "HYD_ASSAY_ORDER";
    // 编码配置映射
    private static final Map<String, CodeConfig> CODE_CONFIG_MAP = new HashMap<>();

    static {
        // 初始化编码配置：billCode, billName, prefix, genBit
        CODE_CONFIG_MAP.put(CODE_TYPE_SAMPLE, new CodeConfig("SAMPLE", "外部送检样品编号", "HX", 4));
        CODE_CONFIG_MAP.put(CODE_TYPE_FEED_ENTRUST_ORDER, new CodeConfig("FEED_ENTRUST", "饲料样品委托单编码", "SLWT", 5));
        CODE_CONFIG_MAP.put(CODE_TYPE_FEED_SAMPLE, new CodeConfig("FEED_SAMPLE", "饲料样品编码", "", 3));
        CODE_CONFIG_MAP.put(CODE_TYPE_PCR_ENTRUST_ORDER, new CodeConfig("PCR_ENTRUST_ORDER", "pcr样品委托单编码", "PCRWT", 5));
        CODE_CONFIG_MAP.put(CODE_TYPE_PCR_SAMPLE, new CodeConfig("PCR_SAMPLE", "pcr样品编码", "PCR", 3));
        CODE_CONFIG_MAP.put(CODE_TYPE_BLOOD_ENTRUST_ORDER, new CodeConfig("BLOOD_ENTRUST_ORDER", "血样样品委托单编码", "XYWT", 5));
        CODE_CONFIG_MAP.put(CODE_TYPE_BLOOD_SAMPLE, new CodeConfig("BLOOD_SAMPLE", "血样样品编码", "XYYP", 5));
        CODE_CONFIG_MAP.put(CODE_TYPE_BLOOD_RESULT, new CodeConfig("BLOOD_RESULT", "血样化验单编码", "XYHY", 3));

        CODE_CONFIG_MAP.put(CODE_TYPE_FEED_RESULT, new CodeConfig("FEED_RESULT", "饲料化验单编码", "SLHY", 3));
        CODE_CONFIG_MAP.put(CODE_TYPE_PCR_RESULT, new CodeConfig("PCR_RESULT", "PCR化验单编码", "PCRHY", 3));

        CODE_CONFIG_MAP.put(CODE_TYPE_FEED_REPORT, new CodeConfig("FEED_REPORT", "饲料报告编码", "", 3));
        CODE_CONFIG_MAP.put(CODE_TYPE_PCR_REPORT, new CodeConfig("PCR_REPORT", "PCR报告编码", "", 3));
        CODE_CONFIG_MAP.put(CODE_TYPE_BLOOD_REPORT, new CodeConfig("BLOOD_REPORT", "BLOOD报告编码", "", 3));
        CODE_CONFIG_MAP.put(CODE_TYPE_SAMPLE_PLAN, new CodeConfig("SAMPLE_PLAN", "取样计划编码", "", 3));
        CODE_CONFIG_MAP.put(CODE_TYPE_UNQUALITY, new CodeConfig("UNQUALITY", "质检不合格处理单", "ZJBHG", 5));
        // 感官不合格处理单
        CODE_CONFIG_MAP.put(CODE_TYPE_UNQUALITY_SENSORY, new CodeConfig("UNQUALITY_SENSORY", "感官不合格处理单", "GGBHG", 5));

        CODE_CONFIG_MAP.put(CODE_TYPE_SAMPLE_SL_QRCODE, new CodeConfig("SL", "饲料取样二维码", "", 3));
        // 奶仓质检单编码
        CODE_CONFIG_MAP.put(CODE_TYPE_MILKBIN_QUALITY_INSPECTION, new CodeConfig("MILKBIN_QUALITY_INSPECTION", "奶仓质检单编码", "NCZJ", 5));

        // === 新增：MC化验单的基础配置 ===
        CODE_CONFIG_MAP.put(CODE_TYPE_HYD_ASSAY_ORDER, new CodeConfig("HYD_ASSAY_ORDER", "公司化验单", "", 3));
        // 奶罐车检查编码
        CODE_CONFIG_MAP.put(CODE_TYPE_INSPECTION_MILK_TANKERS, new CodeConfig("INSPECTION_MILK_TANKERS", "奶罐车检查编码", "NGCJC", 5));
        // 装奶单编码
        CODE_CONFIG_MAP.put(CODE_TYPE_MILK_FILLING_ORDER, new CodeConfig("MILK_FILLING_ORDER", "装奶单编码", "ZNDH", 5));
        // 铅封单编码
        CODE_CONFIG_MAP.put(CODE_TYPE_LEAD_SEAL_SHEET, new CodeConfig("LEAD_SEAL_SHEET", "铅封单编码", "QFDH", 5));
        // 奶样质量单编码
        CODE_CONFIG_MAP.put(CODE_TYPE_MILK_SAMPLE_QUALITY_INSPECTION, new CodeConfig("MILK_SAMPLE_QUALITY_INSPECTION", "奶样质量单编码", "NYZJ", 5));

        // 成品取样计划编码
        CODE_CONFIG_MAP.put(CODE_TYPE_FINISHED_PRODUCT_SAMPLING_PLAN, new CodeConfig("FINISHED_PRODUCT_SAMPLING_PLAN", "成品取样计划编码", "CPQYJH", 5));

        // 库存取样计划编码
        CODE_CONFIG_MAP.put(CODE_TYPE_INVENTORY_SAMPLING_PLAN, new CodeConfig("INVENTORY_SAMPLING_PLAN", "库存取样计划编码", "KCQYJH", 5));

        // 垫料取样计划编码
        CODE_CONFIG_MAP.put(CODE_TYPE_FEED_SAMPLING_PLAN, new CodeConfig("FEED_SAMPLING_PLAN", "垫料取样计划编码", "DLQYJH", 5));
        // 原料取样计划编码
        CODE_CONFIG_MAP.put(CODE_TYPE_YL_SAMPLING_PLAN, new CodeConfig("YL_SAMPLING_PLAN", "原料取样计划编码", "YLYJH", 5));

        // 成品取样单编码
        CODE_CONFIG_MAP.put(CODE_TYPE_FINISHED_PRODUCT_SAMPLING_ORDER, new CodeConfig("FINISHED_PRODUCT_SAMPLING_ORDER", "成品样品单编码", "CPYPDH", 5));
        // 库存取样单编码
        CODE_CONFIG_MAP.put(CODE_TYPE_INVENTORY_SAMPLING_ORDER, new CodeConfig("INVENTORY_SAMPLING_ORDER", "库存取样单编码", "KCYPDH", 5));
        // 垫料取样单编码
        CODE_CONFIG_MAP.put(CODE_TYPE_FEED_SAMPLING_ORDER, new CodeConfig("FEED_SAMPLING_ORDER", "垫料取样单编码", "DLYPDH", 5));

        // 原料取样单编码
        CODE_CONFIG_MAP.put(CODE_TYPE_YLQYD, new CodeConfig("YLQYD", "原料取样单编码", "YLYPDH", 5));

        // 手动新增的物料编码
        CODE_CONFIG_MAP.put(CODE_TYPE_MANUAL_INVBILL, new CodeConfig("MANUAL_INVBILL", "手动新增的物料编码", "WLDH", 5));
        // 检验报告模板编码
        CODE_CONFIG_MAP.put(CODE_TYPE_INSPECTION_REPORT_TEMPLATE, new CodeConfig("INSPECTION_REPORT_TEMPLATE", "检验报告模板编码", "JYBGMB", 5));
    }

    @Autowired
    private ISysSequenceService sequenceService;

    /**
     * 生成【外部送检样品编号】
     * 格式：前缀（HX） + 日期（YYMMDD） + 4位流水号
     *
     * @return 生成的样品编号
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateSampleCode() throws BusinessException {
        return generateCode(CODE_TYPE_SAMPLE);
    }

    /**
     * 生成【饲料样品委托单编码】
     * 格式：前缀（SLWT） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成的委托单编码
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateFeedEntrustOrderNo() throws BusinessException {
        return generateCode(CODE_TYPE_FEED_ENTRUST_ORDER);
    }
    /**
     * 生成【公司化验单号】（按公司隔离）
     * 规则: MC + yyMMdd + 3位流水号
     *
     * @param companyId 公司标识 (用于隔离流水号, 不能为空)
     * @return 生成的化验单号
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateHydAssayOrderNo(String companyId) throws BusinessException {
        if (companyId == null || companyId.trim().isEmpty()) {
            throw new BusinessException("生成公司化验单号失败：必须提供公司标识");
        }
        // 调用重载的 generateCode 方法，传入公司ID
        return generateCode(CODE_TYPE_HYD_ASSAY_ORDER, companyId);
    }

    /**
     * 生成【pcr化验单编码】
     * 格式：前缀（PCRHY） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成的委托单编码
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generatePcrResultNo() throws BusinessException {
        return generateCode(CODE_TYPE_PCR_RESULT);
    }

    /**
     * 生成【血样化验单编码】
     * 格式：前缀（PCRHY） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成的委托单编码
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateBloodResultNo() throws BusinessException {
        return generateCode(CODE_TYPE_BLOOD_RESULT);
    }

    /**
     * 生成【pcr样品委托单编码】
     * 格式：前缀（PCRWT） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成的委托单编码
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generatePcrEntrustOrderNo() throws BusinessException {
        return generateCode(CODE_TYPE_PCR_ENTRUST_ORDER);
    }

    /**
     * 生成【血样样品委托单编码】
     * 格式：前缀（XYWT） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成的委托单编码
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateBloodEntrustOrderNo() throws BusinessException {
        return generateCode(CODE_TYPE_BLOOD_ENTRUST_ORDER);
    }

    /**
     * 生成【饲料样品编码】
     * 格式：前缀（SLYP） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成的饲料样品编码
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateFeedSampleNo() throws BusinessException {
        return generateCode(CODE_TYPE_FEED_SAMPLE);
    }

    /**
     * 生成【pcr样品编码】
     * 格式：前缀（PCRYP） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成的饲料样品编码
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generatePcrSampleNo() throws BusinessException {
        return generateCode(CODE_TYPE_PCR_SAMPLE);
    }

    /**
     * 生成【p血样样品编码】
     * 格式：前缀（XYYP） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成的饲料样品编码
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateBloodSampleNo() throws BusinessException {
        return generateCode(CODE_TYPE_BLOOD_SAMPLE);
    }

    /**
     * 生成【饲料报告编码】
     * 格式：前缀（XYYP） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成的饲料报告
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateFeedReportNo() throws BusinessException {
        return generateCode(CODE_TYPE_FEED_REPORT);
    }

    /**
     * 生成【PCR报告编码】
     * 格式：前缀（XYYP） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成的PCR报告
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generatePCRReportNo() throws BusinessException {
        return generateCode(CODE_TYPE_PCR_REPORT);
    }

    /**
     * 生成【BLOOD报告编码】
     * 格式：前缀（XYYP） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成的PCR报告
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateBLOODReportNo() throws BusinessException {
        return generateCode(CODE_TYPE_BLOOD_REPORT);
    }

    /**
     * 生成【BLOOD报告编码】
     * 格式：前缀（XYYP） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成的PCR报告
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateSAMPLEPlanNo() throws BusinessException {
        return generateCode(CODE_TYPE_SAMPLE_PLAN);
    }

    /**
     * 生成【质检不合格单】
     * 格式：前缀（XYYP） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成质检不合格单
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateUNQUALITYNo() throws BusinessException {
        return generateCode(CODE_TYPE_UNQUALITY);
    }

    /**
     * 生成【感官不合格单】
     * 格式：前缀（XYYP） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成感官不合格单
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateUNQUALITYSensoryNo() throws BusinessException {
        return generateCode(CODE_TYPE_UNQUALITY_SENSORY);
    }

    /**
     * 生成饲料取样二维码
     * 格式：前缀（XYYP） + 日期（YYMMDD） + 5位流水号
     *
     * @return 生成饲料取样二维码
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateSamoleSLQRNo() throws BusinessException {
        return generateCode(CODE_TYPE_SAMPLE_SL_QRCODE);
    }

    /**
     * 通用编码生成方法 (旧版)
     *
     * @param codeType 编码类型
     * @return 生成的编码
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateCode(String codeType) throws BusinessException {
        // 保持兼容，调用新的重载方法，公司ID为null
        return generateCode(codeType, null);
    }
    /**
     * 【核心修改】通用编码生成方法（支持按公司隔离）
     *
     * @param codeType  编码类型
     * @param companyId 公司标识 (可选, null表示不隔离)
     * @return 生成的编码
     * @throws BusinessException 编码生成失败时抛出
     */
    public String generateCode(String codeType, String companyId) throws BusinessException {
        CodeConfig config = CODE_CONFIG_MAP.get(codeType);
        if (config == null) {
            throw new BusinessException("不支持的编码类型: " + codeType);
        }

        // 将 companyId 传入，用于构建 SysSequence
        SysSequence sequence = buildSysSequence(config, companyId);

        try {
            Assert.notNull(sequence, "序列参数不能为空");
            return sequenceService.genCodeBySequence(sequence);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("生成" + config.getBillName() + "失败：参数非法", e);
        } catch (Exception e) {
            // 捕获更具体的异常或保留泛型异常
            // e.printStackTrace(); // 考虑日志记录
            throw new BusinessException("生成" + config.getBillName() + "失败：服务调用异常", e);
        }
    }

    /**
     * 构建SysSequence参数 (旧版)
     *
     * @param config 编码配置
     * @return 构建好的SysSequence
     */
    private SysSequence buildSysSequence(CodeConfig config) {
        // 保持兼容，调用新的重载方法，公司ID为null
        return buildSysSequence(config, null);
    }

    /**
     * 【核心修改】构建SysSequence参数（支持按公司隔离）
     *
     * @param config    编码配置
     * @param companyId 公司标识 (可选)
     * @return 构建好的SysSequence
     */
    private SysSequence buildSysSequence(CodeConfig config, String companyId) {
        SysSequence sequence = new SysSequence();

        String billCode = config.getBillCode();
        String billName = config.getBillName();

        // 核心隔离逻辑：
        // 如果传入了 companyId，将其作为后缀拼接到 billCode 和 billName 中。
        // 这样 ISysSequenceService 会为 "MC_ASSAY_ORDER_COMPANY_A" 和 "MC_ASSAY_ORDER_COMPANY_B"
        // 分别创建和管理序列，从而实现了按公司隔离。
        if (companyId != null && !companyId.trim().isEmpty()) {
            // 例如: "MC_ASSAY_ORDER" + "_" + "COMPANY_A"
            billCode = billCode + "_" + companyId.trim();
            // 例如: "公司化验单" + " (COMPANY_A)"
            billName = billName + " (" + companyId.trim() + ")";
        }

        sequence.setBillCode(billCode);        // 设置动态 bill_code
        sequence.setBillName(billName);        // 设置动态 bill_name
        sequence.setPrefix(config.getPrefix());    // 设置 prefix (例如 "MC")
        sequence.setGenBit(config.getGenBit());    // 设置 genBit (例如 3)

        LocalDate now = LocalDate.now();
        // 修改为2位年份：yyMMdd格式
        sequence.setSeqYear(now.getYear() % 100);          // 取后2位年份
        sequence.setSeqMonth(now.getMonthValue());
        // 修改：PCR样品按月生成，不包含日，格式为 PCR + yyMM + 001
        if (CODE_TYPE_PCR_SAMPLE.equals(config.getBillCode())) {
            sequence.setSeqDay(null); // 设为0，表示按月生成（忽略日）
        } else {
            sequence.setSeqDay(now.getDayOfMonth());
        }
        return sequence;
    }

    /**
     * 编码配置内部类
     */
    private static class CodeConfig {
        private final String billCode;     // 对应数据库bill_code字段
        private final String billName;     // 对应数据库bill_name字段
        private final String prefix;       // 对应数据库sequence_code字段
        private final int genBit;          // 流水号位数

        public CodeConfig(String billCode, String billName, String prefix, int genBit) {
            this.billCode = billCode;
            this.billName = billName;
            this.prefix = prefix;
            this.genBit = genBit;
        }

        public String getBillCode() {
            return billCode;
        }

        public String getBillName() {
            return billName;
        }

        public String getPrefix() {
            return prefix;
        }

        public int getGenBit() {
            return genBit;
        }
    }
}