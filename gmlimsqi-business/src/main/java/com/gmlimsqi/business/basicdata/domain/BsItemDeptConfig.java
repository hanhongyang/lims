package com.gmlimsqi.business.basicdata.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 项目部门资源配置对象 bs_item_dept_config
 * * @author hhy
 * @date 2025-01-01
 */
@Data
public class BsItemDeptConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String configId;

    /** 检测项目ID */
    private String itemId;

    /** 部门ID */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String deptId;

    /** 关联设备ID (逗号分隔字符串) */
    private String instrumentId;

    /** 关联地点ID (逗号分隔字符串) */
    private String locationId;

    /** 关联设备名称 (逗号分隔字符串) */
    private String instrumentName;

    /** 关联设备编码 (逗号分隔字符串) - 新增字段 */
    private String instrumentCode;

    /** 关联地点名称 (逗号分隔字符串) */
    private String locationName;

    /** 是否删除 */
    private String isDelete;

    /* --- 以下为辅助字段，用于查询回显 --- */

    /** 部门名称 (需要关联 sys_dept 表查询) */
    private String deptName;
}