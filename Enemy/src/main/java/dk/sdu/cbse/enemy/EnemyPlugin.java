package dk.sdu.cbse.enemy;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.EntityType;
import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.services.IGamePluginService;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EnemyPlugin implements IGamePluginService {

    private static final int INITIAL_ENEMIES = 2;
    private final Random rng = new Random();

    @Override
    public void start(GameData gd, World world) {
        for (int i = 0; i < INITIAL_ENEMIES; i++) {
            world.addEntity(spawnEnemy(gd));
        }
    }

    public Entity spawnEnemy(GameData gd) {
        Entity enemy = new Entity();
        enemy.setType(EntityType.ENEMY);

        int edge = rng.nextInt(4);
        float x = switch (edge) {
            case 0, 2 -> rng.nextFloat() * gd.getDisplayWidth();
            case 1    -> gd.getDisplayWidth();
            default   -> 0f;
        };
        float y = switch (edge) {
            case 0    -> 0f;
            case 2    -> gd.getDisplayHeight();
            default   -> rng.nextFloat() * gd.getDisplayHeight();
        };
        enemy.setX(x);
        enemy.setY(y);
        enemy.setRadius(15);
        enemy.setPolygon(new float[]{ 0, 16, 13, -10, -13, -10 });

        float angle = (float)(rng.nextDouble() * 2 * Math.PI);
        float speed = 60 + rng.nextFloat() * 50;
        enemy.setDx((float)(Math.cos(angle) * speed));
        enemy.setDy((float)(Math.sin(angle) * speed));

        return enemy;
    }

    @Override
    public void stop(GameData gd, World world) {
        world.getEntities(EntityType.ENEMY).forEach(world::removeEntity);
    }
}
