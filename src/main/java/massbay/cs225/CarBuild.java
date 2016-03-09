package massbay.cs225;

public interface CarBuild {
    Wheels getWheels();
    Tires getTires();
    Engine getEngine();
    double getAverageMPG();

    default String getName() {
        return String.join(" ", getClass().getSimpleName().split("(?<=[a-z])(?=[A-Z0-9])"));
    }
}