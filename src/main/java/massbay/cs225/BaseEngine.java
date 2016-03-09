package massbay.cs225;

public class BaseEngine implements Engine{

    private double horsepower;
    private int cylinders;
    private GasType gasType;
    private double averageMPG;

    public BaseEngine(){
        this.horsepower = 3.0;
        this.cylinders = 2;
        this.gasType = GasType.GASOLINE;
        this.averageMPG = (horsepower * gasType.getDensity() * cylinders);
    }

    public int getCylinders() {
        return cylinders;
    }

    public double getHorsepower() {
        return horsepower;
    }

    public GasType getGasType() {
        return gasType;
    }

    public double getAverageMPG() {
        return averageMPG;
    }
}