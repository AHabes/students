package jdbc;

import code.Student;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

public class JDBCModel {

    private static final String[] COL_NAMES_GOLUCOS = {"Id", "EntryType", "GlucoseValue", "TakenAt"};
    private static final String[] COL_NAMES_ACCOUNT = {"Name", "YearOfBirth", "Weight"};

    private static final String QUERY_ENTRYTYPE_SELECT = "SELECT EntryType FROM entrytype";


    private static final String QUERY_CREATE_DB =
            "CREATE TABLE IF NOT EXISTS students (studentId int PRIMARY KEY AUTO_INCREMENT NOT NULL, firstName varchar(255), " +
                    "lastName varchar(255), dob date, email varchar (255), mt1Score double , mt2Score double," +
                    "assignment1Score double,assignment2Score double,assignment3Score double," +
                    "assignment4Score double, assignment5Score double)";


    private static final String QUERY_ADD_STUDENT = "INSERT INTO students (firstName, lastName, dob, email, mt1Score, mt2Score," +
            "assignment1Score, assignment2Score, assignment3Score, assignment4Score, assignment5Score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String QUERY_GET_STUDENTS = "SELECT * FROM students";
    private static final String QUERY_GET_STUDENT_BY_ID = "SELECT * FROM students WHERE studentId=?";
    private static final String QUERY_DELETE_STUDENT_BY_ID = "DELETE FROM students WHERE studentId=?";

    private static final String QUERY_ACCOUNT_SELECT = "SELECT Name, YearOfBirth, Weight FROM account where id=?";
    private static final String QUERY_ACCOUNT_UPDATE = "UPDATE account SET Name = ?, YearOfBirth = ?, Weight = ? WHERE Id = ?";
    private static final String QUERY_VALIDATE = "SELECT AccountId FROM security where security.username=? and security.password=?";
    private static final String QUERY_GLUCOSE_INSERT = "INSERT INTO glucosevalue(EntryTypeId,AccountId,GlucoseValue,TakenAt)VALUES((select id from entrytype where entrytype=?),?,?,now())";
    private static final String QUERY_GLUCOSE_NUMBERS = "SELECT g.Id, (SELECT EntryType FROM entrytype where g.EntryTypeId=Id ) as EntryType, g.GlucoseValue, g.TakenAt FROM glucosevalue g where AccountId=?";

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

    public void addGlucoseValue(String entryType, int activeAccountId, double glucose) throws SQLException {

        hasValidConnection();

        try (PreparedStatement ps = connection.prepareStatement(QUERY_GLUCOSE_INSERT)) {
            ps.setString(1, entryType);
            ps.setInt(2, activeAccountId);
            ps.setDouble(3, glucose);

            ps.executeUpdate();
        }
    }

    public List<String> getEntryTypes() throws SQLException {
        hasValidConnection();

        List<String> types = new LinkedList<>();

        try (PreparedStatement ps = connection.prepareStatement(QUERY_ENTRYTYPE_SELECT)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    types.add(rs.getString(1));
                }
            }
        }
        return types;
    }

    public List<String> getAccountInfoFor(int activeAccountId) throws SQLException {
        hasValidConnection();

        List<String> info = new LinkedList<>();

        try (PreparedStatement ps = connection.prepareStatement(QUERY_ACCOUNT_SELECT)) {
            ps.setInt(1, activeAccountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    for (String col : COL_NAMES_ACCOUNT) {
                        info.add(rs.getString(col));
                    }
                }
            }
        }
        return info;
    }

    public void updateInfo(String name, String yob, String weight, int activeAccountId) throws SQLException {
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(yob, "Year of Birth cannot be null");
        Objects.requireNonNull(weight, "Weight cannot be null");

        hasValidConnection();

        try (PreparedStatement ps = connection.prepareStatement(QUERY_ACCOUNT_UPDATE)) {
            ps.setString(1, name);
            ps.setString(2, yob);
            ps.setString(3, weight);
            ps.setInt(4, activeAccountId);

            ps.executeUpdate();
        }
    }

    public int loginWith(String username, String password) throws SQLException {
        Objects.requireNonNull(username, "Username cannot be null");
        Objects.requireNonNull(password, "Password cannot be null");

        hasValidConnection();

        try (PreparedStatement ps = connection.prepareStatement(QUERY_VALIDATE)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public List<String> getColumnNames() {
        return List.of(COL_NAMES_GOLUCOS);
    }

    public List<List<Object>> getAllGlucoseNumbers(int activeAccountId) throws SQLException {
        hasValidConnection();

        List<List<Object>> rows = new LinkedList<>();

        try (PreparedStatement ps = connection.prepareStatement(QUERY_GLUCOSE_NUMBERS)) {
            ps.setInt(1, activeAccountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    List<Object> cols = new LinkedList<>();
                    for (String col : COL_NAMES_GOLUCOS) {
                        cols.add(rs.getObject(col));
                    }
                    rows.add(cols);
                }
            }
        }
        return rows;
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
            System.out.println("id: " + id);
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
    }

}

