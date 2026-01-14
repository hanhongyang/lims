package com.gmlimsqi.business.basicdata.controller;

import com.gmlimsqi.business.basicdata.domain.BusinessSupplier;
import com.gmlimsqi.business.basicdata.service.IBusinessSupplierService;
import com.gmlimsqi.common.annotation.Anonymous;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.annotation.RepeatSubmit;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.domain.R;
import com.gmlimsqi.common.core.page.TableDataInfo;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.sap.accept.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

/**
 * 供应商Controller
 *
 * @author gmlimsqi
 * @date 2023-04-13
 */
@RestController
@RequestMapping("/business/supplier")
public class BusinessSupplierController extends BaseController {
    
    @Autowired
    private IBusinessSupplierService businessSupplierService;
    @Autowired
    private CustomerService customerService;
    
    /**
     * 查询供应商列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BusinessSupplier businessSupplier) {
        startPage();
        List<BusinessSupplier> list =
                businessSupplierService.selectBusinessSupplierList(businessSupplier);
        return getDataTable(list);
    }
    
    /**
     * 查询供应商启用列表
     */
    @Anonymous
    @GetMapping("/listNoAuth")
    public TableDataInfo listNoAuth(BusinessSupplier businessSupplier) {
        startPage();
        List<BusinessSupplier> list =
                businessSupplierService.selectBusinessSupplierList(businessSupplier);
        return getDataTable(list);
    }


    
    /**
     * 导出供应商列表
     */
    @PreAuthorize("@ss.hasPermi('business:supplier:export')")
    @Log(title = "供应商", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response,
                       BusinessSupplier businessSupplier) {
        List<BusinessSupplier> list =
                businessSupplierService.selectBusinessSupplierList(businessSupplier);
        ExcelUtil<BusinessSupplier> util =
                new ExcelUtil<BusinessSupplier>(BusinessSupplier.class);
        util.exportExcel(response, list, "供应商数据");
    }
    
    /**
     * 获取供应商详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:supplier:query')")
    @GetMapping(value = "/{id}")
    public R<BusinessSupplier> getInfo(@PathVariable("id") String id) {
        return R.ok(businessSupplierService.selectBusinessSupplierById(id));
    }
    
    /**
     * 新增供应商
     */
    @PreAuthorize("@ss.hasPermi('business:supplier:add')")
    @Log(title = "供应商", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusinessSupplier businessSupplier) {
        return toAjax(businessSupplierService.insertBusinessSupplier(businessSupplier));
    }
    
    /**
     * 修改供应商
     */
    @PreAuthorize("@ss.hasPermi('business:supplier:edit')")
    @Log(title = "供应商", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusinessSupplier businessSupplier) {
        return toAjax(businessSupplierService.updateBusinessSupplier(businessSupplier));
    }
    
    /**
     * 删除供应商
     */
    @PreAuthorize("@ss.hasPermi('business:supplier:remove')")
    @Log(title = "供应商", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(businessSupplierService.deleteBusinessSupplierByIds(ids));
    }
    
    /**
     * sap获取供应商数据
     * @return
     */
    @GetMapping("/info")
    public R<Integer> info() {
        try {
            customerService.quartzSupplier();
        } catch (ParseException e) {
            return R.fail();
        }
        return R.ok();
    }
    

    
}
