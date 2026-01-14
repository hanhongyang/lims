package com.gmlimsqi.business.labtest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.labtest.domain.OpJczxFeedResultInfo;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedResultBaseDto;
import com.gmlimsqi.business.labtest.vo.OpJczxFeedResultVo;
import com.gmlimsqi.common.utils.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.business.labtest.domain.OpJczxFeedResultBase;
import com.gmlimsqi.business.labtest.service.IOpJczxFeedResultBaseService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 检测中心饲料检测结果基础Controller
 * 
 * @author hhy
 * @date 2025-09-25
 */
@RestController
@RequestMapping("/labtest/jczxFeedResultBase")
public class OpJczxFeedResultBaseController extends BaseController
{
    @Autowired
    private IOpJczxFeedResultBaseService opJczxFeedResultBaseService;

    /**
     * 查询检测中心饲料检测结果基础列表
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxFeedResultBase:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpJczxFeedResultBaseDto dto)
    {
        startPage();
        List<OpJczxFeedResultBase> list = opJczxFeedResultBaseService.selectOpJczxFeedResultBaseList(dto);
        return getDataTable(list);
    }

    @GetMapping("/jhwList")
    public TableDataInfo jhwList(OpJczxFeedResultBaseDto dto)
    {
        startPage();
        List<OpJczxFeedResultBase> list = opJczxFeedResultBaseService.selectJhwListList(dto);
        return getDataTable(list);
    }

    /**
     * 导出检测中心饲料检测结果基础列表
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxFeedResultBase:export')")
    @Log(title = "检测中心饲料检测结果基础", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpJczxFeedResultBaseDto dto)
    {
        List<OpJczxFeedResultBase> list = opJczxFeedResultBaseService.selectOpJczxFeedResultBaseList(dto);
        ExcelUtil<OpJczxFeedResultBase> util = new ExcelUtil<OpJczxFeedResultBase>(OpJczxFeedResultBase.class);
        util.exportExcel(response, list, "检测中心饲料检测结果基础数据");
    }

    /**
     * 获取检测中心饲料检测结果基础详细信息
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxFeedResultBase:query')")
    @GetMapping(value = "/{opJczxFeedResultBaseId}")
    public AjaxResult getInfo(@PathVariable("opJczxFeedResultBaseId") String opJczxFeedResultBaseId)
    {
        return success(opJczxFeedResultBaseService.selectOpJczxFeedResultBaseByOpJczxFeedResultBaseId(opJczxFeedResultBaseId));
    }

    /**
     * 获取检测中心饲料检测结果基础详细信息
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxFeedResultBase:query')")
    @GetMapping(value = "/jhw/{opJczxFeedResultBaseId}")
    public AjaxResult getJhwInfo(@PathVariable("opJczxFeedResultBaseId") String opJczxFeedResultBaseId)
    {
        return success(opJczxFeedResultBaseService.selectJhwInfoByBaseId(opJczxFeedResultBaseId));
    }


    /**
     * 获取初水分化验数据
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxFeedResultBase:query')")
    @GetMapping(value = "/getCsfInfo/{parentId}")
    public AjaxResult getCsfInfo(@PathVariable("parentId") String parentId)
    {
        return success(opJczxFeedResultBaseService.selectCsfByParentId(parentId));
    }

    /**
     * 获取初水分化验数据
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxFeedResultBase:query')")
    @PostMapping(value = "/getCsfInfoBySampleList/")
    public AjaxResult getCsfInfoBySampleList(@RequestBody List<String> sampleList)
    {
        return success(opJczxFeedResultBaseService.getCsfInfoBySampleList(sampleList));
    }

    /**
     * 获取初水分化验数据
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxFeedResultBase:query')")
    @PostMapping(value = "/getJhwCsfInfoBySampleList/")
    public AjaxResult getJhwCsfInfoBySampleList(@RequestBody List<String> sampleList)
    {
        return success(opJczxFeedResultBaseService.getJhwCsfInfoBySampleList(sampleList));
    }

    /**
     * 获取检测中心饲料检测结果基础初始化信息
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxFeedResultBase:query')")
    @GetMapping(value = "/getInitInfo/{invbillCode}/{itemId}")
    public AjaxResult getInitInfo(@PathVariable("invbillCode") String invbillCode,@PathVariable("itemId") String itemId)
    {
        return success(opJczxFeedResultBaseService.getInitInfo(invbillCode,itemId));
    }

    /**
     * 新增检测中心饲料检测结果基础
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxFeedResultBase:add')")
    @Log(title = "检测中心饲料检测结果基础", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpJczxFeedResultBaseDto dto) throws Exception {
        if (CollectionUtil.isEmpty(dto.getInfoList())) {
            return error("未选择样品");
        }
        String id = opJczxFeedResultBaseService.insertOpJczxFeedResultBase(dto);
        List<OpJczxFeedResultInfo> infoList = opJczxFeedResultBaseService.selectInfoListByBaseId(id);
        Map<String,Object> data = new HashMap<>();
        data.put("opJczxFeedResultBaseId",id);
        data.put("infoList",infoList);
        return success(data);
    }

    /**
     * 修改检测中心饲料检测结果基础
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxFeedResultBase:edit')")
    @Log(title = "检测中心饲料检测结果基础", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpJczxFeedResultBaseDto dto) throws Exception
    {
        if (CollectionUtil.isEmpty(dto.getInfoList())) {
            return error("未选择样品");
        }
        return toAjax(opJczxFeedResultBaseService.updateOpJczxFeedResultBase(dto));
    }


    /**
     * 新增检测中心饲料检测结果基础
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxFeedResultBase:add')")
    @Log(title = "检测中心饲料检测结果基础", businessType = BusinessType.INSERT)
    @PostMapping("/addjhw")
    public AjaxResult addjhw(@RequestBody OpJczxFeedResultBaseDto dto) throws Exception {
        if (CollectionUtil.isEmpty(dto.getInfoList())) {
            return error("未选择样品");
        }
        String id = opJczxFeedResultBaseService.insertJhwResultBase(dto);
        List<OpJczxFeedResultInfo> infoList = opJczxFeedResultBaseService.selectInfoListByBaseId(id);
        Map<String,Object> data = new HashMap<>();
        data.put("opJczxFeedResultBaseId",id);
        data.put("infoList",infoList);
        return success(data);
    }

    /**
     * 修改检测中心饲料检测结果基础
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxFeedResultBase:edit')")
    @Log(title = "检测中心饲料检测结果基础", businessType = BusinessType.UPDATE)
    @PutMapping("editJhw")
    public AjaxResult editJhw(@RequestBody OpJczxFeedResultBaseDto dto) throws Exception
    {
        if (CollectionUtil.isEmpty(dto.getInfoList())) {
            return error("未选择样品");
        }
        return toAjax(opJczxFeedResultBaseService.updateJhwResultBase(dto));
    }

    //化验完成提交
    @PostMapping("/testSubmit")
    public AjaxResult testSubmit(@RequestBody OpJczxFeedResultBaseDto dto)
    {
        if(StringUtils.isEmpty(dto.getOpJczxFeedResultBaseId())){
            return error("缺少参数");
        }
        return toAjax(opJczxFeedResultBaseService.testSubmit(dto));
    }

    @PostMapping("/saveCheck")
    public AjaxResult saveCheck(@RequestBody OpJczxFeedResultBaseDto dto)
    {
        if(StringUtils.isEmpty(dto.getOpJczxFeedResultBaseId())){
            return error("缺少参数");
        }
        if(StringUtils.isEmpty(dto.getCheckUserSignature())){
            return error("缺少签名");
        }
        return toAjax(opJczxFeedResultBaseService.saveCheck(dto));
    }

    @PostMapping("/saveExamine")
    public AjaxResult saveExamine(@RequestBody OpJczxFeedResultBaseDto dto)
    {
        if(StringUtils.isEmpty(dto.getOpJczxFeedResultBaseId())){
            return error("缺少参数");
        }
//        if(StringUtils.isEmpty(dto.getExamineUserSignature())){
//            return error("缺少签名");
//        }
        return toAjax(opJczxFeedResultBaseService.saveExamine(dto));
    }

    //取消校对


    //取消审核

    //复测
    @PostMapping("/retest")
    public AjaxResult retest(@RequestBody List<String> opJczxFeedResultInfoId)
    {
        if(CollectionUtil.isEmpty(opJczxFeedResultInfoId)){
            return error("缺少参数");
        }
        return toAjax(opJczxFeedResultBaseService.retest(opJczxFeedResultInfoId));
    }


    //复测
    @PostMapping("/jhwRetest")
    public AjaxResult jhwRetest(@RequestBody List<String> entrustOrderSampleId)
    {
        if(CollectionUtil.isEmpty(entrustOrderSampleId)){
            return error("缺少参数");
        }
        return toAjax(opJczxFeedResultBaseService.jhwRetest(entrustOrderSampleId.get(0)));
    }


    //退回到待提交
    @PostMapping("/backToSubmit")
    public AjaxResult backToSubmit(@RequestBody OpJczxFeedResultBaseDto dto)
    {
        opJczxFeedResultBaseService.backToSubmit(dto);
        return success();
    }


    //退回到待提交
    @PostMapping("/jhwBackToSubmit")
    public AjaxResult jhwBackToSubmit(@RequestBody OpJczxFeedResultBaseDto dto)
    {
        opJczxFeedResultBaseService.jhwBackToSubmit(dto);
        return success();
    }


    //根据样品编号查询化验单
    @PostMapping("/getResultBySampleNo")
    public AjaxResult getResultBySampleNo(@RequestBody OpJczxFeedResultBaseDto dto)
    {
        OpJczxFeedResultVo vo =opJczxFeedResultBaseService.getResultBySampleNo(dto);
        return success(vo);
    }

    //根据样品编号查询化验单
    @PostMapping("/getResultBySampleNo2")
    public AjaxResult getResultBySampleNo2(@RequestBody OpJczxFeedResultBaseDto dto)
    {
        OpJczxFeedResultVo vo =opJczxFeedResultBaseService.getResultBySampleNo2(dto);
        return success(vo);
    }
}
