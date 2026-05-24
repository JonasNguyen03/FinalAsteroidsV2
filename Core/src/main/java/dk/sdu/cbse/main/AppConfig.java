package dk.sdu.cbse.main;

import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "dk.sdu.cbse.player",
        "dk.sdu.cbse.bullet",
        "dk.sdu.cbse.enemy",
        "dk.sdu.cbse.collision",
        "dk.sdu.cbse.main"
})
public class AppConfig {

    private static final float WIDTH  = 800f;
    private static final float HEIGHT = 600f;

    @Bean
    public GameData gameData() {
        return new GameData(WIDTH, HEIGHT);
    }

    @Bean
    public World world() {
        return new World();
    }
}