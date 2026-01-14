package com.gmlimsqi.business.sequence.mapper;



import com.gmlimsqi.business.sequence.domain.SysSequence;

import java.math.BigDecimal;

public interface SysSequenceMapper {

    SysSequence selectBySequence(SysSequence sequence);
    SysSequence selectBySequenceForUpdate(SysSequence sequence);
    int insertSelective(SysSequence sequence);
    int updateSequenceWithVersion(SysSequence sequence);
    int updateSequence(SysSequence sequence);
    BigDecimal getTotalSequence();
}
