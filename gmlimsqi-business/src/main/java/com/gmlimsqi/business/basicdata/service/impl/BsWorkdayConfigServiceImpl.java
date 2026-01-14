package com.gmlimsqi.business.basicdata.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.http.HttpUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.mapper.BsWorkdayConfigMapper;
import com.gmlimsqi.business.basicdata.domain.BsWorkdayConfig;
import com.gmlimsqi.business.basicdata.service.IBsWorkdayConfigService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工作日配置Service业务层处理
 *
 * @author hhy
 * @date 2025-09-09
 */
@Service
public class BsWorkdayConfigServiceImpl implements IBsWorkdayConfigService
{
    private static final Logger logger = LoggerFactory.getLogger(BsWorkdayConfigServiceImpl.class);

    @Autowired
    private BsWorkdayConfigMapper bsWorkdayConfigMapper;

    /**
     * 查询工作日配置
     *
     * @param bsWorkdayConfigId 工作日配置主键
     * @return 工作日配置
     */
    @Override
    public BsWorkdayConfig selectBsWorkdayConfigByBsWorkdayConfigId(String bsWorkdayConfigId)
    {
        return bsWorkdayConfigMapper.selectBsWorkdayConfigByBsWorkdayConfigId(bsWorkdayConfigId);
    }
    /**
     * 计算两个日期之间的工作日天数
     * * @param start Date对象 或 "yyyy-MM-dd" / "yyyyMMdd" 字符串
     * @param end   Date对象 或 "yyyy-MM-dd" / "yyyyMMdd" 字符串
     * @return 工作日数量
     */
    @Override
    public int calculateWorkDays(Date start, Date end) {
        if (start == null || end == null) {
            return 0;
        }

        // 1. 确保开始日期小于等于结束日期，如果反了则交换（可选，看业务需求）
        if (start.after(end)) {
            Date temp = start;
            start = end;
            end = temp;
        }

        // 2. 转换为 yyyyMMdd 格式字符串，对应数据库 date_str 字段
        // 这里使用 Hutool 工具类简化代码，也可以用 SimpleDateFormat
        String startDateStr = DateUtil.format(start, "yyyyMMdd");
        String endDateStr = DateUtil.format(end, "yyyyMMdd");

        // 3. 调用 Mapper 查询
        return bsWorkdayConfigMapper.selectWorkDayCount(startDateStr, endDateStr);
    }

    /**
     * 重载方法：如果输入直接是 yyyyMMdd 字符串
     */
    @Override
    public int calculateWorkDays(String startDateStr, String endDateStr) {
        // 简单的字符串比较，确保 start <= end
        if (startDateStr.compareTo(endDateStr) > 0) {
            String temp = startDateStr;
            startDateStr = endDateStr;
            endDateStr = temp;
        }
        return bsWorkdayConfigMapper.selectWorkDayCount(startDateStr, endDateStr);
    }
    /**
     * 根据日期查询工作日配置
     *
     * @param dateStr 日期字符串
     * @return 工作日配置
     */
    @Override
    public BsWorkdayConfig selectBsWorkdayConfigByDateStr(String dateStr)
    {
        return bsWorkdayConfigMapper.selectBsWorkdayConfigByDateStr(dateStr);
    }

    /**
     * 查询工作日配置列表
     *
     * @param bsWorkdayConfig 工作日配置
     * @return 工作日配置
     */
    @Override
    public List<BsWorkdayConfig> selectBsWorkdayConfigList(BsWorkdayConfig bsWorkdayConfig)
    {
        return bsWorkdayConfigMapper.selectBsWorkdayConfigList(bsWorkdayConfig);
    }

    /**
     * 根据年月查询工作日配置
     *
     * @param year 年份
     * @param month 月份
     * @return 工作日配置列表
     */
    @Override
    public List<BsWorkdayConfig> selectWorkdayConfigByYearMonth(Integer year, Integer month)
    {
        return bsWorkdayConfigMapper.selectWorkdayConfigByYearMonth(year, month);
    }

    /**
     * 新增工作日配置
     *
     * @param bsWorkdayConfig 工作日配置
     * @return 结果
     */
    @Override
    public int insertBsWorkdayConfig(BsWorkdayConfig bsWorkdayConfig)
    {
        if (StringUtils.isEmpty(bsWorkdayConfig.getBsWorkdayConfigId())) {
            bsWorkdayConfig.setBsWorkdayConfigId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        bsWorkdayConfig.fillCreateInfo();
        return bsWorkdayConfigMapper.insertBsWorkdayConfig(bsWorkdayConfig);
    }

    /**
     * 修改工作日配置
     *
     * @param bsWorkdayConfig 工作日配置
     * @return 结果
     */
    @Override
    public int updateBsWorkdayConfig(BsWorkdayConfig bsWorkdayConfig)
    {
        // 自动填充更新信息
        bsWorkdayConfig.fillUpdateInfo();
        return bsWorkdayConfigMapper.updateBsWorkdayConfig(bsWorkdayConfig);
    }

    /**
     * 更新工作日状态
     *
     * @param dateStr 日期字符串
     * @param isWorkday 是否为工作日
     * @param updateBy 更新人
     * @return 结果
     */
    @Override
    public int updateWorkdayStatus(String dateStr, Integer isWorkday, String updateBy)
    {
        return bsWorkdayConfigMapper.updateWorkdayStatus(dateStr, isWorkday, updateBy);
    }

    /**
     * 批量删除工作日配置
     *
     * @param bsWorkdayConfigIds 需要删除的工作日配置主键
     * @return 结果
     */
    @Override
    public int deleteBsWorkdayConfigByBsWorkdayConfigIds(String[] bsWorkdayConfigIds)
    {
        return bsWorkdayConfigMapper.deleteBsWorkdayConfigByBsWorkdayConfigIds(bsWorkdayConfigIds);
    }

    /**
     * 删除工作日配置信息
     *
     * @param bsWorkdayConfigId 工作日配置主键
     * @return 结果
     */
    @Override
    public int deleteBsWorkdayConfigByBsWorkdayConfigId(String bsWorkdayConfigId)
    {
        return bsWorkdayConfigMapper.deleteBsWorkdayConfigByBsWorkdayConfigId(bsWorkdayConfigId);
    }

    /**
     * 检查日期是否唯一
     *
     * @param dateStr 日期字符串
     * @return 结果
     */
    @Override
    public int checkDateStrUnique(String dateStr)
    {
        return bsWorkdayConfigMapper.checkDateStrUnique(dateStr);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int initFromHolidayData(Integer queryYear, String username) {
        // 参数验证
        if (queryYear == null) {
            throw new IllegalArgumentException("年份不能为空");
        }

        int currentYear = LocalDate.now().getYear();
        if (queryYear < currentYear - 10 || queryYear > currentYear + 10) {
            throw new IllegalArgumentException("年份范围必须在" + (currentYear - 10) + "到" + (currentYear + 10) + "之间");
        }

        try {
            // 调用节假日API获取数据
            String apiUrl = "https://api.apihubs.cn/holiday/get?size=500&year=" + queryYear + "&cn=1";
            String response = HttpUtils.sendGet(apiUrl);

            if (StringUtils.isEmpty(response)) {
                throw new RuntimeException("API调用失败，返回空响应");
            }

            // 解析JSON响应
            JSONObject result = JSON.parseObject(response);
            if (!"0".equals(result.getString("code"))) {
                throw new RuntimeException("API调用失败: " + result.getString("msg"));
            }

            JSONObject data = result.getJSONObject("data");
            JSONArray holidayList = data.getJSONArray("list");

            if (holidayList == null || holidayList.isEmpty()) {
                logger.warn("未获取到{}年的节假日数据", queryYear);
                return 0;
            }

            int count = 0;
            List<BsWorkdayConfig> configsToInsert = new ArrayList<>();
            List<BsWorkdayConfig> configsToUpdate = new ArrayList<>();

            // 批量处理节假日数据
            for (int i = 0; i < holidayList.size(); i++) {
                try {
                    JSONObject holiday = holidayList.getJSONObject(i);
                    BsWorkdayConfig config = parseHolidayData(holiday, username);

                    if (config != null) {
                        BsWorkdayConfig existingConfig = bsWorkdayConfigMapper.selectBsWorkdayConfigByDateStr(config.getDateStr());

                        if (existingConfig != null) {
                            // 只更新接口初始化的数据
                            if (existingConfig.getSourceType() == 1) {
                                updateExistingConfig(existingConfig, config, username);
                                configsToUpdate.add(existingConfig);
                            }
                        } else {
                            configsToInsert.add(config);
                        }
                    }
                } catch (Exception e) {
                    logger.error("处理第{}条节假日数据失败", i + 1, e);
                    // 继续处理下一条数据
                }
            }

            // 批量操作
            if (!configsToInsert.isEmpty()) {
                bsWorkdayConfigMapper.batchInsertBsWorkdayConfig(configsToInsert);
                count += configsToInsert.size();
            }

            if (!configsToUpdate.isEmpty()) {
                bsWorkdayConfigMapper.batchUpdateBsWorkdayConfig(configsToUpdate);
                count += configsToUpdate.size();
            }

            return count;

        } catch (Exception e) {
            logger.error("初始化{}年节假日数据失败", queryYear, e);
            throw new RuntimeException("初始化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 解析节假日数据
     */
    private BsWorkdayConfig parseHolidayData(JSONObject holiday, String username) {
        try {
            // 解析日期
            Integer dateValue = holiday.getInteger("date");
            if (dateValue == null) {
                return null;
            }

            String dateStr = String.valueOf(dateValue);
            if (dateStr.length() != 8) {
                return null;
            }

            Integer year = Integer.parseInt(dateStr.substring(0, 4));
            Integer month = Integer.parseInt(dateStr.substring(4, 6));
            Integer day = Integer.parseInt(dateStr.substring(6, 8));

            // 解析关键字段
            Integer workdayValue = holiday.getInteger("workday");
            boolean isWorkday = workdayValue != null && workdayValue == 1;

            Integer holidayTodayValue = holiday.getInteger("holiday_today");
            Integer holidayLegalValue = holiday.getInteger("holiday_legal");
            boolean isHoliday = (holidayTodayValue != null && holidayTodayValue == 1) ||
                    (holidayLegalValue != null && holidayLegalValue == 1);

            Integer holidayOvertimeValue = holiday.getInteger("holiday_overtime");
            boolean isOvertime = holidayOvertimeValue != null && holidayOvertimeValue == 1;

            String holidayName = holiday.getString("holiday_cn");
            Integer weekDay = holiday.getInteger("week");

            // 创建配置对象
            BsWorkdayConfig config = new BsWorkdayConfig();
            config.setBsWorkdayConfigId(IdUtils.simpleUUID());
            config.setDateStr(dateStr);
            config.setYear(year);
            config.setMonth(month);
            config.setDay(day);
            config.setIsWorkday(isWorkday ? 1 : 0);
            config.setIsHoliday(isHoliday ? 1 : 0);
            config.setHolidayName(StringUtils.substring(holidayName, 0, 50)); // 限制长度
            config.setIsOvertime(isOvertime ? 1 : 0);
            config.setWeekDay(weekDay);
            config.setSourceType(1);
            config.setCreateBy(username);
            config.setUpdateBy(username);
            config.setCreateTime(new Date());
            config.setUpdateTime(new Date());

            return config;

        } catch (Exception e) {
            logger.error("解析节假日数据失败: {}", holiday, e);
            return null;
        }
    }

    /**
     * 更新已存在的配置
     */
    private void updateExistingConfig(BsWorkdayConfig existingConfig, BsWorkdayConfig newConfig, String username) {
        existingConfig.setIsWorkday(newConfig.getIsWorkday());
        existingConfig.setIsHoliday(newConfig.getIsHoliday());
        existingConfig.setHolidayName(newConfig.getHolidayName());
        existingConfig.setIsOvertime(newConfig.getIsOvertime());
        existingConfig.setWeekDay(newConfig.getWeekDay());
        existingConfig.setUpdateBy(username);
        existingConfig.setUpdateTime(new Date());
    }

}