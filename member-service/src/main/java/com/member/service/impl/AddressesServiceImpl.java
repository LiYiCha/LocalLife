package com.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.member.pojo.Addresses;
import com.member.mapper.AddressesMapper;
import com.member.service.AddressesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 地址信息表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@Service
public class AddressesServiceImpl extends ServiceImpl<AddressesMapper, Addresses> implements AddressesService {


    /**
     * 添加地址
     */
    @Override
    public boolean addAddress(Addresses address) {
        // 检查 isDefault 是否为 null，避免空指针异常
        Byte isDefault = address.getIsDefault();
        if (isDefault != null && isDefault == 1) {
            // 创建查询条件，查找同一用户的其他地址
            QueryWrapper<Addresses> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", address.getUserId())
                    .eq("is_default", 1); // 只查找默认地址

            // 更新其他地址为非默认
            Addresses otherAddress = new Addresses();
            otherAddress.setIsDefault((byte) 0); // 设置为非默认
            this.update(otherAddress, wrapper);
        }
        // 保存当前地址
        return this.save(address);
    }


    /**
     * 更新地址
     */
    @Override
    public boolean updateAddress(Addresses address) {
        // 检查 isDefault 是否为 null，避免空指针异常
        Byte isDefault = address.getIsDefault();
        if (isDefault != null && isDefault == 1) {
            // 创建查询条件，查找同一用户的其他默认地址
            QueryWrapper<Addresses> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", address.getUserId())
                    .ne("address_id", address.getAddressId())
                    .eq("is_default", 1);

            // 检查是否存在其他默认地址
            if (this.count(wrapper) > 0) {
                // 更新其他地址为非默认
                Addresses otherAddress = new Addresses();
                otherAddress.setIsDefault((byte) 0);
                this.update(otherAddress, wrapper);
            }
        }
        return this.updateById(address);
    }

}
