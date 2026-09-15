package com.example.employeemanagement.validation;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class PassportFileValidator {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png"
    );

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "pdf",
            "jpg",
            "jpeg",
            "png"
    );

    private PassportFileValidator() {
        // Utility class
    }

    public static String validate(MultipartFile file) {

        // File is required
        if (file == null || file.isEmpty()) {
            return "Passport file is required.";
        }

        // File size validation
        if (file.getSize() > MAX_FILE_SIZE) {
            return "Passport file size must not exceed 5 MB.";
        }

        // Original filename validation
        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null ||
                originalFileName.isBlank()) {

            return "Invalid passport file name.";
        }

        // Get extension
        String extension = getExtension(originalFileName);

        if (extension == null ||
                !ALLOWED_EXTENSIONS.contains(
                        extension.toLowerCase())) {

            return "Only PDF, JPG, JPEG and PNG files are allowed.";
        }

        // MIME type validation
        String contentType = file.getContentType();

        if (contentType == null ||
                !ALLOWED_CONTENT_TYPES.contains(
                        contentType.toLowerCase())) {

            return "Invalid passport file type.";
        }

        // Extension and MIME type must match
        if (!isMatchingType(extension, contentType)) {

            return "Passport file extension does not match its file type.";
        }

        return null;
    }

    private static String getExtension(String fileName) {

        int lastDot =
                fileName.lastIndexOf('.');

        if (lastDot == -1 ||
                lastDot == fileName.length() - 1) {

            return null;
        }

        return fileName
                .substring(lastDot + 1)
                .toLowerCase();
    }

    private static boolean isMatchingType(
            String extension,
            String contentType) {

        extension = extension.toLowerCase();
        contentType = contentType.toLowerCase();

        return switch (extension) {

            case "pdf" ->
                    contentType.equals("application/pdf");

            case "jpg", "jpeg" ->
                    contentType.equals("image/jpeg");

            case "png" ->
                    contentType.equals("image/png");

            default -> false;
        };
    }
}
