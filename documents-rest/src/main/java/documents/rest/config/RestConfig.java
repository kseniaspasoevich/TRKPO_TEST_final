package documents.rest.config;


import documents.service.ServiceConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("documents.rest")
@EnableAutoConfiguration
@Import({ServiceConfig.class})
public class RestConfig {

}
