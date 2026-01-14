package com.gmlimsqi.business.basicdata.mapper;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsWorkdayConfig;
import org.apache.ibatis.annotations.Param;

/**
 * 工作日配置Mapper接口
 * 
 * @author hhy
 * @date 2025-09-09
 */
public interface BsWorkdayConfigMapper 
{
    /**
     * 查询工作日配置
     * 
     * @param bsWorkdayConfigId 工作日配置主键
     * @return 工作日配置
     */
    public BsWorkdayConfig selectBsWorkdayConfigByBsWorkdayConfigId(String bsWorkdayConfigId);
    /**
     * 统计指定日期范围内的有效工作日数量（包含开始和结束日期）
     *
     * @param startDate 开始日期 (格式: yyyyMMdd)
     * @param endDate   结束日期 (格式: yyyyMMdd)
     * @return 工作日天数
     */
    int selectWorkDayCount(@Param("startDate") String startDate, @Param("endDate") String endDate);

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
     * 通过工作日配置主键更新删除标志
     *
     * @param bsWorkdayConfigId 工作日配置ID
     * @return 结果
     */
    public int updateDeleteFlagById(String bsWorkdayConfigId);

    /**
     * 批量通过工作日配置主键更新删除标志
     *
     * @param bsWorkdayConfigId 工作日配置ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] bsWorkdayConfigIds);

    /**
     * 删除工作日配置
     * 
     * @param bsWorkdayConfigId 工作日配置主键
     * @return 结果
     */
    public int deleteBsWorkdayConfigByBsWorkdayConfigId(String bsWorkdayConfigId);

    /**
     * 批量删除工作日配置
     * 
     * @param bsWorkdayConfigIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBsWorkdayConfigByBsWorkdayConfigIds(String[] bsWorkdayConfigIds);

    int checkDateStrUnique(@Param("dateStr")String dateStr);

    int updateWorkdayStatus(@Param("dateStr")String dateStr, @Param("isWorkday")Integer isWorkday, @Param("updateBy")String updateBy);

    List<BsWorkdayConfig> selectWorkdayConfigByYearMonth( @Param("year") Integer year,
                                                          @Param("month") Integer month);

    BsWorkdayConfig selectBsWorkdayConfigByDateStr(@Param("dateStr")String dateStr);

    /**
     * 批量插入工作日配置
     * @param configList 配置列表
     * @return 插入条数
     */
    int batchInsertBsWorkdayConfig(List<BsWorkdayConfig> configList);

    /**
     * 批量更新工作日配置
     * @param configList 配置列表
     * @return 更新条数
     */
    int batchUpdateBsWorkdayConfig(List<BsWorkdayConfig> configList);
}
