package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.LabtestMethodsFormula;

/**
 * 检测公式Service接口
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
public interface ILabtestMethodsFormulaService
{
    /**
     * 查询检测公式
     * 
     * @param bsLabtestMethodsFormulaId 检测公式主键
     * @return 检测公式
     */
    public LabtestMethodsFormula selectBsLabtestMethodsFormulaByBsLabtestMethodsFormulaId(String bsLabtestMethodsFormulaId);

    /**
     * 查询检测公式列表
     * 
     * @param labtestMethodsFormula 检测公式
     * @return 检测公式集合
     */
    public List<LabtestMethodsFormula> selectBsLabtestMethodsFormulaList(LabtestMethodsFormula labtestMethodsFormula);

    /**
     * 新增检测公式
     * 
     * @param labtestMethodsFormula 检测公式
     * @return 结果
     */
    public int insertBsLabtestMethodsFormula(LabtestMethodsFormula labtestMethodsFormula);

    /**
     * 修改检测公式
     * 
     * @param labtestMethodsFormula 检测公式
     * @return 结果
     */
    public int updateBsLabtestMethodsFormula(LabtestMethodsFormula labtestMethodsFormula);

    /**
     * 批量删除检测公式
     * 
     * @param bsLabtestMethodsFormulaIds 需要删除的检测公式主键集合
     * @return 结果
     */
    public int deleteBsLabtestMethodsFormulaByBsLabtestMethodsFormulaIds(String[] bsLabtestMethodsFormulaIds);

    /**
     * 删除检测公式信息
     * 
     * @param bsLabtestMethodsFormulaId 检测公式主键
     * @return 结果
     */
    public int deleteBsLabtestMethodsFormulaByBsLabtestMethodsFormulaId(String bsLabtestMethodsFormulaId);
}
