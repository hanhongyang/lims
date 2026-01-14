package com.gmlimsqi.business.ranch.domain;

import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 样品化验结果变更日志对象 op_test_result_change_log
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class OpTestResultChangeLog extends BaseEntity
        implements BaseEntity.CreateAware, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（自增）
     */
    private Long id;

    /**
     * test_id / 化验记录ID
     */
    private String resultId;

    /**
     * 原result
     */
    private String originResult;

    /**
     * 原check_result
     */
    private String originCheckResult;

    /**
     * 变更后result
     */
    private String newResult;

    /**
     * 变更后check_result
     */
    private String newCheckResult;

    /**
     * 变更原因
     */
    private String changeReason;

}