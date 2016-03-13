package massbay.cs225;

public class DebugTires implements Tires {
    private double coefficientOfFriction;
    private double inflationPercentage;
    private double addedRadius;
    private double widthInsert;

    public DebugTires(double coefficientOfFriction, double inflationPercentage, double addedRadius, double widthInsert){
        this.addedRadius = addedRadius;
        this.coefficientOfFriction = coefficientOfFriction; //TODO
        if(inflationPercentage < 1.0) {
            this.inflationPercentage = inflationPercentage;
        }
        else{
            this.inflationPercentage = 1.0;
        }
        this.widthInsert = widthInsert;
    }

    @Override
    public double getCoefficientOfFriction() {
        return coefficientOfFriction;
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
