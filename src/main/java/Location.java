/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author whis
 */
public class Location {
    private String nameOfLocation;
    
    public Location(String location){
        location = nameOfLocation;
    }
    
    public String getNameOfLocation(){
        return nameOfLocation;
    }
    
    @Override
    public String toString(){
        return "";
    }
    
    public int hasCode(){
    
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj == this){
            return true;
        } if(obj == null || obj.getClass() != this.getClass()){
            return false;
        }
    }
}
