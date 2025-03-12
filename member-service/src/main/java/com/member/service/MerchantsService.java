package com.member.service;

import com.member.pojo.Merchants;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商家信息表 服务类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
public interface MerchantsService extends IService<Merchants> {

    boolean deleteMerchant(Integer merchantId);
}
