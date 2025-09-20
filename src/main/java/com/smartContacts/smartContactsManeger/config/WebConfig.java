package com.smartContacts.smartContactsManeger.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map URL path "/uploads/**" to your F: drive folder
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:F:/Users/Documents/Spring/SpringBoot/uploads/contactsProfilesImgs/");
    }
}