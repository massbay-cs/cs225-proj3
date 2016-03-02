import java.util.List;
import java.util.Map;

public interface IRace {
    Map<String, Car> getCarsByName();

    List<Car> getCarsByPosition();

    RaceTrack getTrack();

    boolean isComplete();

    void tick();
}
