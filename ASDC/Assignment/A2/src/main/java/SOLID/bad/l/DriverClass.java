package SOLID.bad.l;

import java.util.ArrayList;
import java.util.List;

public class DriverClass {
    public static void main(String[] args) {

        List<GovernmentFacility> facilities = new ArrayList<>();
        facilities.add(new GovernmentFacility("Addhar Card Office", "New Ranip, Ahmedabad", 5));
        facilities.add(new Hospital("Narayan Hospital", "Navrangpura, Ahmedabad", 200, 150));

        for (GovernmentFacility facility : facilities) {
            System.out.println("Here: " + facility.getName());
            facility.operate();
            facility.addStaff(10);
            facility.removeStaff(5);
            facility.servePeople(100);
        }

        Hospital hospital = new Hospital("KD Hospital", "Ahmedabad", 500, 250);
        hospital.operate();

        try {
            hospital.servePeople(300);
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught an exception: " + e.getMessage());
        }

        hospital.setActive(true);
        hospital.addStaff(60);
        hospital.removeStaff(10);
        hospital.setNumberOfBeds(623);
        hospital.servePeople(300);
        hospital.getNumberOfBeds();
        hospital.closeHospital();
    }
}
