import java.sql.Connection;
import java.sql.DriverManager;

public class ConnHelper {
    public Connection db() {
        String dbURL = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");
        try {
        Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
        if (conn != null) {
        }
        return conn;
        } catch ( Exception e ) {
            System.err.println(e);
        }
        return null;
    }
}