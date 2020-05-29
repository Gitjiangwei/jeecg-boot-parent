package org.kunze.diansh.WxPayAPI;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.apache.commons.io.IOUtils;
import org.kunze.diansh.entity.properties.WeChatPayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Component
public class MiniprogramConfig extends WXPayConfig {


    private static WeChatPayProperties weChatPayProperties;

    @Autowired
    public void setWeChatPayProperties(WeChatPayProperties weChatPayProperties) {
        MiniprogramConfig.weChatPayProperties = weChatPayProperties;
    }

    private static MiniprogramConfig INSTANCE;

    private byte[] certData;

    public MiniprogramConfig() throws Exception {

        InputStream in = MiniprogramConfig.class.getResourceAsStream("/cert/apiclient_cert.p12");
        File tempFile = File.createTempFile("apiclient_cert",".p12");
        FileUtils.copyInputStreamToFile(in,tempFile);

        //获取证书
        //File classPathResource = new ClassPathResource("classpath:"+File.separator+"cert"+File.separator+"apiclient_cert.p12").getFile();

        InputStream certStream = new FileInputStream(tempFile);
        this.certData = IOUtils.toByteArray(certStream);
        certStream.close();
    }

    public static MiniprogramConfig getInstance() throws Exception {
        if (INSTANCE == null) {
            synchronized (MiniprogramConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MiniprogramConfig();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    String getAppID() {
        return weChatPayProperties.getWxAppAppId();
    }

    @Override
    String getMchID() {
        return weChatPayProperties.getMchId();
    }

    @Override
    String getKey() {
        return weChatPayProperties.getApiKey();
    }

    @Override
    InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        WXPayDomainImpl iwxPayDomain = new WXPayDomainImpl();
        return iwxPayDomain;
    }
}
