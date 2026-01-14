package com.gmlimsqi.business.labtest.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 检测中心pce化验单子对象 op_jczx_pcr_result_info
 * 
 * @author hhy
 * @date 2025-10-13
 */
@Data
public class OpJczxPcrResultInfo extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opJczxPcrResultInfoId;

    /** 主表id */
    @Excel(name = "主表id")
    private String baseId;

    /** 所属牧场 */
    @Excel(name = "所属牧场")
    private String deptName;

    /** 样品编号 */
    @Excel(name = "样品编号")
    private String sampleNo;
    /** 样品名称 */
    @Excel(name = "样品名称")
    private String sampleName;
    /** 委托单样品表id */
    private String pcrEntrustOrderSampleId;
    /** 提取试剂盒批号 */
    @Excel(name = "提取试剂盒批号")
    private String tqsjh;

    /** 上传文件id */
    @Excel(name = "上传文件id")
    private String fileId;

    /** 扩增试剂盒批号 */
    @Excel(name = "扩增试剂盒批号")
    private String kzsjh;
    /** 项目表id */
    private String itemId;

    /** 委托单项目表id */
    private String pcrEntrustOrderItemId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;

    /** 检测结果 */
    @Excel(name = "检测结果")
    private String testResult;

    /** 序号 */
    @Excel(name = "序号")
    private String sequence;

    /** 删除id（0为未删除，删除则为id） */
    private String deleteId;
    private String invbillName;

}
