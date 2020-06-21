package org.kunze.diansh.WxPayAPI;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jeecg.common.util.MD5Util;
import org.springframework.util.Base64Utils;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 *
 * 微信退款解密工具类
 *
 * (PKCS7Padding 在java底层存在限制,需要更换java底层jar包)
 * DJK 8下载地址：http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
 * 下载后解压，可以看到local_policy.jar和US_export_policy.jar以及readme.txt。
 * 如果安装了JRE，将两个jar文件放到%JRE_HOME%\lib\security\policy\limite下覆盖原来文件，记得先备份。
 * 如果安装了JDK，将两个jar文件也放到%JDK_HOME%\jre\lib\security\policy\limite下。
 *
 * @Author Backlight
 */
public class WxPayDecodeUtil {

    public static boolean initialized = false;

    private static final String ALGORITHM = "AES";

    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS7Padding";


    //退款结果解密
    public static String decryptData(String base64Data) throws Exception {
        initialize();
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING,"BC");
        SecretKeySpec keySpec = new SecretKeySpec(MD5Util.MD5Encode(MiniprogramConfig.getInstance().getKey(), "UTF-8").toLowerCase().getBytes(), ALGORITHM); //生成加密解密需要的Key
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return new String(cipher.doFinal(Base64Utils.decode(base64Data.getBytes())));
    }

    public static void initialize(){
        if (initialized) return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

}
