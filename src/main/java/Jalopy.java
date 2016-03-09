public class Jalopy implements CarBuild{
    private Wheels wheels;
    private Engine engine;
    private GasTank gasTank;
    private Tires tires;

    public Jalopy(){
        this.wheels = new BaseWheels();
        this.engine = new BaseEngine();
        this.gasTank = new BaseTank();
        this.tires = new BaseTires();
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