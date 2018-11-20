package app.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    public static String connectionString = "jdbc:postgresql://localhost:5433/recommendation";
    public static String dbUser = "postgres";
    public static String dbPassword = "password";

    public static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
