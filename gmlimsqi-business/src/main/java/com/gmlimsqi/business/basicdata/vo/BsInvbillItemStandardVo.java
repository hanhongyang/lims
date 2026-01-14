package com.gmlimsqi.business.basicdata.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.business.basicdata.domain.BsInvbillItemStandard;
import com.gmlimsqi.business.basicdata.domain.BsLabtestItemFeature;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

import javax.sound.midi.Instrument;
import java.util.Date;
import java.util.List;

/**
 * 物料项目标准VO（分组展示）
 */
@Data
public class BsInvbillItemStandardVo extends BaseEntity {
    
    /** 物料ID */
    private String invbillId;
    
    /** 物料编号 */
    private String invbillCode;
    
    /** 物料名称 */
    private String invbillName;
    
    /** 部门ID */
    private String deptId;

    /** 部门名称 */
    private String deptName;
    
    /** 项目标准列表 */
    private List<BsInvbillItemStandard> standardList;

}