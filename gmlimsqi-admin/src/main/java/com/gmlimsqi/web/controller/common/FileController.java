package com.gmlimsqi.web.controller.common;

import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.framework.config.ServerConfig;
import com.gmlimsqi.system.domain.SysUploadFile;
import com.gmlimsqi.system.service.ISysUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件管理接口
 */
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private ISysUploadFileService sysUploadFileService;
    
    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public AjaxResult upload(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = sysUploadFileService.uploadFile(file);
            return AjaxResult.success("上传成功", fileId);
        } catch (Exception e) {
            return AjaxResult.error("上传失败: " + e.getMessage());
        }
    }


    /**
     * 文件上传并返回FileId和Url
     */
    @PostMapping("/uploadAndGetUrl")
    public AjaxResult uploadAndGetUrl(@RequestParam("file") MultipartFile file)
    {
        try
        {

            // 3. 调用 Service 方法，获取 SysUploadFile 对象
            SysUploadFile sysFile = sysUploadFileService.uploadFileAndGetInfo(file,serverConfig.getUrl(),false); // 假设您已修改 Service

            // 4. 在 Controller 层组装完整的 URL
            String fileUrl = sysFile.getUrl();

            // 5. 构建返回给前端的 Map
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("fileId", sysFile.getFileId());
            resultMap.put("fileUrl", fileUrl);

            return AjaxResult.success(resultMap);
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 文件上传并返回FileId和Url
     */
    @PostMapping("/uploadAndGetUrlWithWatermark")
    public AjaxResult uploadAndGetUrlWithWatermark(@RequestParam("file") MultipartFile file)
    {
        try
        {

            // 3. 调用 Service 方法，获取 SysUploadFile 对象
            SysUploadFile sysFile = sysUploadFileService.uploadFileAndGetInfo(file,serverConfig.getUrl(),true); // 假设您已修改 Service

            // 4. 在 Controller 层组装完整的 URL
            String fileUrl = sysFile.getUrl();

            // 5. 构建返回给前端的 Map
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("fileId", sysFile.getFileId());
            resultMap.put("fileUrl", fileUrl);

            return AjaxResult.success(resultMap);
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 多文件上传
     */
    @PostMapping("/uploads")
    public AjaxResult uploads(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> fileIdList = new ArrayList<>();
            for (MultipartFile file : files) {
                String fileId = sysUploadFileService.uploadFile(file);
                fileIdList.add(fileId);
            }
            // 将文件ID列表以逗号拼接
            String fileIds = String.join(",", fileIdList);

            return AjaxResult.success("上传成功", fileIds);
        } catch (Exception e) {
            return AjaxResult.error("上传失败: " + e.getMessage());
        }
    }



    
    /**
     * 文件下载
     */
    @GetMapping("/download/{fileId}")
    public void download(@PathVariable String fileId, HttpServletResponse response) {
        sysUploadFileService.downloadFile(fileId, response);
    }
    
    /**
     * 文件预览
     */
    @GetMapping("/preview/{fileId}")
    public void preview(@PathVariable String fileId, HttpServletResponse response) {
        sysUploadFileService.previewFile(fileId, response);
    }
    /**
     * excel文件预览
     */
    @GetMapping("/previewExcel/{fileId}")
    public void previewExcel(@PathVariable String fileId, HttpServletResponse response) {
        sysUploadFileService.previewExcel(fileId, response);
    }
    /**
     * 删除文件
     */
    @DeleteMapping("/{fileId}")
    public AjaxResult delete(@PathVariable String fileId) {
        return toAjax(sysUploadFileService.deleteFile(fileId));
    }
}