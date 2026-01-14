package com.gmlimsqi.business.labtest.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 检测中心化验单模版配置对象 op_jczx_test_model
 * 
 * @author hhy
 * @date 2025-09-29
 */
@Data
public class OpJczxTestModel extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opJczxTestModelId;

    /** 化验单模板编号 */
    private String testModelNo;

    /** 化验单模板名称 */
    private String testModelName;

    /** 物料标签 */
    private String invbillTag;

    /** 项目id */
    private String itemId;
    /** 检测依据 */
    private String testBasis;
    /** 删除id（0为未删除，删除则为id） */
    private String deleteId;

}
