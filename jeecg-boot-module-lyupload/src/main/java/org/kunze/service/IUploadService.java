package org.kunze.service;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {


    /**
     * 上传图片
     * @param file
     * @return
     */
    String uploadImge(MultipartFile file);

}
