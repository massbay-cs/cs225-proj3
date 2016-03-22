package massbay.cs225.api.event;

import massbay.cs225.api.race.Car;
import massbay.cs225.api.world.RaceLeg;

public interface ReachLocationEventHandler {
    void reachLocation(Car car, RaceLeg completedLeg);
}
