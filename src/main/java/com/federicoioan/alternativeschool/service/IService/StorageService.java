package com.federicoioan.alternativeschool.service.IService;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface StorageService {

    void StorageService();

    void save(MultipartFile file, String filename);

    Resource load(String filename);

    void delete(String filename);

    void deleteAll();
}
