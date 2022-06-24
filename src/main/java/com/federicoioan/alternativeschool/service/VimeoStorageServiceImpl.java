package com.federicoioan.alternativeschool.service;

import com.federicoioan.alternativeschool.service.IService.VimeoStorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class VimeoStorageServiceImpl implements VimeoStorageService {


    private Path root = Paths.get("uploadsVideo");

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public File save(MultipartFile file, String subDirectory) {
        try {
            String filename = file.getOriginalFilename();

            if (filename == null || filename.isEmpty())
                throw new RuntimeException("Filename is null or empty!");

            // Create the subdirectory if it does not exist
            Path folder = Files.createDirectory(Path.of(subDirectory));

            // Save the file to the subdirectory
            Files.copy(file.getInputStream(), folder.resolve(filename));

            // Return the saved file
            return new File(folder.toFile().getAbsolutePath() + "/" + filename);

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll(String subDirectory) {
        try {
            Path subFolder = Paths.get(subDirectory);
            FileSystemUtils.deleteRecursively(subFolder.toFile());
        } catch (Exception e) {
            throw new RuntimeException("Could not delete the file. Error: " + e.getMessage());
        }
    }
}