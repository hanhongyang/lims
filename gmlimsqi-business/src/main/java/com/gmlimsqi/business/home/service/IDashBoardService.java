package com.gmlimsqi.business.home.service;

import com.gmlimsqi.business.home.vo.StatsVo;

public interface IDashBoardService {

    /**
     * 统计各状态数量
     *
     * @return 结果
     */
    public StatsVo getStatistics();
}
