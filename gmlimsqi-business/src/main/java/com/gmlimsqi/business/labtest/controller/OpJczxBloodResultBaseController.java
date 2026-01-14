package com.gmlimsqi.business.labtest.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.labtest.bo.BloodResultImportBo;
import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderSample;
import com.gmlimsqi.business.labtest.domain.OpJczxBloodResultBase;
import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderItem;
import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderItem;
import com.gmlimsqi.business.labtest.dto.OpJczxBloodResultDto;
import com.gmlimsqi.business.labtest.dto.OpJczxPcrResultDto;
import com.gmlimsqi.business.labtest.service.IOpBloodEntrustOrderSampleService;
import com.gmlimsqi.business.labtest.service.IOpJczxBloodResultBaseService;
import com.gmlimsqi.business.labtest.service.IOpBloodEntrustOrderItemService;
import com.gmlimsqi.business.util.BloodReportParser;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.page.TableDataInfo;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 样品化验BloodController
 * 
 * @author hhy
 * @date 2025-10-13
 */
@RestController
@RequestMapping("/labtest/jczxBloodResult")
public class OpJczxBloodResultBaseController extends BaseController
{
    @Autowired
    private IOpJczxBloodResultBaseService opJczxBloodResultBaseService;
    @Autowired
    private IOpBloodEntrustOrderSampleService opBloodEntrustOrderSampleService;


    /**
     * 【新】审核项目结果（更新备注和流转状态）
     */
    //@PreAuthorize("@ss.hasPermi('labtest:jczxBloodResult:examine')")
    @Log(title = "Blood结果项目审核", businessType = BusinessType.UPDATE)
    @PostMapping("/examineBase") // Matches FE's /examineBase
    public AjaxResult examineBloodResultBase(@RequestBody OpJczxBloodResultDto param)
    {
        if (CollectionUtil.isEmpty(param.getSampleList())) {
            return AjaxResult.error("未接收到审核项目数据");
        }
        return toAjax(opJczxBloodResultBaseService.examineBloodResultBase(param.getSampleList(),param.getExaminePassFlag(),param.getResultNo()));
    }
    /**
     * 查询样品化验PCR列表
     */
   // @PreAuthorize("@ss.hasPermi('labtest:jczxBloodResult:query')")
    @GetMapping("/getBaseByResultNo")
    public AjaxResult getBaseByResultNo(@RequestParam("resultNo") String resultNo )
    {
        List<OpBloodEntrustOrderSample> list= opBloodEntrustOrderSampleService.getBaseByResultNo(resultNo);
        return success(list);
    }
    
   // @PreAuthorize("@ss.hasPermi('labtest:jczxBloodResult:examine')")
    @PostMapping("/cancelExamine")
    public AjaxResult cancelExamine(@RequestBody OpJczxBloodResultDto dto)
    {
        String resultNo = dto.getResultNo();

        if (StringUtils.isAnyBlank(resultNo)) {
            return AjaxResult.error("缺少必要的参数：委托单号");
        }


            int rows = opJczxBloodResultBaseService.cancelExamineBloodResult(resultNo);
            return toAjax(rows);


    }


    /**
     * 新增样品化验Blood
     */
   // @PreAuthorize("@ss.hasPermi('labtest:jczxBloodResult:add')")
    @Log(title = "样品化验Blood", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpJczxBloodResultBase opJczxBloodResultBase)
    {
        return toAjax(opJczxBloodResultBaseService.insertOpJczxBloodResultBase(opJczxBloodResultBase));
    }

    /**
     * 修改样品化验Blood
     */
   // @PreAuthorize("@ss.hasPermi('labtest:jczxBloodResult:edit')")
    @Log(title = "样品化验Blood", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpJczxBloodResultBase opJczxBloodResultBase)
    {
        return toAjax(opJczxBloodResultBaseService.updateOpJczxBloodResultBase(opJczxBloodResultBase));
    }

    /**
     * 导入Blood检测结果数据
     */
  //  @PreAuthorize("@ss.hasPermi('labtest:base:import')")
    @Log(title = "Blood检测结果", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        String message = opJczxBloodResultBaseService.importBloodResult(file);
        return AjaxResult.success(message);
    }

    @Log(title = "导入生化检测结果", businessType = BusinessType.IMPORT)
    @PostMapping("/importShData")
    public AjaxResult importShData(MultipartFile file  ) throws Exception {
        // 直接传 file 给 service
        String message = opJczxBloodResultBaseService.importBloodResultNew(file, SecurityUtils.getLoginUser().getUser().getNickName());
        return AjaxResult.success(message);
    }

}
