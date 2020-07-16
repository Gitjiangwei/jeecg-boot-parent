package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.kunze.diansh.controller.vo.RiderVo;
import org.kunze.diansh.entity.Rider;

public interface IRiderService extends IService<Rider> {

    /**
     * 添加骑手信息
     * @param rider
     * @return
     */
    Boolean saveRider(Rider rider);


    /***
     * 修改骑手信息
     * @param rider
     * @return
     */
    Boolean editRider(Rider rider);


    /***
     * 查询骑手信息
     * @param rider
     * @return
     */
    PageInfo<RiderVo> queryRiderList(Rider rider,Integer pageNo,Integer pageSize);


    /***
     * 删除骑手信息
     * jw
     * @param ids
     * @return
     */
    Boolean delRider(String ids);

}
