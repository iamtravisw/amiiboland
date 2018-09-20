import com.mysql.jdbc.Statement;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;

public class AuthHelper {

    // Variables
    public int userID;
    public String name;
    public String userName;
    public String email;
    public String password;

    /*
    // Setters and Getters
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getUserID() {
        return userID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setuserName(String userID) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    */

    private static String generateSalt() {
        return BCrypt.gensalt();
    }
    private static String generateSecurePassword(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }
    private static HashMap<String, String> getUserDetails(String email) {
        // TODO: Fetch user from database and return their data as a HashMap
        return new HashMap<String, String>();
    }
    public static int tryLogin(String email, String password) {
        // TODO: Find user using email
        HashMap<String, String> user = getUserDetails(email);
        String securePassword = generateSecurePassword(password, user.get("salt"));

        // Should be true if they're equal, false otherwise
        boolean correctPassword = securePassword.equals(user.get("securePassword"));
        if (correctPassword) {
            return Integer.parseInt(user.get("id"));
        }
        return -1;
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
                    "securePassword, ModUser, ModDate, AddUser, AddDate)"
                    + "values (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psInsertUser = conn.prepareStatement(storeUser, Statement.RETURN_GENERATED_KEYS);
            psInsertUser.setString(1, name);                    // Name
            psInsertUser.setString(2, userName);                // UserName
            psInsertUser.setString(3, email);                   // UserID
            psInsertUser.setString(4, securePassword);          // securePassword
            psInsertUser.setString(5, "root@localhost");     // ModUser
            psInsertUser.setString(6, modDate.toString());      // ModDate
            psInsertUser.setString(7, "root@localhost");     // AddUser
            psInsertUser.setString(8, addDate.toString());      // AddDate
            psInsertUser.execute();                                // Execute

            // Get userID of the last row
            ResultSet rs = psInsertUser.getGeneratedKeys();
            if (rs.next()) {
                int lastUserID = rs.getInt(1);
                System.out.println("UserID: " + lastUserID);

                return lastUserID;
            }
        }
        catch (Exception e)
        {
            System.out.println(e); // Did it work? If not, why.
            return -1;
        }
        // TODO: Return User ID

        return -1;
    }
    public int register(String email, String password, String userName, String name) {
        // Generate salt
        String salt = generateSalt();
        // Append salt to password and hash them both
        String securePassword = generateSecurePassword(password, salt);
        return saveNewUser(name, email, securePassword, userName, salt);
    }
}