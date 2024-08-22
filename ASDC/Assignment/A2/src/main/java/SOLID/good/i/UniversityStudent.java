package SOLID.good.i;

import java.util.Map;

public interface UniversityStudent {
    boolean registerForCourse(String course);
    void viewGrades();
    void updateGrades(Map<String, Integer> grade);
    boolean dropCourse(String course);
}
