package code;

import jdbc.Controller;
import jdbc.JDBCController;
import jdbc.url.MySQLURLBuilder;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class driver {


    public static void main(String[] args) throws SQLException {

        Controller controller = new JDBCController();
        controller.setURLBuilder(new MySQLURLBuilder());

        final String user = "root";
        final String pass = "11111111";
        final String host = "localhost";
        final String port = "3306";
        final String dbName = "students";

        controller.setDataBase(host, port, dbName)
                .addConnectionURLProperty("serverTimezone", "UTC").addConnectionURLProperty("useUnicode", "true")
                .setCredentials(user, pass);

        controller.connect();

        controller.createStudentsDB();

        Student.setController(controller);

        //     dob.atZone(ZoneId.of("UTC"));

        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2015, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);

        String fn = getSaltString(3);
        String ln = getSaltString(3);
        LocalDateTime dob = randomDate.atStartOfDay();

        Random r = new Random();
        int rangeMin = 10;
        int rangeMax = 100;

        Student s1 = new Student(fn, ln, dob,
                Math.round((100 * r.nextDouble()) * 100.0) / 100.0,
                Math.round((100 * r.nextDouble()) * 100.0) / 100.0,
                Math.round((rangeMin + (rangeMax - rangeMin) * r.nextDouble()) * 100.0) / 100.0,
                Math.round((rangeMin + (rangeMax - rangeMin) * r.nextDouble()) * 100.0) / 100.0,
                Math.round((rangeMin + (rangeMax - rangeMin) * r.nextDouble()) * 100.0) / 100.0,
                Math.round((rangeMin + (rangeMax - rangeMin) * r.nextDouble()) * 100.0) / 100.0,
                Math.round((rangeMin + (rangeMax - rangeMin) * r.nextDouble()) * 100.0) / 100.0);

        System.out.println(s1.addStudents());

        List<Student> ls = Student.getAllStudents();

        ls.forEach(System.out::println);

        System.out.println("========= ============= ============");
        Student res = Student.getStudentById(1);
        boolean res_ = Student.deleteStudentById(8);
        System.out.println(res.toString());
    }

    protected static String getSaltString(int len) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < len) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}
