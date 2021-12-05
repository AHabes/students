package code;

import jdbc.Controller;
import jdbc.JDBCController;
import jdbc.url.MySQLURLBuilder;

import java.sql.SQLException;
import java.util.Scanner;

public class AppDriver {
    private static final int APP_EXIT = 0, APP_ADD_STUDENT = 1, APP_MOD_STUDENT = 2,
    APP_FIND_STUDENT_BY_ID = 3, APP_LIST_STUDENTS = 4, APP_DELETE_STUDENT = 5;

    private static final String dbUsername = "root";
    private static final String dbPassword = "11111111";
    private static final String dbHost = "localhost";
    private static final String dbPort = "3306";
    private static final String dbName = "students";

    private static Scanner in = new Scanner(System.in);
    StudentController stdCtr;

    public AppDriver() {
        stdCtr = new StudentController();

        Controller controller = new JDBCController();
        controller.setURLBuilder(new MySQLURLBuilder());

        controller.setDataBase(dbHost, dbPort, dbName)
                .addConnectionURLProperty("serverTimezone", "UTC").addConnectionURLProperty("useUnicode", "true")
                .setCredentials(dbUsername, dbPassword);

        controller.connect();
        controller.createStudentsDB();
        Student.setController(controller);
    }

    private static int displayAppMenu() {

        StringBuilder sb = new StringBuilder("Enter a selection from the following menu:\n");
        sb.append(APP_ADD_STUDENT + ". Add a new student.\n"
                + APP_MOD_STUDENT + ". Modify student details.\n"
                + APP_FIND_STUDENT_BY_ID + ". Find a student by identification number.\n"
                + APP_LIST_STUDENTS + ". Display list of all students.\n"
                + APP_DELETE_STUDENT + ". Delete a student.\n"
                + APP_EXIT + ". Exit program.\n");

        int choice = 0;
        try {
            System.out.println(sb);
            String input = in.nextLine();
            choice = Integer.parseInt(input);
        } catch (Exception ex) {
            choice = -1;
        }
        return choice;
    }

    public void startApp() throws SQLException {
        int choice = -1;
        do {
            choice = displayAppMenu();
            executeMenuItem(choice);
        } while (choice != APP_EXIT);
    }

    private void executeMenuItem(int choice) throws SQLException {
        switch (choice) {
            case APP_ADD_STUDENT -> stdCtr.addStudent();
            case APP_MOD_STUDENT -> stdCtr.updateStudentById();
            case APP_LIST_STUDENTS -> stdCtr.getAllStudents();
            case APP_FIND_STUDENT_BY_ID -> stdCtr.getStudentById();
            case APP_DELETE_STUDENT -> stdCtr.deleteStudentById();
            case APP_EXIT -> System.out.println("Good Bye!");
            default -> System.out.println("Unknown command!");
        }
    }
}
