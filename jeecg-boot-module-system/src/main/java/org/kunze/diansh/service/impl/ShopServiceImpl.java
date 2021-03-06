package org.kunze.diansh.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jeecg.common.util.CalculationUtil;
import org.jeecg.common.util.CommonUtil;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.controller.vo.ShopVo;
import org.kunze.diansh.entity.Commodity;
import org.kunze.diansh.entity.KzShop;
import org.kunze.diansh.mapper.ShopMapper;
import org.kunze.diansh.service.IShopService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;


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
        KzShop shop = new KzShop();
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
//        if (shop.getPostFree() != null){
//            BigDecimal postFree = new BigDecimal(shop.getPostFree());
//            BigDecimal newPostFree = postFree.multiply(new BigDecimal(100));
//            shop.setPostFree(newPostFree.intValue());
//        }
        if(EmptyUtils.isNotEmpty(shop.getMinPrice())){
            BigDecimal newMinPrice = NumberUtil.mul(shop.getMinPrice().toString(),"100");
            shop.setMinPrice(newMinPrice.intValue());
        }
        if(shop.getId() != null && !shop.getId().equals("")){
            int result =  shopMapper.updateShop(shop);
            if(result>0){

                flag = true;
            }
        }
        return flag;
    }

    /***
     * 删除超市信息
     * @param shopId
     * @return
     */
    @Override
    public Boolean delShops(String shopId) {
        Boolean isflag = false;
        if(shopId!=null&&!shopId.equals("")){
            char a = shopId.charAt(shopId.length()-1);
            if(a == ','){
                shopId = shopId.substring(0,shopId.length()-1);
            }
            if(shopId==null||shopId.equals("")){
                return false;
            }
            List<String> list = new ArrayList<String>();
            if(shopId.contains(",")){
                list = new ArrayList<String>(Arrays.asList(shopId.split(",")));
            }else {
                list.add(shopId);
            }
            int result = shopMapper.delShops(list);
            if(result>0){
                isflag = true;
            }

        }
        return isflag;
    }

    /**
     * 通过超市id查询超市信息
     * @param shopId
     * @return
     */
    @Override
    public List<Map<String, Object>> selectShopInfoById(String shopId) {
        return CommonUtil.toCamel(shopMapper.selectShopInfoById(shopId));
    }

    /**
     * 修改商家配送方式
     *
     * @param shopId
     * @param distModel
     * @param postFree
     * @return
     */
    @Override
    public Boolean editShopDistModel(String shopId, String distModel, String postFree) {
        Boolean isFlag = false;
        if(!StringUtils.isEmpty(shopId)){
            int result = shopMapper.editShopDistModel(shopId,distModel,Integer.valueOf(CalculationUtil.MetaconversionScore(postFree)));
            if(result>0){
                isFlag = true;
            }
        }
        return isFlag;
    }
}
