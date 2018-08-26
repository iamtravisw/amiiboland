import java.sql.*;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;

public class RemoveAmiibo {
    public static void main(String[] args) {

        // Variables from REST
        int userID = 1;
        int amiiboID = 1;

        // Date conversion for MySQL
        Date adddate = new Date();
        Date moddate = new Date();

        // Connect to the Database
        String dbURL = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {


                // Check to see if the row already exists before inserting any new rows
                String checkForDelete = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                PreparedStatement psCheckDelete = conn.prepareStatement(checkForDelete);
                psCheckDelete.setInt(1, amiiboID);                               // AmiiboID
                psCheckDelete.setInt(2, userID);                             // UserID
                ResultSet rsDelete = psCheckDelete.executeQuery();              // Execute


                // If the row does not exist, insert a new row for this user
                if (!rsDelete.isBeforeFirst()) {
                    System.out.println("Data does not exist... Nothing to do.");
                } else {
                    while (rsDelete.next()) {
                        int collectionID = rsDelete.getInt("CollectionID");
                        System.out.println("The row exists as CollectionID: " + collectionID + ". Updating database...");

                        // update the data
                        PreparedStatement psDelete = conn.prepareStatement(
                                "UPDATE Collection SET Collected = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                        // set the preparedstatement parameters
                        psDelete.setString(1, "N");              // Collected
                        psDelete.setInt(2, userID);                 // ModUser
                        psDelete.setString(3, moddate.toString());  // ModDate
                        psDelete.setInt(4, collectionID);           // CollectionID
                        psDelete.executeUpdate();                      // Execute
                    }
                }
                conn.close();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}