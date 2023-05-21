package com.qy.tx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tencent.cos")
public class CosConfig {

    private String accessKey;

    private String secretKey;

    private String bucket;

    private String bucketName;

    private String host;
}
