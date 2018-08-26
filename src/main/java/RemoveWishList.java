import java.sql.*;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;

public class RemoveWishList {

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
            if (conn != null)
            {

                // Check to see if the row already exists before inserting any new rows
                String checkExistsWishRemove = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                PreparedStatement psWishRemove = conn.prepareStatement(checkExistsWishRemove);
                psWishRemove.setInt(1, amiiboID);                                 // AmiiboID
                psWishRemove.setInt(2, userID);                               // UserID
                ResultSet rsWishRemove = psWishRemove.executeQuery();         // Execute

                // If the row does not exist, do nothing
                if (!rsWishRemove.isBeforeFirst()) {
                    System.out.println("Data does not exist... No need to add it.");


                    // If the row does exist, simply update it
                } else {
                    while (rsWishRemove.next()) {
                        int collectionID = rsWishRemove.getInt("CollectionID");
                        System.out.println("This row already exists as CollectionID: " + collectionID + ". Updating database...");

                        // update the data
                        PreparedStatement psFaveUpdateRemove = conn.prepareStatement(
                                "UPDATE Collection SET WishList = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                        // set the preparedstatement parameters
                        psFaveUpdateRemove.setString(1, "N");             // WishList
                        psFaveUpdateRemove.setInt(2, userID);                 // ModUser
                        psFaveUpdateRemove.setString(3, moddate.toString()); // ModDate
                        psFaveUpdateRemove.setInt(4, collectionID);           // CollectionID
                        psFaveUpdateRemove.executeUpdate();                    // Execute
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

