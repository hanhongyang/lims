package com.gmlimsqi.business.basicdata.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.annotation.DataScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.mapper.BsInspectionReportTemplateMapper;
import com.gmlimsqi.business.basicdata.domain.BsInspectionReportTemplate;
import com.gmlimsqi.business.basicdata.service.IBsInspectionReportTemplateService;

import static com.gmlimsqi.business.util.CodeGeneratorUtil.CODE_TYPE_INSPECTION_REPORT_TEMPLATE;
import static com.gmlimsqi.business.util.CodeGeneratorUtil.CODE_TYPE_MILK_FILLING_ORDER;

/**
 * 检验报告模板Service业务层处理
 * * @author hhy
 * @date 2025-11-26
 */
@Service
public class BsInspectionReportTemplateServiceImpl implements IBsInspectionReportTemplateService
{
    @Autowired
    private BsInspectionReportTemplateMapper bsInspectionReportTemplateMapper;
    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;

    /**
     * 查询检验报告模板
     * * @param id 检验报告模板主键
     * @return 检验报告模板
     */
    @Override
    public BsInspectionReportTemplate selectBsInspectionReportTemplateById(String id)
    {
        return bsInspectionReportTemplateMapper.selectBsInspectionReportTemplateById(id);
    }

    /**
     * 查询检验报告模板列表
     * * @param bsInspectionReportTemplate 检验报告模板
     * @return 检验报告模板
     */
    // +++ 新增：@DataScope 注解以激活数据权限切面 +++
    @Override
    public List<BsInspectionReportTemplate> selectBsInspectionReportTemplateList(BsInspectionReportTemplate bsInspectionReportTemplate)
    {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            bsInspectionReportTemplate.setDeptId(SecurityUtils.getDeptId().toString());
        }

        List<BsInspectionReportTemplate> items = bsInspectionReportTemplateMapper.selectBsInspectionReportTemplateList(bsInspectionReportTemplate);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(BsInspectionReportTemplate::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增检验报告模板
     * * @param bsInspectionReportTemplate 检验报告模板
     * @return 结果
     */
    @Override
    public int insertBsInspectionReportTemplate(BsInspectionReportTemplate bsInspectionReportTemplate)
    {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            throw new BusinessException("请联系管理员新增模板");
        }

        if (StringUtils.isEmpty(bsInspectionReportTemplate.getId())) {
            bsInspectionReportTemplate.setId(IdUtils.simpleUUID());
        }

        // +++ 自动填充部门ID，用于数据权限 +++
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser))
        {
            // 假设您的实体类中有名为 setDeptId 的方法
            // (请确保您的 BsInspectionReportTemplate 实体类中有 setDeptId(String deptId) 方法)
            bsInspectionReportTemplate.setDeptId(loginUser.getDeptId().toString());
        }
        // +++  +++

        try {
            String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_INSPECTION_REPORT_TEMPLATE);
            bsInspectionReportTemplate.setTemplateCode(resultNo);
        } catch (BusinessException e) {
            throw new RuntimeException("生成检验报告模板编号失败: " + e.getMessage());
        }

        // 自动填充创建/更新信息
        bsInspectionReportTemplate.fillCreateInfo();
        bsInspectionReportTemplate.setIsDelete("0");
        bsInspectionReportTemplate.setTemplateType("0");
        return bsInspectionReportTemplateMapper.insertBsInspectionReportTemplate(bsInspectionReportTemplate);
    }

    /**
     * 修改检验报告模板
     * * @param bsInspectionReportTemplate 检验报告模板
     * @return 结果
     */
    @Override
    public int updateBsInspectionReportTemplate(BsInspectionReportTemplate bsInspectionReportTemplate)
    {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            throw new BusinessException("请联系管理员修改模板");
        }

        // 自动填充更新信息
        bsInspectionReportTemplate.fillUpdateInfo();
        return bsInspectionReportTemplateMapper.updateBsInspectionReportTemplate(bsInspectionReportTemplate);
    }

}