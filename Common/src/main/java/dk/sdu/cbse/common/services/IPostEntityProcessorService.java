package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;

/**
 * Service interface for systems that run after all entity processors gave completed for the current game tick.
 *
 * Discovered at runtime via {java.util.ServiceLoader}.
 */
public interface IPostEntityProcessorService {

    /**
     * Performs cross-entity processing after all entity updates for the
     * current frame are complete.
     *
     *Preconditions:
     *   {gameData} is not {null}
     *   {world} is not {null}
     *   All {process(GameData, World)}
     *       calls for this frame have already completed
     *   The world's entity collection is not being modified concurrently
     *
     * Postconditions:
     *   All collision pairs have been evaluated according to the
     *       collision matrix:
     *       BULLET vs ASTEROID — both removed; two smaller child asteroids spawned if parent radius >= 10
     *       BULLET vs ENEMY — both removed
     *       ENEMY_BULLET vs PLAYER — both removed
     *       ENEMY vs PLAYER — both removed
     *       ASTEROID vs PLAYER — player removed; asteroid preserved
     *       ASTEROID vs ENEMY — no interaction
     *   No entity is removed more than once
     *   Child asteroids spawned this frame have a valid type, radius, position, velocity, and polygon
     *   {gameData} is not modified
     *
     * @param gameData shared game state containing delta time and display dimensions
     * @param world    the game world whose entities are inspected, removed, or added
     */
    void process(GameData gameData, World world);
}