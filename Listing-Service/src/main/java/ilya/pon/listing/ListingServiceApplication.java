package ilya.pon.listing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class ListingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ListingServiceApplication.class, args);
    }
}