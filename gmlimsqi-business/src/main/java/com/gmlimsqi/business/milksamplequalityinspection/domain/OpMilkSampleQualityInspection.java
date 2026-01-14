package com.gmlimsqi.business.milksamplequalityinspection.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class OpMilkSampleQualityInspection extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 奶样质检表主键 */
    private String opMilkSampleQualityInspectionId;

    /** 奶源计划单号 */
    @Excel(name = "奶源计划单号")
    private String milkSourcePlanOrderNumber;

    /** 装奶单主键 */
    @Excel(name = "装奶单主键")
    private String opMilkFillingOrderId;

    /** 奶罐车检查表主键 */
    @Excel(name = "奶罐车检查表主键")
    private String inspectionMilkTankersId;

    /** 状态，0：保存，1：审核 */
    @Excel(name = "状态，0：保存，1：审核")
    private String status;

    /** 前置是否完成 */
    @Excel(name = "前置是否完成")
    private String preStepCompleted;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String licensePlateNumber;

    /** 奶样质检单号 */
    @Excel(name = "奶样质检单号")
    private String milkSampleQualityInspectionNumber;

    /** 目的地 */
    @Excel(name = "目的地")
    private String destination;

    /** 进场时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "进场时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date entryTime;

    /** 取样人id */
    @Excel(name = "取样人id")
    private String samplerId;

    /** 取样人（默认当前人可修改） */
    @Excel(name = "取样人", readConverterExp = "默=认当前人可修改")
    private String sampler;

    /** 取样时间（默认当前时间，待取样时为空） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "取样时间", readConverterExp = "默=认当前时间，待取样时为空")
    private Date samplingTime;

    /** 检测人id */
    @Excel(name = "检测人id")
    private String testerId;

    /** 检测人（默认当前人可修改） */
    @Excel(name = "检测人", readConverterExp = "默=认当前人可修改")
    private String tester;

    /** 检测日期（待取样时为空） */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测日期", readConverterExp = "待=取样时为空")
    private Date testTime;

    /** 审核人id */
    @Excel(name = "审核人id")
    private String reviewerId;

    /** 审核人 */
    @Excel(name = "审核人")
    private String reviewer;

    /** 审核日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reviewTime;

    /** 奶温（定量，输入数值） */
    @Excel(name = "奶温", readConverterExp = "定=量，输入数值")
    private String milkTemperature;

    /** 奶温照片（存储文件路径） */
    @Excel(name = "奶温照片", readConverterExp = "存=储文件路径")
    private String milkTempPhoto;

    /** 奶温照片URL */
    @Excel(name = "奶温照片URL")
    private String milkTempPhotoUrl;

    /** 附件 */
    @Excel(name = "附件")
    private String file;

    /** 附件路径 */
    private List<String> fileUrl;

    /** 脂肪%（定量） */
    @Excel(name = "脂肪%", readConverterExp = "定=量")
    private String fatPercent;

    /** 蛋白%（定量） */
    @Excel(name = "蛋白%", readConverterExp = "定=量")
    private String proteinPercent;

    /** 76%度酒精2:1（＋/ -，定性） */
    /*@Excel(name = "76%度酒精2:1", readConverterExp = "＋=/,-=，定性")
    private String alcohol76Percent;*/

    /** 80%度酒精2:2（＋/ -，定性） */
    /*@Excel(name = "80%度酒精2:2", readConverterExp = "＋=/,-=，定性")
    private String alcohol80Percent;*/

    /** 磷酸盐试验（＋/-，定性） */
    @Excel(name = "磷酸盐试验", readConverterExp = "＋=/-，定性")
    private String phosphateTest;

    /** 酸度(°T)（定量） */
    @Excel(name = "酸度(°T)", readConverterExp = "定=量")
    private String acidity;

    /** 玫瑰红（＋/-，定性） */
    @Excel(name = "玫瑰红", readConverterExp = "＋=/-，定性")
    private String roseBengal;

    /** 溴百里香酚蓝（＋/-，定性） */
    @Excel(name = "溴百里香酚蓝", readConverterExp = "＋=/-，定性")
    private String bromothymolBlue;

    /** 三氯化铁（＋/-，定性） */
    @Excel(name = "三氯化铁", readConverterExp = "＋=/-，定性")
    private String ferricChloride;

    /** 美兰试验(4小时)（合格"√"/不合格"×"，定性） */
    @Excel(name = "美兰试验(4小时)", readConverterExp = "合=格√不合格×，定性")
    private String methyleneBlueTest;

    /** 血奶检测（合格"√"/不合格"×"，定性） */
    @Excel(name = "血奶检测", readConverterExp = "合=格√不合格×，定性")
    private String bloodMilkTest;

    /** E50（定量） */
    @Excel(name = "E50", readConverterExp = "定=量")
    private String e50;

    /** 感官指标（定性，如合格/不合格/优良等） */
    @Excel(name = "感官指标", readConverterExp = "定=性，如合格/不合格/优良等")
    private String sensoryIndex;

    /** 创建人名称 */
    @Excel(name = "创建人名称")
    private String createByName;

    /** 部门id */
    @Excel(name = "部门id")
    private String deptId;

    /** 是否删除：0-否 1-是 */
    @Excel(name = "是否删除：0-否 1-是")
    private String isDelete;

    /** 是否推送奶源：0-否 1-是 */
    private String isPushMilkSource;

    /** 是否取样，0-否 1-是 */
    @Excel(name = "是否取样，0-否 1-是")
    private String isSampling;

    /** 异常类型 */
    @Excel(name = "异常类型")
    private String exceptionType;

    /** 异常描述 */
    @Excel(name = "异常描述")
    private String exceptionDesc;

    /** 异常是否提交：0-否 1-是 */
    @Excel(name = "异常是否提交：0-否 1-是")
    private String isExceptionSubmit;

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

    /** 磷酸盐照片 */
    @Excel(name = "磷酸盐照片")
    private String phosphatePhoto;

    /** 磷酸盐照片URL */
    private String phosphatePhotoUrl;

    /** 血奶检测照片 */
    @Excel(name = "血奶检测照片")
    private String bloodMilkTestPhoto;

     /** 血奶检测照片URL */
    private String bloodMilkTestPhotoUrl;

    /** 感官指标照片 */
    @Excel(name = "感官指标照片")
    private String sensoryIndexPhoto;

     /** 感官指标照片URL */
    private String sensoryIndexPhotoUrl;

    /** 黄曲霉毒素M1（＋/-，定性） */
    @Excel(name = "黄曲霉毒素M1")
    private String aflatoxinM1;

    /** 黄曲霉毒素M1照片 */
    @Excel(name = "黄曲霉毒素M1照片")
    private String aflatoxinPhoto;

    /** 黄曲霉毒素M1照片URL */
    private String aflatoxinPhotoUrl;

    /** 抗生素照片 */
    @Excel(name = "抗生素照片")
    private String antibioticPhoto;

    /** 抗生素照片URL */
    private String antibioticPhotoUrl;

    /** 酸度照片 */
    @Excel(name = "酸度照片")
    private String acidityPhoto;

    /** 酸度照片URL */
    private String acidityPhotoUrl;

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
    private String cephalexin;

    /** 喹诺酮 */
    @Excel(name = "喹诺酮")
    private String quinolone;

    /** 冰点 */
    @Excel(name = "冰点")
    private String freezingPoint;

    /** 解抗剂 */
    private String antibioticResidue;

    /** 氟尼辛 */
    private String flunixin;

    /** 美洛昔康 */
    private String meloxicam;

    /** 链霉素/双氢链霉素 */
    private String streptomycin;

    /** 卡那 */
    private String kanamycin;

    /** 莫能菌素 */
    private String monensin;
}

