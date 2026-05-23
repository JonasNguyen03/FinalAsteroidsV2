package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;

/**
 * Service interface for systems that update entity state each game tick.
 *
 * Discovered at runtime via {java.util.ServiceLoader}.
 */
public interface IEntityProcessingService {

    /**
     * Updates the state of relevant entities for the current game tick.
     *
     * Preconditions:
     *   {gameData} is not {null}
     *   {gameData.getDeltaTime()} is greater than {0} and
     *   {world} is not {null}
     *   The world's entity collection is not being modified concurrently
     *
     * Postconditions:
     *   All entities relevant to this processor have had their state
     *       updated (position, rotation, timers, etc.)
     *   Entities that have expired (e.g. bullets past their lifetime)
     *       have been removed from {world}
     *   Any newly spawned entities (e.g. bullets fired this frame)
     *       have been added to {world} with valid type, position,
     *       velocity, radius, and lifetime
     *   Entities not managed by this processor are not modified
     *   {gameData} is not modified
     *
     * @param gameData shared game state containing delta time and display dimensions
     * @param world    the game world whose entities are read and updated
     */    
    void process(GameData gameData, World world);
}
