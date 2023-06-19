package com.dosth.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 跨域配置
 * @author Zhidong.Guo
 *
 */
@Configuration
public class DosthCrosConfiguration {
	
    @Bean
    CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 配置跨域
        // 允许所有请求头跨域
        configuration.addAllowedHeader("*");
        // 允许所有请求方法跨域
        configuration.addAllowedMethod("*");
        // 允许所有请求来源跨域
        configuration.addAllowedOrigin("*");
        // 允许携带cookie跨域,否则跨域请求会丢失cookie信息
        configuration.setAllowCredentials(true);
        
        source.registerCorsConfiguration("/**", configuration);
        
        return new CorsWebFilter(source);
    }
}