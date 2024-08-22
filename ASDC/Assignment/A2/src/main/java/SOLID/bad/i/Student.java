package SOLID.bad.i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student implements University{

    private String name;
    private String BooNumber;
    private List<String> courses;
    Map<String, Integer> grades;

    public Student(String name, String BooNumber){
        this.name = name;
        this.BooNumber = BooNumber;
        courses = new ArrayList<>();
        grades = new HashMap<>();
    }


    @Override
    public boolean registerForCourse(String course) {

        boolean isRegister = false;

        for (String currCourse : courses){
            if (currCourse.equals(course)){
                System.out.println("Student is already registered for this course!");
                return false;
            }
        }

        courses.add(course);
        System.out.println("Student Registered Successfully!");
        return true;
    }

    @Override
    public void viewGrades() {

        if (grades.isEmpty()){
            System.out.println("Student have no grades to show!");
            return;
        }

        for (Map.Entry<String, Integer> grade : grades.entrySet()){
            System.out.println("Subject: " + grade.getKey() + ", and Grade : " + grade);
        }

        return;
    }

    @Override
    public void updateGrades(Map<String, Integer> grade){

        for (Map.Entry<String, Integer> currGrade : grade.entrySet()){

            boolean isCourseExist = false;

            for (String course : courses){
                if (course.equals(currGrade.getKey())) {
                    isCourseExist = true;
                    this.grades.put(currGrade.getKey(), currGrade.getValue());
                }
            }

            if (!isCourseExist){
                System.out.println("Grade not updated because student is not registered for the " + currGrade.getKey() + " course");
            }
        }

        return;
    }

    @Override
    public boolean dropCourse(String course) {

        boolean isDrop = false;

        for (String currCourse : courses){
            if (currCourse.equals(course)){
                courses.remove(course);
                System.out.println("Student is already registered for this course!");
                return true;
            }
        }

        System.out.println("Student is not registered for this course!");
        return false;
    }

    @Override
    public boolean teachCourse(String course) {
        throw new UnsupportedOperationException("Students cannot teach courses.");
    }

    @Override
    public void gradeAssignment(Map<Student, Integer> grades, String course) {
        throw new UnsupportedOperationException("Students cannot grade courses.");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBooNumber() {
        return BooNumber;
    }

    public void setBooNumber(String booNumber) {
        BooNumber = booNumber;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public Map<String, Integer> getGrades() {
        return grades;
    }

    public void setGrades(Map<String, Integer> grades) {
        this.grades = grades;
    }
}
