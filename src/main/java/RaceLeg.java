/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author whis
 */
public class RaceLeg {
    private double distance;
    private double elevationDelta;
    private double averageTurnRadius;
    private Conditions conditions;
    private Location startLocation;
    private Location endLocation;
    
    //Constructor for the class
    public RaceLeg(double d, double elevation, double averageTurn, Conditions c,
             Location start, Location end){
        distance = d;
        elevationDelta = elevation;
        averageTurnRadius = averageTurn;
        conditions = c;
        startLocation = start;
        endLocation = end;
    }
    
    //Accesors for the instance variables in the class
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
