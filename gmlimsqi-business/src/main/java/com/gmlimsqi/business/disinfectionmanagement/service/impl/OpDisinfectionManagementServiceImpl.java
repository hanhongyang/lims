package com.gmlimsqi.business.disinfectionmanagement.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.annotation.DataScope;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;
import com.gmlimsqi.business.disinfectionmanagement.domain.OpDisinfectionManagementRecord;
import com.gmlimsqi.business.disinfectionmanagement.mapper.OpDisinfectionManagementMapper;
import com.gmlimsqi.business.disinfectionmanagement.domain.OpDisinfectionManagement;
import com.gmlimsqi.business.disinfectionmanagement.service.IOpDisinfectionManagementService;

/**
 * 消毒管理Service业务层处理
 *
 * @author hhy
 * @date 2025-11-06
 */
@Service
public class OpDisinfectionManagementServiceImpl implements IOpDisinfectionManagementService {
    @Autowired
    private OpDisinfectionManagementMapper opDisinfectionManagementMapper;
    @Autowired
    private UserCacheService userCacheService;

    /**
     * 查询消毒管理
     *
     * @param disinfectionManagementId 消毒管理主键
     * @return 消毒管理
     */
    @Override
    public OpDisinfectionManagement selectOpDisinfectionManagementByDisinfectionManagementId(String disinfectionManagementId) {
        return opDisinfectionManagementMapper.selectOpDisinfectionManagementByDisinfectionManagementId(disinfectionManagementId);
    }

    /**
     * 查询消毒管理列表
     *
     * @param opDisinfectionManagement 消毒管理
     * @return 消毒管理
     */
//    @DataScope(deptAlias = "a")
    @Override
    public List<OpDisinfectionManagement> selectOpDisinfectionManagementList(OpDisinfectionManagement opDisinfectionManagement) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            opDisinfectionManagement.setDeptId(SecurityUtils.getDeptId().toString());
        }
        List<OpDisinfectionManagement> items = opDisinfectionManagementMapper.selectOpDisinfectionManagementList(opDisinfectionManagement);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpDisinfectionManagement::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 根据部门查询唯一可通过
     *
     * @param deptId 部门id
     * @return OpDisinfectionManagement 对象
     */
    @Override
    public OpDisinfectionManagement selectOnePassedByDeptId(Long deptId) {
        // 1. 查询
        List<OpDisinfectionManagement> list = opDisinfectionManagementMapper.selectOpDisinfectionManagementByDeptId(deptId);
        // 2. 筛选
        for (OpDisinfectionManagement dm : list) {
            if ("1".equals(dm.getPassed())) {
                return dm;
            }
        }
        return null;
    }

    /**
     * 新增消毒管理
     *
     * @param opDisinfectionManagement 消毒管理
     * @return 结果
     */
    @Transactional
    @Override
    public int insertOpDisinfectionManagement(OpDisinfectionManagement opDisinfectionManagement) {
        if (StringUtils.isEmpty(opDisinfectionManagement.getDisinfectionManagementId())) {
            Long deptId = SecurityUtils.getDeptId();
            opDisinfectionManagement.setDeptId(deptId.toString());
            opDisinfectionManagement.setDisinfectionManagementId(IdUtils.simpleUUID());

            List<OpDisinfectionManagementRecord> opDisinfectionManagementRecordList = opDisinfectionManagement.getOpDisinfectionManagementRecordList();

            if (opDisinfectionManagementRecordList != null && !opDisinfectionManagementRecordList.isEmpty()) {
                for (OpDisinfectionManagementRecord opDisinfectionManagementRecord : opDisinfectionManagementRecordList) {
                    opDisinfectionManagementRecord.setDisinfectionManagementRecordId(IdUtils.simpleUUID());
                    opDisinfectionManagementRecord.setDisinfectionManagementId(opDisinfectionManagement.getDisinfectionManagementId());
                    opDisinfectionManagementRecord.setIsDelete("0");
                    opDisinfectionManagementRecord.setDisinfectant(opDisinfectionManagement.getDisinfectant());
                    opDisinfectionManagementRecord.setDisinfectionManagementStatus(opDisinfectionManagement.getDisinfectionManagementStatus());
                    opDisinfectionManagementRecord.setDisinfectionTankName(opDisinfectionManagement.getDisinfectionTankName());
                    opDisinfectionManagementRecord.setConcentration(opDisinfectionManagement.getConcentration());
                    opDisinfectionManagementRecord.setDisinfectionTime(opDisinfectionManagement.getDisinfectionTime());
                    opDisinfectionManagementRecord.setDisinfectionMethod(opDisinfectionManagement.getDisinfectionMethod());
                    opDisinfectionManagementRecord.setDeptId(deptId.toString());
                    opDisinfectionManagementRecord.fillCreateInfo();
                }
            }
        }
        // 自动填充创建/更新信息
        opDisinfectionManagement.fillCreateInfo();
        int rows = opDisinfectionManagementMapper.insertOpDisinfectionManagement(opDisinfectionManagement);
        insertOpDisinfectionManagementRecord(opDisinfectionManagement);
        return rows;
    }

    /**
     * 修改消毒管理
     *
     * @param opDisinfectionManagement 消毒管理
     * @return 结果
     */
    @Transactional
    @Override
    public int updateOpDisinfectionManagement(OpDisinfectionManagement opDisinfectionManagement) {
        // 自动填充更新信息
        opDisinfectionManagement.fillUpdateInfo();
        // 更新子表删除标志并插入
//        opDisinfectionManagementMapper.updateOpDisinfectionManagementRecordDeleteToTrueByDisinfectionManagementId(opDisinfectionManagement.getDisinfectionManagementId());
        List<OpDisinfectionManagementRecord> list = new ArrayList<>();
        OpDisinfectionManagementRecord opDisinfectionManagementRecord = new OpDisinfectionManagementRecord();
        opDisinfectionManagementRecord.setDisinfectionManagementRecordId(IdUtils.simpleUUID());
        opDisinfectionManagementRecord.setDisinfectionManagementId(opDisinfectionManagement.getDisinfectionManagementId());
        opDisinfectionManagementRecord.setIsDelete("0");
        opDisinfectionManagementRecord.setDisinfectant(opDisinfectionManagement.getDisinfectant());
        opDisinfectionManagementRecord.setDisinfectionManagementStatus(opDisinfectionManagement.getDisinfectionManagementStatus());
        opDisinfectionManagementRecord.setDisinfectionTankName(opDisinfectionManagement.getDisinfectionTankName());
        opDisinfectionManagementRecord.setConcentration(opDisinfectionManagement.getConcentration());
        opDisinfectionManagementRecord.setDisinfectionTime(opDisinfectionManagement.getDisinfectionTime());
        opDisinfectionManagementRecord.setDisinfectionMethod(opDisinfectionManagement.getDisinfectionMethod());
        opDisinfectionManagementRecord.setDeptId(opDisinfectionManagement.getDeptId());
        opDisinfectionManagementRecord.fillCreateInfo();
        list.add(opDisinfectionManagementRecord);

        opDisinfectionManagement.setOpDisinfectionManagementRecordList(list);
        insertOpDisinfectionManagementRecord(opDisinfectionManagement);
        return opDisinfectionManagementMapper.updateOpDisinfectionManagement(opDisinfectionManagement);
    }

    /**
     * 批量删除消毒管理
     *
     * @param disinfectionManagementIds 需要删除的消毒管理主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOpDisinfectionManagementByDisinfectionManagementIds(String[] disinfectionManagementIds) {
        opDisinfectionManagementMapper.deleteOpDisinfectionManagementRecordByDisinfectionManagementIds(disinfectionManagementIds);
        return opDisinfectionManagementMapper.deleteOpDisinfectionManagementByDisinfectionManagementIds(disinfectionManagementIds);
    }

    /**
     * 删除消毒管理信息
     *
     * @param disinfectionManagementId 消毒管理主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOpDisinfectionManagementByDisinfectionManagementId(String disinfectionManagementId) {
        opDisinfectionManagementMapper.deleteOpDisinfectionManagementRecordByDisinfectionManagementId(disinfectionManagementId);
        return opDisinfectionManagementMapper.deleteOpDisinfectionManagementByDisinfectionManagementId(disinfectionManagementId);
    }

    /**
     * 新增消毒管理记录信息
     *
     * @param opDisinfectionManagement 消毒管理对象
     */
    public void insertOpDisinfectionManagementRecord(OpDisinfectionManagement opDisinfectionManagement) {
        List<OpDisinfectionManagementRecord> opDisinfectionManagementRecordList = opDisinfectionManagement.getOpDisinfectionManagementRecordList();
        String disinfectionManagementId = opDisinfectionManagement.getDisinfectionManagementId();
        if (StringUtils.isNotNull(opDisinfectionManagementRecordList)) {
            List<OpDisinfectionManagementRecord> list = new ArrayList<OpDisinfectionManagementRecord>();
            for (OpDisinfectionManagementRecord opDisinfectionManagementRecord : opDisinfectionManagementRecordList) {
                opDisinfectionManagementRecord.setDisinfectionManagementId(disinfectionManagementId);
                opDisinfectionManagementRecord.setIsDelete("0");
                opDisinfectionManagementRecord.setDisinfectionTankName(opDisinfectionManagement.getDisinfectionTankName());
                opDisinfectionManagementRecord.setConcentration(opDisinfectionManagement.getConcentration());
                opDisinfectionManagementRecord.setDisinfectionTime(opDisinfectionManagement.getDisinfectionTime());
                opDisinfectionManagementRecord.setDisinfectionMethod(opDisinfectionManagement.getDisinfectionMethod());
                opDisinfectionManagementRecord.setDisinfectant(opDisinfectionManagement.getDisinfectant());
                opDisinfectionManagementRecord.setDeptId(opDisinfectionManagement.getDeptId());
                list.add(opDisinfectionManagementRecord);
            }
            if (!list.isEmpty()) {
                opDisinfectionManagementMapper.batchOpDisinfectionManagementRecord(list);
            }
        }
    }
}
