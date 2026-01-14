package com.gmlimsqi.sap.accept.domain.material;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * sap同步时间对象 base_sapup
 *
 * @author EGP
 * @date 2024-03-27
 */
@Data
@Accessors(chain = true)
public class BaseSapup extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    /**
     * $column.columnComment
     */
    private String id;
    
    /**
     * $column.columnComment
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date dcreatetime;
    
    /**
     * $column.columnComment
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date dmodifytime;
    
    /**
     * $column.columnComment
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String bsysdel;
    
    /**
     * 物料同步时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "物料同步时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date inv;
    
    /**
     * 客户同步时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "客户同步时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date cus;
    
    /**
     * 供应商同步时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "供应商同步时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date ven;
    
    /**
     * $column.columnComment
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String cztcode;
    
}
