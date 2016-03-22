package massbay.cs225.gui;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import massbay.cs225.api.world.Location;

import java.util.Arrays;
import java.util.List;

public class GuiLocation {
    private final Location location;
    private final Text label;
    private final Circle point;
    private double position;
    private double theta;
    private double angle;
    private double x;
    private double y;

    public GuiLocation(Location location) {
        this(location, new Text(), new Circle());
    }

    public GuiLocation(Location location, Text label, Circle point) {
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

    public void setPosition(double position) {
        this.position = position;

        theta = Math.PI * 2.0 * position - Math.PI / 2;
        angle = theta * 180 / Math.PI;
        x = Math.cos(theta);
        y = Math.sin(theta);
        System.out.printf("theta:%f  angle:%f  cos:%f  sin:%f  x:%f  y:%f%n", theta, angle, x, y, 100 * x, 100 * y);
    }

    public double getPosition() {
        return position;
    }

    public double getTheta() {
        return theta;
    }

    public double getAngle() {
        return angle;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
