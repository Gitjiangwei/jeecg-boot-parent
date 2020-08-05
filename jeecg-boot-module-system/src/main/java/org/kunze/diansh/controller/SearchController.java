package org.kunze.diansh.controller;


import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.controller.vo.BeSimilarSpuVo;
import org.kunze.diansh.entity.Goods;
import org.kunze.diansh.entity.SearchRequest;
import org.kunze.diansh.entity.SearchResult;
import org.kunze.diansh.service.ISkuService;
import org.kunze.diansh.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "全文检索商品")
@RestController
@RequestMapping(value = "/kunze/homeSpu")
public class SearchController {

    @Autowired
    private IndexService indexService;

    @Autowired
    private ISkuService skuService;


    /**
     * 搜索商品
     *
     * @param
     * @return
     */
    @ApiOperation("全文检索")
    @PostMapping(value ="/page")
    public Result<SearchResult> search(@RequestParam(name = "key") String key) {
        Result<SearchResult> result = new Result<>();
        SearchRequest request = new SearchRequest();
        request.setKey(key);
        request.setDescending(true);
        request.setSortBy("createTime");
        SearchResult goodsPageInfo = this.indexService.search(request);
        result.setResult(goodsPageInfo);
        result.setSuccess(true);
        return result;
    }

    /**
     * 搜索商品
     *
     * @param
     * @return
     */
    @ApiOperation("新全文检索")
    @PostMapping(value ="/spuTitle")
    public Result selectSpuTitleLike(@RequestParam(name = "key") String key,
                                                          @RequestParam(name = "shopId")  String shopId,
                                                          @RequestParam(name = "shopType") String shopType,
                                                          @RequestParam(name = "pageNo") String pageNo,
                                                          @RequestParam(name = "pageSize",defaultValue = "10") String pageSize){
        Result result = new Result();
        if(EmptyUtils.isEmpty(shopId)){
            return result.error500("shopId is not null!");
        }
        if(EmptyUtils.isEmpty(shopType)){
            return result.error500("shopType is not null!");
        }
        if("1".equals(shopType)){
            PageInfo list = indexService.selectSpuTitleLike(key,shopId,Integer.valueOf(pageNo),Integer.valueOf(pageSize));
            result.setSuccess(true);
            result.setResult(list);
        }else if ("2".equals(shopType)){
            PageInfo hotelSkuList = skuService.searchHotelSku(shopId,key,Integer.parseInt(pageNo),Integer.parseInt(pageSize));
            result.setSuccess(true);
            result.setResult(hotelSkuList);
        }
        return result;
    }
}
