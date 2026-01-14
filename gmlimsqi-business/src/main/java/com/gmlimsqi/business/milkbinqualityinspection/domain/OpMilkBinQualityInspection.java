package com.gmlimsqi.business.milkbinqualityinspection.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 奶仓质检单对象 op_milk_bin_quality_inspection
 * 
 * @author hhy
 * @date 2025-11-10
 */
@Data
public class OpMilkBinQualityInspection extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 化验单号 */
    @Excel(name = "化验单号")
    private String inspectionNumber;

    /** 状态，0：保存，1：审核 */
    @Excel(name = "状态，0：保存，1：审核")
    private String status;

    /** 奶仓id */
    @Excel(name = "奶仓id")
    private String milkBinId;

    /** 奶仓名称(下拉选择) */
    @Excel(name = "奶仓名称(下拉选择)")
    private String milkBinName;

    /** 取样人 */
    @Excel(name = "取样人")
    private String sampler;

    /** 取样时间(默认当前时间) */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "取样时间(默认当前时间)", width = 30, dateFormat = "yyyy-MM-dd")
    private Date samplingTime;

    /** 检测人 */
    @Excel(name = "检测人")
    private String tester;

    /** 检测日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date testDate;

    /** 制单人 */
    @Excel(name = "制单人")
    private String creator;

    /** 制单日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "制单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    /** 附件列表 */
    @Excel(name = "附件列表")
    private String attachmentList;

    /** 附件列表url */
    private List<String> attachmentListUrl;

    /** 脂肪% */
    @Excel(name = "脂肪%")
    private String fatPercent;

    /** 蛋白% */
    @Excel(name = "蛋白%")
    private String proteinPercent;

    /** 76%度酒精2:1(＋/ -) */
    /*@Excel(name = "76%度酒精2:1(＋/ -)")
    private String alcohol76Percent;*/

    /** 80%度酒精2:2(＋/ -) */
    /*@Excel(name = "80%度酒精2:2(＋/ -)")
    private String alcohol80Percent;*/

    /** 磷酸盐试验(＋/-) */
    @Excel(name = "磷酸盐试验(＋/-)")
    private String phosphateTest;

    /** 酸度(°T) */
    @Excel(name = "酸度(°T)")
    private String acidity;

    /** 解抗剂(读数＜0.9) */
    @Excel(name = "解抗剂(读数＜0.9)")
    private String antibioticResidue;

    /** 黄曲霉毒素M1(读数≥1.2) */
    @Excel(name = "黄曲霉毒素M1(读数≥1.2)")
    private String aflatoxinM1;

    /** β-内酰胺 */
    @Excel(name = "β-内酰胺")
    private String betaLactam;

    /** 四环素类 */
    @Excel(name = "四环素类")
    private String tetracyclines;

    /** 头孢噻呋 */
    @Excel(name = "头孢噻呋")
    private String ceftiofur;

    /** 头孢氨苄 */
    @Excel(name = "头孢氨苄")
    private String cefalexin;

    /** 氟尼辛 */
    @Excel(name = "氟尼辛")
    private String flunixin;

    /** 链霉素/双氢链霉素 */
    @Excel(name = "链霉素/双氢链霉素")
    private String streptomycin;

    /** 卡那 */
    @Excel(name = "卡那")
    private String kanamycin;

    /** 莫能菌素 */
    @Excel(name = "莫能菌素")
    private String monensin;

    /** 玫瑰红(＋/-) */
    @Excel(name = "玫瑰红(＋/-)")
    private String roseBengal;

    /** 溴百里香酚蓝(＋/-) */
    @Excel(name = "溴百里香酚蓝(＋/-)")
    private String bromothymolBlue;

    /** 三氯化铁(＋/-) */
    @Excel(name = "三氯化铁(＋/-)")
    private String ferricChloride;

    /** 美兰试验(4小时)合格"√"/不合格"×" */
    @Excel(name = "美兰试验(4小时)合格：√，不合格：×")
    private String methyleneBlueTest;

    /** 血奶检测(合格"√"/不合格"×") */
    @Excel(name = "血奶检测(合格：√，不合格：×)")
    private String bloodMilkTest;

    /** E50 */
    @Excel(name = "E50")
    private String e50;

    /** 感官指标 */
    @Excel(name = "感官指标")
    private String sensoryIndex;

    /** 部门id */
    @Excel(name = "部门id")
    private String deptId;

    /** 审核人 */
    @Excel(name = "审核人")
    private String reviewer;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reviewTime;

    /** 是否删除：0-否 1-是 */
    @Excel(name = "是否删除：0-否 1-是")
    private String isDelete;

    /** 审核人ID */
    private String reviewerId;

    /** 取样人ID */
    private String samplerId;

    /** 检测人id */
    private String testerId;

    // 查询条件

    /** 检测开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date testStartTime;

    /** 检测结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date testEndTime;

    /** 取样开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date samplingStartTime;

     /** 取样结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date samplingEndTime;

    /** 审核开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reviewStartTime;

     /** 审核结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reviewEndTime;

    /** 潮次 */
    @Excel(name = "潮次")
    private String chaoCi;

    /** 数量（吨） */
    @Excel(name = "数量（吨）")
    private String quantity;

    /** 采样奶温（℃） */
    @Excel(name = "采样奶温（℃）")
    private String samplingMilkTemperature;

     /** 喹诺酮 */
    @Excel(name = "喹诺酮")
    private String quinolone;

    /** 冰点 */
    @Excel(name = "冰点")
    private String freezingPoint;

    /** 酒精类型 */
    @Excel(name = "酒精类型")
    private String alcoholType;

    /** 酒精值 */
    @Excel(name = "酒精值")
    private String alcoholValue;

    /** 酒精照片 */
    @Excel(name = "酒精照片")
    private String alcoholPhoto;

    /** 酒精照片URL */
    private String alcoholPhotoUrl;

    /** 美洛昔康 */
    private String meloxicam;

    /**
     * 脂肪质检图片
     */
    private String fatPercentPhoto;

    /**
     * 脂肪质检图片URL
     */
    private String fatPercentPhotoUrl;

    /**
     * 蛋白质检图片
     */
    private String proteinPercentPhoto;

    /**
     * 蛋白质检图片URL
     */
    private String proteinPercentPhotoUrl;

    /**
     * 磷酸盐试验图片
     */
    private String phosphateTestPhoto;

    /**
     * 磷酸盐试验图片URL
     */
    private String phosphateTestPhotoUrl;

    /**
     * 酸度图片
     */
    private String acidityPhoto;

    /**
     * 酸度图片URL
     */
    private String acidityPhotoUrl;

    /**
     * 解抗剂质检图片
     */
    private String antibioticResiduePhoto;

    /**
     * 解抗剂质检图片URL
     */
    private String antibioticResiduePhotoUrl;

    /**
     * 黄曲霉毒素M1质检图片
     */
    private String aflatoxinM1Photo;

    /**
     * 黄曲霉毒素M1质检图片URL
     */
    private String aflatoxinM1PhotoUrl;

    /**
     * β-内酰胺图片
     */
    private String betaLactamPhoto;

    /**
     * β-内酰胺图片URL
     */
    private String betaLactamPhotoUrl;

    /**
     * 四环素类
     */
    private String tetracyclinesPhoto;

    /**
     * 四环素类图片URL
     */
    private String tetracyclinesPhotoUrl;

    /**
     * 头孢噻呋图片
     */
    private String ceftiofurPhoto;

    /**
     * 头孢噻呋图片URL
     */
    private String ceftiofurPhotoUrl;

    /**
     * 头孢氨苄图片
     */
    private String cefalexinPhoto;

    /**
     * 头孢氨苄图片URL
     */
    private String cefalexinPhotoUrl;

    /**
     * 氟尼辛质检图片
     */
    private String flunixinPhoto;

    /**
     * 氟尼辛质检图片URL
     */
    private String flunixinPhotoUrl;

    /**
     * 链霉素/双氢链霉素质检图片
     */
    private String streptomycinPhoto;

    /**
     * 链霉素/双氢链霉素质检图片URL
     */
    private String streptomycinPhotoUrl;

    /**
     * 卡那图片
     */
    private String kanamycinPhoto;

    /**
     * 卡那图片URL
     */
    private String kanamycinPhotoUrl;

    /**
     * 莫能菌素质检图片
     */
    private String monensinPhoto;

    /**
     * 莫能菌素质检图片URL
     */
    private String monensinPhotoUrl;

    /**
     * 玫瑰红质检图片
     */
    private String roseBengalPhoto;

    /**
     * 玫瑰红质检图片URL
     */
    private String roseBengalPhotoUrl;

    /**
     * 溴百里香酚蓝图片
     */
    private String bromothymolBluePhoto;

    /**
     * 溴百里香酚蓝图片URL
     */
    private String bromothymolBluePhotoUrl;

    /**
     * 三氯化铁质检图片
     */
    private String ferricChloridePhoto;

    /**
     * 三氯化铁质检图片URL
     */
    private String ferricChloridePhotoUrl;

    /**
     * 美兰试验质检图片
     */
    private String methyleneBlueTestPhoto;

    /**
     * 美兰试验质检图片URL
     */
    private String methyleneBlueTestPhotoUrl;

    /**
     * 血奶检测质检图片
     */
    private String bloodMilkTestPhoto;

    /**
     * 血奶检测质检图片URL
     */
    private String bloodMilkTestPhotoUrl;

    /**
     * e50质检图片
     */
    private String e50Photo;

    /**
     * e50质检图片URL
     */
    private String e50PhotoUrl;

    /**
     * 感官指标图片
     */
    private String sensoryIndexPhoto;

    /**
     * 感官指标图片URL
     */
    private String sensoryIndexPhotoUrl;

    /**
     * 采样奶温图片
     */
    private String samplingMilkTemperaturePhoto;

    /**
     * 采样奶温图片URL
     */
    private String samplingMilkTemperaturePhotoUrl;

    /**
     * 喹诺酮图片
     */
    private String quinolonePhoto;

    /**
     * 喹诺酮图片URL
     */
    private String quinolonePhotoUrl;

    /**
     * 冰点图片
     */
    private String freezingPointPhoto;

    /**
     * 冰点图片URL
     */
    private String freezingPointPhotoUrl;

    /**
     * 美洛昔康图片
     */
    private String meloxicamPhoto;

    /**
     * 美洛昔康图片URL
     */
    private String meloxicamPhotoUrl;

}
