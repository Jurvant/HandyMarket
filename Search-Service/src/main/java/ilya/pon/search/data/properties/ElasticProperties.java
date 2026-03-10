package ilya.pon.search.data.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.search-service.elastic-search")
@Data
public class ElasticProperties {
    private String url;
}

