package com.mohan.studentresources.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageService {

    // Use temporary directory for Railway (local file storage won't work in cloud)
    private final Path uploadDir;

    public StorageService() throws IOException {
        // For Railway, we need to use a writable directory
        String uploadPath = System.getenv("UPLOAD_DIR");
        if (uploadPath == null) {
            // Use temp directory in production, local directory in development
            uploadPath = System.getProperty("java.io.tmpdir") + "/uploads";
        }
        
        this.uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
        
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    public String storeFile(MultipartFile file) throws IOException {
        // Generate unique filename to avoid conflicts
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path target = uploadDir.resolve(filename);
        file.transferTo(target);
        
        // Return only filename for Railway compatibility
        // Frontend will need to request files through a dedicated endpoint
        return filename;
    }
    
    // Add method to serve files (important for Railway)
    public byte[] getFileBytes(String filename) throws IOException {
        Path filePath = uploadDir.resolve(filename).normalize();
        if (!filePath.startsWith(uploadDir)) {
            throw new IOException("Invalid file path");
        }
        return Files.readAllBytes(filePath);
    }
    
    // Check if file exists
    public boolean fileExists(String filename) {
        return Files.exists(uploadDir.resolve(filename));
    }
}