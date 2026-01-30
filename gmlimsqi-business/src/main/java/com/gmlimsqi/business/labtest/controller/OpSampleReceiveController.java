package com.gmlimsqi.business.labtest.controller;

import com.gmlimsqi.business.labtest.dto.OpSampleReceiveDto;
import com.gmlimsqi.business.labtest.dto.ReturnDTO;
import com.gmlimsqi.business.labtest.vo.OpSampleReceiveDetailVo;
import com.gmlimsqi.business.labtest.vo.OpSampleReceiveVo;
import com.gmlimsqi.business.labtest.service.IOpSampleReceiveService;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.page.TableDataInfo;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.gmlimsqi.common.utils.PageUtils.startPage;

@RestController
@RequestMapping("/labtest/sampleReceive")
public class OpSampleReceiveController  extends BaseController {

    @Autowired
    private IOpSampleReceiveService sampleReceiveService;



    /**
     * 查询样品接收列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:sampleReceive:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpSampleReceiveDto sampleReceiveDto)
    {
        startPage();
        List<OpSampleReceiveVo> list = sampleReceiveService.selectSampleReceiveList(sampleReceiveDto);
        return getDataTable(list);
    }

    /**
     * 查询样品接收列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:sampleReceive:list')")
    @GetMapping("/jhwlist")
    public TableDataInfo selectJhwSampleReceiveList(OpSampleReceiveDto sampleReceiveDto)
    {
        startPage();
        List<OpSampleReceiveVo> list = sampleReceiveService.selectJhwSampleReceiveList(sampleReceiveDto);
        return getDataTable(list);
    }

    /**
     * 导出样品接收列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:sampleReceive:export')")
    @Log(title = "样品接收", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpSampleReceiveDto sampleReceiveDto)
    {
        List<OpSampleReceiveVo> list = sampleReceiveService.selectSampleReceiveList(sampleReceiveDto);
        ExcelUtil<OpSampleReceiveVo> util = new ExcelUtil<OpSampleReceiveVo>(OpSampleReceiveVo.class);
        util.exportExcel(response, list, "样品接收单数据");
    }

    /**
     * 获取样品接收详细信息
     */
    //@PreAuthorize("@ss.hasPermi('labtest:sampleReceive:query')")
    @GetMapping(value = "/{entrustOrderNo}")
    public AjaxResult getInfo( @RequestParam(value = "type", required = false) String type,@PathVariable("entrustOrderNo") String entrustOrderNo)
    {
        if(StringUtils.isEmpty(entrustOrderNo)){
            return error("缺少参数委托单编号");
        }
        OpSampleReceiveDetailVo detailVo = sampleReceiveService.selectSampleReceiveByEntrustOrderNo(type, entrustOrderNo);
        if(detailVo == null){
            return error("查询失败");
        }
        return success(detailVo);
    }


    //作废样品委托单
    //@PreAuthorize("@ss.hasPermi('labtest:sampleReceive:edit')")
    @PostMapping("/deleteSample")
    public AjaxResult deleteSample(@RequestParam(value = "type", required = true) String type, @RequestParam("sampleId") String sampleId)
    {
        if(StringUtils.isEmpty(sampleId)){
            return error("缺少参数样品id");
        }
        sampleReceiveService.deleteSample(type, sampleId);

        return success("作废成功");
    }


    //接收样品
    @PostMapping
    public AjaxResult add(@RequestBody OpSampleReceiveDto dto)
    {
        sampleReceiveService.add(dto);
        return success("接收成功");
    }

    /**
     * 样品退回
     */
     //@PreAuthorize("@ss.hasPermi('labtest:sampleReceive:returnSample')")
    @Log(title = "样品退回", businessType = BusinessType.UPDATE)
    @PostMapping("/returnSample")
    public AjaxResult returnSample(@RequestBody ReturnDTO dto)
    {
        sampleReceiveService.returnSample(dto);
        return success("退回成功");
    }


}
