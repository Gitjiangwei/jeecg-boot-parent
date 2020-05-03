package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.checkerframework.checker.units.qual.A;
import org.kunze.diansh.entity.Address;

import java.util.List;

public interface AddressMapper extends BaseMapper<Address> {
    int insertAddress(Address address);

    List<Address> selectAddress(@Param("userID") String userID);

    Boolean updateAddress(@Param("address") Address address);

    Boolean deleteAddress(@Param("id") String id);

    Integer countByUid(@Param("userID") String userID);

    Integer resetAddressDefault(@Param("userID") String userID);

    Address selectAddressByID(@Param("id") String id);

    Integer updateDefaultByID(@Param("id") String id,@Param("userID") String userID);

}
