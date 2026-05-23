package dk.sdu.cbse.player;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.EntityType;
import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;

public class PlayerControlSystem implements IEntityProcessingService {

    private static final float ROT_SPEED    = 180f;
    private static final float THRUST       = 220f;
    private static final float MAX_SPEED    = 400f;
    private static final float FRICTION     = 0.985f;
    private static final float BULLET_SPEED = 420f;
    private static final float SHOOT_CD     = 0.25f;

    @Override
    public void process(GameData gd, World world) {
        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            float dt = gd.getDeltaTime();

            if (gd.isPressed("LEFT"))
                player.setRotation(player.getRotation() - ROT_SPEED * dt);
            if (gd.isPressed("RIGHT"))
                player.setRotation(player.getRotation() + ROT_SPEED * dt);

            if (gd.isPressed("UP")) {
                double rad = Math.toRadians(player.getRotation() - 90);
                player.setDx(player.getDx() + (float)(Math.cos(rad) * THRUST * dt));
                player.setDy(player.getDy() + (float)(Math.sin(rad) * THRUST * dt));
            }

            player.setDx(player.getDx() * FRICTION);
            player.setDy(player.getDy() * FRICTION);
            float spd = (float) Math.hypot(player.getDx(), player.getDy());
            if (spd > MAX_SPEED) {
                player.setDx(player.getDx() * MAX_SPEED / spd);
                player.setDy(player.getDy() * MAX_SPEED / spd);
            }

            player.setX(player.getX() + player.getDx() * dt);
            player.setY(player.getY() + player.getDy() * dt);
            wrapAround(player, gd);

            float cd = player.getData("shootCd", 0f) - dt;
            player.setData("shootCd", Math.max(cd, 0f));

            if (gd.isPressed("SPACE") && player.getData("shootCd", 0f) <= 0f) {
                shoot(player, world);
                player.setData("shootCd", SHOOT_CD);
            }
        }
    }

    private void shoot(Entity player, World world) {
        double rad = Math.toRadians(player.getRotation() - 90);
        Entity bullet = new Entity();
        bullet.setType(EntityType.BULLET);
        bullet.setX(player.getX() + (float)(Math.cos(rad) * 20));
        bullet.setY(player.getY() + (float)(Math.sin(rad) * 20));
        bullet.setDx(player.getDx() + (float)(Math.cos(rad) * BULLET_SPEED));
        bullet.setDy(player.getDy() + (float)(Math.sin(rad) * BULLET_SPEED));
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
