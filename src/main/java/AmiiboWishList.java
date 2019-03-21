import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class AmiiboWishList {

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
    public void wishlistAmiibo(String[] args) {

        ConnHelper connHelper = new ConnHelper();
        Date addDate = new Date();
        Date modDate = new Date();
        int amiiboID = this.amiiboID;
        int userID = this.userID;

        System.out.println("UserID is: " +userID);

        try {

            // Check to see if the row already exists before inserting any new rows
            String checkExistsWishList = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
            PreparedStatement psAddWishList = connHelper.db().prepareStatement(checkExistsWishList);
            psAddWishList.setInt(1, amiiboID);                                 // AmiiboID
            psAddWishList.setInt(2, userID);                               // UserID
            ResultSet rsAddWishList = psAddWishList.executeQuery();               // Execute

            System.out.println("AddWishList class has an ID of: " + this.amiiboID);

            // If the row does not exist, insert a new row for this user
            if (!rsAddWishList.isBeforeFirst()) {
                System.out.println("Data does not exist... Inserting into database...");

                // sql insert statement
                String faveAmiibo = "insert into Collection (AmiiboID, UserID, WishList, ModUser, ModDate, AddUser, AddDate)"
                        + "values (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement psWishInsert = connHelper.db().prepareStatement(faveAmiibo);

                // sql insert values
                psWishInsert.setInt(1, amiiboID);                  // AmiiboID
                psWishInsert.setInt(2, userID);                 // UserID
                psWishInsert.setString(3, "Y");             // WishList Y/N
                psWishInsert.setInt(4, userID);                 // ModUser (ID)
                psWishInsert.setString(5, modDate.toString()); // ModDate
                psWishInsert.setInt(6, userID);                 // AddUser (ID)
                psWishInsert.setString(7, addDate.toString()); // AddDate
                psWishInsert.execute();                           // Execute

                // If the row does exist, simply update it
            } else {
                while (rsAddWishList.next()) {
                    int collectionID = rsAddWishList.getInt("CollectionID");
                    System.out.println("This row already exists as CollectionID: " + collectionID + ". Updating database...");

                    // update the data
                    PreparedStatement psWishUpdate = connHelper.db().prepareStatement(
                            "UPDATE Collection SET WishList = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                    // set the preparedstatement parameters
                    psWishUpdate.setString(1, "Y");             // WishList
                    psWishUpdate.setInt(2, userID);                 // ModUser
                    psWishUpdate.setString(3, modDate.toString()); // ModDate
                    psWishUpdate.setInt(4, collectionID);           // CollectionID
                    psWishUpdate.executeUpdate();                    // Execute

                    System.out.println("AddWishList class has an ID of: " + this.amiiboID);
                }
            }
            connHelper.db().close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public void unwishstAmiibo(String[] args) {

        ConnHelper connHelper = new ConnHelper();
        Date modDate = new Date();
        int amiiboID = this.amiiboID;
        int userID = this.userID;

        System.out.println("UserID is: " +userID);

        try {
            // Check to see if the row already exists before inserting any new rows
            String checkExistsWishRemove = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
            PreparedStatement psWishRemove = connHelper.db().prepareStatement(checkExistsWishRemove);
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
                    PreparedStatement psFaveUpdateRemove = connHelper.db().prepareStatement(
                            "UPDATE Collection SET WishList = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                    // set the preparedstatement parameters
                    psFaveUpdateRemove.setString(1, "N");             // WishList
                    psFaveUpdateRemove.setInt(2, userID);                 // ModUser
                    psFaveUpdateRemove.setString(3, modDate.toString()); // ModDate
                    psFaveUpdateRemove.setInt(4, collectionID);           // CollectionID
                    psFaveUpdateRemove.executeUpdate();                    // Execute

                    System.out.println("RemoveWishList class has an ID of: " + this.amiiboID);
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