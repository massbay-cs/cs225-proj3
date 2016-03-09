package massbay.cs225;

import java.util.List;
import java.util.Map;

public interface IRace {
    Map<String, Car> getCarsByName();

    List<Car> getCarsByPosition();

    IRaceTrack getTrack();

    boolean isComplete();

    void tick();
}
