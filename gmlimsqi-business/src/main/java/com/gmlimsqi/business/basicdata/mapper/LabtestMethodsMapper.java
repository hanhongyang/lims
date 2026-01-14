package com.gmlimsqi.business.basicdata.mapper;

import java.util.List;

import com.gmlimsqi.business.basicdata.domain.LabtestMethodInstrument;
import com.gmlimsqi.business.basicdata.domain.LabtestMethods;
import com.gmlimsqi.business.basicdata.domain.LabtestMethodsAttribute;
import com.gmlimsqi.business.basicdata.domain.LabtestMethodsFormula;

/**
 * 检测方法Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
public interface LabtestMethodsMapper
{
    /**
     * 查询检测方法
     * 
     * @param bsLabtestMethodsId 检测方法主键
     * @return 检测方法
     */
    public LabtestMethods selectMethodsByMethodsId(String bsLabtestMethodsId);

    /**
     * 查询检测方法列表
     * 
     * @param labtestMethods 检测方法
     * @return 检测方法集合
     */
    public List<LabtestMethods> selectMethodsList(LabtestMethods labtestMethods);

    /**
     * 新增检测方法
     * 
     * @param labtestMethods 检测方法
     * @return 结果
     */
    public int insertMethods(LabtestMethods labtestMethods);

    /**
     * 修改检测方法
     * 
     * @param labtestMethods 检测方法
     * @return 结果
     */
    public int updateMethods(LabtestMethods labtestMethods);

    /**
     * 删除检测方法
     * 
     * @param bsLabtestMethodsId 检测方法主键
     * @return 结果
     */
    public int deleteMethodsByMethodsId(String bsLabtestMethodsId);

    /**
     * 批量删除检测方法
     * 
     * @param bsLabtestMethodsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMethodsByMethodsIds(String[] bsLabtestMethodsIds);

    /**
     * 批量删除检测公式
     * 
     * @param bsLabtestMethodsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFormulaByMethodsIds(String[] bsLabtestMethodsIds);
    
    /**
     * 批量新增检测公式
     * 
     * @param labtestMethodsFormulaList 检测公式列表
     * @return 结果
     */
    public int batchFormula(List<LabtestMethodsFormula> labtestMethodsFormulaList);

    public void batchLabtestMethodInstrument(List<LabtestMethodInstrument> labtestMethodInstruments);
    /**
     * 通过检测方法主键删除检测公式信息
     * 
     * @param bsLabtestMethodsId 检测方法ID
     * @return 结果
     */
    public int deleteFormulaByMethodsId(String bsLabtestMethodsId);


    /**
     * 更新启用状态
     * @param labtestMethods
     * @return
     */
    public int updateEnableById(LabtestMethods labtestMethods);

    public void batchAttribute(List<LabtestMethodsAttribute> list);

    public void deleteAttributeByMethodsId(String bsLabtestMethodsId);

    public void deleteAttributeByMethodsIds(String[] bsLabtestMethodsIds);


}
