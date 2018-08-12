import java.sql.*;
import java.util.Map;
import java.util.Scanner;
import java.util.Date;
import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import static spark.Spark.get;
import spark.template.velocity.*;


public class Main {

    public static void main(String[] args) {

        // ==================================================
        // Global Variables
        // ==================================================

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

        // ==================================================
        // Spark Configuration
        // ==================================================

        staticFiles.location("/public");

        // this is my home page
        get("/", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "templates/index.vm");
        });

         // favicon
         get("/favicon", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "templates/index.vm");
        });



// get name and email from user input
        post("/input", (request, response) -> {
            String switchLarge;
            switchLarge = request.queryParams("switchLarge");

            if (switchLarge != null) {

        // ==================================================
        // Main Menu
        // ==================================================

      //  do {
            try {

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
                        psAdd.setInt(1, menu);                              // AmiiboID
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
                            psAddInsert.setInt(1, menu);                  // AmiiboID
                            psAddInsert.setInt(2, userID);                // UserID
                            psAddInsert.setString(3, "Y");             // Collected Y/N
                            psAddInsert.setInt(4, userID);          // ModUser (ID)
                            psAddInsert.setString(5, moddate.toString()); // ModDate
                            psAddInsert.setInt(6, userID);          // AddUser (ID)
                            psAddInsert.setString(7, adddate.toString()); // AddDate
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
                                psAddUpdate.setString(3, moddate.toString()); // ModDate
                                psAddUpdate.setInt(4, collectionID);          // CollectionID
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
                                psDelete.setString(3, moddate.toString());  // ModDate
                                psDelete.setInt(4, collectionID);           // CollectionID
                                psDelete.executeUpdate();                      // Execute
                            }
                        }

                        // ==================================================
                        // Add Favorite Logic
                        // ==================================================

                    } else if (subMenu == 3) {

                        // Check to see if the row already exists before inserting any new rows
                        String checkExistsFave = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                        PreparedStatement psAddFave = conn.prepareStatement(checkExistsFave);
                        psAddFave.setInt(1, menu);                                 // AmiiboID
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
                            psFaveInsert.setInt(1, menu);                  // AmiiboID
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

                        // ==================================================
                        // Remove Favorite Logic
                        // ==================================================

                    } else if (subMenu == 4) {

                        // Check to see if the row already exists before inserting any new rows
                        String checkExistsFaveRemove = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                        PreparedStatement psAddFaveRemove = conn.prepareStatement(checkExistsFaveRemove);
                        psAddFaveRemove.setInt(1, menu);                                 // AmiiboID
                        psAddFaveRemove.setInt(2, userID);                               // UserID
                        ResultSet rsFaveRemove = psAddFaveRemove.executeQuery();               // Execute

                        // If the row does not exist, insert a new row for this user
                        if (!rsFaveRemove.isBeforeFirst()) {
                            System.out.println("Data does not exist... No need to add it.");

                            /*
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

                        // ==================================================
                        // Add Wish List Logic
                        // ==================================================

                    } else if (subMenu == 5) {

                        // Check to see if the row already exists before inserting any new rows
                        String checkExistsWishList = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                        PreparedStatement psAddWishList = conn.prepareStatement(checkExistsWishList);
                        psAddWishList.setInt(1, menu);                                 // AmiiboID
                        psAddWishList.setInt(2, userID);                               // UserID
                        ResultSet rsAddWishList = psAddWishList.executeQuery();               // Execute

                        // If the row does not exist, insert a new row for this user
                        if (!rsAddWishList.isBeforeFirst()) {
                            System.out.println("Data does not exist... Inserting into database...");

                            // sql insert statement
                            String faveAmiibo = "insert into Collection (AmiiboID, UserID, WishList, ModUser, ModDate, AddUser, AddDate)"
                                    + "values (?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement psWishInsert = conn.prepareStatement(faveAmiibo);

                            // sql insert values
                            psWishInsert.setInt(1, menu);                  // AmiiboID
                            psWishInsert.setInt(2, userID);                 // UserID
                            psWishInsert.setString(3, "Y");             // WishList Y/N
                            psWishInsert.setInt(4, userID);                 // ModUser (ID)
                            psWishInsert.setString(5, moddate.toString()); // ModDate
                            psWishInsert.setInt(6, userID);                 // AddUser (ID)
                            psWishInsert.setString(7, adddate.toString()); // AddDate
                            psWishInsert.execute();                           // Execute

                            // If the row does exist, simply update it
                        } else {
                            while (rsAddWishList.next()) {
                                int collectionID = rsAddWishList.getInt("CollectionID");
                                System.out.println("This row already exists as CollectionID: " + collectionID + ". Updating database...");

                                // update the data
                                PreparedStatement psWishUpdate = conn.prepareStatement(
                                        "UPDATE Collection SET WishList = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                                // set the preparedstatement parameters
                                psWishUpdate.setString(1, "Y");             // WishList
                                psWishUpdate.setInt(2, userID);                 // ModUser
                                psWishUpdate.setString(3, moddate.toString()); // ModDate
                                psWishUpdate.setInt(4, collectionID);           // CollectionID
                                psWishUpdate.executeUpdate();                    // Execute

                            }
                        }

                        // ==================================================
                        // Remove Wish List Logic
                        // ==================================================

                    } else if (subMenu == 6) {

                        // Check to see if the row already exists before inserting any new rows
                        String checkExistsWishRemove = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                        PreparedStatement psWishRemove = conn.prepareStatement(checkExistsWishRemove);
                        psWishRemove.setInt(1, menu);                                 // AmiiboID
                        psWishRemove.setInt(2, userID);                               // UserID
                        ResultSet rsWishRemove = psWishRemove.executeQuery();         // Execute

                        // If the row does not exist, insert a new row for this user
                        if (!rsWishRemove.isBeforeFirst()) {
                            System.out.println("Data does not exist... No need to add it.");

                            /*
                            System.out.println("Data does not exist... Inserting into database...");

                            // sql insert statement
                            String wishAmiiboRemove = "insert into Collection (AmiiboID, UserID, Favorited, ModUser, ModDate, AddUser, AddDate)"
                                    + "values (?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement psWishInsertRemove = conn.prepareStatement(wishAmiiboRemove);

                            // sql insert values
                            psWishInsertRemove.setInt(1, menu);                  // AmiiboID
                            psWishInsertRemove.setInt(2, userID);                 // UserID
                            psWishInsertRemove.setString(3, "N");             // Favorited Y/N
                            psWishInsertRemove.setInt(4, userID);                 // ModUser (ID)
                            psWishInsertRemove.setString(5, moddate.toString()); // ModDate
                            psWishInsertRemove.setInt(6, userID);                 // AddUser (ID)
                            psWishInsertRemove.setString(7, adddate.toString()); // AddDate
                            psWishInsertRemove.execute();                           // Execute
                            */

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
                                psFaveUpdateRemove.setString(3, moddate.toString()); // ModDate
                                psFaveUpdateRemove.setInt(4, collectionID);           // CollectionID
                                psFaveUpdateRemove.executeUpdate();                    // Execute

                            }
                        }
                    } else {
                        System.out.println("You must select option 1-6.");
                    }
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
      //  while (true);
            return String.join(" / ", switchLarge);
        });
    }

    public static String render(Map<String, Object> model, String templatePath) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}


