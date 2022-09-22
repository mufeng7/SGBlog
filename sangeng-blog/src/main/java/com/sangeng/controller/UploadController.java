package com.sangeng.controller;


import com.sangeng.domain.ResponseResult;
import com.sangeng.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;
    @PostMapping("/upload")
    public ResponseResult uploadImag(MultipartFile img){
        return uploadService.uploadImg(img);
    }

}
