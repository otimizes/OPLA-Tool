package br.otimizes.oplatool.api.resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
        //.addResourceHandler("/**") // « /css/myStatic.css
            .addResourceHandler("/static/**") // « /static/css/myStatic.css
                .addResourceLocations("classpath:/static/");
    }
}
