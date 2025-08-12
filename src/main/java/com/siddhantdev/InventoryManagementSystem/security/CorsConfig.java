package com.siddhantdev.InventoryManagementSystem.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer mvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        /// here
                        .allowedOrigins("*");
            }
        };

        ///  I'm going to allow everything from our origin.
        /// So in production we might probably specify the origin we want, maybe our production URL or our front end url
        /// can specify only one access from our particular front end URL.
        /// So that's going to add more security to our back end.
        /// in production we're going to change this to our front end URL.
    }
}
