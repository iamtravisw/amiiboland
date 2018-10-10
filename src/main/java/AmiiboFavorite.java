import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class AmiiboFavorite {

    // Variables
    public int userID;
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

    public void favoriteAmiibo(String[] args) {

        ConnHelper connHelper = new ConnHelper();
        Date addDate = new Date();
        Date modDate = new Date();
        int amiiboID = this.amiiboID;
        int userID = this.userID;

        System.out.println("UserID is: " +userID);

        try {
            // Check to see if the row already exists before inserting any new rows
            String checkExistsFave = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
            PreparedStatement psAddFave = connHelper.db().prepareStatement(checkExistsFave);
            psAddFave.setInt(1, amiiboID);                                 // AmiiboID
            psAddFave.setInt(2, userID);                               // UserID
            ResultSet rsAddFave = psAddFave.executeQuery();               // Execute

            // If the row does not exist, insert a new row for this user
            if (!rsAddFave.isBeforeFirst()) {
                System.out.println("Data does not exist... Inserting into database...");

                // sql insert statement
                String faveAmiibo = "insert into Collection (AmiiboID, UserID, Favorited, ModUser, ModDate, AddUser, AddDate)"
                        + "values (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement psFaveInsert = connHelper.db().prepareStatement(faveAmiibo);

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
                    PreparedStatement psFaveUpdate = connHelper.db().prepareStatement(
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
            connHelper.db().close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public void unfavoriteAmiibo(String[] args) {

        ConnHelper connHelper = new ConnHelper();
        Date modDate = new Date();
        int amiiboID = this.amiiboID;
        int userID = this.userID;

        System.out.println("UserID is: " +userID);

        try {
            // Check to see if the row already exists before inserting any new rows
            String checkExistsFaveRemove = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
            PreparedStatement psAddFaveRemove = connHelper.db().prepareStatement(checkExistsFaveRemove);
            psAddFaveRemove.setInt(1, amiiboID);                                 // AmiiboID
            psAddFaveRemove.setInt(2, userID);                               // UserID
            ResultSet rsFaveRemove = psAddFaveRemove.executeQuery();               // Execute
            System.out.println("RemoveFavorite class has an ID of: " + this.amiiboID);
            // If the row does not exist, insert a new row for this user
            if (!rsFaveRemove.isBeforeFirst()) {
                System.out.println("Data does not exist... No need to add it.");
                // If the row does exist, simply update it
            } else {
                while (rsFaveRemove.next()) {
                    int collectionID = rsFaveRemove.getInt("CollectionID");
                    System.out.println("This row already exists as CollectionID: " + collectionID + ". Updating database...");

                    // update the data
                    PreparedStatement psFaveUpdateRemove = connHelper.db().prepareStatement(
                            "UPDATE Collection SET Favorited = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                    // set the preparedstatement parameters
                    psFaveUpdateRemove.setString(1, "N");             // Favorited
                    psFaveUpdateRemove.setInt(2, userID);                 // ModUser
                    psFaveUpdateRemove.setString(3, modDate.toString()); // ModDate
                    psFaveUpdateRemove.setInt(4, collectionID);           // CollectionID
                    psFaveUpdateRemove.executeUpdate();                    // Execute

                    System.out.println("RemoveFavorite class has an ID of: " + this.amiiboID);
                }
                connHelper.db().close();
            }
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }
}