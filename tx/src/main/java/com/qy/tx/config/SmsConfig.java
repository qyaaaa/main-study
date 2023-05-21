package com.qy.tx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tencent.sms")
public class SmsConfig {

    private String accessKey;

    private String secretKey;

    private String region;

    private String sdkAppid;

    private String templateId;

    private String signName;
}
