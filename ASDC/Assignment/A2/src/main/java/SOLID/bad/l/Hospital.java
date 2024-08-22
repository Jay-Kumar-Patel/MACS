package SOLID.bad.l;

public class Hospital extends GovernmentFacility{

    private int numberOfBeds;

    public Hospital(String name, String officeAddress, int staffCount, int numberOfBeds) {
        super(name, officeAddress, staffCount);
        this.numberOfBeds = numberOfBeds;
    }

    @Override
    public void servePeople(int numberOfPeople) {
        if (!isActive) {
            System.out.println("Hospital " + name + " is inactive so it can't serve people.");
        } else if (numberOfPeople > numberOfBeds) {
            throw new UnsupportedOperationException("Not enough beds to serve " + numberOfPeople + " people at " + name + ".");
        } else {
            this.serveNoOfPeople = numberOfPeople;
            System.out.println("Serving " + numberOfPeople + " people at " + name + ".");
        }
    }

    public void closeHospital() {
        if (!isActive){
            System.out.println("Hospital is already closed!");
            return;
        }
        isActive = false;
        System.out.println("Hospital " + name + " is now closed.");
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }
}
