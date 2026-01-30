package com.gmlimsqi.business.labtest.mapper;

import com.gmlimsqi.business.labtest.dto.OpSampleReceiveDto;
import com.gmlimsqi.business.labtest.vo.OpSampleReceiveVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OpSampleReceiceMapper {

    public List<OpSampleReceiveVo> selectSampleReceiveList(OpSampleReceiveDto sampleReceiveDto);

    public List<OpSampleReceiveVo> selectSampleReceiveList2(OpSampleReceiveDto sampleReceiveDto);
    public List<OpSampleReceiveVo> selectSampleReceiveListNotReceive(OpSampleReceiveDto sampleReceiveDto);
    public List<OpSampleReceiveVo> selectSampleReceiveListIsReceive(OpSampleReceiveDto sampleReceiveDto);
    public List<OpSampleReceiveVo> selectJhwSampleReceiveListIsReceive(OpSampleReceiveDto sampleReceiveDto);
    public OpSampleReceiveVo selectSampleReceiveByEntrustOrderId(@Param("type") String type, @Param("entrustOrderId")String entrustOrderId);
}
