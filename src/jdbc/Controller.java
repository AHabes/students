package jdbc;

import java.sql.SQLException;
import java.util.List;

import code.Student;
import javafx.collections.ObservableList;
import jdbc.url.JDBCUrl;

public interface Controller extends AutoCloseable {

    boolean loginWith( String username, String password);

    Controller setURLBuilder( JDBCUrl builder);

    Controller setDataBase( String address, String port, String catalog);

    Controller addConnectionURLProperty( String key, String value);

    Controller setCredentials( String user, String pass);

    Controller connect();

    List< String> getColumnNamesOfGlucosEntries();

    List< List< Object>> findAllGlucoseNumbersForLoggedAccount() throws Exception;

    boolean isConnected();

    boolean isLoggedIn();

    void updateInfo( String name, String yob, String weight);

    ObservableList< String> getEntryTypes();

    void addGlucoseValue( String string, double glucose);

    boolean createStudentsDB();

    void addStudent(Student student) throws SQLException;

    List<Student> getAllStudents() throws SQLException;

    Student getStudentById(int id) throws SQLException;

    boolean deleteStudentById(int id) throws SQLException;
}
