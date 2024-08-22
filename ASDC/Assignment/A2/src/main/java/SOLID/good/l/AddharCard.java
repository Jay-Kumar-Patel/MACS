package SOLID.good.l;

public class AddharCard implements GovernmentFacility{

    public String name;
    public String officeAddress;
    public int staffCount;
    public boolean isActive;
    public int serveNoOfPeople;

    public AddharCard(String name, String officeAddress, int staffCount, boolean isActive, int serveNoOfPeople) {
        this.name = name;
        this.officeAddress = officeAddress;
        this.staffCount = staffCount;
        this.isActive = isActive;
        this.serveNoOfPeople = serveNoOfPeople;
    }

    @Override
    public void operate() {
        if (isActive){
            if (staffCount > 0){
                System.out.println("Government facility " + name + " is operating with " + staffCount + " staff members.");
            }
            else {
                System.out.println("Government facility " + name + " is not operating because staff count (" + staffCount + ") is equal to zero");
            }
        }
        else {
            System.out.println("Government facility " + name + " is not operating with because it is already inActive");
        }
    }

    @Override
    public void addStaff(int count) {
        if (isActive){
            staffCount += count;
            System.out.println(count + " staff members added to " + name + ". Total staff: " + staffCount);
        }
        else {
            System.out.println("Government facility is inActive so it can't able to add staff");
        }
    }

    @Override
    public void removeStaff(int count) {
        if (isActive){
            if (staffCount > 0 && staffCount >= count) {
                staffCount -= count;
                System.out.println(count + " staff members removed from " + name + ". Total staff: " + staffCount);
            } else {
                System.out.println("Not enough staff to remove.");
            }
        }
        else {
            System.out.println("Government facility is inActive so it can't able to remove staff");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public int getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(int staffCount) {
        this.staffCount = staffCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getServeNoOfPeople() {
        return serveNoOfPeople;
    }

    public void setServeNoOfPeople(int serveNoOfPeople) {
        this.serveNoOfPeople = serveNoOfPeople;
    }
}
