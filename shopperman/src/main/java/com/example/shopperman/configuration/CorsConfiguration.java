package com.example.shopperman.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfiguration implements WebMvcConfigurer {
 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 매핑에 대해 CORS 허용
            .allowedOriginPatterns("*") // 모든 도메인에서 요청 허용
            .allowedMethods("GET", "POST", "PUT", "DELETE") // 요청 허용 HTTP 메서드
            .allowedHeaders("*") // 모든 헤더 허용
            .exposedHeaders("Authorization") // 노출할 헤더 추가
            .allowCredentials(true) // 쿠키를 허용
            .maxAge(3600); // preflight 요청 캐시 시간
    }
}