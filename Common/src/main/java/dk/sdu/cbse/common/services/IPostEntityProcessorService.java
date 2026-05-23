package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;

public interface IPostEntityProcessorService {
    void process(GameData gameData, World world);
}