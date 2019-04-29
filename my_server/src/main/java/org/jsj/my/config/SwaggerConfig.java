package org.jsj.my.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Swagger UI configurations
 *
 * @author JSJ
 */
@Profile("!prod")
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * 不受保护的URL列表
     */
    private static final String[] PUBLIC_URLS = {"/my/public"};

    private static final String PACKAGE = "org.jsj.my";

    @Bean
    public Docket api() {
        Predicate<RequestHandler> filter = input -> {
            String name = input.declaringClass().getPackage().getName();
            return name.startsWith(PACKAGE + ".controller")
                    || name.startsWith(PACKAGE + ".model")
                    || name.startsWith(PACKAGE + ".exception");
        };

        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(filter);

        builder.paths(PathSelectors.ant("/**"));

        Docket docket= builder.build();
        docket.securitySchemes(newArrayList(apiKey()));
        docket.securityContexts(newArrayList(securityContext()));
        docket.produces(newHashSet(MediaType.APPLICATION_JSON_VALUE));
        docket.consumes(newHashSet(MediaType.APPLICATION_JSON_VALUE));
        return docket;
    }

    private ApiKey apiKey() {
        return new ApiKey("token", "token", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(input -> needAuthorize(input))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference("token", authorizationScopes));
    }

    private boolean needAuthorize(String path) {
        for (String pubUrl : PUBLIC_URLS) {
            if(path.startsWith(pubUrl)) {
                return false;
            }
        }
        return true;
    }

    @Bean
    SecurityConfiguration security() {
        return new SecurityConfiguration(
                "dapp-client-id",
                "dapp-client-secret",
                "dapp-realm",
                "dapp",
                "token",
                ApiKeyVehicle.HEADER,
                "api_key",
                ",");
    }

    @Bean
    UiConfiguration uiConfig() {
        return new UiConfiguration(
                null,
                "none",
                "alpha",
                "schema",
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS,
                true,
                true,
                60000L);
    }
}
