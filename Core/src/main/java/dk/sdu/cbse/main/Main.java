package dk.sdu.cbse.main;

import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main extends Application {

    private static final float WIDTH  = 800f;
    private static final float HEIGHT = 600f;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        Pane   root   = new Pane(canvas);
        Scene  scene  = new Scene(root, WIDTH, HEIGHT);

        // Boot the Spring container — discovers all @Component classes
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppConfig.class);

        // GameData and World come from Spring (@Bean in AppConfig)
        GameData gameData = ctx.getBean(GameData.class);
        World    world    = ctx.getBean(World.class);

        scene.setOnKeyPressed (e -> gameData.getKeys().add(e.getCode().toString()));
        scene.setOnKeyReleased(e -> gameData.getKeys().remove(e.getCode().toString()));

        // Game is a @Component — Spring injected all its services
        Game game = ctx.getBean(Game.class);
        game.setGc(canvas.getGraphicsContext2D());
        game.start();

        primaryStage.setTitle("AsteroidsFX – Jonas Nguyen");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        canvas.setFocusTraversable(true);
        canvas.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}