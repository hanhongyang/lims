package com.gmlimsqi.business.bsweighbridge.service;

import com.gmlimsqi.business.bsweighbridge.domain.BsWeighBridge;

import java.util.List;

/**
 * 过磅单Service接口
 * 
 * @author hhy
 * @date 2025-11-13
 */
public interface IBsWeighBridgeService 
{
    /**
     * 查询过磅单
     * 
     * @param id 过磅单主键
     * @return 过磅单
     */
    public BsWeighBridge selectBsWeighBridgeById(String id);

    /**
     * 查询过磅单列表
     * 
     * @param bsWeighBridge 过磅单
     * @return 过磅单集合
     */
    public List<BsWeighBridge> selectBsWeighBridgeList(BsWeighBridge bsWeighBridge);

    /**
     * 新增过磅单
     * 
     * @param bsWeighBridge 过磅单
     * @return 结果
     */
    public int insertBsWeighBridge(BsWeighBridge bsWeighBridge);

    /**
     * 修改过磅单
     * 
     * @param bsWeighBridge 过磅单
     * @return 结果
     */
    public int updateBsWeighBridge(BsWeighBridge bsWeighBridge);

    /**
     * 地磅上传过磅单
     *
     * @param bsWeighBridge 过磅单
     * @return 结果
     */
    int uploadBsWeighBridge(BsWeighBridge bsWeighBridge);
}
