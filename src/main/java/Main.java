import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Map;
import static spark.Spark.*;
import java.util.HashMap;
import spark.Filter;
import spark.ModelAndView;
import static spark.Spark.get;
import spark.Request;
import spark.Response;
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

        /* List all the routes that you want to have protected by authentication here:
         *
         * 1. Let them see the page but view-only mode, unable to take any action.
         * /index
         * /new
         * /comingsoon
         *
         *2. Redirect them to login, they should not even see this page.
         * /collection
         * /favorites
         * /wishlist
         * /profile/collection
         * /profile/favorites
         * /profile/wishlist
        */

        // Repeat this for all routes that add entries to the database
        before("/profile/*", (request, response) -> {
            if (!AuthHelper.isAuthenticated(request)) {
                //halt(401, "Please login");
                response.redirect("/pleaselogin");
            }
        });

        before("/collected", (request, response) -> {
            if (!AuthHelper.isAuthenticated(request)) {
                //halt(401, "Please login");
                response.redirect("/pleaselogin");
            }
        });
        before("/favorited", (request, response) -> {
            if (!AuthHelper.isAuthenticated(request)) {
                //halt(401, "Please login");
                response.redirect("/pleaselogin");
            }
        });

        before("/missing", (request, response) -> {
            if (!AuthHelper.isAuthenticated(request)) {
                //halt(401, "Please login");
                response.redirect("/pleaselogin");
            }
        });

        before("/collection", (request, response) -> {
            if (!AuthHelper.isAuthenticated(request)) {
                //halt(401, "Please login");
                response.redirect("/pleaselogin");
            }
        }); 

        before("/favorites", (request, response) -> {
            if (!AuthHelper.isAuthenticated(request)) {
                //halt(401, "Please login");
                response.redirect("/pleaselogin");
            }
        }); 
        before("/wishlist", (request, response) -> {
            if (!AuthHelper.isAuthenticated(request)) {
                //halt(401, "Please login");
                response.redirect("/pleaselogin");
            }
        });
        // Index 'All Amiibo'
        get("/", (rq, rs) -> {
            boolean loggedIn = rq.session().attribute("userID") != null; // Return UserID if value is not NULL
            int userID = loggedIn ? rq.session().attribute("userID") : -1; // 1=True and 0=False ... if loggedIn is NULL, assign value to prevent Server500 error.
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            // 'All' Tab from Home Page
            PreparedStatement psAll = conn.prepareStatement ("SELECT a.AmiiboID, a.Name, a.ImageURL, c.Favorited, c.Collected, c.WishList, c.UserID\n" +
                    "FROM Amiibo a LEFT JOIN Collection c ON a.AmiiboID = c.AmiiboID\n" +
                    "GROUP BY AmiiboID ASC");
                ResultSet resultsAll = psAll.executeQuery();
                ArrayList<HashMap<String, String>> amiibos = new ArrayList<HashMap<String, String>>();
                while (resultsAll.next()) {
                    String AmiiboID = resultsAll.getString("AmiiboID");
                    String Name = resultsAll.getString("Name");
                    String ImageURL = resultsAll.getString("ImageURL");
                    String Favorited = resultsAll.getString("Favorited");
                    String Collected = resultsAll.getString("Collected");
                    String WishList = resultsAll.getString("WishList");
                    String UserID = resultsAll.getString("UserID");
                    HashMap<String, String> amiibo = new HashMap<String, String>();
                    amiibo.put("Name", Name);
                    amiibo.put("AmiiboID", AmiiboID);
                    amiibo.put("ImageURL", ImageURL);
                    amiibo.put("Favorited", Favorited);
                    amiibo.put("Collected", Collected);
                    amiibo.put("WishList", WishList);
                    amiibo.put("UserID", UserID);
                    amiibos.add(amiibo);
                }
                Map<String, Object> model = new HashMap<>();
                model.put("amiibos", amiibos);
                // Add authenticated status to model
                model.put("authenticated", AuthHelper.isAuthenticated(rq));
                model.put("UserID", userID);
                System.out.println(amiibos);
                // Pass amiibos to template

                return render(model, "templates/index.vm");
            });

       /*
        // NavBar for Series
        get("/navbar", (rq, rs) -> {

            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }

            // 'Series' Menu on All Pages
            String selectSeries = "SELECT DISTINCT Series FROM Amiibo ORDER BY Series ASC";
            PreparedStatement psSeries = conn.prepareStatement(selectSeries);
            ResultSet resultsSeries = psSeries.executeQuery(selectSeries);

            ArrayList<HashMap<String,String>> allSeries = new ArrayList<HashMap<String,String>>();

            while (resultsSeries.next()) {
                String Series = resultsSeries.getString("Series");
                HashMap<String, String> series = new HashMap<String, String>();
                series.put("Series", Series);
                allSeries.add(series);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("allSeries", allSeries);
            System.out.println(allSeries);
            // Pass amiibos to template
            return render(model,"templates/navbar.vm");
        });
        */

        // 'New' Tab from Home Page
        get("/new", (rq, rs) -> {
            boolean loggedIn = rq.session().attribute("userID") != null; // Return UserID if value is not NULL
            int userID = loggedIn ? rq.session().attribute("userID") : -1; // 1=True and 0=False ... if loggedIn is NULL, assign value to prevent Server500 error.
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            PreparedStatement selectNew = conn.prepareStatement ("SELECT a.ReleaseDate, a.AmiiboID, a.Name, a.ImageURL, c.Favorited, c.Collected, c.WishList, c.UserID\n" +
                    "FROM Amiibo a LEFT JOIN Collection c ON a.AmiiboID = c.AmiiboID\n" +
                    "WHERE a.ReleaseDate BETWEEN DATE_SUB(now(), INTERVAL 12 MONTH) AND CURDATE() \n" +
                    "GROUP BY a.AmiiboID ORDER BY a.ReleaseDate DESC");
            ResultSet resultsNew = selectNew.executeQuery();
            ArrayList<HashMap<String, String>> newAmiibo = new ArrayList<HashMap<String, String>>();
            while (resultsNew.next()) {
                String ReleaseDate = resultsNew.getString("ReleaseDate");
                String AmiiboID = resultsNew.getString("AmiiboID");
                String Name = resultsNew.getString("Name");
                String ImageURL = resultsNew.getString("ImageURL");
                String Favorited = resultsNew.getString("Favorited");
                String Collected = resultsNew.getString("Collected");
                String WishList = resultsNew.getString("WishList");
                String UserID = resultsNew.getString("UserID");
                HashMap<String, String> recents = new HashMap<String, String>();
                recents.put("ReleaseDate", ReleaseDate);
                recents.put("Name", Name);
                recents.put("AmiiboID", AmiiboID);
                recents.put("ImageURL", ImageURL);
                recents.put("Favorited", Favorited);
                recents.put("Collected", Collected);
                recents.put("WishList", WishList);
                recents.put("UserID", UserID);
                newAmiibo.add(recents);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("newAmiibo", newAmiibo);
            // Add authenticated status to model
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            model.put("UserID", userID);
            System.out.println(newAmiibo);
            // Pass amiibos to template
            return render(model, "templates/new.vm");
        });

        // 'Collected' Tab from Home Page
        get("/collected", (rq, rs) -> {
            boolean loggedIn = rq.session().attribute("userID") != null; // Return UserID if value is not NULL
            int userID = loggedIn ? rq.session().attribute("userID") : -1; // if loggedIn is NULL, assign value to prevent Server500 error.
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            PreparedStatement psCollected = conn.prepareStatement
                    ("SELECT a.AmiiboID, a.Name, a.ImageURL, c.UserID, c.Favorited, c.Collected, c.WishList  FROM Collection c JOIN Amiibo a ON c.AmiiboID = a.AmiiboID WHERE c.UserID = ? AND Collected = 'Y' ORDER BY c.ModDate DESC");
            psCollected.setInt(1, userID); // UserID
            ResultSet resultsCollected = psCollected.executeQuery();
            ArrayList<HashMap<String, String>> collectedAmiibo = new ArrayList<HashMap<String, String>>();
            while (resultsCollected.next()) {
                String AmiiboID = resultsCollected.getString("AmiiboID");
                String Name = resultsCollected.getString("Name");
                String ImageURL = resultsCollected.getString("ImageURL");
                String UserID = resultsCollected.getString("UserID");
                String Favorited = resultsCollected.getString("Favorited");
                String Collected = resultsCollected.getString("Collected");
                String WishList = resultsCollected.getString("WishList");
                HashMap<String, String> collected = new HashMap<String, String>();
                collected.put("Name", Name);
                collected.put("AmiiboID", AmiiboID);
                collected.put("ImageURL", ImageURL);
                collected.put("UserID", UserID);
                collected.put("Favorited", Favorited);
                collected.put("Collected", Collected);
                collected.put("WishList", WishList);
                collectedAmiibo.add(collected);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("collectedAmiibo", collectedAmiibo);
            // Add authenticated status to model
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            System.out.println(collectedAmiibo);
            // Pass amiibos to template
            return render(model, "templates/collected.vm");
        });

        // PROFILE for 'Collected'
        get("/profile/collection", (rq, rs) -> {
            boolean loggedIn = rq.session().attribute("userID") != null; // Return UserID if value is not NULL
            int userID = loggedIn ? rq.session().attribute("userID") : -1; // 1=True and 0=False ... if loggedIn is NULL, assign value to prevent Server500 error.
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            PreparedStatement psCollected = conn.prepareStatement
                    ("SELECT a.AmiiboID, a.Name, a.ImageURL, c.UserID, c.Favorited, c.Collected, c.WishList, u.UserName FROM Collection c JOIN Amiibo a ON c.AmiiboID = a.AmiiboID JOIN Users u ON c.UserID = u.UserID WHERE c.UserID = ? AND Collected = 'Y' ORDER BY c.ModDate DESC");
            psCollected.setInt(1, userID); // UserID
            ResultSet resultsCollected = psCollected.executeQuery();
            ArrayList<HashMap<String, String>> collectedAmiibo = new ArrayList<HashMap<String, String>>();
            while (resultsCollected.next()) {
                String AmiiboID = resultsCollected.getString("AmiiboID");
                String Name = resultsCollected.getString("Name");
                String ImageURL = resultsCollected.getString("ImageURL");
                String UserID = resultsCollected.getString("UserID");
                String Favorited = resultsCollected.getString("Favorited");
                String Collected = resultsCollected.getString("Collected");
                String WishList = resultsCollected.getString("WishList");
                String UserName = resultsCollected.getString("UserName");
                HashMap<String, String> collected = new HashMap<String, String>();
                collected.put("Name", Name);
                collected.put("AmiiboID", AmiiboID);
                collected.put("ImageURL", ImageURL);
                collected.put("UserID", UserID);
                collected.put("Favorited", Favorited);
                collected.put("Collected", Collected);
                collected.put("WishList", WishList);
                collected.put("UserName", UserName);
                collectedAmiibo.add(collected);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("collectedAmiibo", collectedAmiibo);
            // Add authenticated status to model
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            System.out.println(collectedAmiibo);
            // Pass amiibos to template
            return render(model, "templates/profile/collection.vm");
        });

        // 'Favorited' Tab from Home Page
        get("/favorited", (rq, rs) -> {
            boolean loggedIn = rq.session().attribute("userID") != null; // Return UserID if value is not NULL
            int userID = loggedIn ? rq.session().attribute("userID") : -1; // 1=True and 0=False ... if loggedIn is NULL, assign value to prevent Server500 error.
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            PreparedStatement selectFavorited = conn.prepareStatement
                    ("SELECT a.AmiiboID, a.Name, a.ImageURL, c.UserID, c.Favorited, c.Collected, c.WishList FROM Collection c JOIN Amiibo a ON c.AmiiboID = a.AmiiboID WHERE c.UserID = ? AND c.Favorited = 'Y' ORDER BY c.ModDate DESC");
            selectFavorited.setInt(1, userID); // UserID
            ResultSet resultsFavorited = selectFavorited.executeQuery();
            ArrayList<HashMap<String, String>> favoritedAmiibo = new ArrayList<HashMap<String, String>>();
            while (resultsFavorited.next()) {
                String AmiiboID = resultsFavorited.getString("AmiiboID");
                String Name = resultsFavorited.getString("Name");
                String ImageURL = resultsFavorited.getString("ImageURL");
                String UserID = resultsFavorited.getString("UserID");
                String Favorited = resultsFavorited.getString("Favorited");
                String Collected = resultsFavorited.getString("Collected");
                String WishList = resultsFavorited.getString("WishList");
                HashMap<String, String> favorited = new HashMap<String, String>();
                favorited.put("Name", Name);
                favorited.put("AmiiboID", AmiiboID);
                favorited.put("ImageURL", ImageURL);
                favorited.put("UserID", UserID);
                favorited.put("Favorited", Favorited);
                favorited.put("Collected", Collected);
                favorited.put("WishList", WishList);
                favoritedAmiibo.add(favorited);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("favoritedAmiibo", favoritedAmiibo);
            // Add authenticated status to model
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            System.out.println(favoritedAmiibo);
            // Pass amiibos to template
            return render(model, "templates/favorited.vm");
        });

        //  PROFILE 'Favorited' Tab
        get("/profile/favorites", (rq, rs) -> {
            boolean loggedIn = rq.session().attribute("userID") != null; // Return UserID if value is not NULL
            int userID = loggedIn ? rq.session().attribute("userID") : -1; // 1=True and 0=False ... if loggedIn is NULL, assign value to prevent Server500 error.
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            PreparedStatement selectFavorited = conn.prepareStatement
                    ("SELECT a.AmiiboID, a.Name, a.ImageURL, c.UserID, c.Favorited, c.Collected, c.WishList, u.UserName FROM Collection c JOIN Amiibo a ON c.AmiiboID = a.AmiiboID JOIN Users u ON c.UserID = u.UserID WHERE c.UserID = ? AND c.Favorited = 'Y' ORDER BY c.ModDate DESC");
            selectFavorited.setInt(1, userID); // UserID
            ResultSet resultsFavorited = selectFavorited.executeQuery();
            ArrayList<HashMap<String, String>> favoritedAmiibo = new ArrayList<HashMap<String, String>>();
            while (resultsFavorited.next()) {
                String AmiiboID = resultsFavorited.getString("AmiiboID");
                String Name = resultsFavorited.getString("Name");
                String ImageURL = resultsFavorited.getString("ImageURL");
                String UserID = resultsFavorited.getString("UserID");
                String Favorited = resultsFavorited.getString("Favorited");
                String Collected = resultsFavorited.getString("Collected");
                String WishList = resultsFavorited.getString("WishList");
                String UserName = resultsFavorited.getString("UserName");
                HashMap<String, String> favorited = new HashMap<String, String>();
                favorited.put("Name", Name);
                favorited.put("AmiiboID", AmiiboID);
                favorited.put("ImageURL", ImageURL);
                favorited.put("UserID", UserID);
                favorited.put("Favorited", Favorited);
                favorited.put("Collected", Collected);
                favorited.put("WishList", WishList);
                favorited.put("UserName", UserName);
                favoritedAmiibo.add(favorited);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("favoritedAmiibo", favoritedAmiibo);
            // Add authenticated status to model
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            System.out.println(favoritedAmiibo);
            // Pass amiibos to template
            return render(model, "templates/profile/favorites.vm");
        });

        // 'Wish List' Tab from Home Page
        get("/wishlist", (rq, rs) -> {
            boolean loggedIn = rq.session().attribute("userID") != null; // Return UserID if value is not NULL
            int userID = loggedIn ? rq.session().attribute("userID") : -1; // 1=True and 0=False ... if loggedIn is NULL, assign value to prevent Server500 error.
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            PreparedStatement selectWishList = conn.prepareStatement
                    ("SELECT a.AmiiboID, a.Name, a.ImageURL, c.UserID, c.Favorited, c.Collected, c.WishList FROM Collection c JOIN Amiibo a ON c.AmiiboID = a.AmiiboID WHERE c.UserID = ? AND c.WishList = 'Y' ORDER BY c.ModDate DESC");            selectWishList.setInt(1, 22); // UserID
            selectWishList.setInt(1, userID); // UserID
            ResultSet resultsWishList = selectWishList.executeQuery();
            ArrayList<HashMap<String, String>> wishlistAmiibo = new ArrayList<HashMap<String, String>>();
            while (resultsWishList.next()) {
                String AmiiboID = resultsWishList.getString("AmiiboID");
                String Name = resultsWishList.getString("Name");
                String ImageURL = resultsWishList.getString("ImageURL");
                String UserID = resultsWishList.getString("UserID");
                String Favorited = resultsWishList.getString("Favorited");
                String Collected = resultsWishList.getString("Collected");
                String WishList = resultsWishList.getString("WishList");
                HashMap<String, String> wishlist = new HashMap<String, String>();
                wishlist.put("Name", Name);
                wishlist.put("AmiiboID", AmiiboID);
                wishlist.put("ImageURL", ImageURL);
                wishlist.put("UserID", UserID);
                wishlist.put("Favorited", Favorited);
                wishlist.put("Collected", Collected);
                wishlist.put("WishList", WishList);
                wishlistAmiibo.add(wishlist);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("wishlistAmiibo", wishlistAmiibo);
            // Add authenticated status to model
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            System.out.println(wishlistAmiibo);
            // Pass amiibos to template
            return render(model, "templates/wishlist.vm");
        });

        // PROFILE 'Wish List' Tab
        get("/profile/wishlist", (rq, rs) -> {
            boolean loggedIn = rq.session().attribute("userID") != null; // Return UserID if value is not NULL
            int userID = loggedIn ? rq.session().attribute("userID") : -1; // 1=True and 0=False ... if loggedIn is NULL, assign value to prevent Server500 error.
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            PreparedStatement selectWishList = conn.prepareStatement
                    ("SELECT a.AmiiboID, a.Name, a.ImageURL, c.UserID, c.Favorited, c.Collected, c.WishList, u.UserID, u.UserName FROM Collection c JOIN Amiibo a ON c.AmiiboID = a.AmiiboID JOIN Users u ON c.UserID = u.UserID WHERE c.UserID = ? AND c.WishList = 'Y' ORDER BY c.ModDate DESC");            selectWishList.setInt(1, 22); // UserID
            selectWishList.setInt(1, userID); // UserID
            ResultSet resultsWishList = selectWishList.executeQuery();
            ArrayList<HashMap<String, String>> wishlistAmiibo = new ArrayList<HashMap<String, String>>();
            while (resultsWishList.next()) {
                String AmiiboID = resultsWishList.getString("AmiiboID");
                String Name = resultsWishList.getString("Name");
                String ImageURL = resultsWishList.getString("ImageURL");
                String UserID = resultsWishList.getString("UserID");
                String Favorited = resultsWishList.getString("Favorited");
                String Collected = resultsWishList.getString("Collected");
                String WishList = resultsWishList.getString("WishList");
                String UserName = resultsWishList.getString("UserName");
                HashMap<String, String> wishlist = new HashMap<String, String>();
                wishlist.put("Name", Name);
                wishlist.put("AmiiboID", AmiiboID);
                wishlist.put("ImageURL", ImageURL);
                wishlist.put("UserID", UserID);
                wishlist.put("Favorited", Favorited);
                wishlist.put("Collected", Collected);
                wishlist.put("WishList", WishList);
                wishlist.put("UserName", UserName);
                wishlistAmiibo.add(wishlist);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("wishlistAmiibo", wishlistAmiibo);
            // Add authenticated status to model
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            System.out.println(wishlistAmiibo);
            // Pass amiibos to template
            return render(model, "templates/profile/wishlist.vm");
        });

        // 'Missing' Tab from Home Page
        get("/missing", (rq, rs) -> {
            boolean loggedIn = rq.session().attribute("userID") != null; // Return UserID if value is not NULL
            int userID = loggedIn ? rq.session().attribute("userID") : -1; // 1=True and 0=False ... if loggedIn is NULL, assign value to prevent Server500 error.
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            PreparedStatement selectMissing = conn.prepareStatement
                    ("SELECT a.AmiiboID, a.Name, a.ImageURL, c.UserID, c.Favorited, c.Collected, c.WishList FROM Amiibo a LEFT JOIN Collection c ON c.AmiiboID = a.AmiiboID WHERE c.AmiiboID IS NULL OR c.UserID = ? AND c.Collected = 'N' OR c.UserID = ? AND c.Collected IS NULL ORDER BY a.AmiiboID ASC");
            selectMissing.setInt(1, userID); // UserID
            selectMissing.setInt(2, userID); // UserID
            ResultSet resultsMissing = selectMissing.executeQuery();

            ArrayList<HashMap<String, String>> missingAmiibo = new ArrayList<HashMap<String, String>>();
            while (resultsMissing.next()) {
                String AmiiboID = resultsMissing.getString("AmiiboID");
                String Name = resultsMissing.getString("Name");
                String ImageURL = resultsMissing.getString("ImageURL");
                String UserID = resultsMissing.getString("UserID");
                String Favorited = resultsMissing.getString("Favorited");
                String Collected = resultsMissing.getString("Collected");
                String WishList = resultsMissing.getString("WishList");
                HashMap<String, String> missing = new HashMap<String, String>();
                missing.put("Name", Name);
                missing.put("AmiiboID", AmiiboID);
                missing.put("ImageURL", ImageURL);
                missing.put("UserID", UserID);
                missing.put("Favorited", Favorited);
                missing.put("Collected", Collected);
                missing.put("WishList", WishList);
                missingAmiibo.add(missing);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("missingAmiibo", missingAmiibo);
            // Add authenticated status to model
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            System.out.println(missingAmiibo);
            // Pass amiibos to template
            return render(model, "templates/missing.vm");
        });

        // 'Coming Soon' Tab from Home Page
        get("/comingsoon", (rq, rs) -> {
            boolean loggedIn = rq.session().attribute("userID") != null; // Return UserID if value is not NULL
            int userID = loggedIn ? rq.session().attribute("userID") : -1; // 1=True and 0=False ... if loggedIn is NULL, assign value to prevent Server500 error.
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            PreparedStatement selectSoon = conn.prepareStatement
                    ("SELECT a.AmiiboID, a.Name, a.ImageURL, a.ReleaseDate, c.Favorited, c.Collected, c.WishList, c.UserID \n" +
                            "FROM Amiibo a LEFT JOIN Collection c ON a.AmiiboID = c.AmiiboID \n" +
                            "WHERE ReleaseDate > CURDATE() \n" +
                            "GROUP BY a.AmiiboID ORDER BY a.ReleaseDate ASC");
            //selectSoon.setInt(1, 22); // UserID
            ResultSet resultsSoon = selectSoon.executeQuery();
            ArrayList<HashMap<String, String>> soonAmiibo = new ArrayList<HashMap<String, String>>();
            while (resultsSoon.next()) {
                String ReleaseDate = resultsSoon.getString("ReleaseDate");
                String AmiiboID = resultsSoon.getString("AmiiboID");
                String Name = resultsSoon.getString("Name");
                String ImageURL = resultsSoon.getString("ImageURL");
                String Favorited = resultsSoon.getString("Favorited");
                String Collected = resultsSoon.getString("Collected");
                String WishList = resultsSoon.getString("WishList");
                String UserID = resultsSoon.getString("UserID");
                HashMap<String, String> unreleased = new HashMap<String, String>();
                unreleased.put("ReleaseDate", ReleaseDate);
                unreleased.put("Name", Name);
                unreleased.put("AmiiboID", AmiiboID);
                unreleased.put("ImageURL", ImageURL);
                unreleased.put("Favorited", Favorited);
                unreleased.put("Collected", Collected);
                unreleased.put("WishList", WishList);
                unreleased.put("UserID", UserID);
                soonAmiibo.add(unreleased);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("soonAmiibo", soonAmiibo);
            // Add authenticated status to model
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            model.put("UserID", userID);
            System.out.println(soonAmiibo);
            // Pass amiibos to template
            return render(model, "templates/comingsoon.vm");
        });

        // Amiibo Collection Count on Profile Page
        get("/count", (rq, rs) -> {
            boolean loggedIn = rq.session().attribute("userID") != null; // Return UserID if value is not NULL
            int userID = loggedIn ? rq.session().attribute("userID") : -1; // 1=True and 0=False ... if loggedIn is NULL, assign value to prevent Server500 error.
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            PreparedStatement selectCount = conn.prepareStatement
                    ("SELECT SUM(CASE WHEN c.UserID = ? AND c.Collected = 'Y' THEN 1 ELSE 0 END) AS MyAmiibo, SUM(CASE WHEN a.AmiiboID IS NOT NULL AND c.UserID IS NULL AND c.Collected = 'N' THEN 0 ELSE 1 END) TotalAmiibo FROM Amiibo a LEFT OUTER JOIN Collection c ON a.AmiiboID = c.AmiiboID");
            selectCount.setInt(1, userID); // UserID
            ResultSet resultsCount = selectCount.executeQuery();
            ArrayList<HashMap<String, String>> countAmiibo = new ArrayList<HashMap<String, String>>();
            while (resultsCount.next()) {
                String MyAmiibo = resultsCount.getString("MyAmiibo");
                String TotalAmiibo = resultsCount.getString("TotalAmiibo");
                HashMap<String, String> count = new HashMap<String, String>();
                count.put("MyAmiibo", MyAmiibo);
                count.put("TotalAmiibo", TotalAmiibo);
                countAmiibo.add(count);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("countAmiibo", countAmiibo);
            System.out.println(countAmiibo);
            // Pass amiibos to template
            return render(model, "templates/count.vm");
        });

        // Amiibo Favorites Count on Profile Page
        get("/countFave", (rq, rs) -> {
            boolean loggedIn = rq.session().attribute("userID") != null; // Return UserID if value is not NULL
            int userID = loggedIn ? rq.session().attribute("userID") : -1; // 1=True and 0=False ... if loggedIn is NULL, assign value to prevent Server500 error.
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            PreparedStatement selectCount = conn.prepareStatement
                    ("SELECT SUM(CASE WHEN c.UserID = ? AND c.Favorited = 'Y' THEN 1 ELSE 0 END) AS MyAmiibo, SUM(CASE WHEN a.AmiiboID IS NOT NULL AND c.UserID IS NULL AND c.Favorited = 'N' THEN 0 ELSE 1 END) TotalAmiibo FROM Amiibo a LEFT OUTER JOIN Collection c ON a.AmiiboID = c.AmiiboID");
            selectCount.setInt(1, userID); // UserID
            ResultSet resultsCount = selectCount.executeQuery();
            ArrayList<HashMap<String, String>> countAmiibo = new ArrayList<HashMap<String, String>>();
            while (resultsCount.next()) {
                String MyAmiibo = resultsCount.getString("MyAmiibo");
                String TotalAmiibo = resultsCount.getString("TotalAmiibo");
                HashMap<String, String> count = new HashMap<String, String>();
                count.put("MyAmiibo", MyAmiibo);
                count.put("TotalAmiibo", TotalAmiibo);
                countAmiibo.add(count);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("countAmiibo", countAmiibo);
            System.out.println(countAmiibo);
            // Pass amiibos to template
            return render(model, "templates/countFave.vm");
        });

        // Amiibo Favorites Count on Profile Page
        get("/countWish", (rq, rs) -> {
            boolean loggedIn = rq.session().attribute("userID") != null; // Return UserID if value is not NULL
            int userID = loggedIn ? rq.session().attribute("userID") : -1; // 1=True and 0=False ... if loggedIn is NULL, assign value to prevent Server500 error.
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null) {
            }
            PreparedStatement selectCount = conn.prepareStatement
                    ("SELECT SUM(CASE WHEN c.UserID = ? AND c.WishList = 'Y' THEN 1 ELSE 0 END) AS MyAmiibo, SUM(CASE WHEN a.AmiiboID IS NOT NULL AND c.UserID IS NULL AND c.WishList = 'N' THEN 0 ELSE 1 END) TotalAmiibo FROM Amiibo a LEFT OUTER JOIN Collection c ON a.AmiiboID = c.AmiiboID");
            selectCount.setInt(1, userID); // UserID
            ResultSet resultsCount = selectCount.executeQuery();
            ArrayList<HashMap<String, String>> countAmiibo = new ArrayList<HashMap<String, String>>();
            while (resultsCount.next()) {
                String MyAmiibo = resultsCount.getString("MyAmiibo");
                String TotalAmiibo = resultsCount.getString("TotalAmiibo");
                HashMap<String, String> count = new HashMap<String, String>();
                count.put("MyAmiibo", MyAmiibo);
                count.put("TotalAmiibo", TotalAmiibo);
                countAmiibo.add(count);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("countAmiibo", countAmiibo);
            System.out.println(countAmiibo);
            // Pass amiibos to template
            return render(model, "templates/countWish.vm");
        });

        // About
        get("/about", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            return render(model, "templates/about.vm");
        });

        // Thanks
        get("/thanks", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            return render(model, "templates/thanks.vm");
        });

        // Sorry
        get("/sorry", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            return render(model, "templates/sorry.vm");
        });

        // Privacy Policy
        get("/privacypolicy", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            return render(model, "templates/privacypolicy.vm");
        });

        // Reset Password
        get("/resetpassword", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            return render(model, "templates/resetpassword.vm");
        });

        // Please Login
        get("/pleaselogin", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            return render(model, "templates/pleaselogin.vm");
        });

        // Terms of Service
        get("/termsofservice", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            return render(model, "templates/termsofservice.vm");
        });

        // Sign Up
        get("/signup", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            return render(model, "templates/signup.vm");
        });

        // Login
        get("/login", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("authenticated", AuthHelper.isAuthenticated(rq));
            return render(model, "templates/login.vm");
        });

        // NavBar
        get("/navbar", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "templates/navbar.vm");
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
            String mine;
            String amiiboID;
            int userID = request.session().attribute("userID");
            amiiboID = request.queryParams("amiiboID");
            mine = request.queryParams("mine");

            if (mine.equals("addAmiibo")) {
                System.out.println("------------------------------------------");
                System.out.println("Add Amiibo Started");
                System.out.println("------------------------------------------");
                System.out.println("Action is " + mine + ". AmiiboID is " + amiiboID + ". Adding to collection.");
                addAmiibo.setAmiiboID(Integer.parseInt(amiiboID));
                addAmiibo.setUserID(userID);
                System.out.println("Main class has an ID of: " + addAmiibo.getAmiiboID());
                addAmiibo.main(args);

            } else if (mine.equals("removeAmiibo")) {
                System.out.println("------------------------------------------");
                System.out.println("Remove Amiibo Started");
                System.out.println("------------------------------------------");
                System.out.println("Action is " + mine + ". AmiiboID is " + amiiboID + ". Removing from collection.");
                removeAmiibo.setAmiiboID(Integer.parseInt(amiiboID));
                removeAmiibo.setUserID(userID);
                System.out.println("Main class has an ID of: " + removeAmiibo.getAmiiboID());
                removeAmiibo.main(args);
            }
            return String.join("Action:" + mine + ". AmiiboID is: " + amiiboID + ".");
        });

        // Favorites (Add/Remove)
        post("/favorites", (request, response) -> {
            AddFavorite addFavorite = new AddFavorite();
            RemoveFavorite removeFavorite = new RemoveFavorite();
            String amiiboID;
            amiiboID = request.queryParams("amiiboID");
            int userID = request.session().attribute("userID");
            String love;
            love = request.queryParams("love");

            if (love.equals("loveAmiibo")) {
                System.out.println("------------------------------------------");
                System.out.println("Favorite Amiibo Started");
                System.out.println("------------------------------------------");
                System.out.println("Value is " + love + ". Adding to Favorites.");
                addFavorite.setAmiiboID(Integer.parseInt(amiiboID));
                addFavorite.setUserID(userID);
                System.out.println("Main class has an ID of: " + addFavorite.getAmiiboID());
                addFavorite.main(args);

            } else if (love.equals("unloveAmiibo")) {
                System.out.println("------------------------------------------");
                System.out.println("Unfavorite Amiibo Started");
                System.out.println("------------------------------------------");
                System.out.println("Value is " + love + ". Removing from Favorites.");
                removeFavorite.setAmiiboID(Integer.parseInt(amiiboID));
                removeFavorite.setUserID(userID);
                System.out.println("Main class has an ID of: " + removeFavorite.getAmiiboID());
                removeFavorite.main(args);
            }
            return String.join(love + ". AmiiboID is: " + amiiboID + ".");
        });

        // WishList (Add/Remove)
        post("/wishlist", (request, response) -> {
            AddWishList addWishList = new AddWishList();
            RemoveWishList removeWishList = new RemoveWishList();
            String amiiboID;
            amiiboID = request.queryParams("amiiboID");
            int userID = request.session().attribute("userID");
            String want;
            want = request.queryParams("want");

            if (want.equals("wantAmiibo")) {
                System.out.println("------------------------------------------");
                System.out.println("WishList Amiibo Started");
                System.out.println("------------------------------------------");
                System.out.println("Value is " + want + ". Adding to WishList.");
                addWishList.setAmiiboID(Integer.parseInt(amiiboID));
                addWishList.setUserID(userID);
                System.out.println("Main class has an ID of: " + addWishList.getAmiiboID());
                addWishList.main(args);

            } else if (want.equals("unwantAmiibo")) {
                System.out.println("------------------------------------------");
                System.out.println("UnWishList Amiibo Started");
                System.out.println("------------------------------------------");
                System.out.println("Value is " + want + ". Removing from WishList.");
                removeWishList.setAmiiboID(Integer.parseInt(amiiboID));
                removeWishList.setUserID(userID);                System.out.println("Main class has an ID of: " + removeWishList.getAmiiboID());
                removeWishList.main(args);
            }
            return String.join(want + ". AmiiboID is: " + amiiboID + ".");
        });

        // SignUp
        post("/newuser", (request, response) -> {
            String name;
            name = request.queryParams("name");
            String userName;
            userName = request.queryParams("userName");
            String email;
            email = request.queryParams("email");
            String password;
            password = request.queryParams("password");
            if (name != null) {
                System.out.println("------------------------------------------");
                System.out.println("User Signup Started");
                System.out.println("------------------------------------------");
                System.out.println("User: " + userName + "\nName: " + name + "\nEmail: " + email + "\nPassword: " + password);
                int userID = AuthHelper.register(email, password, userName, name);
                System.out.println(userID);
                request.session().attribute("userID", userID);
                System.out.println("Sending data to AuthHelper...");
                if(userID != -1) {
                response.redirect("/thanks"); // Take the user to another page
                } else {
                    // TODO Make this more granular and give the user more specific reasoning
                    response.redirect("/sorry"); // Take the user to another page
                }
            }
            return String.join(" / ", name, userName, email, password);
        });

        // Login
        post("/loginuser", (request, response) -> {
            String email;
            email = request.queryParams("email");
            String password;
            password = request.queryParams("password");
            if (email != null) {
                System.out.println("------------------------------------------");
                System.out.println("User Login Started");
                System.out.println("------------------------------------------");
                System.out.println("User: " + email + "\nPassword: " + password);
                int userID = AuthHelper.tryLogin(email, password);
                System.out.println("Sending data to AuthHelper...");
                if (userID != -1) {
                    System.out.println("IF: Logging in");
                    request.session().attribute("userID", userID);
                    response.redirect("/profile/collection"); // Take the user to another page

                } else {
                    System.out.println("ELSE: Not logging in");
                    request.session().removeAttribute("userID");
                    response.redirect("/pleaselogin"); // Take the user to another page
                }
            }
            return String.join(" / ", email, password);
        });

        // Log Out
        post("/signout", (request, response) -> {
            request.session().removeAttribute("userID");
            response.redirect("/"); // Take the user to another page
            return String.join("You are logged out.");
        });

        // Reset Password
        post("/reset", (request, response) -> {
            String email;
            email = request.queryParams("email");
            String password;
            password = request.queryParams("password");
            if (email != null) {
                System.out.println("------------------------------------------");
                System.out.println("Password Reset Started");
                System.out.println("------------------------------------------");
                System.out.println("Email: " + email + "\nNew Password: " + password);
                System.out.println("Sending data to AuthHelper...");
                int resetPassword = AuthHelper.updatePassword(email, password);
                response.redirect("/pleaselogin"); // Take the user to another page
            }
            return String.join(" / ", email, password);
        });
    }
    public static String render(Map<String, Object> model, String templatePath) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}