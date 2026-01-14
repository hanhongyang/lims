import com.gmlimsqi.business.labtest.domain.OpOutentrustRegisterItem;
import com.gmlimsqi.business.labtest.service.IOpOutentrustRegisterItemService;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.page.TableDataInfo;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 外部委托检测单化验项目子Controller
 *
 * @author wgq
 * @date 2025-09-17
 */
@RestController
@RequestMapping("/labtest/item")
public class OpOutentrustRegisterItemController extends BaseController
{
    @Autowired
    private IOpOutentrustRegisterItemService opOutentrustRegisterItemService;

    /**
     * 查询外部委托检测单化验项目子列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:item:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpOutentrustRegisterItem opOutentrustRegisterItem)
    {
        startPage();
        List<OpOutentrustRegisterItem> list = opOutentrustRegisterItemService.selectOpOutentrustRegisterItemList(opOutentrustRegisterItem);
        return getDataTable(list);
    }

    /**
     * 导出外部委托检测单化验项目子列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:item:export')")
    @Log(title = "外部委托检测单化验项目子", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpOutentrustRegisterItem opOutentrustRegisterItem)
    {
        List<OpOutentrustRegisterItem> list = opOutentrustRegisterItemService.selectOpOutentrustRegisterItemList(opOutentrustRegisterItem);
        ExcelUtil<OpOutentrustRegisterItem> util = new ExcelUtil<OpOutentrustRegisterItem>(OpOutentrustRegisterItem.class);
        util.exportExcel(response, list, "外部委托检测单化验项目子数据");
    }

    /**
     * 获取外部委托检测单化验项目子详细信息
     */
    //@PreAuthorize("@ss.hasPermi('labtest:item:query')")
    @GetMapping(value = "/{outentrustRegisterItemId}")
    public AjaxResult getInfo(@PathVariable("outentrustRegisterItemId") String outentrustRegisterItemId)
    {
        return success(opOutentrustRegisterItemService.selectOpOutentrustRegisterItemByOutentrustRegisterItemId(outentrustRegisterItemId));
    }

    /**
     * 新增外部委托检测单化验项目子
     */
    //@PreAuthorize("@ss.hasPermi('labtest:item:add')")
    @Log(title = "外部委托检测单化验项目子", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpOutentrustRegisterItem opOutentrustRegisterItem)
    {
        return toAjax(opOutentrustRegisterItemService.insertOpOutentrustRegisterItem(opOutentrustRegisterItem));
    }

    /**
     * 修改外部委托检测单化验项目子
     */
    //@PreAuthorize("@ss.hasPermi('labtest:item:edit')")
    @Log(title = "外部委托检测单化验项目子", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpOutentrustRegisterItem opOutentrustRegisterItem)
    {
        return toAjax(opOutentrustRegisterItemService.updateOpOutentrustRegisterItem(opOutentrustRegisterItem));
    }

    /**
     * 删除外部委托检测单化验项目子
     */
    //@PreAuthorize("@ss.hasPermi('labtest:item:remove')")
    @Log(title = "外部委托检测单化验项目子", businessType = BusinessType.DELETE)
    @DeleteMapping("/{outentrustRegisteritemId}")
    public AjaxResult remove(@PathVariable String[] outentrustRegisteritemId)
    {
        return toAjax(opOutentrustRegisterItemService.deleteOpOutentrustRegisterItemByOutentrustRegisteritemId(outentrustRegisteritemId));
    }
}
