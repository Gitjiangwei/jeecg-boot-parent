package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.kunze.diansh.controller.vo.WheelVo;
import org.kunze.diansh.entity.Wheel;

import java.util.List;

public interface IWheelService extends IService<Wheel> {

    /***
     * 添加轮播图片
     * @param wheelVo
     * @return
     */
    Boolean saveWheel(WheelVo wheelVo);


    /***
     * 查询轮播图片
     * @param wheelVo
     * @return
     */
    PageInfo<Wheel> queryWheel(WheelVo wheelVo,Integer pageNo,Integer pageSize);

    /***
     * 修改轮播图片
     * @param wheelVo
     * @return
     */
    Boolean updateWheel(WheelVo wheelVo);


    /***
     * 单个删除
     * @param wheelId
     * @return
     */
    Boolean delWheel(String wheelId);

    /**
     * 批量删除
     * @param wheelIds
     * @return
     */
    Boolean delWheels(String wheelIds);

    /**
     * 批量修改启用还是不启用1
     * @param
     * @return
     */
    Boolean updateIsFlag(String isFlag,String wheelIds);
}
