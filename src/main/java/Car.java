/**
*
*
*/
public class Car{
    private String name;
    private CarBuild build;
    private double timeToNext;
    private RaceLeg currentLeg;
    private List<ReachLocationEventHandler> reachLocationEventHandlers;
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
    private void onReachLocationListner(Location previous, Location next){

    }
    /**
     *
     *
     */
    public void drive(double time){
    this.timeToNext = this.timeToNext - time;
        if(timeToNext < 0){
            timeToNext = 0;
        onReachLocationListner();
        }

    }
    /**
     *
     *
     */
    public void addReachLocationListner(reachLocationEventHandlers listner){

    }
    /**
     *
     *
     */
    public void removeReachLocationListner(reachLocationEventHandlers listner){

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

    }
    /**
     *
     *
     */
    public Location getEndLocation(){

    }
    /**
     *
     *
     */
    public List<Location> getPath(){

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
//    public List<Location> getPath(){
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
