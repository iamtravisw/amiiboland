import java.sql.*;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;

public class AddFavorite {

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
                String checkExistsFave = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                PreparedStatement psAddFave = conn.prepareStatement(checkExistsFave);
                psAddFave.setInt(1, amiiboID);                                 // AmiiboID
                psAddFave.setInt(2, userID);                               // UserID
                ResultSet rsAddFave = psAddFave.executeQuery();               // Execute

                // If the row does not exist, insert a new row for this user
                if (!rsAddFave.isBeforeFirst()) {
                    System.out.println("Data does not exist... Inserting into database...");

                    // sql insert statement
                    String faveAmiibo = "insert into Collection (AmiiboID, UserID, Favorited, ModUser, ModDate, AddUser, AddDate)"
                            + "values (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement psFaveInsert = conn.prepareStatement(faveAmiibo);

                    // sql insert values
                    psFaveInsert.setInt(1, amiiboID);                  // AmiiboID
                    psFaveInsert.setInt(2, userID);                 // UserID
                    psFaveInsert.setString(3, "Y");             // Favorited Y/N
                    psFaveInsert.setInt(4, userID);                 // ModUser (ID)
                    psFaveInsert.setString(5, moddate.toString()); // ModDate
                    psFaveInsert.setInt(6, userID);                 // AddUser (ID)
                    psFaveInsert.setString(7, adddate.toString()); // AddDate
                    psFaveInsert.execute();                           // Execute

                    // If the row does exist, simply update it
                } else {
                    while (rsAddFave.next()) {
                        int collectionID = rsAddFave.getInt("CollectionID");
                        System.out.println("This row already exists as CollectionID: " + collectionID + ". Updating database...");

                        // update the data
                        PreparedStatement psFaveUpdate = conn.prepareStatement(
                                "UPDATE Collection SET Favorited = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                        // set the preparedstatement parameters
                        psFaveUpdate.setString(1, "Y");             // Favorited
                        psFaveUpdate.setInt(2, userID);                 // ModUser
                        psFaveUpdate.setString(3, moddate.toString()); // ModDate
                        psFaveUpdate.setInt(4, collectionID);           // CollectionID
                        psFaveUpdate.executeUpdate();                    // Execute

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

