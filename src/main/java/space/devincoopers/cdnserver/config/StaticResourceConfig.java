package space.devincoopers.cdnserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String[] apps = {"portfolio"};

        for (String app : apps) {
            registry.addResourceHandler("/portfolio/**")
                    .addResourceLocations("file:/app/uploads/portfolio/")
                    .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic());
        }
    }
}
