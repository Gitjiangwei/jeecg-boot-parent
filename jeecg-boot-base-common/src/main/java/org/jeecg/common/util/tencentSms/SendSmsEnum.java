package org.jeecg.common.util.tencentSms;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DySmsEnum;

public enum SendSmsEnum {
    //LOGIN_TEMPLATE_CODE("642066","JEECG","code"),
    //修改密码
    FORGET_PASSWORD_TEMPLATE_CODE("642066","山西乾森网络科技有限公司","code","1400388965"),
    //注册
    REGISTER_TEMPLATE_CODE("642064","山西乾森网络科技有限公司","code","1400388965");

    /**
     * 短信模板编码
     */
    private String templateCode;
    /**
     * 签名
     */
    private String signName;
    /**
     * 短信模板必需的数据名称，多个key以逗号分隔，此处配置作为校验
     */
    private String keys;

    //生成的实际 SDKAppID
    private String sdkAppId;

    private SendSmsEnum(String templateCode,String signName,String keys,String skdAppId) {
        this.templateCode = templateCode;
        this.signName = signName;
        this.keys = keys;
        this.sdkAppId = skdAppId;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getSdkAppId() {
        return sdkAppId;
    }

    public void setSdkAppId(String sdkAppId) {
        this.sdkAppId = sdkAppId;
    }

    public static DySmsEnum toEnum(String templateCode) {
        if(StringUtils.isEmpty(templateCode)){
            return null;
        }
        for(DySmsEnum item : DySmsEnum.values()) {
            if(item.getTemplateCode().equals(templateCode)) {
                return item;
            }
        }
        return null;
    }
}
