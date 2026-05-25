package dk.sdu.cbse.collision;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.EntityType;
import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class CollisionSystemTest {

    private CollisionSystem collisionSystem;
    private GameData        gameData;
    private World           world;

    @Before
    public void setUp() {
        collisionSystem = new CollisionSystem();
        gameData        = new GameData(800f, 600f);
        world           = new World();
    }

    //helpers

    private Entity makeEntity(EntityType type, float x, float y, float radius) {
        Entity e = new Entity();
        e.setType(type);
        e.setX(x);
        e.setY(y);
        e.setRadius(radius);
        e.setActive(true);
        return e;
    }

    //tests

    @Test
    public void bullet_overlappingAsteroid_removesBoth() {
        Entity bullet   = makeEntity(EntityType.BULLET,   100, 100, 5);
        Entity asteroid = makeEntity(EntityType.ASTEROID, 102, 100, 30);
        world.addEntity(bullet);
        world.addEntity(asteroid);

        collisionSystem.process(gameData, world);

        Collection<Entity> entities = world.getEntities();
        assertFalse("Bullet should be removed", entities.contains(bullet));
        assertFalse("Asteroid should be removed", entities.contains(asteroid));
    }

    @Test
    public void bullet_overlappingEnemy_removesEnemy() {
        Entity bullet = makeEntity(EntityType.BULLET, 200, 200, 5);
        Entity enemy  = makeEntity(EntityType.ENEMY,  202, 200, 20);
        world.addEntity(bullet);
        world.addEntity(enemy);

        collisionSystem.process(gameData, world);

        Collection<Entity> entities = world.getEntities();
        assertFalse("Enemy should be removed",  entities.contains(enemy));
        assertFalse("Bullet should be removed", entities.contains(bullet));
    }

    @Test
    public void asteroid_overlappingPlayer_removesPlayer() {
        Entity asteroid = makeEntity(EntityType.ASTEROID, 300, 300, 30);
        Entity player   = makeEntity(EntityType.PLAYER,   310, 300, 15);
        world.addEntity(asteroid);
        world.addEntity(player);

        collisionSystem.process(gameData, world);

        assertFalse("Player should be removed", world.getEntities().contains(player));
    }

    @Test
    public void enemy_overlappingPlayer_removesBoth() {
        Entity enemy  = makeEntity(EntityType.ENEMY,  400, 400, 20);
        Entity player = makeEntity(EntityType.PLAYER, 405, 400, 15);
        world.addEntity(enemy);
        world.addEntity(player);

        collisionSystem.process(gameData, world);

        Collection<Entity> entities = world.getEntities();
        assertFalse("Enemy should be removed",  entities.contains(enemy));
        assertFalse("Player should be removed", entities.contains(player));
    }

    @Test
    public void bullet_farFromAsteroid_neitherRemoved() {
        Entity bullet   = makeEntity(EntityType.BULLET,   100, 100, 5);
        Entity asteroid = makeEntity(EntityType.ASTEROID, 500, 500, 30);
        world.addEntity(bullet);
        world.addEntity(asteroid);

        collisionSystem.process(gameData, world);

        Collection<Entity> entities = world.getEntities();
        assertTrue("Bullet should survive",   entities.contains(bullet));
        assertTrue("Asteroid should survive", entities.contains(asteroid));
    }

    @Test
    public void asteroid_doesNotCollideWithEnemy() {
        Entity asteroid = makeEntity(EntityType.ASTEROID, 200, 200, 30);
        Entity enemy    = makeEntity(EntityType.ENEMY,    205, 200, 20);
        world.addEntity(asteroid);
        world.addEntity(enemy);

        collisionSystem.process(gameData, world);

        Collection<Entity> entities = world.getEntities();
        assertTrue("Asteroid should survive", entities.contains(asteroid));
        assertTrue("Enemy should survive",    entities.contains(enemy));
    }
}