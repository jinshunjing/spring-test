package org.jsj.my.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Repository bean configurations
 *
 * @author JSJ
 */
@Configuration
@ImportResource(locations={"classpath:/repository-bean.xml"})
public class RepositoryBean {
}
