package com.gmlimsqi.business.labtest.service;

import com.gmlimsqi.business.labtest.dto.OpSampleTestProgressDto;
import com.gmlimsqi.business.labtest.vo.OpSampleTestProgressVo;

import java.util.List;

public interface IOpSampleTestProgressService {
    List<OpSampleTestProgressVo> selectSampleTestProgressList(OpSampleTestProgressDto sampleTestProgressDto);
}
