package space.devincoopers.cdnserver.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class StaticResourceLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(StaticResourceLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            String path = httpRequest.getRequestURI();
            logger.info("🌀 Static resource requested: {}", path);
        }
        chain.doFilter(request, response);
    }
}