package com.gmlimsqi.business.basicdata.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 工作日配置对象 bs_workday_config
 *
 * @author hhy
 * @date 2025-09-09
 */
@Data
public class BsWorkdayConfig extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String bsWorkdayConfigId;

    /** 日期字符串，格式: YYYYMMDD */
    @Excel(name = "日期", width = 30, dateFormat = "yyyyMMdd")
    private String dateStr;

    /** 年份 */
    @Excel(name = "年份")
    private Integer year;

    /** 月份 */
    @Excel(name = "月份")
    private Integer month;

    /** 日 */
    @Excel(name = "日")
    private Integer day;

    /** 是否为工作日: 0-否, 1-是 */
    @Excel(name = "是否工作日", readConverterExp = "0=否,1=是")
    private Integer isWorkday;

    /** 是否为节假日: 0-否, 1-是 */
    @Excel(name = "是否节假日", readConverterExp = "0=否,1=是")
    private Integer isHoliday;

    /** 节假日名称 */
    @Excel(name = "节假日名称")
    private String holidayName;

    /** 是否为调休: 0-否, 1-是 */
    @Excel(name = "是否调休", readConverterExp = "0=否,1=是")
    private Integer isOvertime;

    /** 星期几: 1-周一, 2-周二, ..., 7-周日 */
    @Excel(name = "星期几")
    private Integer weekDay;

    /** 数据来源: 1-接口初始, 2-手动修改 */
    @Excel(name = "数据来源", readConverterExp = "1=接口初始,2=手动修改")
    private Integer sourceType;

    /** 创建者 */
    @Excel(sort = 100, name = "制单人")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(sort = 110, name = "制单时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    private String remark;

    // 星期几中文显示
    public String getWeekDayCn() {
        if (weekDay == null) return "";
        String[] weekDays = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        return weekDays[weekDay];
    }

    // 是否工作日中文显示
    public String getIsWorkdayCn() {
        if (isWorkday == null) return "";
        return isWorkday == 1 ? "是" : "否";
    }

    // 是否节假日中文显示
    public String getIsHolidayCn() {
        if (isHoliday == null) return "";
        return isHoliday == 1 ? "是" : "否";
    }
}