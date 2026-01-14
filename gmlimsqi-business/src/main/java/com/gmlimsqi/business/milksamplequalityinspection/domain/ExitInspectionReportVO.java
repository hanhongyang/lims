package com.gmlimsqi.business.milksamplequalityinspection.domain;

import lombok.Data;
import java.util.Date;

/**
 * 牛奶质量检测VO（待销毁列表/质检列表）
 * 对应取样计划-牛奶质检联合查询结果
 */
@Data
public class ExitInspectionReportVO {
    /**
     * 检测时间（日期）
     */
    private Date testTime;

    /**
     * 潮次/车牌号
     */
    private String chaoCi;

    /**
     * 缸号
     */
    private String milkBinName;

    /**
     * 去向
     */
    private String destination;

    /**
     * 数量/总量
     */
    private String quantity;

    /**
     * 温度
     */
    private String samplingMilkTemperature;

    /**
     * 脂肪含量
     */
    private String fatPercent;

    /**
     * 蛋白含量
     */
    private String proteinPercent;

    /**
     * 酒精类型（1=76%，2=80%）
     */
    private String alcoholType;

    /**
     * 酒精值
     */
    private String alcoholValue;

    /**
     * 磷酸盐实验结果
     */
    private String phosphateTest;

    /**
     * 酸度
     */
    private String acidity;

    /**
     * 解抗剂检测结果
     */
    private String antibioticResidue;

    /**
     * 黄曲霉毒素M1(读数≥1.2)
     */
    private String aflatoxinM1;

    /**
     * β-内酰胺检测结果
     */
    private String betaLactam;

    /**
     * 四环素类检测结果
     */
    private String tetracyclines;

    /**
     * 头孢噻呋检测结果
     */
    private String ceftiofur;

    /**
     * 头孢氨苄检测结果
     */
    private String cefalexin;

    /**
     * 氟尼辛检测结果
     */
    private String flunixin;

    /**
     * 美洛昔康检测结果
     */
    private String meloxicam;

    /**
     * 链霉素/双氢链霉素检测结果
     */
    private String streptomycin;

    /**
     * 喹诺酮检测结果
     */
    private String quinolone;

    /**
     * 卡那霉素检测结果
     */
    private String kanamycin;

    /**
     * 莫能菌素检测结果
     */
    private String monensin;

    /**
     * 玫瑰红(＋/-)
     */
    private String roseBengal;

    /**
     * 溴百里香酚蓝检测结果
     */
    private String bromothymolBlue;

    /**
     * 三氯化铁(＋/-)
     */
    private String ferricChloride;

    /**
     * 美兰试验(4小时)合格"√"/不合格"×"
     */
    private String methyleneBlueTest;

    /**
     * 血奶检测结果
     */
    private String bloodMilkTest;

    /**
     * 冰点
     */
    private String freezingPoint;

    /**
     * e50值
     */
    private String e50;

    /**
     * 感官指标
     */
    private String sensoryIndex;

    /**
     * 取样人
     */
    private String sampler;

    /**
     * 检测人
     */
    private String tester;

    /**
     * 公司ID
     */
    private String deptId;
}