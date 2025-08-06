package space.devincoopers.cdnserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("api/images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Value("${api.key}")
    private String apiKey;

    private final Path rootDir = Paths.get("/app/uploads");

    private boolean isAuthorized(HttpServletRequest request) {
        return apiKey.equals(request.getHeader("CDN_API_KEY"));
    }

    @PostMapping("/**")
    public ResponseEntity<String> uploadImage(HttpServletRequest request,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        String fullPath = extractRelativePath(request, "/api/images/");

        logger.info("Incoming request to upload image to: {}", fullPath);
        if (!isAuthorized(request)) {
            logger.warn("Unauthorized upload attempt from: " + request.getRemoteAddr());
            return ResponseEntity.status(403).build();
        }

        Path appDir = rootDir.resolve(fullPath).normalize();
        Files.createDirectories(appDir);

        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path filePath = appDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok(fullPath + "/" + filename);
    }

    @DeleteMapping("/**")
    public ResponseEntity<Void> deleteImage(HttpServletRequest request) throws IOException {
        if (!isAuthorized(request)) {
            logger.warn("Unauthorized delete attempt from: " + request.getRemoteAddr());
            return ResponseEntity.status(403).build();
        }

        String relativePath = extractRelativePath(request, "/api/images/");

        Path filePath = rootDir.resolve(relativePath).normalize();
        Files.deleteIfExists(filePath);

        logger.info("Deleted: {}", filePath.toString());
        return ResponseEntity.noContent().build();
    }

    private String extractRelativePath(HttpServletRequest request, String basePath) {
        String uri = request.getRequestURI(); // e.g. /api/images/portfolio/icon
        return uri.substring(basePath.length()); // returns: portfolio/icon
    }
}