package SOLID.good.i;

import java.util.Map;

public interface UniversityProfessor {

    void viewGrades();
    boolean teachCourse(String course);
    void gradeAssignment(Map<Student, Integer> grades, String course);
}
