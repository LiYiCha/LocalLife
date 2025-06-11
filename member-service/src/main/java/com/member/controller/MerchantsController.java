package com.member.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.utils.MD5Utils;
import com.member.pojo.Merchants;
import com.member.service.MerchantsService;
import com.core.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商家信息表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@RestController
@RequestMapping("/api/member/merchants")
public class MerchantsController {

    @Autowired
    private MerchantsService ms;

    /**
     * 注册
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestParam("username") String username,
                      @RequestParam("password") String password){
        // 密码加密加盐
        String pw = MD5Utils.encrypt(password);
        //保存
        Merchants merchants = new Merchants();
        merchants.setUsername(username);
        merchants.setPassword(pw);
        boolean save = ms.save(merchants);
        if (save) {
            return Result.success();
        }
        return Result.error();
    }
    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
       //查询账号是否存在
        Merchants merchants = ms.getOne(new QueryWrapper<Merchants>().eq("username", username));
        if (merchants!=null){
            //密码加密加盐
            String enPwd = MD5Utils.encrypt(password);
            //判断密码是否正确
            if (merchants.getPassword().equals(enPwd)){
                return Result.success(merchants);
            }else{
                return Result.error("密码错误");
            }
        }
        return Result.error("账号不存在");
    }

    /**
     * 修改
     * @param merchants
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody Merchants merchants) {
        boolean update = ms.updateById(merchants);
        if (update) {
            return Result.success();
        }
        return Result.error();
    }

    /**
     * 删除
     * @param merchantId
     * @return
     */
    @PostMapping("/delete")
    public Result delete(@RequestParam("merchantId") Integer merchantId) {
        boolean remove = ms.deleteMerchant(merchantId);
        if (remove) {
            return Result.success();
        }
        return Result.error(40011, "存在关联店铺，请先关闭所有店铺后再执行注销操作");
    }


    /**
     * 根据商家账号查询商家信息
     * @param username
     * @return
     */
    @GetMapping("/getByMerchantUserName")
    public Result getByMerchantUserName(@RequestParam("username") String username) {
        return ms.getByMerchantUserName(username);
    }

}
