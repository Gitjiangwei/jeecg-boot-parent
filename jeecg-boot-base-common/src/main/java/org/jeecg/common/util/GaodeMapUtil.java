package org.jeecg.common.util;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GaodeMapUtil {

    public static String getLngLat(String address) {
        StringBuffer json = new StringBuffer();
        try {

            URL u = new URL("https://restapi.amap.com/v3/geocode/geo?address=" + address + "&output=JSON&key=d950601606f1e1bef2913a3708f8ec02");
            //URL u = new URL("http://restapi.amap.com/v3/geocode/geo?address=" + address + "&output=JSON&key=d950601606f1e1bef2913a3708f8ec02");
            URLConnection yc = u.openConnection();
            //读取返回的数据
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
            String inputline = null;
            while ((inputline = in.readLine()) != null) {
                json.append(inputline);
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonStr = json.toString();
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);

        //判断输入的位置点是否存在
        if (jsonObject.getJSONArray("geocodes").size() > 0)
        {
            return jsonObject.getJSONArray("geocodes").getJSONObject(0).get("location").toString();
        }

        else {
            return null;
        }

    }


    public static void main(String[] args)  {
        GaodeMapUtil addressLngLatExchange=new GaodeMapUtil();
        System.out.println(addressLngLatExchange.getLngLat("山西省长治兜兜便利"));

    }

}
