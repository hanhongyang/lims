package com.gmlimsqi.business.disinfection.service.impl;

import java.util.*;

import com.gmlimsqi.business.disinfection.controller.vo.DisinfectionRespVo;
import com.gmlimsqi.business.disinfection.emum.DisinfectionPoolConstant;
import com.gmlimsqi.common.annotation.DataScope;
import com.gmlimsqi.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.disinfection.mapper.DisinfectionPoolMapper;
import com.gmlimsqi.business.disinfection.domain.DisinfectionPool;
import com.gmlimsqi.business.disinfection.service.IDisinfectionPoolService;

/**
 * 消毒池管理Service业务层处理
 * * @author yangjw
 *
 * @date 2026-01-06
 */
@Service
@RequiredArgsConstructor
public class DisinfectionPoolServiceImpl implements IDisinfectionPoolService {

    private final DisinfectionPoolMapper disinfectionPoolMapper;

    /**
     * 根据id查询消毒池
     * * @param id 消毒池管理主键
     *
     * @return 消毒池vo
     */
    @Override
    public DisinfectionRespVo selectDisinfectionPoolById(Long id) {
        return disinfectionPoolMapper.selectDisinfectionPoolById(id);
    }

    /**
     * 查询消毒池管理列表
     * * @param disinfectionPool 消毒池管理
     *
     * @return 消毒池vo 列表
     */
    @DataScope(deptAlias = "d", permission = "basicdata:disinfection:list")
    @Override
    public List<DisinfectionRespVo> selectDisinfectionPoolList(DisinfectionPool disinfectionPool) {
        return disinfectionPoolMapper.selectDisinfectionPoolList(disinfectionPool);
    }

    /**
     * 新增消毒池管理
     * * @param disinfectionPool 消毒池管理
     *
     * @return 结果
     */
    @Override
    public int insertDisinfectionPool(DisinfectionPool disinfectionPool) {
        // 自动填充创建/更新信息
        disinfectionPool.fillCreateInfo();
        return disinfectionPoolMapper.insertDisinfectionPool(disinfectionPool);
    }

    /**
     * 修改消毒池管理
     * * @param disinfectionPool 消毒池管理
     *
     * @return 结果
     */
    @Override
    public int updateDisinfectionPool(DisinfectionPool disinfectionPool) {
        // 自动填充更新信息
        disinfectionPool.fillUpdateInfo();
        return disinfectionPoolMapper.updateDisinfectionPool(disinfectionPool);
    }

    /**
     * 删除消毒池管理信息
     * * @param id 消毒池管理ID
     *
     * @return 删除结果
     */
    @Override
    public int deleteDisinfectionPoolById(Long id) {
        return disinfectionPoolMapper.deleteDisinfectionPoolById(
                id, // 删除消毒池id
                SecurityUtils.getUserId().toString(), // 删除人
                new Date() // 删除时间
        );
    }

    /**
     * 批量删除消毒池管理
     * * @param ids 需要删除的消毒池管理ID
     *
     * @return 结果
     */
    @Override
    public int deleteDisinfectionPoolByIds(List<Long> ids) {
        return disinfectionPoolMapper.deleteDisinfectionPoolByIds(
                ids, // 删除消毒池id列表
                SecurityUtils.getUserId().toString(), // 删除人
                new Date() // 删除时间
        );
    }
}