package com.federicoioan.alternativeschool.service.IService;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;


public interface VimeoStorageService {

    void init();

    File save(MultipartFile file, String subDirectory);

    void deleteAll(String subDirectory);
}
