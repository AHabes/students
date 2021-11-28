//import jdbc.Controller;
//import jdbc.JDBCController;
//import jdbc.url.MySQLURLBuilder;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.Month;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//public class driver {
//
//    public static void main(String[] args) throws SQLException {
//
//        Controller controller = new JDBCController();
//        controller.setURLBuilder(new MySQLURLBuilder());
//
//
//        final String address = "jdbc:mysql://localhost:3306/books?useUnicode=true&useJDBCCompliantTimezoneShift = " +
//                "true & useLegacyDatetimeCode = false & serverTimezone = UTC ";
//        final String user = "root";
//        final String pass = "11111111";
//        final String host = "localhost";
//        final String port = "3306";
//        final String dbName = "students";
//
//
//        controller.setDataBase(host, port, dbName)
//                .addConnectionURLProperty("serverTimezone", "UTC").addConnectionURLProperty("useUnicode", "true")
//                .setCredentials(user, pass);
//
//        //   controller.addConnectionURLProperty("createDatabaseIfNotExist", "true");
//        controller.connect();
//
//        controller.createStudentsDB();
//
//        Student.setController(controller);
//
//        Student s1 = new Student();
//
//        s1.addStudents();
//        // Student s2 = new Student();
//        // Student s4 = new Student();
//        // Student s5 = new Student();
//        // Student s6 = new Student();
//        // Student s7 = new Student();
//
//        LocalDate dob = LocalDate.of(2014, 12, 11);
//        Student s8 = new Student("firstName", "lastName", dob);
//        List<Student> ls = Student.getStudents();
//        System.out.println(ls);
//
//        System.out.println(s1);
//
//    }
//}
