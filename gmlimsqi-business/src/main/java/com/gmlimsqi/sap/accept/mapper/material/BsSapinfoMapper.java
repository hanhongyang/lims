package com.gmlimsqi.sap.accept.mapper.material;


import com.gmlimsqi.sap.accept.domain.material.BsSapinfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * SAP接口配置Mapper接口
 *
 * @author EGP
 * @date 2024-03-27
 */
@Mapper
public interface BsSapinfoMapper {
    
    /**
     * 根据编码获取所有配置信息
     *
     * @param inventoryTB
     * @return
     */
    BsSapinfo getSapConfig(String inventoryTB);
    
}