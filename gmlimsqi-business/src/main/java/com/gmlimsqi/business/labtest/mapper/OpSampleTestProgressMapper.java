package com.gmlimsqi.business.labtest.mapper;

import com.gmlimsqi.business.labtest.dto.OpSampleReceiveDto;
import com.gmlimsqi.business.labtest.dto.OpSampleTestProgressDto;
import com.gmlimsqi.business.labtest.vo.OpSampleReceiveVo;
import com.gmlimsqi.business.labtest.vo.OpSampleTestProgressVo;

import java.util.List;

public interface OpSampleTestProgressMapper {
    public List<OpSampleTestProgressVo> selectFeedSampleTestProgressList(OpSampleTestProgressDto sampleTestProgressDto);
    public List<OpSampleTestProgressVo> selectBloodSampleTestProgressList(OpSampleTestProgressDto sampleTestProgressDto);
    public List<OpSampleTestProgressVo> selectPcrSampleTestProgressList(OpSampleTestProgressDto sampleTestProgressDto);
}
