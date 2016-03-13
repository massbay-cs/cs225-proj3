package massbay.cs225;

public class DebugCarBuild implements CarBuild {
    private Wheels wheels;
    private Engine engine;
    private GasTank gasTank;
    private Tires tires;

    public DebugCarBuild(double width, double radius, double horsepower, int cylinders, GasType gasType, double Gallons,
                         double currentGallons, double coefficientOfFriction, double inflationPercentage, double addedRadius, double widthInsert ){
        this.wheels = new DebugWheels(width,radius);
        this.engine = new DebugEngine(horsepower, cylinders, gasType);
        this.gasTank = new DebugGasTank(Gallons, currentGallons);
        this.tires = new DebugTires(coefficientOfFriction, inflationPercentage, addedRadius, widthInsert);
    }

    public Engine getEngine() {
        return engine;
    }

    public Tires getTires() {
        return tires;
    }

    public GasTank getGasTank() {
        return gasTank;
    }

    public Wheels getWheels() {
        return wheels;
    }

    public double getAverageMPG() {
        return engine.getAverageMPG();
    }

    @Override
    public String toString() {
        return "DEBUG";
    }
}
