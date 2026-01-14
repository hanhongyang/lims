package com.gmlimsqi.sap.log.mapper;


import com.gmlimsqi.sap.log.domain.BsSapLog;

/**
 * SAP接口日志Mapper接口
 *
 * @author EGP
 * @date 2024-03-27
 */
public interface BsSapLogMapper {
    
    /**
     * 新增SAP接口日志
     *
     * @param bsSapLog SAP接口日志
     * @return 结果
     */
    public int insertBsSapLog(BsSapLog bsSapLog);
    
    
}