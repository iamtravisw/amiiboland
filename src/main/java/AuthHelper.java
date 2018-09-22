import com.mysql.jdbc.Statement;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class AuthHelper {
    
    private static String generateSalt() {
        return BCrypt.gensalt();
    }
    private static String generateSecurePassword(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }
    private static HashMap<String, String> getUserDetails(String email) {
        System.out.println("Starting getUserDetails module...");
        String dbURL = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");
        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            String checkExists = "SELECT Email, salt, securePassword, UserID, UserName FROM Users WHERE Email = ? LIMIT 1";
            PreparedStatement psEmail = conn.prepareStatement(checkExists);
            psEmail.setString(1, email);                                  // Email
            ResultSet rs = psEmail.executeQuery();                          // Execute
            // If the row does not exist, insert a new row for this user
            if (!rs.isBeforeFirst()) {
                System.out.println(email + " does not exist... ");
            } else {
                System.out.println("Login found for: " + email);
                rs.first();
                HashMap<String, String> map = new HashMap<String, String>();
                String mapEmail = rs.getString("Email");
                String mapSalt = rs.getString("salt");
                String mapSecurePassword = rs.getString("securePassword");
                String mapUserID = rs.getString("UserID");
                String mapUserName = rs.getString("UserName");
                    map.put("eMail", mapEmail);
                    map.put("salt", mapSalt);
                    map.put("securePassword", mapSecurePassword);
                    map.put("userID", mapUserID);
                    map.put("userName", mapUserName);
                System.out.println(mapEmail+" / "+mapSalt+" / "+mapSecurePassword+" / "+mapUserID+" / "+mapUserName);
                // System.out.println(map); // for troubleshooting
                return map;
            }
        } catch (Exception e) {
            System.out.println(e); // Did it work? If not, why.
        }
        return null; // need to return something
    }
    public static int tryLogin(String email, String password) {
        System.out.println("Starting tryLogin module...");
        HashMap<String, String> user = getUserDetails(email);
        System.out.println("tryLogin retrieved email: " + user.get("eMail") + " and salt: "+ user.get("salt"));
        String securePassword = generateSecurePassword(password, user.get("salt"));
        // Should be true if they're equal, false otherwise
        boolean correctPassword = securePassword.equals(user.get("securePassword"));
        if (correctPassword) {
            System.out.println("Password Match: " +correctPassword+ " for " +email+ " with " +securePassword);
            return Integer.parseInt(user.get("userID"));
        } else {
            System.out.println("Password Match: " + correctPassword);
            return Integer.parseInt(user.get("userID")); // return something
        }
    }
    private static int saveNewUser(String name, String email, String securePassword, String userName, String salt) {
        Date addDate = new Date();
        Date modDate = new Date();
        String dbURL = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");
        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            // sql insert statement
            String storeUser = "insert into Users (Name, UserName, Email, " +
                    "securePassword, salt, ModUser, ModDate, AddUser, AddDate)"
                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psInsertUser = conn.prepareStatement(storeUser, Statement.RETURN_GENERATED_KEYS);
            psInsertUser.setString(1, name);                    // Name
            psInsertUser.setString(2, userName);                // UserName
            psInsertUser.setString(3, email);                   // UserID
            psInsertUser.setString(4, securePassword);          // securePassword
            psInsertUser.setString(5, salt);                    // salt
            psInsertUser.setString(6, "root@localhost");     // ModUser
            psInsertUser.setString(7, modDate.toString());      // ModDate
            psInsertUser.setString(8, "root@localhost");     // AddUser
            psInsertUser.setString(9, addDate.toString());      // AddDate
            psInsertUser.execute();                                // Execute
            // Get userID of the last row
            ResultSet rs = psInsertUser.getGeneratedKeys();
            if (rs.next()) {
                int lastUserID = rs.getInt(1);
                System.out.println("UserID: " + lastUserID);
                return lastUserID;
            }
        } catch (Exception e) {
            System.out.println(e); // Did it work? If not, why.
            return -1; // return something
        }
        return -1; // Need to return something
    }
    public static int register(String email, String password, String userName, String name) {
        // Generate salt
        String salt = generateSalt();
        // Append salt to password and hash them both
        String securePassword = generateSecurePassword(password, salt);
        return saveNewUser(name, email, securePassword, userName, salt);
    }
}