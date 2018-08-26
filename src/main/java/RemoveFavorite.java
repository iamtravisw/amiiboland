import java.sql.*;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;

public class RemoveFavorite {

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

            }
            // Check to see if the row already exists before inserting any new rows
            String checkExistsFaveRemove = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
            PreparedStatement psAddFaveRemove = conn.prepareStatement(checkExistsFaveRemove);
            psAddFaveRemove.setInt(1, amiiboID);                                 // AmiiboID
            psAddFaveRemove.setInt(2, userID);                               // UserID
            ResultSet rsFaveRemove = psAddFaveRemove.executeQuery();               // Execute

            // If the row does not exist, insert a new row for this user
            if (!rsFaveRemove.isBeforeFirst()) {
                System.out.println("Data does not exist... No need to add it.");

                            /* // This logic was removed as after writing it I realized that if a row does not exist, no point in adding it to NOT have it favorited. We'll Treat NULL as No on Website...
                            System.out.println("Data does not exist... Inserting into database...");

                            // sql insert statement
                            String faveAmiiboRemove = "insert into Collection (AmiiboID, UserID, Favorited, ModUser, ModDate, AddUser, AddDate)"
                                    + "values (?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement psFaveInsertRemove = conn.prepareStatement(faveAmiiboRemove);

                            // sql insert values
                            psFaveInsertRemove.setInt(1, menu);                  // AmiiboID
                            psFaveInsertRemove.setInt(2, userID);                 // UserID
                            psFaveInsertRemove.setString(3, "N");             // Favorited Y/N
                            psFaveInsertRemove.setInt(4, userID);                 // ModUser (ID)
                            psFaveInsertRemove.setString(5, moddate.toString()); // ModDate
                            psFaveInsertRemove.setInt(6, userID);                 // AddUser (ID)
                            psFaveInsertRemove.setString(7, adddate.toString()); // AddDate
                            psFaveInsertRemove.execute();                           // Execute
                            */

                // If the row does exist, simply update it
            } else {
                while (rsFaveRemove.next()) {
                    int collectionID = rsFaveRemove.getInt("CollectionID");
                    System.out.println("This row already exists as CollectionID: " + collectionID + ". Updating database...");

                    // update the data
                    PreparedStatement psFaveUpdateRemove = conn.prepareStatement(
                            "UPDATE Collection SET Favorited = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                    // set the preparedstatement parameters
                    psFaveUpdateRemove.setString(1, "N");             // Favorited
                    psFaveUpdateRemove.setInt(2, userID);                 // ModUser
                    psFaveUpdateRemove.setString(3, moddate.toString()); // ModDate
                    psFaveUpdateRemove.setInt(4, collectionID);           // CollectionID
                    psFaveUpdateRemove.executeUpdate();                    // Execute
                }
            }
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }
}

