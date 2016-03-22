package massbay.cs225.api.race;

import lombok.Getter;
import lombok.Setter;
import massbay.cs225.api.carbuild.CarBuild;
import massbay.cs225.api.event.ReachLocationEventHandler;
import massbay.cs225.api.world.Location;
import massbay.cs225.api.world.RaceLeg;

import java.util.ArrayList;
import java.util.List;

public class Car {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private CarBuild build;
    private double elapsedLegTime;
    @Getter
    private RaceLeg currentLeg;
    private double totalLegTime;
    @Getter
    private double cumulativeTime;
    private final List<ReachLocationEventHandler> reachLocationListeners = new ArrayList<>();

    public List<Location> getPath() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Location getStartLocation() {
        return getPath().get(0);
    }

    public Location getEndLocation() {
        return getPath().get(getPath().size() - 1);
    }

    public double getLegCompletion() {
        return elapsedLegTime / totalLegTime;
    }

    public void addReachLocationListener(ReachLocationEventHandler listener) {
        reachLocationListeners.add(listener);
    }

    public boolean removeReachLocationListener(ReachLocationEventHandler listener) {
        return reachLocationListeners.remove(listener);
    }

    private void onReachLocation(RaceLeg completedLeg) {
        for (ReachLocationEventHandler listener : reachLocationListeners) {
            listener.reachLocation(this, completedLeg);
        }
    }

    private double calculateLegTime(RaceLeg leg) {
        if (isFinished()) {
            return 1.0;
        }

        // TODO
        return leg.getDistance();
    }

    public void nextLeg(RaceLeg leg) {
        currentLeg = leg;
        elapsedLegTime = 0.0;
        totalLegTime = calculateLegTime(leg);
    }

    public boolean isFinished() {
        return currentLeg == null;
    }

    public double getTimeToNext() {
        return totalLegTime - elapsedLegTime;
    }

    public void drive(double time) {
        while (time > 0) {
            if (isFinished()) {
                return;
            }

            double maxDriveTime = getTimeToNext();

            if (time >= maxDriveTime) {
                elapsedLegTime = totalLegTime;
                onReachLocation(currentLeg);
                time -= maxDriveTime;
            } else {
                elapsedLegTime += time;
                time = 0;
            }
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
