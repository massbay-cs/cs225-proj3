package massbay.cs225;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;

import static massbay.cs225.util.I18n.s;

public class Game extends Application {
    private static final FontSmoothingType SMOOTHING = FontSmoothingType.LCD; // GRAY has lousy kerning
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 600;
    private static final Paint TRACK_PAINT = Color.BLACK;
    private static final double TRACK_WIDTH = 3.0;
    private static final double TRACK_PADDING = 20.0;
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
    private final List<GuiCar> guiCars = new ArrayList<>();

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

        createLocations(5);
        updateLocations();
        createCars(5);
        updateCars();

        int min = 2;
        int max = 10;

        Slider locationsSlider = new Slider(2, 10, getLocations().size());
        Slider carsSlider = new Slider(2, 10, getCars().size());
        VBox sliders = new VBox(
                new HBox(new Label("Locations:"), locationsSlider),
                new HBox(new Label("Cars:"), carsSlider)
        );
        root.setBottom(sliders);

        locationsSlider.valueProperty().addListener((obs, o, n) -> {
            int x = (int) Math.round(o.doubleValue());
            int y = (int) Math.round(n.doubleValue());
            if (x != y && y >= min && y <= max) {
                createLocations(y);
                updateLocations();
                updateCars();
            }
        });
        carsSlider.valueProperty().addListener((obs, o, n) -> {
            int x = (int) Math.round(o.doubleValue());
            int y = (int) Math.round(n.doubleValue());
            if (x != y && y >= min && y <= max) {
                createCars(y);
                updateCars();
            }
        });

        return new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    private void updateCars() {
        if (canvas == null || track == null) {
            return;
        }

        for (GuiCar car : guiCars) {
            canvas.getChildren().removeAll(car.getNodes());
        }
        guiCars.clear();

        // TODO: Use actual cars; this is all just debug code
        int count = getCars().size();
        for (int i = 0; i < count; i++) {
            double position = 0;
            GuiCar car = new GuiCar(getCars().get(i), i, count, position, track);
            guiCars.add(car);

            position = guiLocations.get((i + 1) % guiLocations.size()).getPosition();
            position += (i + 1) / guiLocations.size();
            car.setPosition(Math.min(1, position));

            car.getIcon().setCursor(Cursor.HAND);
            car.getIcon().setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 1) {
                    Double pos = car.getPosition();

                    if (pos >= 1) {
                        car.setPosition(0);
                    } else {
                        double target = 1;
                        for (GuiLocation location : guiLocations) {
                            if (location.getPosition() > pos) {
                                target = location.getPosition();
                                break;
                            }
                        }

                        car.driveTo(target, count * 1_000 * (target - pos));
                    }
                }
            });
        }

        for (GuiCar car : guiCars) {
            canvas.getChildren().addAll(car.getNodes());
        }
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
                guiLocations.add(new GuiLocation(locationIterator.next()));
            }
        }


        {
            Iterator<RaceLeg> legIterator = legs.iterator();
            double cumulDistance = 0.0;
            int i = 0;
            RaceLeg previousLeg = legs.get(legs.size() - 1);
            for (GuiLocation loc : guiLocations) {
                RaceLeg nextLeg = legIterator.next();

                if (i > 0) {
                    cumulDistance += previousLeg.getDistance();
                }

                double completion = cumulDistance / totalDistance;
                double nextMidpoint = previousLeg.getDistance() / totalDistance / 2;
                double previousMidpoint = nextLeg.getDistance() / totalDistance / 2;

                loc.setPosition(completion);
                double x = loc.getX();
                double y = loc.getY();

                Circle point = loc.getPoint();
                point.setRadius(LOCATION_RADIUS);
                point.setFill(LOCATION_FILL);
                point.setStrokeWidth(LOCATION_WIDTH);
                point.setStroke(LOCATION_PAINT);
                point.centerXProperty().bind(track.radiusProperty().multiply(x).add(track.centerXProperty()));
                point.centerYProperty().bind(track.radiusProperty().multiply(y).add(track.centerYProperty()));

                Text label = loc.getLabel();
                label.setText(loc.getLocation().getName());
                label.setFont(font);
                label.setFontSmoothingType(SMOOTHING);
                label.setFill(LOCATION_LABEL_FILL);
                label.setTextOrigin(VPos.TOP);
                label.setTextAlignment(TextAlignment.CENTER);
                label.xProperty().bind(point.centerXProperty());
                label.yProperty().bind(point.centerYProperty());
                label.wrappingWidthProperty().bind(track.radiusProperty().multiply(2 * Math.PI * Math.min(previousMidpoint, nextMidpoint) * 2));


                DoubleBinding offset = BoundsBindings.height(label.layoutBoundsProperty()).divide(2.0).add(LOCATION_LABEL_MARGIN);
                label.translateXProperty().bind(BoundsBindings.width(label.layoutBoundsProperty()).divide(-2).add(Bindings.multiply(x, offset)));
                label.translateYProperty().bind(BoundsBindings.height(label.layoutBoundsProperty()).divide(-2).add(Bindings.multiply(y, offset)));
                label.setRotate(loc.getAngle() + 90);

                // Debug
                Rectangle rect = new Rectangle();
                rect.setStroke(Color.BLUE);
                rect.setStrokeWidth(2);
                rect.setFill(Color.TRANSPARENT);
                rect.xProperty().bind(label.xProperty());
                rect.yProperty().bind(label.yProperty());
                rect.widthProperty().bind(BoundsBindings.width(label.layoutBoundsProperty()));
                rect.heightProperty().bind(BoundsBindings.height(label.layoutBoundsProperty()));
                rect.translateXProperty().bind(label.translateXProperty());
                rect.translateYProperty().bind(label.translateYProperty());
                rect.rotateProperty().bind(label.rotateProperty());
                //canvas.getChildren().add(rect);

                previousLeg = nextLeg;
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

    private List<Location> locations;

    private void createLocations(int count) {
        Location[] all = {
                new Location("Apple Headquarters"),
                new Location("Baseball Field"),
                new Location("Candy Store"),
                new Location("Duck Hunting"),
                new Location("East of Eden"),
                new Location("Frisbee Store"),
                new Location("Googleplex"),
                new Location("Hidden Volcano"),
                new Location("Istanbul"),
                new Location("Jamaica"),
        };

        locations = Arrays.asList(Arrays.copyOf(all, count));

        Random random = new Random();
        RaceLeg[] legs = new RaceLeg[count];

        for (int i = 0; i < count; i++) {
            int next = i + 1 < count ? i + 1 : 0;
            double distance = random.nextDouble() + 1;
            double elevation = random.nextInt(10_000) - 5_000;
            double averageTurnRadius = random.nextDouble();
            Conditions conditions = random.nextDouble() > 0.2 ? Conditions.values()[random.nextInt(Conditions.values().length)] : Conditions.SUNNY;
            Location start = locations.get(i);
            Location end = locations.get(next);
            legs[i] = new RaceLeg(distance, elevation, averageTurnRadius, conditions, start, end);
        }

        raceLegs = Arrays.asList(legs);
    }

    private List<RaceLeg> raceLegs;

    private List<RaceLeg> getRaceLegs() {
        return raceLegs;
    }

    private List<Location> getLocations() {
        return locations;
    }

    private List<Car> cars;

    private void createCars(int n) {
        cars = Arrays.asList(new Car[n]);
    }

    private List<Car> getCars() {
        return cars;
    }
}
