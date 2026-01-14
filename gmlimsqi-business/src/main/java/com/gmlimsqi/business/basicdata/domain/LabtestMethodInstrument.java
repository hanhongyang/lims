package com.gmlimsqi.business.basicdata.domain;

import lombok.Data;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 检测方法设备对应对象 bs_labtest_method_instrument
 * 
 * @author hhy
 * @date 2025-08-07
 */
@Data
public class LabtestMethodInstrument extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String labtestMethodInstrumentId;

    /** 检测方法id */
    private String labtestMethodsId;

    /** 设备id */
    private String instrumentsId;

    /** 是否删除 */
    private String isDelete;


}
