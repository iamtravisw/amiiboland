import java.sql.*;
import java.util.Scanner;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // Instance Variables
        String dbURL = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        Date adddate = new Date();
        Date moddate = new Date();

        /*
        Amiibo amiibo = new Amiibo();
        amiibo.setMario("");
        amiibo.setDonkeykong("");
        amiibo.setFox("");

        String mario = amiibo.getMario();
        String donkeykong = amiibo.getDonkeykong();
        String fox = amiibo.getFox();
        System.out.println(mario+", "+donkeykong+", "+fox);
        */


    //    do {
            try {

                System.out.println("Type the Menu Number of the Amiibo you wish to modify.\n");

                Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
                if (conn != null) {
                }
                String selectSQL = "SELECT AmiiboID, Name FROM Amiibo";
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
                ResultSet rs = preparedStatement.executeQuery(selectSQL);

                while (rs.next()) {
                    String AmiiboID = rs.getString("AmiiboID");
                    String Name = rs.getString("Name");
                    System.out.println(AmiiboID + " - " + Name);
                }
                int menu = input.nextInt();


                if (menu == 1) {
                    System.out.println("What do you want to do?\n1 - Add Amiibo\n2 - Remove Amiibo\n3 - Add to Favorite\n4 - Remove from Favorites\n5 - Add to Wish List\n6 - Remove from Wish List\n");
                    int collectMenu = input.nextInt();

                    if (collectMenu == 1) {

                        String checkExists = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                        PreparedStatement preparedStatement2 = conn.prepareStatement(checkExists);
                        preparedStatement2.setString(1, "1");             // AmiiboID
                        preparedStatement2.setString(2, "1");             // UserID
                        ResultSet rs2 = preparedStatement2.executeQuery();

                            if (!rs2.isBeforeFirst()) { // Does this row already exist? If not...
                                System.out.println("Data does not exist... Inserting into database...");

                                // sql insert statement
                                String addAmiibo = "insert into Collection (AmiiboID, UserID, Collected, ModUser, ModDate, AddUser, AddDate)"
                                        + "values (?, ?, ?, ?, ?, ?, ?)";
                                PreparedStatement insertUsers = conn.prepareStatement(addAmiibo);

                                // sql insert values
                                insertUsers.setString(1, "1");             // AmiiboID
                                insertUsers.setString(2, "1");             // UserID
                                insertUsers.setString(3, "Y");             // Collected Y/N
                                insertUsers.setString(4, "1");             // ModUser (ID)
                                insertUsers.setString(5, moddate.toString()); // ModDate
                                insertUsers.setString(6, "1");             // AddUser (ID)
                                insertUsers.setString(7, adddate.toString()); // AddDate
                                insertUsers.execute();                           // execute

                            } else { // However, if the row does exist... do this...
                                while (rs2.next()) {
                                    String CollectionID = rs2.getString("CollectionID");
                                    System.out.println("This row already exists as CollectionID: "+CollectionID+". Updating database...");

                                    // update the data
                                    PreparedStatement psSubjects = conn.prepareStatement(
                                            "UPDATE Collection SET Collected = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                                    // set the preparedstatement parameters
                                    psSubjects.setString(1, "Y");             // Collected
                                    psSubjects.setString(2, "1");             // ModUser
                                    psSubjects.setString(3, moddate.toString()); // ModDate
                                    psSubjects.setString(4, "1");            // CollectionID
                                    psSubjects.executeUpdate();
                                    System.out.println("Updated row...");
                            }
                        }
                    }

                }
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
       // while (true);
    }


