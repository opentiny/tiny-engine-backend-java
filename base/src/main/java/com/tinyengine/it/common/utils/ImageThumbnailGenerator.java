/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.common.utils;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 智能图片缩略图生成工具类
 * 支持自动识别输入格式，输出保持相同格式
 */
public class ImageThumbnailGenerator {

    // 支持的输出格式
    public static final String FORMAT_JPG = "jpg";
    public static final String FORMAT_JPEG = "jpeg";
    public static final String FORMAT_PNG = "png";
    public static final String FORMAT_GIF = "gif";
    public static final String FORMAT_BMP = "bmp";
    public static final String FORMAT_SVG = "svg";

    // 数据URI模式匹配
    private static final Pattern DATA_URI_PATTERN =
            Pattern.compile("^data:image/([a-zA-Z+]+);base64,(.*)$", Pattern.CASE_INSENSITIVE);

    private static final Map<String, String> MIME_TYPES = new HashMap<>();
    private static final Map<String, String> EXTENSION_TO_MIME = new HashMap<>();

    static {
        // MIME类型映射
        MIME_TYPES.put(FORMAT_JPG, "image/jpeg");
        MIME_TYPES.put(FORMAT_JPEG, "image/jpeg");
        MIME_TYPES.put(FORMAT_PNG, "image/png");
        MIME_TYPES.put(FORMAT_GIF, "image/gif");
        MIME_TYPES.put(FORMAT_BMP, "image/bmp");
        MIME_TYPES.put(FORMAT_SVG, "image/svg+xml");

        // 扩展名到MIME的映射
        EXTENSION_TO_MIME.put("jpg", "image/jpeg");
        EXTENSION_TO_MIME.put("jpeg", "image/jpeg");
        EXTENSION_TO_MIME.put("png", "image/png");
        EXTENSION_TO_MIME.put("gif", "image/gif");
        EXTENSION_TO_MIME.put("bmp", "image/bmp");
        EXTENSION_TO_MIME.put("svg", "image/svg+xml");
    }

    /**
     * 生成缩略图（自动保持输入格式）
     */
    public static String createThumbnail(String imageInput, int maxWidth, int maxHeight) {
        validateParameters(maxWidth, maxHeight);

        try {
            ImageInputInfo inputInfo = parseImageInput(imageInput);

            String thumbnailBase64;
            if (FORMAT_SVG.equalsIgnoreCase(inputInfo.getFormat())) {
                thumbnailBase64 = createSVGThumbnail(inputInfo.getCleanBase64(), maxWidth, maxHeight, inputInfo.getFormat());
            } else {
                thumbnailBase64 = createRasterThumbnail(inputInfo.getCleanBase64(), maxWidth, maxHeight, inputInfo.getFormat());
            }

            // 如果输入是数据URI格式，输出也保持数据URI格式
            if (inputInfo.isDataUri()) {
                return createDataUri(thumbnailBase64, inputInfo.getFormat());
            } else {
                return thumbnailBase64;
            }

        } catch (Exception e) {
            throw new RuntimeException("生成缩略图失败: " + e.getMessage(), e);
        }
    }

    /**
     * 生成缩略图（指定输出格式）
     */
    public static String createThumbnail(String imageInput, int maxWidth, int maxHeight, String outputFormat) {
        validateParameters(maxWidth, maxHeight, outputFormat);

        try {
            ImageInputInfo inputInfo = parseImageInput(imageInput);

            String thumbnailBase64;
            if (FORMAT_SVG.equalsIgnoreCase(inputInfo.getFormat()) && !FORMAT_SVG.equalsIgnoreCase(outputFormat)) {
                // SVG转位图
                thumbnailBase64 = createSVGThumbnail(inputInfo.getCleanBase64(), maxWidth, maxHeight, outputFormat);
            } else if (FORMAT_SVG.equalsIgnoreCase(outputFormat)) {
                // 位图转SVG（不支持，使用PNG代替）
                thumbnailBase64 = createRasterThumbnail(inputInfo.getCleanBase64(), maxWidth, maxHeight, FORMAT_PNG);
            } else {
                // 位图到位图
                thumbnailBase64 = createRasterThumbnail(inputInfo.getCleanBase64(), maxWidth, maxHeight, outputFormat);
            }

            // 如果输入是数据URI格式，输出也保持数据URI格式
            if (inputInfo.isDataUri()) {
                return createDataUri(thumbnailBase64, outputFormat);
            } else {
                return thumbnailBase64;
            }

        } catch (Exception e) {
            throw new RuntimeException("生成缩略图失败: " + e.getMessage(), e);
        }
    }

    /**
     * 解析图片输入信息
     */
    private static ImageInputInfo parseImageInput(String imageInput) {
        if (imageInput == null || imageInput.trim().isEmpty()) {
            throw new IllegalArgumentException("图片输入不能为空");
        }

        String trimmed = imageInput.trim();

        // 检查是否为数据URI格式
        Matcher matcher = DATA_URI_PATTERN.matcher(trimmed);
        if (matcher.matches()) {
            String mimeType = matcher.group(1).toLowerCase();
            String cleanBase64 = matcher.group(2);
            String format = mimeTypeToFormat(mimeType);

            return new ImageInputInfo(cleanBase64, format, true, mimeType);
        }

        // 如果不是数据URI，尝试检测格式
        String cleanBase64 = trimmed;
        String detectedFormat = detectFormatFromBase64(trimmed);

        return new ImageInputInfo(cleanBase64, detectedFormat, false, null);
    }

    /**
     * 从Base64检测图片格式
     */
    public static String detectFormatFromBase64(String base64Input) {
        try {
            String cleanBase64 = extractCleanBase64(base64Input);
            byte[] imageBytes = Base64.getDecoder().decode(cleanBase64);

            // 先检查是否为SVG
            String content = new String(imageBytes, 0, Math.min(1000, imageBytes.length)).trim().toLowerCase();
            if (content.contains("<svg") || (content.startsWith("<?xml") && content.contains("<svg"))) {
                return FORMAT_SVG;
            }

            // 检查位图格式
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (image != null) {
                // 根据图片特性判断格式
                if (image.getColorModel().hasAlpha()) {
                    return FORMAT_PNG;
                }
                return FORMAT_JPG;
            }
        } catch (Exception e) {
            // 忽略异常，使用默认格式
        }

        return FORMAT_JPG; // 默认格式
    }

    /**
     * 获取Base64数据的MIME类型
     */

    /**
     * 提取纯净的Base64字符串
     */
    public static String extractCleanBase64(String input) {
        if (input == null) return "";

        String trimmed = input.trim();
        Matcher matcher = DATA_URI_PATTERN.matcher(trimmed);
        if (matcher.matches()) {
            return matcher.group(2);
        }

        if (trimmed.contains(",")) {
            String[] parts = trimmed.split(",", 2);
            if (parts.length == 2) {
                return parts[1];
            }
        }

        return trimmed;
    }

    /**
     * 从 Base64 数据中提取 MIME 类型
     * @param base64Data 完整的 Base64 数据（包含 data:image/png;base64, 前缀）
     * @return 提取到的 MIME 类型，如 "image/png", "image/svg+xml"
     */
    public static String extractContentType(String base64Data) {
        if (base64Data == null || !base64Data.startsWith("data:")) {
            throw new IllegalArgumentException("Invalid Base64 data format");
        }

        Pattern pattern = Pattern.compile("^data:([^;]+);");
        Matcher matcher = pattern.matcher(base64Data);

        if (matcher.find()) {
            return matcher.group(1);
        }

        throw new IllegalArgumentException("Cannot extract content type from Base64 data");
    }

    /**
     * MIME类型转格式
     */
    private static String mimeTypeToFormat(String mimeType) {
        if (mimeType == null) return FORMAT_JPG;

        switch (mimeType.toLowerCase()) {
            case "jpeg":
            case "jpg":
                return FORMAT_JPG;
            case "png":
                return FORMAT_PNG;
            case "gif":
                return FORMAT_GIF;
            case "bmp":
                return FORMAT_BMP;
            case "svg":
            case "svg+xml":
                return FORMAT_SVG;
            default:
                return FORMAT_JPG;
        }
    }

    /**
     * 格式转MIME类型
     */
    private static String formatToMimeType(String format) {
        return MIME_TYPES.getOrDefault(format.toLowerCase(), "image/jpeg");
    }

    /**
     * 创建数据URI
     */
    private static String createDataUri(String base64Content, String format) {
        String mimeType = formatToMimeType(format);
        return "data:" + mimeType + ";base64," + base64Content;
    }

    /**
     * 处理SVG格式缩略图
     */
    private static String createSVGThumbnail(String base64SVG, int maxWidth, int maxHeight, String format) {
        try {
            byte[] svgBytes = Base64.getDecoder().decode(base64SVG);

            // 如果是SVG输出SVG格式，直接返回原内容（不进行缩放）
            if (FORMAT_SVG.equalsIgnoreCase(format)) {
                return base64SVG;
            }

            // 配置Batik转换器
            ImageTranscoder transcoder = createTranscoder(format);
            transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, (float) maxWidth);
            transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, (float) maxHeight);

            // 创建SVG文档
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
            Document document = factory.createDocument(null, new ByteArrayInputStream(svgBytes));

            TranscoderInput input = new TranscoderInput(document);

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                TranscoderOutput output = new TranscoderOutput(baos);
                transcoder.transcode(input, output);

                byte[] thumbnailBytes = baos.toByteArray();
                return Base64.getEncoder().encodeToString(thumbnailBytes);
            }

        } catch (Exception e) {
            throw new RuntimeException("SVG缩略图生成失败: " + e.getMessage(), e);
        }
    }

    /**
     * 创建合适的图片转换器
     */
    private static ImageTranscoder createTranscoder(String format) {
        switch (format.toLowerCase()) {
            case FORMAT_JPG:
            case FORMAT_JPEG:
                JPEGTranscoder jpegTranscoder = new JPEGTranscoder();
                jpegTranscoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, 0.9f);
                return jpegTranscoder;

            case FORMAT_PNG:
            default:
                PNGTranscoder pngTranscoder = new PNGTranscoder();
                return pngTranscoder;
        }
    }

    /**
     * 处理位图格式缩略图
     */
    private static String createRasterThumbnail(String base64Image, int maxWidth, int maxHeight, String format) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(base64Image))) {
            BufferedImage originalImage = ImageIO.read(bais);

            if (originalImage == null) {
                throw new IllegalArgumentException("无法读取图片数据，可能是不支持的格式或损坏的图片");
            }

            int[] newDimensions = calculateDimensions(
                    originalImage.getWidth(),
                    originalImage.getHeight(),
                    maxWidth,
                    maxHeight
            );

            BufferedImage thumbnail = createHighQualityThumbnail(originalImage, newDimensions[0], newDimensions[1]);
            return encodeImageToBase64(thumbnail, format);

        } catch (Exception e) {
            throw new RuntimeException("位图缩略图生成失败: " + e.getMessage(), e);
        }
    }

    /**
     * 计算保持比例的尺寸
     */
    private static int[] calculateDimensions(int originalWidth, int originalHeight, int maxWidth, int maxHeight) {
        if (originalWidth <= 0 || originalHeight <= 0) {
            return new int[]{maxWidth, maxHeight};
        }

        if (originalWidth <= maxWidth && originalHeight <= maxHeight) {
            return new int[]{originalWidth, originalHeight};
        }

        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double ratio = Math.min(widthRatio, heightRatio);

        int newWidth = Math.max(1, (int) (originalWidth * ratio));
        int newHeight = Math.max(1, (int) (originalHeight * ratio));

        return new int[]{newWidth, newHeight};
    }

    /**
     * 创建高质量缩略图
     */
    private static BufferedImage createHighQualityThumbnail(BufferedImage originalImage, int width, int height) {
        int imageType = originalImage.getColorModel().hasAlpha() ?
                BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;

        BufferedImage thumbnail = new BufferedImage(width, height, imageType);
        Graphics2D g = thumbnail.createGraphics();

        // 高质量渲染设置
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        // 绘制缩略图
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return thumbnail;
    }

    /**
     * 编码图片为Base64
     */
    private static String encodeImageToBase64(BufferedImage image, String format) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            boolean success = ImageIO.write(image, format, baos);
            if (!success) {
                // 格式回退机制
                String[] fallbackFormats = {FORMAT_PNG, FORMAT_JPG, FORMAT_BMP, FORMAT_GIF};
                for (String fallback : fallbackFormats) {
                    if (ImageIO.write(image, fallback, baos)) {
                        break;
                    }
                }
            }

            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);

        } catch (Exception e) {
            throw new RuntimeException("编码图片失败: " + e.getMessage(), e);
        }
    }

    /**
     * 验证参数有效性
     */
    private static void validateParameters(int maxWidth, int maxHeight) {
        if (maxWidth <= 0 || maxHeight <= 0) {
            throw new IllegalArgumentException("宽度和高度必须大于0");
        }
    }

    private static void validateParameters(int maxWidth, int maxHeight, String format) {
        validateParameters(maxWidth, maxHeight);

        if (format == null || !MIME_TYPES.containsKey(format.toLowerCase())) {
            throw new IllegalArgumentException("不支持的输出格式: " + format +
                    "，支持格式: " + MIME_TYPES.keySet());
        }
    }
}