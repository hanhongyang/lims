package com.gmlimsqi.business.disinfection.service;

import java.util.List;

import com.gmlimsqi.business.disinfection.controller.vo.DisinfectionRespVo;
import com.gmlimsqi.business.disinfection.domain.DisinfectionPool;

/**
 * 消毒池管理Service接口
 *
 * @author yangjw
 * @date 2026-01-06
 */
public interface IDisinfectionPoolService {

    /**
     * 根据id查询消毒池
     * * @param id 消毒池管理主键
     *
     * @return 消毒池vo
     */
    DisinfectionRespVo selectDisinfectionPoolById(Long id);

    /**
     * 查询消毒池管理列表
     * * @param disinfectionPool 消毒池管理
     *
     * @return 消毒池vo 列表
     */
    List<DisinfectionRespVo> selectDisinfectionPoolList(DisinfectionPool disinfectionPool);

    /**
     * 新增消毒池管理
     * * @param disinfectionPool 消毒池管理
     *
     * @return 结果
     */
    int insertDisinfectionPool(DisinfectionPool disinfectionPool);

    /**
     * 修改消毒池管理
     * * @param disinfectionPool 消毒池管理
     *
     * @return 结果
     */
    int updateDisinfectionPool(DisinfectionPool disinfectionPool);

    /**
     * 删除消毒池管理信息
     * * @param id 消毒池管理ID
     *
     * @return 删除结果
     */
    int deleteDisinfectionPoolById(Long id);

    /**
     * 批量删除消毒池管理
     * * @param ids 需要删除的消毒池管理ID
     *
     * @return 结果
     */
    int deleteDisinfectionPoolByIds(List<Long> ids);

}
