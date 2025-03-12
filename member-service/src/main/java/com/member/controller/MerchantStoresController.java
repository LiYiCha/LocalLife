package com.member.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.member.pojo.MerchantStores;
import com.member.service.MerchantStoresService;
import com.core.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商家店铺信息表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@RestController
@RequestMapping("/merchantStores")
public class MerchantStoresController {

    @Autowired
    private MerchantStoresService mss;

    /**
     * 添加商家店铺信息
     * @param store
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody MerchantStores store) {
        boolean flag = mss.save(store);
        if (!flag) {
            return Result.error();
        }
        return Result.success();
    }

    /**
     * 修改商家店铺信息
     * @param store
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody MerchantStores store) {
        boolean flag = mss.updateById(store);
        if (!flag) {
            return Result.error();
        }
        return Result.success();
    }

    /**
     * 根据商家id删除商家店铺信息
     * @param merchantId
     * @return
     */
    @PostMapping("/delete")
    public Result delete(@RequestParam("merchantId") Integer merchantId) {
        QueryWrapper<MerchantStores> qw = new QueryWrapper<>();
        qw.eq("merchant_id", merchantId);
        boolean flag = mss.remove(qw);
        if (!flag) {
            return Result.error();
        }
        return Result.success();
    }


    /**
     * 根据商家id查询商家店铺信息
     * @param merchantId
     * @return
     */
    @PostMapping("/get")
    public Result get(@RequestParam("merchantId") Integer merchantId) {
        QueryWrapper<MerchantStores> qw = new QueryWrapper<>();
        qw.eq("merchant_id", merchantId);
        List<MerchantStores> stores = mss.list(qw);
        if (stores == null) {
            return Result.error();
        }
        return Result.success(stores);
    }
}
