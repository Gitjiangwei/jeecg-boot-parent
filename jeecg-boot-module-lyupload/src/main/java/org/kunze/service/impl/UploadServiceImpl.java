package org.kunze.service.impl;

import com.google.api.client.util.Value;
import lombok.extern.slf4j.Slf4j;
import org.kunze.service.IUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.management.BufferPoolMXBean;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UploadServiceImpl implements IUploadService {

    @Value("${uploadpath}")
    private String uploadPath;

    private static final List<String> ALL_TYPES = Arrays.asList("image/png","image/jpeg","image/jpg");

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @Override
    public String uploadImge(MultipartFile file) {
        String result = "ERROR";

        try {
            //1、图片信息校验
            // 1）校验文件类型
            String contentType = file.getContentType();
            if(!ALL_TYPES.contains(contentType)){
                return "NOT_FILE_TYPE";
            }
            //2)、校验图片类型
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null){
                return "NOT_CONTENT";
            }

            //2、保持图片
            //准备目标路径
            File desc = new File(uploadPath,file.getOriginalFilename());
            //保存文件到本地
            file.transferTo(desc);
            return "http://image.kunze.com/"+file.getOriginalFilename();
        }catch (Exception e){
            log.error("上传失败："+e);
            return result;
        }

    }
}
