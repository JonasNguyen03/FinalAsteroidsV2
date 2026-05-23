package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;

public interface IEntityProcessingService {
    void process(GameData gameData, World world);
}
