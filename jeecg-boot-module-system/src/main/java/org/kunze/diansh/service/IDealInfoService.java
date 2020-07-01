package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.kunze.diansh.controller.bo.DealInfoBo;
import org.kunze.diansh.controller.vo.DealInfoVo;
import org.kunze.diansh.entity.DealInfo;


public interface IDealInfoService extends IService<DealInfo> {


    /**
     * 商家查询历史交易清单
     * @param dealInfoBo
     * @return
     */
    PageInfo<DealInfoVo> queryDealInfoList(DealInfoBo dealInfoBo,Integer pageNo,Integer pageSize);
}
