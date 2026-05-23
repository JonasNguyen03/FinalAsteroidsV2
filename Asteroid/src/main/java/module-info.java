module Asteroid {
    requires Common;
    provides dk.sdu.cbse.common.services.IGamePluginService
        with dk.sdu.cbse.asteroid.AsteroidPlugin;
    provides dk.sdu.cbse.common.services.IEntityProcessingService
        with dk.sdu.cbse.asteroid.AsteroidControlSystem; 
}