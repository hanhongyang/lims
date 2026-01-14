package com.gmlimsqi.business.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件工具类：将本地文件转换为MultipartFile
 */
public class FileConvertUtils {

    /**
     * 根据文件路径获取文件并转换为MultipartFile
     *
     * @param absolutePath 文件绝对路径（例如：D:/test/image.png 或 /home/user/docs/report.pdf）
     * @return 转换后的MultipartFile对象
     * @throws IOException 当文件不存在、无法读取或转换失败时抛出
     */
    public static MultipartFile getMultipartFileByAbsolutePath(String profile,String absolutePath) throws IOException {
        // 1. 参数校验：路径不能为空
        if (absolutePath == null || absolutePath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        String s = absolutePath.replaceFirst("/profile", "");

        String realAbsolutePath = profile + s;
// 4. 此时创建File对象就能找到文件
        File file = new File(realAbsolutePath);
        if (file.exists()) {
            System.out.println("文件存在，可正常操作");
        } else {
            System.out.println("文件仍不存在，检查根目录或拼接是否错误");
        }

        // 3. 获取文件名（含后缀）
        String fileName = file.getName();

        // 4. 自动探测文件MIME类型（如text/plain、image/png等，若无法探测则为null）
        String contentType = Files.probeContentType(Paths.get(absolutePath));

        // 5. 通过输入流读取文件（适合大文件，避免一次性加载字节数组占用内存）
        try (FileInputStream inputStream = new FileInputStream(file)) {
            // 构造MultipartFile（表单字段名默认用"file"，可根据实际业务修改）
            return new MockMultipartFile(
                    "file",       // 表单字段名（对应前端<input name="file">）
                    fileName,     // 原始文件名
                    contentType,  // 文件MIME类型
                    inputStream   // 文件输入流（try-with-resources确保自动关闭）
            );
        }
    }

}