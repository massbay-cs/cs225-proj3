import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import static util.I18n.*;

public class Game extends Application {
    private static final int DEFAULT_WIDTH = 1100;
    private static final int DEFAULT_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane center = new GridPane();
        center.setId("center");

        BorderPane root = new BorderPane(center);
        root.setId("root");

        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        ObservableList<String> css = scene.getStylesheets();
        css.add("metro.css");
        css.add("app.css");

        primaryStage.setTitle(s("game.title"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
