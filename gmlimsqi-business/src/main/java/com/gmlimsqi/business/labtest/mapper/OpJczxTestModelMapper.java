package com.gmlimsqi.business.labtest.mapper;

import java.util.List;
import com.gmlimsqi.business.labtest.domain.OpJczxTestModel;
import org.apache.ibatis.annotations.Param;

/**
 * 检测中心化验单模版配置Mapper接口
 * 
 * @author hhy
 * @date 2025-09-29
 */
public interface OpJczxTestModelMapper 
{
    /**
     * 查询检测中心化验单模版配置
     * 
     * @param opJczxTestModelId 检测中心化验单模版配置主键
     * @return 检测中心化验单模版配置
     */
    public OpJczxTestModel selectOpJczxTestModelByOpJczxTestModelId(String opJczxTestModelId);

    /**
     * 查询检测中心化验单模版配置列表
     * 
     * @param opJczxTestModel 检测中心化验单模版配置
     * @return 检测中心化验单模版配置集合
     */
    public List<OpJczxTestModel> selectOpJczxTestModelList(OpJczxTestModel opJczxTestModel);

    /**
     * 新增检测中心化验单模版配置
     * 
     * @param opJczxTestModel 检测中心化验单模版配置
     * @return 结果
     */
    public int insertOpJczxTestModel(OpJczxTestModel opJczxTestModel);

    /**
     * 修改检测中心化验单模版配置
     * 
     * @param opJczxTestModel 检测中心化验单模版配置
     * @return 结果
     */
    public int updateOpJczxTestModel(OpJczxTestModel opJczxTestModel);

    public OpJczxTestModel selectByItemIdInvbillCode(@Param("itemId") String itemId, @Param("invbillCode") String invbillCode);
}
