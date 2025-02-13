package com.qeema.aps.common.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

import com.qeema.aps.common.utils.LoggingInterceptor;

@Configuration
public class ApplicationConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // .allowedOrigins("http://localhost:4200") // Replace with your frontend domain
                        // .allowedOrigins("https://sbcheckout.payfort.com/FortAPI/*")
                        // .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // .allowedHeaders("Content-Type", "Authorization")
                        .allowedOrigins("*").allowedMethods("*").allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Add custom message converters if needed
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        converters.add(new MappingJackson2HttpMessageConverter());

        // Ensure there are no null converters
        converters.removeIf(converter -> converter == null);

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new LoggingInterceptor());
        restTemplate.setInterceptors(interceptors);
        restTemplate.setMessageConverters(converters);

        return restTemplate;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

}