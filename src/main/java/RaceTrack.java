import java.util.*;

public abstract class RaceTrack implements IRaceTrack {
    private List<Location> locations = new ArrayList<>();
    private Map<Tuple<Location, Location>, RaceLeg> legsByLocation = new HashMap<>();
    private List<RaceLeg> legs = new ArrayList<>();

    protected void addLocation(Location location) {
        locations.add(location);
    }

    protected void addLeg(RaceLeg leg) {
        legs.add(leg);
        legsByLocation.put(new Tuple<>(leg.getStartLocation(), leg.getEndLocation()), leg);
    }

    @Override
    public final List<Location> getLocations() {
        return Collections.unmodifiableList(locations);
    }

    @Override
    public final List<RaceLeg> getLegs() {
        return Collections.unmodifiableList(legs);
    }

    @Override
    public final Map<Tuple<Location, Location>, RaceLeg> getLegsByLocations() {
        return Collections.unmodifiableMap(legsByLocation);
    }

    @Override
    public final RaceLeg getLeg(Location start, Location finish) {
        return getLegsByLocations().get(new Tuple<>(start, finish));
    }
}
