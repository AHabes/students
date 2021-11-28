package code;

import jdbc.Controller;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class Student {
    private static final String EMAIL_SUFFIX = "@algomail.com";
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1000);
    private static Controller controller;
    //   @Id
    //   @GeneratedValue(strategy = GenerationType.AUTO)
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

    //- List all students
    public static List<Student> getAllStudents() throws SQLException {
        return controller.getAllStudents();
    }

    //- Fetch a single student by ID
    public static Student getStudentById(int id) throws SQLException {
        return controller.getStudentById(id);
    }

    //- Remove a single student from the database by ID
    public static boolean deleteStudentById(int id) throws SQLException {
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

//- Change a single student information by ID

    public boolean addStudents() throws SQLException {
        controller.addStudent(this);
        return true;
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
