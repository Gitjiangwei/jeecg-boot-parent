package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.HomeShopVo;
import org.kunze.diansh.entity.HomeShop;

import java.util.List;

public interface IHomeShopService extends IService<HomeShop> {

    /**
     * 超市查询分类专区
     * @param shopId
     * @param homegName
     * @return
     */
    PageInfo<HomeShopVo> queryHomeShop(String shopId,String homegName,Integer pageNo,Integer pageSize);

    /**
     * 超市添加分类专区
     * @param homeShop
     * @return
     */
    Boolean saveHomeShop(HomeShop homeShop);


    /**
     * 超市修改分类专区
     * @param homeShop
     * @return
     */
    Boolean editHomeShop(HomeShop homeShop);

    /**
     * 超市删除分类专区
     * @param ids
     * @return
     */
    Boolean delHomeShop(String ids);


    /**
     * 检索当前超市下是否存在相同的专区
     * @param homePageId
     * @param shopId
     * @return
     */
    Boolean queryNotHomeShop(String homePageId,String shopId);
}
