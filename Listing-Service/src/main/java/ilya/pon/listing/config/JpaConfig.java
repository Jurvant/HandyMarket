package ilya.pon.listing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    // Separated JPA Auditing to allow @WebMvcTest to run without full JPA context.
}