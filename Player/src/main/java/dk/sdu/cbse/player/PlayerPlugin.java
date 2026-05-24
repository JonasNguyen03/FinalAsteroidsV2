package dk.sdu.cbse.player;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.EntityType;
import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.services.IGamePluginService;
import org.springframework.stereotype.Component;

@Component
public class PlayerPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        world.addEntity(createPlayer(gameData));
    }

    private Entity createPlayer(GameData gd) {
        Entity p = new Entity();
        p.setType(EntityType.PLAYER);
        p.setX(gd.getDisplayWidth()  / 2f);
        p.setY(gd.getDisplayHeight() / 2f);
        p.setRadius(15);
        p.setPolygon(new float[]{ 0, -16, 11, 10, -11, 10 });
        return p;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.getEntities(EntityType.PLAYER).forEach(world::removeEntity);
    }
}
