package com.gmlimsqi.business.labtest.mapper;


import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrder;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 血样样品委托单Mapper接口
 * 
 * @author hhy
 * @date 2025-09-20
 */
public interface OpBloodEntrustOrderMapper 
{
    /**
     * 查询血样样品委托单
     * 
     * @param opBloodEntrustOrderId 血样样品委托单主键
     * @return 血样样品委托单
     */
    public OpBloodEntrustOrder selectOpBloodEntrustOrderByOpBloodEntrustOrderId(String opBloodEntrustOrderId);

    /**
     * 查询血样样品委托单列表
     * 
     * @param opBloodEntrustOrder 血样样品委托单
     * @return 血样样品委托单集合
     */
    public List<OpBloodEntrustOrder> selectOpBloodEntrustOrderList(OpBloodEntrustOrder opBloodEntrustOrder);

    /**
     * 新增血样样品委托单
     * 
     * @param opBloodEntrustOrder 血样样品委托单
     * @return 结果
     */
    public int insertOpBloodEntrustOrder(OpBloodEntrustOrder opBloodEntrustOrder);

    /**
     * 修改血样样品委托单
     * 
     * @param opBloodEntrustOrder 血样样品委托单
     * @return 结果
     */
    public int updateOpBloodEntrustOrder(OpBloodEntrustOrder opBloodEntrustOrder);



    public OpBloodEntrustOrder selectByNo(@Param("entrustOrderNo") String entrustOrderNo);

    public void updateDeleteIdById(String opBloodEntrustOrderId);

    public int updateOrderStatusByOrderIdList(@Param("orderIdList") List<String> orderIdList);

    /**
     * 更新委托单状态
     */
    int updateStatusById(@Param("opBloodEntrustOrderId") String opBloodEntrustOrderId,
                         @Param("status") String status,
                         @Param("examineUser") String examineUser,
                         @Param("examineUserId") String examineUserId,
                         @Param("examineTime") Date examineTime,
                         @Param("updateBy") String updateBy,
                         @Param("updateTime") Date updateTime
    );

    //根据样品id查询生化项目
    List<String> selectBiochemistryItemTypeBySampleIdList(@Param("sampleIdList")List<String> sampleIdList);
}



