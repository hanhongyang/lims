package com.gmlimsqi.business.labtest.service;

import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrderSample;
import com.gmlimsqi.business.labtest.dto.OpSampleReceiveDto;
import com.gmlimsqi.business.labtest.dto.ReturnDTO;
import com.gmlimsqi.business.labtest.vo.OpSampleReceiveDetailVo;
import com.gmlimsqi.business.labtest.vo.OpSampleReceiveVo;

import java.util.List;

public interface IOpSampleReceiveService {
    List<OpSampleReceiveVo> selectSampleReceiveList(OpSampleReceiveDto sampleReceiveDto);

    OpSampleReceiveDetailVo selectSampleReceiveByEntrustOrderNo(String type, String entrustOrderNo);

    void deleteSample(String type, String sampleId);

    void add(OpSampleReceiveDto dto);

    /**
     * 退回样品
     * @param dto
     */
    void returnSample(ReturnDTO dto);


}
