package com.gmlimsqi.sap.accept.domain.material;


import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Inv {
    
    @JSONField(name = "MODE")
    private String MODE;
    @JSONField(name = "BEGTM")
    private String BEGTM;
    @JSONField(name = "ENDTM")
    private String ENDTM;
    @JSONField(name = "WERKS")
    private String WERKS;
    @JSONField(name = "RESERVED1")
    private String RESERVED1;
    @JSONField(name = "RESERVED2")
    private String RESERVED2;
    @JSONField(name = "RESERVED3")
    private String RESERVED3;
    @JSONField(name = "RESERVED4")
    private String RESERVED4;
    
}
