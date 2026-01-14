package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.LabtestMethods;

/**
 * 检测方法Service接口
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
public interface ILabtestMethodsService
{
    /**
     * 查询检测方法
     * 
     * @param bsLabtestMethodsId 检测方法主键
     * @return 检测方法
     */
    public LabtestMethods selectBsLabtestMethodsByBsLabtestMethodsId(String bsLabtestMethodsId);

    /**
     * 查询检测方法列表
     * 
     * @param labtestMethods 检测方法
     * @return 检测方法集合
     */
    public List<LabtestMethods> selectBsLabtestMethodsList(LabtestMethods labtestMethods);

    /**
     * 新增检测方法
     * 
     * @param labtestMethods 检测方法
     * @return 结果
     */
    public int insertBsLabtestMethods(LabtestMethods labtestMethods);

    /**
     * 修改检测方法
     * 
     * @param labtestMethods 检测方法
     * @return 结果
     */
    public int updateBsLabtestMethods(LabtestMethods labtestMethods);

    /**
     * 批量删除检测方法
     * 
     * @param bsLabtestMethodsIds 需要删除的检测方法主键集合
     * @return 结果
     */
    public int deleteBsLabtestMethodsByBsLabtestMethodsIds(String[] bsLabtestMethodsIds);

    /**
     * 删除检测方法信息
     * 
     * @param bsLabtestMethodsId 检测方法主键
     * @return 结果
     */
    public int deleteBsLabtestMethodsByBsLabtestMethodsId(String bsLabtestMethodsId);

    public int updateEnableById(LabtestMethods labtestMethods);
}
