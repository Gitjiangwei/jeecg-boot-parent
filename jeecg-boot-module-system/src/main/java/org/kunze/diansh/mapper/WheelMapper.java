package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Wheel;
import org.kunze.diansh.pojo.domain.wheel.WheelDO;
import org.kunze.diansh.pojo.param.wheel.UpdateWheelStatusParam;
import org.kunze.diansh.pojo.param.wheel.WheelParam;

import java.util.List;

/**
 * 轮播图持久层接口
 *
 * @author 姜伟
 * @date 2020/7/17
 */
public interface WheelMapper extends BaseMapper< Wheel > {
    /**
     * 添加轮播图
     *
     * @param wheelParam 添加参数
     * @return 添加成功行数
     * @author 姜伟
     * @date 2020/7/18 20:04
     */
    Integer saveWheel(WheelParam wheelParam);

    /**
     * 获取首页展示轮播图数据
     *
     * @param wheelParam 查询条件
     * @return 轮播图数据列表
     * @author 姜伟
     * @date 2020/7/17 20:32
     */
    List< WheelDO > listWheel(WheelParam wheelParam);

    /**
     * 修改轮播图信息
     *
     * @param wheelParam 修改参数
     * @return 修改成功返回行数
     * @author 姜伟
     * @date 2020/7/18 20:19
     */
    Integer updateWheel(WheelParam wheelParam);

    /**
     * 批量删除轮播图
     *
     * @param delWheels 轮播图id
     * @return 删除后返回行数
     * @author 姜伟
     * @date 2020/7/20 18:31
     */
    Integer batchDeleteWheel(@Param("list") List< String > delWheels);

    /**
     * 修改轮播图展示状态
     *
     * @param updateWheelStatusParam 修改参数
     * @return 修改成功后返回行数
     * @author 姜伟
     * @date 2020/7/20 18:31
     */
    Integer updateIsFlag(UpdateWheelStatusParam updateWheelStatusParam);

    /**
     * 获取轮播图列表数据
     *
     * @param wheelParam 查询条件
     * @return 轮播图列表数据
     * @author 姜伟
     * @date 2020/7/17 19:01
     */
    List< WheelDO > listWheelBackstage(WheelParam wheelParam);

    /**
     * 获取超市id数据列表
     *
     * @param id 轮播图id
     * @return 超市Id数据列表
     * @author 姜伟
     * @date 2020/7/20 18:29
     */
    List< String > listShopId(@Param("id") String id);

    /**
     * 根据轮播图id查询轮播图
     *
     * @param id 轮播图Id
     * @return 轮播图信息
     * @author 姜伟
     * @date 2020/7/20 20:04
     */
    String queryImages(@Param("id") String id);

}
