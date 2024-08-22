package SOLID.bad.i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Professor implements University{

    List<Student> students;
    List<String> courses;

    public Professor(){
        students = new ArrayList<>();
        courses = new ArrayList<>();
    }

    public void SetStudents(List<Student> students){
        for (Student stu : students){
            this.students.add(stu);
        }
        return;
    }

    @Override
    public boolean registerForCourse(String course) {
        throw new UnsupportedOperationException("Professors cannot register for courses.");
    }

    @Override
    public void viewGrades() {

        if (students == null || students.isEmpty()){
            return;
        }

        for (Student currStudent : students){
            currStudent.viewGrades();
        }

        return;
    }

    @Override
    public void updateGrades(Map<String, Integer> grade) {
        throw new UnsupportedOperationException("Professors cannot upgrade grade course because they didn't enroll in any course!");
    }

    @Override
    public boolean dropCourse(String course) {
        throw new UnsupportedOperationException("Professors cannot able to drop course.");
    }

    @Override
    public boolean teachCourse(String course) {

        for (String currCourse : courses){
            if (currCourse.equals(currCourse)){
                System.out.println("Professor is already teach " + currCourse + " course!");
                return false;
            }
        }

        courses.add(course);
        System.out.println("Course is assign to professor successfully!");
        return true;
    }

    @Override
    public void gradeAssignment(Map<Student, Integer> grades, String course) {
        if (students == null || students.isEmpty()){
            return;
        }

        for (Map.Entry<Student, Integer> grade : grades.entrySet()){

            String studentName = grade.getKey().getName();

            for (Student student : students){
                if (student.getName().equals(studentName)){
                    Map<String, Integer> studentGrades = new HashMap<>();
                    studentGrades.put(course,grade.getValue());
                    student.updateGrades(studentGrades);
                }
            }
        }

        return;
    }

    public List<Student> getStudents() {
        return this.students;
    }

    public List<String> getCourses() {
        return this.courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }
}
