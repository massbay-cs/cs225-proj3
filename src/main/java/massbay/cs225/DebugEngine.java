package massbay.cs225;

public class DebugEngine implements Engine{

    private double horsepower;
    private int cylinders;
    private GasType gasType;
    private double averageMPG;

    public DebugEngine(double horsepower, int cylinders, GasType gasType){
        this.horsepower = horsepower;
        this.cylinders = cylinders;
        this.gasType = gasType;
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
