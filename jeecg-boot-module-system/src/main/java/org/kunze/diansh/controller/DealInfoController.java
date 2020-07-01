package org.kunze.diansh.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.kunze.diansh.controller.bo.DealInfoBo;
import org.kunze.diansh.controller.vo.DealInfoVo;
import org.kunze.diansh.entity.DealInfo;
import org.kunze.diansh.service.IDealInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "商家历史销售额管理")
@RequestMapping(value = "/kunze/deal")
@RestController
public class DealInfoController {

    @Autowired
    private IDealInfoService dealInfoService;



    @GetMapping(value = "/queryDeal")
    public Result<PageInfo<DealInfoVo>> queryDealInfoList(DealInfoBo dealInfoBo,
                                                          @RequestParam(name = "pageNo") String pageNo,
                                                          @RequestParam(name = "pageSize") String pageSize){
        Result<PageInfo<DealInfoVo>> result = new Result<PageInfo<DealInfoVo>>();
        if(StringUtils.isEmpty(dealInfoBo.getDateFlag())){
            result.error500("参数丢失");
        }else if(!dealInfoBo.getDateFlag().equals("0") && !dealInfoBo.getDateFlag().equals("1")){
            result.error500("参数被篡改");
        }else {
            PageInfo<DealInfoVo> pageInfo = dealInfoService.queryDealInfoList(dealInfoBo,Integer.valueOf(pageNo),Integer.valueOf(pageSize));
            result.setSuccess(true);
            result.setResult(pageInfo);
        }
        return result;
    }
}
