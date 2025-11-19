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

package com.tinyengine.it.login.service;

import com.tinyengine.it.login.config.PasswordPolicyConfig;
import com.tinyengine.it.login.model.PasswordValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * 密码验证
 */
@Service
public class ConfigurablePasswordValidator {

    @Autowired
    private PasswordPolicyConfig passwordPolicy;

    public PasswordValidationResult validateWithPolicy(String password) {
        PasswordValidationResult result = new PasswordValidationResult();

        if (password == null || password.trim().isEmpty()) {
            result.setValid(false);
            result.addError("密码不能为空");
            return result;
        }

        // 长度检查
        if (password.length() < passwordPolicy.getMinLength()) {
            result.setValid(false);
            result.addError(String.format("密码长度至少%d位", passwordPolicy.getMinLength()));
        }

        if (password.length() > passwordPolicy.getMaxLength()) {
            result.setValid(false);
            result.addError(String.format("密码长度不能超过%d位", passwordPolicy.getMaxLength()));
        }

        // 字符类型检查
        if (passwordPolicy.isRequireLowerCase() &&
                !Pattern.compile("[a-z]").matcher(password).find()) {
            result.setValid(false);
            result.addError("密码必须包含小写字母");
        }

        if (passwordPolicy.isRequireUpperCase() &&
                !Pattern.compile("[A-Z]").matcher(password).find()) {
            result.setValid(false);
            result.addError("密码必须包含大写字母");
        }

        if (passwordPolicy.isRequireDigit() &&
                !Pattern.compile("[0-9]").matcher(password).find()) {
            result.setValid(false);
            result.addError("密码必须包含数字");
        }

        if (passwordPolicy.isRequireSpecialChar()) {
            String allowedSpecialChars = passwordPolicy.getAllowedSpecialChars();
            boolean hasAllowedSpecial = password.chars().anyMatch(ch -> allowedSpecialChars.indexOf(ch) >= 0);
            if (!hasAllowedSpecial) {
                result.setValid(false);
                result.addError("密码必须包含特殊字符: " + passwordPolicy.getAllowedSpecialChars());
            }
        }

        // 其他安全检查
        if (passwordPolicy.isCheckConsecutiveChars() &&
                Pattern.compile("(.)\\1{2,}").matcher(password).find()) {
            result.setValid(false);
            result.addError("密码不能包含3个及以上连续相同字符");
        }

        if (result.getErrors().isEmpty()) {
            result.setValid(true);
        }

        return result;
    }
}
