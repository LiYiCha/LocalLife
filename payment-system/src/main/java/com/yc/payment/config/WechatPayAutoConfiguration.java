package com.yc.payment.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.refund.RefundService;
import com.yc.payment.service.impl.WechatNotifyServiceImpl;
import com.yc.payment.service.WechatNotifyService;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@ConditionalOnProperty(name = "wechat.enabled", havingValue = "true", matchIfMissing = false)
@Configuration
public class WechatPayAutoConfiguration {

    @Bean
    public Config wechatPayConfig(WeChatPayConfig payConfig) {
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(payConfig.getMchId())
                .privateKeyFromPath(payConfig.getPrivateKeyPath())
                .merchantSerialNumber(payConfig.getCertSerialNo())
                .apiV3Key(payConfig.getApiV3Key())
                .build();
    }
    //        //使用本地的微信支付平台证书
//        Config config =
//                new RSAConfig.Builder()
//                        .merchantId(wechatPayConfig.getMchId())
//                        .privateKeyFromPath(wechatPayConfig.getPrivateKeyPath())
//                        .merchantSerialNumber(wechatPayConfig.getCertSerialNo())
//                        .wechatPayCertificatesFromPath(wechatPayCertificatePath)
//                        .build();
    @Bean
    public HttpClient wechatHttpClient(Config wechatPayConfig) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        return new DefaultHttpClientBuilder()
                .config(wechatPayConfig)
                .okHttpClient(client)
                .build();
    }

    /**
     * 微信公众号服务 JsapiService Bean
     */
    @Bean
    public JsapiService jsapiService(HttpClient httpClient) {
        return new JsapiService.Builder().httpClient(httpClient).build();
    }
    /**
     * 微信退款服务 RefundService Bean
     */
    @Bean
    public RefundService refundService(HttpClient httpClient) {
        return new RefundService.Builder().httpClient(httpClient).build();
    }
    @Bean
    public RSAAutoCertificateConfig rsaAutoCertificateConfig(WeChatPayConfig payConfig) {
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(payConfig.getMchId())
                .privateKeyFromPath(payConfig.getPrivateKeyPath())
                .merchantSerialNumber(payConfig.getCertSerialNo())
                .apiV3Key(payConfig.getApiV3Key())
                .build();
    }
    /**
     * 微信回调通知解析器 NotificationParser Bean
     */
    @Bean
    public NotificationParser notificationParser(RSAAutoCertificateConfig rsaaAutoCertConfig) {
        return new NotificationParser(rsaaAutoCertConfig);
    }


    /**
     * 微信回调服务 WechatNotifyService Bean
     */
    @Bean
    public WechatNotifyService wechatNotifyService(NotificationParser notificationParser) {
        return new WechatNotifyServiceImpl(notificationParser);
    }


}
