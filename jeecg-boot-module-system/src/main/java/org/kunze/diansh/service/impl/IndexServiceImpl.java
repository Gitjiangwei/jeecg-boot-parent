package org.kunze.diansh.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.formula.functions.T;
import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.entity.*;
import org.kunze.diansh.mapper.NewCategoryMapper;
import org.kunze.diansh.mapper.SkuMapper;
import org.kunze.diansh.mapper.SpuDetailMapper;
import org.kunze.diansh.service.ISpecificationService;
import org.kunze.diansh.service.ISpuService;
import org.kunze.diansh.service.IndexService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class IndexServiceImpl implements IndexService {


    @Autowired
    private ISpuService spuService;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private ISpecificationService specificationService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private NewCategoryMapper categoryMapper;

    @Override
    public  Goods buildGoods(SpuBo spuBo) {

        String spuId = spuBo.getId();
        //准备数据
        //sku集合
        List<Sku> skuList = this.skuMapper.querySkuBySpuId(spuId);

        //spuDetail
        SpuDetail spuDetail = this.spuDetailMapper.qreySpuDetail(spuId);

        //分类名称查询
        List<Category> categories = this.categoryMapper.qryByIds(Arrays.asList(spuBo.getCid1(),spuBo.getCid2(),spuBo.getCid3()));
        List<String> names = new ArrayList<String>();
        for(Category item:categories){
            names.add(item.getName());
        }

        //查询通用规格参数
        Specification specifications = this.specificationService.qrySpecification(spuBo.getCid3());

        //处理sku
        //把商品价格取出单独存放，便于展示
        List<String> prices = new ArrayList<String>();
        List<String> newPrices = new ArrayList<String>();
        List<Map<String,Object>> skuLists = new ArrayList<Map<String,Object>>();
        for (Sku sku:skuList){
            prices.add(sku.getPrice());
            newPrices.add(sku.getNewPrice());
            Map<String,Object> skuMap = new HashMap<String, Object>();
            skuMap.put("id",sku.getId());
            skuMap.put("title",sku.getTitle());
            skuMap.put("image", StringUtils.isBlank(sku.getImages()) ? "":sku.getImages().split(",")[0]);
            skuMap.put("price",sku.getPrice());
            skuMap.put("newPrice",sku.getNewPrice());
            skuLists.add(skuMap);
        }

        //处理规格参数
        Map<String, Object> specMap = new HashMap<String, Object>();
        if(specifications
                != null) {
            String jsonStr = JSONObject.toJSONString(specifications.getSpecifications());
            jsonStr = StringEscapeUtils.unescapeJava(jsonStr);
            jsonStr = jsonStr.substring(jsonStr.indexOf("\"") + 1, jsonStr.lastIndexOf("\""));
            List<SpuParam> list = JSON.parseArray(jsonStr, SpuParam.class);
            //将可以进行全文检索的数据进行重新整理
            List<SpuParams> newList = new ArrayList<SpuParams>();
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).getParams().length; j++) {
                    if (list.get(i).getParams()[j].getSearchable()) {
                        SpuParams spuParams = new SpuParams();
                        BeanUtils.copyProperties(list.get(i).getParams()[j], spuParams);
                        newList.add(spuParams);
                    }
                }
            }
            //处理规格参数问题，默认显示id+值，处理后显示对应的名称+值

            for (SpuParams item : newList) {
                //通用参数
                String value = String.valueOf(item.getOptions()==null?"0":item.getOptions().length);
                if (item.getNumerical()!=null) {
                    if (item.getNumerical()) {
                        value = this.chooseSegment(value, item);
                    }
                }
                specMap.put(item.getK(), value);
            }
        }
        Goods goods = new Goods();
        goods.setId(spuBo.getId());
        //搜索条件拼接：这里如果要加品牌，可以再写个BrandClient，根据id查品牌
        goods.setAll(spuBo.getTitle()+" "+StringUtils.join(names," "));
        goods.setSubTitle(spuBo.getSubTitle());
        goods.setBrandId(spuBo.getBrandId());
        goods.setCid1(spuBo.getCid1());
        goods.setCid2(spuBo.getCid2());
        goods.setCid3(spuBo.getCid3());
        goods.setPrice(prices);
        goods.setSkus(JSONObject.toJSONString(skuLists));
        goods.setSpecs(specMap);
        return goods;
    }

    private String chooseSegment(String value,SpuParams spuParams){
        double val = NumberUtils.toDouble(value);
        String reult = "其它";
        String options = spuParams.getOptions().toString();
        //保存数值段
        for(String segment : options.split(",")){
            String [] segs = segment.split("-");
            //数值取值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[2]);
            }
            //判断是否在范围内
            if(val >= begin && val <= end){
                if(segs.length == 1){
                    reult = segs[0] + spuParams.getUnit() + "以上";
                }else if(begin == 0){
                    reult = segs[1] + spuParams.getUnit() + "以下";
                }else {
                    reult = segment + spuParams.getUnit();
                }
                break;
            }
        }
        return reult;
    }
}
