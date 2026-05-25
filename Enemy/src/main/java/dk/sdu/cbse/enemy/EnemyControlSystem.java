package dk.sdu.cbse.enemy;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.EntityType;
import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EnemyControlSystem implements IEntityProcessingService {

    private static final float SHOOT_INTERVAL  = 2.0f;
    private static final float DIR_INTERVAL    = 1.5f;
    private static final float ENEMY_SPEED     = 50f;
    private static final float BULLET_SPEED    = 150f;

    private final Random rng = new Random();

    @Override
    public void process(GameData gd, World world) {
        float dt = gd.getDeltaTime();

        for (Entity enemy : world.getEntities(EntityType.ENEMY)) {
            float dirTimer = enemy.getData("dirTimer", 0f) + dt;
            if (dirTimer >= DIR_INTERVAL) {
                dirTimer = 0f;
                changeDirection(enemy);
            }
            enemy.setData("dirTimer", dirTimer);

            float shootTimer = enemy.getData("shootTimer", 0f) + dt;
            if (shootTimer >= SHOOT_INTERVAL) {
                shootTimer = 0f;
                shoot(enemy, world);
            }
            enemy.setData("shootTimer", shootTimer);

            enemy.setX(enemy.getX() + enemy.getDx() * dt);
            enemy.setY(enemy.getY() + enemy.getDy() * dt);
            wrapAround(enemy, gd);
        }
    }

    private void changeDirection(Entity enemy) {
        float angle = (float)(rng.nextDouble() * 2 * Math.PI);
        float speed = ENEMY_SPEED + rng.nextFloat() * 50;
        enemy.setDx((float)(Math.cos(angle) * speed));
        enemy.setDy((float)(Math.sin(angle) * speed));
        enemy.setRotation((float) Math.toDegrees(angle) + 90f);
    }

    private void shoot(Entity enemy, World world) {
        float angle = (float)(rng.nextDouble() * 2 * Math.PI);
        Entity bullet = new Entity();
        bullet.setType(EntityType.ENEMY_BULLET);
        bullet.setX(enemy.getX());
        bullet.setY(enemy.getY());
        bullet.setDx((float)(Math.cos(angle) * BULLET_SPEED));
        bullet.setDy((float)(Math.sin(angle) * BULLET_SPEED));
        bullet.setRadius(3f);
        bullet.setLife(1.5f);
        world.addEntity(bullet);
    }

    private void wrapAround(Entity e, GameData gd) {
        if (e.getX() < 0)                   e.setX(gd.getDisplayWidth());
        if (e.getX() > gd.getDisplayWidth()) e.setX(0);
        if (e.getY() < 0)                   e.setY(gd.getDisplayHeight());
        if (e.getY() > gd.getDisplayHeight()) e.setY(0);
    }
}
