package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.HomeShopVo;
import org.kunze.diansh.entity.HomeShop;

import java.util.List;

public interface HomeShopMapper extends BaseMapper<HomeShop> {


    /**
     * 超市查询分类专区
     * @param homeShop
     * @param homegName
     * @return
     */
    List<HomeShopVo> queryHomeShop(@Param("homeShop") HomeShop homeShop,@Param("homgName") String homegName);

    /**
     * 超市添加分类专区
     * @param homeShop
     * @return
     */
    int saveHomeShop(HomeShop homeShop);


    /**
     * 超市修改分类专区
     * @param homeShop
     * @return
     */
    int editHomeShop(HomeShop homeShop);

    /**
     * 超市删除分类专区
     * @param list
     * @return
     */
    int delHomeShop(@Param("list") List<String> list);


    /**
     * 检索当前超市下是否存在相同的专区
     * @param homePageId
     * @param shopId
     * @return
     */
    int queryNotHomeShop(@Param("homePageId") String homePageId,@Param("shopId") String shopId);

}
