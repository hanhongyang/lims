package com.gmlimsqi.business.milksamplequalityinspection.mapper;

import com.gmlimsqi.business.milksamplequalityinspection.domain.MilkSampleQualityInspectionChangeLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MilkSampleQualityInspectionChangeLogMapper {

    /**
     * 新增变更日志
     */
    int insert(MilkSampleQualityInspectionChangeLogDO log);

    /**
     * 根据主键查询
     */
    MilkSampleQualityInspectionChangeLogDO selectById(@Param("id") Long id);

    /**
     * 根据奶样质检主表ID查询日志（按时间倒序）
     */
    List<MilkSampleQualityInspectionChangeLogDO> selectListByBizId(@Param("bizId") String bizId);

    /**
     * 条件查询列表（都非必填）
     * 你可以按需要删减条件
     */
    List<MilkSampleQualityInspectionChangeLogDO> selectList(@Param("query") MilkSampleQualityInspectionChangeLogDO query);

}