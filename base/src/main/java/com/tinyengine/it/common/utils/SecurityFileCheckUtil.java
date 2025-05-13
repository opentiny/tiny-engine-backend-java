/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */

package com.tinyengine.it.common.utils;

import cn.hutool.core.util.ObjectUtil;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * The file check Utils.
 *
 * @since 2025-05-13
 */
public class SecurityFileCheckUtil {

    private static final String REGX_FILE_NAME = "^[a-z0-9A-Z][^\\\\/:*<>|]+$";
    private static final Pattern PATTERN_FILE_NAME = Pattern.compile(REGX_FILE_NAME);

    /**
     * Determine whether the file has cross path connections.
     *
     * @param dirOrFileName the dirOrFileName
     * @return true or false
     */
    public static boolean checkPathHasCrossDir(String dirOrFileName) {
        if (!dirOrFileName.contains("../") && !dirOrFileName.contains("/..")) {
            if (!dirOrFileName.contains("..\\") && !dirOrFileName.contains("\\..")) {
                return dirOrFileName.contains("./") || dirOrFileName.contains(".\\.\\") || dirOrFileName.contains("%00");
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * Type of inspection document.
     *
     * @param file the file
     * @param fileTypeMap the fileTypeMap
     * @return true or false
     */
    public static boolean checkFileType(MultipartFile file, Map<String, String> fileTypeMap) {
        if (Objects.isNull(file) || fileTypeMap.isEmpty()) {
            throw new ServiceException(ExceptionEnum.CM307.getResultCode(), ExceptionEnum.CM307.getResultMsg());
        }
        String originalFileName = file.getOriginalFilename();
        for (Map.Entry<String, String> entry : fileTypeMap.entrySet()) {
            if (originalFileName.endsWith(entry.getKey())) {
                return checkFileType(file, entry.getKey(), entry.getValue());
            }
        }
        return false;
    }

    /**
     * Type of inspection document.
     *
     * @param file the file
     * @param fileNameEnd the fileNameEnd
     * @param fileType the fileType
     * @return true or false
     */
    public static boolean checkFileType(MultipartFile file, String fileNameEnd, String fileType) {
        String originalFileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        if (ObjectUtil.isEmpty(originalFileName) || ObjectUtil.isEmpty(contentType)) {
            return false;
        }
        if (!originalFileName.endsWith(fileNameEnd)) {
            return false;
        }
        if (!contentType.equalsIgnoreCase(fileType)) {
            return false;
        }
        return true;
    }

    /**
     * Inspection file name.
     *
     * @param fileName the fileName
     */
    public static void validFileName(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            throw new ServiceException(ExceptionEnum.CM320.getResultCode(), ExceptionEnum.CM320.getResultMsg());
        }
        if (!checkFileNameLength(fileName, 1, 100)) {
            throw new ServiceException(ExceptionEnum.CM323.getResultCode(), ExceptionEnum.CM323.getResultMsg());
        }
        if (!filePathIsValid(fileName)) {
            throw new ServiceException(ExceptionEnum.CM324.getResultCode(), ExceptionEnum.CM324.getResultMsg());
        }
        String fullFileName = getFileName(fileName);
        if (!PATTERN_FILE_NAME.matcher(fullFileName).matches()) {
            throw new ServiceException(ExceptionEnum.CM324.getResultCode(), ExceptionEnum.CM324.getResultMsg());
        }
    }

    /**
     * Check if the file name length is within the specified range.
     *
     * @param fileName the fileName
     * @param min the min
     * @param max the max
     * @return true or false
     */
    public static boolean checkFileNameLength(String fileName, int min, int max) {
        if (!StringUtils.hasText(fileName)) {
            return min <= 0;
        }
        String temp = fileName.replaceAll("[^\\x00-\\xff]", "**");
        return temp.length() <= max;
    }

    /**
     * Verify file path.
     *
     * @param fileName the fileName
     * @return true or false
     */
    public static boolean filePathIsValid(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }

        // 获取当前操作系统的名称
        String os = System.getProperty("os.name").toLowerCase();

        // 定义通用的非法字符
        String illegalChars = "";

        if (os.contains("win")) {
            // 针对Windows的非法字符
            illegalChars = "[<>:\"/\\|?*]";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            // 针对Linux和macOS的非法字符（一般来说，Linux和macOS对文件名的限制较少，但有一些常见的非法字符）
            illegalChars = "[/]"; // Linux和macOS的路径不能包含斜杠 '/'
        }
        // 检查路径中是否包含非法字符
        if (fileName.matches(".*" + illegalChars + ".*")) {
            return false;
        }
        // 检查路径是否超过文件系统允许的最大长度（例如，Windows上的路径限制通常为260个字符）
        if (fileName.length() > 260) {
            return false;
        }

        // 检查路径中是否包含空格或其他特殊字符，视需要进行定制
        // 如果需要你也可以根据不同操作系统做不同的检查

        return true;
    }

    private static String getFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }


}
