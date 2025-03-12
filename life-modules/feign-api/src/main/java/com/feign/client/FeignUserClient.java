package com.feign.client;

import com.core.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 文件名: FeignUserClient
 * 创建者: @一茶
 * 创建时间:2025/1/16 11:33
 * 描述：
 */
@FeignClient(name = "member-service")
public interface FeignUserClient {
    //用户
    @PostMapping("/users/add")
    Result add(@RequestParam("username") String username,
               @RequestParam("password") String password);

    @PostMapping("/users/login")
    Result login(@RequestParam("username") String username,
                 @RequestParam("password") String password);
    @PostMapping("/users/get")
    Result get(@RequestParam("username") String username);

    //商家
    @PostMapping("/merchants/add")
    Result add2(@RequestParam("username") String username,
               @RequestParam("password") String password);
    @PostMapping("/merchants/login")
    Result login2(@RequestParam("username") String username,
                 @RequestParam("password") String password);
}
