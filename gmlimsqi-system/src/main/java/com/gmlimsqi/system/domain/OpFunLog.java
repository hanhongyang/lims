package com.gmlimsqi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;


import java.util.Date;

/**
 * 第三方对接日志接口对象 op_fun_log
 *
 * @author EGP
 * @date 2024-08-24
 */
@Data
public class OpFunLog extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    /** 日志主键 */
    private String funId;
    
    /** 模块标题 */
    @Excel(name = "模块标题")
    private String title;
    
    /** 业务类型（0其它 1新增 2修改 3删除） */
    @Excel(name = "业务类型", readConverterExp = "0=其它,1=新增,2=修改,3=删除")
    private Integer businessType;
    
    /** 方法名称 */
    @Excel(name = "方法名称")
    private String method;
    
    /** 请求方式 */
    @Excel(name = "请求方式")
    private String requestMethod;
    
    /** 操作人员 */
    @Excel(name = "操作人员")
    private String operName;
    
    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;
    
    /** 请求URL */
    @Excel(name = "请求URL")
    private String operUrl;
    
    /** 主机地址 */
    @Excel(name = "主机地址")
    private String operIp;
    
    /** 操作地点 */
    @Excel(name = "操作地点")
    private String operLocation;
    
    /** 操作状态（0正常 1异常） */
    @Excel(name = "操作状态", readConverterExp = "0=正常,1=异常")
    private Integer status;
    
    /** 错误消息 */
    @Excel(name = "错误消息")
    private String errorMsg;
    
    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operTime;
    
    /** 请求参数 */
    @Excel(name = "请求参数")
    private String operParam;
    
    /** 返回参数 */
    @Excel(name = "返回参数")
    private String jsonResult;
    
    /** 功能描述 */
    @Excel(name = "功能描述")
    private String description;
    
    /** 系统名称 */
    @Excel(name = "系统名称")
    private String systemName;
    
    /** 部门id */
    @Excel(name = "部门id")
    private String deptId;
    
    /** 公司id */
    @Excel(name = "公司id")
    private String companyId;
    
    /** 公司名称 */
    @Excel(name = "公司名称")
    private String companyName;
    
    /** 操作人id */
    @Excel(name = "操作人id")
    private String operId;
    
}
