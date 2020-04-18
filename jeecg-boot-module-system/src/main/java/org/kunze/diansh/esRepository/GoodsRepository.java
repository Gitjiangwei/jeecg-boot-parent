package org.kunze.diansh.esRepository;

import org.kunze.diansh.entity.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

public interface GoodsRepository extends ElasticsearchRepository<Goods,String> {

}
