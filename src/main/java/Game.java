import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.When;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static util.I18n.s;

public class Game extends Application {
    private static final FontSmoothingType SMOOTHING = FontSmoothingType.LCD; // GRAY has lousy kerning
    private static final int DEFAULT_WIDTH = 1100;
    private static final int DEFAULT_HEIGHT = 600;
    private static final Paint TRACK_PAINT = Color.BLACK;
    private static final double TRACK_WIDTH = 3.0;
    private static final double TRACK_PADDING = 10.0;
    private static final Paint LOCATION_PAINT = Color.BLACK;
    private static final double LOCATION_RADIUS = 5.0;
    private static final double LOCATION_WIDTH = 2.0;
    private static final Paint LOCATION_FILL = Color.WHITE;
    private static final Paint LOCATION_LABEL_FILL = Color.BLACK;
    private static final double LOCATION_LABEL_MARGIN = 10.0;
    private static final double LOCATION_LABEL_FONT_SIZE = 16.0;

    private IRace race;
    private Stage stage;
    private final List<GuiLocation> guiLocations = new ArrayList<>();

    // Race Scene
    private Pane canvas;
    private Circle track;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        setScene(createRaceScene());

        primaryStage.setTitle(s("game.title"));
        primaryStage.show();
    }

    private void setScene(Scene scene) {
        // Hardcoded for simplicity (for now)
        //ObservableList<String> css = scene.getStylesheets();
        //css.add("metro.css");
        //css.add("app.css");

        stage.setScene(scene);
    }

    private Scene createRaceScene() {
        Pane canvas = new Pane();
        this.canvas = canvas;
        canvas.setId("center");

        BorderPane root = new BorderPane(canvas);
        root.setId("root");

        Circle track = new Circle();
        this.track = track;
        canvas.getChildren().add(track);
        track.radiusProperty().bind(Bindings.min(canvas.widthProperty(), canvas.heightProperty()).divide(2).subtract(Math.max(TRACK_WIDTH / 2.0, LOCATION_RADIUS + LOCATION_WIDTH) + TRACK_PADDING + LOCATION_LABEL_MARGIN + LOCATION_LABEL_FONT_SIZE));
        track.centerXProperty().bind(canvas.widthProperty().divide(2));
        track.centerYProperty().bind(canvas.heightProperty().divide(2));
        track.setStroke(TRACK_PAINT);
        track.setStrokeWidth(TRACK_WIDTH);
        track.setFill(Color.TRANSPARENT);

        updateLocations();

        return new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    private void updateLocations() {
        if (canvas == null || track == null) {
            return;
        }

        for (GuiLocation loc : guiLocations) {
            canvas.getChildren().removeAll(loc.getNodes());
        }
        guiLocations.clear();

        Font font = Font.font(LOCATION_LABEL_FONT_SIZE);
        int count = getLocations().size();
        List<RaceLeg> legs = getRaceLegs();
        double totalDistance = legs.stream().mapToDouble(RaceLeg::getDistance).sum();

        {
            Iterator<Location> locationIterator = getLocations().iterator();
            for (int i = 0; i < count && locationIterator.hasNext(); i++) {
                guiLocations.add(new GuiLocation(locationIterator.next(), new Text(), new Circle()));
            }
        }


        {
            Iterator<RaceLeg> legIterator = legs.iterator();
            double cumulDistance = 0.0;
            int i = 0;
            for (GuiLocation loc : guiLocations) {
                if (i > 0) {
                    cumulDistance += legIterator.next().getDistance();
                }

                double theta = Math.PI * 2.0 * cumulDistance / totalDistance - Math.PI / 2;
                double x = Math.cos(theta);
                double y = Math.sin(theta);
                System.out.printf("theta:%f  cos:%f  sin:%f  x:%f  y:%f%n", theta, x, y, 100 * x, 100 * y);

                Circle point = loc.getPoint();
                point.setRadius(LOCATION_RADIUS);
                point.setFill(LOCATION_FILL);
                point.setStrokeWidth(LOCATION_WIDTH);
                point.setStroke(LOCATION_PAINT);
                point.centerXProperty().bind(track.radiusProperty().multiply(x).add(track.centerXProperty()));
                point.centerYProperty().bind(track.radiusProperty().multiply(y).add(track.centerYProperty()));

                Text previous = guiLocations.get(i - 1 >= 0 ? i - 1 : count - 1).getLabel();
                Text label = loc.getLabel();
                label.setText(loc.getLocation().getName());
                label.setFont(font);
                label.setFontSmoothingType(SMOOTHING);
                label.setFill(LOCATION_LABEL_FILL);
                label.setTextOrigin(VPos.CENTER);
                label.xProperty().bind(point.centerXProperty());
                label.yProperty().bind(point.centerYProperty());

                DoubleBinding margin = BoundsBindings.height(label.layoutBoundsProperty()).divide(2.0).add(LOCATION_LABEL_MARGIN);
                When intersecting = Bindings.when(BoundsBindings.intersect(label.layoutBoundsProperty(), previous.layoutBoundsProperty()));
                DoubleBinding offset = margin.multiply(intersecting.then(-1).otherwise(1)).subtract(intersecting.then(BoundsBindings.height(label.layoutBoundsProperty())).otherwise(0));
                label.translateXProperty().bind(BoundsBindings.width(label.layoutBoundsProperty()).divide(-2.0).add(Bindings.multiply(x, offset)));
                label.translateYProperty().bind(Bindings.multiply(y, offset));
                label.setRotate(theta * 180 / Math.PI + 90);

                i++;
            }
        }

        for (GuiLocation loc : guiLocations) {
            canvas.getChildren().addAll(loc.getNodes());
        }
    }

    //
    // TEST CODE
    //

    private List<Location> locations = Arrays.asList(
            new Location("Apple Headquarters"),
            new Location("Baseball Field"),
            new Location("Candy Store"),
            new Location("Duck Hunting"),
            new Location("East of Eden"),
            new Location("Frisbee Store"),
            new Location("Googleplex"),
            new Location("Hidden Volcano"),
            new Location("Istanbul"),
            new Location("Jamaica")
    );

    private List<RaceLeg> raceLegs = Arrays.asList(
            new RaceLeg(1, 0, 0, Conditions.SUNNY, locations.get(0), locations.get(1)),
            new RaceLeg(1, 0, 0, Conditions.SUNNY, locations.get(1), locations.get(3)),
            new RaceLeg(1, 0, 0, Conditions.SUNNY, locations.get(2), locations.get(3)),
            new RaceLeg(2, 0, 0, Conditions.SUNNY, locations.get(3), locations.get(4)),
            new RaceLeg(2, 0, 0, Conditions.SUNNY, locations.get(4), locations.get(5)),
            new RaceLeg(2, 0, 0, Conditions.SUNNY, locations.get(5), locations.get(6)),
            new RaceLeg(2, 0, 0, Conditions.SUNNY, locations.get(6), locations.get(7)),
            new RaceLeg(3, 0, 0, Conditions.SUNNY, locations.get(7), locations.get(8)),
            new RaceLeg(3, 0, 0, Conditions.SUNNY, locations.get(8), locations.get(9)),
            new RaceLeg(3, 0, 0, Conditions.SUNNY, locations.get(9), locations.get(0))
    );

    private List<RaceLeg> getRaceLegs() {
        return raceLegs;
    }

    private List<Location> getLocations() {
        return locations;
    }


    private static class GuiLocation {
        private final Location location;
        private final Text label;
        private final Circle point;

        private GuiLocation(Location location) {
            this(location, new Text(), new Circle());
        }

        private GuiLocation(Location location, Text label, Circle point) {
            this.location = location;
            this.label = label;
            this.point = point;
        }

        public List<Node> getNodes() {
            return Arrays.asList(label, point);
        }

        public Location getLocation() {
            return location;
        }

        public Text getLabel() {
            return label;
        }

        public Circle getPoint() {
            return point;
        }
    }
}
