package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.kunze.diansh.entity.Address;

public interface AddressMapper extends BaseMapper<Address> {
    int insertAddress(Address address);
}
