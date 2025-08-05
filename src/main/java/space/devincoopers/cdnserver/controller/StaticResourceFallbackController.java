package space.devincoopers.cdnserver.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@ControllerAdvice
public class StaticResourceFallbackController {

    @ExceptionHandler
    public ResponseEntity<Resource> handleStaticResourceNotFound(Exception ex) throws IOException {
        if (ex.getMessage() != null && ex.getMessage().contains("Resource not found")) {
            Resource fallback = new ClassPathResource("/fallback/not-found.png");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(fallback);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}