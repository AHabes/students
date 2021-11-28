package jdbc;

import java.sql.SQLException;
import java.util.List;

import code.Student;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbc.url.JDBCUrl;
import jdbc.url.MySQLURLBuilder;

public class JDBCController implements Controller {

    private JDBCUrl builder;
    private JDBCModel model;
    private boolean isLoggedIn;
    private int activeAccountId;
    private BooleanProperty updateTable;
    private ObservableList<String> entryTypes;

    public JDBCController() {
        builder = new MySQLURLBuilder(); // JDBCUrl is an abstract it can't be instantiated
        model = new JDBCModel();
        isLoggedIn = false;
        activeAccountId = -1;

        updateTable = new SimpleBooleanProperty();
        updateTable.setValue(false);
        entryTypes = FXCollections.observableArrayList();

    }

    @Override
    public void close() throws Exception {
        model.close();

    }

    @Override
    public boolean loginWith(String username, String password) {
        isLoggedIn = false;
        try {
            activeAccountId = model.loginWith(username, password);
            isLoggedIn = activeAccountId != -1;
            if (isLoggedIn) {
                List<String> info = model.getAccountInfoFor(activeAccountId);
                updateTable.setValue(!updateTable.getValue()); //negate the value if updateTable which was false
                getEntryTypes();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return isLoggedIn;
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
    public List<String> getColumnNamesOfGlucosEntries() {
        return model.getColumnNames();
    }

    @Override
    public List<List<Object>> findAllGlucoseNumbersForLoggedAccount() throws Exception {
        hasValidLogin(); // should only work if login is true
        try {
            return model.getAllGlucoseNumbers(activeAccountId);
        } catch (SQLException e) {
            throw new IllegalStateException("Validation failed", e);
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
    public void updateInfo(String name, String yob, String weight) {
        try {
            model.updateInfo(name, yob, weight, activeAccountId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException();

        }
    }

    @Override
    public ObservableList<String> getEntryTypes() {
        if (isConnected()) {
            try {
                entryTypes.clear();
                entryTypes.addAll(model.getEntryTypes());
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IllegalStateException();
            }
        }
        return entryTypes;
    }

    @Override
    public void addGlucoseValue(String string, double glucose) {
        try {
            System.out.print(
                    "addGlucoseValue " + string + " glucose: " + glucose + "activeAccountId: " + activeAccountId);
            model.addGlucoseValue(string, activeAccountId, glucose);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException();

        }
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
        System.out.println("adding the student");
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
    public boolean deleteStudentById(int id) throws SQLException {
        return model.deleteStudentById(id);
    }


}