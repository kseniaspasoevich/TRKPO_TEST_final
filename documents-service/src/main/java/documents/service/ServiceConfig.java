package documents.service;

import documents.jpa.JpaConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "documents.service")
@Import({JpaConfig.class})
public class ServiceConfig {
}
