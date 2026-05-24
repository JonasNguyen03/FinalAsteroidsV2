package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.EntityType;
import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import org.springframework.stereotype.Component;

@Component
public class AsteroidControlSystem implements IEntityProcessingService {

    private static final float ROTATION_SPEED = 25f;

    @Override
    public void process(GameData gd, World world) {
        float dt = gd.getDeltaTime();

        for (Entity a : world.getEntities(EntityType.ASTEROID)) {
            a.setX(a.getX() + a.getDx() * dt);
            a.setY(a.getY() + a.getDy() * dt);
            a.setRotation(a.getRotation() + ROTATION_SPEED * dt);

            float r = a.getRadius();
            float w = gd.getDisplayWidth();
            float h = gd.getDisplayHeight();

            if (a.getX() < -r)  a.setX(w + r);
            if (a.getX() > w+r) a.setX(-r);
            if (a.getY() < -r)  a.setY(h + r);
            if (a.getY() > h+r) a.setY(-r);
        }
    }
}
