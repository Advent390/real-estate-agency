package com.metrazh.agency.config;

import com.metrazh.agency.interceptor.AdminAuthInterceptor;
import com.metrazh.agency.interceptor.ClientAuthInterceptor;
import com.metrazh.agency.util.FlashService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final FlashService flashService;

    public WebConfig(FlashService flashService) {
        this.flashService = flashService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // @client_required: /cabinet, /object/{id}/viewing, /object/{id}/favorite
        registry.addInterceptor(new ClientAuthInterceptor(flashService))
                .addPathPatterns("/cabinet", "/object/*/viewing", "/object/*/favorite");

        // @admin_required: усе під /admin, крім /admin/login
        registry.addInterceptor(new AdminAuthInterceptor(flashService))
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");
    }
}
