package massbay.cs225;

public class BaseWheels implements Wheels{
    private double width;
    private double radius;


    public BaseWheels(){
        this.width = 4.0;
        this.radius = 2.0;
    }



    public double getWidth(){
        return width;
    }

    public double getRadius(){
        return radius;
    }
}