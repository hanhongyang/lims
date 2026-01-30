package com.gmlimsqi.business.home.service.impl;

import com.gmlimsqi.business.home.service.IDashBoardService;
import com.gmlimsqi.business.home.vo.StatsVo;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedResultBaseDto;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.service.IOpJczxFeedResultBaseService;
import com.gmlimsqi.business.labtest.service.IOpJczxTestTaskService;
import com.gmlimsqi.business.labtest.vo.OpJczxTestTaskVo;
import com.gmlimsqi.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements IDashBoardService {

    private final IOpJczxTestTaskService opJczxTestTaskService;

    private final IOpJczxFeedResultBaseService iOpJczxFeedResultBaseService;

    @Override
    public StatsVo getStatistics() {
        StatsVo stats = new StatsVo();
        // ==================== 1. 饲料检测统计 ====================
        StatsVo.FeedStats feedStats = getFeedStats();
        stats.setFeed(feedStats);

        // ==================== 2. PCR 检测统计 ====================
        StatsVo.SingleStats pcrStats = new StatsVo.SingleStats();
        // PCR待测试项目数
        pcrStats.setWaitTest(
                opJczxTestTaskService.countPcrWaitTest()
        );
        //PCR待受理数
        pcrStats.setWaitAccept(
                opJczxTestTaskService.countPcrWaitAccept()
        );
        stats.setPcr(pcrStats);

        // ==================== 3. 疾病检测统计 ====================
        StatsVo.SingleStats diseaseStats = new StatsVo.SingleStats();
        // 疾病待测试项目数
        diseaseStats.setWaitTest(
                opJczxTestTaskService.countDiseaseWaitTest()
        );
        //疾病待受理数
        diseaseStats.setWaitAccept(
                opJczxTestTaskService.countDiseaseWaitAccept()
        );
        stats.setDisease(diseaseStats);

        // ==================== 4. 生化检测统计 ====================
        StatsVo.SingleStats biochemistryStats = new StatsVo.SingleStats();
        // 生化待测试项目数
        biochemistryStats.setWaitTest(
                opJczxTestTaskService.countBiochemistryWaitTest()
        );
        //生化待受理数
        biochemistryStats.setWaitAccept(
                opJczxTestTaskService.countBiochemistryWaitAccept()
        );
        stats.setBiochemistry(biochemistryStats);

        // ==================== 5. 早孕检测统计 ====================
        StatsVo.SingleStats earlyPregnancyStats = new StatsVo.SingleStats();
        // 早孕待测试项目数
        earlyPregnancyStats.setWaitTest(
                opJczxTestTaskService.countEarlyPregnancyWaitTest()
        );
        //早孕待受理数
        earlyPregnancyStats.setWaitAccept(
                opJczxTestTaskService.countEarlyPregnancyWaitAccept()
        );
        stats.setEarlyPregnancy(earlyPregnancyStats);

        // ==================== 6. 近红外统计 ====================
        StatsVo.NirStats nirStats = getNirStats();
        stats.setNir(nirStats);

        return stats;
    }

    /**
     * 获取近红外统计
     *
     * @return 统计信息
     */
    private StatsVo.NirStats getNirStats() {
        // 0. 准备条件
        StatsVo.NirStats nirStats = new StatsVo.NirStats();

        // 1. 查询待测项目
        nirStats.setWaitTest(
                opJczxTestTaskService.selectJhwTaskList(new OpJczxTestTaskDto()).size()
        );
        // 2. 查询待提交项目
        OpJczxFeedResultBaseDto dto = new OpJczxFeedResultBaseDto();
        dto.setStatusList("1,6");
        nirStats.setWaitSubmit(
                iOpJczxFeedResultBaseService.selectJhwListList(dto).size()
        );
        // 3. 查询待审核项目
        dto = new OpJczxFeedResultBaseDto();
        dto.setStatus("2");
        nirStats.setWaitCheck(
                iOpJczxFeedResultBaseService.selectJhwListList(dto).size()
        );
        // 4. 查询待审核项目
        return nirStats;
    }

    /**
     * 获取饲料检测统计
     *
     * @return 统计信息
     */
    private StatsVo.FeedStats getFeedStats() {
        StatsVo.FeedStats feedStats = new StatsVo.FeedStats();
        // 1 待化验（化学法）
        feedStats.setWaitChemistry(
                opJczxTestTaskService.countFeedWaitTestChemistryByCurrentUser()
        );
        // 2 待化验（出水分）
        feedStats.setWaitMoisture(
                opJczxTestTaskService.countFeedWaitTestCsfByCurrentUser()
        );
        // 3 待提交
        feedStats.setWaitSubmit(iOpJczxFeedResultBaseService.countFeedWaitSubmitByCurrentUser());
        // 4 待校对
        feedStats.setWaitCheck(iOpJczxFeedResultBaseService.countFeedWaitCheckByCurrentUser());
        return feedStats;
    }
}
