module Asteroid {
    requires Common;
    requires spring.context;

    provides dk.sdu.cbse.common.services.IEntityProcessingService
        with dk.sdu.cbse.asteroid.AsteroidControlSystem;
    provides dk.sdu.cbse.common.services.IGamePluginService
        with dk.sdu.cbse.asteroid.AsteroidPlugin;

    opens dk.sdu.cbse.asteroid to spring.core, spring.beans, spring.context;
}