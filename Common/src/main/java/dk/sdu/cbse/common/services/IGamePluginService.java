package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;

/**
 * Service interface for game plugins that manage lifecyle of a component
 */
public interface IGamePluginService {

    /**
     * Called once when the game starts. The implementation creates and adds entities into the world.
     * 
     * Preconditions:
     * {gameData} is not {null}
     * {gameData.getDisplayWidth()} and {gameData.getDisplayHeight()}
     *       return valid positive values
     *   {world} is not {null}
     * 
     *
     * Postconditions:
     * One or more entities managed by this plugin have been added to {world}
     *   Each added entity has a valid {dk.sdu.cbse.common.EntityType},
     *       a position within display bounds, and a positive radius
     *   {gameData} is not modified
     * 
     * @param gameData shared game state containing display dimensions and timing
     * @param world    the game world that entities are added to
     */
    void start(GameData gameData, World world);
    
    
    /**
     * Called once when the game stops. The implementation should remove all entities from the world.
     * Preconditions:
     *   {gameData} is not {null}
     *   {world} is not {null}
     *   {start(GameData, World)} has previously been called
     *
     * Postconditions:
     *   All entities created by this plugin have been removed from {world}
     *   No entities belonging to other plugins have been removed
     *   {gameData} is not modified
     * @param gameData shared game state
     * @param world    the game world that entities are removed from
     */      
      
    void stop(GameData gameData, World world);
}
