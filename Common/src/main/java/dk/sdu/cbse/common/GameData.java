package dk.sdu.cbse.common;

import java.util.HashSet;
import java.util.Set;

public class GameData {

    private final float displayWidth;
    private final float displayHeight;
    private float deltaTime;
    private final Set<String> keys = new HashSet<>();
    private long score = 0;
    private boolean gameOver = false;


    public GameData(float displayWidth, float displayHeight) {
        this.displayWidth  = displayWidth;
        this.displayHeight = displayHeight;
    }

    public float getDisplayWidth()          { return displayWidth; }
    public float getDisplayHeight()         { return displayHeight; }

    public float getDeltaTime()             { return deltaTime; }
    public void  setDeltaTime(float dt)     { this.deltaTime = dt; }

    public Set<String> getKeys()            { return keys; }

    public boolean isPressed(String key)    { return keys.contains(key); }
    public long getScore() { return score; }
    public void setScore(long score) { this.score = score; }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
}
