package com.gmlimsqi.business.milksamplequalityinspection.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.change.annotation.ChangeField;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 奶样质检对象 op_milk_sample_quality_inspection
 *
 * @author hhy
 * @date 2025-11-10
 */
@Data
public class OpMilkSampleQualityInspection extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware {
    private static final long serialVersionUID = 1L;

    /**
     * 奶样质检表主键
     */
    @ChangeField(value = "opMilkSampleQualityInspectionId", enabled = false)
    private String opMilkSampleQualityInspectionId;

    /**
     * 奶源计划单号
     */
    @Excel(name = "奶源计划单号")
    @ChangeField(value = "milkSourcePlanOrderNumber", enabled = false)
    private String milkSourcePlanOrderNumber;

    /**
     * 装奶单主键
     */
    @Excel(name = "装奶单主键")
    @ChangeField(value = "opMilkFillingOrderId", enabled = false)
    private String opMilkFillingOrderId;

    /**
     * 奶罐车检查表主键
     */
    @Excel(name = "奶罐车检查表主键")
    @ChangeField(value = "inspectionMilkTankersId", enabled = false)
    private String inspectionMilkTankersId;

    /**
     * 状态，0:待取样  1：取样，2：审核
     */
    @Excel(name = "状态，0：保存，1：审核")
    private String status;

    /**
     * 前置是否完成
     */
    @Excel(name = "前置是否完成")
    private String preStepCompleted;

    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    private String licensePlateNumber;

    /**
     * 奶样质检单号
     */
    @Excel(name = "奶样质检单号")
    private String milkSampleQualityInspectionNumber;

    /**
     * 目的地
     */
    @Excel(name = "目的地")
    private String destination;

    /**
     * 进场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "进场时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date entryTime;

    /**
     * 取样人id
     */
    @Excel(name = "取样人id")
    private String samplerId;

    /**
     * 取样人（默认当前人可修改）
     */
    @Excel(name = "取样人", readConverterExp = "默=认当前人可修改")
    private String sampler;

    /**
     * 取样时间（默认当前时间，待取样时为空）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "取样时间", readConverterExp = "默=认当前时间，待取样时为空")
    private Date samplingTime;

    /**
     * 检测人id
     */
    @Excel(name = "检测人id")
    private String testerId;

    /**
     * 检测人（默认当前人可修改）
     */
    @Excel(name = "检测人", readConverterExp = "默=认当前人可修改")
    private String tester;

    /**
     * 检测日期（待取样时为空）
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测日期", readConverterExp = "待=取样时为空")
    private Date testTime;

    /**
     * 审核人id
     */
    @Excel(name = "审核人id")
    @ChangeField(value = "reviewerId", enabled = false)
    private String reviewerId;

    /**
     * 审核人
     */
    @Excel(name = "审核人")
    private String reviewer;

    /**
     * 审核日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reviewTime;

    /**
     * 奶温（定量，输入数值）
     */
    @Excel(name = "奶温", readConverterExp = "定=量，输入数值")
    private String milkTemperature;

    /**
     * 奶温照片（存储文件路径）
     */
    @Excel(name = "奶温照片", readConverterExp = "存=储文件路径")
    private String milkTempPhoto;

    /**
     * 奶温照片URL
     */
    @Excel(name = "奶温照片URL")
    private String milkTempPhotoUrl;

    /**
     * 附件
     */
    @Excel(name = "附件")
    @ChangeField(value = "file", enabled = false)
    private String file;

    /**
     * 附件路径
     */
    @ChangeField(value = "fileUrl", enabled = false)
    private List<String> fileUrl;

    /**
     * 脂肪%（定量）
     */
    @Excel(name = "脂肪%", readConverterExp = "定=量")
    @ChangeField("fatPercent")
    private String fatPercent;

    /**
     * 脂肪%照片（存储文件路径）
     */
    @Excel(name = "脂肪%照片", readConverterExp = "存=储文件路径")
    @ChangeField(value = "fatPercentPhoto", enabled = false)
    private String fatPercentPhoto;

    /**
     * 脂肪%照片URL
     */
    private String fatPercentPhotoUrl;

    /**
     * 蛋白%（定量）
     */
    @Excel(name = "蛋白%", readConverterExp = "定=量")
    @ChangeField("proteinPercent")
    private String proteinPercent;

    /**
     * 蛋白%照片（存储文件路径）
     */
    @Excel(name = "蛋白%照片", readConverterExp = "存=储文件路径")
    @ChangeField(value = "proteinPercentPhoto", enabled = false)
    private String proteinPercentPhoto;

    /**
     * 蛋白%照片URL
     */
    private String proteinPercentPhotoUrl;

    /**
     * 磷酸盐试验（＋/-，定性）
     */
    @Excel(name = "磷酸盐试验", readConverterExp = "＋=/-，定性")
    @ChangeField("phosphateTest")
    private String phosphateTest;

    /**
     * 酸度(°T)（定量）
     */
    @Excel(name = "酸度(°T)", readConverterExp = "定=量")
    @ChangeField("acidity")
    private String acidity;

    /**
     * 玫瑰红（＋/-，定性）
     */
    @Excel(name = "玫瑰红", readConverterExp = "＋=/-，定性")
    @ChangeField("roseBengal")
    private String roseBengal;

    /**
     * 玫瑰红照片（存储文件路径）
     */
    @Excel(name = "玫瑰红照片", readConverterExp = "存=储文件路径")
    @ChangeField(value = "roseBengalPhoto", enabled = false)
    private String roseBengalPhoto;

    /**
     * 玫瑰红照片URL
     */
    @Excel(name = "玫瑰红照片URL")
    private String roseBengalPhotoUrl;

    /**
     * 溴百里香酚蓝（＋/-，定性）
     */
    @Excel(name = "溴百里香酚蓝", readConverterExp = "＋=/-，定性")
    @ChangeField("bromothymolBlue")
    private String bromothymolBlue;

     /**
     * 溴百里香酚蓝照片（存储文件路径）
     */
    @Excel(name = "溴百里香酚蓝照片", readConverterExp = "存=储文件路径")
    @ChangeField(value = "bromothymolBluePhoto", enabled = false)
    private String bromothymolBluePhoto;

    /**
     * 溴百里香酚蓝照片URL
     */
    private String bromothymolBluePhotoUrl;

    /**
     * 三氯化铁（＋/-，定性）
     */
    @Excel(name = "三氯化铁", readConverterExp = "＋=/-，定性")
    @ChangeField("ferricChloride")
    private String ferricChloride;

     /**
     * 三氯化铁照片（存储文件路径）
     */
    @Excel(name = "三氯化铁照片", readConverterExp = "存=储文件路径")
    @ChangeField(value = "ferricChloridePhoto", enabled = false)
    private String ferricChloridePhoto;

    /**
     * 三氯化铁照片URL
     */
    private String ferricChloridePhotoUrl;

    /**
     * 美兰试验(4小时)（合格"√"/不合格"×"，定性）
     */
    @Excel(name = "美兰试验(4小时)", readConverterExp = "合=格√不合格×，定性")
    @ChangeField("methyleneBlueTest")
    private String methyleneBlueTest;

     /**
     * 美兰试验(4小时)照片（存储文件路径）
     */
    @Excel(name = "美兰试验(4小时)照片", readConverterExp = "存=储文件路径")
    @ChangeField(value = "methyleneBlueTestPhoto", enabled = false)
    private String methyleneBlueTestPhoto;

    /**
     * 美兰试验(4小时)照片URL
     */
    private String methyleneBlueTestPhotoUrl;

    /**
     * 血奶检测（合格"√"/不合格"×"，定性）
     */
    @Excel(name = "血奶检测", readConverterExp = "合=格√不合格×，定性")
    @ChangeField("bloodMilkTest")
    private String bloodMilkTest;

    /**
     * E50（定量）
     */
    @Excel(name = "E50", readConverterExp = "定=量")
    @ChangeField("e50")
    private String e50;

     /**
     * E50照片（存储文件路径）
     */
    @Excel(name = "E50照片", readConverterExp = "存=储文件路径")
    @ChangeField(value = "e50Photo", enabled = false)
    private String e50Photo;

    /**
     * E50照片URL
     */
    private String e50PhotoUrl;

    /**
     * 感官指标（定性，如合格/不合格/优良等）
     */
    @Excel(name = "感官指标", readConverterExp = "定=性，如合格/不合格/优良等")
    @ChangeField("sensoryIndex")
    private String sensoryIndex;

    /**
     * 创建人名称
     */
    @Excel(name = "创建人名称")
    private String createByName;

    /**
     * 部门id
     */
    @Excel(name = "部门id")
    private String deptId;

    /**
     * 是否删除：0-否 1-是
     */
    @Excel(name = "是否删除：0-否 1-是")
    private String isDelete;

    /**
     * 是否推送奶源：0-否 1-是
     */
    private String isPushMilkSource;

    /**
     * 是否取样，0-否 1-是
     */
    @Excel(name = "是否取样，0-否 1-是")
    private String isSampling;

    /**
     * 异常类型
     */
    @Excel(name = "异常类型")
    private String exceptionType;

    /**
     * 异常描述
     */
    @Excel(name = "异常描述")
    private String exceptionDesc;

    /**
     * 异常是否提交：0-否 1-是
     */
    @Excel(name = "异常是否提交：0-否 1-是")
    private String isExceptionSubmit;

    /**
     * 酒精类型
     */
    @Excel(name = "酒精类型")
    @ChangeField("alcoholType")
    private String alcoholType;

    /**
     * 酒精值
     */
    @Excel(name = "酒精值")
    @ChangeField("alcoholValue")
    private String alcoholValue;

    /**
     * 酒精照片
     */
    @Excel(name = "酒精照片")
    @ChangeField(value = "alcoholPhoto", enabled = false)
    private String alcoholPhoto;

    /**
     * 酒精照片URL
     */
    @ChangeField(value = "alcoholPhotoUrl", enabled = false)
    private String alcoholPhotoUrl;

    /**
     * 磷酸盐照片
     */
    @Excel(name = "磷酸盐照片")
    @ChangeField(value = "phosphateTestPhoto", enabled = false)
    private String phosphateTestPhoto;

    /**
     * 磷酸盐照片URL
     */
    private String phosphateTestPhotoUrl;

    /**
     * 血奶检测照片
     */
    @Excel(name = "血奶检测照片")
    @ChangeField(value = "bloodMilkTestPhoto", enabled = false)
    private String bloodMilkTestPhoto;

    /**
     * 血奶检测照片URL
     */
    @ChangeField(value = "bloodMilkTestPhotoUrl", enabled = false)
    private String bloodMilkTestPhotoUrl;

    /**
     * 感官指标照片
     */
    @Excel(name = "感官指标照片")
    @ChangeField(value = "sensoryIndexPhoto", enabled = false)
    private String sensoryIndexPhoto;

    /**
     * 感官指标照片URL
     */
    @ChangeField(value = "sensoryIndexPhotoUrl", enabled = false)
    private String sensoryIndexPhotoUrl;

    /**
     * 黄曲霉毒素M1（＋/-，定性）
     */
    @Excel(name = "黄曲霉毒素M1")
    @ChangeField("aflatoxinM1")
    private String aflatoxinM1;

    /**
     * 黄曲霉毒素M1照片
     */
    @Excel(name = "黄曲霉毒素M1照片")
    @ChangeField(value = "aflatoxinM1Photo", enabled = false)
    private String aflatoxinM1Photo;

    /**
     * 黄曲霉毒素M1照片URL
     */
    private String aflatoxinM1PhotoUrl;

    /**
     * 抗生素照片
     */
    @Excel(name = "抗生素照片")
    @ChangeField(value = "antibioticPhoto", enabled = false)
    private String antibioticPhoto;

    /**
     * 抗生素照片URL
     */
    @ChangeField(value = "antibioticPhotoUrl", enabled = false)
    private String antibioticPhotoUrl;

    /**
     * 酸度照片
     */
    @Excel(name = "酸度照片")
    @ChangeField(value = "acidityPhoto", enabled = false)
    private String acidityPhoto;

    /**
     * 酸度照片URL
     */
    private String acidityPhotoUrl;

    /**
     * β-内酰胺
     */
    @Excel(name = "β-内酰胺")
    @ChangeField("beta_lacbetaLactamtam")
    private String betaLactam;

    /**
     * β-内酰胺照片
     */
    @Excel(name = "β-内酰胺照片")
    @ChangeField(value = "betaLactamPhoto")
    private String betaLactamPhoto;

    /**
     * β-内酰胺照片URL
     */
    private String betaLactamPhotoUrl;

    /**
     * 四环素类
     */
    @ChangeField("tetracyclines")
    private String tetracyclines;

    /**
     * 四环素类照片
     */
    @Excel(name = "四环素类照片")
    @ChangeField(value = "tetracyclinesPhoto")
    private String tetracyclinesPhoto;

    /**
     * 四环素类照片URL
     */
    private String tetracyclinesPhotoUrl;

    /**
     * 头孢噻呋
     */
    @ChangeField("ceftiofur")
    private String ceftiofur;

    /**
     * 头孢噻呋照片
     */
    @Excel(name = "头孢噻呋照片")
    @ChangeField(value = "ceftiofurPhoto")
    private String ceftiofurPhoto;

    /**
     * 头孢噻呋照片URL
     */
    private String ceftiofurPhotoUrl;

    /**
     * 头孢氨苄
     */
    @ChangeField("cefalexin")
    private String cefalexin;

    /**
     * 头孢氨苄照片
     */
    @Excel(name = "头孢氨苄照片")
    @ChangeField(value = "cefalexinPhoto")
    private String cefalexinPhoto;

    /**
     * 头孢氨苄照片URL
     */
    private String cefalexinPhotoUrl;

    /**
     * 喹诺酮
     */
    @ChangeField("quinolone")
    private String quinolone;

    /**
     * 喹诺酮照片
     */
    @Excel(name = "喹诺酮照片")
    @ChangeField(value = "quinolonePhoto")
    private String quinolonePhoto;

    /**
     * 喹诺酮照片URL
     */
    private String quinolonePhotoUrl;

    /**
     * 冰点
     */
    @ChangeField("freezingPoint")
    private String freezingPoint;

    /**
     * 冰点照片
     */
    @Excel(name = "冰点照片")
    @ChangeField(value = "freezingPointPhoto")
    private String freezingPointPhoto;

    /**
     * 冰点照片URL
     */
    private String freezingPointPhotoUrl;

    /**
     * 解抗剂
     */
    @ChangeField("antibioticResidue")
    private String antibioticResidue;

    /**
     * 解抗剂照片
     */
    @Excel(name = "解抗剂照片")
    @ChangeField(value = "antibioticResiduePhoto")
    private String antibioticResiduePhoto;

    /**
     * 解抗剂照片URL
     */
    private String antibioticResiduePhotoUrl;

    /**
     * 氟尼辛
     */
    @ChangeField("flunixin")
    private String flunixin;

    /**
     * 氟尼辛照片
     */
    @Excel(name = "氟尼辛照片")
    @ChangeField(value = "flunixinPhoto")
    private String flunixinPhoto;

    /**
     * 氟尼辛照片URL
     */
    private String flunixinPhotoUrl;

    /**
     * 美洛昔康
     */
    @ChangeField("meloxicam")
    private String meloxicam;

    /**
     * 美洛昔康照片
     */
    @Excel(name = "美洛昔康照片")
    @ChangeField(value = "meloxicamPhoto")
    private String meloxicamPhoto;

    /**
     * 美洛昔康照片URL
     */
    private String meloxicamPhotoUrl;

    /**
     * 链霉素/双氢链霉素
     */
    @ChangeField("streptomycin")
    private String streptomycin;

    /**
     * 链霉素/双氢链霉素照片
     */
    @Excel(name = "链霉素/双氢链霉素照片")
    @ChangeField(value = "streptomycinPhoto")
    private String streptomycinPhoto;

    /**
     * 链霉素/双氢链霉素照片URL
     */
    private String streptomycinPhotoUrl;

    /**
     * 卡那
     */
    @ChangeField("kanamycin")
    private String kanamycin;

    /**
     * 卡那照片
     */
    @Excel(name = "卡那照片")
    @ChangeField(value = "kanamycinPhoto")
    private String kanamycinPhoto;

    /**
     * 卡那照片URL
     */
    private String kanamycinPhotoUrl;

    /**
     * 莫能菌素
     */
    @ChangeField("monensin")
    private String monensin;

    /**
     * 莫能菌素照片
     */
    @Excel(name = "莫能菌素照片")
    @ChangeField(value = "monensinPhoto")
    private String monensinPhoto;

    /**
     * 莫能菌素照片URL
     */
    private String monensinPhotoUrl;

    /**
     * 乳中吡虫啉
     */
    @ChangeField("imidacloprid")
    private String imidacloprid;

    /**
     * 乳中吡虫啉照片
     */
    @Excel(name = "乳中吡虫啉照片")
    @ChangeField(value = "imidaclopridPhoto")
    private String imidaclopridPhoto;

    /**
     * 乳中吡虫啉照片URL
     */
    private String imidaclopridPhotoUrl;

    /**
     * 乳中啶虫脒
     */
    @ChangeField("acetamiprid")
    private String acetamiprid;

    /**
     * 乳中啶虫脒照片
     */
    @Excel(name = "乳中啶虫脒照片")
    @ChangeField(value = "acetamipridPhoto")
    private String acetamipridPhoto;

    /**
     * 乳中啶虫脒照片URL
     */
    private String acetamipridPhotoUrl;

    /**
     * 是否手动添加，0-否，1-是
     */
    private String isManuallyAdd;
}
