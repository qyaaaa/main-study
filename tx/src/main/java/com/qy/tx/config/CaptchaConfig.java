package com.qy.tx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tencent.captcha")
public class CaptchaConfig {

    private String accessKey;

    private String secretKey;

    private Long captchaAppId;

    private String appSecretKey;

    private String host;
}
