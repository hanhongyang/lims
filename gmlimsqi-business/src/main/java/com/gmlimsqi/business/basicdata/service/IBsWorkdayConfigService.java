package com.gmlimsqi.business.basicdata.service;

import java.util.Date;
import java.util.List;

import com.gmlimsqi.business.basicdata.domain.BsWorkdayConfig;

/**
 * 工作日配置Service接口
 * 
 * @author hhy
 * @date 2025-09-09
 */
public interface IBsWorkdayConfigService 
{
    /**
     * 查询工作日配置
     * 
     * @param bsWorkdayConfigId 工作日配置主键
     * @return 工作日配置
     */
    public BsWorkdayConfig selectBsWorkdayConfigByBsWorkdayConfigId(String bsWorkdayConfigId);
    public int calculateWorkDays(Date start, Date end);
    public int calculateWorkDays(String start, String end);
    /**
     * 查询工作日配置列表
     * 
     * @param bsWorkdayConfig 工作日配置
     * @return 工作日配置集合
     */
    public List<BsWorkdayConfig> selectBsWorkdayConfigList(BsWorkdayConfig bsWorkdayConfig);

    /**
     * 新增工作日配置
     * 
     * @param bsWorkdayConfig 工作日配置
     * @return 结果
     */
    public int insertBsWorkdayConfig(BsWorkdayConfig bsWorkdayConfig);

    /**
     * 修改工作日配置
     * 
     * @param bsWorkdayConfig 工作日配置
     * @return 结果
     */
    public int updateBsWorkdayConfig(BsWorkdayConfig bsWorkdayConfig);

    /**
     * 批量删除工作日配置
     * 
     * @param bsWorkdayConfigIds 需要删除的工作日配置主键集合
     * @return 结果
     */
    public int deleteBsWorkdayConfigByBsWorkdayConfigIds(String[] bsWorkdayConfigIds);

    /**
     * 删除工作日配置信息
     * 
     * @param bsWorkdayConfigId 工作日配置主键
     * @return 结果
     */
    public int deleteBsWorkdayConfigByBsWorkdayConfigId(String bsWorkdayConfigId);

    int updateWorkdayStatus(String dateStr, Integer isWorkday, String username);

    int checkDateStrUnique(String dateStr);

    BsWorkdayConfig selectBsWorkdayConfigByDateStr(String dateStr);

    List<BsWorkdayConfig> selectWorkdayConfigByYearMonth(Integer year, Integer month);
    /**
     * 从节假日数据初始化工作日配置
     *
     * @param year 年份
     * @param username 操作人
     * @return 处理的数据条数
     */
    int initFromHolidayData(Integer year, String username) throws Exception;
}
