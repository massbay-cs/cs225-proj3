/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Objects;

/**
 *
 * @author whis
 */
public class Location {
    private final String name;
    
    public Location(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    @Override
    public String toString(){
        return name;
    }

    @Override
    public int hashCode(){
        return Objects.hash(name);
    }
    
    @Override
    public boolean equals(Object obj){
        if (obj == this){
            return true;
        } else if(obj == null || obj.getClass() != this.getClass()){
            return false;
        }

        Location other = (Location) obj;
        return Objects.equals(other.getName(), getName());
    }
}
