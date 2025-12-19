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

package com.tinyengine.it.login.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 密码检验配置
 */
@Configuration
@ConfigurationProperties(prefix = "security.password")
@Data
public class PasswordPolicyConfig {

    // 最小长度
    private int minLength = 8;

    // 最大长度
    private int maxLength = 20;

    // 是否要求小写字母
    private boolean requireLowerCase = true;

    // 是否要求大写字母
    private boolean requireUpperCase = true;

    // 是否要求数字
    private boolean requireDigit = true;

    // 是否要求特殊字符
    private boolean requireSpecialChar = true;

    // 允许的特殊字符
    private String allowedSpecialChars = "!@#$%^&*()_+-=[]{};':\"|,.<>?";

    // 是否检查连续字符
    private boolean checkConsecutiveChars = false;

    // 是否检查顺序字符
    private boolean checkSequentialChars = false;

    // 是否检查弱密码
    private boolean checkWeakPasswords = true;

}
