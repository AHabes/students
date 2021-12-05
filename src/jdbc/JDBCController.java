package jdbc;

import code.Student;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import jdbc.url.JDBCUrl;
import jdbc.url.MySQLURLBuilder;

import java.sql.SQLException;
import java.util.List;

public class JDBCController implements Controller {

    private JDBCUrl builder;
    private JDBCModel model;
    private boolean isLoggedIn;

    public JDBCController() {
        builder = new MySQLURLBuilder(); // JDBCUrl is an abstract it can't be instantiated
        model = new JDBCModel();
        isLoggedIn = false;
    }

    @Override
    public void close() throws Exception {
        model.close();

    }

    private void hasValidLogin() {
        if (!isLoggedIn())
            throw new IllegalStateException("Not logged in");
    }

    @Override
    public Controller setURLBuilder(JDBCUrl builder) {
        this.builder = builder;
        return this;
    }

    @Override
    public Controller setDataBase(String address, String port, String catalog) {
        builder.setURL(address, port, catalog);
        return this;
    }

    @Override
    public Controller addConnectionURLProperty(String key, String value) {
        builder.addURLProperty(key, value);
        return this;
    }

    @Override
    public Controller setCredentials(String user, String pass) {
        model.setCredentials(user, pass);
        return this;
    }

    @Override
    public Controller connect() {
        try {
            model.connectTo(builder.getURL());
            return this;
        } catch (SQLException | ClassNotFoundException e) {
            throw new IllegalStateException("Failed to connect", e);
        }
    }

    @Override
    public boolean isConnected() {
        try {
            return model.isConnected();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to check for connection", e);

        }
    }

    @Override
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    @Override
    public boolean createStudentsDB() {
        try {
            model.createStudentsDB();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException();

        }
    }

    @Override
    public void addStudent(Student student) throws SQLException {
        model.addStudent(student);
    }

    @Override
    public List<Student> getAllStudents() throws SQLException {
        return model.getAllStudents();
    }

    @Override
    public Student getStudentById(int id) throws SQLException {
        return model.getStudentById(id);
    }

    @Override
    public boolean updateStudentById(int id, Student student) throws SQLException {
        return model.updateStudentById(id, student);
    }

    @Override
    public boolean deleteStudentById(int id) throws SQLException {
        return model.deleteStudentById(id);
    }

}