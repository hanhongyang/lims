package com.gmlimsqi.business.basicdata.mapper;

import com.gmlimsqi.business.basicdata.domain.BusinessSupplier;
import com.gmlimsqi.business.basicdata.vo.SupplierSapVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 供应商Mapper接口
 *
 * @author egap
 * @date 2023-04-13
 */
public interface BusinessSupplierMapper {
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
     * 删除供应商
     *
     * @param id 供应商主键
     * @return 结果
     */
    public int deleteBusinessSupplierById(Long id);
    
    /**
     * 批量删除供应商
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusinessSupplierByIds(String[] ids);
    
    /**
     * 从sqlserver中读取数据
     * @param username sqlserver数据库中的ccustomercode
     * @return 数据
     */
    List<SupplierSapVO> selectSupplierListBySqlServer(@Param("username") String username);
    
    List<SupplierSapVO> selectClientListBySqlServer(@Param("username") String username);

    void insertBusinessSupplierBatch(@Param("list")List<BusinessSupplier> businessSuppliers);
}
