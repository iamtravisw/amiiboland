/*
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class AuthHelper {

  // Just setting basic structure
  private static String generateSalt() {
    return BCrypt.gensalt();
  }

  private static String generateSecurePassword(String password, String salt) {
    return BCrypt.hashpw(password, salt);
  }

  private static HashMap<String, String> getUserDetails(String email) {
    // TODO: Fetch user from database and return their data as a HashMap
    // We need to fetch ID, email, salt, and securePassword
  }

  public static int tryLogin(String email, String password) {
    // TODO: Find user using email
    HashMap<String, String> user = getUserDetails(email);

    String securePassword = generateSecurePassword(password, user["salt"]);

    // Should be true if they're equal, false otherwise
    boolean correctPassword = securePassword.equals(user["securePassword"]);

    if (correctPassword) {
      // TODO: Convert it to an integer
      return user["id"];
    }

    return -1;
  }

  private static int saveNewUser(String email, String securePassword, String salt) {

    String dbURL = System.getenv("DB_URL");
    String dbUser = System.getenv("DB_USER");
    String dbPassword = System.getenv("DB_PASSWORD");

    try {
    Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
    if (conn != null) {
    }

    // sql insert statement
    String storeUser = "insert into Users (UserName, Email, securePassword)"
            + "values (?, ?, ?)";
    PreparedStatement psInsertUser = conn.prepareStatement(storeUser);

    psInsertUser.setString(1, "UserID");             // AmiiboID
    psInsertUser.setString(2, "Email");              // UserID
    psInsertUser.setString(3, "securePassword");     // Collected Y/N

    ResultSet rsInsertUser = psInsertUser.executeQuery();   // Execute

    System.out.println(rsInsertUser.next());



    }
    catch (Exception e)
    {
      System.out.println(e); // Did it work? If not, why.
      return -1;
    }

    // TODO: Return User ID
  }

  public static int register(String email, String password) {
    // Generate salt
    String salt = generateSalt();

    // Append salt to password and hash them both
    String securePassword = generateSecurePassword(password, salt);

    return saveNewUser(email, securePassword, salt);
  }
}

*/