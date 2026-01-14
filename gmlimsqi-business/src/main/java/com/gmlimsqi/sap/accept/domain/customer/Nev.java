package com.gmlimsqi.sap.accept.domain.customer;


import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Nev {
    
    @JSONField(name = "SRC_SYSTEM")
    private String SRC_SYSTEM;
    @JSONField(name = "DEST_SYSTEM")
    private String DEST_SYSTEM;
    @JSONField(name = "WERKS")
    private String WERKS;
    @JSONField(name = "BPROLE")
    private String BPROLE;
    @JSONField(name = "BEGTM")
    private String BEGTM;
    @JSONField(name = "ENDTM")
    private String ENDTM;
    
}
