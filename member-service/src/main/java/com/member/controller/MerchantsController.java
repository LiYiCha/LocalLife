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
@RequestMapping("/merchants")
public class MerchantsController {

    @Autowired
    private MerchantsService ms;

    /**
     * 添加
     * @param merchants
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Merchants merchants) {
        // 密码加密加盐
        String pw = merchants.getPassword();
        MD5Utils.encrypt(pw);
        //保存密码
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
            MD5Utils.encrypt(password);
            //判断密码是否正确
            if (merchants.getPassword().equals(password)){
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
        return Result.error("注销失败，还有未关闭的店铺");
    }


    /**
     * 根据id查询
     * @param merchantId
     * @return
     */
    @GetMapping("/get")
    public Result get(@RequestParam("merchantId") Integer merchantId) {
        QueryWrapper<Merchants> qw = new QueryWrapper<>();
        qw.eq("merchant_id",merchantId);
        List<Merchants> merchants = ms.list(qw);
        if (merchants!=null){
            return Result.success(merchants);
        }
        return Result.error();
    }

}
