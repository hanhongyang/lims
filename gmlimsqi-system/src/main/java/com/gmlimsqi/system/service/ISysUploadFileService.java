package com.gmlimsqi.system.service;

import com.gmlimsqi.system.domain.SysUploadFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface ISysUploadFileService {
    
    /**
     * 上传文件
     */
    String uploadFile(MultipartFile file) throws Exception;
    // 在 ISysUploadFileService 接口中添加
    String uploadFile(byte[] fileBytes, String originalFilename, String contentType) throws Exception;
    /**
     * 下载文件
     */
    void downloadFile(String fileId, HttpServletResponse response);
    
    /**
     * 预览文件
     */
    void previewFile(String fileId, HttpServletResponse response);
    /**
     * Excel文件预览（转换为HTML）
     */
    void previewExcel(String fileId, HttpServletResponse response);
    /**
     * 删除文件
     */
    int deleteFile(String fileId);
    
    /**
     * 根据文件ID获取文件信息
     */
    SysUploadFile getFileById(String fileId);
    
    /**
     * 标记文件为正式使用
     */
    int markFileAsPermanent(String fileId);

    SysUploadFile uploadFileAndGetInfo(MultipartFile file,String url,boolean waterMark) throws Exception ;

    /**
     * ( V V V V V 变更点 V V V V V )
     * 上传 Base64 编码的文件
     *
     * @param base64Data Base64 字符串
     * @param originalFilename 原始文件名
     * @param urlPrefix API基础路径(用于构建URL)
     * @return 包含 FileId 和 Url 的文件信息
     * @throws Exception
     */
    SysUploadFile uploadBase64File(String base64Data, String originalFilename ,String urlPrefix) throws Exception;
}