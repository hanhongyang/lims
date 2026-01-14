package com.gmlimsqi.common.utils.file;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Base64;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gmlimsqi.common.config.RuoYiConfig;
import com.gmlimsqi.common.constant.Constants;
import com.gmlimsqi.common.utils.StringUtils;

/**
 * 图片处理工具类
 *
 * @author ruoyi
 */
public class ImageUtils
{
    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    public static byte[] getImage(String imagePath)
    {
        InputStream is = getFile(imagePath);
        try
        {
            return IOUtils.toByteArray(is);
        }
        catch (Exception e)
        {
            log.error("图片加载异常 {}", e);
            return null;
        }
        finally
        {
            IOUtils.closeQuietly(is);
        }
    }

    public static InputStream getFile(String imagePath)
    {
        try
        {
            byte[] result = readFile(imagePath);
            result = Arrays.copyOf(result, result.length);
            return new ByteArrayInputStream(result);
        }
        catch (Exception e)
        {
            log.error("获取图片异常 {}", e);
        }
        return null;
    }

    /**
     * 读取文件为字节数据
     * 
     * @param url 地址
     * @return 字节数据
     */
    public static byte[] readFile(String url)
    {
        InputStream in = null;
        try
        {
            if (url.startsWith("http"))
            {
                // 网络地址
                URL urlObj = new URL(url);
                URLConnection urlConnection = urlObj.openConnection();
                urlConnection.setConnectTimeout(30 * 1000);
                urlConnection.setReadTimeout(60 * 1000);
                urlConnection.setDoInput(true);
                in = urlConnection.getInputStream();
            }
            else
            {
                // 本机地址
                String localPath = RuoYiConfig.getProfile();
//                String downloadPath = localPath + StringUtils.substringAfter(url, Constants.RESOURCE_PREFIX);
                String downloadPath = localPath + "/" +url;
                in = new FileInputStream(downloadPath);
            }
            return IOUtils.toByteArray(in);
        }
        catch (Exception e)
        {
            log.error("获取文件路径异常 {}", e);
            return null;
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }

    public static String imageUrlToBase64(String imageUrl) throws IOException {

        // 获取图片字节数组
        byte[] imageBytes = readFile(imageUrl);
        if (imageBytes == null || imageBytes.length == 0) {
            log.warn("无法将图片转换为Base64，读取到的字节数组为空: {}", imageUrl);
            return null;
        }
        // 获取图片格式（如jpeg、png，用于Base64前缀）
        String imageFormat = getImageFormat(imageUrl);
        // 字节数组转Base64，拼接格式前缀
        return "data:image/" + imageFormat + ";base64," + Base64.getEncoder().encodeToString(imageBytes);

    }

    /**
     * 辅助方法：InputStream转为字节数组
     */
    private static byte[] inputStreamToBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        return outputStream.toByteArray();
    }

    /**
     * 辅助方法：从URL或文件路径中提取图片格式（如jpeg、png、gif）
     */
    private static String getImageFormat(String pathOrUrl) {
        // 从路径/URL的最后一个"."后提取后缀
        int lastDotIndex = pathOrUrl.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == pathOrUrl.length() - 1) {
            return "jpeg"; // 默认格式
        }
        String suffix = pathOrUrl.substring(lastDotIndex + 1).toLowerCase();
        // 支持常见图片格式
        if (suffix.matches("jpg|jpeg|png|gif|bmp|webp")) {
            return suffix.equals("jpg") ? "jpeg" : suffix; // jpg统一转为jpeg（Base64标准格式）
        }
        return "jpeg"; // 不识别的格式默认按jpeg处理
    }


}
