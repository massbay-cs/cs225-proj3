package massbay.cs225;

public interface CarBuild {
    Wheels getWheels();
    Tires getTires();
    Engine getEngine();
    double getAverageMPG();
}