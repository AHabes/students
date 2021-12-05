package jdbc;

import java.sql.SQLException;
import java.util.List;

import code.Student;
import javafx.collections.ObservableList;
import jdbc.url.JDBCUrl;

public interface Controller extends AutoCloseable {

    Controller setURLBuilder(JDBCUrl builder);

    Controller setDataBase(String address, String port, String catalog);

    Controller addConnectionURLProperty(String key, String value);

    Controller setCredentials(String user, String pass);

    Controller connect();

    boolean isConnected();

    boolean isLoggedIn();

    boolean createStudentsDB();

    void addStudent(Student student) throws SQLException;

    List<Student> getAllStudents() throws SQLException;

    Student getStudentById(int id) throws SQLException;

    boolean deleteStudentById(int id) throws SQLException;

    boolean updateStudentById(int id, Student student) throws SQLException;
}
