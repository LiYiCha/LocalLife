package com.auth.controller;

import com.auth.enums.UserType;
import com.core.utils.RedisUtil;
import com.core.utils.Result;
import com.core.utils.TokenUtil;
import com.feign.client.FeignUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 登录注册和注销 控制器
 * <p/>
 */
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private FeignUserClient fuc;
    @Autowired
    private RedisUtil redis;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(name= "type",defaultValue="USER",required=false) String type) {
        Result result;
        // 判断用户类型(是商家类型就调用商家登录接口)
        if (type != null && type.equals(UserType.MERCHANT)){
            result = fuc.login2(username, password);
            if (result.getCode() == 200) {
                // token存入redis中
                return saveToken(username,type);
            }
        }else if(type != null && type.equals(UserType.USER)){
            result = fuc.login(username, password);
            if (result.getCode() == 200) {
                return saveToken(username,type);
            }
        }else{
            return Result.error("登录失败,用户类型错误。");
        }
        return result;
    }
    // 保存token到redis中
    public Result saveToken(String username, String type) {
        // 生成token
        String token= "6";
        token = TokenUtil.sign(username , type);
        if (token == null || token.equals("6")) {
            return Result.error("Failed to generate token");
        }
        // token存入redis中
        try {
            //redis.set("token_" + token, username, 10 * 24 * 60 * 60, TimeUnit.SECONDS); // 单位秒，10天
            redis.set("token_" + token, username, 24*60 * 60, TimeUnit.SECONDS); // 单位秒，1小时
            return Result.success(token);
        } catch (Exception e) {
            return Result.error("Failed to store token in Redis");
        }
    }
    /**
     * 注册
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(name = "type", defaultValue = "USER") String type) {
        // 判断用户类型(是商家类型就调用商家注册接口)
        if (type != null && type.equals(UserType.MERCHANT)){
            return fuc.add2(username, password);
        }else if (type != null && type.equals(UserType.USER)){
             return fuc.add(username, password);
        }else{
            return Result.error("注册失败,用户类型错误。");
        }
    }

    /**
     * 退出登录
     * @param token
     * @return
     */
    @PostMapping("/logout")
    public Result logout(@RequestParam("token") String token) {
        try {
            if (token==null||token.equals("")){
                return Result.error("token不能为空");
            }
            redis.delete("token_" + token);
            return Result.success();
        } catch (Exception e) {
            return Result.error("Failed to delete token from Redis");
        }
    }

    /**
     * 刷新token
     * @param refreshToken
     * @return
     */
    @PostMapping("/refresh")
    public Result refreshToken(@RequestParam("refreshToken") String refreshToken) {
        if (TokenUtil.verifyRefreshToken(refreshToken)) {
            String username = TokenUtil.getUsername(refreshToken);
            String role = TokenUtil.getRole(refreshToken);
            if (role == null || role.trim().isEmpty()) {
                return Result.error("Invalid role");
            }
            String newAccessToken = TokenUtil.sign(username,role);
            return Result.success(newAccessToken);
        }
        return Result.error("Invalid refresh token");
    }


}
