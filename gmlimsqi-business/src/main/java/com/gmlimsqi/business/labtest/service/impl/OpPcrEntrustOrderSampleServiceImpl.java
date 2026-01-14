package com.gmlimsqi.business.labtest.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.labtest.mapper.OpPcrEntrustOrderSampleMapper;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrderSample;
import com.gmlimsqi.business.labtest.service.IOpPcrEntrustOrderSampleService;

/**
 * pcr样品委托单-样品Service业务层处理
 * 
 * @author hhy
 * @date 2025-09-17
 */
@Service
public class OpPcrEntrustOrderSampleServiceImpl implements IOpPcrEntrustOrderSampleService 
{
    @Autowired
    private OpPcrEntrustOrderSampleMapper opPcrEntrustOrderSampleMapper;


    /**
     * 查询pcr样品委托单-样品
     * 
     * @param opPcrEntrustOrderSampleId pcr样品委托单-样品主键
     * @return pcr样品委托单-样品
     */
    @Override
    public OpPcrEntrustOrderSample selectOpPcrEntrustOrderSampleByOpPcrEntrustOrderSampleId(String opPcrEntrustOrderSampleId)
    {
        return opPcrEntrustOrderSampleMapper.selectOpPcrEntrustOrderSampleByOpPcrEntrustOrderSampleId(opPcrEntrustOrderSampleId);
    }

    /**
     * 查询pcr样品委托单-样品列表
     * 
     * @param opPcrEntrustOrderSample pcr样品委托单-样品
     * @return pcr样品委托单-样品
     */
    @Override
    public List<OpPcrEntrustOrderSample> selectOpPcrEntrustOrderSampleList(OpPcrEntrustOrderSample opPcrEntrustOrderSample)
    {
        List<OpPcrEntrustOrderSample> items = opPcrEntrustOrderSampleMapper.selectOpPcrEntrustOrderSampleList(opPcrEntrustOrderSample);



        return items;
    }

    /**
     * 新增pcr样品委托单-样品
     * 
     * @param opPcrEntrustOrderSample pcr样品委托单-样品
     * @return 结果
     */
    @Override
    public int insertOpPcrEntrustOrderSample(OpPcrEntrustOrderSample opPcrEntrustOrderSample)
    {
        if (StringUtils.isEmpty(opPcrEntrustOrderSample.getOpPcrEntrustOrderSampleId())) {
            opPcrEntrustOrderSample.setOpPcrEntrustOrderSampleId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opPcrEntrustOrderSample.fillCreateInfo();
        return opPcrEntrustOrderSampleMapper.insertOpPcrEntrustOrderSample(opPcrEntrustOrderSample);
    }




}
