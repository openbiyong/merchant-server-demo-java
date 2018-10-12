package com.biyong.open.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@ComponentScan(basePackages = "com.biyong")
@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  // TODO 允许跨域访问，方便调试
  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration c = new CorsConfiguration();
    c.addAllowedOrigin("*");
    c.addAllowedHeader("*");
    c.addAllowedMethod("*");
    UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
    s.registerCorsConfiguration("/**", c);
    return new CorsFilter(s);
  }
}
