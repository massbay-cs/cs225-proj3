public class Jalopy implements CarBuild{
    private Wheels wheels;
    private Engine engine;
    private GasTank gasTank;
    private Tires tires;

    public class Jalopy(){
        this.wheels = BaseWheels;
        this.engine = BaseEngine;
        this.gasTank = BaseTank;
        this.tires = BaseTires;
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
}