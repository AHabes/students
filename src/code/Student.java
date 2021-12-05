package code;

import jdbc.Controller;

import javax.persistence.Entity;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class Student {
    private static final String EMAIL_SUFFIX = "@algomail.com";
    private static Controller controller;

    private int studentId;
    private String firstName;
    private String lastName;
    private LocalDateTime dob;
    private String email;
    private double mt1Score;
    private double mt2Score;
    private double assignment1Score;
    private double assignment2Score;
    private double assignment3Score;
    private double assignment4Score;
    private double assignment5Score;
    private double finalScore;
    private String finalGrade;

    public Student() {
    }

    public Student(String firstName, String lastName, LocalDateTime dob, double mt1Score, double mt2Score,
                   double assignment1Score, double assignment2Score, double assignment3Score,
                   double assignment4Score, double assignment5Score) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;

        int month = dob.getMonthValue();
        DecimalFormat dFormatter = new DecimalFormat("00");
        this.email = firstName + dFormatter.format(Double.valueOf(month)) + lastName + EMAIL_SUFFIX;

        this.mt1Score = mt1Score;
        this.mt2Score = mt2Score;
        this.assignment1Score = assignment1Score;
        this.assignment2Score = assignment2Score;
        this.assignment3Score = assignment3Score;
        this.assignment4Score = assignment4Score;
        this.assignment5Score = assignment5Score;
    }


    public static void setController(Controller controller) {
        Student.controller = controller;
    }

    private double calculateFinalGrade(Student student) {
        return student.getMt1Score() * 0.25
                + student.getMt2Score() * 0.25
                + student.getAssignment1Score() * 0.10
                + student.getAssignment2Score() * 0.10
                + student.getAssignment3Score() * 0.10
                + student.getAssignment4Score() * 0.10
                + student.getAssignment5Score() * 0.10;
    }

    private String calculateLetterGrade(double grade) {
        String letterGrade = "";
        int finalGrade = (int) Math.round(grade);
        switch (finalGrade >= 90 ? 0
                : finalGrade >= 85 ? 1
                : finalGrade >= 80 ? 2
                : finalGrade >= 77 ? 3
                : finalGrade >= 73 ? 4
                : finalGrade >= 70 ? 5
                : finalGrade >= 67 ? 6
                : finalGrade >= 63 ? 7
                : finalGrade >= 60 ? 8
                : finalGrade >= 57 ? 9
                : finalGrade >= 53 ? 10
                : finalGrade >= 50 ? 11
                : 12) {
            case 0 -> letterGrade = "A+";
            case 1 -> letterGrade = "A";
            case 2 -> letterGrade = "A-";
            case 3 -> letterGrade = "B+";
            case 4 -> letterGrade = "B";
            case 5 -> letterGrade = "B-";
            case 6 -> letterGrade = "C+";
            case 7 -> letterGrade = "C";
            case 8 -> letterGrade = "C-";
            case 9 -> letterGrade = "D+";
            case 10 -> letterGrade = "D";
            case 11 -> letterGrade = "D-";
            case 12 -> letterGrade = "F";
            default -> System.out.println("Unknown command!");
        }
        return letterGrade;
    }

    public boolean addStudent(Student student) throws SQLException {
        double finalScore = calculateFinalGrade(student);
        String finalGrade = calculateLetterGrade(finalScore);
        student.setFinalScore(finalScore);
        student.setFinalGrade(finalGrade);
        controller.addStudent(student);
        return true;
    }


    //- List all students
    public List<Student> getAllStudents() throws SQLException {
        return controller.getAllStudents();
    }

    //- Fetch a single student by ID
    public Student getStudentById(int id) throws SQLException {
        return controller.getStudentById(id);
    }

    //- Change a single student information by ID
    public boolean updateStudentById(int id, Student student) throws SQLException {
        double finalScore = calculateFinalGrade(student);
        String finalGrade = calculateLetterGrade(finalScore);
        student.setFinalScore(finalScore);
        student.setFinalGrade(finalGrade);
        return controller.updateStudentById(id, student);
    }

    //- Remove a single student from the database by ID
    public boolean deleteStudentById(int id) throws SQLException {
        return controller.deleteStudentById(id);
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob=" + dob +
                ", email='" + email + '\'' +
                ", mt1Score=" + mt1Score +
                ", mt2Score=" + mt2Score +
                ", assignment1Score=" + assignment1Score +
                ", assignment2Score=" + assignment2Score +
                ", assignment3Score=" + assignment3Score +
                ", assignment4Score=" + assignment4Score +
                ", assignment5Score=" + assignment5Score +
                ", finalScore=" + finalScore +
                ", finalGrade='" + finalGrade + '\'' +
                '}';
    }


    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int id) {
        this.studentId = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getDob() {
        return dob;
    }

    public void setDob(LocalDateTime dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getMt1Score() {
        return mt1Score;
    }

    public void setMt1Score(double mt1Score) {
        this.mt1Score = mt1Score;
    }

    public double getMt2Score() {
        return mt2Score;
    }

    public void setMt2Score(double mt2Score) {
        this.mt2Score = mt2Score;
    }

    public double getAssignment1Score() {
        return assignment1Score;
    }

    public void setAssignment1Score(double assignment1Score) {
        this.assignment1Score = assignment1Score;
    }

    public double getAssignment2Score() {
        return assignment2Score;
    }

    public void setAssignment2Score(double assignment2Score) {
        this.assignment2Score = assignment2Score;
    }

    public double getAssignment3Score() {
        return assignment3Score;
    }

    public void setAssignment3Score(double assignment3Score) {
        this.assignment3Score = assignment3Score;
    }

    public double getAssignment4Score() {
        return assignment4Score;
    }

    public void setAssignment4Score(double assignment4Score) {
        this.assignment4Score = assignment4Score;
    }

    public double getAssignment5Score() {
        return assignment5Score;
    }

    public void setAssignment5Score(double assignment5Score) {
        this.assignment5Score = assignment5Score;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }
}
