package com.member.service;

import com.member.pojo.Addresses;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 地址信息表 服务类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
public interface AddressesService extends IService<Addresses> {
    boolean addAddress(Addresses address);

    boolean updateAddress(Addresses address);
}
