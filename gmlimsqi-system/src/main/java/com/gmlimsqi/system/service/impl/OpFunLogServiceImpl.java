package com.gmlimsqi.system.service.impl;

import com.gmlimsqi.system.domain.OpFunLog;
import com.gmlimsqi.system.mapper.OpFunLogMapper;
import com.gmlimsqi.system.service.IOpFunLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 第三方对接日志接口Service业务层处理
 *
 * @author EGP
 * @date 2024-08-24
 */
@Service
public class OpFunLogServiceImpl implements IOpFunLogService {
    @Autowired
    private OpFunLogMapper opFunLogMapper;
    
    /**
     * 查询第三方对接日志接口
     *
     * @param funId 第三方对接日志接口主键
     * @return 第三方对接日志接口
     */
    @Override
    public OpFunLog selectOpFunLogByFunId(Long funId) {
        return opFunLogMapper.selectOpFunLogByFunId(funId);
    }
    
    /**
     * 查询第三方对接日志接口列表
     *
     * @param opFunLog 第三方对接日志接口
     * @return 第三方对接日志接口
     */
    @Override
    public List<OpFunLog> selectOpFunLogList(OpFunLog opFunLog) {
        return opFunLogMapper.selectOpFunLogList(opFunLog);
    }
    
    /**
     * 新增第三方对接日志接口
     *
     * @param opFunLog 第三方对接日志接口
     * @return 结果
     */
    @Override
    public int insertOpFunLog(OpFunLog opFunLog) {
        return opFunLogMapper.insertOpFunLog(opFunLog);
    }

}
