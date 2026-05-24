package dk.sdu.cbse.main;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.EntityType;
import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.common.services.IPostEntityProcessorService;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.springframework.stereotype.Component;
import javafx.scene.image.Image;
import java.util.Objects;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@Component
public class Game extends AnimationTimer {

    private final List<IGamePluginService>          plugins;
    private final List<IEntityProcessingService>    processors;
    private final List<IPostEntityProcessorService> postProcessors;
    private final GameData                          gameData;
    private final World                             world;
    private Image                                   playerImage;

    private GraphicsContext gc;
    private long lastNanos = 0;

    // Spring automatically uses this constructor for injection
    public Game(List<IGamePluginService>          plugins,
                List<IEntityProcessingService>    processors,
                List<IPostEntityProcessorService> postProcessors,
                GameData                          gameData,
                World                             world) {

        this.plugins        = new ArrayList<>(plugins);
        this.processors     = new ArrayList<>(processors);
        this.postProcessors = new ArrayList<>(postProcessors);
        this.gameData       = gameData;
        this.world          = world;

        // Asteroid lives in the plugin layer — still loaded via PluginLoader
        ModuleLayer pluginLayer = PluginLoader.loadPlugins("plugins");
        ServiceLoader.load(pluginLayer, IGamePluginService.class)
                .forEach(this.plugins::add);
        ServiceLoader.load(pluginLayer, IEntityProcessingService.class)
                .forEach(this.processors::add);
        ServiceLoader.load(pluginLayer, IPostEntityProcessorService.class)
                .forEach(this.postProcessors::add);
        
        try {
            playerImage = new Image(Objects.requireNonNull(getClass().getModule().getResourceAsStream("spaceship.png"),"spaceship.png not found in module"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load spaceship image", e);
        }


        for (IGamePluginService plugin : this.plugins) {
            plugin.start(gameData, world);
        }
    }

    /** Called by Main after Spring creates the bean, to hand over the canvas context. */
    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    @Override
    public void handle(long now) {
        if (lastNanos == 0) { lastNanos = now; return; }

        float dt = Math.min((now - lastNanos) / 1_000_000_000f, 0.05f);
        lastNanos = now;
        gameData.setDeltaTime(dt);

        for (IEntityProcessingService ps : processors)     ps.process(gameData, world);
        for (IPostEntityProcessorService ps : postProcessors) ps.process(gameData, world);

        render();
    }

    private void render() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gameData.getDisplayWidth(), gameData.getDisplayHeight());
        for (Entity e : world.getEntities()) {
            if (e.isActive()) drawEntity(e);
        }
        drawHUD();
    }

    private void drawEntity(Entity e) {
        gc.save();
        gc.translate(e.getX(), e.getY());
        gc.rotate(e.getRotation());
    
        float r = e.getRadius();
    
        if (e.getType() == EntityType.PLAYER) {
            // PNG points upward; -90° aligns nose with game's 0° = facing right
            gc.rotate(0);
            gc.drawImage(playerImage, -r, -r, r * 2, r * 2);
    
        } else if (e.getType() == EntityType.BULLET || e.getType() == EntityType.ENEMY_BULLET) {
            gc.setFill(e.getType() == EntityType.BULLET ? Color.GREEN : Color.RED);
            gc.fillOval(-r, -r, r * 2, r * 2);
    
        } else if (e.getPolygon() != null) {
            Color color = switch (e.getType()) {
                case ENEMY    -> Color.RED;
                case ASTEROID -> Color.LIGHTGRAY;
                default       -> Color.WHITE;
            };
            float[] poly = e.getPolygon();
            int n = poly.length / 2;
            double[] xs = new double[n];
            double[] ys = new double[n];
            for (int i = 0; i < n; i++) { xs[i] = poly[i * 2]; ys[i] = poly[i * 2 + 1]; }
            gc.setStroke(color);
            gc.setLineWidth(2);
            gc.strokePolygon(xs, ys, n);
        }
    
        gc.restore();
    }

    private void drawHUD() {
        gc.setFill(Color.LIGHTGRAY);
        gc.setFont(Font.font("Monospaced", 13));
        gc.fillText("A/D Rotate   W Thrust   SPACE Shoot", 10, 20);
    }
}