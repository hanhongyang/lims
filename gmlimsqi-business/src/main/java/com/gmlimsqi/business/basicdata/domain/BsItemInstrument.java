package com.gmlimsqi.business.basicdata.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 项目使用设备对象 bs_item_instrument
 * 
 * @author hhy
 * @date 2025-09-29
 */
@Data
public class BsItemInstrument extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String bsItemInstrumentId;

    /** 设备id */
    private String instrumentId;
    /**  */
    private String instrumentName;
    /**  */
    private String instrumentCode;
    /** 检测项目id */
    private String itemId;

    /** 是否删除 */
    private String isDelete;

}
