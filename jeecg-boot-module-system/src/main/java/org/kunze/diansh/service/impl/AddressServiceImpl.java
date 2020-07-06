package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.exception.AddressException;
import org.jeecg.common.util.TreeUtil;
import org.kunze.diansh.entity.Address;
import org.kunze.diansh.entity.Region;
import org.kunze.diansh.mapper.AddressMapper;
import org.kunze.diansh.mapper.RegionMapper;
import org.kunze.diansh.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private RegionMapper regionMapper;

    @Value("${address_max}")
    private Integer addressMaxSizi;

    /**
     * 添加地址
     * @param address
     * @return
     */
    @Override
    @Transactional
    public void insertAddress(Address address) throws AddressException {
        //统计用户的地址数量
        Integer count = addressMapper.countByUid(address.getUserId());
        if(count > addressMaxSizi){
            throw new AddressException("添加收获地址失败，您的收货地址已达到上限"+addressMaxSizi+"条！");
        }

        if(count > 0){
            if(address.getIsDefault().equals("1")){
                //重置用户所有地址为非默认地址
                addressMapper.resetAddressDefault(address.getUserId());
            }
        }
        address.setId(UUID.randomUUID().toString().replace("-",""));
        int resultNum = addressMapper.insertAddress(address);
        if(resultNum != 1){
            throw new AddressException("添加收获地址时出现未知错误，请联系管理员！");
        }
    }

    /**
     * 查询地址
     * @param userID 用户id
     * @return
     */
    @Override
    public List<Address> selectAddress(String userID) {
        List<Address> list = addressMapper.selectAddress(userID);
        return list;
    }

    /**
     * 修改地址
     * @param address
     * @return
     */
    @Override
    @Transactional
    public void updateAddress(Address address) {
        //如何当前地址为默认地址，则重置所有地址状态
        if("1".equals(address.getIsDefault())){
            //重置收获地址为非默认
            addressMapper.resetAddressDefault(address.getUserId());
        }
        addressMapper.updateAddress(address);
    }

    /**
     * 删除地址
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Boolean deleteAddress(String id) {
        if(null == id || "".equals(id)){
            return false;
        }
        return addressMapper.deleteAddress(id);
    }

    /**
     * 设置某个用户的某个地址
     * @param id 地址id
     * @param userID 用户id
     * @return 受影响的行数
     */
    @Override
    @Transactional
    public void updateDefaultByID(String id, String userID) throws AddressException {
        Address address = addressMapper.selectAddressByID(id);
        if(null == address){
            throw new AddressException("设置默认地址失败!您尝试访问的数据不存在!");
        }

        if(!address.getUserId().equals(userID)){
            throw new AddressException("设置默认地址失败，非法访问已被拒绝！");
        }
        //重置收获地址为非默认
        Integer rows = addressMapper.resetAddressDefault(userID);
        if(rows < 1){
            throw new AddressException("[1]设置默认地址失败!更新时出现未知错误，请联系管理员！");
        }
        rows = addressMapper.updateDefaultByID(id,userID);
        if(rows != 1){
            throw new AddressException("[2]设置默认地址失败!更新时出现未知错误，请联系管理员！");
        }
    }

    /**
     * 根据地址id查询地址
     * @param id
     * @return
     */
    @Override
    public Address selectAddressByID(String id) {
        return addressMapper.selectAddressByID(id);
    }

    /**
     * 查询省市区全部信息
     * 用Tree格式返回
     * @return
     */

    public Collection selectRegionInfo(){
        QueryWrapper<Region> queryWrapper = new QueryWrapper<>();
        List<Region> regionList = regionMapper.selectList(queryWrapper);
        Collection collection = TreeUtil.toTree(regionList,"id","pid","children", Region.class,"0");
        return collection;
    }
}
