package ru.skillbox.mcpost.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Getter
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    private String path;
    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        path = System.getProperty("java.class.path");
        String slash = "/";
        if (System.getProperty("os.name").startsWith("Windows")) {
            slash = "\\";
        }
        path = path.substring(0, path.indexOf(slash, path.indexOf(appName)));
        String uri = Paths.get(path).toUri().toString();
        registry.addResourceHandler("/photo/**").addResourceLocations(uri + "/photo/");
    }
}
