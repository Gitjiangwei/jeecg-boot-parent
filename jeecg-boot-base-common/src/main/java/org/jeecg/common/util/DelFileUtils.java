package org.jeecg.common.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 删除原文件工具类
 */
@Component
public class DelFileUtils {


    private static String uploadpath;


    @Value(value = "${jeecg.path.upload}")
    public void setUploadpath(String uploadpath){
        DelFileUtils.uploadpath = uploadpath;
    }

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
