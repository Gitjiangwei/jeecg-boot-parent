package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kunze.diansh.entity.Collect;

import java.util.List;
import java.util.Map;

public interface ICollectService extends IService<Collect> {

    /**
     * 添加商品收藏记录
     * @param collect
     * @return
     */
    String insertCollect(Collect collect);

    /**
     * 删除商品收藏记录
     * @param cList 收藏商品的主键集合
     * @return
     */
    Boolean deleteCollect(List<String> cList);

    /**
     * 查询当前用户的收藏商品
     * @param userId
     * @return
     */
    List<Map<String,Object>> selectCollectByUId(String userId);

    /**
     * 查询当前用户收藏商品总数
     * @param userId
     * @return
     */
    Integer countCollectByUId(String userId);

    /**
     * 查询当前商品是否被收藏
     * @param spuId
     * @param userId
     * @return
     */
    Collect isCollect(String userId,String spuId);
}
