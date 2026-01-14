package com.gmlimsqi.business.labtest.controller;

import com.gmlimsqi.business.labtest.dto.OpSampleTestProgressDto;
import com.gmlimsqi.business.labtest.service.IOpSampleTestProgressService;
import com.gmlimsqi.business.labtest.vo.OpSampleTestProgressVo;
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

@RestController
@RequestMapping("/labtest/sampleTestProgress")
public class OpSampleTestProgressController extends BaseController {

    @Autowired
    private IOpSampleTestProgressService sampleTestProgressService;

    /**
     * 查询样品接收列表
     */
    @PreAuthorize("@ss.hasPermi('labtest:sampleTestProgress:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpSampleTestProgressDto sampleTestProgressDto)
    {
        startPage();
        List<OpSampleTestProgressVo> list = sampleTestProgressService.selectSampleTestProgressList(sampleTestProgressDto);
        return getDataTable(list);
    }

    /**
     * 导出样品接收列表
     */
    @PreAuthorize("@ss.hasPermi('labtest:sampleTestProgress:export')")
    @Log(title = "样品接收", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpSampleTestProgressDto sampleTestProgressDto)
    {
        List<OpSampleTestProgressVo> list = sampleTestProgressService.selectSampleTestProgressList(sampleTestProgressDto);
        ExcelUtil<OpSampleTestProgressVo> util = new ExcelUtil<OpSampleTestProgressVo>(OpSampleTestProgressVo.class);
        util.exportExcel(response, list, "样品接收单数据");
    }
    

}
