package documents.jpa;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
//@PropertySource("classpath:persistence.properties")
@ComponentScan("documents.jpa")
@EnableAutoConfiguration
public class JpaConfig {

}

