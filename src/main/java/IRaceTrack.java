import java.util.List;
import java.util.Map;

public interface IRaceTrack {
    List<Location> getLocations();

    List<RaceLeg> getLegs();

    Map<Tuple<Location, Location>, RaceLeg> getLegsByLocations();

    RaceLeg getLeg(Location start, Location finish);
}
