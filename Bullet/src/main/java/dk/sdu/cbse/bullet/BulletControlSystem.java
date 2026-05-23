package dk.sdu.cbse.bullet;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.EntityType;
import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;

import java.util.ArrayList;
import java.util.List;

public class BulletControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gd, World world) {
        float dt = gd.getDeltaTime();
        List<Entity> expired = new ArrayList<>();

        for (Entity bullet : world.getEntities()) {
            if (bullet.getType() != EntityType.BULLET &&
                bullet.getType() != EntityType.ENEMY_BULLET) continue;

            float life = bullet.getLife();
            if (life > 0) {
                life -= dt;
                bullet.setLife(life);
                if (life <= 0f) { expired.add(bullet); continue; }
            }

            bullet.setX(bullet.getX() + bullet.getDx() * dt);
            bullet.setY(bullet.getY() + bullet.getDy() * dt);

            float margin = 20f;
            if (bullet.getX() < -margin || bullet.getX() > gd.getDisplayWidth()  + margin ||
                bullet.getY() < -margin || bullet.getY() > gd.getDisplayHeight() + margin) {
                expired.add(bullet);
            }
        }

        expired.forEach(world::removeEntity);
    }
}
