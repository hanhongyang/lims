package com.gmlimsqi.business.ranch.vo;

import com.gmlimsqi.business.ranch.domain.OpSamplingPlanItem;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;
import lombok.Data;

import java.util.List;

// 样品监控VO
@Data
public class OpSamplingPlanSampleMonitorVO extends OpSamplingPlanSample {
    // --- 冗余计划层级的关键信息，方便平铺展示 ---
    private String samplingPlanNo;   // 送检单号
    private String supplierName;     // 供应商
    private String driverName;       // 司机
    private String driverCode;       // 车牌号
    private String samplerName;     // 取样人 (从主表获取)
    private String sampleTime;      // 取样时间 (从主表获取)

    private Integer totalItemCount;      // 总检测项目数
    private Integer finishedItemCount;   // 已检测项目数
    private List<OpSamplingPlanItem> itemList; // 穿透看每个项目的详情

    /** 是否检测完成 (自动判断) */
    public Boolean getIsCompleted() {
        if (totalItemCount == null || finishedItemCount == null) return false;
        return totalItemCount.equals(finishedItemCount);
    }

    /**
     * 综合判定结果 (基于感官和化验项)
     * 1-合格, 2-化验不合格, 3-让步接收, 4-感官不合格
     */
    public String getCheckResult() {
        // 1. 优先判断感官 (0-不合格)
        if ("0".equals(this.getGgQualityResult())) {
            return "4";
        }

        // 2. 如果没有化验项，直接返回合格 (或根据业务逻辑调整)
        if (itemList == null || itemList.isEmpty()) {
            return "1";
        }

        // 3. 判断化验项是否有不合格 (2-不合格)
        boolean hasUnqualified = itemList.stream().anyMatch(item -> "2".equals(item.getCheckResult()));
        if (hasUnqualified) {
            return "2";
        }

        // 4. 判断化验项是否有让步接收 (3-让步接收)
        boolean hasConcession = itemList.stream().anyMatch(item -> "3".equals(item.getCheckResult()));
        if (hasConcession) {
            return "3";
        }

        // 5. 默认合格
        return "1";
    }
}
