package SOLID.bad.i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverClass {

    public static void main(String[] args) {

        Student student1 = new Student("Jay", "B00982253");
        student1.registerForCourse("Math");
        student1.registerForCourse("Physics");


        Map<String, Integer> gradesToUpdate = new HashMap<>();
        gradesToUpdate.put("Math", 85);
        gradesToUpdate.put("Physics", 90);
        student1.updateGrades(gradesToUpdate);

        student1.viewGrades();

        System.out.println(student1.dropCourse("Physics"));

        Professor professor1 = new Professor();
        professor1.teachCourse("Physics");
        List<Student> students = new ArrayList<>();
        students.add(student1);
        professor1.SetStudents(students);

        professor1.viewGrades();

        Map<Student, Integer> assignmentGradesForProfessor = new HashMap<>();
        assignmentGradesForProfessor.put(student1, 85);
        professor1.gradeAssignment(assignmentGradesForProfessor, "Physics");
    }

}
