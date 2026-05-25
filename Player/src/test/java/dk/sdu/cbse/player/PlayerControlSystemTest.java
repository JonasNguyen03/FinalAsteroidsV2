package dk.sdu.cbse.player;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.EntityType;
import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerControlSystemTest {

    private PlayerControlSystem controlSystem;
    private GameData            gameData;
    private World               world;
    private Entity              player;

    @Before
    public void setUp() {
        controlSystem = new PlayerControlSystem();

        gameData = Mockito.mock(GameData.class);
        when(gameData.getDisplayWidth()).thenReturn(800f);
        when(gameData.getDisplayHeight()).thenReturn(600f);
        when(gameData.getDeltaTime()).thenReturn(0.016f);

        world  = new World();
        player = new Entity();
        player.setType(EntityType.PLAYER);
        player.setX(400f);
        player.setY(300f);
        player.setRotation(0f);
        player.setActive(true);
        world.addEntity(player);
    }

    @Test
    public void pressingA_rotatesPlayerLeft() {
        when(gameData.isPressed("A")).thenReturn(true);
        when(gameData.isPressed("D")).thenReturn(false);
        when(gameData.isPressed("W")).thenReturn(false);

        float rotationBefore = player.getRotation();
        controlSystem.process(gameData, world);

        assertTrue("A should rotate left (decrease rotation)",
                player.getRotation() < rotationBefore);
    }

    @Test
    public void pressingD_rotatesPlayerRight() {
        when(gameData.isPressed("A")).thenReturn(false);
        when(gameData.isPressed("D")).thenReturn(true);
        when(gameData.isPressed("W")).thenReturn(false);

        float rotationBefore = player.getRotation();
        controlSystem.process(gameData, world);

        assertTrue("D should rotate right (increase rotation)",
                player.getRotation() > rotationBefore);
    }

    @Test
    public void pressingW_movesPlayerForward() {
        when(gameData.isPressed("A")).thenReturn(false);
        when(gameData.isPressed("D")).thenReturn(false);
        when(gameData.isPressed("W")).thenReturn(true);
    
        float xBefore = player.getX();
        float yBefore = player.getY();
    
        controlSystem.process(gameData, world);
    
        boolean moved = player.getX() != xBefore || player.getY() != yBefore;
        assertTrue("W should thrust the player (position should change)", moved);
    }

    @Test
    public void noKeysPressed_playerDoesNotMove() {
        when(gameData.isPressed(anyString())).thenReturn(false);

        float xBefore = player.getX();
        float yBefore = player.getY();
        float rBefore = player.getRotation();

        controlSystem.process(gameData, world);

        assertEquals(xBefore, player.getX(), 0.001f);
        assertEquals(yBefore, player.getY(), 0.001f);
        assertEquals(rBefore, player.getRotation(), 0.001f);
    }
}