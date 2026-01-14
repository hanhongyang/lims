package com.gmlimsqi.business.labtest.mapper;

import com.gmlimsqi.business.labtest.domain.OpOutentrustRegister;
import com.gmlimsqi.business.labtest.domain.OpOutentrustRegisterItem;
import com.gmlimsqi.business.labtest.domain.OpOutentrustRegisterSample;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * 外部委托检测单Mapper接口
 *
 * @author wgq
 * @date 2025-09-17
 */
public interface OpOutentrustRegisterMapper
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
     * 通过外部委托检测单主键更新删除标志
     *
     * @param id 外部委托检测单ID
     * @return 结果
     */
    public int updateDeleteFlagById(String id);

    /**
     * 批量通过外部委托检测单主键更新删除标志
     *
     * @param id 外部委托检测单ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] ids);

    /**
     * 删除外部委托检测单
     *
     * @param id 外部委托检测单主键
     * @return 结果
     */
    public int deleteOpOutentrustRegisterById(String id);

    /**
     * 批量删除外部委托检测单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpOutentrustRegisterByIds(String[] ids);

    /**
     * 批量删除外部委托检测单样品子
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpOutentrustRegisterSampleByOutentrustRegisterSampleIds(String[] ids);

    /**
     * 批量新增外部委托检测单样品子
     *
     * @param opOutentrustRegisterSampleList 外部委托检测单样品子列表
     * @return 结果
     */
    public int batchOpOutentrustRegisterSample(List<OpOutentrustRegisterSample> opOutentrustRegisterSampleList);


    /**
     * 通过外部委托检测单主键删除外部委托检测单样品子信息
     *
     * @param id 外部委托检测单ID
     * @return 结果
     */
    public int deleteOpOutentrustRegisterSampleByOutentrustRegisterSampleId(String id);

    /**
     * 批量更新外部委托检测单样品子删除标志
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int updateDeleteFlagByOutentrustRegisterSampleIds(@Param("outentrustRegisterSampleId")String[] ids, @Param("now") Date now, @Param("updateUserId") String updateUserId);



    /**
     * 通过外部委托检测单主键更新删除标志外部委托检测单样品子信息
     *
     * @param id 外部委托检测单ID
     * @return 结果
     */
    public int updateDeleteFlagByOutentrustRegisterSampleId(@Param("id")String id,@Param("now") Date now,@Param("updateUserId") String updateUserId);

    /**
     * 批量插入检测项目子表
     * @param opOutentrustRegisterItemList
     */
    public int  batchOpOutentrustRegisterItem(List<OpOutentrustRegisterItem> opOutentrustRegisterItemList);

    void updateDeleteFlagByOutentrustRegisterId(String id);
}
