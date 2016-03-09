public class BaseTank implements GasTank{

    private double Gallons;
    private double currentGallons;

    public BaseTank(){
        this.Gallons = 10.0;
        this.currentGallons = 10.0;

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