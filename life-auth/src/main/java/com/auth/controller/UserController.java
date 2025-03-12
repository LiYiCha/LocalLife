package com.auth.controller;

import com.core.utils.RedisUtil;
import com.core.utils.Result;
import com.core.utils.TokenUtil;
import com.feign.client.FeignUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 文件名: UserController
 * 创建者: @一茶
 * 创建时间:2025/1/18 13:07
 * 描述：
 */
@RestController
@RequestMapping("/auth")
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
                        @RequestParam(value = "type", defaultValue = "user") String type) {
        // 判断用户类型(是商家类型就调用商家登录接口)
        if (type != null && type.equals("merchant")){
            Result result = fuc.login2(username, password);
            if (result.getCode() == 200) {
                // token存入redis中
                saveToken(username);
            }
            return result;
        }else {
            Result result = fuc.login(username, password);
            if (result.getCode() == 200) {
                saveToken(username);
            }
            return result;
        }
    }
    // 保存token到redis中
    public Result saveToken(String username) {
        // 生成token
        String token = TokenUtil.sign(username);
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
                           @RequestParam(value = "type", defaultValue = "user") String type) {
        // 判断用户类型(是商家类型就调用商家注册接口)
        if (type != null && type.equals("merchant")){
            Result result = fuc.add2(username, password);
            return result;
        }else{
        Result result = fuc.add(username, password);
        return result;
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

}
