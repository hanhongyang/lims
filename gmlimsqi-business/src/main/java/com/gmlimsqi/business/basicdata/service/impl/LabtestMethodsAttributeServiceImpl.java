package com.gmlimsqi.business.basicdata.service.impl;

import java.util.List;

import com.gmlimsqi.business.basicdata.domain.LabtestMethodsAttribute;
import com.gmlimsqi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.mapper.LabtestMethodsAttributeMapper;
import com.gmlimsqi.business.basicdata.service.ILabtestMethodsAttributeService;

/**
 * 检测属性Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
@Service
public class LabtestMethodsAttributeServiceImpl implements ILabtestMethodsAttributeService
{
    @Autowired
    private LabtestMethodsAttributeMapper labtestMethodsAttributeMapper;

    /**
     * 查询检测属性
     * 
     * @param bsLabtestMethodsAttributeId 检测属性主键
     * @return 检测属性
     */
    @Override
    public LabtestMethodsAttribute selectBsLabtestMethodsAttributeByBsLabtestMethodsAttributeId(String bsLabtestMethodsAttributeId)
    {
        return labtestMethodsAttributeMapper.selectBsLabtestMethodsAttributeByBsLabtestMethodsAttributeId(bsLabtestMethodsAttributeId);
    }

    /**
     * 查询检测属性列表
     * 
     * @param labtestMethodsAttribute 检测属性
     * @return 检测属性
     */
    @Override
    public List<LabtestMethodsAttribute> selectBsLabtestMethodsAttributeList(LabtestMethodsAttribute labtestMethodsAttribute)
    {
        return labtestMethodsAttributeMapper.selectBsLabtestMethodsAttributeList(labtestMethodsAttribute);
    }

    /**
     * 新增检测属性
     * 
     * @param labtestMethodsAttribute 检测属性
     * @return 结果
     */
    @Override
    public int insertBsLabtestMethodsAttribute(LabtestMethodsAttribute labtestMethodsAttribute)
    {
        labtestMethodsAttribute.setCreateTime(DateUtils.getNowDate());
        return labtestMethodsAttributeMapper.insertBsLabtestMethodsAttribute(labtestMethodsAttribute);
    }

    /**
     * 修改检测属性
     * 
     * @param labtestMethodsAttribute 检测属性
     * @return 结果
     */
    @Override
    public int updateBsLabtestMethodsAttribute(LabtestMethodsAttribute labtestMethodsAttribute)
    {
        labtestMethodsAttribute.setUpdateTime(DateUtils.getNowDate());
        return labtestMethodsAttributeMapper.updateBsLabtestMethodsAttribute(labtestMethodsAttribute);
    }

    /**
     * 批量删除检测属性
     * 
     * @param bsLabtestMethodsAttributeIds 需要删除的检测属性主键
     * @return 结果
     */
    @Override
    public int deleteBsLabtestMethodsAttributeByBsLabtestMethodsAttributeIds(String[] bsLabtestMethodsAttributeIds)
    {
        return labtestMethodsAttributeMapper.deleteBsLabtestMethodsAttributeByBsLabtestMethodsAttributeIds(bsLabtestMethodsAttributeIds);
    }

    /**
     * 删除检测属性信息
     * 
     * @param bsLabtestMethodsAttributeId 检测属性主键
     * @return 结果
     */
    @Override
    public int deleteBsLabtestMethodsAttributeByBsLabtestMethodsAttributeId(String bsLabtestMethodsAttributeId)
    {
        return labtestMethodsAttributeMapper.deleteBsLabtestMethodsAttributeByBsLabtestMethodsAttributeId(bsLabtestMethodsAttributeId);
    }
}
