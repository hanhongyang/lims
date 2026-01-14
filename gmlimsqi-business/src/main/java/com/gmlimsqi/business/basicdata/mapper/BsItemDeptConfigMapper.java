package com.gmlimsqi.business.basicdata.mapper;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsItemDeptConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目部门资源配置Mapper接口
 */
@Mapper
public interface BsItemDeptConfigMapper 
{
    /**
     * 批量新增配置
     * * @param configList 配置列表
     * @return 结果
     */
    public int batchInsert(List<BsItemDeptConfig> configList);

    /**
     * 根据项目ID物理删除配置（更新时先删后增）
     * * @param itemId 项目ID
     * @return 结果
     */
    public int deleteByItemId(String itemId);

    /**
     * 根据项目ID查询配置列表（包含关联名称）
     * * @param itemId 项目ID
     * @return 结果
     */
    public List<BsItemDeptConfig> selectConfigListByItemId(String itemId);
}