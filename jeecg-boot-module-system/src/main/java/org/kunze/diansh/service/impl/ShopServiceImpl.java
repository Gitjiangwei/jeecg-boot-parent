package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.kunze.diansh.controller.vo.ShopVo;
import org.kunze.diansh.entity.KzShop;
import org.kunze.diansh.mapper.ShopMapper;
import org.kunze.diansh.service.IShopService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, KzShop> implements IShopService {


    @Autowired
    private ShopMapper shopMapper;


    /***
     * 查询所有商家
     * @param shopVo
     * @return
     */
    @Override
    public PageInfo<ShopVo> queryShopList(ShopVo shopVo,Integer pageNo,Integer pageSize) {
        Page page = PageHelper.startPage(pageNo,pageSize);
        KzShop shop = new KzShop();
        BeanUtils.copyProperties(shopVo,shop);
        List<ShopVo> shops = shopMapper.queryShopList(shop);
        PageInfo pageInfo =  new PageInfo<ShopVo>(shops);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    @Override
    public List<ShopVo> queryShopLists() {
        KzShop shop = new KzShop();;
        return shopMapper.queryShopList(shop);
    }

    /***
     * 添加超市信息
     * @param shop
     * @return
     */
    @Override
    public Boolean insertShop(KzShop shop) {
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
    public Boolean updateShop(KzShop shop) {
        Boolean flag = false;
        if (shop.getPostFree() != null){
            BigDecimal postFree = new BigDecimal(shop.getPostFree());
            BigDecimal newPostFree = postFree.multiply(new BigDecimal(100));
            shop.setPostFree(newPostFree.intValue());
        }
        if(shop.getId() != null && !shop.getId().equals("")){
            int result =  shopMapper.updateShop(shop);
            if(result>0){
                flag = true;
            }
        }
        return flag;
    }
}
