package ilya.pon.search.configuration;
import ilya.pon.search.data.properties.ElasticProperties;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
@AllArgsConstructor
public class ElasticConfig extends ElasticsearchConfiguration{
    private final ElasticProperties properties;

    @Override
    public @NonNull ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(properties.getUrl())
                .build();
    }
}