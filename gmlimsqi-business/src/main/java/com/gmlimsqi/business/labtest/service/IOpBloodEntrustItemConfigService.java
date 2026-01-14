package com.gmlimsqi.business.labtest.service;

import java.util.List;
import com.gmlimsqi.business.labtest.domain.OpBloodEntrustItemConfig;

/**
 * 血样委托项目配置Service接口
 * 
 * @author hhy
 * @date 2025-09-23
 */
public interface IOpBloodEntrustItemConfigService 
{
    /**
     * 查询血样委托项目配置
     * 
     * @param opBloodEntrustItemConfigId 血样委托项目配置主键
     * @return 血样委托项目配置
     */
    public OpBloodEntrustItemConfig selectOpBloodEntrustItemConfigByOpBloodEntrustItemConfigId(String opBloodEntrustItemConfigId);

    /**
     * 查询血样委托项目配置列表
     * 
     * @param opBloodEntrustItemConfig 血样委托项目配置
     * @return 血样委托项目配置集合
     */
    public List<OpBloodEntrustItemConfig> selectOpBloodEntrustItemConfigList(OpBloodEntrustItemConfig opBloodEntrustItemConfig);

    /**
     * 新增血样委托项目配置
     * 
     * @param opBloodEntrustItemConfig 血样委托项目配置
     * @return 结果
     */
    public int insertOpBloodEntrustItemConfig(OpBloodEntrustItemConfig opBloodEntrustItemConfig);

    /**
     * 修改血样委托项目配置
     * 
     * @param opBloodEntrustItemConfig 血样委托项目配置
     * @return 结果
     */
    public int updateOpBloodEntrustItemConfig(OpBloodEntrustItemConfig opBloodEntrustItemConfig);

}
