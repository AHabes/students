package code;

import java.sql.SQLException;

public class AppLauncher {

    public static void main(String[] args) throws SQLException {
        AppDriver app = new AppDriver();
        app.startApp();
    }
}
