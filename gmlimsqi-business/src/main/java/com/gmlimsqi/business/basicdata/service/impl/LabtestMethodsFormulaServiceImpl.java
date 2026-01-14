package com.gmlimsqi.business.basicdata.service.impl;

import java.util.List;

import com.gmlimsqi.business.basicdata.domain.LabtestMethodsFormula;
import com.gmlimsqi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.mapper.LabtestMethodsFormulaMapper;
import com.gmlimsqi.business.basicdata.service.ILabtestMethodsFormulaService;

/**
 * 检测公式Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
@Service
public class LabtestMethodsFormulaServiceImpl implements ILabtestMethodsFormulaService
{
    @Autowired
    private LabtestMethodsFormulaMapper labtestMethodsFormulaMapper;

    /**
     * 查询检测公式
     * 
     * @param bsLabtestMethodsFormulaId 检测公式主键
     * @return 检测公式
     */
    @Override
    public LabtestMethodsFormula selectBsLabtestMethodsFormulaByBsLabtestMethodsFormulaId(String bsLabtestMethodsFormulaId)
    {
        return labtestMethodsFormulaMapper.selectBsLabtestMethodsFormulaByBsLabtestMethodsFormulaId(bsLabtestMethodsFormulaId);
    }

    /**
     * 查询检测公式列表
     * 
     * @param labtestMethodsFormula 检测公式
     * @return 检测公式
     */
    @Override
    public List<LabtestMethodsFormula> selectBsLabtestMethodsFormulaList(LabtestMethodsFormula labtestMethodsFormula)
    {
        return labtestMethodsFormulaMapper.selectBsLabtestMethodsFormulaList(labtestMethodsFormula);
    }

    /**
     * 新增检测公式
     * 
     * @param labtestMethodsFormula 检测公式
     * @return 结果
     */
    @Override
    public int insertBsLabtestMethodsFormula(LabtestMethodsFormula labtestMethodsFormula)
    {
        labtestMethodsFormula.setCreateTime(DateUtils.getNowDate());
        return labtestMethodsFormulaMapper.insertBsLabtestMethodsFormula(labtestMethodsFormula);
    }

    /**
     * 修改检测公式
     * 
     * @param labtestMethodsFormula 检测公式
     * @return 结果
     */
    @Override
    public int updateBsLabtestMethodsFormula(LabtestMethodsFormula labtestMethodsFormula)
    {
        labtestMethodsFormula.setUpdateTime(DateUtils.getNowDate());
        return labtestMethodsFormulaMapper.updateBsLabtestMethodsFormula(labtestMethodsFormula);
    }

    /**
     * 批量删除检测公式
     * 
     * @param bsLabtestMethodsFormulaIds 需要删除的检测公式主键
     * @return 结果
     */
    @Override
    public int deleteBsLabtestMethodsFormulaByBsLabtestMethodsFormulaIds(String[] bsLabtestMethodsFormulaIds)
    {
        return labtestMethodsFormulaMapper.deleteBsLabtestMethodsFormulaByBsLabtestMethodsFormulaIds(bsLabtestMethodsFormulaIds);
    }

    /**
     * 删除检测公式信息
     * 
     * @param bsLabtestMethodsFormulaId 检测公式主键
     * @return 结果
     */
    @Override
    public int deleteBsLabtestMethodsFormulaByBsLabtestMethodsFormulaId(String bsLabtestMethodsFormulaId)
    {
        return labtestMethodsFormulaMapper.deleteBsLabtestMethodsFormulaByBsLabtestMethodsFormulaId(bsLabtestMethodsFormulaId);
    }
}
