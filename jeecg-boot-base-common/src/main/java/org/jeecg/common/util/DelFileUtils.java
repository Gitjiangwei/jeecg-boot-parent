package org.jeecg.common.util;


import com.google.api.client.util.Value;
import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * 删除原文件工具类
 */
public class DelFileUtils {

    @Value(value = "${jeecg.path.upload}")
    private static String uploadpath;


    /***
     * 删除文件
     * @param path 文件相对路径地址
     * @return
     */
    public static Boolean delFile(String path){
        Boolean isFlag = false;
        if(!StringUtils.isEmpty(path)){
            uploadpath = uploadpath.replace("\\","/");
            path = uploadpath + "/" + path;
            //path是预删除的文件路径
            File file = new File(path);
            if(file.exists()){
                if(file.isFile()){
                    file.delete();
                    isFlag = true;
                }
            }
        }
        return isFlag;
    }


}
