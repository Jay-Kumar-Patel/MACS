package SOLID.bad.i;

import java.util.Map;

public interface University {
    boolean registerForCourse(String course);
    void viewGrades();
    void updateGrades(Map<String, Integer> grade);
    boolean dropCourse(String course);
    boolean teachCourse(String course);
    void gradeAssignment(Map<Student, Integer> grades, String course);
}
