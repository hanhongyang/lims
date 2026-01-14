package com.gmlimsqi.business.labtest.domain;

import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * pcr样品委托-项目对应对象 op_pcr_entrust_order_item
 * 
 * @author hhy
 * @date 2025-09-17
 */
@Data
public class OpPcrEntrustOrderItem extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opPcrEntrustOrderItemId;

    /** pcr样品委托单物料表id */
    @Excel(name = "pcr样品委托单物料表id")
    private String pcrEntrustOrderSampleId;

    /** 项目id */
    @Excel(name = "项目id")
    private String itemId;
    private String itemName;

    /** 是否删除 */
    private String isDelete;

    /** 检测结果 */
    private String testResult;

    /** 检测人id */
    private String testUserId;
    /** 检测人 */
    private String testUser;
    /** 提取试剂盒批号 */
    private String tqsjh;

    /** 上传文件id */
    private String fileId;

    /** 扩增试剂盒批号 */
    private String kzsjh;
    /** 样品编号 */
    private String sampleNo;
    /** 所属牧场 */
    @Excel(name = "所属牧场")
    private String deptName;
    /** 序号 */
    @Excel(name = "序号")
    private String sequence;
    /** 样品名称 */
    @Excel(name = "样品名称")
    private String sampleName;
    private String examineUserId;
    private String examineUser;
    private String examineTime;
    private String sortOrder;

    private String resultNo;
}
