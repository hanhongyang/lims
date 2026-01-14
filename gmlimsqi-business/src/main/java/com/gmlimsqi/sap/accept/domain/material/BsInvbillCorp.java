package com.gmlimsqi.sap.accept.domain.material;

import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 物料档案牧场信息对象 bs_invbill_corp
 *
 * @author EGP
 * @date 2024-02-26
 */
@Data
@Accessors(chain = true)
public class BsInvbillCorp extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    /**
     * $column.columnComment
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long bsInvbillCorpAutoid;
    
    /**
     * $column.columnComment
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String bsInvbillCorpId;
    
    /**
     * $column.columnComment
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long bsInvbillCorpSortno;
    
    /**
     * 牧场名称
     */
    @Excel(name = "牧场名称")
    private String ccorpnameCorp;
    
    /**
     * 牧场编号
     */
    @Excel(name = "牧场编号")
    private String ccorpcodeCorp;
    
    private String pCztCode;
    
}
