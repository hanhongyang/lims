package com.gmlimsqi.business.basicdata.mapper;

import java.util.Date;
import java.util.List;
import com.gmlimsqi.business.basicdata.domain.LabtestMethodsAttribute;
import org.apache.ibatis.annotations.Param;

/**
 * 检测属性Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
public interface LabtestMethodsAttributeMapper
{
    /**
     * 查询检测属性
     * 
     * @param bsLabtestMethodsAttributeId 检测属性主键
     * @return 检测属性
     */
    public LabtestMethodsAttribute selectBsLabtestMethodsAttributeByBsLabtestMethodsAttributeId(String bsLabtestMethodsAttributeId);

    /**
     * 查询检测属性列表
     * 
     * @param labtestMethodsAttribute 检测属性
     * @return 检测属性集合
     */
    public List<LabtestMethodsAttribute> selectBsLabtestMethodsAttributeList(LabtestMethodsAttribute labtestMethodsAttribute);

    /**
     * 新增检测属性
     * 
     * @param labtestMethodsAttribute 检测属性
     * @return 结果
     */
    public int insertBsLabtestMethodsAttribute(LabtestMethodsAttribute labtestMethodsAttribute);

    /**
     * 修改检测属性
     * 
     * @param labtestMethodsAttribute 检测属性
     * @return 结果
     */
    public int updateBsLabtestMethodsAttribute(LabtestMethodsAttribute labtestMethodsAttribute);

    /**
     * 删除检测属性
     * 
     * @param bsLabtestMethodsAttributeId 检测属性主键
     * @return 结果
     */
    public int deleteBsLabtestMethodsAttributeByBsLabtestMethodsAttributeId(String bsLabtestMethodsAttributeId);

    /**
     * 批量删除检测属性
     * 
     * @param bsLabtestMethodsAttributeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBsLabtestMethodsAttributeByBsLabtestMethodsAttributeIds(String[] bsLabtestMethodsAttributeIds);


    /**
     * 更新某个方法的所有属性的isDelete=1
     * @param bsLabtestMethodsId
     */
    public void deleteByMethodsId( @Param("bsLabtestMethodsId")String bsLabtestMethodsId, @Param("now") Date now, @Param("updateUserId") String updateUserId);
}
