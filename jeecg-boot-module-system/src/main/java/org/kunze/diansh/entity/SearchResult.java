package org.kunze.diansh.entity;


import cn.hutool.db.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.*;


public class SearchResult extends PageInfo<Goods> {

    private List<Category> categories;// 分类过滤条件

    private List<Brand> brands; // 品牌过滤条件

    private List<Map<String,Object>> specs; // 规格参数过滤条件

    public SearchResult(Long total, Integer totalPage, List<Goods> items,
                        List<Category> categories, List<Brand> brands,
                        List<Map<String, Object>> specs) {
        super(items);
        super.setTotal(total);
        super.setPages(totalPage);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
