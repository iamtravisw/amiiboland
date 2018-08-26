import java.sql.*;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;

public class RemoveWishList {

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
            if (conn != null)
            {

                // Check to see if the row already exists before inserting any new rows
                String checkExistsWishRemove = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                PreparedStatement psWishRemove = conn.prepareStatement(checkExistsWishRemove);
                psWishRemove.setInt(1, amiiboID);                                 // AmiiboID
                psWishRemove.setInt(2, userID);                               // UserID
                ResultSet rsWishRemove = psWishRemove.executeQuery();         // Execute

                System.out.println("RemoveWishList class has an ID of: " + this.amiiboID);

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
                        psFaveUpdateRemove.setString(3, modDate.toString()); // ModDate
                        psFaveUpdateRemove.setInt(4, collectionID);           // CollectionID
                        psFaveUpdateRemove.executeUpdate();                    // Execute

                        System.out.println("RemoveWishList class has an ID of: " + this.amiiboID);
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

