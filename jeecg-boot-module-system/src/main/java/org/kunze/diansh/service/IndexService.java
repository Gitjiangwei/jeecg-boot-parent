package org.kunze.diansh.service;

import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.entity.Goods;
import org.kunze.diansh.entity.SearchRequest;
import org.kunze.diansh.entity.SearchResult;

public interface IndexService {

    Goods buildGoods(SpuBo spuBo);

    /**
     * 全文检索
     * @param request
     * @return
     */
    SearchResult search(SearchRequest request);
}
