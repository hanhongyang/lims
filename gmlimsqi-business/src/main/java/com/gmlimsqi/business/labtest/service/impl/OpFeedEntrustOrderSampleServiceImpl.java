package com.gmlimsqi.business.labtest.service.impl;

import java.util.List;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.service.ISysUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.labtest.mapper.OpFeedEntrustOrderSampleMapper;
import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrderSample;
import com.gmlimsqi.business.labtest.service.IOpFeedEntrustOrderSampleService;

/**
 * 饲料样品委托单-样品Service业务层处理
 * 
 * @author hhy
 * @date 2025-09-15
 */
@Service
public class OpFeedEntrustOrderSampleServiceImpl implements IOpFeedEntrustOrderSampleService 
{
    @Autowired
    private OpFeedEntrustOrderSampleMapper opFeedEntrustOrderSampleMapper;
    @Autowired
    private ISysUploadFileService sysUploadFileService;

    /**
     * 查询饲料样品委托单-样品
     * 
     * @param opFeedEntrustOrderSampleId 饲料样品委托单-样品主键
     * @return 饲料样品委托单-样品
     */
    @Override
    public OpFeedEntrustOrderSample selectOpFeedEntrustOrderSampleByOpFeedEntrustOrderSampleId(String opFeedEntrustOrderSampleId)
    {
        return opFeedEntrustOrderSampleMapper.selectOpFeedEntrustOrderSampleByOpFeedEntrustOrderSampleId(opFeedEntrustOrderSampleId);
    }

    /**
     * 查询饲料样品委托单-样品列表
     * 
     * @param opFeedEntrustOrderSample 饲料样品委托单-样品
     * @return 饲料样品委托单-样品
     */
    @Override
    public List<OpFeedEntrustOrderSample> selectOpFeedEntrustOrderSampleList(OpFeedEntrustOrderSample opFeedEntrustOrderSample)
    {
        List<OpFeedEntrustOrderSample> items = opFeedEntrustOrderSampleMapper.selectOpFeedEntrustOrderSampleList(opFeedEntrustOrderSample);

        return items;
    }

    /**
     * 新增饲料样品委托单-样品
     * 
     * @param opFeedEntrustOrderSample 饲料样品委托单-样品
     * @return 结果
     */
    @Override
    public int insertOpFeedEntrustOrderSample(OpFeedEntrustOrderSample opFeedEntrustOrderSample)
    {
        if (StringUtils.isEmpty(opFeedEntrustOrderSample.getOpFeedEntrustOrderSampleId())) {
            opFeedEntrustOrderSample.setOpFeedEntrustOrderSampleId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opFeedEntrustOrderSample.fillCreateInfo();
        return opFeedEntrustOrderSampleMapper.insertOpFeedEntrustOrderSample(opFeedEntrustOrderSample);
    }

    /**
     * 修改饲料样品委托单-样品
     * 
     * @param opFeedEntrustOrderSample 饲料样品委托单-样品
     * @return 结果
     */
    @Override
    public int updateOpFeedEntrustOrderSample(OpFeedEntrustOrderSample opFeedEntrustOrderSample)
    {
        // 自动填充更新信息
        opFeedEntrustOrderSample.fillUpdateInfo();
        return opFeedEntrustOrderSampleMapper.updateOpFeedEntrustOrderSample(opFeedEntrustOrderSample);
    }

    @Override
    public int uploadNirReport(OpFeedEntrustOrderSample feedEntrustOrderSample) {

        opFeedEntrustOrderSampleMapper.updateOpFeedEntrustOrderSample(feedEntrustOrderSample);
        // 标记文件为正式文件
        sysUploadFileService.markFileAsPermanent(feedEntrustOrderSample.getFileId());
        return 1;
    }

}
