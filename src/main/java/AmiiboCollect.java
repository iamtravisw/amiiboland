import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class AmiiboCollect {

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

    public void addAmiibo(String[] args) {
        ConnHelper connHelper = new ConnHelper(); // Make a database connection

        Date addDate = new Date();
        Date modDate = new Date();
        int amiiboID = this.amiiboID;
        int userID = this.userID;

        System.out.println("UserID is: " + userID);

        try {
            // Check to see if the row already exists before inserting any new rows
            String checkExists = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
            PreparedStatement psAdd = connHelper.db().prepareStatement(checkExists);
            psAdd.setInt(1, this.amiiboID);                              // AmiiboID
            psAdd.setInt(2, userID);                                     // UserID
            ResultSet rsAdd = psAdd.executeQuery();                        // Execute

            // If the row does not exist, insert a new row for this user
            if (!rsAdd.isBeforeFirst()) {
                System.out.println("Data does not exist... Inserting into database...");

                // sql insert statement
                String collectAmiibo = "insert into Collection (AmiiboID, UserID, Collected, ModUser, ModDate, AddUser, AddDate)"
                        + "values (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement psAddInsert = connHelper.db().prepareStatement(collectAmiibo);

                // sql insert values
                psAddInsert.setInt(1, amiiboID);              // AmiiboID
                psAddInsert.setInt(2, userID);                // UserID
                psAddInsert.setString(3, "Y");             // Collected Y/N
                psAddInsert.setInt(4, userID);                // ModUser (ID)
                psAddInsert.setString(5, modDate.toString()); // ModDate
                psAddInsert.setInt(6, userID);                // AddUser (ID)
                psAddInsert.setString(7, addDate.toString()); // AddDate
                psAddInsert.execute();                           // Execute
                System.out.println("AddAmiibo class has an ID of: " + this.amiiboID);

                // If the row does exist, simply update it
            } else {
                while (rsAdd.next()) {
                    int collectionID = rsAdd.getInt("CollectionID");
                    System.out.println("This row already exists as CollectionID: " + collectionID + ". Updating database...");

                    // update the data
                    PreparedStatement psAddUpdate = connHelper.db().prepareStatement(
                            "UPDATE Collection SET Collected = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                    // set the preparedstatement parameters
                    psAddUpdate.setString(1, "Y");             // Collected
                    psAddUpdate.setInt(2, userID);                // ModUser
                    psAddUpdate.setString(3, modDate.toString()); // ModDate
                    psAddUpdate.setInt(4, collectionID);          // CollectionID
                    psAddUpdate.executeUpdate();                     // Execute
                    System.out.println("Updated row...");
                    System.out.println("AddAmiibo class has an ID of: " + this.amiiboID);
                }
            }
            connHelper.db().close();
        } catch (
                Exception e) {
            System.out.println(e);
        }
    }
    public void removeAmiibo(String[] args) {

        ConnHelper connHelper = new ConnHelper();
        Date modDate = new Date();
        int amiiboID = this.amiiboID;
        int userID = this.userID;

        System.out.println("UserID is: " +userID);

        try {
            // Check to see if the row already exists before inserting any new rows
            String checkForDelete = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
            PreparedStatement psCheckDelete = connHelper.db().prepareStatement(checkForDelete);
            psCheckDelete.setInt(1, amiiboID);                               // AmiiboID
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
                    PreparedStatement psDelete = connHelper.db().prepareStatement(
                            "UPDATE Collection SET Collected = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                    // set the preparedstatement parameters
                    psDelete.setString(1, "N");              // Collected
                    psDelete.setInt(2, userID);                 // ModUser
                    psDelete.setString(3, modDate.toString());  // ModDate
                    psDelete.setInt(4, collectionID);           // CollectionID
                    psDelete.executeUpdate();                      // Execute

                    System.out.println("RemoveAmiibo class has an ID of: " + this.amiiboID);
                }
                connHelper.db().close();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
