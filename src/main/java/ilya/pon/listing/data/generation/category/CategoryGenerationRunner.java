package ilya.pon.listing.data.generation.category;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CategoryGenerationRunner implements CommandLineRunner {

    private final CategoriesPusher pusher;

    public CategoryGenerationRunner(CategoriesPusher pusher) {
        this.pusher = pusher;
    }

    @Override
    public void run(String... args) throws Exception {
        if(!pusher.checkDatabase()){
            pusher.readAndPush();
        }
    }
}
