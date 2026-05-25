package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.EntityType;
import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.services.IGamePluginService;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AsteroidPlugin implements IGamePluginService {

    static final float LARGE_RADIUS  = 40f;
    private static final int INITIAL_COUNT = 3;

    @Override
    public void start(GameData gameData, World world) {
        for (int i = 0; i < INITIAL_COUNT; i++) {
            world.addEntity(createAsteroid(gameData, LARGE_RADIUS));
        }
    }

    static Entity createAsteroid(GameData gd, float radius) {
        Random rnd = new Random();
        float x, y;
        do {
            x = rnd.nextFloat() * gd.getDisplayWidth();
            y = rnd.nextFloat() * gd.getDisplayHeight();
        } while (Math.hypot(x - gd.getDisplayWidth() / 2f,
                            y - gd.getDisplayHeight() / 2f) < 150);

        float  speed = 20f + rnd.nextFloat() * 30f;
        double angle = rnd.nextDouble() * Math.PI * 2;

        Entity a = new Entity();
        a.setType(EntityType.ASTEROID);
        a.setRadius(radius);
        a.setX(x);
        a.setY(y);
        a.setDx((float)(Math.cos(angle) * speed));
        a.setDy((float)(Math.sin(angle) * speed));
        a.setRotation(rnd.nextFloat() * 360f);
        a.setPolygon(makePolygon(radius, rnd));
        return a;
    }

    static float[] makePolygon(float radius, Random rnd) {
        int sides = 8;
        float[] poly = new float[sides * 2];
        for (int i = 0; i < sides; i++) {
            double theta   = 2 * Math.PI * i / sides;
            float  r       = radius * (0.70f + rnd.nextFloat() * 0.40f);
            poly[i * 2]     = (float)(Math.cos(theta) * r);
            poly[i * 2 + 1] = (float)(Math.sin(theta) * r);
        }
        return poly;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.getEntities(EntityType.ASTEROID).forEach(world::removeEntity);
    }
}
