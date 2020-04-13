package org.kunze.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.kunze.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Api("文件上传接口")
@RestController
@RequestMapping(value = "/upload")
public class UploadController {

    @Autowired
    private IUploadService uploadService;


    @ApiOperation("图片上传")
    @PostMapping(value = "/image")
    public Result<String> upload(@RequestParam(name = "file") MultipartFile file){
        Result<String> result = new Result<String>();
        String resultOk = uploadService.uploadImge(file);
        if(resultOk.equals("ERROR")){
            result.success("图片上传失败!");
        }else if(resultOk.equals("NOT_FILE_TYPE")){
            result.success("图片格式不正确，只支持【png、jecg、jpg】格式的图片！");
        }else if(resultOk.equals("NOT_CONTENT")){
            result.success("上传的文件不是图片！");
        }else{
            result.setResult(resultOk);
        }
        return result;
    }
}

