package com.gmlimsqi.business.labtest.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import com.gmlimsqi.business.basicdata.service.IBsWorkdayConfigService;
import com.gmlimsqi.business.basicdata.mapper.BsContactMapper;
import com.gmlimsqi.business.labtest.domain.*;
import com.gmlimsqi.business.labtest.dto.EmailDTO;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedReportDto;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedResultInfoDto;
import com.gmlimsqi.business.labtest.mapper.OpFeedEntrustOrderMapper;
import com.gmlimsqi.business.labtest.mapper.OpFeedEntrustOrderSampleMapper;
import com.gmlimsqi.business.labtest.mapper.OpJczxFeedReportBaseMapper;
import com.gmlimsqi.business.labtest.service.IOpJczxFeedReportBaseService;
import com.gmlimsqi.business.labtest.service.IOpJczxFeedResultBaseService;
import com.gmlimsqi.business.labtest.vo.*;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.FileConvertUtils;
import com.gmlimsqi.common.core.domain.entity.SysDept;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.enums.FeedReportEnum;
import com.gmlimsqi.common.enums.JczxFeedReportStatusEnum;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.*;
import com.gmlimsqi.common.utils.email.EmailUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import com.gmlimsqi.system.mapper.SysUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 检测中心饲料报告主Service业务层处理
 *
 * @author hhy
 * @date 2025-10-14
 */
@Service
public class OpJczxFeedReportBaseServiceImpl implements IOpJczxFeedReportBaseService {
    @Autowired
    private OpJczxFeedReportBaseMapper opJczxFeedReportBaseMapper;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private OpFeedEntrustOrderSampleMapper opFeedEntrustOrderSampleMapper;

    @Autowired
    private OpFeedEntrustOrderMapper opFeedEntrustOrderMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    IBsWorkdayConfigService bsWorkdayConfigService;
    @Autowired
    private BsContactMapper bsContactMapper;
    @Autowired
    private IOpJczxFeedResultBaseService feedResultBaseService;
    @Value("${ruoyi.profile}")
    private String profile;


    /**
     * 查询检测中心饲料报告主
     *
     * @param opJczxFeedReportBaseId 检测中心饲料报告主主键
     * @return 检测中心饲料报告主
     */
    @Override
    public OpFeedReportVo selectOpJczxFeedReportBaseByOpJczxFeedReportBaseId(String opJczxFeedReportBaseId) {
        OpJczxFeedReportBase opJczxFeedReportBase = opJczxFeedReportBaseMapper.selectOpJczxFeedReportBaseByOpJczxFeedReportBaseId(opJczxFeedReportBaseId);
        if (ObjectUtils.isEmpty(opJczxFeedReportBase)) {
            throw new RuntimeException("未生成报告，无法查看");
        }
        OpFeedReportBaseVo feedReportBase = new OpFeedReportBaseVo();
        BeanUtils.copyProperties(opJczxFeedReportBase, feedReportBase);
        feedReportBase.setReceiveTime(DateUtils.parseDateToStr("yyyy-MM-dd", opJczxFeedReportBase.getReceiveTime()));
        List<OpJczxFeedReportInfo> opJczxFeedReportInfoList = opJczxFeedReportBaseMapper.selectReportInfoByBaseId(opJczxFeedReportBaseId);
        List<OpFeedReportInfoVo> reportInfoVos = new ArrayList<>();
        for (OpJczxFeedReportInfo opJczxFeedReportInfo : opJczxFeedReportInfoList) {
            OpFeedReportInfoVo reportInfoVo = new OpFeedReportInfoVo();
            BeanUtils.copyProperties(opJczxFeedReportInfo, reportInfoVo);
            reportInfoVos.add(reportInfoVo);
        }
        // 查询签名
        String editUserId = opJczxFeedReportBase.getEditUserId();
        String checkUserId = opJczxFeedReportBase.getCheckUserId();
        String approveUserId = opJczxFeedReportBase.getApproveUserId();
        if (!ObjectUtils.isEmpty(editUserId)) {
            String sign = sysUserMapper.selectSignByUserId(editUserId);
            feedReportBase.setEditSign(sign);
        }
        if (!ObjectUtils.isEmpty(checkUserId)) {
            String sign = sysUserMapper.selectSignByUserId(checkUserId);
            feedReportBase.setCheckSign(sign);
        }
        if (!ObjectUtils.isEmpty(approveUserId)) {
            String sign = sysUserMapper.selectSignByUserId(approveUserId);
            feedReportBase.setApproveSign(sign);
        }
        OpFeedReportVo opFeedReportVo = new OpFeedReportVo();
        opFeedReportVo.setFeedReportBase(feedReportBase);
        opFeedReportVo.setOpJczxFeedReportInfoList(reportInfoVos);
        return opFeedReportVo;
    }

    /**
     * 查询检测中心饲料报告主列表
     *
     * @param opJczxFeedReportBase 检测中心饲料报告主
     * @return 检测中心饲料报告主
     */
    @Override
    public List<OpFeedReportListVo> selectOpJczxFeedReportBaseList(OpJczxFeedReportDto opJczxFeedReportBase) {
        List<OpFeedReportListVo> items = new ArrayList<>();
        // 排除近红外数据
        opJczxFeedReportBase.setTestMethod("2");
        if (StringUtils.isNotEmpty(opJczxFeedReportBase.getStatus())) {
            if (JczxFeedReportStatusEnum.DZZ.getCode().equals(opJczxFeedReportBase.getStatus())) {
                items = opJczxFeedReportBaseMapper.selectOpJczxFeedReportBaseListStatus0(opJczxFeedReportBase);
            } else if (JczxFeedReportStatusEnum.YPZ.getCode().equals(opJczxFeedReportBase.getStatus())) {
                items = opJczxFeedReportBaseMapper.selectOpJczxFeedReportBaseListStatus4(opJczxFeedReportBase);
            } else {
                items = opJczxFeedReportBaseMapper.selectOpJczxFeedReportBaseList(opJczxFeedReportBase);
            }
        } else {
            throw new RuntimeException("缺少参数，联系管理员");
        }

        return items;
    }

    /**
     * 新增检测中心饲料报告主
     *
     * @param opJczxFeedReportBase 检测中心饲料报告主
     * @return 结果
     */
    @Transactional
    @Override
    public int insertOpJczxFeedReportBase(OpJczxFeedReportBase opJczxFeedReportBase) {
        if (StringUtils.isEmpty(opJczxFeedReportBase.getOpJczxFeedReportBaseId())) {
            opJczxFeedReportBase.setOpJczxFeedReportBaseId(IdUtils.simpleUUID());
            for (OpJczxFeedReportInfo opJczxFeedReportInfo : opJczxFeedReportBase.getOpJczxFeedReportInfoList()) {
                opJczxFeedReportInfo.setOpJczxFeedReportInfoId(IdUtils.simpleUUID());
                opJczxFeedReportInfo.setBaseId(opJczxFeedReportBase.getOpJczxFeedReportBaseId());
                opJczxFeedReportInfo.fillCreateInfo();
            }
        }
        // 自动填充创建/更新信息
        opJczxFeedReportBase.fillCreateInfo();
        int rows = opJczxFeedReportBaseMapper.insertOpJczxFeedReportBase(opJczxFeedReportBase);
        insertOpJczxFeedReportInfo(opJczxFeedReportBase);
        return rows;
    }

    /**
     * 修改检测中心饲料报告主
     *
     * @param opJczxFeedReportBase 检测中心饲料报告主
     * @return 结果
     */
    @Transactional
    @Override
    public int updateOpJczxFeedReportBase(OpJczxFeedReportBase opJczxFeedReportBase) {
        // 自动填充更新信息
        opJczxFeedReportBase.fillUpdateInfo();
        // 更新子表删除标志并插入
        String updateUserId = String.valueOf(SecurityUtils.getUserId());
        opJczxFeedReportBaseMapper.updateInfoDeleteByBaseId(opJczxFeedReportBase.getOpJczxFeedReportBaseId(), updateUserId);
        for (OpJczxFeedReportInfo opJczxFeedReportInfo : opJczxFeedReportBase.getOpJczxFeedReportInfoList()) {
            opJczxFeedReportInfo.setOpJczxFeedReportInfoId(IdUtils.simpleUUID());
            opJczxFeedReportInfo.setBaseId(opJczxFeedReportBase.getOpJczxFeedReportBaseId());
            opJczxFeedReportInfo.fillCreateInfo();
        }
        insertOpJczxFeedReportInfo(opJczxFeedReportBase);
        return opJczxFeedReportBaseMapper.updateOpJczxFeedReportBase(opJczxFeedReportBase);
    }

    @Override
    public OpFeedReportVo selecReportMakeInfoByEntrustOrderSampleId(String entrustOrderSampleId) {
        List<OpJczxFeedReportBase> reportBases = opJczxFeedReportBaseMapper.selectReportBaseBySampleId(entrustOrderSampleId);
        if (ObjectUtils.isEmpty(reportBases)) {
            throw new RuntimeException("服务器网络错误");
        }
        OpJczxFeedReportBase opJczxFeedReportBase = reportBases.get(0);
        OpFeedReportBaseVo opFeedReportBaseVo = new OpFeedReportBaseVo();
        BeanUtils.copyProperties(opJczxFeedReportBase, opFeedReportBaseVo);
        String testLocation = "光明牧业有限公司检验测试中心";
        opFeedReportBaseVo.setTestLocation(testLocation);
        opFeedReportBaseVo.setConclusion("根据委托方的要求，仅提供实测数据，详见检测结果汇总页。");
        opFeedReportBaseVo.setRemark("样品信息为客户提供，实验室不对其负责。");
        opFeedReportBaseVo.setTestType("委托检测");

        opFeedReportBaseVo.setReceiveTime(DateUtils.parseDateToStr("yyyy-MM-dd", opJczxFeedReportBase.getReceiveTime()));
        if (reportBases.size() > 1) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // 处理检测时间、仪器、检测项目
            String testTime = calculateAndFormatTimeRange(reportBases, formatter);
            String instrumentNames = collectInstrumentNames2(reportBases);
            String itemNames = collectItemNames(reportBases);
            opFeedReportBaseVo.setTestTime(testTime);
            opFeedReportBaseVo.setMainInstrument(instrumentNames);
            opFeedReportBaseVo.setItemName(itemNames);

        }
        List<OpJczxFeedResultInfoDto> infoVoList = opJczxFeedReportBaseMapper.selectReportInfoBySampleId(entrustOrderSampleId);
        List<OpFeedReportInfoVo> reportInfoVos = new ArrayList<>();
        for (OpJczxFeedResultInfoDto opFeedReportInfoVo : infoVoList) {
            OpFeedReportInfoVo reportInfoVo = new OpFeedReportInfoVo();
            if (!StringUtils.isEmpty(opFeedReportInfoVo.getModelNo())) {
                if (opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-53") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-19") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-18") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-51")) {
                    if (StringUtils.isEmpty(opFeedReportInfoVo.getXyzhl())) {
                        reportInfoVo.setValueOfTest(opFeedReportInfoVo.getYpzxxhl());
                    } else {
                        reportInfoVo.setValueOfTest(opFeedReportInfoVo.getXyzhl());
                    }
                } else if (opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-17") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-11")) {
                    // 拼上结果范围
                    if (StringUtils.isEmpty(opFeedReportInfoVo.getXyzhl())) {
                        if (!StringUtils.isEmpty(opFeedReportInfoVo.getJcjgfw())) {
                            String jcjgfw = "(" + opFeedReportInfoVo.getJcjgfw() + ")";
                            reportInfoVo.setValueOfTest(opFeedReportInfoVo.getJcjg() + jcjgfw);
                        } else {
                            reportInfoVo.setValueOfTest(opFeedReportInfoVo.getJcjg());
                        }
                    } else {
                        reportInfoVo.setValueOfTest(opFeedReportInfoVo.getXyzhl());
                    }

                } else if (opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-02") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-03") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-04") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-06") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-07") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-08") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-10") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-14") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-47") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-54") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-13") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-04") ||
                        opFeedReportInfoVo.getModelNo().equals("SHGM/JJ-04")) {
                    if (StringUtils.isEmpty(opFeedReportInfoVo.getXyzhl())) {
                        reportInfoVo.setValueOfTest(opFeedReportInfoVo.getAverage());
                    } else {
                        reportInfoVo.setValueOfTest(opFeedReportInfoVo.getXyzhl());
                    }
                } else {
                    reportInfoVo.setValueOfTest(opFeedReportInfoVo.getAverage());
                }
            } else {
                // 近红外
                if (StringUtils.isEmpty(opFeedReportInfoVo.getXyzhl())) {
                    reportInfoVo.setValueOfTest(opFeedReportInfoVo.getAverage());
                } else {
                    reportInfoVo.setValueOfTest(opFeedReportInfoVo.getXyzhl());
                }
                reportInfoVo.setUnit("%");
            }

            reportInfoVo.setItemName(opFeedReportInfoVo.getItemName());
            reportInfoVo.setItemId(opFeedReportInfoVo.getItemId());
            // reportInfoVo.setTestBasis(opFeedReportInfoVo.getTestBasis());
            reportInfoVo.setStandard(opFeedReportInfoVo.getStandard());
            reportInfoVo.setSampleNo(opFeedReportInfoVo.getSampleNo());
            reportInfoVo.setEntrustOrderItemId(opFeedReportInfoVo.getEntrustOrderItemId());
            String unitOfMeasurement = DictUtils.getDictLabel("unit_of_measurement", opFeedReportInfoVo.getUnit());
            reportInfoVo.setUnit(unitOfMeasurement);
            reportInfoVos.add(reportInfoVo);

        }
        OpFeedReportVo opFeedReportVo = new OpFeedReportVo();
        opFeedReportVo.setFeedReportBase(opFeedReportBaseVo);
        opFeedReportVo.setOpJczxFeedReportInfoList(reportInfoVos);
        return opFeedReportVo;
    }

    @Override
    @Transactional
    public String saveOpJczxFeedReport(OpJczxFeedReportBase reportBase) {
        // status： 1保存 2提交
        if (ObjectUtils.isEmpty(reportBase)) {
            throw new RuntimeException("服务器网络异常");
        }
        String status = reportBase.getStatus();
        if (ObjectUtils.isEmpty(status)) {
            throw new RuntimeException("服务器网络异常");
        }

        String opJczxFeedReportBaseId = reportBase.getOpJczxFeedReportBaseId();
        String sampleId = reportBase.getFeedEntrustOrderSampleId();
        if (Objects.isNull(sampleId)) {
            throw new RuntimeException("服务器网络异常");
        }
        // 根据是否传输reportBaseId,判断是否首次保存
        String updateUserId = String.valueOf(SecurityUtils.getUserId());
        SysUser sysUser = sysUserMapper.selectUserById(SecurityUtils.getUserId());
        String username = sysUser.getNickName();
        // 更新
        if (StringUtils.isNotEmpty(opJczxFeedReportBaseId)) {
            // 主表更新
            reportBase.setUpdateBy(updateUserId);
            reportBase.setUpdateTime(DateTime.now());
            OpJczxFeedReportBase opJczxFeedReportBase = opJczxFeedReportBaseMapper.selectOpJczxFeedReportBaseByOpJczxFeedReportBaseId(opJczxFeedReportBaseId);
            reportBase.setReportNo(opJczxFeedReportBase.getReportNo());
            opJczxFeedReportBaseMapper.updateOpJczxFeedReportBase(reportBase);
            // 子表先删除，再插入
            opJczxFeedReportBaseMapper.updateInfoDeleteByBaseId(opJczxFeedReportBaseId, updateUserId);

        } else {
            // 新增
            List<OpJczxFeedReportBase> opJczxFeedReportBases = opJczxFeedReportBaseMapper.selectReportBaseBySampleId2(sampleId);
            if (!CollectionUtils.isAnyEmpty(opJczxFeedReportBases)) {
                throw new RuntimeException("重复保存，请刷新页面");
            }
            OpJczxFeedReportBase reportBaseNew = new OpJczxFeedReportBase();
            BeanUtils.copyProperties(reportBase, reportBaseNew);
            opJczxFeedReportBaseId = IdUtils.simpleUUID();
            reportBaseNew.setMainInsturment(reportBase.getMainInstrument());
            reportBaseNew.setOpJczxFeedReportBaseId(opJczxFeedReportBaseId);
            reportBaseNew.setFeedEntrustOrderSampleId(sampleId);
            reportBaseNew.fillCreateInfo();
            reportBaseNew.setReportNo(codeGeneratorUtil.generateFeedReportNo());
            reportBaseNew.setEditUserId(updateUserId);
            reportBaseNew.setEditUser(username);
            reportBaseNew.setTestType("委托检测");
            opJczxFeedReportBaseMapper.insertOpJczxFeedReportBase(reportBaseNew);
        }
        List<OpJczxFeedReportInfo> opJczxFeedReportInfoList = reportBase.getOpJczxFeedReportInfoList();
        for (OpJczxFeedReportInfo opJczxFeedReportInfo : opJczxFeedReportInfoList) {
            // opJczxFeedReportInfo.setStandard(opJczxFeedReportInfo.getTestBasis());
            opJczxFeedReportInfo.setBaseId(opJczxFeedReportBaseId);
            opJczxFeedReportInfo.setSampleNo(reportBase.getSampleNo());
            opJczxFeedReportInfo.setEntrustOrderItemId(opJczxFeedReportInfo.getEntrustOrderItemId());
            opJczxFeedReportInfo.setOpJczxFeedReportInfoId(IdUtils.simpleUUID());
            opJczxFeedReportInfo.fillCreateInfo();
        }

        if (!opJczxFeedReportInfoList.isEmpty()) {
            // 子表插入
            opJczxFeedReportBaseMapper.batchOpJczxFeedReportInfo(opJczxFeedReportInfoList);
        }

        return opJczxFeedReportBaseId;
    }

    /**
     * 发送检测中心饲料报告邮件
     *
     * @param emailDTOList
     * @return
     */
    @Override
    public int sendOpJczxFeedReport(List<EmailDTO> emailDTOList) throws IOException {
        if (emailDTOList.isEmpty()) {
            throw new ServiceException("请选择要发送的报告");
        }
        // emailDTOList数量总是1，
        for (EmailDTO emailDTO : emailDTOList) {
            // 获取委托单号id
            String feedEntrustOrderId = emailDTO.getFeedEntrustOrderId();
            if (StringUtils.isEmpty(feedEntrustOrderId)) {
                throw new ServiceException("缺少必要参数");
            }
            OpFeedEntrustOrder opFeedEntrustOrder1 = opFeedEntrustOrderMapper.selectOrderDetailById(feedEntrustOrderId);
            if (ObjectUtils.isEmpty(opFeedEntrustOrder1)) {
                throw new ServiceException("获取委托单数据失败");
            }
            if (CollectionUtils.isAnyEmpty(opFeedEntrustOrder1.getSampleList())) {
                throw new ServiceException("委托单样品查询失败，无法发送");
            }
            // 委托单样品数量
            Long orderSampleCount = Long.valueOf(opFeedEntrustOrder1.getSampleList().size());

            List<OpJczxFeedReportBase> baseList = opJczxFeedReportBaseMapper.selectReportBaseByOrderId(feedEntrustOrderId);
            if (ObjectUtils.isEmpty(baseList)) {
                throw new ServiceException("获取报告数据失败");
            }
            // 已发送数量
            long yfsCount = baseList.stream().filter(base -> "5".equals(base.getStatus())).count();
            // 已批准数量
            long ypzCount = baseList.stream().filter(base -> "4".equals(base.getStatus())).count();
            // 已批准的报告数量要大于0且委托单样品数量等于已发送数量加已批准数量
            if (orderSampleCount == yfsCount + ypzCount && ypzCount > 0) {
                System.out.println("报告全部批准，开始发送");
                System.out.println("orderSampleCount:" + orderSampleCount + ",yfsCount:" + yfsCount + ",ypzCount:" + ypzCount);
            } else {
                throw new ServiceException("委托单样品对应的报告未全部批准，无法发送");
            }

            // 根据委托单主表id查询邮箱
            OpFeedEntrustOrder opFeedEntrustOrder = opFeedEntrustOrderMapper.selectOpFeedEntrustOrderByOpFeedEntrustOrderId(feedEntrustOrderId);
            if (ObjectUtils.isEmpty(opFeedEntrustOrder)) {
                throw new ServiceException("获取邮箱数据失败");
            }
            // 获取当前登录人的部门
            Long deptId = SecurityUtils.getDeptId();
            if (Objects.isNull(deptId)) {
                throw new ServiceException("部门为空");
            }
            // 根据部门id查询邮箱类型和授权码
            SysDept sysDept = sysDeptMapper.selectDeptById(deptId);
            if (ObjectUtils.isEmpty(sysDept)) {
                throw new ServiceException("获取部门邮箱数据失败");
            }
            String sendDate = DateUtils.getDate();

            // 遍历报告，发送
            for (OpJczxFeedReportBase opJczxFeedReportBase : baseList) {
                opJczxFeedReportBase.setSendEmailTime(new Date());
                opJczxFeedReportBase.setSendEmailUserId(String.valueOf(SecurityUtils.getUserId()));
                if (StringUtils.isEmpty(opJczxFeedReportBase.getPdfFileInfo())) {
                    throw new ServiceException(opJczxFeedReportBase.getReportNo() + " 报告文件不存在");
                }

                // 查询文件
                MultipartFile file = FileConvertUtils.getMultipartFileByAbsolutePath(profile, opJczxFeedReportBase.getPdfFileInfo());
                if (ObjectUtils.isEmpty(file)) {
                    throw new ServiceException("请上传文件");
                }

                MultipartFile[] files = {file};

                if (ObjectUtils.isEmpty(opJczxFeedReportBase)) {
                    throw new ServiceException("获取报告数据失败");
                }

                if (!"4".equals(opJczxFeedReportBase.getStatus()) && !"5".equals(opJczxFeedReportBase.getStatus())) {
                    throw new ServiceException("状态错误，无法发送");
                }


                // 从委托单主表中获取邮箱
                String toEmail = opFeedEntrustOrder.getEntrustContactEmail();
                if (StringUtils.isEmpty(toEmail)) {
                    throw new ServiceException("获取邮箱失败");
                }


                // 获取通讯方式表邮箱
                String toEmailUser = emailDTO.getEmails();
                Set<String> emailSet = new LinkedHashSet<>();

                // 统一处理逻辑
                String[] emailSources = {toEmail, toEmailUser};
                for (String emailSource : emailSources) {
                    if (StringUtils.isNotEmpty(emailSource)) {
                        String[] emails = emailSource.split(",");
                        for (String email : emails) {
                            if (StringUtils.isNotEmpty(email.trim())) {
                                emailSet.add(email.trim());
                            }
                        }
                    }
                }

                String finalEmails = String.join(",", emailSet);


                // 从部门中获取邮箱和授权码
                // 从部门中获取邮箱
                String deptEmail = sysDept.getEmail();
                if (StringUtils.isEmpty(deptEmail)) {
                    throw new ServiceException("获取部门邮箱失败");
                }

                // 从部门中获取授权码
                String emailAuthCode = sysDept.getEmailAuthorizationCode();
                if (StringUtils.isEmpty(emailAuthCode)) {
                    throw new ServiceException("邮箱未授权，请联系管理员");
                }

                // 发送邮件
                String[] toEmails = finalEmails.split(",");

                // 计算工作日

                int gzr = bsWorkdayConfigService.calculateWorkDays(opJczxFeedReportBase.getReceiveTime(), new Date());
                String deptName = opJczxFeedReportBase.getEntrustDeptName();
                String content = "您好!\n" +
                        "　　检测中心于" + DateUtils.parseDateToStr("yyyy-MM-dd", opJczxFeedReportBase.getReceiveTime()) + "收到"+deptName+"样品编号为" + opJczxFeedReportBase.getSampleNo() + "样品。检测报告于" + sendDate + "已发送至您邮箱。此次检测从样品接收至报告发送共使用" + gzr + "个工作日。\n" +
                        "　　注意查收！顺祝工作愉快！\n" +
                        "\n" +
                        "　　光明牧业有限公司检验测试中心\n" +
                        "　　Add：上海市闵行区万源路2729号309室\n" +
                        "　　Tel：021-56481688*8105";
                try {
                    System.out.println("开始发送邮件 toEmails:" + Arrays.toString(toEmails));
                    EmailUtils.sendEmail(deptEmail, emailAuthCode, toEmails,
                            "光明牧业有限公司检验测试中心饲料报告",
                            content,
                            false,
                            files);
                } catch (MessagingException | IOException e) {
                    e.printStackTrace();
                    throw new ServiceException("发送邮件失败" + e.getMessage());
                }

                opJczxFeedReportBase.setStatus("5");
                opJczxFeedReportBaseMapper.updateOpJczxFeedReportBase(opJczxFeedReportBase);
            }
        }

/*
        for (EmailDTO emailDTO : emailDTOList) {
            // 查询数据
            String opJczxFeedReportBaseId = emailDTO.getOpJczxFeedReportBaseId();
            if (StringUtils.isEmpty(opJczxFeedReportBaseId)) {
                throw new ServiceException("获取报告数据失败");
            }

            OpJczxFeedReportBase opJczxFeedReportBase = opJczxFeedReportBaseMapper.
                    selectOpJczxFeedReportBaseByOpJczxFeedReportBaseId(opJczxFeedReportBaseId);

            if (StringUtils.isEmpty(opJczxFeedReportBase.getPdfFileInfo())) {
                throw new ServiceException(opJczxFeedReportBase.getReportNo() + " 报告文件不存在");
            }

            // 查询文件
            MultipartFile file = FileConvertUtils.getMultipartFileByAbsolutePath(profile, opJczxFeedReportBase.getPdfFileInfo());
            if (ObjectUtils.isEmpty(file)) {
                throw new ServiceException("请上传文件");
            }

            MultipartFile[] files = {file};

            if (ObjectUtils.isEmpty(opJczxFeedReportBase)) {
                throw new ServiceException("获取报告数据失败");
            }

            if (!"4".equals(opJczxFeedReportBase.getStatus()) && !"5".equals(opJczxFeedReportBase.getStatus())) {
                throw new ServiceException("状态错误，无法发送");
            }

            // 饲料样品委托单-样品表ID
            String feedEntrustOrderSampleId = emailDTO.getFeedEntrustOrderSampleId();

            if (StringUtils.isEmpty(feedEntrustOrderSampleId)) {
                throw new ServiceException("获取样品表数据失败");
            }

            // 根据样品表id查询feed_entrust_order_id
            String feedEntrustOrderId = opFeedEntrustOrderSampleMapper.
                    selectOrderIdById(feedEntrustOrderSampleId);

            if (ObjectUtils.isEmpty(feedEntrustOrderId)) {
                throw new ServiceException("获取委托数据失败");
            }

            // 根据委托单主表id查询邮箱
            OpFeedEntrustOrder opFeedEntrustOrder = opFeedEntrustOrderMapper.selectOpFeedEntrustOrderByOpFeedEntrustOrderId(feedEntrustOrderId);
            if (ObjectUtils.isEmpty(opFeedEntrustOrder)) {
                throw new ServiceException("获取邮箱数据失败");
            }

            // 从委托单主表中获取邮箱
            String toEmail = opFeedEntrustOrder.getEntrustContactEmail();
            if (StringUtils.isEmpty(toEmail)) {
                throw new ServiceException("获取邮箱失败");
            }

            // 获取当前登录人的部门
            Long deptId = SecurityUtils.getDeptId();
            if (Objects.isNull(deptId)) {
                throw new ServiceException("部门为空");
            }

            // 根据部门id查询邮箱类型和授权码
            SysDept sysDept = sysDeptMapper.selectDeptById(deptId);
            if (ObjectUtils.isEmpty(sysDept)) {
                throw new ServiceException("获取部门邮箱数据失败");
            }

            // 从部门中获取邮箱和授权码
            // 从部门中获取邮箱
            String deptEmail = sysDept.getEmail();
            if (StringUtils.isEmpty(deptEmail)) {
                throw new ServiceException("获取部门邮箱失败");
            }

            // 从部门中获取授权码
            String emailAuthCode = sysDept.getEmailAuthorizationCode();
            if (StringUtils.isEmpty(emailAuthCode)) {
                throw new ServiceException("邮箱未授权，请联系管理员");
            }

            // 发送邮件
            String[] toEmails = toEmail.split(",");

            try {
                EmailUtils.sendEmail(deptEmail, emailAuthCode, toEmails,
                        "检测中心饲料报告",
                        "请查收检测中心饲料报告",
                        false,
                        files);
            } catch (MessagingException | IOException e) {
                throw new ServiceException("发送邮件失败");
            }

            opJczxFeedReportBase.setStatus("5");
            opJczxFeedReportBaseMapper.updateOpJczxFeedReportBase(opJczxFeedReportBase);
        }*/

        return 1;
    }


    /**
     * 发送检测中心饲料报告邮件
     *
     * @param emailDTOList
     * @return
     */
    @Override
    public int sendOpJczxFeedReport2(List<EmailDTO> emailDTOList) throws IOException {
        if (CollectionUtils.isAnyEmpty(emailDTOList)) {
            throw new ServiceException("请选择要发送的报告");
        }

        // 1. 获取当前登录人的部门及发送方邮箱配置（提到循环外，只查一次）
        Long deptId = SecurityUtils.getDeptId();
        if (Objects.isNull(deptId)) {
            throw new ServiceException("当前登录用户部门为空");
        }
        SysDept sysDept = sysDeptMapper.selectDeptById(deptId);
        if (ObjectUtils.isEmpty(sysDept)) {
            throw new ServiceException("获取部门数据失败");
        }
        String deptEmail = sysDept.getEmail();
        String emailAuthCode = sysDept.getEmailAuthorizationCode();

        if (StringUtils.isEmpty(deptEmail) || StringUtils.isEmpty(emailAuthCode)) {
            throw new ServiceException("部门邮箱或授权码未配置，请联系管理员");
        }

        String sendDate = DateUtils.getDate();
        int successCount = 0;

        // 2. 遍历前端传来的选中列表，逐个发送
        for (EmailDTO emailDTO : emailDTOList) {
            String reportBaseId = emailDTO.getOpJczxFeedReportBaseId();
            if (StringUtils.isEmpty(reportBaseId)) {
                // 如果前端传的是 sampleId，也可以在这里根据 sampleId 查 reportBaseId，但推荐直接传 reportBaseId
                throw new ServiceException("报告ID不能为空");
            }

            // A. 获取报告主表信息
            OpJczxFeedReportBase reportBase = opJczxFeedReportBaseMapper.selectOpJczxFeedReportBaseByOpJczxFeedReportBaseId(reportBaseId);
            if (ObjectUtils.isEmpty(reportBase)) {
                throw new ServiceException("未找到对应的检测报告");
            }

            // B. 校验状态 (3:待批准 不可发, 4:待发送, 5:已发送 可发)
            // 只要不是4或5，就报错。或者根据业务需求，如果是3则跳过
            if (!"4".equals(reportBase.getStatus()) && !"5".equals(reportBase.getStatus())) {
                throw new ServiceException("样品 [" + reportBase.getSampleNo() + "] 报告未批准，无法发送");
            }

            // C. 获取委托单信息 (为了获取接收时间、委托方默认邮箱等上下文)
            // 注意：reportBase里应该有 feedEntrustOrderId
            String orderId = reportBase.getFeedEntrustOrderId();
            if (StringUtils.isEmpty(orderId)) {
                // 兼容逻辑：如果reportBase没存orderId，尝试从DTO取
                orderId = emailDTO.getFeedEntrustOrderId();
            }
            OpFeedEntrustOrder order = opFeedEntrustOrderMapper.selectOpFeedEntrustOrderByOpFeedEntrustOrderId(orderId);
            if (ObjectUtils.isEmpty(order)) {
                throw new ServiceException("关联的委托单数据缺失");
            }

            // D. 准备附件
            if (StringUtils.isEmpty(reportBase.getPdfFileInfo())) {
                throw new ServiceException("样品 [" + reportBase.getSampleNo() + "] 报告PDF文件未生成");
            }
            MultipartFile file = FileConvertUtils.getMultipartFileByAbsolutePath(profile, reportBase.getPdfFileInfo());
            if (ObjectUtils.isEmpty(file)) {
                throw new ServiceException("样品 [" + reportBase.getSampleNo() + "] 报告文件读取失败");
            }
            MultipartFile[] files = {file};

            // E. 处理收件人邮箱 (合并 委托单默认邮箱 + 弹窗输入的邮箱)
            String toEmailDefault = order.getEntrustContactEmail(); // 委托单里的默认联系人邮箱
            String toEmailInput = emailDTO.getEmails();             // 弹窗里勾选/输入的邮箱

            Set<String> emailSet = new LinkedHashSet<>();
            String[] emailSources = {toEmailDefault, toEmailInput};

            for (String source : emailSources) {
                if (StringUtils.isNotEmpty(source)) {
                    // 支持中文逗号和英文逗号
                    String[] emails = source.replace("，", ",").split(",");
                    for (String email : emails) {
                        if (StringUtils.isNotBlank(email)) {
                            emailSet.add(email.trim());
                        }
                    }
                }
            }

            if (emailSet.isEmpty()) {
                throw new ServiceException("样品 [" + reportBase.getSampleNo() + "] 未指定收件邮箱");
            }
            String[] toEmails = emailSet.toArray(new String[0]);

            // F. 计算工作日
            int gzr = bsWorkdayConfigService.calculateWorkDays(reportBase.getReceiveTime(), new Date());
            String deptName = reportBase.getEntrustDeptName();
            // G. 构建邮件内容
            String content = "您好!\n" +
                    "　　检测中心于" + DateUtils.parseDateToStr("yyyy-MM-dd", reportBase.getReceiveTime()) +
                    "收到"+deptName+"样品编号为" + reportBase.getSampleNo() + "样品。检测报告于" + sendDate +
                    "已发送至您邮箱。此次检测从样品接收至报告发送共使用" + gzr + "个工作日。\n" +
                    "　　注意查收！顺祝工作愉快！\n" +
                    "\n" +
                    "　　光明牧业有限公司检验测试中心\n" +
                    "　　Add：上海市闵行区万源路2729号309室\n" +
                    "　　Tel：021-56481688*8105";

            // H. 发送邮件
            try {
                System.out.println("开始发送邮件 样品:" + reportBase.getSampleNo() + " 收件人:" + Arrays.toString(toEmails));
                EmailUtils.sendEmail(deptEmail, emailAuthCode, toEmails,
                        "光明牧业有限公司检验测试中心饲料报告-" + reportBase.getSampleNo(), // 标题建议加上样品编号区分
                        content,
                        false,
                        files);
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
                throw new ServiceException("发送邮件失败: " + e.getMessage());
            }

            // I. 更新状态
            reportBase.setSendEmailTime(new Date());
            reportBase.setSendEmailUserId(String.valueOf(SecurityUtils.getUserId()));
            reportBase.setStatus("5"); // 更新为已发送
            opJczxFeedReportBaseMapper.updateOpJczxFeedReportBase(reportBase);

            successCount++;
        }

        return successCount;
    }

    /**
     * 保存检测中心饲料报告pdf
     *
     * @param reportBase
     * @return
     */
    @Override
    public int saveOpJczxFeedReportPdf(OpJczxFeedReportBase reportBase) {
        return opJczxFeedReportBaseMapper.updateOpJczxFeedReportBase(reportBase);
    }

    /**
     * 查询发送报告邮箱
     */
    @Override
    public ReportSendInfoVo selectOpJczxFeedReportBaseEmailList(String feedEntrustOrderId) {

        OpFeedEntrustOrder opFeedEntrustOrder = opFeedEntrustOrderMapper.selectOpFeedEntrustOrderByOpFeedEntrustOrderId(feedEntrustOrderId);
        // 获取检测中心通讯方式
        List<ReportEmailVo> jczxEmailList = bsContactMapper.selectFeedEmailByDeptId(SecurityUtils.getDeptId());

        // 获取通讯方式表邮箱列表（委托部门）
        List<ReportEmailVo> toEmailByContact = bsContactMapper.selectFeedEmailByDeptId(opFeedEntrustOrder.getEntrustDeptId());

        // 合并两个邮箱列表并去重（检测中心邮箱 + 委托部门邮箱）
        List<ReportEmailVo> emailList = Stream.concat(
                        // 检测中心邮箱流
                        Optional.ofNullable(jczxEmailList).orElse(Collections.emptyList()).stream()
                                .filter(vo -> vo != null && StringUtils.isNotBlank(vo.getEmail())), // 过滤空对象和空邮箱
                        // 委托部门邮箱流
                        Optional.ofNullable(toEmailByContact).orElse(Collections.emptyList()).stream()
                                .filter(vo -> vo != null && StringUtils.isNotBlank(vo.getEmail())) // 过滤空对象和空邮箱
                )
                // 收集并去重：根据邮箱地址去重，保留第一个出现的重复项
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                vo -> vo.getEmail().trim().toLowerCase(), // 去重键：去除空格并转为小写
                                Function.identity(),                       // 值：对象本身
                                (first, second) -> first                   // 合并函数：重复时保留第一个
                        ),
                        map -> new ArrayList<>(map.values())           // 将Map的值转换为List
                ));
        ReportSendInfoVo vo = new ReportSendInfoVo();
        vo.setEmailList(emailList);
        // 查询这个委托单所有的样品
        List<String> orderId = new ArrayList<>();
        orderId.add(feedEntrustOrderId);
        List<OpFeedEntrustOrderSample> opFeedEntrustOrderSamples = opFeedEntrustOrderSampleMapper.selectSamplesByOrderIds(orderId);
        List<OpJczxFeedReportBase> opJczxFeedReportBaseList = new ArrayList<>();
        for (OpFeedEntrustOrderSample opFeedEntrustOrderSample : opFeedEntrustOrderSamples) {
            String opFeedEntrustOrderSampleId = opFeedEntrustOrderSample.getOpFeedEntrustOrderSampleId();
            List<OpJczxFeedReportBase> bases =
                    opJczxFeedReportBaseMapper.selectReportBaseBySampleId2(opFeedEntrustOrderSampleId);
            OpJczxFeedReportBase base = new OpJczxFeedReportBase();
            if (CollectionUtil.isNotEmpty(bases)) {
                base.setOpJczxFeedReportBaseId(bases.get(0).getOpJczxFeedReportBaseId());
                base.setSampleNo(opFeedEntrustOrderSample.getSampleNo());
                base.setSampleName(opFeedEntrustOrderSample.getName());
                base.setStatus(bases.get(0).getStatus());
            } else {
                base.setSampleNo(opFeedEntrustOrderSample.getSampleNo());
                base.setSampleName(opFeedEntrustOrderSample.getName());
                base.setStatus("3");
            }
            opJczxFeedReportBaseList.add(base);
        }
        vo.setBaseList(opJczxFeedReportBaseList);
        return vo;
    }

    @Override
    public Map<String, Integer> getStatusCount(OpJczxFeedReportDto opJczxFeedReportBase) {
        // 排除近红外数据
        opJczxFeedReportBase.setTestMethod("2");

        Integer status_0 = opJczxFeedReportBaseMapper.getStatus0Count(opJczxFeedReportBase);

        List<Map<String, Object>> rows = opJczxFeedReportBaseMapper.getStatusCount(opJczxFeedReportBase);
        Map<String, Integer> countsMap = rows.stream()
                .collect(Collectors.toMap(
                        row -> String.valueOf(row.get("status")),
                        row -> ((Number) row.get("count")).intValue()
                ));

        Map<String, Integer> statusCount = new HashMap<>();
        statusCount.put("status_1", countsMap.getOrDefault("1", 0));
        statusCount.put("status_2", countsMap.getOrDefault("2", 0));
        statusCount.put("status_3", countsMap.getOrDefault("3", 0));
        statusCount.put("status_4", countsMap.getOrDefault("4", 0));
        statusCount.put("status_5", countsMap.getOrDefault("5", 0));
        statusCount.put("status_0", status_0 != null ? status_0 : 0);

        return statusCount;

    }

    // 合并时间范围计算和格式化
    private String calculateAndFormatTimeRange(List<OpJczxFeedReportBase> reports, DateTimeFormatter formatter) {
        List<LocalDate> dates = reports.stream()
                .map(OpJczxFeedReportBase::getTestTime)
                .filter(Objects::nonNull)
                .filter(timeStr -> !timeStr.trim().isEmpty())
                .map(timeStr -> LocalDate.parse(timeStr, formatter))
                .collect(Collectors.toList());

        if (dates.isEmpty()) {
            return null;
        }

        LocalDate startDate = dates.stream()
                .min(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalStateException("无法计算最小时间"));

        LocalDate endDate = dates.stream()
                .max(LocalDate::compareTo)
                .orElse(startDate);

        if (startDate.equals(endDate)) {
            return startDate.format(formatter);
        } else {
            return String.format("%s——%s",
                    startDate.format(formatter),
                    endDate.format(formatter));
        }
    }

    // 收集仪器名称
    private String collectInstrumentNames(List<OpJczxFeedReportBase> reports) {
        return reports.stream()
                .map(OpJczxFeedReportBase::getMainInsturment)
                .filter(name -> name != null && !name.trim().isEmpty())
                .distinct()
                .collect(Collectors.joining("；"));
    }

    /**
     * 收集并去重设备名称
     * 修改逻辑：先按逗号分割，再trim去空格，最后distinct去重
     */
    private String collectInstrumentNames2(List<OpJczxFeedReportBase> resultInfoList) {
        if (CollectionUtils.isAnyEmpty(resultInfoList)) {
            return "";
        }
        return resultInfoList.stream()
                .map(OpJczxFeedReportBase::getMainInstrument)
                .filter(StringUtils::isNotBlank)
                // 1. 统一分隔符（防止中文逗号），并按逗号分割成数组
                .map(s -> s.replace("，", ",").split(","))
                // 2. 将数组扁平化为单个流 (Stream<String[] -> Stream<String>)
                .flatMap(Arrays::stream)
                // 3. 去除首尾空格
                .map(String::trim)
                // 4. 过滤掉分割后可能产生的空字符串
                .filter(StringUtils::isNotBlank)
                // 5. 对单个设备名称进行去重
                .distinct()
                // 6. 重新连接
                .collect(Collectors.joining(","));
    }

    // 收集项目名称
    private String collectItemNames(List<OpJczxFeedReportBase> reports) {
        return reports.stream()
                .map(OpJczxFeedReportBase::getItemName)
                .filter(name -> name != null && !name.trim().isEmpty())
                .distinct()
                .collect(Collectors.joining("；"));
    }

    /**
     * 报告审核通过/不通过
     *
     * @param opReportBase
     * @return
     */
    @Override
    @Transactional
    public int verifyOpJczxFeedReport(OpJczxFeedReportBase opReportBase) {
        String status = opReportBase.getStatus();
        if (ObjectUtils.isEmpty(status)) {
            throw new RuntimeException("服务器网络异常");
        }
        String opJczxFeedReportBaseId = opReportBase.getOpJczxFeedReportBaseId();
        if (ObjectUtils.isEmpty(opJczxFeedReportBaseId)) {
            throw new RuntimeException("服务器网络异常");
        }
        if (status.equals(FeedReportEnum.Veriy.getCode())) {
            Long userIdL = SecurityUtils.getUserId();
            String userId = String.valueOf(userIdL);
            SysUser sysUser = sysUserMapper.selectUserById(userIdL);
            String username = sysUser.getNickName();
//            String sign = sysUserMapper.selectSignByUserId(userId);
//            String sign = SecurityUtils.getLoginUser().getUser().getSign();

            opReportBase.setCheckUserId(userId);
            opReportBase.setCheckUser(username);
            opReportBase.setCheckTime(new Date());
        }
        if(!StringUtils.isEmpty(opReportBase.getReturnReason())){
            OpJczxFeedReportBase base = opJczxFeedReportBaseMapper.selectOpJczxFeedReportBaseByOpJczxFeedReportBaseId(opJczxFeedReportBaseId);

            opFeedEntrustOrderSampleMapper.updateReturnReason(opReportBase.getReturnReason(),base.getSampleNo());
        }
        return opJczxFeedReportBaseMapper.updateOpJczxFeedReportBase(opReportBase);


    }


    /**
     * 报告审核通过/不通过
     *
     * @param opReportBase
     * @return
     */
    @Override
    @Transactional
    public int commitOpJczxFeedReport(OpJczxFeedReportBase opReportBase) {
        String status = opReportBase.getStatus();
        if (ObjectUtils.isEmpty(status)) {
            throw new RuntimeException("服务器网络异常");


        }
        String opJczxFeedReportBaseId = opReportBase.getOpJczxFeedReportBaseId();
        if (ObjectUtils.isEmpty(opJczxFeedReportBaseId)) {
            throw new RuntimeException("服务器网络异常");
        }
        if (status.equals(FeedReportEnum.Commit.getCode())) {
            Long userIdL = SecurityUtils.getUserId();
            String userId = String.valueOf(userIdL);
            SysUser sysUser = sysUserMapper.selectUserById(userIdL);
            String username = sysUser.getNickName();
            opReportBase.setApproveUserId(userId);
            opReportBase.setApproveUser(username);
            opReportBase.setApproveTime(new Date());
            OpFeedEntrustOrderSample opFeedEntrustOrderSample = new OpFeedEntrustOrderSample();
            opFeedEntrustOrderSample.setOpFeedEntrustOrderSampleId(opReportBase.getFeedEntrustOrderSampleId());
            opFeedEntrustOrderSample.setReportId(opJczxFeedReportBaseId);
            opFeedEntrustOrderSampleMapper.updateOpFeedEntrustOrderSample(opFeedEntrustOrderSample);

        }
        if(!StringUtils.isEmpty(opReportBase.getReturnReason())){
            OpJczxFeedReportBase base = opJczxFeedReportBaseMapper.selectOpJczxFeedReportBaseByOpJczxFeedReportBaseId(opJczxFeedReportBaseId);

            opFeedEntrustOrderSampleMapper.updateReturnReason(opReportBase.getReturnReason(),base.getSampleNo());

        }
        //通过后，回填牧场化验单
        if(JczxFeedReportStatusEnum.YPZ.getCode().equals(opReportBase.getStatus())){
            OpFeedEntrustOrderSample sample = opFeedEntrustOrderSampleMapper.selectOpFeedEntrustOrderSampleByOpFeedEntrustOrderSampleId(opReportBase.getFeedEntrustOrderSampleId());
            feedResultBaseService.addRanchResult(sample.getSampleNo(),opJczxFeedReportBaseId);
        }
        opReportBase.setIssuanceTime(DateUtils.getNowDate());
        return opJczxFeedReportBaseMapper.updateOpJczxFeedReportBase(opReportBase);

    }


    /**
     * 新增检测中心饲料报告子信息
     *
     * @param opJczxFeedReportBase 检测中心饲料报告主对象
     */
    public void insertOpJczxFeedReportInfo(OpJczxFeedReportBase opJczxFeedReportBase) {
        List<OpJczxFeedReportInfo> opJczxFeedReportInfoList = opJczxFeedReportBase.getOpJczxFeedReportInfoList();
        String opJczxFeedReportBaseId = opJczxFeedReportBase.getOpJczxFeedReportBaseId();
        if (StringUtils.isNotNull(opJczxFeedReportInfoList)) {
            List<OpJczxFeedReportInfo> list = new ArrayList<OpJczxFeedReportInfo>();
            for (OpJczxFeedReportInfo opJczxFeedReportInfo : opJczxFeedReportInfoList) {
                opJczxFeedReportInfo.setBaseId(opJczxFeedReportBaseId);
                list.add(opJczxFeedReportInfo);
            }
            if (list.size() > 0) {
                opJczxFeedReportBaseMapper.batchOpJczxFeedReportInfo(list);
            }
        }
    }


    @Override
    public OpFeedReportVo getReport(String opJczxFeedReportBaseId) {
        OpJczxFeedReportBase opJczxFeedReportBase = opJczxFeedReportBaseMapper.selectOpJczxFeedReportBaseByOpJczxFeedReportBaseId(opJczxFeedReportBaseId);
        if (ObjectUtils.isEmpty(opJczxFeedReportBase)) {
            throw new RuntimeException("未生成报告，无法查看");
        }
        if (!opJczxFeedReportBase.getStatus().equals(FeedReportEnum.Send.getCode())) {
            throw new RuntimeException("该报告未完成批准，无法查看");
        }
        OpFeedReportBaseVo feedReportBase = new OpFeedReportBaseVo();
        BeanUtils.copyProperties(opJczxFeedReportBase, feedReportBase);
        feedReportBase.setReceiveTime(DateUtils.parseDateToStr("yyyy-MM-dd", opJczxFeedReportBase.getReceiveTime()));
        List<OpJczxFeedReportInfo> opJczxFeedReportInfoList = opJczxFeedReportBaseMapper.selectReportInfoByBaseId(opJczxFeedReportBaseId);
        List<OpFeedReportInfoVo> reportInfoVos = new ArrayList<>();
        for (OpJczxFeedReportInfo opJczxFeedReportInfo : opJczxFeedReportInfoList) {
            OpFeedReportInfoVo reportInfoVo = new OpFeedReportInfoVo();
            BeanUtils.copyProperties(opJczxFeedReportInfo, reportInfoVo);
            reportInfoVos.add(reportInfoVo);
        }
        // 查询签名
        String editUserId = opJczxFeedReportBase.getEditUserId();
        String checkUserId = opJczxFeedReportBase.getCheckUserId();
        String approveUserId = opJczxFeedReportBase.getApproveUserId();
        if (!ObjectUtils.isEmpty(editUserId)) {
            String sign = sysUserMapper.selectSignByUserId(editUserId);
            feedReportBase.setEditSign(sign);
        }
        if (!ObjectUtils.isEmpty(checkUserId)) {
            String sign = sysUserMapper.selectSignByUserId(checkUserId);
            feedReportBase.setCheckSign(sign);
        }
        if (!ObjectUtils.isEmpty(approveUserId)) {
            String sign = sysUserMapper.selectSignByUserId(approveUserId);
            feedReportBase.setApproveSign(sign);
        }
        OpFeedReportVo opFeedReportVo = new OpFeedReportVo();
        opFeedReportVo.setFeedReportBase(feedReportBase);
        opFeedReportVo.setOpJczxFeedReportInfoList(reportInfoVos);
        return opFeedReportVo;


    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAll(OpJczxFeedReportDto opJczxFeedReportBase) {
        List<String> sampleIdList = opJczxFeedReportBase.getSampleIdList();

        if (CollectionUtil.isEmpty(sampleIdList)) {
            return;
        }

        for (String sampleId : sampleIdList) {
            // 1. 获取自动生成的报告详情 (VO)
            OpFeedReportVo opFeedReportVo = this.selecReportMakeInfoByEntrustOrderSampleId(sampleId);
            System.out.println(opFeedReportVo.getFeedReportBase().getReceiveTime() + "!!!!!!!!!");
            // 判空处理，防止空指针
            if (opFeedReportVo == null || opFeedReportVo.getFeedReportBase() == null) {
                continue;
            }

            OpFeedReportBaseVo baseVo = opFeedReportVo.getFeedReportBase();
            List<OpFeedReportInfoVo> infoVos = opFeedReportVo.getOpJczxFeedReportInfoList();

            // 2. 将 VO 转换为 实体对象 (Entity)
            OpJczxFeedReportBase reportEntity = new OpJczxFeedReportBase();

            // 2.1 复制主表属性
            BeanUtils.copyProperties(baseVo, reportEntity);
            // 手动转换日期类型
            if (StringUtils.isNotEmpty(baseVo.getReceiveTime())) {
                try {
                    // 使用您的 DateUtils 将字符串解析回 Date 对象
                    // 假设您的工具类支持自动解析，或者使用 parseDate(str, pattern)
                    reportEntity.setReceiveTime(DateUtils.parseDate(baseVo.getReceiveTime()));
                } catch (Exception e) {
                    e.printStackTrace();
                    // 防止日期格式错误导致整个流程失败，视情况处理
                }
            }
            // 检查 testTime 是否也存在同样问题（如果是 String 转 Date 也需要处理）
            // if (StringUtils.isNotEmpty(baseVo.getTestTime())) { ... }

            System.out.println(reportEntity.getReceiveTime() + "!!!!!!!!!");
            // 2.2 补充/覆盖关键字段
            reportEntity.setFeedEntrustOrderSampleId(sampleId);
            // 3. 处理子表列表 (检测项目结果)
            // 【修正点1】这里应该是 OpJczxFeedReportInfo (报告子表)，而不是 ResultInfo (原始数据表)
            List<OpJczxFeedReportInfo> reportInfos = new ArrayList<>();

            if (CollectionUtil.isNotEmpty(infoVos)) {
                for (OpFeedReportInfoVo infoVo : infoVos) {
                    // 【修正点2】实例化正确的对象
                    OpJczxFeedReportInfo reportInfo = new OpJczxFeedReportInfo();

                    // 复制属性
                    // (OpFeedReportInfoVo 和 OpJczxFeedReportInfo 的字段如 itemName, valueOfTest, standard 都能对应上)
                    BeanUtils.copyProperties(infoVo, reportInfo);

                    // 填充关联ID，防止为null (虽然 insert 方法可能会覆盖，但手动设置更安全)
                    reportInfo.setOpJczxFeedReportInfoId(IdUtils.simpleUUID()); // 如果需要手动生成ID
                    // reportInfo.setBaseId(reportId); // baseId 通常在主表插入后再反填，或者由 MyBatis 级联处理

                    reportInfos.add(reportInfo);
                }
            }

            // 【修正点3】将正确的列表设置进去
            reportEntity.setOpJczxFeedReportInfoList(reportInfos);

            // 4. 第一步保存：状态设为 "编制中(ZZZ)"
            reportEntity.setStatus(JczxFeedReportStatusEnum.ZZZ.getCode());
            // 调用保存/插入方法 (假设该方法会返回ID，或回填ID到 reportEntity)

            String reportId = this.saveOpJczxFeedReport(reportEntity);
            // 如果 save 方法内部没有返回值，记得手动 reportEntity.setOpJczxFeedReportBaseId(生成的主键);

            // 5. 第二步提交：状态流转为 "待审核(DSH)"
            reportEntity.setStatus(JczxFeedReportStatusEnum.DSH.getCode());
            reportEntity.setOpJczxFeedReportBaseId(reportId);
            this.saveOpJczxFeedReport(reportEntity);
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void invalidateReport(OpJczxFeedReportDto opJczxFeedReportBase) {
        String updateUserId = String.valueOf(SecurityUtils.getUserId());
        OpJczxFeedReportBase base = opJczxFeedReportBaseMapper.selectOpJczxFeedReportBaseByOpJczxFeedReportBaseId(opJczxFeedReportBase.getOpJczxFeedReportBaseId());
        if (base != null) {
            opFeedEntrustOrderSampleMapper.updateDeleteByNo(SecurityUtils.getUserId().toString(),base.getSampleNo());
        }


        opJczxFeedReportBaseMapper.updateStatus6ById(opJczxFeedReportBase.getOpJczxFeedReportBaseId(),updateUserId);

    }

    /**
     * 获取检测中心饲料报告列表
     *
     * @param opJczxFeedReportDto 查询参数
     * @return 结果
     */
    @Override
    public List<OpFeedReportListVo> queryFeedReport(OpJczxFeedReportDto opJczxFeedReportDto) {
        return opJczxFeedReportBaseMapper.selectOpJczxFeedReportBaseList(opJczxFeedReportDto);
    }
}
