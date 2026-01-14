package com.gmlimsqi.business.labtest.service.impl;

import com.gmlimsqi.business.basicdata.domain.LabtestItems;
import com.gmlimsqi.business.basicdata.mapper.LabtestItemsMapper;
import com.gmlimsqi.business.labtest.domain.OpOutentrustRegister;
import com.gmlimsqi.business.labtest.domain.OpOutentrustRegisterItem;
import com.gmlimsqi.business.labtest.domain.OpOutentrustRegisterSample;
import com.gmlimsqi.business.labtest.vo.OpTestItemVo;
import com.gmlimsqi.business.labtest.mapper.OpOutentrustRegisterItemMapper;
import com.gmlimsqi.business.labtest.mapper.OpOutentrustRegisterMapper;
import com.gmlimsqi.business.labtest.mapper.OpOutentrustRegisterSampleMapper;
import com.gmlimsqi.business.labtest.service.IOpOutentrustRegisterService;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 外部委托检测单Service业务层处理
 *
 * @author wgq
 * @date 2025-09-17
 */
@Slf4j
@Service
public class OpOutentrustRegisterServiceImpl implements IOpOutentrustRegisterService {
    @Autowired
    private OpOutentrustRegisterMapper opOutentrustRegisterMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private LabtestItemsMapper labtestItemsMapper;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;

    @Autowired
    private OpOutentrustRegisterSampleMapper opOutentrustRegisterSampleMapper;
    @Autowired
    private OpOutentrustRegisterItemMapper opOutentrustRegisterItemMapper;

    /**
     * 查询外部委托检测单
     *
     * @param id 外部委托检测单主键
     * @return 外部委托检测单
     */
    @Override
    public OpOutentrustRegister selectOpOutentrustRegisterById(String id) {
        OpOutentrustRegister opOutentrustRegister = opOutentrustRegisterMapper.selectOpOutentrustRegisterById(id);

        List<OpOutentrustRegisterSample> opOutentrustRegisterSampleList = opOutentrustRegisterSampleMapper.selectAllByOutentrustRegisterId(id);
        if (!ObjectUtils.isEmpty(opOutentrustRegisterSampleList)) {
            opOutentrustRegisterSampleList.forEach(opors -> {
                OpOutentrustRegisterItem opOutentrustRegisterItem = new OpOutentrustRegisterItem();
                opOutentrustRegisterItem.setOutentrustRegisterSampleId(opors.getOutentrustRegisterSampleId());
                List<OpOutentrustRegisterItem> opori = opOutentrustRegisterItemMapper.selectOpOutentrustRegisterItemList(opOutentrustRegisterItem);
                List<OpTestItemVo> testItem = new ArrayList<>();
                opori.forEach(oporsItem -> {
                            OpTestItemVo testItemVo = new OpTestItemVo();
                            testItemVo.setItemId(oporsItem.getItemsId());
                            testItemVo.setItemName(oporsItem.getItemName());
                            testItem.add(testItemVo);
                        }

                );
                opors.setTestItem(testItem);
            });


            opOutentrustRegister.setOpOutentrustRegisterSampleList(opOutentrustRegisterSampleList);
        }

        return opOutentrustRegister;
    }

    /**
     * 查询外部委托检测单列表
     *
     * @param opOutentrustRegister 外部委托检测单
     * @return 外部委托检测单
     */
    @Override
    public List<OpOutentrustRegister> selectOpOutentrustRegisterList(OpOutentrustRegister opOutentrustRegister) {
        List<OpOutentrustRegister> items = opOutentrustRegisterMapper.selectOpOutentrustRegisterList(opOutentrustRegister);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpOutentrustRegister::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增外部委托检测单
     *
     * @param opOutentrustRegister 外部委托检测单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertOpOutentrustRegister(OpOutentrustRegister opOutentrustRegister) {
        if (StringUtils.isEmpty(opOutentrustRegister.getId())) {
            opOutentrustRegister.setId(IdUtils.simpleUUID());
            for (OpOutentrustRegisterSample opOutentrustRegisterSample : opOutentrustRegister.getOpOutentrustRegisterSampleList()) {
                opOutentrustRegisterSample.setOutentrustRegisterSampleId(IdUtils.simpleUUID());
                opOutentrustRegisterSample.setOutentrustRegisterId(opOutentrustRegister.getId());
                opOutentrustRegisterSample.fillCreateInfo();
            }
        }
        // 自动填充创建/更新信息
        opOutentrustRegister.fillCreateInfo();
        int rows = opOutentrustRegisterMapper.insertOpOutentrustRegister(opOutentrustRegister);
        //插入子表
        try {
            insertOpOutentrustRegisterInfo(opOutentrustRegister);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rows;
    }

    /**
     * 修改外部委托检测单
     *
     * @param opOutentrustRegister 外部委托检测单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateOpOutentrustRegister(OpOutentrustRegister opOutentrustRegister) {
//        // 自动填充更新信息
//        opOutentrustRegister.fillUpdateInfo();
//        //更新子表删除标志并插入
//        opOutentrustRegisterMapper.updateOpOutentrustRegisterSampleDeleteToTrueByOutentrustRegisterSampleId(opOutentrustRegister.getId());
//        for (OpOutentrustRegisterSample opOutentrustRegisterSample : opOutentrustRegister.getOpOutentrustRegisterSampleList) {
//            opOutentrustRegisterSample.set$ {
//                subTablePkClassName
//            } (IdUtils.simpleUUID());
//            opOutentrustRegisterSample.setOutentrustRegisterSampleId(opOutentrustRegister.getId());
//            opOutentrustRegisterSample.fillCreateInfo();
//        }
//        insertOpOutentrustRegisterSample(opOutentrustRegister);
//        opOutentrustRegisterMapper.updateDeleteFlagById(opOutentrustRegister.getId());
        List<OpOutentrustRegisterSample> opOutentrustRegisterSampleList = opOutentrustRegister.getOpOutentrustRegisterSampleList();

        String[] ids = opOutentrustRegisterSampleList.stream()
                .map(OpOutentrustRegisterSample::getOutentrustRegisterSampleId) // 替换SampleClass为实际类名
                .toArray(String[]::new);
        opOutentrustRegisterMapper.updateDeleteFlagByOutentrustRegisterSampleIds(ids, DateUtils.getNowDate(), SecurityUtils.getUserId().toString());
        String id = opOutentrustRegister.getId();
        List<OpOutentrustRegisterSample>  list =  opOutentrustRegister.getOpOutentrustRegisterSampleList();
        if (!ObjectUtils.isEmpty(list)) {
            for (OpOutentrustRegisterSample opOutentrustRegisterSample : opOutentrustRegister.getOpOutentrustRegisterSampleList()) {
                opOutentrustRegisterSample.setOutentrustRegisterSampleId(IdUtils.simpleUUID());
                opOutentrustRegisterSample.setOutentrustRegisterId(id);
                opOutentrustRegisterSample.fillCreateInfo();
            }
        }

        // 自动填充创建/更新信息
        opOutentrustRegister.fillCreateInfo();
        int rows = opOutentrustRegisterMapper.updateOpOutentrustRegister(opOutentrustRegister);
        //插入子表
        try {
            insertOpOutentrustRegisterInfo(opOutentrustRegister);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rows;
    }

    /**
     * 批量删除外部委托检测单
     *
     * @param ids 需要删除的外部委托检测单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOpOutentrustRegisterByIds(String[] ids) {
        if (!ObjectUtils.isEmpty(ids)) {
            for (String id : ids) {
                opOutentrustRegisterMapper.updateDeleteFlagById(id);
                opOutentrustRegisterMapper.updateDeleteFlagByOutentrustRegisterId(id);

            }
        }
        return 1;
    }

    /**
     * 删除外部委托检测单信息
     *
     * @param id 外部委托检测单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOpOutentrustRegisterById(String id) {
        opOutentrustRegisterMapper.deleteOpOutentrustRegisterSampleByOutentrustRegisterSampleId(id);
        return opOutentrustRegisterMapper.deleteOpOutentrustRegisterById(id);
    }

    /**
     * 弃审/审核外部委托检测单
     */
    @Override
    public int enableOpOutentrustRegister(OpOutentrustRegister opOutentrustRegister) {
        String id = opOutentrustRegister.getId();
        if (ObjectUtils.isEmpty(id)) {
            log.error("未获取到单据id");
            throw new RuntimeException("服务器网络异常");
        }
        String isEnable = opOutentrustRegister.getIsEnable();
        if (ObjectUtils.isEmpty(isEnable)) {
            log.error("未获取到启用/弃用的按钮状态");
            throw new RuntimeException("服务器网络异常");
        }
        OpOutentrustRegister opor = opOutentrustRegisterMapper.selectOpOutentrustRegisterById(id);
        opor.setIsEnable(isEnable);
        return opOutentrustRegisterMapper.updateOpOutentrustRegister(opor);
    }

    /**
     * 新增外部委托检测单样品子信息
     *
     * @param opOutentrustRegister 外部委托检测单对象
     */
    public void insertOpOutentrustRegisterInfo(OpOutentrustRegister opOutentrustRegister) throws Exception {
        LabtestItems lti = new LabtestItems();
        //获取检测项目
        List<LabtestItems> labtestItems = labtestItemsMapper.selectBsLabtestItemsList(lti);
        if (ObjectUtils.isEmpty(labtestItems)) {
            throw new RuntimeException("服务器网络错误");
        }
        //检测项目id - 检测名称Map
        Map<String, String> itemMap = labtestItems.stream()
                .collect(Collectors.toMap(
                        LabtestItems::getLabtestItemsId,
                        LabtestItems::getItemName
                ));

        //检验样品子表
        List<OpOutentrustRegisterSample> opOutentrustRegisterSampleList = opOutentrustRegister.getOpOutentrustRegisterSampleList();
        //检验项目子表
        List<OpOutentrustRegisterItem> opOutentrustRegisterItemList = new ArrayList<>();
        String id = opOutentrustRegister.getId();

        if (StringUtils.isNotNull(opOutentrustRegisterSampleList)) {
            //收集检测项目子表
            for (OpOutentrustRegisterSample opSample : opOutentrustRegisterSampleList) {
//                opSample.setSampleNo(codeGeneratorUtil.generateSampleCode());
                opSample.setOutentrustRegisterSampleId(IdUtils.simpleUUID());
                opSample.setOutentrustRegisterId(opOutentrustRegister.getId());
                opSample.setSampleNo(codeGeneratorUtil.generateSampleCode());
                opSample.setIsDelete("0");
                opSample.setCreateBy(SecurityUtils.getUserId().toString());
                opSample.setCreateTime(new Date());
                //取出检测项目
                System.out.println(opSample.getItemId());
                String[] itemIds = opSample.getItemId().split(",");
                if (StringUtils.isNotEmpty(itemIds)) {
                    for (String itemId : itemIds) {
                        OpOutentrustRegisterItem opItem = new OpOutentrustRegisterItem();
                        opItem.setOutentrustRegisterSampleId(opSample.getOutentrustRegisterSampleId())
                                .setOutentrustRegisterId(id)
                                .setItemsId(itemId)
                                .setItemName(itemMap.get(itemId))
                                .setOutentrustRegisterItemId(IdUtils.simpleUUID())
                                .setSampleNo(opSample.getSampleNo())

                                .setIsDelete("0")
                                .setSampleName(opSample.getSampleName())
                                .setCreateBy(opSample.getCreateBy());
                        opItem.setCreateTime(opSample.getCreateTime());
                        opOutentrustRegisterItemList.add(opItem);
                    }
                }

            }
            //插入子表
            opOutentrustRegisterMapper.batchOpOutentrustRegisterSample(opOutentrustRegisterSampleList);
            opOutentrustRegisterMapper.batchOpOutentrustRegisterItem(opOutentrustRegisterItemList);
        }
    }
}
