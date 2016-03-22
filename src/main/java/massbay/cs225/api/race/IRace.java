package massbay.cs225.api.race;

import massbay.cs225.api.world.IRaceTrack;

import java.util.List;
import java.util.Map;

public interface IRace {
    Map<String, Car> getCarsByName();

    List<Car> getCarsByPosition();

    IRaceTrack getTrack();

    boolean isComplete();

    void tick();
}
