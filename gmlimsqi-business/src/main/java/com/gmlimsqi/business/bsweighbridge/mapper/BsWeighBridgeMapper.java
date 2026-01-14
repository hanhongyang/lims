package com.gmlimsqi.business.bsweighbridge.mapper;

import com.gmlimsqi.business.bsweighbridge.domain.BsWeighBridge;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 过磅单Mapper接口
 * 
 * @author hhy
 * @date 2025-11-13
 */
public interface BsWeighBridgeMapper 
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
     * 根据磅单号修改过磅单
     *
     * @param bsWeighBridge 过磅单
     * @return 结果
     */
     public int updateBsWeighBridgeByCweightno(BsWeighBridge bsWeighBridge);

    /**
     * 通过过磅单主键更新删除标志
     *
     * @param id 过磅单ID
     * @return 结果
     */
    public int updateDeleteFlagById(String id);

    /**
     * 批量通过过磅单主键更新删除标志
     *
     * @param id 过磅单ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] ids);

    /**
     * 判断磅单号是否已经上传过未上传过保存已上传过修改
     *
     * @param cweightno 磅单号
     * @return 结果
     */
    BsWeighBridge IsExist(String cweightno);

    /***
     * 根据签到id和物料凭证查询过磅单
     */
     BsWeighBridge selectBsWeighBridgeBySignIdAndMaterialVoucher(@Param("signId") String signId,
                                                                 @Param("materialVoucher") String materialVoucher);

    /**
     * 根据车牌号查询最新过磅单
     */
    BsWeighBridge selectLatestBsWeighBridgeByDriverCode(String driverCode);
}
