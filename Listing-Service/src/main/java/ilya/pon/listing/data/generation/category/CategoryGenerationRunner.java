package ilya.pon.listing.data.generation.category;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CategoryGenerationRunner implements CommandLineRunner {
    //Push all categories from resources file. For running categories table has to be empty.
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
