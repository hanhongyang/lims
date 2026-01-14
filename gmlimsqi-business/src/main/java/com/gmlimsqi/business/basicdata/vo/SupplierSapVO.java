package com.gmlimsqi.business.basicdata.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 读取sqlserver数据
 *
 * @author 89304
 * @date 2023/04/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SupplierSapVO {
    
    private String ccustomercode;
    private String ccustomername;
    
    private String type;

    /** 是否为承运商 */
    private String cifcarrier;
}
