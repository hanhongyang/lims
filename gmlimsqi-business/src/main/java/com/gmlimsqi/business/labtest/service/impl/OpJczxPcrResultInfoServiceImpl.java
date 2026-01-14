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
import com.gmlimsqi.business.labtest.mapper.OpJczxPcrResultInfoMapper;
import com.gmlimsqi.business.labtest.domain.OpJczxPcrResultInfo;
import com.gmlimsqi.business.labtest.service.IOpJczxPcrResultInfoService;

/**
 * 检测中心pce化验单子Service业务层处理
 * 
 * @author hhy
 * @date 2025-10-13
 */
@Service
public class OpJczxPcrResultInfoServiceImpl implements IOpJczxPcrResultInfoService 
{
    @Autowired
    private OpJczxPcrResultInfoMapper opJczxPcrResultInfoMapper;


    /**
     * 查询检测中心pce化验单子
     * 
     * @param opJczxPcrResultInfoId 检测中心pce化验单子主键
     * @return 检测中心pce化验单子
     */
    @Override
    public OpJczxPcrResultInfo selectOpJczxPcrResultInfoByOpJczxPcrResultInfoId(String opJczxPcrResultInfoId)
    {
        return opJczxPcrResultInfoMapper.selectOpJczxPcrResultInfoByOpJczxPcrResultInfoId(opJczxPcrResultInfoId);
    }

    /**
     * 查询检测中心pce化验单子列表
     * 
     * @param opJczxPcrResultInfo 检测中心pce化验单子
     * @return 检测中心pce化验单子
     */
    @Override
    public List<OpJczxPcrResultInfo> selectOpJczxPcrResultInfoList(OpJczxPcrResultInfo opJczxPcrResultInfo)
    {
        List<OpJczxPcrResultInfo> items = opJczxPcrResultInfoMapper.selectOpJczxPcrResultInfoList(opJczxPcrResultInfo);

        return items;
    }

    /**
     * 新增检测中心pce化验单子
     * 
     * @param opJczxPcrResultInfo 检测中心pce化验单子
     * @return 结果
     */
    @Override
    public int insertOpJczxPcrResultInfo(OpJczxPcrResultInfo opJczxPcrResultInfo)
    {
        if (StringUtils.isEmpty(opJczxPcrResultInfo.getOpJczxPcrResultInfoId())) {
            opJczxPcrResultInfo.setOpJczxPcrResultInfoId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opJczxPcrResultInfo.fillCreateInfo();
        return opJczxPcrResultInfoMapper.insertOpJczxPcrResultInfo(opJczxPcrResultInfo);
    }

    /**
     * 修改检测中心pce化验单子
     * 
     * @param opJczxPcrResultInfo 检测中心pce化验单子
     * @return 结果
     */
    @Override
    public int updateOpJczxPcrResultInfo(OpJczxPcrResultInfo opJczxPcrResultInfo)
    {
        // 自动填充更新信息
        opJczxPcrResultInfo.fillUpdateInfo();
        return opJczxPcrResultInfoMapper.updateOpJczxPcrResultInfo(opJczxPcrResultInfo);
    }

}
