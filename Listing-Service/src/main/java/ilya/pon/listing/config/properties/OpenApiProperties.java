package ilya.pon.listing.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "open-api.config.url")
@Data
public class OpenApiProperties {
    private String service;
    private String title;
    private String description;
    private String version;
}