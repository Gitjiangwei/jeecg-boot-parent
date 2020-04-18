package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kunze.diansh.entity.Address;

public interface IAddressService extends IService<Address> {
    /**
     * 添加用户地址信息
     * @param address
     * @return
     */
    int insertAddress(Address address);
}
