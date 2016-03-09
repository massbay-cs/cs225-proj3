package massbay.cs225;

public class BaseTires implements Tires{
    private double coefficentOfFriction;
    private double inflationPercentage;
    private double addedRadius;
    private double widthInsert;

    public BaseTires(){
        this.addedRadius = .5;
        this.coefficentOfFriction = 1.0; //TODO
        this.inflationPercentage = 1.0;
        this.widthInsert = 1.0;
    }

    @Override
    public double getCoefficientOfFriction() {
        return coefficentOfFriction;
    }

    @Override
    public double getAddedRadius() {
        return addedRadius;
    }

    @Override
    public double getInflationPercentage() {
        return inflationPercentage;
    }

    @Override
    public double getWidthInset() {
        return widthInsert;
    }
}