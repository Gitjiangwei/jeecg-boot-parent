package org.kunze.diansh.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jeecg.OrderComsumer;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.OrderCodeUtils;
import org.kunze.diansh.WxPayAPI.MiniprogramConfig;
import org.kunze.diansh.WxPayAPI.WXPay;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.mapper.OrderMapper;
import org.kunze.diansh.mapper.SkuMapper;
import org.kunze.diansh.mapper.SpuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping(value = "/kunze/mytest")
public class MyTestController {



    public static void main(String[] args) {


        //InputStream in = MiniprogramConfig.class.getResourceAsStream("/cert/apiclient_cert.p12");
        InputStream in = MiniprogramConfig.class.getResourceAsStream(File.separator+"cert"+File.separator+"apiclient_cert.p12");
        System.out.println("1111111");
        try {
            byte[] certData;
            File tempFile = File.createTempFile("apiclient_cert",".p12");
            FileUtils.copyInputStreamToFile(in,tempFile);

            //获取证书
            //File classPathResource = new ClassPathResource("classpath:"+File.separator+"cert"+File.separator+"apiclient_cert.p12").getFile();

            InputStream certStream = new FileInputStream(tempFile);
            certData = IOUtils.toByteArray(certStream);
            certStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MiniprogramConfig miniprogramConfig;
    private WXPay wxPay;

//    @Autowired
//    public MyTestController(){
//        try {
//            miniprogramConfig = MiniprogramConfig.getInstance();
//            wxPay = new WXPay(miniprogramConfig);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("微信配置初始化错误", e);
//        }
//    }

    @GetMapping(value = "/testInsert")
    public void testInsert() {

        Instant now = Instant.now();
        Date nowDate = Date.from(now);

       String str = DateUtils.date2Str(nowDate,new SimpleDateFormat("yyyyMMddHHmmss"));
       System.out.println(str);



//        Order o = new Order();
//        o.setCreateTime(new Date());
//        o.setCancelTime(OrderCodeUtils.createCancelTime(o.getCreateTime()));
//        o.setOrderId("123456");
//        System.out.println("预计取消时间"+ o.getCancelTime().toString());
//        OrderComsumer.queue.put(o);

        //OrderComsumer.removeToOrderDelayQueue(o.getOrderId());
        //System.out.println("目前队列中有"+OrderComsumer.queue.size()+"个订单！");
        //orderMapper.updateOrderStatus("6","202005191005497106122000001");


//        JSONObject json = JSONObject.parseObject(getJson());
//
//        JSONArray jsonArray = json.getJSONArray("data");
//
//
//        Integer spuFlag = 1000;
//        for(int i=0;i<jsonArray.size();i++){
//            JSONObject object = jsonArray.getJSONObject(i);
//            Spu spu = new Spu();
//            spuFlag++;
//            spu.setId(spuFlag.toString());
//            spu.setTitle(object.get("name").toString());
//            spu.setImage(object.get("main_image").toString());
//            spu.setCid3("877");
//            spu.setCid2("872");
//            spu.setCid1("871");
//            spu.setBrandId("9637");
//            int rows = spuMapper.saveSpu(spu);
//            List<Sku> skuList = new ArrayList<Sku>();
//            if(rows != 0){
//
//                Sku s = new Sku();
//                s.setId(UUID.randomUUID().toString().replace("-",""));
//                s.setSpuId(spu.getId());
//                s.setTitle(object.get("name").toString());
//                s.setImages(object.get("main_image").toString());
//                s.setPrice("1000");
//                s.setNewPrice("2000");
//                s.setEnable("1");
//                skuList.add(s);
//                skuMapper.saveSku(skuList);
//            }
//        }
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
