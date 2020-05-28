package org.kunze.diansh.WxPayAPI;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.apache.commons.io.IOUtils;
import org.kunze.diansh.entity.properties.WeChatPayProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MiniprogramConfig extends WXPayConfig {

    @Autowired
    private WeChatPayProperties weChatPayProperties;

    private static MiniprogramConfig INSTANCE;

    private byte[] certData;

    public MiniprogramConfig() throws Exception {

        InputStream in = MiniprogramConfig.class.getResourceAsStream(File.separator+"cert"+File.separator+"apiclient_cert.p12");
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
        return null;
    }
}
