import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RaceTrack implements IRaceTrack {
    private List<Location> locations = new ArrayList<>();
    private Map<Tuple<Location, Location>, RaceLeg> legsByLocation = new HashMap<>();
    private List<RaceLeg> legs = new ArrayList<>();

    @Override
    public List<Location> getLocations() {
        return null;
    }

    @Override
    public List<RaceLeg> getLegs() {
        return null;
    }

    @Override
    public Map<Tuple<Location, Location>, RaceLeg> getLegsByLocations() {
        return null;
    }

    @Override
    public RaceLeg getLeg(Location start, Location finish) {
        return null;
    }
}
