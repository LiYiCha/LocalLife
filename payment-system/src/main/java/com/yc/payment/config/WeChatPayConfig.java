package com.yc.payment.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@ConditionalOnProperty(name = "wechat.enabled", havingValue = "true", matchIfMissing = false)
@Configuration
@ConfigurationProperties(prefix = "wechat")
public class WeChatPayConfig {
    // 基础配置
    private String appId;          // 公众号或小程序的AppID
    private String mchId;          // 商户号
    private String apiV3Key;       // APIv3密钥
    private String privateKeyPath; // 商户私钥文件路径
    private String certSerialNo;   // 商户证书序列号
    private String notifyUrl;      // 支付结果通知URL

    // 服务商模式配置
    private boolean serviceProviderMode = false; // 是否服务商模式
    private String subAppId;                    // 子商户appId
    private String subMchId;                    // 子商户mchId

    // 多商户支持
    private List<WeChatMerchantConfig> merchants = new ArrayList<>();

    // 高性能配置
    private int maxConnTotal = 100;
    private int maxConnPerRoute = 50;

    @Data
    public static class WeChatMerchantConfig {
        private String merchantId;      // 商户ID（自定义）
        private String appId;          // 应用ID
        private String mchId;          // 商户号
        private String apiV3Key;       // APIv3密钥
        private String privateKeyPath; // 私钥路径
        private String certSerialNo;   // 证书序列号

    }

}
