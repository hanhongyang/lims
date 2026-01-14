package com.gmlimsqi.system.service;


import com.gmlimsqi.system.domain.OpFunLog;

import java.util.List;

/**
 * 第三方对接日志接口Service接口
 *
 * @author EGP
 * @date 2024-08-24
 */
public interface IOpFunLogService
{
    /**
     * 查询第三方对接日志接口
     *
     * @param funId 第三方对接日志接口主键
     * @return 第三方对接日志接口
     */
    public OpFunLog selectOpFunLogByFunId(Long funId);

    /**
     * 查询第三方对接日志接口列表
     *
     * @param opFunLog 第三方对接日志接口
     * @return 第三方对接日志接口集合
     */
    public List<OpFunLog> selectOpFunLogList(OpFunLog opFunLog);

    /**
     * 新增第三方对接日志接口
     *
     * @param opFunLog 第三方对接日志接口
     * @return 结果
     */
    public int insertOpFunLog(OpFunLog opFunLog);


}