package dk.sdu.cbse.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Base game entity. Polygon coordinates are relative to the entity's centre.
 * A negative life value means the entity lives forever; positive counts down in seconds.
 */
public class Entity {

    private final UUID id = UUID.randomUUID();
    private float x, y;
    private float dx, dy;
    private float rotation;   // degrees – 0 = pointing up (screen Y-axis inverted)
    private float radius;
    private boolean active = true;
    private EntityType type;
    private float[] polygon;  // alternating x/y pairs, centred on (0,0)
    private float life = -1f; // seconds remaining; -1 = infinite

    // Per-entity generic float storage (used by control systems for timers etc.)
    private final Map<String, Float> data = new HashMap<>();

    public UUID   getId()                       { return id; }

    public float  getX()                        { return x; }
    public void   setX(float x)                 { this.x = x; }

    public float  getY()                        { return y; }
    public void   setY(float y)                 { this.y = y; }

    public float  getDx()                       { return dx; }
    public void   setDx(float dx)               { this.dx = dx; }

    public float  getDy()                       { return dy; }
    public void   setDy(float dy)               { this.dy = dy; }

    public float  getRotation()                 { return rotation; }
    public void   setRotation(float r)          { this.rotation = r; }

    public float  getRadius()                   { return radius; }
    public void   setRadius(float r)            { this.radius = r; }

    public boolean isActive()                   { return active; }
    public void    setActive(boolean active)    { this.active = active; }

    public EntityType getType()                 { return type; }
    public void       setType(EntityType t)     { this.type = t; }

    public float[] getPolygon()                 { return polygon; }
    public void    setPolygon(float[] polygon)  { this.polygon = polygon; }

    public float getLife()                      { return life; }
    public void  setLife(float life)            { this.life = life; }

    public float getData(String key, float defaultValue) {
        return data.getOrDefault(key, defaultValue);
    }

    public void setData(String key, float value) {
        data.put(key, value);
    }
}
