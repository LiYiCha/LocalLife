package com.yc.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {
    // 基础配置
    private String appId;
    private String gatewayUrl;
    private String returnUrl;
    private String notifyUrl;
    private String signType;
    private String charset;

    // 密钥模式配置
    private String merchantPrivateKey;     //  商户私钥
    private String alipayPublicKey;        //  支付宝公钥

    // 加密相关
    private String encryptKey;

    // 证书模式配置
    private String merchantCertPath;      // 应用公钥证书路径
    private String alipayCertPath;       // 支付宝公钥证书路径
    private String alipayRootCertPath;   // 支付宝根证书路径

    // 模式切换
    private boolean isCertMode = false;    // 默认使用普通模式

    // 高性能配置
    private int maxConnTotal = 100;      // 最大连接数
    private int maxConnPerRoute = 50;    // 每路由最大连接数
    private int connTimeout = 3000;      // 连接超时(ms)
    private int readTimeout = 5000;      // 读取超时(ms)
}
