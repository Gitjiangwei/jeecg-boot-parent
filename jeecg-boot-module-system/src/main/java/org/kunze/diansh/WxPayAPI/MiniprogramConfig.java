package org.kunze.diansh.WxPayAPI;

import org.apache.commons.io.IOUtils;
import org.kunze.diansh.entity.properties.WeChatPayProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MiniprogramConfig extends WXPayConfig {

    @Autowired
    private WeChatPayProperties weChatPayProperties;

    private byte[] certData;

    public MiniprogramConfig() throws Exception {
        InputStream certStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("cert/miniProgram/apiclient_cert.p12");
        this.certData = IOUtils.toByteArray(certStream);
        certStream.close();
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
