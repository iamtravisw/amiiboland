import java.sql.*;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;

public class RemoveAmiibo {

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

        Date modDate = new Date();
        String dbURL = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");
        int amiiboID = this.amiiboID;
        int userID = this.userID;

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {


                // Check to see if the row already exists before inserting any new rows
                String checkForDelete = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                PreparedStatement psCheckDelete = conn.prepareStatement(checkForDelete);
                psCheckDelete.setInt(1, this.amiiboID);                               // AmiiboID
                psCheckDelete.setInt(2, userID);                             // UserID
                ResultSet rsDelete = psCheckDelete.executeQuery();              // Execute

                System.out.println("RemoveAmiibo class has an ID of: " + this.amiiboID);


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
                        psDelete.setString(3, modDate.toString());  // ModDate
                        psDelete.setInt(4, collectionID);           // CollectionID
                        psDelete.executeUpdate();                      // Execute

                        System.out.println("RemoveAmiibo class has an ID of: " + this.amiiboID);
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