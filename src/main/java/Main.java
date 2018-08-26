import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Map;
import static spark.Spark.*;
import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.get;
import spark.template.velocity.*;

public class Main {

    public static void main(String[] args) {

        String dbURL = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        // ==================================================
        // Spark Configuration
        // ==================================================

        staticFiles.location("/public");

        // Index
        get("/", (rq, rs) -> {
            // Get all amiibos
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            String selectMain = "SELECT AmiiboID, Name, ImageURL FROM Amiibo";
            PreparedStatement psMain = conn.prepareStatement(selectMain);
            ResultSet resultSet = psMain.executeQuery(selectMain);

            ArrayList<HashMap<String,String>> amiibos = new ArrayList<HashMap<String,String>>();

            // [
            //     { "Name": "Mario", "AmiiboID": "2"},
            //     { "Name": "Mario", "AmiiboID": "2"},
            // ]

            // If we want to display them (text)
            while (resultSet.next()) {
                String AmiiboID = resultSet.getString("AmiiboID");
                String Name = resultSet.getString("Name");
                String ImageURL = resultSet.getString("ImageURL");

                // Hashmap = key value pair
                HashMap<String, String> amiibo = new HashMap<String, String>();
                amiibo.put("Name", Name);
                amiibo.put("AmiiboID", AmiiboID);
                amiibo.put("ImageURL", ImageURL);

                amiibos.add(amiibo);
                // System.out.println(AmiiboID + " - " + Name);
            }

            Map<String, Object> model = new HashMap<>();
            model.put("amiibos", amiibos);

            System.out.println(amiibos);

            // Pass amiibos to template
            return render(model, "templates/index.vm");
        });

        // Favicon
        get("/favicon", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "templates/index.vm");
        });

        // Collection (Add/Remove)
        post("/collection", (request, response) -> {
            AddAmiibo addAmiibo = new AddAmiibo();
            RemoveAmiibo removeAmiibo = new RemoveAmiibo();
            String mine, love, want;
            mine = request.queryParams("mine");

            if (mine.equals("addAmiibo")) {
                System.out.println("------------------------------------------");
                System.out.println("Add Amiibo Started");
                System.out.println("------------------------------------------");
                System.out.println("Value is " + mine + ". Adding to collection.");
                addAmiibo.setAmiiboID(22);
                System.out.println("Main class has an ID of: " + addAmiibo.getAmiiboID());
                addAmiibo.main(args);

            } else if (mine.equals("removeAmiibo")) {
                System.out.println("------------------------------------------");
                System.out.println("Remove Amiibo Started");
                System.out.println("------------------------------------------");
                System.out.println("Value is " + mine + ". Removing from collection.");
                removeAmiibo.setAmiiboID(22);
                System.out.println("Main class has an ID of: " + removeAmiibo.getAmiiboID());
                removeAmiibo.main(args);
            }
            return String.join(mine);
        });

        // Favorites (Add/Remove)
        post("/favorites", (request, response) -> {
            AddFavorite addFavorite = new AddFavorite();
            RemoveFavorite removeFavorite = new RemoveFavorite();
            String love;
            love = request.queryParams("love");

            if (love.equals("loveAmiibo")) {
                System.out.println("------------------------------------------");
                System.out.println("Favorite Amiibo Started");
                System.out.println("------------------------------------------");
                System.out.println("Value is " + love + ". Adding to Favorites.");
                addFavorite.setAmiiboID(22);
                System.out.println("Main class has an ID of: " + addFavorite.getAmiiboID());
                addFavorite.main(args);

            } else if (love.equals("unloveAmiibo")) {
                System.out.println("------------------------------------------");
                System.out.println("Unfavorite Amiibo Started");
                System.out.println("------------------------------------------");
                System.out.println("Value is " + love + ". Removing from Favorites.");
                removeFavorite.setAmiiboID(22);
                System.out.println("Main class has an ID of: " + removeFavorite.getAmiiboID());
                removeFavorite.main(args);
            }
            return String.join(love);
        });

        // WishList (Add/Remove)
        post("/wishlist", (request, response) -> {
            AddWishList addWishList = new AddWishList();
            RemoveWishList removeWishList = new RemoveWishList();
            String want;
            want = request.queryParams("want");

            if (want.equals("wantAmiibo")) {
                System.out.println("------------------------------------------");
                System.out.println("WishList Amiibo Started");
                System.out.println("------------------------------------------");
                System.out.println("Value is " + want + ". Adding to WishList.");
                addWishList.setAmiiboID(22);
                System.out.println("Main class has an ID of: " + addWishList.getAmiiboID());
                addWishList.main(args);

            } else if (want.equals("unwantAmiibo")) {
                System.out.println("------------------------------------------");
                System.out.println("UnWishList Amiibo Started");
                System.out.println("------------------------------------------");
                System.out.println("Value is " + want + ". Removing from WishList.");
                removeWishList.setAmiiboID(22);
                System.out.println("Main class has an ID of: " + removeWishList.getAmiiboID());
                removeWishList.main(args);
            }
            return String.join(want);
        });






    }
    public static String render(Map<String, Object> model, String templatePath) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}

