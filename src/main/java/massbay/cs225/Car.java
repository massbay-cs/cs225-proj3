package massbay.cs225;

import java.util.ArrayList;
import java.util.List;
/**
* A car with a name and a premade build praramiters
*
*/
public class Car{
    private String name;
    private CarBuild build;
    private double timeToNext;
    private RaceLeg currentLeg;
    private final List<ReachLocationEventHandler> reachLocationEventHandlers = new ArrayList<>();
    public double cumulativeTime;

    /**
     *
     *
     */
    public Car(String n, CarBuild b){
        this.name = n;
        this.build = b;
    }



    /**
     *
     *
     */
    private void onReachLocation(RaceLeg completedLeg){
        for (ReachLocationEventHandler listener : reachLocationEventHandlers) {
            listener.reachLocation(this, completedLeg);
        }
    }

    /**
     *
     *
     */
    public void drive(double time){
        this.timeToNext = this.timeToNext - time;
        this.cumulativeTime += time;
        if(timeToNext <= 0){
            timeToNext = 0;
            onReachLocation(currentLeg);
        }

    }
    /**
     *
     *
     */
    public void addReachLocationListener(ReachLocationEventHandler listener){
        reachLocationEventHandlers.add(listener);
    }
    /**
     *
     *
     */
    public void removeReachLocationListener(ReachLocationEventHandler listener){
        reachLocationEventHandlers.remove(listener);
    }
    /**
     *
     *
     */
    public void setCurrentLeg(RaceLeg value){
        this.currentLeg = value;
    }
    /**
     *
     *
     */
    public CarBuild getBuild(){
        return this.build;
    }
    /**
     *
     *
     */
    public String getName(){
        return this.name;
    }
    /**
     *
     *
     */
    public double getTimeToNext(){
        return timeToNext;
    }
    /**
     *
     *
     */
    public Location getStartLocation(){
        return getCurrentLeg().getStartLocation();
    }
    /**
     *
     *
     */
    public Location getEndLocation(){
        return getCurrentLeg().getEndLocation();
    }

    /**
     *
     *
     */
    public RaceLeg getCurrentLeg(){
        return this.currentLeg;
    }
    /**
     *
     *
     */
    public double getCumulativeTime(){
        return this.cumulativeTime;
    }
//    public List<massbay.cs225.Location> getPath(){
//        return currentLeg;
//    }
//
//    public String toString(){
//        String s = "";
//        s += name;
//        s += " made of " + build;
//        s += " is on " + currentLeg + ".";
//
//    }

}
