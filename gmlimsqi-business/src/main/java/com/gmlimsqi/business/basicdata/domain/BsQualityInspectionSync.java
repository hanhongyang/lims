package com.gmlimsqi.business.basicdata.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 质检信息同步对象 bs_quality_inspection_sync
 * 
 * @author hhy
 * @date 2025-11-20
 */
@Data
public class BsQualityInspectionSync extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 质检同步表主键 */
    private String id;

    /** 订单编号（计划单号） */
    @Excel(name = "订单编号", readConverterExp = "计=划单号")
    private String orderNumber;

    /** 工厂编码 */
    @Excel(name = "工厂编码")
    private String factoryCode;

    /** 工厂简称 */
    @Excel(name = "工厂简称")
    private String factoryName;

    /** 实际奶量 */
    @Excel(name = "实际奶量")
    private String actualMilk;

    /** 卸车完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "卸车完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date unloadTime;

    /** 离开牧场所时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "离开牧场所时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date leavePastureTime;

    /** 到达工厂时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "到达工厂时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date arriveFactoryTime;

    /** 进场奶温 */
    @Excel(name = "进场奶温")
    private String unloadTemperature;

    /** 脂肪 */
    @Excel(name = "脂肪")
    private String testingFat;

    /** 蛋白 */
    @Excel(name = "蛋白")
    private String testingProtein;

    /** 菌落总数 */
    @Excel(name = "菌落总数")
    private String testingColonies;

    /** 体细胞 */
    @Excel(name = "体细胞")
    private String testingSomaticCell;

    /** 嗜冷菌 */
    @Excel(name = "嗜冷菌")
    private String testingBacteria;

    /** 需氧芽孢 */
    @Excel(name = "需氧芽孢")
    private String zxyyb;

    /** 耐热需氧芽孢 */
    @Excel(name = "耐热需氧芽孢")
    private String znrxyyb;

    /** 嗜热需氧芽孢 */
    @Excel(name = "嗜热需氧芽孢")
    private String zsrxyyb;

    /** 芽孢总数 */
    @Excel(name = "芽孢总数")
    private String testingSpore;

    /** 黄曲霉毒素M1 */
    @Excel(name = "黄曲霉毒素M1")
    private String testingAflatoxin;

    /** 酸度 */
    @Excel(name = "酸度")
    private String testingAcidity;

    /** 煮沸后酸度差 */
    @Excel(name = "煮沸后酸度差")
    private String testingAcidDifference;

    /** 冰点 */
    @Excel(name = "冰点")
    private String testingIce;

    /** 杂质度 */
    @Excel(name = "杂质度")
    private String testingImpurity;

    /** 感官评级 */
    @Excel(name = "感官评级")
    private String testingSensoryRating;

}
