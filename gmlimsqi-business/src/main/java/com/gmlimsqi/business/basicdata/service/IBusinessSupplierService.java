package com.gmlimsqi.business.basicdata.service;

import com.gmlimsqi.business.basicdata.domain.BusinessSupplier;

import java.util.List;

/**
 * 供应商Service接口
 *
 * @author egap
 * @date 2023-04-13
 */
public interface IBusinessSupplierService  {
    /**
     * 查询供应商
     *
     * @param id 供应商主键
     * @return 供应商
     */
    public BusinessSupplier selectBusinessSupplierById(String id);
    
    /**
     * 查询供应商列表
     *
     * @param businessSupplier 供应商
     * @return 供应商集合
     */
    public List<BusinessSupplier> selectBusinessSupplierList(BusinessSupplier businessSupplier);

    /**
     * 新增供应商
     *
     * @param businessSupplier 供应商
     * @return 结果
     */
    public int insertBusinessSupplier(BusinessSupplier businessSupplier);
    
    /**
     * 修改供应商
     *
     * @param businessSupplier 供应商
     * @return 结果
     */
    public int updateBusinessSupplier(BusinessSupplier businessSupplier);
    
    /**
     * 批量删除供应商
     *
     * @param ids 需要删除的供应商主键集合
     * @return 结果
     */
    public int deleteBusinessSupplierByIds(String[] ids);
    
    /**
     * 删除供应商信息
     *
     * @param id 供应商主键
     * @return 结果
     */
    public int deleteBusinessSupplierById(Long id);
    
    /**
     * 获取sap系统数据
     * @return 数据
     */
    Integer info();




    void saveBatch(List<BusinessSupplier> businessSuppliers);
}
