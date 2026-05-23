package dk.sdu.cbse.main;

import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private static final float WIDTH  = 800f;
    private static final float HEIGHT = 600f;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        Pane   root   = new Pane(canvas);
        Scene  scene  = new Scene(root, WIDTH, HEIGHT);

        GameData gameData = new GameData(WIDTH, HEIGHT);
        World    world    = new World();

        scene.setOnKeyPressed (e -> gameData.getKeys().add(e.getCode().toString()));
        scene.setOnKeyReleased(e -> gameData.getKeys().remove(e.getCode().toString()));

        Game game = new Game(canvas.getGraphicsContext2D(), gameData, world);
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
