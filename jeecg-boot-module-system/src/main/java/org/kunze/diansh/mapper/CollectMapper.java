package org.kunze.diansh.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Collect;
import org.kunze.diansh.entity.Spu;

import java.util.List;
import java.util.Map;

/**
 * 商品收藏映射
 */
public interface CollectMapper extends BaseMapper<Collect> {

    /**
     * 添加商品收藏记录
     * @param collect
     * @return
     */
    Integer insertCollect(@Param("collect") Collect collect);

    /**
     * 删除商品收藏记录
     * @param cList 收藏商品的主键集合
     * @return
     */
    Integer deleteCollect(@Param("list") List<String> cList);

    /**
     * 查询当前用户的收藏商品
     * @param userId
     * @return
     */
    List<Map<String,Object>> selectCollectByUId(@Param("userId") String userId);

    /**
     * 查询当前用户收藏商品总数
     * @param userId
     * @return
     */
    Integer countCollectByUId(@Param("userId") String userId);

    /**
     * 查询当前商品是否被收藏
     * @param spuId
     * @param userId
     * @return
     */
    Collect isCollect(@Param("userId") String userId,@Param("spuId") String spuId);
}
