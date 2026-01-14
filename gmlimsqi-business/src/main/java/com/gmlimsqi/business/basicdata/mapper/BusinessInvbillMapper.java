package com.gmlimsqi.business.basicdata.mapper;

import com.gmlimsqi.business.basicdata.domain.BusinessInvbill;
import com.gmlimsqi.business.basicdata.domain.BusinessInvbillInfoDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料档案Mapper接口
 *
 * @author egap
 * @date 2024-12-16
 */
public interface BusinessInvbillMapper {
    /**
     * 查询物料档案
     *
     * @param id 物料档案主键
     * @return 物料档案
     */
    public BusinessInvbill selectBusinessInvbillById(String id);
    
    /**
     * 查询物料档案列表
     *
     * @param businessInvbill 物料档案
     * @return 物料档案集合
     */
    public List<BusinessInvbill> selectBusinessInvbillList(BusinessInvbill businessInvbill);
    
    /**
     * 新增物料档案
     *
     * @param businessInvbill 物料档案
     * @return 结果
     */
    public int insertBusinessInvbill(BusinessInvbill businessInvbill);
    
    /**
     * 修改物料档案
     *
     * @param businessInvbill 物料档案
     * @return 结果
     */
    public int updateBusinessInvbill(BusinessInvbill businessInvbill);
    
    /**
     * 删除物料档案
     *
     * @param id 物料档案主键
     * @return 结果
     */
    public int deleteBusinessInvbillById(String id);
    
    /**
     * 批量删除物料档案
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusinessInvbillByIds(String[] ids);
    
    void insertBusinessInvbillBatch(List<BusinessInvbill> list);
    
    String selectInfoByMaterialCode(String materialCode);

    /**
     * 根据id查询物料档案信息
     * @param businessInvbillInfoDTO
     * @return
     */
    BusinessInvbill getBusinessInvbillWithInfoById(BusinessInvbillInfoDTO businessInvbillInfoDTO);
    
    BusinessInvbill selectInfoWithCodeAndSap(@Param("matnr") String matnr, @Param("werks") String werks);

    BusinessInvbill selectInfoBySapCode(@Param("matnr") String matnr);
    String selectSapCodeWithName(@Param("sapCode") String sapCode, @Param("materialName") String materialName);
    
}