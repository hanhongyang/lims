package com.gmlimsqi.business.basicdata.service.impl;

import java.util.*;

import com.gmlimsqi.business.basicdata.domain.LabtestMethodInstrument;
import com.gmlimsqi.business.basicdata.domain.LabtestMethodsAttribute;
import com.gmlimsqi.business.basicdata.domain.LabtestMethodsFormula;
import com.gmlimsqi.business.basicdata.mapper.LabtestMethodInstrumentMapper;
import com.gmlimsqi.business.basicdata.mapper.LabtestMethodsAttributeMapper;
import com.gmlimsqi.business.basicdata.mapper.LabtestMethodsFormulaMapper;
import com.gmlimsqi.business.instrument.domain.Instruments;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.business.util.UserInfoProcessor;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import com.gmlimsqi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.gmlimsqi.business.basicdata.mapper.LabtestMethodsMapper;
import com.gmlimsqi.business.basicdata.domain.LabtestMethods;
import com.gmlimsqi.business.basicdata.service.ILabtestMethodsService;

/**
 * 检测方法Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
@Service
public class LabtestMethodsServiceImpl implements ILabtestMethodsService
{
    @Autowired
    private LabtestMethodsMapper labtestMethodsMapper;
    @Autowired
    private LabtestMethodsFormulaMapper formulaMapper;
    @Autowired
    private LabtestMethodsAttributeMapper attributeMapper;
    @Autowired
    private LabtestMethodInstrumentMapper labtestMethodInstrumentMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private UserInfoProcessor userInfoProcessor;

    /**
     * 查询检测方法
     * 
     * @param bsLabtestMethodsId 检测方法主键
     * @return 检测方法
     */
    @Override
    public LabtestMethods selectBsLabtestMethodsByBsLabtestMethodsId(String bsLabtestMethodsId)
    {
        LabtestMethods items = labtestMethodsMapper.selectMethodsByMethodsId(bsLabtestMethodsId);

        // 处理用户名
        if (items != null) {
            Map<String, String> usernameMap = userCacheService.batchGetUsernames(Collections.singleton(items.getCreateBy()));

            items.setCreateBy(usernameMap.get(items.getCreateBy()));
        }
        return items;

    }

    /**
     * 查询检测方法列表
     * 
     * @param labtestMethods 检测方法
     * @return 检测方法
     */
    @Override
    public List<LabtestMethods> selectBsLabtestMethodsList(LabtestMethods labtestMethods)
    {
        List<LabtestMethods> items = labtestMethodsMapper.selectMethodsList(labtestMethods);

        // 批量处理用户名（创建人和更新人）
        userInfoProcessor.processBaseEntityUserInfo(items);


        return items;
    }

    /**
     * 新增检测方法
     * 
     * @param labtestMethods 检测方法
     * @return 结果
     */
    @Transactional
    @Override
    public int insertBsLabtestMethods(LabtestMethods labtestMethods)
    {
        if (StringUtils.isEmpty(labtestMethods.getBsLabtestMethodsId())) {
            labtestMethods.setBsLabtestMethodsId(IdUtils.simpleUUID());
            for (LabtestMethodsAttribute labtestMethodsAttribute : labtestMethods.getLabtestMethodsAttributeList()) {
                labtestMethodsAttribute.setBsLabtestMethodsAttributeId(IdUtils.simpleUUID());
                labtestMethodsAttribute.setBsLabtestMethodsId(labtestMethods.getBsLabtestMethodsId());
                labtestMethodsAttribute.fillCreateInfo();
            }
            for (LabtestMethodsFormula labtestMethodsFormula : labtestMethods.getLabtestMethodsFormulaList()) {
                labtestMethodsFormula.setBsLabtestMethodsFormulaId(IdUtils.simpleUUID());
                labtestMethodsFormula.setBsLabtestMethodsId(labtestMethods.getBsLabtestMethodsId());
                labtestMethodsFormula.fillCreateInfo();
            }
            List<LabtestMethodInstrument> labtestMethodInstrumentList = new ArrayList<>();
            for (Instruments instruments : labtestMethods.getInstrumentsList()) {
                LabtestMethodInstrument labtestMethodInstrument = new LabtestMethodInstrument();
                labtestMethodInstrument.setLabtestMethodInstrumentId(IdUtils.simpleUUID());
                labtestMethodInstrument.setInstrumentsId(instruments.getBsInstrumentsId());
                labtestMethodInstrument.setLabtestMethodsId(labtestMethods.getBsLabtestMethodsId());
                labtestMethodInstrument.fillCreateInfo();
                labtestMethodInstrumentList.add(labtestMethodInstrument);
            }
            labtestMethods.setLabtestMethodInstruments(labtestMethodInstrumentList);
        }

        // 自动填充创建/更新信息
        labtestMethods.fillCreateInfo();

        int rows = labtestMethodsMapper.insertMethods(labtestMethods);
        insertMethodsFormula(labtestMethods);
        insertMethodsAttribute(labtestMethods);
        insertMethodsInsturment(labtestMethods);
        return rows;
    }



    /**
     * 修改检测方法
     * 
     * @param labtestMethods 检测方法
     * @return 结果
     */
    @Transactional
    @Override
    public int updateBsLabtestMethods(LabtestMethods labtestMethods)
    {
        Date now = DateUtils.getNowDate();
        String updateUserId = String.valueOf(SecurityUtils.getUserId());

        // 自动填充更新信息
        labtestMethods.fillUpdateInfo();
        //删除公式和属性，插入公式和属性
        attributeMapper.deleteByMethodsId(labtestMethods.getBsLabtestMethodsId(),now,updateUserId);
        for (LabtestMethodsAttribute labtestMethodsAttribute : labtestMethods.getLabtestMethodsAttributeList()) {
            labtestMethodsAttribute.setBsLabtestMethodsAttributeId(IdUtils.simpleUUID());
            labtestMethodsAttribute.setBsLabtestMethodsId(labtestMethods.getBsLabtestMethodsId());
            labtestMethodsAttribute.fillCreateInfo();
        }
        insertMethodsAttribute(labtestMethods);

        formulaMapper.deleteByMethodsId(labtestMethods.getBsLabtestMethodsId(),now,updateUserId);
        for (LabtestMethodsFormula labtestMethodsFormula : labtestMethods.getLabtestMethodsFormulaList()) {
            labtestMethodsFormula.setBsLabtestMethodsFormulaId(IdUtils.simpleUUID());
            labtestMethodsFormula.setBsLabtestMethodsId(labtestMethods.getBsLabtestMethodsId());
            labtestMethodsFormula.fillCreateInfo();
        }
        insertMethodsFormula(labtestMethods);

        labtestMethodInstrumentMapper.updateDeleteByMethodId(labtestMethods.getBsLabtestMethodsId(),now,updateUserId);
        List<LabtestMethodInstrument> labtestMethodInstrumentList = new ArrayList<>();
        for (Instruments instruments : labtestMethods.getInstrumentsList()) {
            LabtestMethodInstrument labtestMethodInstrument = new LabtestMethodInstrument();
            labtestMethodInstrument.setLabtestMethodInstrumentId(IdUtils.simpleUUID());
            labtestMethodInstrument.setInstrumentsId(instruments.getBsInstrumentsId());
            labtestMethodInstrument.setLabtestMethodsId(labtestMethods.getBsLabtestMethodsId());
            labtestMethodInstrument.fillCreateInfo();
            labtestMethodInstrumentList.add(labtestMethodInstrument);
        }
        labtestMethods.setLabtestMethodInstruments(labtestMethodInstrumentList);
        insertMethodsInsturment(labtestMethods);

        return labtestMethodsMapper.updateMethods(labtestMethods);
    }

    /**
     * 修改启用状态
     *
     * @param labtestMethods 检测方法
     * @return 结果
     */
    @Override
    public int updateEnableById(LabtestMethods labtestMethods)
    {
        // 自动填充更新信息
        labtestMethods.fillUpdateInfo();
        return labtestMethodsMapper.updateEnableById(labtestMethods);
    }

    /**
     * 批量删除检测方法
     * 
     * @param bsLabtestMethodsIds 需要删除的检测方法主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteBsLabtestMethodsByBsLabtestMethodsIds(String[] bsLabtestMethodsIds)
    {
        labtestMethodsMapper.deleteFormulaByMethodsIds(bsLabtestMethodsIds);
        labtestMethodsMapper.deleteAttributeByMethodsIds(bsLabtestMethodsIds);
        //TODO 删除设备关联表
        return labtestMethodsMapper.deleteMethodsByMethodsIds(bsLabtestMethodsIds);
    }

    /**
     * 删除检测方法信息
     * 
     * @param bsLabtestMethodsId 检测方法主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteBsLabtestMethodsByBsLabtestMethodsId(String bsLabtestMethodsId)
    {
        labtestMethodsMapper.deleteFormulaByMethodsId(bsLabtestMethodsId);
        labtestMethodsMapper.deleteAttributeByMethodsId(bsLabtestMethodsId);
        //TODO 删除设备关联表
        return labtestMethodsMapper.deleteMethodsByMethodsId(bsLabtestMethodsId);
    }

    /**
     * 新增检测公式信息
     * 
     * @param labtestMethods 检测方法对象
     */
    public void insertMethodsFormula(LabtestMethods labtestMethods)
    {
        List<LabtestMethodsFormula> labtestMethodsFormulaList = labtestMethods.getLabtestMethodsFormulaList();
        String bsLabtestMethodsId = labtestMethods.getBsLabtestMethodsId();
        if (StringUtils.isNotNull(labtestMethodsFormulaList))
        {
            List<LabtestMethodsFormula> list = new ArrayList<LabtestMethodsFormula>();
            for (LabtestMethodsFormula labtestMethodsFormula : labtestMethodsFormulaList)
            {
                labtestMethodsFormula.setBsLabtestMethodsId(bsLabtestMethodsId);
                list.add(labtestMethodsFormula);
            }
            if (list.size() > 0)
            {
                labtestMethodsMapper.batchFormula(list);
            }
        }
    }

    /**
     * 新增检测方法属性信息
     *
     * @param labtestMethods 检测方法对象
     */
    public void insertMethodsAttribute(LabtestMethods labtestMethods)
    {
        List<LabtestMethodsAttribute> labtestMethodsAttributeList = labtestMethods.getLabtestMethodsAttributeList();
        String bsLabtestMethodsId = labtestMethods.getBsLabtestMethodsId();
        if (StringUtils.isNotNull(labtestMethodsAttributeList))
        {
            List<LabtestMethodsAttribute> list = new ArrayList<LabtestMethodsAttribute>();
            for (LabtestMethodsAttribute labtestMethodsAttribute : labtestMethodsAttributeList)
            {
                labtestMethodsAttribute.setBsLabtestMethodsId(bsLabtestMethodsId);
                list.add(labtestMethodsAttribute);
            }
            if (list.size() > 0)
            {
                labtestMethodsMapper.batchAttribute(list);
            }
        }
    }

    /**
     * 新增检测方法设备信息
     *
     * @param labtestMethods 检测方法对象
     */
    private void insertMethodsInsturment(LabtestMethods labtestMethods) {
        List<LabtestMethodInstrument> labtestMethodInstruments = labtestMethods.getLabtestMethodInstruments();

        if (StringUtils.isNotNull(labtestMethodInstruments))
        {
            if (labtestMethodInstruments.size() > 0)
            {
                labtestMethodsMapper.batchLabtestMethodInstrument(labtestMethodInstruments);
            }
        }
    }
}
