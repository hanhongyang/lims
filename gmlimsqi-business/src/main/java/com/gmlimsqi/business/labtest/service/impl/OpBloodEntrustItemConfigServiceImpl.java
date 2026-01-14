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
import com.gmlimsqi.business.labtest.mapper.OpBloodEntrustItemConfigMapper;
import com.gmlimsqi.business.labtest.domain.OpBloodEntrustItemConfig;
import com.gmlimsqi.business.labtest.service.IOpBloodEntrustItemConfigService;

/**
 * 血样委托项目配置Service业务层处理
 * 
 * @author hhy
 * @date 2025-09-23
 */
@Service
public class OpBloodEntrustItemConfigServiceImpl implements IOpBloodEntrustItemConfigService 
{
    @Autowired
    private OpBloodEntrustItemConfigMapper opBloodEntrustItemConfigMapper;


    /**
     * 查询血样委托项目配置
     * 
     * @param opBloodEntrustItemConfigId 血样委托项目配置主键
     * @return 血样委托项目配置
     */
    @Override
    public OpBloodEntrustItemConfig selectOpBloodEntrustItemConfigByOpBloodEntrustItemConfigId(String opBloodEntrustItemConfigId)
    {
        return opBloodEntrustItemConfigMapper.selectOpBloodEntrustItemConfigByOpBloodEntrustItemConfigId(opBloodEntrustItemConfigId);
    }

    /**
     * 查询血样委托项目配置列表
     * 
     * @param opBloodEntrustItemConfig 血样委托项目配置
     * @return 血样委托项目配置
     */
    @Override
    public List<OpBloodEntrustItemConfig> selectOpBloodEntrustItemConfigList(OpBloodEntrustItemConfig opBloodEntrustItemConfig)
    {
        List<OpBloodEntrustItemConfig> items = opBloodEntrustItemConfigMapper.selectOpBloodEntrustItemConfigList(opBloodEntrustItemConfig);



        return items;
    }

    /**
     * 新增血样委托项目配置
     * 
     * @param opBloodEntrustItemConfig 血样委托项目配置
     * @return 结果
     */
    @Override
    public int insertOpBloodEntrustItemConfig(OpBloodEntrustItemConfig opBloodEntrustItemConfig)
    {
        if (StringUtils.isEmpty(opBloodEntrustItemConfig.getOpBloodEntrustItemConfigId())) {
            opBloodEntrustItemConfig.setOpBloodEntrustItemConfigId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opBloodEntrustItemConfig.fillCreateInfo();
        return opBloodEntrustItemConfigMapper.insertOpBloodEntrustItemConfig(opBloodEntrustItemConfig);
    }

    /**
     * 修改血样委托项目配置
     * 
     * @param opBloodEntrustItemConfig 血样委托项目配置
     * @return 结果
     */
    @Override
    public int updateOpBloodEntrustItemConfig(OpBloodEntrustItemConfig opBloodEntrustItemConfig)
    {
        // 自动填充更新信息
        opBloodEntrustItemConfig.fillUpdateInfo();
        return opBloodEntrustItemConfigMapper.updateOpBloodEntrustItemConfig(opBloodEntrustItemConfig);
    }


}
