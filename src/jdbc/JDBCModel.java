package jdbc;

import code.Student;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class JDBCModel {

    private static final String QUERY_CREATE_DB =
            "CREATE TABLE IF NOT EXISTS students (studentId int PRIMARY KEY AUTO_INCREMENT NOT NULL, firstName varchar(255), " +
                    "lastName varchar(255), dob date, email varchar (255), mt1Score double , mt2Score double," +
                    "assignment1Score double,assignment2Score double,assignment3Score double," +
                    "assignment4Score double, assignment5Score double, finalScore double, finalGrade varchar (5))";


    private static final String QUERY_ADD_STUDENT = "INSERT INTO students (firstName, lastName, dob, email, mt1Score, mt2Score," +
            "assignment1Score, assignment2Score, assignment3Score, assignment4Score, assignment5Score, finalScore, finalGrade ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String QUERY_GET_STUDENTS = "SELECT * FROM students";
    private static final String QUERY_GET_STUDENT_BY_ID = "SELECT * FROM students WHERE studentId=?";
    private static final String QUERY_DELETE_STUDENT_BY_ID = "DELETE FROM students WHERE studentId=?";
    private static final String QUERY_UPDATE_STUDENT_BY_ID = "UPDATE students SET firstName=?, lastName=?, dob=?," +
            "email=?,mt1Score=?, mt2Score=?, assignment1Score=?, assignment2Score=?, assignment3Score=?," +
            " assignment4Score=?, assignment5Score=?, finalScore=?, finalGrade=? WHERE studentId=?";


    private Connection connection;
    private String user;
    private String pass;

    public void setCredentials(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    public void connectTo(String url) throws SQLException, ClassNotFoundException {
        if (isConnected())
            close();
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, pass);
    }

    public boolean isConnected() throws SQLException {
        return !(connection == null || connection.isClosed() || !connection.isValid(60));
    }

    private void hasValidConnection() throws SQLException {
        if (!isConnected())
            throw new SQLException("No connection to DB");
    }

    public void close() throws SQLException {
        if (connection != null)
            connection.close();
    }

    public void createStudentsDB() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(QUERY_CREATE_DB)) {
            ps.execute();
        }
    }

    public void addStudent(Student student) throws SQLException {
        hasValidConnection();
        try (PreparedStatement ps = connection.prepareStatement(QUERY_ADD_STUDENT, Statement.RETURN_GENERATED_KEYS)) {
            setDetails(student, ps);
            ps.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        hasValidConnection();

        List<Student> students = new LinkedList();
        try (PreparedStatement ps = connection.prepareStatement(QUERY_GET_STUDENTS)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    recordDetails(student, rs);
                    students.add(student);
                }
                rs.close();
                ps.close();
            }
        }
        return students;
    }

    public Student getStudentById(int id) throws SQLException {
        hasValidConnection();

        Student student = new Student();
        try (PreparedStatement ps = connection.prepareStatement(QUERY_GET_STUDENT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    recordDetails(student, rs);
                rs.close();
                ps.close();
            }
        }
        return student;
    }

    public boolean deleteStudentById(int id) throws SQLException {
        hasValidConnection();
        try (PreparedStatement ps = connection.prepareStatement(QUERY_DELETE_STUDENT_BY_ID)) {
            ps.setInt(1, id);
            try {
                ps.execute();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void recordDetails(Student student, ResultSet rs) throws SQLException {
        student.setStudentId(rs.getInt("studentId"));
        student.setFirstName(rs.getString("firstName"));
        student.setLastName(rs.getString("lastName"));

        Instant instant = Instant.ofEpochMilli(rs.getDate("dob").getTime());
        student.setDob(LocalDateTime.ofInstant(instant, ZoneOffset.UTC));

        student.setEmail(rs.getString("email"));
        student.setLastName(rs.getString("lastName"));
        student.setMt1Score(rs.getDouble("mt1Score"));
        student.setMt2Score(rs.getDouble("mt2Score"));
        student.setAssignment1Score(rs.getDouble("assignment1Score"));
        student.setAssignment2Score(rs.getDouble("assignment2Score"));
        student.setAssignment3Score(rs.getDouble("assignment3Score"));
        student.setAssignment4Score(rs.getDouble("assignment4Score"));
        student.setAssignment5Score(rs.getDouble("assignment5Score"));
        student.setFinalScore(rs.getDouble("finalScore"));
        student.setFinalGrade(rs.getString("finalGrade"));
    }

    public boolean updateStudentById(int id, Student newStudent) throws SQLException {
        hasValidConnection();

        try (PreparedStatement ps = connection.prepareStatement(QUERY_UPDATE_STUDENT_BY_ID)) {
            setDetails(newStudent, ps);
            ps.setInt(14, id);
            ps.executeUpdate();
        }
        return true;
    }

    private void setDetails(Student student, PreparedStatement ps) throws SQLException {
        ps.setString(1, student.getFirstName());
        ps.setString(2, student.getLastName());

        LocalDateTime dob = student.getDob();
        dob.atZone(ZoneId.of("UTC"));
        ps.setObject(3, dob);

        ps.setString(4, student.getEmail());
        ps.setDouble(5, student.getMt1Score());
        ps.setDouble(6, student.getMt2Score());
        ps.setDouble(7, student.getAssignment1Score());
        ps.setDouble(8, student.getAssignment2Score());
        ps.setDouble(9, student.getAssignment3Score());
        ps.setDouble(10, student.getAssignment4Score());
        ps.setDouble(11, student.getAssignment5Score());
        ps.setDouble(12, student.getFinalScore());
        ps.setString(13, student.getFinalGrade());
    }
}

