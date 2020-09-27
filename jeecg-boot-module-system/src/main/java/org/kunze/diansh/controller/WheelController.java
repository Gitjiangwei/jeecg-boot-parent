package org.kunze.diansh.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.pojo.request.wheel.*;
import org.kunze.diansh.pojo.vo.wheel.HomeWheelVO;
import org.kunze.diansh.pojo.vo.wheel.WheelVO;
import org.kunze.diansh.service.IWheelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 轮播图管理控制类
 *
 * @author 姜伟
 * @date 2020/7/17
 */
@RestController
@Slf4j
@Api(tags = "轮播图")
@RequestMapping(value = "/kunze/wheel")
public class WheelController {

    /**
     * 轮播图控制类
     */
    @Autowired
    private IWheelService wheelService;

    /**
     * 添加轮播图
     *
     * @param saveWheelRequest 插入参数
     * @return 添加成功或失败提示内容
     * @author 姜伟
     * @date 2020/7/18 19:46
     */
    @ApiOperation("添加轮播图片")
    @AutoLog("添加轮播图片")
    @PostMapping(value = "/saveWheel")
    public Result< T > saveWheel(@RequestBody @Valid SaveWheelRequest saveWheelRequest) {
        return wheelService.saveWheel(saveWheelRequest);
    }

    /**
     * 获取首页轮播图展示数据
     *
     * @param homeWheelRequest 查询条件
     * @return 首页轮播图数据列表
     * @author 姜伟
     * @date 2020/7/17 19:50
     */
    @ApiOperation("首页查询轮播图")
    @AutoLog("首页查询轮播图")
    @GetMapping(value = "/homeWheelList")
    public Result< PageInfo< HomeWheelVO > > queryWheelHome(@Valid HomeWheelRequest homeWheelRequest) {
        return wheelService.listWheelHome(homeWheelRequest);
    }

    /**
     * 修改轮播图信息
     *
     * @param updateWheelRequest 修改参数
     * @return 修改成功或者失败标记
     * @author 姜伟
     * @date 2020/7/18 20:10
     */
    @ApiOperation("修改轮播图片")
    @AutoLog("修改轮播图")
    @PostMapping(value = "/updateWheel")
    public Result< T > updateWheel(@RequestBody @Valid UpdateWheelRequest updateWheelRequest) {
        return wheelService.updateWheel(updateWheelRequest);
    }

    /**
     * 删除轮播图
     *
     * @param id 轮播图id
     * @return 删除成功或者失败状态
     * @author 姜伟
     * @date 2020/7/18 20:36
     */
    @ApiOperation("删除轮播图片")
    @AutoLog("删除轮播图片")
    @DeleteMapping(value = "/delWheel")
    public Result< T > delWheel(@RequestParam(name = "id") String id) {
        return wheelService.delWheel(id);
    }

    /**
     * 批量删除轮播图
     *
     * @param deleteWheelRequest 传入参数
     * @return 批量删除成功或者失败标记
     * @author 姜伟
     * @date 2020/7/18 20:36
     */
    @ApiOperation("批量删除轮播图片")
    @AutoLog("批量删除轮播图片")
    @DeleteMapping(value = "/delWheels")
    public Result< T > delWheels(@Valid DeleteWheelRequest deleteWheelRequest) {
        return wheelService.delWheels(deleteWheelRequest);
    }

    /**
     * 首页轮播图展示开店或者关闭
     *
     * @param updateWheelStatusRequest 传入修改参数
     * @return 返回修改成功标记
     * @author 姜伟
     * @date 2020/7/20 15:56
     */
    @ApiOperation("首页轮播图展示打开或关闭")
    @AutoLog("首页轮播图展示打开或关闭")
    @PostMapping(value = "/updateIsFlag")
    public Result< T > updateWheelIsFlag(@RequestBody @Valid UpdateWheelStatusRequest updateWheelStatusRequest) {
        return wheelService.updateWheelIsFlag(updateWheelStatusRequest);
    }

    /**
     * 后台获取轮播图数据列表
     *
     * @param queryWheelRequest 查询条件
     * @return 获取轮播图展示数据
     * @author 姜伟
     * @date 2020/7/17 17:49
     */
    @AutoLog("查询轮播图列表")
    @ApiOperation("后台查询轮播图列表")
    @GetMapping(value = "/pageWheelList")
    public Result< PageInfo< WheelVO > > queryPageWheelBackstageList(@Valid QueryWheelRequest queryWheelRequest) {
        return wheelService.listPageWheelBackstage(queryWheelRequest);
    }

    /**
     * 获取超市数据列表
     *
     * @param id 轮播图Id
     * @return 超市时间列表
     * @author 姜伟
     * @date 2020/7/20 18:21
     */
    @GetMapping(value = "/selectByShopId")
    public Result< List< String > > queryShopList(@RequestParam(name = "id") String id) {
        return wheelService.listShopByShopId(id);
    }
}
