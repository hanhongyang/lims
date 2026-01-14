package com.gmlimsqi.business.basicdata.domain;

import java.util.List;

import com.gmlimsqi.business.instrument.domain.Instruments;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 检测方法对象 bs_labtest_methods
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
@Data
public class LabtestMethods extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String bsLabtestMethodsId;

    /** 检测项目id */
    private String bsLabtestItemsId;

    /** 检测项目 */
    @Excel(name = "检测项目")
    private String itemName;

    /** 方法名称 */
    @Excel(name = "方法名称")
    private String methodName;

//
//
//    /** 仪器名称 */
//    @Excel(name = "仪器类型")
//    private String instrumentName;
    /** 温度最大值(℃) */
    @Excel(name = "温度最大值(℃)")
    private String temperatureMax;

    /** 温度最小值(℃) */
    @Excel(name = "温度最小值(℃)")
    private String temperatureMin;

    /** 湿度最大值 */
    @Excel(name = "湿度最大值")
    private String humidityMax;

    /** 湿度最小值 */
    @Excel(name = "湿度最小值")
    private String humidityMin;

    /** 是否删除（0否1是） */
    private String isDelete;

    /** 是否启用（0否1是） */
    private String isEnable;


    /** 检测公式信息 */
    private List<LabtestMethodsFormula> labtestMethodsFormulaList;

    /** 检测属性信息 */
    private List<LabtestMethodsAttribute> labtestMethodsAttributeList;

    /** 设备信息 */
    private List<Instruments> instrumentsList;

    /** 设备关联信息 */
    private List<LabtestMethodInstrument> labtestMethodInstruments;

}
