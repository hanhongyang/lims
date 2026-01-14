package com.gmlimsqi.business.labtest.service.impl;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.basicdata.domain.LabtestItems;
import com.gmlimsqi.business.basicdata.mapper.LabtestItemsMapper;
import com.gmlimsqi.business.labtest.domain.*;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.dto.OpSampleReceiveDto;
import com.gmlimsqi.business.labtest.mapper.OpPcrEntrustOrderItemMapper;
import com.gmlimsqi.business.labtest.mapper.OpPcrEntrustOrderSampleMapper;
import com.gmlimsqi.business.labtest.service.IOpSampleReceiveService;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.business.util.UserInfoProcessor;
import com.gmlimsqi.common.enums.EntrustOrderStatusEnum;
import com.gmlimsqi.common.enums.EntrustOrderTypeEnum;
import com.gmlimsqi.common.enums.YesNo2Enum;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.ServletUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import com.gmlimsqi.business.labtest.mapper.OpPcrEntrustOrderMapper;
import com.gmlimsqi.business.labtest.service.IOpPcrEntrustOrderService;

import javax.servlet.http.HttpServletResponse;

/**
 * PCR样品委托单Service业务层处理
 * 
 * @author hhy
 * @date 2025-09-17
 */
@Service
public class OpPcrEntrustOrderServiceImpl implements IOpPcrEntrustOrderService 
{
    @Autowired
    private OpPcrEntrustOrderMapper opPcrEntrustOrderMapper;
    @Autowired
    private UserInfoProcessor userInfoProcessor;
    @Autowired
    private OpPcrEntrustOrderSampleMapper sampleMapper;
    @Autowired
    private OpPcrEntrustOrderItemMapper orderItemMapper;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;
    @Autowired
    private IOpSampleReceiveService sampleReceiveService;
    @Autowired
    private LabtestItemsMapper itemMapper;
    @Value("${ruoyi.profile}/templates")
    private String templateBasePath;
    @Autowired
    private ISysConfigService configService;
    /**
     * 查询PCR样品委托单
     * 
     * @param opPcrEntrustOrderId PCR样品委托单主键
     * @return PCR样品委托单
     */
    @Override
    public OpPcrEntrustOrder selectOpPcrEntrustOrderByOpPcrEntrustOrderId(String opPcrEntrustOrderId)
    {
        return opPcrEntrustOrderMapper.selectOrderDetailById(opPcrEntrustOrderId);
    }

    /**
     * 查询PCR样品委托单列表
     * 
     * @param opPcrEntrustOrder PCR样品委托单
     * @return PCR样品委托单
     */
    @Override
    public List<OpPcrEntrustOrder> selectOpPcrEntrustOrderList(OpPcrEntrustOrder opPcrEntrustOrder)
    {
        //检测中心看到所有委托单，牧场只看自己委托单
        String jczxDeptId = configService.selectConfigByKey("jczx.deptId");
        if(jczxDeptId.equals(SecurityUtils.getDeptId().toString())) {

        }else {
            opPcrEntrustOrder.setEntrustDeptId(SecurityUtils.getDeptId());
        }
        // 处理结束日期，设置为当天的23:59:59
        if (opPcrEntrustOrder.getSendSampleDateEnd() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(opPcrEntrustOrder.getSendSampleDateEnd());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            opPcrEntrustOrder.setSendSampleDateEnd(calendar.getTime());
        }

        List<OpPcrEntrustOrder> items = opPcrEntrustOrderMapper.selectOpPcrEntrustOrderList(opPcrEntrustOrder);

        // 批量处理用户名（创建人和更新人）
        userInfoProcessor.processBaseEntityUserInfo(items);

        return items;
    }

    /**
     * 新增PCR样品委托单
     * 
     * @param opPcrEntrustOrder PCR样品委托单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertOpPcrEntrustOrder(OpPcrEntrustOrder opPcrEntrustOrder){
        opPcrEntrustOrder.setOpPcrEntrustOrderId(IdUtils.simpleUUID());
        // 自动填充创建/更新信息
        opPcrEntrustOrder.fillCreateInfo();
        try {
            String orderNo = codeGeneratorUtil.generatePcrEntrustOrderNo();
            opPcrEntrustOrder.setEntrustOrderNo(orderNo);
        } catch (BusinessException e) {
            throw new BusinessException("生成委托单编号失败: " + e.getMessage());
        }
        if(!CollectionUtil.isEmpty(opPcrEntrustOrder.getSampleList())){
            for (OpPcrEntrustOrderSample opPcrEntrustOrderSample : opPcrEntrustOrder.getSampleList()){

                try {
//                    String sampleNo = codeGeneratorUtil.generatePcrSampleNo();
//                    opPcrEntrustOrderSample.setSampleNo(sampleNo);
                    opPcrEntrustOrderSample.setOpPcrEntrustOrderSampleId(IdUtils.simpleUUID());
                    opPcrEntrustOrderSample.setPcrEntrustOrderId(opPcrEntrustOrder.getOpPcrEntrustOrderId());

                    opPcrEntrustOrderSample.fillCreateInfo();
                    sampleMapper.insertOpPcrEntrustOrderSample(opPcrEntrustOrderSample);

                    //根据检测项目类型匹配项目
                    if(StringUtils.isEmpty(opPcrEntrustOrderSample.getPcrTaskItemType())){
                        throw new BusinessException(opPcrEntrustOrderSample.getName()+"检测项目为空:");
                    }else {
                        List<OpPcrEntrustOrderItem> itemList = new ArrayList<>();
                        LabtestItems queryItem = new LabtestItems();
                        queryItem.setTag(opPcrEntrustOrderSample.getPcrTaskItemType());
                        queryItem.setIsDelete(YesNo2Enum.NO.getCode());
                        queryItem.setIsEnable(YesNo2Enum.YES.getCode());
                        List<LabtestItems> labtestItems = itemMapper.selectBsLabtestItemsList(queryItem);
                        for (LabtestItems labtestItem : labtestItems) {
                            OpPcrEntrustOrderItem item = new OpPcrEntrustOrderItem();
                            item.setOpPcrEntrustOrderItemId(IdUtils.simpleUUID());
                            item.setPcrEntrustOrderSampleId(opPcrEntrustOrderSample.getOpPcrEntrustOrderSampleId());
                            item.setItemId(labtestItem.getLabtestItemsId());
                            item.fillCreateInfo();
                            itemList.add(item);
                        }

                        if (!itemList.isEmpty()) {
                            orderItemMapper.insertBatch(itemList);
                        }
                    }

                } catch (BusinessException e) {
                    throw new BusinessException("生成样品编号失败: " + e.getMessage());
                }

            }
        }


        // 自动填充创建/更新信息
        opPcrEntrustOrder.fillCreateInfo();
        int rows = opPcrEntrustOrderMapper.insertOpPcrEntrustOrder(opPcrEntrustOrder);
        return rows;
    }



    /**
     * 修改PCR样品委托单
     * 
     * @param opPcrEntrustOrder PCR样品委托单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateOpPcrEntrustOrder(OpPcrEntrustOrder opPcrEntrustOrder)
    {
        //如果状态不是待处理，则不允许修改
        OpPcrEntrustOrder selectOrder = opPcrEntrustOrderMapper.selectOpPcrEntrustOrderByOpPcrEntrustOrderId(opPcrEntrustOrder.getOpPcrEntrustOrderId());
        if(selectOrder==null){
            throw new RuntimeException("委托单不存在");
        }
        if(!EntrustOrderStatusEnum.DSL.getCode().equals(selectOrder.getStatus()) &&
                !EntrustOrderStatusEnum.YBH.getCode().equals(selectOrder.getStatus())){
            throw new RuntimeException("委托单已受理，不允许修改");
        }
        // 自动填充更新信息
        opPcrEntrustOrder.fillUpdateInfo();
        //更新子表删除标志并插入
        orderItemMapper.updateDeleteByOrderId(opPcrEntrustOrder.getUpdateBy(),opPcrEntrustOrder.getOpPcrEntrustOrderId());
        sampleMapper.updateDeleteByOrderId(opPcrEntrustOrder.getUpdateBy(),opPcrEntrustOrder.getOpPcrEntrustOrderId());
        if(!CollectionUtil.isEmpty(opPcrEntrustOrder.getSampleList())){
            for (OpPcrEntrustOrderSample opPcrEntrustOrderSample : opPcrEntrustOrder.getSampleList()){
//                String sampleNo = codeGeneratorUtil.generatePcrSampleNo();
//                opPcrEntrustOrderSample.setSampleNo(sampleNo);
                opPcrEntrustOrderSample.setOpPcrEntrustOrderSampleId(IdUtils.simpleUUID());
                opPcrEntrustOrderSample.setPcrEntrustOrderId(opPcrEntrustOrder.getOpPcrEntrustOrderId());
                opPcrEntrustOrderSample.fillCreateInfo();
                sampleMapper.insertOpPcrEntrustOrderSample(opPcrEntrustOrderSample);
                if(!CollectionUtil.isEmpty(opPcrEntrustOrderSample.getTestItem())){
                    List<OpPcrEntrustOrderItem> itemList = new ArrayList<>();
                    for (OpPcrEntrustOrderItem testItem : opPcrEntrustOrderSample.getTestItem()) {
                        OpPcrEntrustOrderItem item = new OpPcrEntrustOrderItem();
                        item.setOpPcrEntrustOrderItemId(IdUtils.simpleUUID());
                        item.setPcrEntrustOrderSampleId(opPcrEntrustOrderSample.getOpPcrEntrustOrderSampleId());
                        item.setItemId(testItem.getItemId());
                        item.setItemName(testItem.getItemName());
                        item.fillCreateInfo();
                        itemList.add(item);
                    }
                    if (!itemList.isEmpty()) {
                        orderItemMapper.insertBatch(itemList);
                    }
                }
            }
        }
        // 提交时修改为未退回状态
        opPcrEntrustOrder.setIsReturn("0");
        opPcrEntrustOrder.setStatus(EntrustOrderStatusEnum.DSL.getCode());
        int count =  opPcrEntrustOrderMapper.updateOpPcrEntrustOrder(opPcrEntrustOrder);
        //接收
        if(YesNo2Enum.YES.getCode().equals(opPcrEntrustOrder.getIsReceive())){
            OpSampleReceiveDto dto = new OpSampleReceiveDto();
            dto.setType(EntrustOrderTypeEnum.PCR.getCode());
            String[] sampleIds = opPcrEntrustOrder.getSampleList().
                    stream().map(OpPcrEntrustOrderSample :: getOpPcrEntrustOrderSampleId).toArray(String[]::new);
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
    public String downloadImportModel(HttpServletResponse response) {
        String fileName = "PCR样品导入模板.xlsx";
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
