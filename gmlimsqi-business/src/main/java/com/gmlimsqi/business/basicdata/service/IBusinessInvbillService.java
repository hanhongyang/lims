package com.gmlimsqi.business.basicdata.service;

import com.gmlimsqi.business.basicdata.domain.BusinessInvbill;
import com.gmlimsqi.business.basicdata.vo.BusinessInvbillVo;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 物料档案Service接口
 *
 * @author egap
 * @date 2024-12-16
 */
public interface IBusinessInvbillService {
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
    

    
    void insertBusinessInvbillBatch(List<BusinessInvbillVo> noMatch);
    
    String selectInfoByMaterialCode(@NotNull String materialCode);
    
    BusinessInvbill selectInfoWithCodeAndSap(String matnr, String werks);

    BusinessInvbill selectInfoBySapCode(String matnr);

    /**
     * 手动新增物料档案
     *
     * @param baseInvbill 物料档案
     * @return 结果
     */
    int insertInvbill(BusinessInvbill baseInvbill);
}