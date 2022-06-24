package com.federicoioan.alternativeschool.service;

import com.federicoioan.alternativeschool.service.IService.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class StorageServiceImpl implements StorageService {

    private final Path root = Paths.get("uploads");

    @Override
    public void StorageService() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectory(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file, String filename) {
        try {
            Files.copy(file.getInputStream(), root.resolve(filename));

        } catch (NullPointerException | IOException e) {
            throw new RuntimeException("Could not store the file!");
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Files.delete(root.resolve(filename));

        } catch (IOException e) {
            throw new RuntimeException("Could not delete the file");
        }
    }

    @Override
    public void deleteAll() {
        try {
            FileSystemUtils.deleteRecursively(root.toFile());

        } catch (Exception e) {
            throw new RuntimeException("Could not delete the files");
        }
    }
}