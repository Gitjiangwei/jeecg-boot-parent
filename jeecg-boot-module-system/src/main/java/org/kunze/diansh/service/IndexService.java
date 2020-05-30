package org.kunze.diansh.service;

import com.github.pagehelper.PageInfo;
import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.controller.vo.BeSimilarSpuVo;
import org.kunze.diansh.entity.Goods;
import org.kunze.diansh.entity.SearchRequest;
import org.kunze.diansh.entity.SearchResult;

import java.util.List;

public interface IndexService {

    Goods buildGoods(SpuBo spuBo);

    /**
     * 全文检索
     * @param request
     * @return
     */
    SearchResult search(SearchRequest request);


    /***
     * 数据库中全文检索
     * @param key
     * @param shopId
     * @return
     */
    PageInfo<BeSimilarSpuVo> selectSpuTitleLike(String key, String shopId, Integer pageNo, Integer pageSize);
}
