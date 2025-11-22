package com.publicservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableConfigurationProperties
public class IssueReportingPlatformApplication {

    @Value("${file.upload-dir:uploads/}")
    private String uploadDir;

    public static void main(String[] args) {
        SpringApplication.run(IssueReportingPlatformApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        String uploadPath = uploadDir.endsWith("/") ? uploadDir : uploadDir + "/";
        String fileLocation = uploadPath.startsWith("/") ? "file:" + uploadPath : "file:" + uploadPath;
        
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations(fileLocation);
            }
        };
    }
}
