package com.gmlimsqi.business.ranch.controller;

import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;
import com.gmlimsqi.business.ranch.vo.OpSamplingPlanSampleMonitorVO;
import com.gmlimsqi.framework.config.ServerConfig;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlan;
import com.gmlimsqi.business.ranch.dto.SignInNotificationDTO;
import com.gmlimsqi.business.ranch.service.IOpSamplingPlanService;
import com.gmlimsqi.business.ranch.vo.SamplingReceiveListVo;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.page.TableDataInfo;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
// [新增] 导入若依的匿名访问注解
import com.gmlimsqi.common.annotation.Anonymous;
// [新增] 导入校验注解
import org.springframework.validation.annotation.Validated;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 取样计划主Controller
 *
 * @author hhy
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/ranch/plan")
public class OpSamplingPlanController extends BaseController
{
    @Autowired
    private IOpSamplingPlanService opSamplingPlanService;
    @Autowired
    private ServerConfig serverConfig;
    /**
     * 查询取样计划主列表
     */
//    @PreAuthorize("@ss.hasPermi('ranch:plan:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpSamplingPlan opSamplingPlan)
    {
        startPage();
        List<OpSamplingPlan> list = opSamplingPlanService.selectOpSamplingPlanList(opSamplingPlan);
        return getDataTable(list);
    }

    /**
     * 导出取样计划主列表
     */
//    @PreAuthorize("@ss.hasPermi('ranch:plan:export')")
    @Log(title = "取样计划主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpSamplingPlan opSamplingPlan)
    {
        List<OpSamplingPlan> list = opSamplingPlanService.selectOpSamplingPlanList(opSamplingPlan);
        ExcelUtil<OpSamplingPlan> util = new ExcelUtil<OpSamplingPlan>(OpSamplingPlan.class);
        util.exportExcel(response, list, "取样计划主数据");
    }

    /**
     * 获取取样计划主详细信息
     */
//    @PreAuthorize("@ss.hasPermi('ranch:plan:query')")
    @GetMapping(value = "/{opSamplingPlanId}")
    public AjaxResult getInfo(@PathVariable("opSamplingPlanId") String opSamplingPlanId)
    {
        return success(opSamplingPlanService.selectOpSamplingPlanByOpSamplingPlanId(opSamplingPlanId));
    }

    /**
     * 原料取样新增
     * @param opSamplingPlan
     */
//    @PreAuthorize("@ss.hasPermi('ranch:plan:add')")
    @Log(title = "原料取样新增", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody OpSamplingPlan opSamplingPlan)
    {
        return success(opSamplingPlanService.insertOpSamplingPlan(opSamplingPlan));
    }

    /**
     * 垫料/库存取样新增
     */
//    @PreAuthorize("@ss.hasPermi('ranch:plan:add')")
    @Log(title = "垫料/库存取样新增", businessType = BusinessType.INSERT)
    @PostMapping("/addKCDL")
    public AjaxResult addKCDL(@RequestBody OpSamplingPlan opSamplingPlan)
    {
        return toAjax(opSamplingPlanService.addKCDL(opSamplingPlan));
    }

    /**
     * 接收 [egap系统] 的司机签到通知
     * (对应 LimsSamplePlanCreatedEvent 事件)
     */
    @Anonymous // 关键: 允许匿名访问
    @Log(title = "接收外部签到通知", businessType = BusinessType.INSERT)
    @PostMapping("/notifySignIn") // 你的 API URL: /ranch/plan/notifySignIn
    public AjaxResult receiveSignInNotification(@Validated @RequestBody SignInNotificationDTO notifyDTO)
    {
        try
        {
            // 打印日志，确认收到 (现在 DTO 就是根对象，不再需要 .getSignInInfo())
            logger.info("成功接收到司机签到通知: {}", notifyDTO.toString());

            // 将 DTO 传递给 Service 层进行业务处理
            // (你需要在 IOpSamplingPlanService 接口中添加这个新方法)
            opSamplingPlanService.processSignInNotification(notifyDTO,serverConfig.getUrl());

            // 告诉 一卡通 系统："我收到了，处理成功了"
            return AjaxResult.success("通知接收成功");
        }
        catch (Exception e)
        {
            // 如果 service 抛出异常
            logger.error("处理外部签到通知失败: {}", e.getMessage(), e);
            // 告诉 egap 系统："我收到了，但处理失败了"
            return AjaxResult.error("处理通知失败: " + e.getMessage());
        }
    }
    /**
     * 修改取样计划主
     */
//    @PreAuthorize("@ss.hasPermi('ranch:plan:edit')")
    @Log(title = "取样计划主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpSamplingPlan opSamplingPlan)
    {
        return toAjax(opSamplingPlanService.updateOpSamplingPlan(opSamplingPlan));
    }
    /**
     * [新增] 状态变更为：取样完成 (1)
     */
//    @PreAuthorize("@ss.hasPermi('ranch:plan:complete')") // 确保分配了此权限
    @Log(title = "取样计划状态", businessType = BusinessType.UPDATE)
    @PutMapping("/complete/{opSamplingPlanId}")
    public AjaxResult complete(@PathVariable String opSamplingPlanId)
    {
        return toAjax(opSamplingPlanService.updateStatus(opSamplingPlanId, "1"));
    }

    /**
     * [新增] 状态变更为：无需取样 (2)
     */
//    @PreAuthorize("@ss.hasPermi('ranch:plan:skip')") // 确保分配了此权限
    @Log(title = "取样计划状态", businessType = BusinessType.UPDATE)
    @PutMapping("/skip/{opSamplingPlanId}")
    public AjaxResult skip(@PathVariable String opSamplingPlanId)
    {
        // 复用 Service 方法
        // SQL Update:
        // "UPDATE op_sampling_plan SET status = '2', update_by = ?, update_time = ? WHERE op_sampling_plan_id = ?"
        return toAjax(opSamplingPlanService.updateStatus(opSamplingPlanId, "2"));
    }
    /**
     * 删除取样计划主
     */
    @PreAuthorize("@ss.hasPermi('ranch:plan:remove')")
    @Log(title = "取样计划主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{opSamplingPlanIds}")
    public AjaxResult remove(@PathVariable String[] opSamplingPlanIds)
    {
        return toAjax(opSamplingPlanService.deleteOpSamplingPlanByOpSamplingPlanIds(opSamplingPlanIds));
    }

    /**
     * 查询样品接收列表
     */
//    @PreAuthorize("@ss.hasPermi('ranch:plan:receiveList')")
    @GetMapping("/receiveList")
    public TableDataInfo receiveList(OpSamplingPlan opSamplingPlan)
    {
        startPage();
        List<SamplingReceiveListVo> list = opSamplingPlanService.selectOpSamplingPlanReceiveList(opSamplingPlan);
        return getDataTable(list);
    }

    @GetMapping("/SampleInfo/{opSamplingPlanSampleId}")
    public AjaxResult getSampleInfo(@PathVariable String opSamplingPlanSampleId){
        return success(opSamplingPlanService.selectopSamplingPlanSampleInfoById(opSamplingPlanSampleId));
    }

    /**
     * 导出样品接收列表
     * */
    @PostMapping("/exportReceiveList")
    public void exportReceiveList(HttpServletResponse response,OpSamplingPlan opSamplingPlan)
    {
        List<SamplingReceiveListVo> list = opSamplingPlanService.selectOpSamplingPlanReceiveList(opSamplingPlan);
        ExcelUtil<SamplingReceiveListVo> util = new ExcelUtil<SamplingReceiveListVo>(SamplingReceiveListVo.class);
        util.exportExcel(response, list, "样品接收列表");
    }

    /**
     * 查询样品接收列表
     */
//    @PreAuthorize("@ss.hasPermi('ranch:plan:receiveList')")
    @GetMapping("/receiveList2")
    public TableDataInfo receiveList2(OpSamplingPlan opSamplingPlan)
    {
        startPage();
        List<SamplingReceiveListVo> list = opSamplingPlanService.selectOpSamplingPlanReceiveList2(opSamplingPlan);
        return getDataTable(list);
    }

    /**
     * 样品接收
     */
//    @PreAuthorize("@ss.hasPermi('ranch:plan:receive')")
    @Log(title = "取样计划主", businessType = BusinessType.UPDATE)
    @PutMapping("/receive")
    public AjaxResult receive(@RequestBody List<String> ids)
    {
        return toAjax(opSamplingPlanService.receiveOpSamplingPlan(ids));
    }


    /**
     * 获取取样二维码
     */
//    @PreAuthorize("@ss.hasPermi('ranch:plan:getSampleQRCode')")
    @GetMapping(value = "/getQRCode/{count}")
    public AjaxResult getQRCode(@PathVariable("count") String count)
    {
        return success(opSamplingPlanService.getSampleQRCode(count));
    }

    //根据签到id查询是否放行
    @Anonymous
    @GetMapping("/isRelease/{signInId}")
    public AjaxResult isRelease(@PathVariable("signInId") String signInId)
    {
        if(signInId==null || signInId.isEmpty()){
            return AjaxResult.error("签到ID不能为空");
        }
        String result=opSamplingPlanService.isRelease(signInId);
        if(result==null || result.isEmpty()){
            return AjaxResult.error("未查询到相关信息");
        }else if(result.equals("1")){
            return AjaxResult.success("已放行");
        }else{
            return AjaxResult.error("未放行");
        }
    }

    /**
     * 取样计划作废
     */
//    @PreAuthorize("@ss.hasPermi('ranch:plan:edit')")
    @Log(title = "取样计划作废", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel/{opSamplingPlanId}")
    public AjaxResult cancel(@PathVariable String opSamplingPlanId)
    {
        return toAjax(opSamplingPlanService.cancelOpSamplingPlan(opSamplingPlanId));
    }

    /**
     * 查询待销毁列表
     */
     //    @PreAuthorize("@ss.hasPermi('ranch:plan:destroyList')")
    @GetMapping("/destroyList")
    public TableDataInfo destroyList(OpSamplingPlanSample opSamplingPlanSample)
    {
        startPage();
        List<OpSamplingPlanSample> list = opSamplingPlanService.selectOpSamplingPlanSampleListByPlanId(opSamplingPlanSample);
        return getDataTable(list);
    }

    /**
     * 销毁样品
     */
     //    @PreAuthorize("@ss.hasPermi('ranch:plan:destroy')")
    @Log(title = "销毁样品", businessType = BusinessType.UPDATE)
    @PutMapping("/destroy")
    public AjaxResult destroy(@RequestBody String opSamplingPlanSampleId)
    {
        return toAjax(opSamplingPlanService.destroyOpSamplingPlanSample(opSamplingPlanSampleId));
    }

    /**
     * 关联取样计划
     */
     //    @PreAuthorize("@ss.hasPermi('ranch:plan:link')")
    @Log(title = "关联取样计划", businessType = BusinessType.UPDATE)
    @PostMapping("/link")
    public AjaxResult link(@RequestBody OpSamplingPlanSample opSamplingPlanSample)
    {
        return toAjax(opSamplingPlanService.linkOpSamplingPlanSample(opSamplingPlanSample));
    }
    /**
     * 管理员全流程监控：所有样品详情平铺列表（含检测项目进度）
     */
    @GetMapping("/monitor/list")
    public TableDataInfo monitorList(OpSamplingPlanSample opSamplingPlanSample) {
        startPage();
        List<OpSamplingPlanSampleMonitorVO> list = opSamplingPlanService.selectSampleMonitorList(opSamplingPlanSample);
        return getDataTable(list);
    }

    /**
     * 修改取样物料
     */
     //    @PreAuthorize("@ss.hasPermi('ranch:plan:editSampleMaterial')")
    @Log(title = "修改取样物料", businessType = BusinessType.UPDATE)
    @PutMapping("/editSampleMaterial")
    public AjaxResult editSampleMaterial(@RequestBody OpSamplingPlanSample opSamplingPlanSample)
    {
        return toAjax(opSamplingPlanService.editOpSamplingPlanSampleMaterial(opSamplingPlanSample));
    }

    /**
     * 修改样品检测项目
     */
     //    @PreAuthorize("@ss.hasPermi('ranch:plan:editSampleItem')")
    @Log(title = "修改样品检测项目", businessType = BusinessType.UPDATE)
    @PutMapping("/editSampleItem")
    public AjaxResult editSampleItem(@RequestBody OpSamplingPlanSample opSamplingPlanSample)
    {
        return toAjax(opSamplingPlanService.editOpSamplingPlanSampleItem(opSamplingPlanSample));
    }

}
