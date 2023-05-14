package documents.app;

import documents.rest.config.RestConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({RestConfig.class})
public class ApplicationConfiguration {
}
