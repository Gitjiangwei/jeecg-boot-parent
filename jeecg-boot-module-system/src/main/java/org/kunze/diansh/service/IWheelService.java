package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.kunze.diansh.entity.Wheel;
import org.kunze.diansh.pojo.request.wheel.DeleteWheelRequest;
import org.kunze.diansh.pojo.request.wheel.HomeWheelRequest;
import org.kunze.diansh.pojo.request.wheel.QueryWheelRequest;
import org.kunze.diansh.pojo.request.wheel.SaveWheelRequest;
import org.kunze.diansh.pojo.request.wheel.UpdateWheelRequest;
import org.kunze.diansh.pojo.request.wheel.UpdateWheelStatusRequest;
import org.kunze.diansh.pojo.vo.wheel.HomeWheelVO;
import org.kunze.diansh.pojo.vo.wheel.WheelVO;

import java.util.List;

/**
 * 轮播图管理业务接口
 *
 * @author 姜伟
 * @date 2020/9/17
 */
public interface IWheelService extends IService< Wheel > {
    /**
     * 添加轮播图
     *
     * @param saveWheelRequest 添加参数
     * @return 添加成功失败标记
     * @author 姜伟
     * @date 2020/9/18 20:01
     */
    Result< T > saveWheel(SaveWheelRequest saveWheelRequest);

    /**
     * 首页获取轮播图展示数据
     *
     * @param homeWheelRequest 查询条件
     * @return 轮播图展示数据
     * @author 姜伟
     * @date 2020/9/17 19:56
     */
    Result< PageInfo< HomeWheelVO > > listWheelHome(HomeWheelRequest homeWheelRequest);

    /**
     * 修改轮播图信息
     *
     * @param updateWheelRequest 修改参数
     * @return 修改成功失败标记
     * @author 姜伟
     * @date 2020/9/18 20:16
     */
    Result< T > updateWheel(UpdateWheelRequest updateWheelRequest);

    /**
     * 单个删除轮播图
     *
     * @param wheelId 轮播图id
     * @return 返回删除是否成功状态
     * @author 姜伟
     * @date 2020/9/20 18:19
     */
    Result< T > delWheel(String wheelId);

    /**
     * 批量删除轮播图
     *
     * @param deleteWheelRequest
     * @return 删除成功或者失败标记
     * @author 姜伟
     * @date 2020/9/18 20:38
     */
    Result< T > delWheels(DeleteWheelRequest deleteWheelRequest);

    /**
     * 批量修改首页轮播图是否启用
     *
     * @param updateWheelStatusRequest 修改参数
     * @return 返回修改结果
     * @author 姜伟
     * @date 2020/9/20 16:51
     */
    Result< T > updateWheelIsFlag(UpdateWheelStatusRequest updateWheelStatusRequest);

    /**
     * 后台获取轮播图展示数据
     *
     * @param queryWheelRequest 查询条件
     * @return 轮播图展示数据列
     * @author 姜伟
     * @date 2020/9/17 17:43
     */
    Result< PageInfo< WheelVO > > listPageWheelBackstage(QueryWheelRequest queryWheelRequest);

    /**
     * 获取超市数据列表
     *
     * @param id 轮播图id
     * @return 超市数据列表
     * @author 姜伟
     * @date 2020/9/20 18:25
     */
    Result< List< String > > listShopByShopId(@Param("id") String id);
}
