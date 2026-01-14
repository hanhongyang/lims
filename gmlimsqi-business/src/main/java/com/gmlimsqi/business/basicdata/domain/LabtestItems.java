package com.gmlimsqi.business.basicdata.domain;

import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 检测项目对象 bs_labtest_items
 * 
 * @author hhy
 * @date 2025-08-05
 */
@Data
public class LabtestItems extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String labtestItemsId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;

    /** 项目编码 */
    @Excel(name = "项目编码")
    private String itemCode;

    /** sap项目编码 */
    private String sapCode;

    /** 是否删除（0否1是） */
    private String isDelete;

    /** 是否启用（0否1是） */
    private String isEnable;
    private String userType;


    /** 保留小数位 */
    private String decimalPlaces;

    /** 有效数字位 */
    private String significantDigits;

    /** 定性或定量 （1：定性，2：定量） */
    private String qualitativeOrQuantitative;

    private List<BsItemDept> itemDeptList;
    //标签
    private String tag;

    private List<BsItemInstrument> itemInstrumentList;
    //实验地点
    private String labLocation;
    //实验地点
    private String zxbz;

    //过滤标签
    private String filterTag;
    //是否近红外
    private String isJhw;
    /** 项目部门资源配置列表 (接收前端传参及返回数据) */
    private List<BsItemDeptConfig> bsItemDeptConfigList;
    private Integer sortOrder;

    /** 文件信息 */
    private String fileInfo;
}
