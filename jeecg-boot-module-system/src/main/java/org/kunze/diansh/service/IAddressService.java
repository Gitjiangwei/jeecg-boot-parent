package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.AddressException;
import org.kunze.diansh.entity.Address;

import java.util.Collection;
import java.util.List;

public interface IAddressService extends IService<Address> {
    /**
     * 添加用户地址信息
     * @param address
     * @return
     */
    void insertAddress(Address address) throws AddressException;

    /**
     * 查询用户地址信息
     * @param userID 用户id
     * @return
     */
    List<Address> selectAddress(String userID);

    /**
     * 修改用户地址信息
     * @param address
     * @return
     */
    void updateAddress(Address address);

    /**
     * 删除地址
     * @param id
     * @return
     */
    Boolean deleteAddress(String id);

    /**
     * 设置某用户的某个地址为默认地址
     * @param id 地址id
     * @param userID 用户id
     * @return 受影响的行数
     */
    void updateDefaultByID(String id,String userID) throws AddressException;

    /**
     * 查询省市区全部信息
     * 用Tree格式返回
     * @return
     */
    Collection selectRegionInfo();
}
