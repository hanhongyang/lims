package com.gmlimsqi.business.sequence.service;


import com.gmlimsqi.business.sequence.domain.SysSequence;
import com.gmlimsqi.business.sequence.mapper.SysSequenceMapper;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.StringJoiner;

/**
 * 编号生成器
 *
 * @author niu
 * @date 2021/07/26
 */
@Service
public class SysSequenceServiceImpl implements ISysSequenceService {
    @Autowired
    private SysSequenceMapper sequenceMapper;

    /**
     * 编号生成 乐观锁实现
     *
     * @param sequence
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String genCodeBySequenceWithVersion(SysSequence sequence) {
        SysSequence sysSequence = sequenceMapper.selectBySequence(sequence);

        Integer sequenceValue = 1;
        String delimiter = sequence.getDelimiter();
        if (StringUtils.isEmpty(delimiter)) {
            delimiter = "";
        }
        StringJoiner sb = new StringJoiner(delimiter);
        String prefix = sequence.getPrefix();
        if (StringUtils.isEmpty(prefix)) {
            throw new ServiceException("单据前缀为空");
        }
        StringBuffer middleValue = new StringBuffer();
        // 判断日期是否为空 拼接
        if (sequence.getSeqMonth() != null) {
            middleValue.append(stringZeroFill(sequence.getSeqMonth(), 2));
        }
        if (sequence.getSeqDay() != null) {
            middleValue.append(stringZeroFill(sequence.getSeqDay(), 2));
        }
        if (sequence.getSeqYear() != null) {
            middleValue.append(stringZeroFill(sequence.getSeqYear(), 4));
        }

        if (sysSequence == null) {
            Long revision = 0L;
            sequence.setSequenceValue(sequenceValue);
            sequence.setRevision(revision);
            sequenceMapper.insertSelective(sequence);
        } else {
            sequenceValue = sysSequence.getSequenceValue() + 1;
            sysSequence.setSequenceValue(sequenceValue);
            int i = sequenceMapper.updateSequenceWithVersion(sysSequence);
            if (i < 1) {
                throw new ServiceException("Transfer failed, retry.");
            }
        }
        sb.add(prefix);
        sb.add(middleValue);
        sb.add(stringZeroFill(sequenceValue, sequence.getGenBit()));
        return sb.toString();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String genCodeBySequence(SysSequence sequence) {
        // 获取编号信息（带锁）
        SysSequence sysSequence = sequenceMapper.selectBySequenceForUpdate(sequence);

        Integer sequenceValue = 1;
        String delimiter = sequence.getDelimiter();
        if (StringUtils.isEmpty(delimiter)) {
            delimiter = "";
        }
        StringJoiner sb = new StringJoiner(delimiter);
        String prefix = sequence.getPrefix();
        String suffix = sequence.getSuffix();
        /*if (StringUtils.isEmpty(prefix)) {
            throw new ServiceException("单据前缀为空");
        }*/
        StringBuffer middleValue = new StringBuffer();

        // 日期拼接顺序修正：年-月-日
        if (sequence.getSeqYear() != null) {
            middleValue.append(stringZeroFill(sequence.getSeqYear(), 2));
        }
        if (sequence.getSeqMonth() != null) {
            middleValue.append(stringZeroFill(sequence.getSeqMonth(), 2));
        }
        if (sequence.getSeqDay() != null) {
            middleValue.append(stringZeroFill(sequence.getSeqDay(), 2));
        }

        if (sysSequence == null) {
            // 设置所有必要字段后再插入
            sequence.setSequenceValue(sequenceValue);
            sequence.setRevision(0L);

            // 确保billCode和billName不为空（如果前端没传的话）
            if (StringUtils.isEmpty(sequence.getBillCode())) {
                sequence.setBillCode(sequence.getPrefix()); // 默认使用prefix作为billCode
            }
            if (StringUtils.isEmpty(sequence.getBillName())) {
                sequence.setBillName("自动生成的序列"); // 默认名称
            }

            sequenceMapper.insertSelective(sequence);
        } else {
            sequenceValue = sysSequence.getSequenceValue() + 1;
            sysSequence.setSequenceValue(sequenceValue);
            sequenceMapper.updateSequence(sysSequence);
        }

        if (sequence.getPrefixSpliceFlag()) {
            sb.add(prefix);
        }
        sb.add(middleValue);
        sb.add(stringZeroFill(sequenceValue, sequence.getGenBit()));
        Boolean suffixSpliceFlag = sequence.getSuffixSpliceFlag();
        if (suffixSpliceFlag) {
            sb.add(suffix);
        }
        return sb.toString();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String genCodeBySequenceByStarchstir(SysSequence sequence) {
        // 获取编号信息
        SysSequence sysSequence = sequenceMapper.selectBySequenceForUpdate(sequence);

        Integer sequenceValue = 1;
        String delimiter = sequence.getDelimiter();
        if (StringUtils.isEmpty(delimiter)) {
            delimiter = "";
        }
        StringJoiner sb = new StringJoiner(delimiter);
        String prefix = sequence.getPrefix();
        String suffix = sequence.getSuffix();
        if (StringUtils.isEmpty(prefix)) {
            throw new ServiceException("单据前缀为空");
        }
        StringBuffer middleValue = new StringBuffer();
        // 判断日期是否为空 拼接

        if (sequence.getSeqYear() != null) {
            int fullYear = sequence.getSeqYear();
            int lastTwoDigits = fullYear % 100;
            middleValue.append(stringZeroFill(lastTwoDigits, 2));
        }
        if (sequence.getSeqMonth() != null) {
            middleValue.append(stringZeroFill(sequence.getSeqMonth(), 2));
        }
        if (sequence.getSeqDay() != null) {
            middleValue.append(stringZeroFill(sequence.getSeqDay(), 2));
        }
        if (sysSequence == null) {
            Long revision = 0L;
            sequence.setSequenceValue(sequenceValue);
            sequence.setRevision(revision);
            sequenceMapper.insertSelective(sequence);
        } else {
            sequenceValue = sysSequence.getSequenceValue() + 1;
            sysSequence.setSequenceValue(sequenceValue);
            sequenceMapper.updateSequence(sysSequence);
        }
        if (sequence.getPrefixSpliceFlag()) {
            sb.add(prefix);
        }
        sb.add(middleValue);
        sb.add(stringZeroFill(sequenceValue, sequence.getGenBit()));
        Boolean suffixSpliceFlag = sequence.getSuffixSpliceFlag();
        if (suffixSpliceFlag) {
            sb.add(suffix);
        }
        return sb.toString();
    }

    /**
     * 补零操作
     *
     * @param maxedValue
     * @param length
     * @return
     */
    private String stringZeroFill(Integer maxedValue, Integer length) {
        if (length == null || length < 1) {
            length = 6;
        }
        int valueLength = String.valueOf(maxedValue).length();
        if (valueLength > length) {
            length++;
        }
        int diff = length - valueLength;
        if (diff == 0) {
            return String.valueOf(maxedValue);
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < diff; i++) {
                sb.append("0");
            }
            sb.append(maxedValue);
            return sb.toString();
        }
    }
}
