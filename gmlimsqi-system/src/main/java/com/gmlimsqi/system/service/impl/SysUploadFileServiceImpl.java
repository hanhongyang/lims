package com.gmlimsqi.system.service.impl;

import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.file.ImageWatermarkUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.domain.SysUploadFile;
import com.gmlimsqi.system.mapper.SysUploadFileMapper;
import com.gmlimsqi.system.mapper.SysUserMapper;
import com.gmlimsqi.system.service.ISysUploadFileService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class SysUploadFileServiceImpl implements ISysUploadFileService {

    @Autowired
    private SysUploadFileMapper sysUploadFileMapper;

    @Value("${ruoyi.profile:/home/ruoyi/upload}")
    private String uploadPath;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public String uploadFile(MultipartFile file) throws Exception {

            // 1. 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String fileName = UUID.randomUUID().toString() + "." + fileExtension;

            // 2. 创建日期目录
            String datePath = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String fullPath = uploadPath + File.separator + datePath;
            File directory = new File(fullPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 3. 保存文件
            File dest = new File(fullPath + File.separator + fileName);
//            file.transferTo(dest);
        //水印
           // handleFileWithWatermark(file, dest, fileExtension);

            // 4. 保存文件记录到数据库
            SysUploadFile sysFile = new SysUploadFile();
            sysFile.setFileId(IdUtils.simpleUUID());
            sysFile.setOriginalName(originalFilename);
            sysFile.setFilePath(datePath + "/" + fileName);
            sysFile.setFileSize(file.getSize());
            sysFile.setFileType(file.getContentType());
            sysFile.setFileExtension(fileExtension);
            sysFile.setUploadTime(new Date());
            sysFile.setStatus("1"); // 临时文件
            sysFile.setCreateTime(new Date());
            sysFile.setCreateBy(SecurityUtils.getUsername());
            sysUploadFileMapper.insertFile(sysFile);
            return sysFile.getFileId();

    }

    @Override
    public SysUploadFile uploadFileAndGetInfo(MultipartFile file,String url,boolean waterMark) throws Exception {
        // 对url地址进行修改，将http修改为https
        // TODO 生产环境需要修改为https
        url = url.replace("http://","https://");
        // 1. 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String fileName = UUID.randomUUID().toString() + "." + fileExtension;

        // 2. 创建日期目录
        String datePath = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String fullPath = uploadPath + File.separator + datePath;
        File directory = new File(fullPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 3. 保存文件
        File dest = new File(fullPath + File.separator + fileName);

        if(waterMark){//水印
            handleFileWithWatermark(file, dest, fileExtension);
        }else {
            // 【修复点】如果不加水印，必须直接保存文件
            file.transferTo(dest);
        }

        // 4. 保存文件记录到数据库
        SysUploadFile sysFile = new SysUploadFile();
        sysFile.setFileId(IdUtils.simpleUUID());
        sysFile.setOriginalName(originalFilename);
        sysFile.setFilePath(datePath + "/" + fileName); // 存入相对路径
        String fileUrl = url + "/prod-api/profile/" + sysFile.getFilePath();
//        String fileUrl = url + "/profile/" + sysFile.getFilePath();
        sysFile.setUrl(fileUrl);
        sysFile.setFileSize(file.getSize());
        sysFile.setFileType(file.getContentType());
        sysFile.setFileExtension(fileExtension);
        sysFile.setUploadTime(new Date());
        sysFile.setStatus("1"); // 临时文件
        sysFile.setCreateTime(new Date());
        sysFile.setCreateBy(SecurityUtils.getUsername());
        sysUploadFileMapper.insertFile(sysFile);


        return sysFile; // <-- 直接返回 sysFile 对象
    }
    @Override
    public String uploadFile(byte[] fileBytes, String originalFilename, String contentType) throws Exception {
        // 1. 生成唯一文件名
        String fileExtension = getFileExtension(originalFilename);
        String fileName = UUID.randomUUID().toString() + "." + fileExtension;

        // 2. 创建日期目录
        String datePath = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String fullPath = uploadPath + File.separator + datePath;
        File directory = new File(fullPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 3. 保存文件
        File dest = new File(fullPath + File.separator + fileName);
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(fileBytes);
        }

        // 4. 保存文件记录到数据库
        SysUploadFile sysFile = new SysUploadFile();
        sysFile.setFileId(IdUtils.simpleUUID());
        sysFile.setOriginalName(originalFilename);
        sysFile.setFilePath(datePath + "/" + fileName);
        sysFile.setFileSize((long) fileBytes.length);
        sysFile.setFileType(contentType);
        sysFile.setFileExtension(fileExtension);
        sysFile.setUploadTime(new Date());
        sysFile.setStatus("1"); // 临时文件
        sysFile.setCreateTime(new Date());
        sysFile.setCreateBy(SecurityUtils.getUsername());
        sysUploadFileMapper.insertFile(sysFile);
        return sysFile.getFileId();
    }
    @Override
    public void downloadFile(String fileId, HttpServletResponse response) {
        try {
            SysUploadFile fileInfo = sysUploadFileMapper.selectFileById(fileId);
            if (fileInfo == null) {
                throw new RuntimeException("文件不存在");
            }

            File file = new File(uploadPath + File.separator + fileInfo.getFilePath());
            if (!file.exists()) {
                throw new RuntimeException("文件不存在");
            }
            // 设置正确的Content-Disposition头，包含原始文件名
            String encodedFileName = java.net.URLEncoder.encode(fileInfo.getOriginalName(), "UTF-8")
                    .replaceAll("\\+", "%20"); // 处理空格

            response.setContentType(fileInfo.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition"); // 允许前端访问这个头
            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("文件下载失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void previewFile(String fileId, HttpServletResponse response) {
        try {
            SysUploadFile fileInfo = sysUploadFileMapper.selectFileById(fileId);
            if (fileInfo == null) {
                throw new RuntimeException("文件不存在");
            }

            File file = new File(uploadPath + File.separator + fileInfo.getFilePath());
            if (!file.exists()) {
                throw new RuntimeException("文件不存在");
            }

            response.setContentType(fileInfo.getFileType());
            response.setHeader("Content-Disposition", "inline; filename=\"" +
                    java.net.URLEncoder.encode(fileInfo.getOriginalName(), "UTF-8") + "\"");

            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("文件预览失败: " + e.getMessage(), e);
        }
    }
    @Override
    public void previewExcel(String fileId, HttpServletResponse response) {
        try {
            SysUploadFile fileInfo = sysUploadFileMapper.selectFileById(fileId);
            if (fileInfo == null) {
                throw new RuntimeException("文件不存在");
            }

            File file = new File(uploadPath + File.separator + fileInfo.getFilePath());
            if (!file.exists()) {
                throw new RuntimeException("文件不存在");
            }

            // 检查文件类型是否为Excel
            String fileExtension = fileInfo.getFileExtension().toLowerCase();
            if (!"xls".equals(fileExtension) && !"xlsx".equals(fileExtension)) {
                throw new RuntimeException("文件不是Excel格式，无法预览");
            }

            // 设置响应类型为HTML
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8"); // 添加字符编码

            // 将Excel转换为HTML
            String htmlContent = convertExcelToHtml(file);
            OutputStream os = response.getOutputStream();
            os.write(htmlContent.getBytes("UTF-8"));
            os.flush();

        } catch (Exception e) {
            throw new RuntimeException("Excel预览失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将Excel文件转换为HTML表格
     */
    private String convertExcelToHtml(File excelFile) throws Exception {
        StringBuilder htmlBuilder = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = WorkbookFactory.create(fis)) {

            htmlBuilder.append("<!DOCTYPE html>")
                    .append("<html>")
                    .append("<head>")
                    .append("<meta charset=\"UTF-8\">")
                    .append("<title>Excel预览</title>")
                    .append("<style>")
                    .append("body { font-family: Arial, sans-serif; margin: 20px; }")
                    .append("table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }")
                    .append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }")
                    .append("th { background-color: #f2f2f2; font-weight: bold; }")
                    .append("tr:nth-child(even) { background-color: #f9f9f9; }")
                    .append(".sheet-title { font-size: 18px; font-weight: bold; margin: 20px 0 10px 0; color: #333; }")
                    .append("</style>")
                    .append("</head>")
                    .append("<body>");

            // 遍历所有工作表
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();

                htmlBuilder.append("<table>");

                // 处理表头
                Row headerRow = sheet.getRow(0);
                if (headerRow != null) {
                    htmlBuilder.append("<thead><tr>");
                    for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                        Cell cell = headerRow.getCell(j);
                        String cellValue = getCellValueAsString(cell);
                        htmlBuilder.append("<th>").append(escapeHtml(cellValue)).append("</th>");
                    }
                    htmlBuilder.append("</tr></thead>");
                }

                // 处理数据行
                htmlBuilder.append("<tbody>");
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    if (row != null) {
                        htmlBuilder.append("<tr>");
                        for (int colNum = 0; colNum < row.getLastCellNum(); colNum++) {
                            Cell cell = row.getCell(colNum);
                            String cellValue = getCellValueAsString(cell);
                            htmlBuilder.append("<td>").append(escapeHtml(cellValue)).append("</td>");
                        }
                        htmlBuilder.append("</tr>");
                    }
                }
                htmlBuilder.append("</tbody></table>");
            }

            htmlBuilder.append("</body></html>");
        }

        return htmlBuilder.toString();
    }

    /**
     * 获取单元格值的字符串表示
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // 处理数字格式，避免显示过多的浮点数
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == Math.floor(numericValue)) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    try {
                        return String.valueOf(cell.getNumericCellValue());
                    } catch (Exception ex) {
                        return cell.getCellFormula();
                    }
                }
            default:
                return "";
        }
    }

    /**
     * HTML转义，防止XSS攻击
     */
    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;")
                .replace("/", "&#x2F;")
                .replace("\n", "<br>");
    }
    @Override
    public int deleteFile(String fileId) {
        try {
            SysUploadFile fileInfo = sysUploadFileMapper.selectFileById(fileId);
            if (fileInfo != null) {
                // 删除物理文件
                File file = new File(uploadPath + File.separator + fileInfo.getFilePath());
                if (file.exists()) {
                    file.delete();
                }
                // 删除数据库记录
                return sysUploadFileMapper.deleteFileById(fileId);
            }
            return 0;
        } catch (Exception e) {
            throw new RuntimeException("文件删除失败: " + e.getMessage(), e);
        }
    }

    @Override
    public SysUploadFile getFileById(String fileId) {
        return sysUploadFileMapper.selectFileById(fileId);
    }

    @Override
    public int markFileAsPermanent(String fileId) {
        SysUploadFile fileInfo = sysUploadFileMapper.selectFileById(fileId);
        if (fileInfo != null) {
            fileInfo.setStatus("0"); // 标记为正式文件
            return sysUploadFileMapper.updateFileStatus(fileInfo);
        }
        return 0;
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * 获取当前用户名（根据你的项目实际情况实现）
     */
    private String getCurrentUsername() {
        // 这里需要根据你的安全框架实现，比如Spring Security或若依的SecurityUtils
        // 示例：return SecurityUtils.getUsername();
        return "system"; // 临时返回固定值
    }

    /**
     * 上传 Base64 编码的文件
     *
     * @param base64Data Base64 字符串
     * @param originalFilename 原始文件名
     * @return 包含 FileId 和 Url 的文件信息
     * @throws Exception
     */
    @Override
    public SysUploadFile uploadBase64File(String base64Data, String originalFilename,String urlPrefix) throws Exception {

        // 0. 清理 Base64 头部 (如果有 data:image/png;base64, )
        String cleanBase64Data = base64Data;
        if (base64Data.contains(",")) {
            cleanBase64Data = base64Data.substring(base64Data.indexOf(",") + 1);
        }

        // 1. 解码
        byte[] fileBytes = Base64.getDecoder().decode(cleanBase64Data);

        // 2. 生成唯一文件名
        String fileExtension = getFileExtension(originalFilename);
        if (StringUtils.isEmpty(fileExtension)) {
            // 如果没有扩展名，给一个默认的（例如 .png 或 .dat）
            fileExtension = "png";
        }
        String fileName = UUID.randomUUID().toString() + "." + fileExtension;

        // 3. 创建日期目录
        String datePath = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String fullPath = uploadPath + File.separator + datePath;
        File directory = new File(fullPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 4. 保存文件
        File dest = new File(fullPath + File.separator + fileName);
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(fileBytes);
        }

        // 5. 保存文件记录到数据库
        SysUploadFile sysFile = new SysUploadFile();
        sysFile.setFileId(IdUtils.simpleUUID());
        sysFile.setOriginalName(originalFilename);
        sysFile.setFilePath(datePath + "/" + fileName); // 存入相对路径

        // 6. 构建访问 URL
        // 假设您的 application.yml 中配置了 server.servlet.context-path
        // 并且配置了 /profile/** 资源映射
        String fileUrl = urlPrefix + "/profile/" + sysFile.getFilePath();
        sysFile.setUrl(fileUrl);

        sysFile.setFileSize((long) fileBytes.length);

        // 7. 确定 ContentType (如果原始文件名有意义的话)
        if ("jpg".equalsIgnoreCase(fileExtension) || "jpeg".equalsIgnoreCase(fileExtension)) {
            sysFile.setFileType("image/jpeg");
        } else if ("png".equalsIgnoreCase(fileExtension)) {
            sysFile.setFileType("image/png");
        } else {
            sysFile.setFileType("application/octet-stream"); // 默认
        }

        sysFile.setFileExtension(fileExtension);
        sysFile.setUploadTime(new Date());
        sysFile.setStatus("1"); // 临时文件
        sysFile.setCreateTime(new Date());

        // [关键] 匿名调用，不能使用 SecurityUtils
        try {
            sysFile.setCreateBy(SecurityUtils.getUsername());
        } catch (Exception e) {
            sysFile.setCreateBy("EgapSystemApi"); // 硬编码创建者
        }

        sysUploadFileMapper.insertFile(sysFile);

        return sysFile; // <-- 返回完整的 SysUploadFile 对象
    }

    private void handleFileWithWatermark(MultipartFile file, File dest, String fileExtension) throws Exception {
        byte[] fileBytes;
        try (InputStream originalIs = file.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // 1. 优化流复制：增大 buffer 减少 IO 次数，确保读取完整
            byte[] buffer = new byte[8192]; // 原4096→8192，提升大文件读取稳定性
            int len;
            while ((len = originalIs.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush(); // 强制刷写缓冲区，避免数据残留
            fileBytes = baos.toByteArray();

            // 2. 关键校验：避免空流或残缺流（字节数组长度为0或过小）
            if (fileBytes == null || fileBytes.length == 0) {
                throw new IOException("文件流读取失败：字节数组为空，可能是文件上传中断");
            }
            // 校验图片最小尺寸（避免极小的无效文件）
            if (ImageWatermarkUtils.isSupportImageFormat(fileExtension) && fileBytes.length < 1024) { // 小于1KB的图片大概率无效
                throw new IOException("图片文件过小（<1KB），可能是损坏或无效文件");
            }
        }

        // 3. 重置 ByteArrayInputStream 指针（确保从开头读取）
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
             OutputStream outputStream = new FileOutputStream(dest)) {

            // 强制重置流指针到开头（防止意外移动）
            inputStream.mark(0);
            inputStream.reset();

            if (ImageWatermarkUtils.isSupportImageFormat(fileExtension)) {
                String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                Long userId = SecurityUtils.getUserId();
                if (userId == null){
                    throw new IllegalArgumentException("添加水印失败:获取用户信息异常");
                }
                SysUser sysUser = sysUserMapper.selectUserById(userId);
                if (sysUser == null){
                    throw new IllegalArgumentException("添加水印失败:获取用户信息异常");
                }
                String watermarkText = String.format("%s %s", dateTime, sysUser.getNickName());

                // 调用工具类（传入完整的字节数组流）
                // 调用工具类（自动触发动态字体+红色清晰样式）
                ImageWatermarkUtils.addBottomLeftWatermark(inputStream, outputStream, watermarkText, fileExtension);
            } else {
                outputStream.write(fileBytes);
                outputStream.flush();
            }
        }
    }

    /**
     * 原生流复制（工具类也可抽走，这里保持原有逻辑）
     */
    private void copyInputStreamToOutputStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        int len;
        while ((len = input.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }
        output.flush();
    }

}
