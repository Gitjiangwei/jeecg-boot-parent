package org.kunze.diansh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.entity.*;
import org.kunze.diansh.esRepository.GoodsRepository;
import org.kunze.diansh.mapper.*;
import org.kunze.diansh.service.ISpecificationService;
import org.kunze.diansh.service.IndexService;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import org.kunze.diansh.entity.SearchResult;

import static org.elasticsearch.search.aggregations.Aggregation.CommonFields.BUCKETS;


@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private ISpecificationService specificationService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private NewCategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private static final Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);
    @Override
    public  Goods buildGoods(SpuBo spuBo) {

        String spuId = spuBo.getId();
        //准备数据
        //sku集合
        List<Sku> skuList = this.skuMapper.querySkuBySpuId(spuId);

        //spuDetail
        SpuDetail spuDetail = this.spuDetailMapper.qreySpuDetail(spuId);

        Spu spu = this.spuMapper.querySpuByIds(spuId);

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
        if(specifications != null) {
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
        goods.setCreateTime(spu.getCreateTime());
        goods.setPrice(prices);
        goods.setSkus(JSONObject.toJSONString(skuLists));
        goods.setSpecs(specMap);
        return goods;
    }

    /**
     * 全文检索
     *
     * @param request
     * @return
     */
    @Override
    public SearchResult search(SearchRequest request) {
        String key = request.getKey();
        if (StringUtils.isBlank(key)) {
            return null;
        }
        PageHelper.startPage(request.getPage() - 1, request.getSize());
        //1、创建查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        MatchQueryBuilder basicQuery = QueryBuilders.matchQuery("all",key).operator(Operator.AND);
        //2、查询
        // 2.1对结果进行筛选
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));
        // 2.2基本查询
        queryBuilder.withQuery(basicQuery);
        // 2.3分页
        searchWithPageAndSort(queryBuilder,request);
        //3、聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("brandId"));
        queryBuilder.addAggregation(AggregationBuilders.terms("category").field("cid3"));
        //执行查询获得结果集
        AggregatedPage<Goods> goodsAggregatedPage = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());
        //获取聚合结果集
        //商品分类的聚合结果
        List<Category> categories = this.getCategoryAggResult(goodsAggregatedPage.getAggregation("category"));
        // 品牌的聚合结果
        List<Brand> brandList = this.getBrandAggResult(goodsAggregatedPage.getAggregation("brands"));

        // 根据商品分类判断是否需要聚合
        List<Map<String, Object>> specs = new ArrayList<>();
        if (categories !=null && categories.size() == 1) {
            // 如果商品分类只有一个才进行聚合，并根据分类与基本查询条件聚合
            specs = getSpec(categories.get(0).getId(),basicQuery);
        }
        //3、返回结果
/*        SearchResult
        PageInfo<Goods> pageInfo = new PageInfo<Goods>();
        pageInfo.setTotal(goodsAggregatedPage.getTotalElements());
        pageInfo.setList(result.getContent());
        pageInfo.setPages((int) (pageInfo.getTotal()+request.getSize()/request.getSize()));*/
        return new SearchResult(goodsAggregatedPage.getTotalElements(),goodsAggregatedPage.getTotalPages(),goodsAggregatedPage.getContent(),
                categories,brandList,specs);
    }

    // 解析品牌聚合结果
    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        try {
            LongTerms brandAgg = (LongTerms) aggregation;
            List<String> bids = new ArrayList<>();
            for (LongTerms.Bucket bucket : brandAgg.getBuckets()) {
                bids.add(String.valueOf(bucket.getKeyAsNumber().longValue()));
            }
            // 根据id查询品牌
            return this.brandMapper.qryByKeys(bids);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * 聚合出规格参数
     * @param cid
     * @param queryBuilder
     * @return
     */
    private List<Map<String,Object>> getSpec(String cid,QueryBuilder queryBuilder){
        try {
            // 不管是全局参数还是sku参数，只要是搜索参数，都根据分类id查询出来
            Specification specifications = this.specificationService.qrySpecification(cid);
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
            List<Map<String,Object>> specs = new ArrayList<>();
            NativeSearchQueryBuilder queryBuilder1 = new NativeSearchQueryBuilder();
            queryBuilder1.withQuery(queryBuilder);

            //聚合规格参数
            newList.forEach(p ->{
                String key = p.getK();
                queryBuilder1.addAggregation(AggregationBuilders.terms(key).field("specs."+key+"keyword"));
            });
            //查询
            Map<String,Aggregation> aggs = this.elasticsearchTemplate.query(queryBuilder1.build(),
                    SearchResponse::getAggregations).asMap();

            //解析聚合结果
            newList.forEach(param ->{
                Map<String,Object> spec = new HashMap<>();
                String key = param.getK();
                spec.put("k",key);
                StringTerms terms = (StringTerms) aggs.get(key);
                spec.put("options",terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString));
                specs.add(spec);
            });
            return specs;
        }catch (Exception e){
            return null;
        }

    }

    //构建基本查询条件
    private void searchWithPageAndSort(NativeSearchQueryBuilder queryBuilder,SearchRequest request){
        //准备分页
        int page = request.getPage();
        int size = request.getSize();
        //1、分页
        queryBuilder.withPageable(PageRequest.of(page-1,size));
        //2、排序
        String sortBy = request.getSortBy();
        Boolean desc = request.getDescending();
        if(StringUtils.isNotBlank(sortBy)){
            //如果不为空
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(desc? SortOrder.DESC:SortOrder.ASC));
        }
    }

    //解析商品分类聚合结果
    private List<Category> getCategoryAggResult(Aggregation aggregation){
        try {
            if (aggregation!=null) {
                //String a = aggregation.getName();
                LongTerms categoryAgg = (LongTerms) aggregation;
               /* List<LongTerms.Bucket> buckets = ((LongTerms) aggregation).getBuckets();*/
                List<String> cids = new ArrayList<>();
          /*      for (LongTerms.Bucket bucket : categoryAgg.getBuckets()) {
                    cids.add(String.valueOf(bucket.getKeyAsNumber().longValue()));
                }*/
                //根据id查询商品分类
                List<Category> categoryList = this.categoryMapper.qryByIds(cids);
                List<Category> categories = new ArrayList<>();
                for (int i = 0; i < categoryList.size(); i++) {
                    Category category = new Category();
                    category.setId(categoryList.get(i).getId());
                    category.setName(categoryList.get(i).getName());
                    categories.add(category);
                }
                return categoryList;
            }else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    private String chooseSegment(String value,SpuParams spuParams){
        double val = NumberUtils.toDouble(value);
        String reult = "其它";
        String options = spuParams.getOptions().toString();
        //保存数值段
        for(String segment : options.split(",")){
            String[] segs = segment.split("-");
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
