package com.gmlimsqi.business.unquality.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

import com.gmlimsqi.business.unquality.domain.YktReadDTO;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.common.core.domain.entity.SysDept;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.business.util.UserCacheService;
// +++
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.annotation.DataScope;
// +++
import com.gmlimsqi.system.mapper.SysDeptMapper;
import org.mybatis.spring.annotation.MapperScannerRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
    import java.util.ArrayList;

    import org.springframework.transaction.annotation.Transactional;
    import com.gmlimsqi.business.unquality.domain.OpSampleUnqualityDetail;
import com.gmlimsqi.business.unquality.mapper.OpSampleUnqualityMapper;
import com.gmlimsqi.business.unquality.domain.OpSampleUnquality;
import com.gmlimsqi.business.unquality.service.IOpSampleUnqualityService;

import static com.gmlimsqi.business.util.CodeGeneratorUtil.*;
import static com.gmlimsqi.business.util.CodeGeneratorUtil.CODE_TYPE_YLQYD;

/**
 * 样品不合格处理单Service业务层处理
 * * @author hhy
 * @date 2025-11-28
 */
@Service
public class OpSampleUnqualityServiceImpl implements IOpSampleUnqualityService
{
    @Autowired
    private OpSampleUnqualityMapper opSampleUnqualityMapper;
    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;

    /**
     * 查询样品不合格处理单
     * * @param opSampleUnqualityId 样品不合格处理单主键
     * @return 样品不合格处理单
     */
    @Override
    public OpSampleUnquality selectOpSampleUnqualityByOpSampleUnqualityId(String opSampleUnqualityId)
    {
        return opSampleUnqualityMapper.selectOpSampleUnqualityByOpSampleUnqualityId(opSampleUnqualityId);
    }

    /**
     * 查询样品不合格处理单列表
     * * @param opSampleUnquality 样品不合格处理单
     * @return 样品不合格处理单
     */
    // +++ 新增：@DataScope 注解以激活数据权限切面 +++
    @DataScope(deptAlias = "d", permission = "unquality:unquality:list")
    @Override
    public List<OpSampleUnquality> selectOpSampleUnqualityList(OpSampleUnquality opSampleUnquality)
    {
        List<OpSampleUnquality> items = opSampleUnqualityMapper.selectOpSampleUnqualityList(opSampleUnquality);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpSampleUnquality::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增样品不合格处理单
     * * @param opSampleUnquality 样品不合格处理单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertOpSampleUnquality(OpSampleUnquality opSampleUnquality)
    {
        int count = 0;
        // 新增感官质检
        if ("感官不合格".equals(opSampleUnquality.getCtype())){
            // 根据来源id查询是否有值，有则修改感官不合格单
            OpSampleUnquality existing = opSampleUnqualityMapper.selectOpSampleUnqualityBySourceId(opSampleUnquality.getSourceId(),
                    "感官不合格");
            if (existing != null) {
                if ("1".equals(existing.getStatus())){
                    return 0;
                }
                // 更新现有记录，感官不合格单没有子表，无需操作子表
                opSampleUnquality.fillUpdateInfo();
                count = opSampleUnqualityMapper.updateOpSampleUnquality(opSampleUnquality);
            }else {
                if (StringUtils.isEmpty(opSampleUnquality.getOpSampleUnqualityId())) {
                    opSampleUnquality.setOpSampleUnqualityId(IdUtils.simpleUUID());
                }

                LoginUser loginUser = SecurityUtils.getLoginUser();
                if (StringUtils.isNotNull(loginUser))
                {
                    opSampleUnquality.setDeptId(loginUser.getDeptId().toString());
                    // 查询部门SAP编码
                    SysDept dept = sysDeptMapper.selectDeptById(loginUser.getDeptId());
                    opSampleUnquality.setSapName(dept.getSapName());
                }

                // 生成感官不合格处理单号
                try {
                    String resultNo = codeGeneratorUtil.generateUNQUALITYSensoryNo();
                    opSampleUnquality.setUnqualityNo(resultNo);
                } catch (BusinessException e) {
                    throw new RuntimeException("生成不合格ha单号失败: " + e.getMessage());
                }

                // 自动填充创建/更新信息
                opSampleUnquality.fillCreateInfo();
                count = opSampleUnqualityMapper.insertOpSampleUnquality(opSampleUnquality);
            }
        }

        // 化验不合格
        if ("化验不合格".equals(opSampleUnquality.getCtype())){
            // 根据来源id查询是否有值，有则修改化验不合格单
            OpSampleUnquality existing = opSampleUnqualityMapper.selectOpSampleUnqualityBySourceId(opSampleUnquality.getSourceId(),
                    "化验不合格");
            if (existing != null) {
                if ("1".equals(existing.getStatus())){
                    return 0;
                }
                // 更新现有记录，化验不合格单有子表，需要更新子表
                opSampleUnquality.fillUpdateInfo();

                // 判断子表是否存在该化验项目，不存在则新增子表
                List<OpSampleUnqualityDetail> existingDetails = opSampleUnqualityMapper.selectOpSampleUnqualityDetailByOpSampleUnqualityId(existing.getOpSampleUnqualityId());
                for (OpSampleUnqualityDetail opSampleUnqualityDetail : opSampleUnquality.getOpSampleUnqualityDetailList()){
                    // 检查子表是否存在该项目，根据项目id和主表id判断是否存在
                    boolean exists = existingDetails.stream()
                            .anyMatch(detail -> detail.getItemId().equals(opSampleUnqualityDetail.getItemId()) &&
                                    detail.getOpSampleUnqualityId().equals(existing.getOpSampleUnqualityId()));
                    if (!exists) {
                        // 设置子表主键
                        opSampleUnqualityDetail.setOpSampleUnqualityDetailId(IdUtils.simpleUUID());
                        // 新增子表记录
                        opSampleUnqualityMapper.insertOpSampleUnqualityDetail(opSampleUnqualityDetail);
                    } else {
                        // 更新子表记录
                        opSampleUnqualityMapper.updateOpSampleUnqualityDetail(opSampleUnqualityDetail);
                    }
                }

                count = opSampleUnqualityMapper.updateOpSampleUnquality(opSampleUnquality);
            }else {
                if (StringUtils.isEmpty(opSampleUnquality.getOpSampleUnqualityId())) {
                    opSampleUnquality.setOpSampleUnqualityId(IdUtils.simpleUUID());
                    for (OpSampleUnqualityDetail opSampleUnqualityDetail : opSampleUnquality.getOpSampleUnqualityDetailList()){
                        opSampleUnqualityDetail.setOpSampleUnqualityDetailId(IdUtils.simpleUUID());
                        opSampleUnqualityDetail.setOpSampleUnqualityId(opSampleUnquality.getOpSampleUnqualityId());
                    }
                }

                LoginUser loginUser = SecurityUtils.getLoginUser();
                if (StringUtils.isNotNull(loginUser))
                {
                    opSampleUnquality.setDeptId(loginUser.getDeptId().toString());
                    // 查询部门SAP编码
                    SysDept dept = sysDeptMapper.selectDeptById(loginUser.getDeptId());
                    opSampleUnquality.setSapName(dept.getSapName());
                }

                // 生成质检不合格处理单号
                try {
                    String resultNo = codeGeneratorUtil.generateUNQUALITYNo();
                    opSampleUnquality.setUnqualityNo(resultNo);
                } catch (BusinessException e) {
                    throw new RuntimeException("生成不合格单号失败: " + e.getMessage());
                }

                // 自动填充创建/更新信息
                opSampleUnquality.fillCreateInfo();
                count = opSampleUnqualityMapper.insertOpSampleUnquality(opSampleUnquality);
                insertOpSampleUnqualityDetail(opSampleUnquality);
            }
        }

        return count;
    }

    /**
     * 修改样品不合格处理单
     * * @param opSampleUnquality 样品不合格处理单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateOpSampleUnquality(OpSampleUnquality opSampleUnquality)
    {
        // 查询处理单状态
        OpSampleUnquality existing = opSampleUnqualityMapper.selectOpSampleUnqualityByOpSampleUnqualityId(opSampleUnquality.getOpSampleUnqualityId());
        if (existing == null) {
            throw new BusinessException("样品不合格处理单不存在");
        }

        // 检查处理单是否已完成
        if ("1".equals(existing.getStatus())){
            throw new BusinessException("已完成的处理单不能修改");
        }

        // 自动填充更新信息
        opSampleUnquality.fillUpdateInfo();
        //更新子表删除标志并插入
//        opSampleUnqualityMapper.updatedelete(opSampleUnquality.getOpSampleUnqualityId());

        if (opSampleUnquality.getOpSampleUnqualityDetailList() != null && !opSampleUnquality.getOpSampleUnqualityDetailList().isEmpty()){
            for (OpSampleUnqualityDetail opSampleUnqualityDetail : opSampleUnquality.getOpSampleUnqualityDetailList()){
                opSampleUnqualityDetail.setOpSampleUnqualityDetailId(IdUtils.simpleUUID());
                opSampleUnqualityDetail.setOpSampleUnqualityId(opSampleUnquality.getOpSampleUnqualityId());
            }
        }

        insertOpSampleUnqualityDetail(opSampleUnquality);
        return opSampleUnqualityMapper.updateOpSampleUnquality(opSampleUnquality);
    }

    @Override
    public List<OpSampleUnquality> selectOpSampleUnqualityBySignId(YktReadDTO yktReadDTO) {
        return opSampleUnqualityMapper.selectOpSampleUnqualityBySignId(yktReadDTO);
    }

    @Override
    public int updateOpSampleUnqualityByDiBang(List<String> opSampleUnqualityIds) {
        return opSampleUnqualityMapper.updateOpSampleUnqualityByDiBang(opSampleUnqualityIds);
    }

    @Override
    public int updateOpSampleUnqualityManually(String opSampleUnqualityId) {
        OpSampleUnquality opSampleUnquality = opSampleUnqualityMapper.selectOpSampleUnqualityByOpSampleUnqualityId(opSampleUnqualityId);
        if ("2".equals(opSampleUnquality.getProcessingType())){
            throw new RuntimeException("该处理单已完成 不需要关闭");
        }
        if (!"1".equals(opSampleUnquality.getProcessingType())){
            throw new RuntimeException("该处理单已完成 不需要关闭");
        }
        return opSampleUnqualityMapper.updateOpSampleUnqualityManually(opSampleUnqualityId);
    }


    /**
         * 新增样品不合格处理单详情信息
         * * @param opSampleUnquality 样品不合格处理单对象
         */
        public void insertOpSampleUnqualityDetail(OpSampleUnquality opSampleUnquality)
        {
            List<OpSampleUnqualityDetail> opSampleUnqualityDetailList = opSampleUnquality.getOpSampleUnqualityDetailList();
            String opSampleUnqualityId = opSampleUnquality.getOpSampleUnqualityId();
            if (StringUtils.isNotNull(opSampleUnqualityDetailList))
            {
                List<OpSampleUnqualityDetail> list = new ArrayList<OpSampleUnqualityDetail>();
                for (OpSampleUnqualityDetail opSampleUnqualityDetail : opSampleUnqualityDetailList)
                {
                    opSampleUnqualityDetail.setOpSampleUnqualityId(opSampleUnqualityId);
                    list.add(opSampleUnqualityDetail);
                }
                if (!list.isEmpty())
                {
                        opSampleUnqualityMapper.batchOpSampleUnqualityDetail(list);
                }
            }
        }
}
