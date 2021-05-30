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

    public static final Setting<SecureString> ELASTIC_USER = SecureSetting.secureString("elastic.user", null);
    public static final Setting<SecureString> ELASTIC_PASS = SecureSetting.secureString("elastic.password", null);
    private String elasticUser;
    private String elasticPass;

    public AuthConfig(final Environment environment) {
        this.elasticUser = "elasticUser";
        this.elasticPass = "elasticPass";

        log.info("[ES-PLUGIN] Configuring with elastic info: {}:{}:{}", this.elasticUser, this.elasticPass);
    }

    public String getElasticUser() {
        return elasticUser;
    }

    public String getElasticPass() {
        return elasticPass;
    }
}
