package org.kunze.diansh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test {
    public static void main(String[] args) {
        String str = "[{\n" +
                " \"group\": \"主体参数\",\n" +
                " \"params\": [{\n" +
                "  \"k\": \"品牌\",\n" +
                "  \"searchable\": false,\n" +
                "  \"global\": true,\n" +
                "  \"numerical\": false,\n" +
                "  \"unit\": \"\",\n" +
                "  \"options\": []\n" +
                " }, {\n" +
                "  \"k\": \"型号\",\n" +
                "  \"searchable\": false,\n" +
                "  \"global\": false,\n" +
                "  \"numerical\": false,\n" +
                "  \"unit\": \"\",\n" +
                "  \"options\": []\n" +
                " }, {\n" +
                "  \"k\": \"产品颜色\",\n" +
                "  \"searchable\": false,\n" +
                "  \"global\": true,\n" +
                "  \"numerical\": false,\n" +
                "  \"unit\": \"\",\n" +
                "  \"options\": []\n" +
                " }, {\n" +
                "  \"k\": \"上市日期\",\n" +
                "  \"searchable\": false,\n" +
                "  \"global\": true,\n" +
                "  \"numerical\": false,\n" +
                "  \"unit\": \"\",\n" +
                "  \"options\": []\n" +
                " }, {\n" +
                "  \"k\": \"能效等级\",\n" +
                "  \"searchable\": true,\n" +
                "  \"global\": true,\n" +
                "  \"numerical\": false,\n" +
                "  \"unit\": \"\",\n" +
                "  \"options\": [\"一级能效\", \"二级能效\", \"三级能效\", \"政府节能\"]\n" +
                " }]\n" +
                "}, {\n" +
                " \"group\": \"显示参数\",\n" +
                " \"params\": [{\n" +
                "  \"k\": \"屏幕尺寸\",\n" +
                "  \"searchable\": true,\n" +
                "  \"global\": false,\n" +
                "  \"numerical\": true,\n" +
                "  \"unit\": \"英寸\",\n" +
                "  \"options\": []\n" +
                " }, {\n" +
                "  \"k\": \"屏幕分辨率\",\n" +
                "  \"searchable\": true,\n" +
                "  \"global\": true,\n" +
                "  \"numerical\": false,\n" +
                "  \"unit\": \"\",\n" +
                "  \"options\": [\"超高清\", \"全高清\", \"高清\"]\n" +
                " }]\n" +
                "}]";

        String str1 = str.substring(str.indexOf("[")+1,str.lastIndexOf("]"));
        System.out.println(str1);
        JSONObject parseObject = JSONArray.parseObject(str1);
        JSONObject obj = parseObject.getJSONObject("params");
    }
}
