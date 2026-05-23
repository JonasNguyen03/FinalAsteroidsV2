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

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class Game extends AnimationTimer {

    private final GraphicsContext gc;
    private final GameData        gameData;
    private final World           world;

    private final List<IGamePluginService>       plugins    = new ArrayList<>();
    private final List<IEntityProcessingService> processors = new ArrayList<>();
    private final List<IPostEntityProcessorService> postProcessors = new ArrayList<>();

    private long lastNanos = 0;

    public Game(GraphicsContext gc, GameData gameData, World world) {
        this.gc       = gc;
        this.gameData = gameData;
        this.world    = world;

        for (IGamePluginService p : ServiceLoader.load(IGamePluginService.class)) {
            plugins.add(p);
            p.start(gameData, world);
        }

        for (IEntityProcessingService ps : ServiceLoader.load(IEntityProcessingService.class)) {
            processors.add(ps);
        }
        
        for (IPostEntityProcessorService ps : ServiceLoader.load(IPostEntityProcessorService.class))
            postProcessors.add(ps);
    }

    @Override
    public void handle(long now) {
        if (lastNanos == 0) { lastNanos = now; return; }

        float dt = Math.min((now - lastNanos) / 1_000_000_000f, 0.05f);
        lastNanos = now;
        gameData.setDeltaTime(dt);

        for (IEntityProcessingService ps : processors) {
            ps.process(gameData, world);
        }
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

        Color color = switch (e.getType()) {
            case PLAYER -> Color.WHITE;
            case ENEMY  -> Color.RED;
            case ENEMY_BULLET -> Color.RED;
            case BULLET -> Color.GREEN;
            case ASTEROID -> Color.LIGHTGRAY;
        };

        if (e.getType() == EntityType.BULLET || e.getType() == EntityType.ENEMY_BULLET) {
            gc.setFill(color);
            float r = e.getRadius();
            gc.fillOval(-r, -r, r * 2, r * 2);
        }
        else if (e.getPolygon() != null) {
            float[] poly = e.getPolygon();
            int n = poly.length / 2;
            double[] xs = new double[n];
            double[] ys = new double[n];
            for (int i = 0; i < n; i++) {
                xs[i] = poly[i * 2];
                ys[i] = poly[i * 2 + 1];
            }
            gc.setStroke(color);
            gc.setLineWidth(2);
            gc.strokePolygon(xs, ys, n);
        }

        gc.restore();
    }

    private void drawHUD() {
        gc.setFill(Color.LIGHTGRAY);
        gc.setFont(Font.font("Monospaced", 13));
        gc.fillText("\u2190 \u2192 Rotate   \u2191 Thrust   SPACE Shoot", 10, 20);
    }
}
