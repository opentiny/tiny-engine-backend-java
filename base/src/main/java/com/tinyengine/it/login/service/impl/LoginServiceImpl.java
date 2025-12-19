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

package com.tinyengine.it.login.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.login.service.LoginService;
import com.tinyengine.it.mapper.AuthUsersUnitsRolesMapper;
import com.tinyengine.it.mapper.UserMapper;
import com.tinyengine.it.model.entity.AuthUsersUnitsRoles;
import com.tinyengine.it.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;

import static com.tinyengine.it.login.utils.SM2EncryptionUtil.decrypt;
import static com.tinyengine.it.login.utils.SM2EncryptionUtil.encrypt;
import static com.tinyengine.it.login.utils.SM2EncryptionUtil.generateSM2KeyPair;
import static com.tinyengine.it.login.utils.SM2EncryptionUtil.getPrivateKeyFromBase64;
import static com.tinyengine.it.login.utils.SM2EncryptionUtil.getPublicKeyFromBase64;

@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {

    @Autowired
    AuthUsersUnitsRolesMapper authUsersUnitsRolesMapper;

    /**
     * 新增表t_user数据
     *
     * @param user user
     * @return execute success data number
     */
    @Override
    public User createUser(User user) throws Exception {
        User userParam = new User();
        userParam.setUsername(user.getUsername());
        List<User> users = baseMapper.queryUserByCondition(userParam);
        if (!users.isEmpty()) {
            throw new ServiceException(ExceptionEnum.CM003.getResultCode(),
                ExceptionEnum.CM003.getResultMsg());
        }
        KeyPair keyPair = generateSM2KeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String cipherText = encrypt(user.getSalt(), publicKey);
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        user.setSalt(cipherText);
        user.setPublicKey(publicKeyBase64);
        user.setPrivateKey(privateKeyBase64);
        baseMapper.createUser(user);
        User result = baseMapper.queryUserById(user.getId());
        result.setPrivateKey(null);

        AuthUsersUnitsRoles authUsersUnitsRoles = new AuthUsersUnitsRoles();
        authUsersUnitsRoles.setTenantId(1);
        authUsersUnitsRoles.setRoleId(2);
        authUsersUnitsRoles.setUnitType("tenant");
        authUsersUnitsRoles.setUnitId(1);
        authUsersUnitsRoles.setUserId(Integer.valueOf(user.getId()));
        authUsersUnitsRolesMapper.createAuthUsersUnitsRoles(authUsersUnitsRoles);
        return result;
    }

    /**
     * 忘记密码
     *
     * @param user the user
     * @return the user
     */
    @Override
    public Result forgotPassword(User user) throws Exception {
        User userParam = new User();
        userParam.setUsername(user.getUsername());
        List<User> users = baseMapper.queryUserByCondition(userParam);
        if (users.isEmpty()) {
            return Result.failed(ExceptionEnum.CM002);
        }
        User userResult = users.get(0);
        PublicKey publicKey = getPublicKeyFromBase64(user.getPublicKey());
        PrivateKey privateKey = getPrivateKeyFromBase64(userResult.getPrivateKey());
        // 验证publickey
        if (!validatorPublicKey(userResult.getSalt(), publicKey, privateKey)) {
            return Result.failed(ExceptionEnum.CM335);
        }
        String cipherText = encrypt(user.getSalt(), publicKey);
        user.setSalt(cipherText);
        user.setId(userResult.getId());
        baseMapper.updateUserById(user);
        User result = baseMapper.queryUserById(user.getId());
        result.setPrivateKey(null);
        if (result.getSalt() == null || result.getSalt().isEmpty()) {
            return Result.failed(ExceptionEnum.CM335);
        }
        return Result.success(ExceptionEnum.CM334.getResultCode(), ExceptionEnum.CM334.getResultMsg());
    }

    private boolean validatorPublicKey(String salt, PublicKey publicKey, PrivateKey privateKey) throws Exception {
        String plainSalt = decrypt(salt, privateKey);
        String cipherSalt = encrypt(plainSalt, publicKey);
        String decryptSalt = decrypt(cipherSalt, privateKey);
        if (plainSalt.equals(decryptSalt)) {
            return true;
        }
        return false;
    }
}
