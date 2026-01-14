package com.gmlimsqi.business.ranch.mapper;

import com.gmlimsqi.business.ranch.domain.OpTestResultChangeLog;
import com.gmlimsqi.business.ranch.dto.changelog.ResultChangeLogQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 样品化验结果变更日志Mapper接口
 *
 * @author yjw
 * @date 2026-01-10
 */
public interface OpTestResultChangeLogMapper {

    /**
     * 查询检测结果变更日志列表
     */
    List<OpTestResultChangeLog> selectList(@Param("query") ResultChangeLogQueryDTO query);

    /**
     * 根据id查询
     */
    OpTestResultChangeLog selectById(Long id);

    /**
     * 新增样品化验结果变更日志
     *
     * @param log 变更日志
     * @return 影响行数
     */
    int insertOpTestResultChangeLog(OpTestResultChangeLog log);
}