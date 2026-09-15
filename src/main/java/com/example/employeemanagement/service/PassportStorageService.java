package com.example.employeemanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class PassportStorageService {

    private final Path uploadDirectory;

    public PassportStorageService(
            @Value("${app.file.upload-dir}") String uploadDir) {

        this.uploadDirectory = Paths
                .get(uploadDir)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.uploadDirectory);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not create passport upload directory",
                    e
            );
        }
    }

    /**
     * Store passport file on filesystem.
     */
    public String store(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Passport file is required"
            );
        }

        String originalFileName =
                file.getOriginalFilename();

        String extension = "";

        if (originalFileName != null &&
                originalFileName.contains(".")) {

            extension = originalFileName.substring(
                    originalFileName.lastIndexOf(".")
            ).toLowerCase();
        }

        String generatedFileName =
                UUID.randomUUID() + extension;

        try {

            Path targetLocation = uploadDirectory
                    .resolve(generatedFileName)
                    .normalize();

            /*
             * Security check:
             * Make sure the final path stays
             * inside the upload directory.
             */
            if (!targetLocation.startsWith(uploadDirectory)) {
                throw new IllegalArgumentException(
                        "Invalid file path"
                );
            }

            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return generatedFileName;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not store passport file",
                    e
            );
        }
    }

    /**
     * Load passport file from filesystem.
     */
    public Resource load(String fileName) {

        try {

            Path filePath = uploadDirectory
                    .resolve(fileName)
                    .normalize();

            /*
             * Security check:
             * Prevent path traversal attacks.
             */
            if (!filePath.startsWith(uploadDirectory)) {
                throw new IllegalArgumentException(
                        "Invalid passport file path"
                );
            }

            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );

            if (resource.exists() &&
                    resource.isReadable()) {

                return resource;
            }

            throw new RuntimeException(
                    "Passport file not found: " + fileName
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not load passport file: "
                            + fileName,
                    e
            );
        }
    }
}