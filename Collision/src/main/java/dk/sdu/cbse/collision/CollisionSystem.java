package dk.sdu.cbse.collision;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.EntityType;
import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.services.IPostEntityProcessorService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CollisionSystem implements IPostEntityProcessorService {

    @Override
    public void process(GameData gd, World world) {
        List<Entity> bullets   = new ArrayList<>();
        List<Entity> enemyBullets = new ArrayList<>();
        List<Entity> asteroids = new ArrayList<>();
        List<Entity> players   = new ArrayList<>();
        List<Entity> enemies   = new ArrayList<>();

        for (Entity e : world.getEntities()) {
            switch (e.getType()) {
                case BULLET   -> bullets.add(e);
                case ENEMY_BULLET -> enemyBullets.add(e);
                case ASTEROID -> asteroids.add(e);
                case PLAYER   -> players.add(e);
                case ENEMY    -> enemies.add(e);
            }
        }

        List<Entity> toRemove = new ArrayList<>();
        List<Entity> toAdd    = new ArrayList<>();

        //Bullet vs Asteroid
        for (Entity bullet : bullets) {
            if (toRemove.contains(bullet)) continue;
            for (Entity asteroid : asteroids) {
                if (toRemove.contains(asteroid)) continue;
                if (collides(bullet, asteroid)) {
                    toRemove.add(bullet);
                    toRemove.add(asteroid);
                    spawnChildren(asteroid, toAdd);
                    break;
                }
            }
        }

        //Bullet vs Enemy
        for (Entity bullet : bullets) {
            if (toRemove.contains(bullet)) continue;
            for (Entity enemy : enemies) {
                if (toRemove.contains(enemy)) continue;
                if (collides(bullet, enemy)) {
                    toRemove.add(bullet);
                    toRemove.add(enemy);
                    break;
                }
            }
        }
        
        // Enemy Bullet vs Player
        for (Entity bullet : enemyBullets) {
            if (toRemove.contains(bullet)) continue;
            for (Entity player : players) {
                if (toRemove.contains(player)) continue;
                if (collides(bullet, player)) {
                    toRemove.add(bullet);
                    toRemove.add(player);
                    break;
                }
            }
        }

        //Enemy vs Player
        for (Entity enemy : enemies) {
            if (toRemove.contains(enemy)) continue;
            for (Entity player : players) {
                if (toRemove.contains(player)) continue;
                if (collides(enemy, player)) {
                    toRemove.add(enemy);
                    toRemove.add(player);
                }
            }
        }

        //Asteroid vs Player only
        for (Entity asteroid : asteroids) {
            if (toRemove.contains(asteroid)) continue;
            for (Entity player : players) {
                if (toRemove.contains(player)) continue;
                if (collides(asteroid, player)) {
                    toRemove.add(player);
                }
            }

        }

        toRemove.forEach(world::removeEntity);
        toAdd.forEach(world::addEntity);
    }

    private boolean collides(Entity a, Entity b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist < (a.getRadius() + b.getRadius());
    }

    private void spawnChildren(Entity parent, List<Entity> toAdd) {
        float childRadius = parent.getRadius() / 2f;
        if (childRadius < 10) return; // too small — destroy without splitting

        for (int i = 0; i < 2; i++) {
            Entity child = new Entity();
            child.setType(EntityType.ASTEROID);
            child.setRadius(childRadius);
            child.setX(parent.getX() + (i == 0 ? -childRadius : childRadius));
            child.setY(parent.getY() + (i == 0 ? -childRadius : childRadius));

            double angle = Math.random() * Math.PI * 2;
            float speed = 60 + (float)(Math.random() * 40);
            child.setDx((float)(Math.cos(angle) * speed));
            child.setDy((float)(Math.sin(angle) * speed));
            child.setRotation((float)(Math.random() * 360));

            // Single float[] with alternating x/y pairs
            int sides = 8;
            float[] poly = new float[sides * 2];
            for (int s = 0; s < sides; s++) {
                double a2 = 2 * Math.PI * s / sides;
                float r = childRadius * (0.75f + (float) Math.random() * 0.25f);
                poly[s * 2]     = (float) Math.cos(a2) * r;  // x
                poly[s * 2 + 1] = (float) Math.sin(a2) * r;  // y
            }
            child.setPolygon(poly);
            toAdd.add(child);
        }
    }
}