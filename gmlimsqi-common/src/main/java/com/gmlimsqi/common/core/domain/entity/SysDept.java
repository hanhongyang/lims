package com.gmlimsqi.common.core.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 部门表 sys_dept
 *
 * @author ruoyi
 */
@Data
public class SysDept extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deptId;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * sap编码
     */
    private String sapName;

    /**
     * 部门状态:0正常,1停用
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 父部门名称
     */
    private String parentName;
    /**
     * 备注
     */
    private String reamrk;
    //地址
    private String address;
    /**
     * 类型 1-总部 2-牧场 3-饲料场 4-检测中心
     */
    private String deptType;
    /**
     * 子部门
     */
    private List<SysDept> children = new ArrayList<SysDept>();

    /**
     * 通讯方式状态（0正常 1停用）
     */
    private String contactStaus;
    /**
     * 通讯方式备注
     */
    private String contactRemark;
    /**
     * 通讯方式表id
     */
    private String deptContactId;

    /**
     * 邮箱类型
     */
    private String emailType;

    /**
     * 邮箱授权码
     */
    private String emailAuthorizationCode;

    /**
     * 牧场简码
     */
    private String pastureCode;
}
