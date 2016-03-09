package massbay.cs225;

public class BaseEngine implements Engine{

    private double Horsepower;
    private int Cylinders;
    private GasType GasType;
    private double AverageMPG;

    public BaseEngine(){
        this.Horsepower = 3.0;
        this.Cylinders = 2;
        this.GasType = GASOLINE;
        this.AverageMPG = (Horsepower * GasType.getDensity() * Cylinders);
    }

    public int getCylinders() {
        return Cylinders;
    }

    public double getHorsepower() {
        return Horsepower;
    }

    public GasType getGasType() {
        return GasType;
    }

    public double getAverageMPG() {
        return AverageMPG;
    }
}