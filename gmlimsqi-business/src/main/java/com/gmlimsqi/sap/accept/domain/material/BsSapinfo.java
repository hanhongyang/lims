package com.gmlimsqi.sap.accept.domain.material;

import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * SAP接口配置对象 bs_sapinfo
 *
 * @author EGP
 * @date 2024-03-27
 */
@Data
@Accessors(chain = true)
public class BsSapinfo extends BaseEntity {
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
     * 接口编号
     */
    @Excel(name = "接口编号")
    private String ccode;
    
    /**
     * 接口名称
     */
    @Excel(name = "接口名称")
    private String cname;
    
    /**
     * 密码
     */
    @Excel(name = "密码")
    private String cpwd;
    
    /**
     * 地址
     */
    @Excel(name = "地址")
    private String curl;
    
    /**
     * 账号
     */
    @Excel(name = "账号")
    private String cusername;
    
}
