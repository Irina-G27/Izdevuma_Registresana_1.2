import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    //example
    private static final String DB_URL = "jdbc:mysql://localhost:3306/izreg";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "17st27_70ar!Go21";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}