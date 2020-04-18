package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.UUIDGenerator;
import org.kunze.diansh.entity.Address;
import org.kunze.diansh.mapper.AddressMapper;
import org.kunze.diansh.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public int insertAddress(Address address) {
        //获取登录对象
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String id = UUIDGenerator.generate(); //uuid
        if(null != sysUser){
            address.setUserId(sysUser.getId());
        }
        address.setId(UUID.randomUUID().toString().replace("-",""));
        int result = addressMapper.insertAddress(address);
        return result;
    }
}
