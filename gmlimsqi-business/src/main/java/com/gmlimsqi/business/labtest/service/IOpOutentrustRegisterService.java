package com.gmlimsqi.business.labtest.service;

import com.gmlimsqi.business.labtest.domain.OpOutentrustRegister;

import java.util.List;

/**
 * 外部委托检测单Service接口
 *
 * @author wgq
 * @date 2025-09-17
 */
public interface IOpOutentrustRegisterService
{
    /**
     * 查询外部委托检测单
     *
     * @param id 外部委托检测单主键
     * @return 外部委托检测单
     */
    public OpOutentrustRegister selectOpOutentrustRegisterById(String id);

    /**
     * 查询外部委托检测单列表
     *
     * @param opOutentrustRegister 外部委托检测单
     * @return 外部委托检测单集合
     */
    public List<OpOutentrustRegister> selectOpOutentrustRegisterList(OpOutentrustRegister opOutentrustRegister);

    /**
     * 新增外部委托检测单
     *
     * @param opOutentrustRegister 外部委托检测单
     * @return 结果
     */
    public int insertOpOutentrustRegister(OpOutentrustRegister opOutentrustRegister);

    /**
     * 修改外部委托检测单
     *
     * @param opOutentrustRegister 外部委托检测单
     * @return 结果
     */
    public int updateOpOutentrustRegister(OpOutentrustRegister opOutentrustRegister);

    /**
     * 批量删除外部委托检测单
     *
     * @param ids 需要删除的外部委托检测单主键集合
     * @return 结果
     */
    public int deleteOpOutentrustRegisterByIds(String[] ids);

    /**
     * 删除外部委托检测单信息
     *
     * @param id 外部委托检测单主键
     * @return 结果
     */
    public int deleteOpOutentrustRegisterById(String id);

    /**
     * 弃审/审核外部委托检测单
     * @param opOutentrustRegister
     * @return
     */
    int enableOpOutentrustRegister(OpOutentrustRegister opOutentrustRegister);
}
