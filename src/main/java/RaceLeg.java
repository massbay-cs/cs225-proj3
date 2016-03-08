/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author whis
 */
public final class RaceLeg {
    private double distance;
    private double elevationDelta;
    private double averageTurnRadius;
    private Conditions conditions;
    private Location startLocation;
    private Location endLocation;
    
    //Constructor for the class
    public RaceLeg(double distance, double elevationDelta, double averageTurnRadius, Conditions conditions, Location startLocation, Location endLocation){
        this.distance = distance;
        this.elevationDelta = elevationDelta;
        this.averageTurnRadius = averageTurnRadius;
        this.conditions = conditions;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }
    
    //Accessors for the instance variables in the class
    public double getDistance(){
        return distance;
    }
    
    public double getElevationDelta(){
        return elevationDelta;
    }
    
    public double getAverageTurnRadius(){
        return averageTurnRadius;
    }
    
    public Conditions getConditions(){
        return conditions;
    }
    
    public Location getStartLocation(){
        return startLocation;
    }
    
    public Location getEndLocation(){
        return endLocation;
    }
    
}
