import java.sql.*;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;

public class AddFavorite {

    // Variables
    public int userID = 1;
    public int amiiboID;

    // Setters and Getters
    public void setAmiiboID(int amiiboID) {
        this.amiiboID = amiiboID;
    }
    public int getAmiiboID() {
        return amiiboID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getUserID() {
        return userID;
    }

    public void main(String[] args) {

        Date addDate = new Date();
        Date modDate = new Date();
        String dbURL = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");
        int amiiboID = this.amiiboID;
        int userID = this.userID;

        System.out.println("UserID is: " +userID);

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
                    psFaveInsert.setString(5, modDate.toString()); // ModDate
                    psFaveInsert.setInt(6, userID);                 // AddUser (ID)
                    psFaveInsert.setString(7, addDate.toString()); // AddDate
                    psFaveInsert.execute();                           // Execute

                    System.out.println("AddFavorite class has an ID of: " + this.amiiboID);

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
                        psFaveUpdate.setString(3, modDate.toString()); // ModDate
                        psFaveUpdate.setInt(4, collectionID);           // CollectionID
                        psFaveUpdate.executeUpdate();                    // Execute

                        System.out.println("AddFavorite class has an ID of: " + this.amiiboID);

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

