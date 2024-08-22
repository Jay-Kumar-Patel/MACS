package SOLID.good.l;

import java.util.ArrayList;
import java.util.List;

public class DriverClass {

    public static void main(String[] args) {

        List<GovernmentFacility> facilities = new ArrayList<>();

        GovernmentFacility hospital = new Hospital("Narayan Hospital", "Navrangpura, Ahmedabad", 200, true, 520, 600);
        GovernmentFacility addharCard = new AddharCard("Addhar Card Office Gujarat", "New Ranip, Ahmedabad", 5, true, 230);

        facilities.add(hospital);
        facilities.add(addharCard);

        for (GovernmentFacility facility : facilities) {

            if (facility instanceof Hospital) {
                Hospital hospitalInstance = (Hospital) facility;
                System.out.println(hospitalInstance.getName());
                hospitalInstance.operate();
                hospitalInstance.addStaff(25);
                hospitalInstance.removeStaff(20);
                hospitalInstance.servePeople(100);
            }

            if (facility instanceof AddharCard) {
                AddharCard addharCardInstance = (AddharCard) facility;
                System.out.println(addharCardInstance.getName());
                addharCardInstance.operate();
                addharCardInstance.addStaff(50);
                addharCardInstance.removeStaff(20);
            }
        }

    }

}
