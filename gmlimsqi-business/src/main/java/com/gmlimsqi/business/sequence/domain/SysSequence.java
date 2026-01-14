package com.gmlimsqi.business.sequence.domain;


import lombok.*;

/**
 * id生成器
 * @author Niu
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Data
public class SysSequence {
    private Long sequenceId;
    /**
     * 乐观锁
     */
    private Long revision;
    /**
     * 单据名
     */
    private String tableName;
    /**
     * 前缀
     */
    private String prefix;
    /**
     * 是否拼接前缀
     */
    private Boolean prefixSpliceFlag=true;

    /**
     * 后缀
     */
    private String suffix;
    /**
     * 是否拼接后缀
     */
    private Boolean suffixSpliceFlag=false;


    /**
     * 年
     */
    private Integer seqYear;

    /**
     * 月
     */
    private Integer seqMonth;

    /**
     * 日
     */
    private Integer seqDay;



    /**
     * 值
     */
    private Integer sequenceValue;
    /**
     * 分隔符
     */
    private String delimiter;
    /**
     * 流水位数
     */
    private Integer genBit;
    /**
     * 单据编码
     */
    private String billCode;
    /**
     * 单据名称
     */
    private String billName;
}
