package massbay.cs225.gui;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import massbay.cs225.api.carbuild.*;
import massbay.cs225.api.carbuild.annotation.CarField;
import massbay.cs225.api.race.Car;
import massbay.cs225.api.race.IRace;
import massbay.cs225.api.world.Conditions;
import massbay.cs225.api.world.Location;
import massbay.cs225.api.world.RaceLeg;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.*;

import static massbay.cs225.util.I18n.s;

public class Game extends Application {
    private static final FontSmoothingType SMOOTHING = FontSmoothingType.LCD; // GRAY has lousy kerning
    private static final int DEFAULT_WIDTH = 700;
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
    private static final double CONFIG_PANE_WIDTH = 350.0;

    private IRace race;
    private Stage stage;
    private GridPane componentConfigGrid;
    private final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private final List<GuiLocation> guiLocations = new ArrayList<>();
    private final List<GuiCar> guiCars = new ArrayList<>();
    private final ObservableList<CarBuild> carBuilds;
    private final ObservableList<Engine> engines;
    private final ObservableList<Wheels> wheels;
    private final ObservableList<Tires> tires;
    private final ObservableList<GasTank> gasTanks;
    private final ObservableList<GasType> gasTypes;
    private final ObservableList<? extends CarComponent> carComponents;
    private final Map<ComponentType, ObservableList<? extends CarComponent>> componentsByType = new EnumMap<>(ComponentType.class);

    // Race Scene
    private Pane canvas;
    private Circle track;

    {
        componentsByType.put(ComponentType.ENGINE, engines = FXCollections.observableArrayList());
        componentsByType.put(ComponentType.WHEELS, wheels = FXCollections.observableArrayList());
        componentsByType.put(ComponentType.TIRES, tires = FXCollections.observableArrayList());
        componentsByType.put(ComponentType.GAS_TANK, gasTanks = FXCollections.observableArrayList());
        componentsByType.put(ComponentType.GAS_TYPE, gasTypes = FXCollections.observableArrayList(GasType.GASOLINE, GasType.DIESEL));

        // Some hax here because generic type parameters are extremely limited in Java.
        @SuppressWarnings("unchecked")
        ObservableList<? extends CarComponent> carComponents = FXCollections.concat(componentsByType.values().toArray(new ObservableList[componentsByType.size()]));;
        this.carComponents = carComponents;

        carBuilds = FXCollections.observableArrayList();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void addComponent(ComponentType type) {
        try {
            addComponent(type, type.getType().newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            error(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends CarComponent> void addComponent(ComponentType type, T component) {
        ((ObservableList) componentsByType.get(type)).add(component);
        ((ObservableList) carComponents).add(component);
    }

    private void removeComponent(CarComponent component) {
        carComponents.remove(component);

        for (ObservableList<? extends CarComponent> components : componentsByType.values()) {
            if (components.remove(component)) {
                break;
            }
        }
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

    private void error(Throwable e) {
        e.printStackTrace();

        new Alert(Alert.AlertType.ERROR, e.getLocalizedMessage(), ButtonType.CLOSE).show();
    }

    private Scene createRaceScene() {
        //
        // Center Pane
        //

        Pane canvas = new Pane();
        this.canvas = canvas;
        canvas.setId("center");

        Circle track = new Circle();
        this.track = track;
        canvas.getChildren().add(track);
        track.radiusProperty().bind(Bindings.min(canvas.widthProperty(), canvas.heightProperty()).divide(2).subtract(Math.max(TRACK_WIDTH / 2.0, LOCATION_RADIUS + LOCATION_WIDTH) + TRACK_PADDING + LOCATION_LABEL_MARGIN + LOCATION_LABEL_FONT_SIZE));
        track.centerXProperty().bind(canvas.widthProperty().divide(2));
        track.centerYProperty().bind(canvas.heightProperty().divide(2));
        track.setStroke(TRACK_PAINT);
        track.setStrokeWidth(TRACK_WIDTH);
        track.setFill(Color.TRANSPARENT);

        //
        // Config Pane
        //

        Accordion configPane = new Accordion();
        configPane.setPrefWidth(CONFIG_PANE_WIDTH);
        configPane.setMaxWidth(CONFIG_PANE_WIDTH);

        {
            Label componentsLabel = new Label(s("game.carComponentsLabel"));
            ListView<? extends CarComponent> componentsView = new ListView<>(carComponents);
            componentsView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            componentsView.setEditable(true);
            componentsView.setCellFactory(CarComponentListCell.factory(true));
            ComboBox<ComponentType> componentType = new ComboBox<>(FXCollections.observableArrayList(Arrays.stream(ComponentType.values()).filter(ComponentType::isTypeMutable).toArray(ComponentType[]::new)));
            Button delete = new Button(s("game.deleteAction"));
            Button create = new Button(s("game.createAction"));
            HBox buttons = new HBox(delete, create);
            VBox componentsGroup = new VBox(componentsLabel, componentsView, componentType, buttons);

            componentConfigGrid = new GridPane();

            VBox vbox = new VBox(componentsGroup, componentConfigGrid);
            TitledPane pane = new TitledPane(s("game.carComponentsTitle"), vbox);
            configPane.getPanes().add(pane);

            componentsView.getSelectionModel().selectedItemProperty().addListener(this::onComponentsListSelectionChanged);

            create.setOnAction(event -> {
                ComponentType type = componentType.getValue();
                if (type == null) {
                    componentType.requestFocus();
                    return;
                }

                addComponent(type);

                componentsView.requestFocus();
                componentsView.getSelectionModel().selectLast();
                componentsView.edit(componentsView.getSelectionModel().getSelectedIndex());
            });

            delete.setOnAction(event -> {
                CarComponent component = componentsView.getSelectionModel().getSelectedItem();
                if (component == null) {
                    componentsView.requestFocus();
                    return;
                }

                if (!component.isMutable()) {
                    new Alert(Alert.AlertType.ERROR, s("game.error.cannotDeleteImmutableComponent"), ButtonType.OK).show();
                    return;
                }

                removeComponent(component);
            });
        }

        {
            Label carBuildsLabel = new Label(s("game.carBuildsLabel"));
            ListView<CarBuild> carBuildsView = new ListView<>(carBuilds);
            Button delete = new Button(s("game.deleteAction"));
            Button create = new Button(s("game.createAction"));
            HBox buttons = new HBox(delete, create);
            VBox carBuildsGroup = new VBox(carBuildsLabel, carBuildsView, buttons);

            GridPane buildGrid = new GridPane();
            int row = 1;
            for (Map.Entry<ComponentType, ObservableList<? extends CarComponent>> entry : componentsByType.entrySet()) {
                Label label = new Label(entry.getKey().getName() + ":");
                ComboBox<? extends CarComponent> options = new ComboBox<>(entry.getValue());
                options.setCellFactory(CarComponentListCell.factory(false));
                buildGrid.addRow(row++, label, options);
            }

            VBox vbox = new VBox(carBuildsGroup, buildGrid);
            TitledPane pane = new TitledPane(s("game.addACarTitle"), vbox);
            configPane.getPanes().add(pane);
        }

        {
            VBox vbox = new VBox();
            TitledPane pane = new TitledPane(s("game.configureRaceTrackTitle"), vbox);
            configPane.getPanes().add(pane);
        }

        //
        // Root
        //

        BorderPane root = new BorderPane(canvas, null, configPane, null, null);
        root.setId("root");

        //
        // Debug Code
        //

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

        return new Scene(root, DEFAULT_WIDTH + CONFIG_PANE_WIDTH, DEFAULT_HEIGHT);
    }

    private void onComponentsListSelectionChanged(ObservableValue<? extends CarComponent> observable, CarComponent oldValue, CarComponent newValue) {
        componentConfigGrid.getChildren().clear();

        if (newValue == null) {
            return;
        }

        Class<? extends CarComponent> type = newValue.getClass();
        Field[] fields = type.getDeclaredFields();

        int row = 1;
        for (Field field : fields) {
            field.setAccessible(true);
            CarField[] metas = field.getDeclaredAnnotationsByType(CarField.class);

            if (metas.length == 0) {
                continue;
            }

            MethodHandle getter, setter;
            try {
                getter = lookup.unreflectGetter(field);
                setter = lookup.unreflectSetter(field);
            } catch (IllegalAccessException e) {
                error(e);
                return;
            }

            // There isn't really any reason there should be multiple with our use case, but just in case
            for (CarField meta : metas) {
                Label label = new Label(s(meta.label()));
                Control control;
                Node node;

                try {
                    switch (meta.type()) {
                        case PERCENTAGE:
                            Slider percentage = new Slider(0, 1, (double) getter.invoke(newValue));
                            control = percentage;
                            percentage.setMajorTickUnit(.1);
                            percentage.setMinorTickCount(9);
                            percentage.valueProperty().addListener((obs, was, n) -> {
                                try {
                                    setter.invoke(newValue, n);
                                } catch (Throwable e) {
                                    error(e);
                                }
                            });

                            Label percentageLabel = new Label();
                            percentageLabel.textProperty().bind(percentage.valueProperty().multiply(100).asString("%.0f%%"));
                            node = new HBox(percentage, percentageLabel);
                            break;

                        case NONNEGATIVE_DOUBLE:
                            TextField decimal = new TextField(Double.toString((double) getter.invoke(newValue)));
                            node = control = decimal;
                            decimal.focusedProperty().addListener((obs, was, is) -> {
                                if (!was && is) {
                                    double n;
                                    try {
                                        n = Double.parseDouble(decimal.getText());
                                    } catch (NumberFormatException e) {
                                        new Alert(Alert.AlertType.ERROR, s("game.error.invalidValue"), ButtonType.OK).showAndWait();
                                        decimal.requestFocus();
                                        return;
                                    }

                                    if (n < 0) {
                                        new Alert(Alert.AlertType.ERROR, s("game.error.valueMustNotBeNegative"), ButtonType.OK).showAndWait();
                                        decimal.requestFocus();
                                        return;
                                    }

                                    try {
                                        setter.invoke(newValue, n);
                                    } catch (Throwable e) {
                                        error(e);
                                        return;
                                    }

                                    decimal.setText(Double.toString(n));
                                }
                            });
                            break;

                        case NONNEGATIVE_INT:
                            TextField whole = new TextField(Integer.toString((int) getter.invoke(newValue)));
                            node = control = whole;
                            whole.focusedProperty().addListener((obs, was, is) -> {
                                if (!was && is) {
                                    int n;
                                    try {
                                        n = Integer.parseInt(whole.getText());
                                    } catch (NumberFormatException e) {
                                        new Alert(Alert.AlertType.ERROR, s("game.error.invalidValue"), ButtonType.OK).showAndWait();
                                        return;
                                    }

                                    if (n < 0) {
                                        new Alert(Alert.AlertType.ERROR, s("game.error.valueMustNotBeNegative"), ButtonType.OK).showAndWait();
                                        whole.requestFocus();
                                        return;
                                    }

                                    try {
                                        setter.invoke(newValue, n);
                                    } catch (Throwable e) {
                                        error(e);
                                        return;
                                    }

                                    whole.setText(Double.toString(n));
                                }
                            });
                            break;

                        default:
                            throw new NullPointerException();
                    }
                } catch (Throwable e) {
                    error(e);
                    return;
                }

                control.setDisable(meta.readonly());

                componentConfigGrid.addRow(row++, label, node);
            }
        }
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
