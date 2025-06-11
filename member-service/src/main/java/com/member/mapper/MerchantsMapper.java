package com.member.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.member.dto.MerchantDTO;
import com.member.pojo.Merchants;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 商家信息表 Mapper 接口
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
public interface MerchantsMapper extends BaseMapper<Merchants> {

    List<MerchantDTO> getByMerchantUserName(String username);
}
