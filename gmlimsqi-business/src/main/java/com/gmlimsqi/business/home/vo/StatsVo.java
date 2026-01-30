package com.gmlimsqi.business.home.vo;

import lombok.Data;

/**
 * 首页统计数据 VO
 * <p>
 * author: yangjw
 * data: 2026-01-05
 */
@Data
public class StatsVo {

    /**
     * 饲料检测统计
     */
    private FeedStats feed;

    /**
     * PCR 检测统计
     */
    private SingleStats pcr;

    /**
     * 疾病检测统计
     */
    private SingleStats disease;

    /**
     * 生化检测统计
     */
    private SingleStats biochemistry;

    /**
     * 早孕检测统计
     */
    private SingleStats earlyPregnancy;

    /**
     * 近红外检测统计
     */
    private NirStats nir;

    /**
     * 饲料检测统计
     */
    @Data
    public static class FeedStats {

        /**
         * 待化验（化学法）
         */
        private Integer waitChemistry;

        /**
         * 待化验（初水分）
         */
        private Integer waitMoisture;

        /**
         * 待提交
         */
        private Integer waitSubmit;

        /**
         * 待校对
         */
        private Integer waitCheck;
    }

    /**
     * 单状态统计（PCR / 疾病 / 生化 / 早孕）
     */
    @Data
    public static class SingleStats {

        /**
         * 待化验
         */
        private Integer waitTest;

        /**
         * 待受理
         */
        private Integer waitAccept;
    }

    /**
     * 近红外统计
     */
    @Data
    public static class NirStats {

        /**
         * 待化验（近红外）
         */
        private Integer waitTest;

        /**
         * 待提交（近红外）
         */
        private Integer waitSubmit;

        /**
         * 待校对（近红外）
         */
        private Integer waitCheck;
    }
}