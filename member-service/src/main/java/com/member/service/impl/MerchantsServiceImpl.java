package com.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.utils.Result;
import com.member.dto.MerchantDTO;
import com.member.pojo.MerchantStores;
import com.member.pojo.Merchants;
import com.member.mapper.MerchantsMapper;
import com.member.service.MerchantStoresService;
import com.member.service.MerchantsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商家信息表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@Service
public class MerchantsServiceImpl extends ServiceImpl<MerchantsMapper, Merchants> implements MerchantsService {

    @Autowired
    private MerchantsMapper mm;
    @Autowired
    private MerchantStoresService mss;
    /**
     * 删除商家账号
     */
    @Override
    public boolean deleteMerchant(Integer merchantId) {
        //查询是否还有店铺没有关闭
        QueryWrapper<MerchantStores> qw = new QueryWrapper<>();
        qw.eq("merchant_id", merchantId);
        MerchantStores one = mss.getOne(qw);
        if (one == null && "".equals(one)){
            return this.removeById(merchantId);
        }
        return false;
    }

    /**
     * 根据商家账号查询商家信息
     */
    @Override
    public Result getByMerchantUserName(String username) {
        List<MerchantDTO> byMerchantName = mm.getByMerchantUserName(username);
        if (byMerchantName!=null){
            return Result.success(byMerchantName);
        }
        return Result.error("商家不存在或者信息为空");
    }
}
