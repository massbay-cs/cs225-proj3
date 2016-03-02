import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import static util.I18n.*;

public class Game extends Application {
    private static final int DEFAULT_WIDTH = 1100;
    private static final int DEFAULT_HEIGHT = 600;

    private IRace race;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        setScene(createRaceScene());

        primaryStage.setTitle(s("game.title"));
        primaryStage.show();
    }

    private void setScene(Scene scene) {
        ObservableList<String> css = scene.getStylesheets();
        css.add("metro.css");
        css.add("app.css");

        stage.setScene(scene);
    }

    private Scene createRaceScene() {
        StackPane canvas = new StackPane();
        canvas.setId("center");

        BorderPane root = new BorderPane(canvas);
        root.setId("root");

        Circle circle = new Circle(100);
        circle.radiusProperty().bind(DoubleBinding.min(canvas.widthProperty(), canvas.heightProperty()));
        circle.centerXProperty().bind(canvas.widthProperty().divide(2));
        circle.centerYProperty().bind(canvas.heightProperty().divide(2));

        return new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}
