package com.core.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class TokenUtil {

    private static final long EXPIRE_TIME= 10*24*60*60*1000;  // 过期时间10天
    private static final String TOKEN_SECRET="authlifeyicha666";  //密钥盐
    private static final String ISSUER="auth0life";

    /**
     * 签名生成
     * @return
     */
    public static String sign(String username, String role){

        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer(ISSUER).withClaim("id","id")
                    .withClaim("username", username)
                    .withClaim("role", role) //角色
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e){
            e.printStackTrace();
        }
        return token;

    }
    /**
     * 生成 Refresh Token
     * @param username 用户名
     * @param role 角色
     * @return String refreshToken
     */
    public static String generateRefreshToken(String username, String role) {
        String token = null;
        try {
            // 设置 refresh token 过期时间为30天
            Date expiresAt = new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000);
            token = JWT.create()
                    .withIssuer(ISSUER)
                    .withClaim("username", username)
                    .withClaim("role", role)
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 签名验证
     * @param token
     * @return
     */
    public static boolean verify(String token){

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            log.info("issuer: " + jwt.getIssuer());
            log.info("username: " + jwt.getClaim("username").asString());
            log.info("id"+jwt.getClaim("id").asString());
            log.info("过期时间：" + jwt.getExpiresAt());
            return true;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * 验证 Refresh Token 是否有效
     * @param token
     * @return boolean
     */
    public static boolean verifyRefreshToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET))
                    .withIssuer(ISSUER) // 指定签发者
                    .build();
            DecodedJWT jwt = verifier.verify(token); //
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取 Token 的过期时间
     * @param token
     * @return Date
     */
    public static Date getExpirationDateFromToken(String token) {
        return decode(token).getExpiresAt();
    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    public static String getUsername(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
        DecodedJWT jwt = verifier.verify(token);
        String id = jwt.getClaim("username").asString();
        return id;

    }
    /**
     * 获取角色
     * @param token
     * @return
     */
    public static String getRole(String token) {
        DecodedJWT jwt = decode(token);
        return jwt.getClaim("role").asString();
    }

    /**
     * 解码 token
     * @param token
     * @return DecodedJWT
     */
    public static DecodedJWT decode(String token) {
        return JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build().verify(token);
    }

}
