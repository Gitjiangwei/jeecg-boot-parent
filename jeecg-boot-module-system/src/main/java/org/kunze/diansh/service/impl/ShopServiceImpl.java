package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.kunze.diansh.controller.vo.ShopVo;
import org.kunze.diansh.entity.Shop;
import org.kunze.diansh.mapper.ShopMapper;
import org.kunze.diansh.service.IShopService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper,Shop> implements IShopService {


    @Autowired
    private ShopMapper shopMapper;


    /***
     * 查询所有商家
     * @param shopVo
     * @return
     */
    @Override
    public PageInfo<ShopVo> queryShopList(ShopVo shopVo,Integer pageNo,Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        Shop shop = new Shop();
        BeanUtils.copyProperties(shopVo,shop);
        List<ShopVo> shops = shopMapper.queryShopList(shop);
        return new PageInfo<ShopVo>(shops);
    }

    /***
     * 添加超市信息
     * @param shop
     * @return
     */
    @Override
    public Boolean insertShop(Shop shop) {
        Boolean flag = false;
        shop.setId(UUID.randomUUID().toString().replace("-",""));
        int reuslt = shopMapper.insertShop(shop);
        if(reuslt > 0){
            flag = true;
        }
        return flag;
    }

    /**
     * 修改超市信息
     *
     * @param shop
     * @return
     */
    @Override
    public Boolean updateShop(Shop shop) {
        Boolean flag = false;
        if(shop.getId() != null && !shop.getId().equals("")){
            int result =  shopMapper.updateShop(shop);
            if(result>0){
                flag = true;
            }
        }
        return flag;
    }
}
