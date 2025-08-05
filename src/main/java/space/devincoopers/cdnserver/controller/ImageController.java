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
        return apiKey.equals(request.getHeader("x-api-key"));
    }

    @PostMapping("/{app}")
    public ResponseEntity<String> uploadImage(@PathVariable String app,
                                              @RequestParam("file")MultipartFile file,
                                              HttpServletRequest request) throws IOException {
        logger.info("Incoming request to upload image for app: {}", app);
        if (!isAuthorized(request)) {
            logger.warn("Unauthorized upload attempt from: " + request.getRemoteAddr());
            return ResponseEntity.status(403).build();
        }

        Path appDir = rootDir.resolve(app);
        Files.createDirectories(appDir);

        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path path = appDir.resolve(filename);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // Return CDN path
        return ResponseEntity.ok(app + "/" + filename);
    }

    @DeleteMapping("{app}/{filename:.+}")
    public ResponseEntity<Void> deleteImage(@PathVariable String app,
                                            @PathVariable String filename,
                                            HttpServletRequest request) throws IOException {
        if (!isAuthorized(request)) {
            logger.warn("Unauthorized delete attempt from: " + request.getRemoteAddr());
            return ResponseEntity.status(403).build();
        }

        Path filePath = rootDir.resolve(app).resolve(filename);
        Files.deleteIfExists(filePath);
        return ResponseEntity.noContent().build();
    }
}