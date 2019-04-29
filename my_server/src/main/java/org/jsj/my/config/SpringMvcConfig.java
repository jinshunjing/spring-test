package org.jsj.my.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsj.my.auth.AddrAuthInterceptor;
import org.jsj.my.spring.JsonMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Spring MVC configurations
 *
 * @author JSJ
 */
@Configuration
public class SpringMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 受保护和不受保护的URL列表
     */
    private static final String[] PRIVATE_URLS = {"/my/**"};
    private static final String[] PUBLIC_URLS = {"/my/public/**"};

    @Bean
    AddrAuthInterceptor authInterceptor() {
        return new AddrAuthInterceptor();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor())
                .addPathPatterns(PRIVATE_URLS)
                .excludePathPatterns(PUBLIC_URLS);
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper mapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            }
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决静态资源无法访问
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler(new String[]{"/res/**"})
                .addResourceLocations(new String[]{"classpath:/res/"})
                .setCacheControl(CacheControl.maxAge(8L, TimeUnit.HOURS).cachePublic()).setCachePeriod(Integer.valueOf(2000000));

        // 解决swagger无法访问
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);

        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(0);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new JsonMethodArgumentResolver());
    }

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }
}
