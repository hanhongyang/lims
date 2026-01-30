package com.gmlimsqi.business.labtest.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrder;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.service.IOpBloodEntrustOrderService;
import com.gmlimsqi.business.labtest.vo.OpBloodEntrustVo;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.page.TableDataInfo;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 血样样品委托单Controller
 * 
 * @author hhy
 * @date 2025-09-20
 */
@RestController
@RequestMapping("/labtest/bloodEntrustOrder")
public class OpBloodEntrustOrderController extends BaseController
{
    @Autowired
    private IOpBloodEntrustOrderService opBloodEntrustOrderService;

    /**
     * 查询血样样品委托单列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:bloodEntrustOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpBloodEntrustOrder opBloodEntrustOrder)
    {
        startPage();
        List<OpBloodEntrustOrder> list = opBloodEntrustOrderService.selectOpBloodEntrustOrderList(opBloodEntrustOrder);
        return getDataTable(list);
    }

    /**
     * 导出血样样品委托单列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:bloodEntrustOrder:export')")
    @Log(title = "血样样品委托单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpBloodEntrustOrder opBloodEntrustOrder)
    {
        List<OpBloodEntrustOrder> list = opBloodEntrustOrderService.selectOpBloodEntrustOrderList(opBloodEntrustOrder);
        ExcelUtil<OpBloodEntrustOrder> util = new ExcelUtil<OpBloodEntrustOrder>(OpBloodEntrustOrder.class);
        util.exportExcel(response, list, "血样样品委托单数据");
    }

    /**
     * 获取血样样品委托单详细信息
     */
    //@PreAuthorize("@ss.hasPermi('labtest:bloodEntrustOrder:query')")
    @GetMapping(value = "/{opBloodEntrustOrderId}")
    public AjaxResult getInfo(@PathVariable("opBloodEntrustOrderId") String opBloodEntrustOrderId)
    {
        return success(opBloodEntrustOrderService.selectOpBloodEntrustOrderByOpBloodEntrustOrderId(opBloodEntrustOrderId));
    }

    /**
     * 新增血样样品委托单
     */
   // @PreAuthorize("@ss.hasPermi('labtest:bloodEntrustOrder:add')")
    @Log(title = "血样样品委托单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpBloodEntrustVo opBloodEntrustVo)
    {
        if(CollectionUtil.isEmpty(opBloodEntrustVo.getSampleList())){
            return AjaxResult.error("请填写样本");
        }
// 检查牛号是否重复（只检查当前列表内）
        List<String> duplicateSampleNames = checkDuplicateSampleNames(opBloodEntrustVo.getSampleList());
        if (!duplicateSampleNames.isEmpty()) {
            return AjaxResult.error("牛号重复: " + String.join(", ", duplicateSampleNames));
        }
        return toAjax(opBloodEntrustOrderService.insertOpBloodEntrustOrder(opBloodEntrustVo));
    }

    /**
     * 修改血样样品委托单
     */
   // @PreAuthorize("@ss.hasPermi('labtest:bloodEntrustOrder:edit')")
    @Log(title = "血样样品委托单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpBloodEntrustVo opBloodEntrustOrder)
    {
        if (ObjectUtils.isEmpty(opBloodEntrustOrder)) {
            throw new RuntimeException("缺少参数");
        }
        if(CollectionUtil.isEmpty(opBloodEntrustOrder.getSampleList())){
            return AjaxResult.error("请填写样本");
        }

        if (StringUtils.isEmpty(opBloodEntrustOrder.getOpBloodEntrustOrderId())) {
            throw new RuntimeException("缺少参数");
        }
// 检查牛号是否重复（只检查当前列表内）
        List<String> duplicateSampleNames = checkDuplicateSampleNames(opBloodEntrustOrder.getSampleList());
        if (!duplicateSampleNames.isEmpty()) {
            return AjaxResult.error("牛号重复: " + String.join(", ", duplicateSampleNames));
        }
        //TODO 如果委托单状态不是待受理，则不允许更新
        return toAjax(opBloodEntrustOrderService.updateOpBloodEntrustOrder(opBloodEntrustOrder));
    }

    /**
     * 下载导入模板
     */
    @PostMapping("/downloadImportModel")
    public void downloadImportModel(HttpServletResponse response, OpJczxTestTaskDto dto) {
        try {
            if(StringUtils.isEmpty(dto.getBloodTaskItemType())){
                throw new RuntimeException("先选择项目再导出");
            }
            opBloodEntrustOrderService.downloadImportModel(response,dto);
        } catch (Exception e) {
            logger.error("下载导入模板失败", e);
            throw new RuntimeException("下载导入模板失败");
        }
    }
    /**
     * 撤回委托单（将待受理状态变回待提交）
     */
    @Log(title = "饲料样品委托单", businessType = BusinessType.UPDATE)
    @GetMapping("/withdraw/{opBloodEntrustOrderId}")
    public AjaxResult withdraw(@PathVariable("opBloodEntrustOrderId") String opBloodEntrustOrderId)
    {
        opBloodEntrustOrderService.withdrawOrder(opBloodEntrustOrderId);
        return success();
    }
    /**
     * 检查牛号是否重复（只检查当前列表内）
     * @param sampleList 样本列表
     * @return 重复的牛号列表
     */
    private List<String> checkDuplicateSampleNames(List<com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderSample> sampleList) {
        List<String> duplicateNames = new ArrayList<>();

        if (CollectionUtil.isEmpty(sampleList)) {
            return duplicateNames;
        }

        // 使用Map统计每个牛号出现的次数
        Map<String, Integer> nameCountMap = new HashMap<>();

        for (com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderSample sample : sampleList) {
            String sampleName = sample.getSampleName();
            if (StringUtils.isNotEmpty(sampleName)) {
                nameCountMap.put(sampleName, nameCountMap.getOrDefault(sampleName, 0) + 1);
            }
        }

        // 找出出现次数大于1的牛号
        for (Map.Entry<String, Integer> entry : nameCountMap.entrySet()) {
            if (entry.getValue() > 1) {
                duplicateNames.add(entry.getKey());
            }
        }

        return duplicateNames;
    }

}
