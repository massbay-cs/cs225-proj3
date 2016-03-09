package massbay.cs225.gui;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import massbay.cs225.Car;

import java.util.Arrays;
import java.util.List;

public class GuiCar {
    private static final double ICON_RADIUS = 5.0;
    private static final double PATH_WIDTH = 3.0;
    private static final double PATH_MARGIN = 10.0;

    private final Car car;
    private final int id;
    private final Circle icon;
    private final Arc trail;
    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();
    private final DoubleProperty position = new SimpleDoubleProperty();

    public GuiCar(Car car, int id, int total, double startPosition, Circle track) {
        this.car = car;
        this.id = id;

        Color color = Color.hsb(360.0 * id / total, 1.0, 1.0);


        DoubleBinding theta = position.multiply(Math.PI * 2).subtract(Math.PI / 2);
        x.bind(MathBindings.cos(theta));
        y.bind(MathBindings.sin(theta));

        trail = new Arc();
        trail.setStroke(color);
        trail.setStrokeWidth(PATH_WIDTH);
        trail.setFill(Color.TRANSPARENT);
        trail.centerXProperty().bind(track.centerXProperty());
        trail.centerYProperty().bind(track.centerYProperty());
        trail.radiusXProperty().bind(track.radiusProperty().subtract((id + 1) * (PATH_WIDTH + PATH_MARGIN)));
        trail.radiusYProperty().bind(trail.radiusXProperty());
        trail.setStartAngle((1 - startPosition) * 360 + 90);
        trail.lengthProperty().bind(positionProperty().subtract(startPosition).multiply(-360));

        icon = new Circle(ICON_RADIUS);
        icon.setFill(color);
        icon.centerXProperty().bind(x.multiply(trail.radiusXProperty()).add(track.centerXProperty()));
        icon.centerYProperty().bind(y.multiply(trail.radiusYProperty()).add(track.centerYProperty()));
    }

    public double getPosition() {
        return position.getValue();
    }

    public void setPosition(double position) {
        this.position.setValue(position);
    }

    public DoubleProperty positionProperty() {
        return position;
    }

    public List<Node> getNodes() {
        return Arrays.asList(trail, icon);  // Order matters
    }

    public void driveTo(double position, double time) {
        double start = getPosition();
        double diff = position - start;

        Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(time));
            }

            @Override
            protected void interpolate(double frac) {
                setPosition(start + diff * frac);
            }
        };

        animation.play();
    }

    public Shape getIcon() {
        return icon;
    }
}
