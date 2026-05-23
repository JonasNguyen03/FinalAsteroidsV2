package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;

/**
 * Lifecycle hook for game components.
 * Implementations are discovered via the Java ServiceLoader.
 */
public interface IGamePluginService {
    void start(GameData gameData, World world);
    void stop(GameData gameData, World world);
}
