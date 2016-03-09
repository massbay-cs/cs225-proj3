package massbay.cs225;

public enum GasType {
    GASOLINE(1.0),  // TODO
    DIESEL(1.0);    // TODO

    private final double density;

    GasType(double density) {
        this.density = density;
    }

    public double getDensity() {
        return density;
    }
}