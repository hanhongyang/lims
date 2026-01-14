package com.gmlimsqi.business.basicdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.basicdata.vo.UserLabtestItemVo;
import com.gmlimsqi.common.utils.SecurityUtils;
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
import com.gmlimsqi.business.basicdata.domain.UserLabtestItem;
import com.gmlimsqi.business.basicdata.service.IUserLabtestItemService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 检测项目对应人员Controller
 * 
 * @author hhy
 * @date 2025-08-08
 */
@RestController
@RequestMapping("/basicdata/userLabTestItem")
public class UserLabtestItemController extends BaseController
{
    @Autowired
    private IUserLabtestItemService userLabtestItemService;

    /**
     * 查询检测项目对应人员列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:userLabTestItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserLabtestItem userLabtestItem)
    {
        startPage();
        List<UserLabtestItemVo> list = userLabtestItemService.selectUserLabtestItemList(userLabtestItem);
        return getDataTable(list);
    }


    /**
     * 获取当前登录用户的校对项目ID列表
     */
    @GetMapping("/getCheckItemList")
    public AjaxResult getCheckItemList()
    {
        String userId = String.valueOf(SecurityUtils.getUserId());
        List<String> list = userLabtestItemService.selectCheckItemIdsByUserId(userId);
        return success(list);
    }

    /**
     * 导出检测项目对应人员列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:userLabTestItem:export')")
    @Log(title = "检测项目对应人员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserLabtestItem userLabtestItem)
    {
        List<UserLabtestItemVo> list = userLabtestItemService.selectUserLabtestItemList(userLabtestItem);
        ExcelUtil<UserLabtestItemVo> util = new ExcelUtil<UserLabtestItemVo>(UserLabtestItemVo.class);
        util.exportExcel(response, list, "检测项目对应人员数据");
    }

    /**
     * 获取检测项目对应人员详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:userLabTestItem:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") String userId)
    {
        if(StringUtils.isEmpty(userId)){
            return error("请传入userId");
        }
        return success(userLabtestItemService.selectUserLabtestItemByUserId(userId));
    }

    /**
     * 新增检测项目对应人员
     */
    @PreAuthorize("@ss.hasPermi('basicdata:userLabTestItem:add')")
    @Log(title = "检测项目对应人员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserLabtestItem userLabtestItem)
    {
        //TODO 如果该人员已存在对应了，合并项目还是报错？
        if(StringUtils.isNotEmpty(userLabtestItem.getLabtestItemsIdList())){
            return toAjax(userLabtestItemService.insertUserLabtestItem(userLabtestItem));
        }else {
            return AjaxResult.error("请输入检测项目");
        }
    }

    /**
     * 修改检测项目对应人员
     */
    @PreAuthorize("@ss.hasPermi('basicdata:userLabTestItem:edit')")
    @Log(title = "检测项目对应人员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserLabtestItem userLabtestItem)
    {
        //TODO 用户已停用或者项目已停用，无法保存，是否删除

        if(StringUtils.isNotEmpty(userLabtestItem.getLabtestItemsIdList())){
            return toAjax(userLabtestItemService.updateUserLabtestItem(userLabtestItem));
        }else {
            return AjaxResult.error("请输入检测项目");
        }
    }

    /**
     * 删除检测项目对应人员
     */
    @PreAuthorize("@ss.hasPermi('basicdata:userLabTestItem:remove')")
    @Log(title = "检测项目对应人员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userLabtestItemId}")
    public AjaxResult remove(@PathVariable String userLabtestItemId)
    {
        return toAjax(userLabtestItemService.updateDeleteFlagById(userLabtestItemId));
    }

    /**
     * 删除检测项目对应人员
     */
    @PreAuthorize("@ss.hasPermi('basicdata:userLabTestItem:remove')")
    @Log(title = "检测项目对应人员", businessType = BusinessType.DELETE)
    @DeleteMapping("/removeAll/{userId}")
    public AjaxResult removeAllByUserId(@PathVariable String userId)
    {
        userLabtestItemService.updateDeleteFlagByUserId(userId);
        return success();
    }
}
