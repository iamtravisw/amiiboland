import java.sql.*;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;

public class AddAmiibo {

    // Variables
    int userID = 1;
    int amiiboID = 1;
    Date addDate = new Date();
    Date modDate = new Date();
    String dbURL = System.getenv("DB_URL");
    String dbUser = System.getenv("DB_USER");
    String dbPassword = System.getenv("DB_PASSWORD");

    // Setters and Getters
    public void setAmiiboID(int AmiiboID) {
        this.amiiboID = AmiiboID;
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
    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }
    public Date getAddDate() {
        return addDate;
    }
    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }
    public Date getModDate() {
        return modDate;
    }
    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }
    public String getDbURL() {
        return dbURL;
    }
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }
    public String getDbUser() {
        return dbUser;
    }
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
    public String getDbPassword() {
        return dbPassword;
    }

    public static void main(String[] args) {

        AddAmiibo add = new AddAmiibo();
        int amiiboID = add.getAmiiboID();
        int userID = add.getUserID();
        Date addDate = add.getAddDate();
        Date modDate = add.getModDate();
        String dbURL = add.getDbURL();
        String dbUser = add.getDbUser();
        String dbPassword = add.getDbPassword();

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null)
            {
                // Check to see if the row already exists before inserting any new rows
                String checkExists = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                PreparedStatement psAdd = conn.prepareStatement(checkExists);
                psAdd.setInt(1, amiiboID);                              // AmiiboID
                psAdd.setInt(2, userID);                           // UserID
                ResultSet rsAdd = psAdd.executeQuery();               // Execute

                // If the row does not exist, insert a new row for this user
                if (!rsAdd.isBeforeFirst()) {
                    System.out.println("Data does not exist... Inserting into database...");

                    // sql insert statement
                    String addAmiibo = "insert into Collection (AmiiboID, UserID, Collected, ModUser, ModDate, AddUser, AddDate)"
                            + "values (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement psAddInsert = conn.prepareStatement(addAmiibo);

                    // sql insert values
                    psAddInsert.setInt(1, amiiboID);               // AmiiboID
                    psAddInsert.setInt(2, userID);                // UserID
                    psAddInsert.setString(3, "Y");             // Collected Y/N
                    psAddInsert.setInt(4, userID);                // ModUser (ID)
                    psAddInsert.setString(5, modDate.toString()); // ModDate
                    psAddInsert.setInt(6, userID);                // AddUser (ID)
                    psAddInsert.setString(7, addDate.toString()); // AddDate
                    psAddInsert.execute();                           // Execute

                    // If the row does exist, simply update it
                } else {
                    while (rsAdd.next()) {
                        int collectionID = rsAdd.getInt("CollectionID");
                        System.out.println("This row already exists as CollectionID: " + collectionID + ". Updating database...");

                        // update the data
                        PreparedStatement psAddUpdate = conn.prepareStatement(
                                "UPDATE Collection SET Collected = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                        // set the preparedstatement parameters
                        psAddUpdate.setString(1, "Y");             // Collected
                        psAddUpdate.setInt(2, userID);                // ModUser
                        psAddUpdate.setString(3, modDate.toString()); // ModDate
                        psAddUpdate.setInt(4, collectionID);          // CollectionID
                        psAddUpdate.executeUpdate();                    // Execute
                        System.out.println("Updated row...");
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

