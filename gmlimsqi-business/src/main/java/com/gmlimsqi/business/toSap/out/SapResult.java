package com.gmlimsqi.business.toSap.out;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SapResult {
    
    private boolean success;
    private String code;
    private String message;
    //    sap凭证
    private String data;
    
}
