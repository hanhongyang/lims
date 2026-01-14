package com.gmlimsqi.business.disinfection.domain;

import java.io.Serial;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.business.disinfection.emum.DisinfectionPoolConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 消毒池管理对象 op_disinfection_pool
 *
 * @author yangjw
 * @date 2026-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DisinfectionPool extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 消毒池名称
     */
    @Excel(name = "消毒池名称")
    private String poolName;

    /**
     * 所属部门ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deptId;

    /**
     * 消毒池类型
     */
    @Excel(name = "消毒池类型")
    private String poolType;

    /**
     * 容量
     */
    @Excel(name = "容量")
    private BigDecimal capacity;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;

    /**
     * 是否通过，0-否，1-是，确定司机进场的消毒池
     */
    @Excel(name = "是否通过")
    private String passed;

    /**
     * 删除标志
     */
    private String delFlag;

    /**
     * 填充创建信息（插入数据时调用）
     * 自动设置：status, createTime, createBy, updateTime, updateBy
     */
    @Override
    public void fillCreateInfo() {
        this.setStatus(DisinfectionPoolConstant.OPEN); // 默认开启
        super.fillCreateInfo();
    }

}
