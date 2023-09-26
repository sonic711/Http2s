package com.example.http2s.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.core.io.Resource;

/**
 * ====================================================================== <br>
 * Licensed Materials - Property of BlueTechnology Corp., Ltd. <br>
 * 藍科數位科技股份有限公司版權所有翻印必究 <br>
 * (C) Copyright BlueTechnology Corp., Ltd. 2019 All Rights Reserved. <br>
 * 日期：2023/9/20<br>
 * 作者：Sean Liu<br>
 * 程式代號: ClientKeyStoreInfo<br>
 * 程式說明: <br>
 * ======================================================================
 */
@Getter
@ToString
public class ClientKeyStoreInfo {
    private Resource keyStoreFile;
    private String keyStorePwd;
    private String privateKeyPwd;

    public void setKeyStoreFile(Resource keyStoreFile) {
        if (this.keyStoreFile == null) {
            this.keyStoreFile = keyStoreFile;
        }
    }

    public void setKeyStorePwd(String keyStorePwd) {
        if (this.keyStorePwd == null) {
            this.keyStorePwd = keyStorePwd;
        }
    }

    public void setPrivateKeyPwd(String privateKeyPwd) {
        if (this.privateKeyPwd == null) {
            this.privateKeyPwd = privateKeyPwd;
        }
    }
}

