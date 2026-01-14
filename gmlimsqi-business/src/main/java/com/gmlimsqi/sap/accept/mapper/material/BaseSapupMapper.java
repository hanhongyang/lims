package com.gmlimsqi.sap.accept.mapper.material;

import com.gmlimsqi.sap.accept.domain.material.BaseSapup;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * sap同步时间Mapper接口
 *
 * @author EGP
 * @date 2024-03-27
 */
public interface BaseSapupMapper {
    /**
     * 新增sap同步时间
     *
     * @param baseSapup sap同步时间
     * @return 结果
     */
    public int insertBaseSapup(BaseSapup baseSapup);
    
    BaseSapup selectSapup(String cztCode);
    
    void update(@Param("name") String name, @Param("nowDate") Date nowDate, @Param("cztCode") String cztCode);
    
}