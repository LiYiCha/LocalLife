package com.yc.payment.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

import java.util.Map;


public class AliPaySignUtil {
    public static boolean verify(Map<String, String> params, String publicKey, String charset, String signType) {
        try {
            return AlipaySignature.rsaCheckV1(params, publicKey, charset, signType);
        } catch (AlipayApiException e) {
            throw new RuntimeException("支付宝签名验证失败", e);
        }
    }
}
