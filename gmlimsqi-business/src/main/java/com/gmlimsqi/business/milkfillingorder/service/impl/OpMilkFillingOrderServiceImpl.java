package com.gmlimsqi.business.milkfillingorder.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.gmlimsqi.business.bsweighbridge.domain.BsWeighBridge;
import com.gmlimsqi.business.bsweighbridge.mapper.BsWeighBridgeMapper;
import com.gmlimsqi.business.inspectionmilktankers.domain.OpInspectionMilkTankers;
import com.gmlimsqi.business.inspectionmilktankers.mapper.OpInspectionMilkTankersMapper;
import com.gmlimsqi.business.leadsealsheet.domain.OpLeadSealSheet;
import com.gmlimsqi.business.leadsealsheet.mapper.OpLeadSealSheetMapper;
import com.gmlimsqi.business.milkfillingorder.domain.OpMilkFillingOrder;
import com.gmlimsqi.business.milkfillingorder.domain.OpMilkFillingOrderDetail;
import com.gmlimsqi.business.milkfillingorder.mapper.OpMilkFillingOrderDetailMapper;
import com.gmlimsqi.business.milkfillingorder.mapper.OpMilkFillingOrderMapper;
import com.gmlimsqi.business.milkfillingorder.service.IOpMilkFillingOrderService;
import com.gmlimsqi.business.milksamplequalityinspection.domain.OpMilkSampleQualityInspection;
import com.gmlimsqi.business.milksamplequalityinspection.mapper.OpMilkSampleQualityInspectionMapper;
import com.gmlimsqi.business.rmts.entity.dto.QualitySyncDTO;
import com.gmlimsqi.business.rmts.service.RmtsRanchLimsService;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.IdListHandler;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.annotation.FunLog;
import com.gmlimsqi.common.core.domain.R;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.annotation.DataScope;
import com.gmlimsqi.system.domain.SysUploadFile;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import com.gmlimsqi.system.mapper.SysUploadFileMapper;
import com.gmlimsqi.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gmlimsqi.business.util.CodeGeneratorUtil.CODE_TYPE_MILK_FILLING_ORDER;

/**
 * 装奶单Service业务层处理
 * * @author hhy
 * @date 2025-11-10
 */
@Slf4j
@Service
public class OpMilkFillingOrderServiceImpl implements IOpMilkFillingOrderService
{
    @Autowired
    private OpMilkFillingOrderMapper opMilkFillingOrderMapper;

    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private OpLeadSealSheetMapper opLeadSealSheetMapper;

    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;

    @Autowired
    private OpMilkFillingOrderDetailMapper opMilkFillingOrderDetailMapper;

    @Autowired
    private OpMilkSampleQualityInspectionMapper opMilkSampleQualityInspectionMapper;

    @Autowired
    private SysUploadFileMapper sysUploadFileMapper;

    @Autowired
    private OpInspectionMilkTankersMapper opInspectionMilkTankersMapper;

    @Autowired
    private RmtsRanchLimsService rmtsRanchLimsService;

    @Autowired
    private BsWeighBridgeMapper bsWeighBridgeMapper;

    /**
     * 查询装奶单
     * * @param opMilkFillingOrderId 装奶单主键
     * @return 装奶单
     */
    @Override
    public OpMilkFillingOrder selectOpMilkFillingOrderByOpMilkFillingOrderId(String opMilkFillingOrderId)
    {
        OpMilkFillingOrder opMilkFillingOrder = opMilkFillingOrderMapper.
                selectOpMilkFillingOrderByOpMilkFillingOrderId(opMilkFillingOrderId);

        // 处理附件，按逗号分隔
        if (StringUtils.isNotEmpty(opMilkFillingOrder.getFile())){
            List<String> list = IdListHandler.parseIdStr(opMilkFillingOrder.getFile());
            if (!list.isEmpty()){
                List<String> urlList = new ArrayList<>();
                for (String fileId : list){
                    SysUploadFile sysUploadFile = sysUploadFileMapper.selectFileById(fileId);
                    urlList.add(sysUploadFile.getUrl());
                }
                opMilkFillingOrder.setFileUrl(urlList);
            }
        }

        // 查询子表数据
        List<OpMilkFillingOrderDetail> opMilkFillingOrderDetails = opMilkFillingOrderDetailMapper.
                selectOpMilkFillingOrderDetailListByOpMilkFillingOrderId(opMilkFillingOrder.getOpMilkFillingOrderId());

        if (!opMilkFillingOrderDetails.isEmpty()){
            opMilkFillingOrder.setOpMilkFillingOrderDetailList(opMilkFillingOrderDetails);
        }

        return opMilkFillingOrder;
    }

    /**
     * 查询装奶单列表
     * * @param opMilkFillingOrder 装奶单
     * @return 装奶单
     */
    // +++ 新增：@DataScope 注解以激活数据权限切面 +++
//    @DataScope(deptAlias = "a")
    @Override
    public List<OpMilkFillingOrder> selectOpMilkFillingOrderList(OpMilkFillingOrder opMilkFillingOrder)
    {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            opMilkFillingOrder.setDeptId(SecurityUtils.getDeptId().toString());
        }
        List<OpMilkFillingOrder> items = opMilkFillingOrderMapper.selectOpMilkFillingOrderList(opMilkFillingOrder);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpMilkFillingOrder::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增装奶单
     * * @param opMilkFillingOrder 装奶单
     * @return 结果
     */
    @Override
    @Transactional
    public String insertOpMilkFillingOrder(OpMilkFillingOrder opMilkFillingOrder)
    {
        if (StringUtils.isEmpty(opMilkFillingOrder.getOpMilkFillingOrderId())) {
            opMilkFillingOrder.setOpMilkFillingOrderId(IdUtils.simpleUUID());
        }

        // +++ 自动填充部门ID，用于数据权限 +++
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser))
        {
            // 假设您的实体类中有名为 setDeptId 的方法
            // (请确保您的 OpMilkFillingOrder 实体类中有 setDeptId(String deptId) 方法)
            opMilkFillingOrder.setDeptId(loginUser.getDeptId().toString());
        }

        try {
            String resultNo = codeGeneratorUtil.generateCode(CODE_TYPE_MILK_FILLING_ORDER);
            opMilkFillingOrder.setMilkFillingOrderNumber(resultNo);
        } catch (BusinessException e) {
            throw new RuntimeException("生成装奶单编号失败: " + e.getMessage());
        }

        Long userId = SecurityUtils.getUserId();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        if (StringUtils.isNotNull(sysUser)){
            opMilkFillingOrder.setCreateByName(sysUser.getNickName());
        }
        opMilkFillingOrder.setStatus("0");
        opMilkFillingOrder.setPreStepCompleted("0");
        opMilkFillingOrder.setIsDelete("0");
        opMilkFillingOrder.setIsPushMilkSource("0");

        // 新增子表
        List<OpMilkFillingOrderDetail> opMilkFillingOrderDetailList = opMilkFillingOrder.getOpMilkFillingOrderDetailList();

        // 装奶总量
        String totalMilkCapacity = "0";

        if (ObjectUtil.isNotEmpty(opMilkFillingOrderDetailList)){
            for (OpMilkFillingOrderDetail opMilkFillingOrderDetail : opMilkFillingOrder.getOpMilkFillingOrderDetailList()){
                opMilkFillingOrderDetail.setOpMilkFillingOrderDetailId(IdUtils.simpleUUID());
                opMilkFillingOrderDetail.setOpMilkFillingOrderId(opMilkFillingOrder.getOpMilkFillingOrderId());

                opMilkFillingOrderDetailMapper.insertOpMilkFillingOrderDetail(opMilkFillingOrderDetail);

                // 累加装奶总量
                totalMilkCapacity = BigDecimal.valueOf(Double.parseDouble(totalMilkCapacity))
                        .add(BigDecimal.valueOf(Double.parseDouble(opMilkFillingOrderDetail.getMilkCapacity())))
                        .toString();
            }
        }

        opMilkFillingOrder.setTotalMilkCapacity(totalMilkCapacity);

        // 自动填充创建/更新信息
        opMilkFillingOrder.fillCreateInfo();
        opMilkFillingOrderMapper.insertOpMilkFillingOrder(opMilkFillingOrder);
        return opMilkFillingOrder.getOpMilkFillingOrderId();
    }

    /**
     * 修改装奶单
     * * @param opMilkFillingOrder 装奶单
     * @return 结果
     */
    @Override
    public int updateOpMilkFillingOrder(OpMilkFillingOrder opMilkFillingOrder)
    {
        // 查询检查单是否存在
        OpMilkFillingOrder status =
                selectOpMilkFillingOrderByOpMilkFillingOrderId(opMilkFillingOrder.getOpMilkFillingOrderId());
        if (status == null) {
            throw new IllegalArgumentException("装奶单不存在");
        }

        if ("1".equals(status.getStatus())){
            throw new IllegalArgumentException("已审核的装奶单不能修改");
        }

        // 删除子表数据
        opMilkFillingOrderDetailMapper.
                deleteOpMilkFillingOrderDetailByOpMilkFillingOrderId(opMilkFillingOrder.getOpMilkFillingOrderId());

        // 新增子表
        List<OpMilkFillingOrderDetail> opMilkFillingOrderDetailList = opMilkFillingOrder.getOpMilkFillingOrderDetailList();

        // 装奶总量
        String totalMilkCapacity = "0";

        if (!opMilkFillingOrderDetailList.isEmpty()){
            for (OpMilkFillingOrderDetail opMilkFillingOrderDetail : opMilkFillingOrderDetailList){
                opMilkFillingOrderDetail.setOpMilkFillingOrderDetailId(IdUtils.simpleUUID());
                opMilkFillingOrderDetail.setOpMilkFillingOrderId(opMilkFillingOrder.getOpMilkFillingOrderId());

                opMilkFillingOrderDetailMapper.insertOpMilkFillingOrderDetail(opMilkFillingOrderDetail);

                // 累加装奶总量
                totalMilkCapacity = BigDecimal.valueOf(Double.parseDouble(totalMilkCapacity))
                        .add(BigDecimal.valueOf(Double.parseDouble(opMilkFillingOrderDetail.getMilkCapacity())))
                        .toString();
            }
        }

        opMilkFillingOrder.setTotalMilkCapacity(totalMilkCapacity);

        // 自动填充更新信息
        opMilkFillingOrder.fillUpdateInfo();
        return opMilkFillingOrderMapper.updateOpMilkFillingOrder(opMilkFillingOrder);
    }

    @Override
    @Transactional
    public int audit(String opMilkFillingOrderId) {
        // 检查单是否存在
        OpMilkFillingOrder opMilkFillingOrder = selectOpMilkFillingOrderByOpMilkFillingOrderId(opMilkFillingOrderId);
        if (opMilkFillingOrder == null) {
            throw new IllegalArgumentException("装奶单不存在");
        }

        // 检查单是否已审核
        if ("1".equals(opMilkFillingOrder.getStatus())){
            throw new IllegalArgumentException("已审核的装奶单不能重复审核");
        }

        // 更新检查单状态为已审核
        opMilkFillingOrder.setStatus("1");
        // 自动填充更新信息
        opMilkFillingOrder.fillUpdateInfo();
        // 从登录用户获取审核人信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)){
            opMilkFillingOrder.setReviewerId(loginUser.getUserId().toString());
        }

        // 从用户表获取审核人名称
        SysUser sysUser = sysUserMapper.selectUserById(Long.parseLong(opMilkFillingOrder.getReviewerId()));
        if (StringUtils.isNotNull(sysUser)){
            opMilkFillingOrder.setReviewer(sysUser.getNickName());
        }
        opMilkFillingOrder.setReviewTime(new Date());

        // 更新铅封单前置状态
        OpLeadSealSheet opLeadSealSheet = opLeadSealSheetMapper.selectOpLeadSealSheetByMilkTankersId(opMilkFillingOrder.getInspectionMilkTankersId());
        if (ObjectUtil.isEmpty(opLeadSealSheet)){
            throw new IllegalArgumentException("未找到与装奶单关联的铅封单");
        }
        opLeadSealSheet.setPreStepCompleted("1");
        opLeadSealSheetMapper.updateOpLeadSealSheet(opLeadSealSheet);

        // 更新质检单前置状态
        OpMilkSampleQualityInspection opMilkSampleQualityInspection = opMilkSampleQualityInspectionMapper.
                selectOpMilkSampleQualityInspectionByInspectionMilkTankersId(opMilkFillingOrder.getInspectionMilkTankersId());
        if (ObjectUtil.isEmpty(opMilkSampleQualityInspection)){
            throw new IllegalArgumentException("未找到与装奶单关联的奶样质检单");
        }
        opMilkSampleQualityInspection.setPreStepCompleted("1");
        opMilkSampleQualityInspectionMapper.updateOpMilkSampleQualityInspection(opMilkSampleQualityInspection);

        if (opMilkFillingOrder.getOpMilkFillingOrderDetailList() == null || opMilkFillingOrder.getOpMilkFillingOrderDetailList().isEmpty()){
            throw new IllegalArgumentException("未选择奶仓");
        }

        List<OpMilkFillingOrderDetail> opMilkFillingOrderDetailList = opMilkFillingOrder.getOpMilkFillingOrderDetailList();

        return opMilkFillingOrderMapper.updateOpMilkFillingOrder(opMilkFillingOrder);
    }

    /**
     * 根据装奶单主键读取磅单数据并返回
     * @param opMilkFillingOrderId
     * @return
     */
    @Override
    public OpMilkFillingOrder readByDriverCode(String opMilkFillingOrderId) {
        // 根据装奶单主键查询磅单数据
        OpMilkFillingOrder opMilkFillingOrder = opMilkFillingOrderMapper.selectOpMilkFillingOrderByOpMilkFillingOrderId(opMilkFillingOrderId);
        if (ObjectUtil.isEmpty(opMilkFillingOrder)){
            throw new ServiceException("未找到装奶单数据");
        }

        // 获取车牌号
        String carNumber = opMilkFillingOrder.getLicensePlateNumber();

        if (StringUtils.isBlank(carNumber)){
            throw new ServiceException("未找到车牌号");
        }

        // 根据车牌号查询过磅单最新数据
        BsWeighBridge bsWeighBridge = bsWeighBridgeMapper.selectLatestBsWeighBridgeByDriverCode(carNumber);
        if (ObjectUtil.isEmpty(bsWeighBridge)){
            throw new ServiceException("未找到过磅单数据");
        }

        // 过磅单数据赋值
        if (!"1".equals(bsWeighBridge.getGmFlag())){
            throw new ServiceException("该磅单未过毛");
        }

        if (!"1".equals(bsWeighBridge.getGpFlag())){
            throw new ServiceException("该磅单未过皮");
        }

        if (StringUtils.isEmpty(bsWeighBridge.getJsWeight())){
            throw new ServiceException("该磅单没有结算重量");
        }

        if (StringUtils.isEmpty(bsWeighBridge.getNetWeight())){
            throw new ServiceException("该磅单没有净重");
        }

        // 过磅单数据赋值
        opMilkFillingOrder.setCweightno(bsWeighBridge.getCweightno());
        opMilkFillingOrder.setDriverCode(bsWeighBridge.getDriverCode());
        opMilkFillingOrder.setGmFlag(bsWeighBridge.getGmFlag());
        opMilkFillingOrder.setGmweight(bsWeighBridge.getGmweight());
        opMilkFillingOrder.setGmtime(bsWeighBridge.getGmtime());
        opMilkFillingOrder.setGpFlag(bsWeighBridge.getGpFlag());
        opMilkFillingOrder.setGpweight(bsWeighBridge.getGpweight());
        opMilkFillingOrder.setGptime(bsWeighBridge.getGptime());
        opMilkFillingOrder.setJsWeight(bsWeighBridge.getJsWeight());
        opMilkFillingOrder.setNetWeight(bsWeighBridge.getNetWeight());
        opMilkFillingOrder.setDbCreateTime(new Date());

        return opMilkFillingOrder;
    }

    @Override
    public List<OpMilkFillingOrder> selectUnassociatedOpMilkFillingOrderList(OpMilkFillingOrder opMilkFillingOrder) {
        // 查询未关联磅单的装奶单列表
        return opMilkFillingOrderMapper.selectUnassociatedOpMilkFillingOrderList(opMilkFillingOrder);
    }

    @Override
    @Transactional
    public int associate(OpMilkFillingOrder opMilkFillingOrder) {
        int count = 0;
        // 查询未关联磅单的装奶单列表
        List<OpMilkFillingOrder> list = opMilkFillingOrderMapper.selectUnassociatedOpMilkFillingOrderList(opMilkFillingOrder);

        if (!CollectionUtil.isEmpty(list)){
            for (OpMilkFillingOrder item : list) {
                // 根据车牌号查询当前磅单最新的一条数据
                BsWeighBridge bsWeighBridge = bsWeighBridgeMapper.selectLatestBsWeighBridgeByDriverCode(item.getLicensePlateNumber());
                if (bsWeighBridge != null){
                    // 关联磅单
                    item.setCweightno(bsWeighBridge.getCweightno());
                    item.setDriverCode(bsWeighBridge.getDriverCode());
                    item.setGmFlag(bsWeighBridge.getGmFlag());
                    item.setGmweight(bsWeighBridge.getGmweight());
                    item.setGmtime(bsWeighBridge.getGmtime());
                    item.setGpFlag(bsWeighBridge.getGpFlag());
                    item.setGpweight(bsWeighBridge.getGpweight());
                    item.setGptime(bsWeighBridge.getGptime());
                    item.setJsWeight(bsWeighBridge.getJsWeight());
                    item.setNetWeight(bsWeighBridge.getNetWeight());
                    item.setDbCreateTime(new Date());

                    opMilkFillingOrderMapper.updateOpMilkFillingOrder(item);
                    count++;
                }
            }

        }
        return count;
    }

    @Override
    @Transactional
    public int manualAssociate(OpMilkFillingOrder opMilkFillingOrder) {
        if (opMilkFillingOrder == null || StringUtils.isEmpty(opMilkFillingOrder.getOpMilkFillingOrderId())) {
            throw new ServiceException("关联失败，装奶单ID不能为空");
        }

        // 1. 查询装奶单
        OpMilkFillingOrder item = opMilkFillingOrderMapper.selectOpMilkFillingOrderByOpMilkFillingOrderId(opMilkFillingOrder.getOpMilkFillingOrderId());
        if (item == null) {
            throw new ServiceException("未找到对应的装奶单");
        }

        BsWeighBridge bsWeighBridge = null;

        // 2. 确定关联逻辑
        if (StringUtils.isNotEmpty(opMilkFillingOrder.getCweightno())) {
            // 2.1 如果传了磅单号，直接按传的关联
            bsWeighBridge = bsWeighBridgeMapper.IsExist(opMilkFillingOrder.getCweightno());
            if (bsWeighBridge == null) {
                throw new ServiceException("未找到磅单号为【" + opMilkFillingOrder.getCweightno() + "】的磅单信息");
            }
        } else if (StringUtils.isNotEmpty(item.getCweightno())) {
            // 2.2 如果没传磅单号，但原本已有关联，则刷新原有磅单数据
            bsWeighBridge = bsWeighBridgeMapper.IsExist(item.getCweightno());
            if (bsWeighBridge == null) {
                throw new ServiceException("原有磅单【" + item.getCweightno() + "】信息已丢失，请手动输入磅单号关联");
            }
        } else {
            // 2.3 如果没传且未关联，则根据车牌号自动匹配最新的一条
            bsWeighBridge = bsWeighBridgeMapper.selectLatestBsWeighBridgeByDriverCode(item.getLicensePlateNumber());
            if (bsWeighBridge == null) {
                throw new ServiceException("未找到车牌号【" + item.getLicensePlateNumber() + "】匹配的磅单信息");
            }
        }

        // 3. 执行数据同步
        item.setCweightno(bsWeighBridge.getCweightno());
        item.setDriverCode(bsWeighBridge.getDriverCode());
        item.setGmFlag(bsWeighBridge.getGmFlag());
        item.setGmweight(bsWeighBridge.getGmweight());
        item.setGmtime(bsWeighBridge.getGmtime());
        item.setGpFlag(bsWeighBridge.getGpFlag());
        item.setGpweight(bsWeighBridge.getGpweight());
        item.setGptime(bsWeighBridge.getGptime());
        item.setJsWeight(bsWeighBridge.getJsWeight());
        item.setNetWeight(bsWeighBridge.getNetWeight());
        item.setDbCreateTime(new Date());

        return opMilkFillingOrderMapper.updateOpMilkFillingOrder(item);
    }

    @Override
    @FunLog(title = "装奶单推送奶源系统", businessType = BusinessType.OTHER)
    public int pushMilkSource(String opMilkFillingOrderId) {
        // 自动推送重量到奶源系统
        // ---------------------------推送奶源逻辑开始
        OpMilkFillingOrder uploadOpmilkFillingOrder = selectOpMilkFillingOrderByOpMilkFillingOrderId(opMilkFillingOrderId);
        StringBuilder milkWarehouseNumber = new StringBuilder();
        List<OpMilkFillingOrderDetail> detailList = uploadOpmilkFillingOrder.getOpMilkFillingOrderDetailList();
        if (detailList != null && !detailList.isEmpty()) {
            for (OpMilkFillingOrderDetail detail : detailList) {
                milkWarehouseNumber.append(detail.getMilkWarehouseName()).append(",");
            }
            if (milkWarehouseNumber.length() > 0) {
                milkWarehouseNumber.deleteCharAt(milkWarehouseNumber.length() - 1);
            }
        }

        // 查询奶罐车检查表
        OpInspectionMilkTankers opInspectionMilkTankers = opInspectionMilkTankersMapper.
                selectOpInspectionMilkTankersByInspectionMilkTankersId(uploadOpmilkFillingOrder.getInspectionMilkTankersId());
        if (opInspectionMilkTankers == null) {
            throw new RuntimeException("推送奶源失败，未找到对应的检查单");
        }
        QualitySyncDTO qualitySyncDTO = new QualitySyncDTO();
        if (StringUtils.isEmpty(uploadOpmilkFillingOrder.getMilkSourcePlanOrderNumber())) {
            throw new RuntimeException("推送奶源失败，奶源计划单号为空");
        }
        qualitySyncDTO.setOrderNumber(uploadOpmilkFillingOrder.getMilkSourcePlanOrderNumber());

        if (StringUtils.isNotEmpty(opInspectionMilkTankers.getCarId())) {
            qualitySyncDTO.setCarId(Integer.valueOf(opInspectionMilkTankers.getCarId()));
        }
        qualitySyncDTO.setCarNumber(opInspectionMilkTankers.getLicensePlateNumber());

        if (StringUtils.isNotEmpty(opInspectionMilkTankers.getDriverId())) {
            qualitySyncDTO.setDriverId(Integer.valueOf(opInspectionMilkTankers.getDriverId()));
        }
        qualitySyncDTO.setDriverName(opInspectionMilkTankers.getDriverName());

        if (StringUtils.isNotEmpty(opInspectionMilkTankers.getTrailerId())) {
            qualitySyncDTO.setTrailerId(Integer.valueOf(opInspectionMilkTankers.getTrailerId()));
        }
        qualitySyncDTO.setTrailerNumber(opInspectionMilkTankers.getTrailerNumber());
        qualitySyncDTO.setOpeningNumber(milkWarehouseNumber.toString());

        // 将结算重量，公斤计算为吨数，防止精度丢失
        if (StringUtils.isNotEmpty(uploadOpmilkFillingOrder.getNetWeight())) {
            double milkQuantity = Double.parseDouble(uploadOpmilkFillingOrder.getNetWeight()) / 1000;
            qualitySyncDTO.setMilkQuantity(milkQuantity);
        }

        qualitySyncDTO.setMilkTime(uploadOpmilkFillingOrder.getFirstMilkingTime());
        qualitySyncDTO.setStatus("0");

        R r = rmtsRanchLimsService.qualityInfo(qualitySyncDTO);
        if (r.getCode() == 200){
            uploadOpmilkFillingOrder.setIsPushMilkSource("1");
        }
        return opMilkFillingOrderMapper.updateOpMilkFillingOrder(uploadOpmilkFillingOrder);
        // ---------------------------推送奶源逻辑结束
    }


}
