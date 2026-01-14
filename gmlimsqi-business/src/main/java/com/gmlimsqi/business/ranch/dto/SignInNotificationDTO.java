package com.gmlimsqi.business.ranch.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank; // 引入校验

/**
 * 接收“司机签到”通知的 DTO
 * (直接对应 egap 系统发送的 signInInfo 对象)
 */
@Data
public class SignInNotificationDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // --- 这些字段是 LIMS 系统关心的，从 egap 的 BusinessDriverSignIn 复制而来 ---
    
    /** 签到主键id (egap系统的主键) */
    @NotBlank(message = "签到ID不能为空")
    private String id;

    /** 送货单id */
    private String deliverGoodsId;
    
    /** 司机名称 */
    private String driverName;
    
    /** 司机手机号 */
    private String driverPhone;
    
    /** 司机车牌号 */
    @NotBlank(message = "车牌号不能为空")
    private String driverCode;
    
    /** 身份证号 */
    private String idNumber;
    
    /** 签到状态(0未签到,1已签到,2已审核,3已驳回) */
    private String signInStatus;
    
    /** 签到时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signInTime;
    
    /** 目的地编码 (LIMS的牧场编码) */
    @NotBlank(message = "目的地编码不能为空")
    private String destinationCode;
    
    /** 附件信息 */
    private String filePath;
    /** 附件信息 */
    private String fileBase64;
    private String originalFilename;
    /** 0:收货，1:发货，2：淘汰牛 */
    private String status;
    
    /** 场内联系人 */
    private String plantLeader;

    /** 签到编码 */
    private String signInCode;
    
    /** 备注 */
    private String remark;



}