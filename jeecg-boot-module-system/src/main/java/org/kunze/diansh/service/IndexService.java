package org.kunze.diansh.service;

import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.entity.Goods;

public interface IndexService {

    Goods buildGoods(SpuBo spuBo);
}
