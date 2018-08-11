import java.sql.*;
import java.util.Scanner;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        // For the users
        Scanner input = new Scanner(System.in);
        int userID = 1;

        // Instance Variables
        String dbURL = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        // Date conversion for MySQL
        Date adddate = new Date();
        Date moddate = new Date();

        do {
            try {

                // ==================================================
                // Main Menu
                // ==================================================

                System.out.println("Type the Menu Number of the Amiibo you wish to modify.\n");

                Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
                if (conn != null) {
                }
                String selectMain = "SELECT AmiiboID, Name FROM Amiibo";
                PreparedStatement psMain = conn.prepareStatement(selectMain);
                ResultSet rsMain = psMain.executeQuery(selectMain);

                while (rsMain.next()) {
                    String AmiiboID = rsMain.getString("AmiiboID");
                    String Name = rsMain.getString("Name");
                    System.out.println(AmiiboID + " - " + Name);
                }
                int menu = input.nextInt();

                if (menu == menu) {
                    System.out.println("What do you want to do?\n1 - Add Amiibo\n2 - Remove Amiibo\n3 - Add to Favorite\n4 - Remove from Favorites\n5 - Add to Wish List\n6 - Remove from Wish List\n");
                    int subMenu = input.nextInt(); // Digging down on options

                    // ==================================================
                    // Add Logic
                    // ==================================================

                    if (subMenu == 1) {

                        // Check to see if the row already exists before inserting any new rows
                        String checkExists = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                        PreparedStatement psAdd = conn.prepareStatement(checkExists);
                        psAdd.setInt(1, menu);                                 // AmiiboID
                        psAdd.setInt(2, userID);                           // UserID
                        ResultSet rsAdd = psAdd.executeQuery();                  // Execute

                        // If the row does not exist, insert a new row for this user
                        if (!rsAdd.isBeforeFirst()) {
                            System.out.println("Data does not exist... Inserting into database...");

                            // sql insert statement
                            String addAmiibo = "insert into Collection (AmiiboID, UserID, Collected, ModUser, ModDate, AddUser, AddDate)"
                                    + "values (?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement psAddInsert = conn.prepareStatement(addAmiibo);

                            // sql insert values
                            psAddInsert.setInt(1, menu);                  // AmiiboID
                            psAddInsert.setInt(2, userID);             // UserID
                            psAddInsert.setString(3, "Y");             // Collected Y/N
                            psAddInsert.setString(4, "1");             // ModUser (ID)
                            psAddInsert.setString(5, moddate.toString()); // ModDate
                            psAddInsert.setString(6, "1");             // AddUser (ID)
                            psAddInsert.setString(7, adddate.toString()); // AddDate
                            psAddInsert.execute();                           // Execute

                            // If the row does exist, simply update it
                        } else {
                            while (rsAdd.next()) {
                                String CollectionID = rsAdd.getString("CollectionID");
                                System.out.println("This row already exists as CollectionID: " + CollectionID + ". Updating database...");

                                // update the data
                                PreparedStatement psAddUpdate = conn.prepareStatement(
                                        "UPDATE Collection SET Collected = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                                // set the preparedstatement parameters
                                psAddUpdate.setString(1, "Y");             // Collected
                                psAddUpdate.setString(2, "1");             // ModUser
                                psAddUpdate.setString(3, moddate.toString()); // ModDate
                                psAddUpdate.setString(4, "1");            // CollectionID
                                psAddUpdate.executeUpdate();                    // Execute
                                System.out.println("Updated row...");
                            }
                        }

                        // ==================================================
                        // Remove Logic
                        // ==================================================

                    } else if (subMenu == 2) {

                        // Check to see if the row already exists before inserting any new rows
                        String checkForDelete = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                        PreparedStatement psCheckDelete = conn.prepareStatement(checkForDelete);
                        psCheckDelete.setInt(1, menu);                               // AmiiboID
                        psCheckDelete.setInt(2, userID);                             // UserID
                        ResultSet rsDelete = psCheckDelete.executeQuery();              // Execute

                        while (rsDelete.next()) {
                            int collectionID = rsDelete.getInt("CollectionID");

                            // sql delete statement
                            String delete = "delete from Collection where CollectionID = ?";
                            PreparedStatement psDelete = conn.prepareStatement(delete);
                            psDelete.setInt(1, collectionID);
                            psDelete.executeUpdate();

                            System.out.println("Deleted CollectionID: " + collectionID);
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
        while (true);
    }
}


