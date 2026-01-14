package com.gmlimsqi.sap.log.domain;

import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * SAP接口日志对象 bs_sap_log
 *
 * @author EGP
 * @date 2024-03-27
 */
@Data
@Accessors(chain = true)
public class BsSapLog extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键
     */
    private String id;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dcreatetime;
    
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dmodifytime;
    
    /**
     * 是否删除
     */
    @Excel(name = "是否删除")
    private String bsysdel;
    
    /**
     * 公司编号
     */
    @Excel(name = "公司编号")
    private String cdep;
    
    /**
     * 结果
     */
    @Excel(name = "结果")
    private String cstate;
    
    /**
     * 调用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "调用时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date ddate;
    
    /**
     * 返回值
     */
    @Excel(name = "返回值")
    private String esOutput;
    
    /**
     * 传输值
     */
    @Excel(name = "传输值")
    private String isInput;
    
    /**
     * 接口名称
     */
    @Excel(name = "接口名称")
    private String ccode;
    
    /**
     * 过磅单号
     */
    @Excel(name = "过磅单号")
    private String cweightno;
    
}
