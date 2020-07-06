package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.kunze.diansh.controller.vo.HomeShopVo;
import org.kunze.diansh.entity.HomeShop;
import org.kunze.diansh.mapper.HomeShopMapper;
import org.kunze.diansh.service.IHomeShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class HomeShopServiceImpl extends ServiceImpl<HomeShopMapper, HomeShop> implements IHomeShopService {

    @Autowired
    private HomeShopMapper homeShopMapper;


    /**
     * 超市查询分类专区
     *
     * @param shopId
     * @param homegName
     * @return
     */
    @Override
    public PageInfo<HomeShopVo> queryHomeShop(String shopId, String homegName,Integer pageNo,Integer pageSize) {
        Page page = PageHelper.startPage(pageNo,pageSize);
        if(StringUtils.isEmpty(shopId)){
            return null;
        }
        HomeShop homeShop = new HomeShop();
        homeShop.setShopId(shopId);
        List<HomeShopVo> homeShopVoList = homeShopMapper.queryHomeShop(homeShop,homegName);
        PageInfo<HomeShopVo> pageInfo = new PageInfo<HomeShopVo>(homeShopVoList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    /**
     * 超市添加分类专区
     *
     * @param homeShop
     * @return
     */
    @Override
    public Boolean saveHomeShop(HomeShop homeShop) {
        Boolean isFlag = false;
        homeShop.setId(UUID.randomUUID().toString().replace("-",""));
        int result = homeShopMapper.saveHomeShop(homeShop);
        if(result>0){
            isFlag = true;
        }
        return isFlag;
    }

    /**
     * 超市修改分类专区
     *
     * @param homeShop
     * @return
     */
    @Override
    public Boolean editHomeShop(HomeShop homeShop) {
        Boolean isFlag = false;
        if(!StringUtils.isEmpty(homeShop.getId())){
            int result = homeShopMapper.editHomeShop(homeShop);
            if(result>0){
                isFlag = true;
            }
        }
        return isFlag;
    }

    /**
     * 超市删除分类专区
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean delHomeShop(String ids) {
        Boolean isFlag = false;
        if(ids != null && !ids.equals("")){
            char a = ids.charAt(ids.length()-1);
            if(a == ','){
                ids = ids.substring(0,ids.length()-1);
            }
            if(ids == null || ids.equals("")){
                return false;
            }
            List<String> stringList = new ArrayList<String>();
            if(ids.contains(",")){
                stringList = new ArrayList<String>(Arrays.asList(ids.split(",")));
            }else {
                stringList.add(ids);
            }
            int result = homeShopMapper.delHomeShop(stringList);
            if(result>0){
                isFlag = true;
            }
        }
        return isFlag;
    }

    /**
     * 检索当前超市下是否存在相同的专区
     *
     * @param homePageId
     * @param shopId
     * @return
     */
    @Override
    public Boolean queryNotHomeShop(String homePageId, String shopId) {
        Boolean isFlag = false;
        int result = homeShopMapper.queryNotHomeShop(homePageId,shopId);
        if(result>0){
            isFlag = true;
        }
        return isFlag;
    }
}
