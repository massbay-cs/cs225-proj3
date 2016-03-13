package massbay.cs225;

public class DebugWheels implements Wheels {
    private double width;
    private double radius;

    public DebugWheels(double width, double radius){
        this.width = width;
        this.radius = radius;
    }

    public double getWidth(){
        return width;
    }

    public double getRadius(){
        return radius;
    }
}
