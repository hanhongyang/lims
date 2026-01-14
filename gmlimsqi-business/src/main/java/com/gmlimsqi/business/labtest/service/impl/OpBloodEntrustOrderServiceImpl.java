package com.gmlimsqi.business.labtest.service.impl;


import com.gmlimsqi.business.basicdata.domain.LabtestItems;
import com.gmlimsqi.business.basicdata.mapper.LabtestItemsMapper;
import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrder;
import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderItem;
import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderSample;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.dto.OpSampleReceiveDto;
import com.gmlimsqi.business.labtest.mapper.OpBloodEntrustOrderItemMapper;
import com.gmlimsqi.business.labtest.mapper.OpBloodEntrustOrderMapper;
import com.gmlimsqi.business.labtest.mapper.OpBloodEntrustOrderSampleMapper;
import com.gmlimsqi.business.labtest.service.IOpBloodEntrustOrderService;
import com.gmlimsqi.business.labtest.service.IOpSampleReceiveService;
import com.gmlimsqi.business.labtest.vo.OpBloodEntrustVo;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.enums.EntrustOrderStatusEnum;
import com.gmlimsqi.common.enums.EntrustOrderTypeEnum;
import com.gmlimsqi.common.enums.YesNo2Enum;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.DictUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.ServletUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.service.ISysConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 血样样品委托单Service业务层处理
 *
 * @author hhy
 * @date 2025-09-20
 */
@Service
public class OpBloodEntrustOrderServiceImpl implements IOpBloodEntrustOrderService {
    @Autowired
    private OpBloodEntrustOrderMapper opBloodEntrustOrderMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private OpBloodEntrustOrderItemMapper opBloodEntrustOrderItemMapper;
    @Autowired
    private OpBloodEntrustOrderSampleMapper opBloodEntrustOrderSampleMapper;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;
    @Autowired
    private IOpSampleReceiveService sampleReceiveService;
    @Autowired
    private LabtestItemsMapper labtestItemsMapper;
    @Autowired
    private ISysConfigService configService;

    @Value("${ruoyi.profile}/templates")
    private String templateBasePath;

    /**
     * 查询血样样品委托单
     *
     * @param opBloodEntrustOrderId 血样样品委托单主键
     * @return 血样样品委托单
     */
    @Override
    public OpBloodEntrustVo selectOpBloodEntrustOrderByOpBloodEntrustOrderId(String opBloodEntrustOrderId) {
        OpBloodEntrustOrder opBloodEntrustOrder = opBloodEntrustOrderMapper.selectOpBloodEntrustOrderByOpBloodEntrustOrderId(opBloodEntrustOrderId);
        OpBloodEntrustVo opBloodEntrustVo = new OpBloodEntrustVo();
        if (!ObjectUtils.isEmpty(opBloodEntrustOrder)) {
            List<OpBloodEntrustOrderItem> opboi = opBloodEntrustOrderItemMapper.selectAllByOpBloodEntrustOrderId(opBloodEntrustOrderId);
            List<OpBloodEntrustOrderSample> opbos = opBloodEntrustOrderSampleMapper.selectAllByOpBloodEntrustOrderId(opBloodEntrustOrderId);

            BeanUtils.copyProperties(opBloodEntrustOrder, opBloodEntrustVo);
            if (!ObjectUtils.isEmpty(opboi)) {
                //组装VO
                String itemCodeStr = opboi.stream()
                        .map(OpBloodEntrustOrderItem::getItemCode)
                        .filter(code -> code != null && !code.isEmpty())
                        .collect(Collectors.joining(","));
                opBloodEntrustVo.setImmunityTime(opboi.get(0).getImmunityTime());
                opBloodEntrustVo.setItemTy(opboi.get(0).getItemTy());
                opBloodEntrustVo.setItemBbkt(opboi.get(0).getItemBbkt());
                opBloodEntrustVo.setItemCode(itemCodeStr.toString());
            }
            opBloodEntrustVo.setSampleList(opbos);
        }
        return opBloodEntrustVo;
    }

    /**
     * 查询血样样品委托单列表
     *
     * @param opBloodEntrustOrder 血样样品委托单
     * @return 血样样品委托单
     */
    @Override
    public List<OpBloodEntrustOrder> selectOpBloodEntrustOrderList(OpBloodEntrustOrder opBloodEntrustOrder) {
        //检测中心看到所有委托单，牧场只看自己委托单
        String jczxDeptId = configService.selectConfigByKey("jczx.deptId");
        if(jczxDeptId.equals(SecurityUtils.getDeptId().toString())) {

        }else {
            opBloodEntrustOrder.setEntrustDeptId(SecurityUtils.getDeptId());
        }

        List<OpBloodEntrustOrder> items = opBloodEntrustOrderMapper.selectOpBloodEntrustOrderList(opBloodEntrustOrder);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpBloodEntrustOrder::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增血样样品委托单
     *
     * @param opBloodEntrustVo 血样样品委托单
     * @return 结果
     */
    @Override
    @Transactional
    public int insertOpBloodEntrustOrder(OpBloodEntrustVo opBloodEntrustVo) {
        String id = IdUtils.simpleUUID();
        OpBloodEntrustOrder opblo = new OpBloodEntrustOrder();
        BeanUtils.copyProperties(opBloodEntrustVo, opblo);
        String itemCode = opBloodEntrustVo.getItemCode();
        if (ObjectUtils.isEmpty(itemCode)) {
            throw new RuntimeException("请选择检测项目");
        }
        opblo.setOpBloodEntrustOrderId(id);
        opblo.setEntrustOrderNo(codeGeneratorUtil.generateBloodEntrustOrderNo());
        opblo.setBloodTaskItemType(itemCode);

        //免疫时间
        String immunityTime = opBloodEntrustVo.getImmunityTime();
        //检测口蹄疫
        String itemTy = opBloodEntrustVo.getItemTy();
        //检测病原体
        String itemBbkt = opBloodEntrustVo.getItemBbkt();

        OpBloodEntrustOrderItem opbeitem = new OpBloodEntrustOrderItem();
        //保存字典值
        opbeitem.setBloodTaskItemType(itemCode);
        //字典值与检测项目项目匹配
        String diseaseTypeXy = DictUtils.getDictLabel("blood_task_item_type", itemCode);
        LabtestItems labtestItems = labtestItemsMapper.selectBsLabtestItemsByLabtestItemsName(diseaseTypeXy);
        opbeitem.setItemId(labtestItems.getLabtestItemsId());
        opbeitem.setItemCode(labtestItems.getItemCode());
        opbeitem.setItemName(labtestItems.getItemName());
        opbeitem.setOpBloodEntrustOrderItemId(IdUtils.simpleUUID());
        opbeitem.setOpBloodEntrustOrderId(id);
        opbeitem.setIsDelete("0");
        opbeitem.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        opbeitem.setCreateTime(DateUtils.getNowDate());
        opbeitem.setImmunityTime(immunityTime);
        opbeitem.setItemTy(itemTy);
        opbeitem.setItemBbkt(itemBbkt);
        opBloodEntrustOrderItemMapper.insertOpBloodEntrustOrderItem(opbeitem);


        //保存样品子表
        List<OpBloodEntrustOrderSample> opbeosampleList = opBloodEntrustVo.getSampleList();
        if (!ObjectUtils.isEmpty(opbeosampleList)) {
            for (OpBloodEntrustOrderSample opbeoSample : opbeosampleList) {
                if("4".equals(itemCode)){
                    opbeoSample.setBw(opbeoSample.getRemark());
                }
                opbeoSample.setOpBloodEntrustOrderId(id);
                opbeoSample.setBloodTaskItemType(itemCode);
                opbeoSample.setOpBloodEntrustOrderSampleId(IdUtils.simpleUUID());
                opbeoSample.fillCreateInfo();
                opBloodEntrustOrderSampleMapper.insertOpBloodEntrustOrderSample(opbeoSample);
            }
        }

        // 自动填充创建/更新信息
        opblo.fillCreateInfo();
        return opBloodEntrustOrderMapper.insertOpBloodEntrustOrder(opblo);
    }

    /**
     * 修改血样样品委托单
     *
     * @param opBloodEntrustVo 血样样品委托单
     * @return 结果
     */
    @Override
    @Transactional
    public int updateOpBloodEntrustOrder(OpBloodEntrustVo opBloodEntrustVo) {

        String opBloodEntrustOrderId = opBloodEntrustVo.getOpBloodEntrustOrderId();
        //如果状态不是待处理，则不允许修改
        OpBloodEntrustOrder opBloodEntrustOrder = opBloodEntrustOrderMapper.selectOpBloodEntrustOrderByOpBloodEntrustOrderId(opBloodEntrustOrderId);
        if(opBloodEntrustOrder==null){
            throw new RuntimeException("委托单不存在");
        }
        if(!EntrustOrderStatusEnum.DSL.getCode().equals(opBloodEntrustOrder.getStatus()) &&
                !EntrustOrderStatusEnum.YBH.getCode().equals(opBloodEntrustOrder.getStatus())){
            throw new RuntimeException("委托单已受理，不允许修改");
        }

        OpBloodEntrustOrder opblo = new OpBloodEntrustOrder();
        BeanUtils.copyProperties(opBloodEntrustVo, opblo);
        opblo.setOpBloodEntrustOrderId(opBloodEntrustOrderId);
        opblo.setEntrustOrderNo(opBloodEntrustVo.getEntrustOrderNo());
        opblo.fillUpdateInfo();

        // 提交时修改为未退回状态
        opBloodEntrustOrder.setIsReturn("0");
        opBloodEntrustOrder.setStatus(EntrustOrderStatusEnum.DSL.getCode());
        int count = opBloodEntrustOrderMapper.updateOpBloodEntrustOrder(opblo);
        //删除
        opBloodEntrustOrderItemMapper.updateDeleteFlagByEntrustOrderId(opBloodEntrustOrderId, DateUtils.getNowDate(), SecurityUtils.getUserId().toString());
        opBloodEntrustOrderSampleMapper.updateDeleteFlagByOrderSample(opBloodEntrustOrderId, DateUtils.getNowDate(), SecurityUtils.getUserId().toString());


       // String itemCode = opBloodEntrustVo.getItemCode();
        String itemCode = opBloodEntrustVo.getBloodTaskItemType();
        //免疫时间
        String immunityTime = opBloodEntrustVo.getImmunityTime();
        //检测口蹄疫
        String itemTy = opBloodEntrustVo.getItemTy();
        //检测病原体
        String itemBbkt = opBloodEntrustVo.getItemBbkt();

        if (ObjectUtils.isEmpty(itemCode)) {
            throw new RuntimeException("请选择检测项目");
        }

        OpBloodEntrustOrderItem opbeitem = new OpBloodEntrustOrderItem();
        //保存字典值
        opbeitem.setBloodTaskItemType(itemCode);
        //字典值与检测项目项目匹配
        String diseaseTypeXy = DictUtils.getDictLabel("blood_task_item_type", itemCode);
        LabtestItems labtestItems = labtestItemsMapper.selectBsLabtestItemsByLabtestItemsName(diseaseTypeXy);
        opbeitem.setItemId(labtestItems.getLabtestItemsId());
        opbeitem.setItemCode(labtestItems.getItemCode());
        opbeitem.setItemName(labtestItems.getItemName());
        opbeitem.setOpBloodEntrustOrderItemId(IdUtils.simpleUUID());
        opbeitem.setOpBloodEntrustOrderId(opBloodEntrustOrderId);
        opbeitem.setIsDelete("0");
        opbeitem.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        opbeitem.setCreateTime(DateUtils.getNowDate());
        opbeitem.setImmunityTime(immunityTime);
        opbeitem.setItemTy(itemTy);
        opbeitem.setItemBbkt(itemBbkt);
        opBloodEntrustOrderItemMapper.insertOpBloodEntrustOrderItem(opbeitem);


//        //保存检测项目子表
//        String[] itemCodes = itemCode.split(",");
//        for (String code : itemCodes) {
//            OpBloodEntrustOrderItem opbeitem = new OpBloodEntrustOrderItem();
//            opbeitem.setItemCode(code);
//            opbeitem.setOpBloodEntrustOrderItemId(IdUtils.simpleUUID());
//            opbeitem.setOpBloodEntrustOrderId(opBloodEntrustOrderId);
//            opbeitem.setImmunityTime(immunityTime);
//            opbeitem.setItemTy(itemTy);
//            opbeitem.setItemBbkt(itemBbkt);
//            opbeitem.fillCreateInfo();
//            opBloodEntrustOrderItemMapper.insertOpBloodEntrustOrderItem(opbeitem);
//        }

        //保存样品子表
        List<OpBloodEntrustOrderSample> opbeosampleList = opBloodEntrustVo.getSampleList();
        if (!ObjectUtils.isEmpty(opbeosampleList)) {
            for (OpBloodEntrustOrderSample opbeoSample : opbeosampleList) {
                if("4".equals(itemCode)){
                    opbeoSample.setBw(opbeoSample.getRemark());
                }
                opbeoSample.setBloodTaskItemType(itemCode);
                opbeoSample.setOpBloodEntrustOrderId(opBloodEntrustOrderId);
                opbeoSample.setOpBloodEntrustOrderSampleId(IdUtils.simpleUUID());
                opbeoSample.fillCreateInfo();
                opBloodEntrustOrderSampleMapper.insertOpBloodEntrustOrderSample(opbeoSample);
            }
        }
        //接收
        if (YesNo2Enum.YES.getCode().equals(opBloodEntrustVo.getIsReceive())) {
            OpSampleReceiveDto dto = new OpSampleReceiveDto();
            dto.setType(EntrustOrderTypeEnum.BLOOD.getCode());
            String[] sampleIds = opBloodEntrustVo.getSampleList().
                    stream().map(OpBloodEntrustOrderSample::getOpBloodEntrustOrderSampleId).toArray(String[]::new);
            dto.setSampleIds(sampleIds);
            sampleReceiveService.add(dto);
        }

        return count;
    }

    /**
     * 根据类型下载导入模板
     * (此版本不依赖 ServletUtils.writeAttachment)
     *
     * @param response HttpServletResponse
     * @param dto      包含 bloodTaskItemType 的 DTO
     * @return null (因为响应是直接写入的)
     */
    @Override
    public String downloadImportModel(HttpServletResponse response, OpJczxTestTaskDto dto) {
        String bloodTaskItemType = dto.getBloodTaskItemType();
        String fileName;

        // 1. 根据类型选择文件名
         if ("8".equals(bloodTaskItemType)) {
            fileName = "生化样品导入模板.xlsx";
        } else if ("9".equals(bloodTaskItemType)) {
            fileName = "早孕样品导入模板.xlsx";
        } else if ("4".equals(bloodTaskItemType)) {
             fileName = "BVDV抗原样品导入模板.xlsx";
        } else {
            fileName = "疫病样品导入模板.xlsx";
        }

        // 2. 构造完整的文件路径
        String filePath = templateBasePath + File.separator + fileName;
        File file = new File(filePath);

        // 3. 检查文件是否存在
        if (!file.exists() || !file.canRead()) {
            //log.error("模板文件不存在或无法读取: {}", filePath);
            throw new ServiceException("指定的模板文件不存在: " + fileName);
        }

        // 4. 【核心修改】手动实现文件下载
        try {
            // A. 替换 FileUtils.readBytes(file)
            // 使用 Java 7+ (NIO) 的标准方法读取文件为字节数组
            byte[] bytes = Files.readAllBytes(file.toPath());

            // B. 替换 ServletUtils.writeAttachment(...)
            //    (I) 重置响应
            response.reset();

            //    (II) 设置响应头 (针对 .xlsx 文件)
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");

            //    (III) 使用您已有的 ServletUtils.urlEncode 方法来处理中文文件名
            //          (您贴出的 ServletUtils.java 源码中包含了这个方法)
            String encodedFileName = ServletUtils.urlEncode(fileName);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

            //    (IV) 手动将字节流写入响应
            //         使用 try-with-resources 语法，确保流自动关闭
            try (OutputStream os = response.getOutputStream()) {
                os.write(bytes);
                os.flush();
            }

        } catch (Exception e) {
            //log.error("下载模板文件失败: {}", filePath, e);
            // 如果异常发生在流写入之后，重置响应以防万一
            if (response.isCommitted()) {
                response.reset();
            }
            // 抛出业务异常，让全局异常处理器捕获
            throw new ServiceException("下载模板文件失败: " + e.getMessage());
        }

        // 5. 返回 null，因为响应已手动处理
        return null;
    }

}
