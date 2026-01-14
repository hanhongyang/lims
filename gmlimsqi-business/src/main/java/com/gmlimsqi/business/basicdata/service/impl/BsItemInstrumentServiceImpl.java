package com.gmlimsqi.business.basicdata.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.mapper.BsItemInstrumentMapper;
import com.gmlimsqi.business.basicdata.domain.BsItemInstrument;
import com.gmlimsqi.business.basicdata.service.IBsItemInstrumentService;

/**
 * 项目使用设备Service业务层处理
 * 
 * @author hhy
 * @date 2025-09-29
 */
@Service
public class BsItemInstrumentServiceImpl implements IBsItemInstrumentService 
{
    @Autowired
    private BsItemInstrumentMapper bsItemInstrumentMapper;


    /**
     * 查询项目使用设备
     * 
     * @param bsItemInstrumentId 项目使用设备主键
     * @return 项目使用设备
     */
    @Override
    public BsItemInstrument selectBsItemInstrumentByBsItemInstrumentId(String bsItemInstrumentId)
    {
        return bsItemInstrumentMapper.selectBsItemInstrumentByBsItemInstrumentId(bsItemInstrumentId);
    }

    /**
     * 查询项目使用设备列表
     * 
     * @param bsItemInstrument 项目使用设备
     * @return 项目使用设备
     */
    @Override
    public List<BsItemInstrument> selectBsItemInstrumentList(BsItemInstrument bsItemInstrument)
    {
        List<BsItemInstrument> items = bsItemInstrumentMapper.selectBsItemInstrumentList(bsItemInstrument);



        return items;
    }

    /**
     * 新增项目使用设备
     * 
     * @param bsItemInstrument 项目使用设备
     * @return 结果
     */
    @Override
    public int insertBsItemInstrument(BsItemInstrument bsItemInstrument)
    {
        if (StringUtils.isEmpty(bsItemInstrument.getBsItemInstrumentId())) {
            bsItemInstrument.setBsItemInstrumentId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        bsItemInstrument.fillCreateInfo();
        return bsItemInstrumentMapper.insertBsItemInstrument(bsItemInstrument);
    }

    /**
     * 修改项目使用设备
     * 
     * @param bsItemInstrument 项目使用设备
     * @return 结果
     */
    @Override
    public int updateBsItemInstrument(BsItemInstrument bsItemInstrument)
    {
        // 自动填充更新信息
        bsItemInstrument.fillUpdateInfo();
        return bsItemInstrumentMapper.updateBsItemInstrument(bsItemInstrument);
    }


}
