package com.yc.payment.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 文件名: AlipayCline
 * 创建者: @一茶
 * 创建时间:2025/6/8 16:47
 * 描述：
 */
@Configuration
public class AlipayCline {
    @Resource
    private AliPayConfig aliPayConfig;

    public AlipayClient createAlipayClient() throws AlipayApiException {
        if (aliPayConfig.isCertMode()) {
            // 证书模式
            CertAlipayRequest certRequest = new CertAlipayRequest();
            certRequest.setServerUrl(aliPayConfig.getGatewayUrl());
            certRequest.setAppId(aliPayConfig.getAppId());
            certRequest.setPrivateKey(aliPayConfig.getMerchantPrivateKey());
            certRequest.setCertPath(aliPayConfig.getMerchantCertPath());
            certRequest.setAlipayPublicCertPath(aliPayConfig.getAlipayCertPath());
            certRequest.setRootCertPath(aliPayConfig.getAlipayRootCertPath());
            certRequest.setSignType(aliPayConfig.getSignType());
            return new DefaultAlipayClient(certRequest);
        } else {
            // 普通模式
            return new DefaultAlipayClient(
                    aliPayConfig.getGatewayUrl(),
                    aliPayConfig.getAppId(),
                    aliPayConfig.getMerchantPrivateKey(),
                    "json",
                    "UTF-8",
                    aliPayConfig.getAlipayPublicKey(),
                    aliPayConfig.getSignType());
        }
    }
}
