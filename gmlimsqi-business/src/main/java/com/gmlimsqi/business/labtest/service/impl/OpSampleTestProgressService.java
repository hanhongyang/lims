package com.gmlimsqi.business.labtest.service.impl;

import com.gmlimsqi.business.labtest.dto.OpSampleTestProgressDto;
import com.gmlimsqi.business.labtest.mapper.OpBloodEntrustItemConfigMapper;
import com.gmlimsqi.business.labtest.mapper.OpSampleTestProgressMapper;
import com.gmlimsqi.business.labtest.service.IOpSampleTestProgressService;
import com.gmlimsqi.business.labtest.vo.OpSampleReceiveVo;
import com.gmlimsqi.business.labtest.vo.OpSampleTestProgressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Service
public class OpSampleTestProgressService implements IOpSampleTestProgressService {
    @Autowired
    private OpSampleTestProgressMapper sampleTestProgressMapper;
    @Override
    public List<OpSampleTestProgressVo> selectSampleTestProgressList(OpSampleTestProgressDto sampleTestProgressDto) {
        // 处理结束日期，设置为当天的23:59:59
        if (sampleTestProgressDto.getEndReceiveTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sampleTestProgressDto.getEndReceiveTime());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            sampleTestProgressDto.setEndReceiveTime(calendar.getTime());
        }

        List<OpSampleTestProgressVo> opSampleReceiveVos = new LinkedList<>();
        //1饲料 2血样 3pcr
        if("1".equals(sampleTestProgressDto.getType())){
            opSampleReceiveVos = sampleTestProgressMapper.selectFeedSampleTestProgressList(sampleTestProgressDto);
        }else if("2".equals(sampleTestProgressDto.getType())){
            opSampleReceiveVos = sampleTestProgressMapper.selectBloodSampleTestProgressList(sampleTestProgressDto);
        }else if("3".equals(sampleTestProgressDto.getType())){
            opSampleReceiveVos = sampleTestProgressMapper.selectPcrSampleTestProgressList(sampleTestProgressDto);
        }


        return opSampleReceiveVos;
    }
}
