package ilya.pon.listing.config.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.listing-service")
@Data
public class GenerationProperties {
    private String categoriesPath;
}