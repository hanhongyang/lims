package com.gmlimsqi.common.utils.file;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Arrays; // 新增：用于数组转集合
import java.awt.BasicStroke; // 新增：描边需要的类（之前漏导入会报另一个错）

import javax.imageio.ImageIO;

/**
 * 图片水印工具类（支持左下角添加文本水印）
 * 特点：无状态、静态方法、配置灵活、支持主流图片格式
 */
public class ImageWatermarkUtils {
    // 终极配置：高清+高对比
    private static final String DEFAULT_FONT_NAME = "Microsoft YaHei"; // 微软雅黑
    private static final int DEFAULT_FONT_STYLE = Font.BOLD;
    private static final int DEFAULT_MARGIN = 25;
    private static final int TEXT_STROKE_WIDTH = 1; // 文字描边宽度
    // 红色字体（完全不透明）+ 黑色描边（半透明）
    private static final Color TEXT_COLOR = new Color(255, 0, 0, 255);
    private static final Color STROKE_COLOR = new Color(0, 0, 0, 180);
    // 动态字体：按图片高度6%计算，最小22px，最大55px
    private static final float FONT_SIZE_RATIO = 0.06f;
    private static final int MIN_FONT_SIZE = 22;
    private static final int MAX_FONT_SIZE = 55;
    // 支持的图片格式
    private static final List<String> SUPPORT_IMAGE_FORMATS = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "webp"
    );

    private ImageWatermarkUtils() {
        throw new UnsupportedOperationException("工具类不允许实例化");
    }

    /**
     * 重载方法：修复 fillString 错误，重构描边+文字绘制逻辑
     */
    public static void addBottomLeftWatermark(InputStream inputStream, OutputStream outputStream,
                                              String watermarkText, String fileFormat,
                                              String fontName, int fontSize, int fontStyle,
                                              Color watermarkColor, int margin) throws Exception {
        validateParams(inputStream, outputStream, watermarkText, fileFormat);

        if (inputStream.markSupported()) {
            inputStream.mark(inputStream.available());
        }

        BufferedImage image = ImageIO.read(inputStream);
        if (image == null) {
            if (inputStream.markSupported()) {
                inputStream.reset();
                image = ImageIO.read(inputStream);
            }
            if (image == null) {
                throw new IllegalArgumentException(
                        String.format("无法读取图片文件！文件格式：%s，流类型：%s，可能是格式不兼容或流不完整",
                                fileFormat, inputStream.getClass().getSimpleName())
                );
            }
        }

        Graphics2D g2d = image.createGraphics();
        try {
            // 极致抗锯齿配置（不变）
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

            // 动态字体计算（不变）
            int imageHeight = image.getHeight();
            int dynamicFontSize = (int) (imageHeight * FONT_SIZE_RATIO);
            dynamicFontSize = Math.max(MIN_FONT_SIZE, Math.min(MAX_FONT_SIZE, dynamicFontSize));
            String finalFontName = (fontName == null || fontName.isEmpty()) ? DEFAULT_FONT_NAME : fontName;
            int finalFontStyle = (fontStyle == -1) ? DEFAULT_FONT_STYLE : fontStyle;
            Font font;

            // 字体兼容性判断（已修正 contains 错误）
            String[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            if (Arrays.asList(availableFonts).contains(finalFontName)) {
                font = new Font(finalFontName, finalFontStyle, dynamicFontSize);
            } else {
                font = new Font(Font.SANS_SERIF, finalFontStyle, dynamicFontSize);
            }
            g2d.setFont(font);

            // 文本超宽自适应（不变）
            FontMetrics fontMetrics = g2d.getFontMetrics(font);
            int textWidth = fontMetrics.stringWidth(watermarkText);
            int textAscent = fontMetrics.getAscent();
            int maxAvailableWidth = image.getWidth() - 2 * margin;
            if (textWidth > maxAvailableWidth && maxAvailableWidth > 0) {
                float scaleRatio = (float) maxAvailableWidth / textWidth;
                AffineTransform transform = AffineTransform.getScaleInstance(scaleRatio, scaleRatio);
                font = font.deriveFont(transform);
                g2d.setFont(font);
                fontMetrics = g2d.getFontMetrics(font);
                textWidth = fontMetrics.stringWidth(watermarkText);
                textAscent = fontMetrics.getAscent();
            }

            // 计算位置（不变）
            int x = margin;
            int y = image.getHeight() - margin;

            // ====================== 核心修正：重构描边+文字绘制 ======================
            // 第一步：绘制黑色宽描边（2px）- 作为文字轮廓，解决模糊
            g2d.setColor(STROKE_COLOR);
            g2d.setStroke(new BasicStroke(TEXT_STROKE_WIDTH)); // 宽描边做轮廓
            g2d.drawString(watermarkText, x, y); // 画描边（轮廓）

            // 第二步：绘制红色文字（覆盖描边中间）- 红色更鲜艳，无模糊
            g2d.setColor(TEXT_COLOR);
            g2d.setStroke(new BasicStroke(1)); // 恢复细笔触，只画文字内容
            g2d.drawString(watermarkText, x, y); // 替换 fillString 为 drawString

        } finally {
            g2d.dispose();
        }

        boolean writeSuccess = ImageIO.write(image, fileFormat.toUpperCase(), outputStream);
        if (!writeSuccess) {
            throw new IllegalStateException("图片写入失败，不支持的图片格式：" + fileFormat);
        }
    }

    // 默认调用方法
    public static void addBottomLeftWatermark(InputStream inputStream, OutputStream outputStream,
                                              String watermarkText, String fileFormat) throws Exception {
        addBottomLeftWatermark(inputStream, outputStream, watermarkText, fileFormat,
                DEFAULT_FONT_NAME, -1, DEFAULT_FONT_STYLE,
                TEXT_COLOR, DEFAULT_MARGIN);
    }

    // 参数校验
    private static void validateParams(InputStream inputStream, OutputStream outputStream,
                                       String watermarkText, String fileFormat) {
        if (inputStream == null) {
            throw new IllegalArgumentException("图片输入流不能为空");
        }
        if (outputStream == null) {
            throw new IllegalArgumentException("图片输出流不能为空");
        }
        if (watermarkText == null || watermarkText.trim().isEmpty()) {
            throw new IllegalArgumentException("水印文本不能为空");
        }
        if (fileFormat == null || fileFormat.trim().isEmpty()) {
            throw new IllegalArgumentException("图片格式不能为空");
        }
        if (!isSupportImageFormat(fileFormat)) {
            throw new IllegalArgumentException("不支持的图片格式：" + fileFormat + "，支持格式：" + SUPPORT_IMAGE_FORMATS);
        }
    }

    // 判断是否为支持的图片格式
    public static boolean isSupportImageFormat(String fileExtension) {
        if (fileExtension == null) {
            return false;
        }
        return SUPPORT_IMAGE_FORMATS.contains(fileExtension.trim().toLowerCase());
    }
}