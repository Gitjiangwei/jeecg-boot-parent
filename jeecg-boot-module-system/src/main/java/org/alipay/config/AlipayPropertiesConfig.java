package org.alipay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.alipay.paysdk.AliPayApiConfig;
import org.alipay.paysdk.AliPayApiConfigKit;
import org.kunze.diansh.entity.properties.WeChatPayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class AlipayPropertiesConfig implements ApplicationListener {

    @Autowired
    private AliPayApiConfig aliPayApiConfig;

    //保存加载配置参数
    private static Map<String, String> aliPropertiesMap = new HashMap<String, String>();

    /*获取配置参数值*/
    public static String getKey(String key) {
        return aliPropertiesMap.get(key);
    }

    /*监听启动完成，执行配置加载到aliPropertiesMap*/
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            this.init(aliPropertiesMap);//应用启动加载
        }
    }

    /*初始化加载aliPropertiesMap*/
    public void init(Map<String, String> map) {
        AliPayApiConfig ApiConfig = aliPayApiConfig.build();
        AliPayApiConfigKit.putApiConfig(ApiConfig);

        // 获得PathMatchingResourcePatternResolver对象
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            //加载resource文件(也可以加载resources)
            Resource resources = resolver.getResource("classpath:alipayConfig/alipay.properties");
            PropertiesFactoryBean config = new PropertiesFactoryBean();
            config.setLocation(resources);
            config.afterPropertiesSet();
            Properties prop = config.getObject();
            //循环遍历所有得键值对并且存入集合
            for (String key : prop.stringPropertyNames()) {
                map.put(key, (String) prop.get(key));
            }
        } catch (Exception e) {
            new Exception("配置文件加载失败");
        }
    }

    /**
     * 支付宝请求客户端入口
     */
    private volatile static AlipayClient alipayClient = null;

    /**
     * 不可实例化
     */
    private AlipayPropertiesConfig(){};

    /**
     * 双重锁单例
     * @return 支付宝请求客户端实例
     */
    public static AlipayClient getInstance(){
        if (alipayClient == null){
            synchronized (AlipayPropertiesConfig.class){
                if (alipayClient == null){
                    alipayClient = new DefaultAlipayClient(
                        AlipayPropertiesConfig.getKey("gatewayUrl"),//支付宝网关
                        AlipayPropertiesConfig.getKey("app_id"),//appid
                        AlipayPropertiesConfig.getKey("app_private_key"),//商户私钥
                        AlipayPropertiesConfig.getKey("format"),
                        AlipayPropertiesConfig.getKey("charset"),//字符编码格式
                        AlipayPropertiesConfig.getKey("alipay_public_key"),//支付宝公钥
                        AlipayPropertiesConfig.getKey("sign_type")//签名方式
                    );
                }
            }
        }
        return alipayClient;
    }

}
