package com.dirk.authorization.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.settings.SecureSetting;
import org.elasticsearch.common.settings.SecureString;
import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.env.Environment;

/**
 * @ClassName AuthConfig
 * @Description 请描述类的业务用途
 * @Author yangsongbai
 * @Date 2021/5/23 下午3:27
 * @email yangsongbaivat@163.com
 * @Version 1.0
 **/
public class AuthConfig {
    protected final Logger log = LogManager.getLogger(AuthConfig.class);

    private Environment environment;

    public static final Setting<SecureString> ADMIN_USER = SecureSetting.secureString("es.admin_user", null);
    public static final Setting<SecureString> ADMIN_PASS = SecureSetting.secureString("es.admin_pass", null);
    public static final Setting<SecureString> AUTH_TOKEN = SecureSetting.secureString("es.auth_token", null);
    private String adminUser;
    private String adminPass;
    private String authToken;

    public AuthConfig(final Environment environment) {
        this.adminUser = "user";
        this.adminPass = "password";
        this.authToken = "token";

        log.info("[ES-PLUGIN] Configuring with admin info: {}:{}:{}", this.adminUser, this.adminPass, this.authToken);
    }

    public String getAdminUser() {
        return adminUser;
    }

    public String getAdminPass() {
        return adminPass;
    }

    public String getAuthToken() {
        return this.authToken;
    }
}
