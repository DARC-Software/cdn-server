package space.devincoopers.cdnserver.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;

@Component
@Profile("!test") // Only activate this initializer outside of test profile
public class UploadDirectoryInitializer {

    @Value("${upload.root:/app/uploads}") // default to /app/uploads
    private String uploadRootPath;

    @PostConstruct
    public void initializeUploadRoot() throws IOException {
        Path rootUploadDir = Paths.get(uploadRootPath);
        if (!Files.exists(rootUploadDir)) {
            Files.createDirectories(rootUploadDir);
            System.out.println("✅ Created root upload directory: " + rootUploadDir.toAbsolutePath());
        } else {
            System.out.println("📁 Root upload directory already exists: " + rootUploadDir.toAbsolutePath());
        }
    }
}