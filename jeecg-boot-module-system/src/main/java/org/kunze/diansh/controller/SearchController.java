package org.kunze.diansh.controller;


import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.kunze.diansh.entity.Goods;
import org.kunze.diansh.entity.SearchRequest;
import org.kunze.diansh.entity.SearchResult;
import org.kunze.diansh.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = "全文检索商品")
@RestController
@RequestMapping(value = "/kunze/homeSpu")
public class SearchController {

    @Autowired
    private IndexService indexService;


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
}