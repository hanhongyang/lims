package com.gmlimsqi.business.disinfection.mapper;

import java.util.List;
import java.util.Date;

import com.gmlimsqi.business.disinfection.controller.vo.DisinfectionRespVo;
import com.gmlimsqi.business.disinfection.domain.DisinfectionPool;
import org.apache.ibatis.annotations.Param;

/**
 * 消毒池管理Mapper接口
 *
 * @author yangjw
 * @date 2026-01-06
 */
public interface DisinfectionPoolMapper {

    /**
     * 查询消毒池管理
     *
     * @param id 消毒池管理主键
     * @return 消毒池管理
     */
    public DisinfectionRespVo selectDisinfectionPoolById(Long id);

    /**
     * 查询消毒池管理列表
     *
     * @param disinfectionPool 消毒池管理
     * @return 消毒池管理集合
     */
    public List<DisinfectionRespVo> selectDisinfectionPoolList(DisinfectionPool disinfectionPool);

    /**
     * 根据部门id查询消毒池列表
     *
     * @return 消毒池列表
     */
    public List<DisinfectionRespVo> selectDisinfectionPoolByDeptId(Long deptId);

    /**
     * 新增消毒池管理
     *
     * @param disinfectionPool 消毒池管理
     * @return 结果
     */
    public int insertDisinfectionPool(DisinfectionPool disinfectionPool);

    /**
     * 修改消毒池管理
     *
     * @param disinfectionPool 消毒池管理
     * @return 结果
     */
    public int updateDisinfectionPool(DisinfectionPool disinfectionPool);

    /**
     * 通过消毒池管理主键更新删除标志
     *
     * @param id         消毒池id
     * @param deleteBy   删除人
     * @param deleteTime 删除时间
     * @return 结果
     */
    public int deleteDisinfectionPoolById(
            @Param("id") Long id,
            @Param("updateBy") String deleteBy,
            @Param("updateTime") Date deleteTime
    );

    /**
     * 批量通过消毒池管理主键更新删除标志
     *
     * @param ids        消毒池id列表
     * @param deleteBy   删除人
     * @param deleteTime 删除时间
     * @return 结果
     */
    public int deleteDisinfectionPoolByIds(
            @Param("id") List<Long> ids,
            @Param("updateBy") String deleteBy,
            @Param("updateTime") Date deleteTime
    );
}
