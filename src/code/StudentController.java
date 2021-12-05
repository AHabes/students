package code;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class StudentController {
    private static Scanner in = new Scanner(System.in);

    private Student student;

    public StudentController() {
        student = new Student();
    }

    private static String getResponseTo(String s) {
        System.out.print(s);
        return (in.nextLine());
    }

    private Student readStudentDetails() {
        String firstName = getResponseTo("First name:\n");
        String lastName = getResponseTo("Last name:\n");
        String dob = getResponseTo("Date of birth in (YYYY-MM-DD) format:\n");
        String mt1Score = getResponseTo("First midterm score:\n");
        String mt2Score = getResponseTo("Second midterm score:\n");
        String assignment1Score = getResponseTo("First assignment score:\n");
        String assignment2Score = getResponseTo("Second assignment score:\n");
        String assignment3Score = getResponseTo("Third assignment score:\n");
        String assignment4Score = getResponseTo("Fourth assignment score:\n");
        String assignment5Score = getResponseTo("Fifth assignment score:\n");

        LocalDate aLD = LocalDate.parse(dob);
        System.out.println("dob: " + aLD);
        LocalDateTime dateOfBirth = aLD.atStartOfDay();

        return new Student(firstName, lastName, dateOfBirth,
                Double.parseDouble(mt1Score),
                Double.parseDouble(mt2Score),
                Double.parseDouble(assignment1Score),
                Double.parseDouble(assignment2Score),
                Double.parseDouble(assignment3Score),
                Double.parseDouble(assignment4Score),
                Double.parseDouble(assignment5Score));
    }

    public void addStudent() throws SQLException {
        System.out.println("New Student Entry");
        student.addStudent(readStudentDetails());
        System.out.println("The new student has been added successfully.");
    }

    public void updateStudentById() throws SQLException {
        String id = getResponseTo("Please enter the identification number of the student to be updated\n");
        System.out.println("Please enter the updated details ");
        if (student.updateStudentById(Integer.parseInt(id), readStudentDetails())) {
            System.out.println("Student has been updated");
        } else
            System.out.println("Student can't be updated");
    }

    public void getAllStudents() throws SQLException {
        List<Student> students = student.getAllStudents();
        for (Student s : students) {
            System.out.println(s.toString());
        }
    }

    public void getStudentById() throws SQLException {
        String id = getResponseTo("Please enter the identification number of the student:\n");
        Student s = student.getStudentById(Integer.parseInt(id));
        if (s.getFirstName() == null && s.getLastName() == null & s.getDob() == null)
            System.out.println("No student matches the entered id.");
        else
            System.out.println(s.toString() + "\n");
    }

    public void deleteStudentById() throws SQLException {
        String id = getResponseTo("Please enter the identification number of the student to be removed:\n");
        if (student.deleteStudentById(Integer.parseInt(id))) {
            System.out.println("Student has been deleted");
        } else
            System.out.println("Student can't be deleted");
    }
}
