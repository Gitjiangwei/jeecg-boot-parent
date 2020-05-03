package org.kunze.diansh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONObjectCodec;
import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.entity.Spu;
import org.kunze.diansh.mapper.SkuMapper;
import org.kunze.diansh.mapper.SpuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class Test {

    @Autowired
    private static SpuServiceImpl spuService;

    @Autowired
    private static SpuMapper spuMapper;

    @Autowired
    private static SkuMapper skuMapper;

    public static void main(String[] args) {

        JSONObject json = JSONObject.parseObject(getJson());

        JSONArray jsonArray = json.getJSONArray("data");


        Integer spuFlag = 1000;
        for(int i=0;i<jsonArray.size();i++){
            JSONObject object = jsonArray.getJSONObject(i);
            Spu spu = new Spu();
            spuFlag++;
            spu.setId(spuFlag.toString());
            spu.setTitle(object.get("name").toString());
            spu.setImage(object.get("main_image").toString());
            spu.setCid3("877");
            spu.setCid2("872");
            spu.setCid1("871");
            int rows = spuMapper.saveSpu(spu);
            List<Sku> skuList = new ArrayList<Sku>();
            if(rows != 0){

                Sku s = new Sku();
                s.setId(UUID.randomUUID().toString().replace("-",""));
                s.setSpuId(spu.getId());
                s.setTitle(object.get("name").toString());
                s.setImages(object.get("main_image").toString());
                s.setPrice("1000");
                skuList.add(s);
                skuMapper.saveSku(skuList);
            }
        }

        //spuService.insertSku(skuList);

        String sql = "insert into kz_spu(id,title,sub_title,cid1,cid2,cid3,brand_id,create_time,last_update_time,update_name,images,image)\n" +
                "        values (#{id},#{title},#{subTitle},#{cid1},#{cid2},#{cid3},#{brandId},NOW(),NOW(),#{updateName},#{images},#{image})";
    }



    public static String getJson() {
        String jsonStr = "";
        try {
            File file = new File("D:\\opt\\分类\\分类\\粮油调味.json");
            FileReader fileReader = new FileReader(file);
            Reader reader = new InputStreamReader(new FileInputStream(file),"Utf-8");

            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (Exception e) {
            return null;
        }
    }

}
