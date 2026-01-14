package com.gmlimsqi.task;

import com.gmlimsqi.business.basicdata.service.IBsWorkdayConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("syncWorkDayTask")
public class SyncWorkDayTask {
    private static final Logger logger = LoggerFactory.getLogger(SyncWorkDayTask.class);

    @Autowired
    private IBsWorkdayConfigService bsWorkdayConfigService;

    /**
     * 同步工作日配置任务
     * 每月1号凌晨1点执行：0 0 1 1 * ?
     */
    public void syncWorkDayConfig()
    {
        try {
            logger.info("开始执行工作日配置同步任务...");

            // 获取当前年份
            int currentYear = LocalDate.now().getYear();

            // 获取系统默认的管理员用户名（根据实际情况调整）
            String username = "system"; // 或者从安全上下文获取

            // 调用初始化方法
            int count = bsWorkdayConfigService.initFromHolidayData(currentYear, username);

            logger.info("工作日配置同步任务执行完成，共处理{}条数据", count);

        } catch (Exception e) {
            logger.error("工作日配置同步任务执行失败", e);
        }
    }
}
