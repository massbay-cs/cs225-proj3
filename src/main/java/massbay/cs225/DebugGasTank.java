package massbay.cs225;

public class DebugGasTank implements GasTank {


    private double Gallons;
    private double currentGallons;

    public DebugGasTank(double Gallons, double currentGallons){
        this.Gallons = Gallons;
        if (this.Gallons > currentGallons){
            this.currentGallons = currentGallons;
        }
        else{
            this.currentGallons = Gallons;
        }

    }

    public double getGallons() {
        return Gallons;
    }

    public double getCurrentGallons() {
        return currentGallons;
    }

    public void addGallons(double gallons) {
        if(gallons <= Gallons) {
            currentGallons += gallons;
        }
        else{ fillTank();}
    }

    public void fillTank(){
        currentGallons = Gallons;
    }

}
