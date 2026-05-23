package dk.sdu.cbse.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class World {

    private final List<Entity> entities = new CopyOnWriteArrayList<>();

    public void addEntity(Entity entity)    { entities.add(entity); }
    public void removeEntity(Entity entity) { entities.remove(entity); }

    public Collection<Entity> getEntities() { return entities; }

    public Collection<Entity> getEntities(EntityType type) {
        List<Entity> result = new ArrayList<>();
        for (Entity e : entities) {
            if (e.getType() == type && e.isActive()) result.add(e);
        }
        return result;
    }
}
