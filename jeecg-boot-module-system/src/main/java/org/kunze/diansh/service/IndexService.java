package org.kunze.diansh.service;

import com.github.pagehelper.PageInfo;
import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.entity.Goods;
import org.kunze.diansh.entity.SearchRequest;

public interface IndexService {

    Goods buildGoods(SpuBo spuBo);

    /**
     * 全文检索
     * @param request
     * @return
     */
    PageInfo<Goods> search(SearchRequest request);
}
