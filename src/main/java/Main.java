import java.util.Map;
import static spark.Spark.*;
import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.get;
import spark.template.velocity.*;

public class Main {

    public static void main(String[] args) {

        // Instances
        AddAmiibo addAmiibo = new AddAmiibo();
        RemoveAmiibo removeAmiibo = new RemoveAmiibo();
        AddFavorite addFavorite = new AddFavorite();
        RemoveFavorite removeFavorite = new RemoveFavorite();
        AddWishList addWishList = new AddWishList();
        RemoveWishList removeWishList = new RemoveWishList();

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

        // Collection
        post("/collection", (request, response) -> {
            String mine, love, want;
            mine = request.queryParams("mine");

            if (mine.equals("addMario")) {
                System.out.println("Value is " + mine + ". Adding to collection.");
                addAmiibo.main(args);
            } else if (mine.equals("removeMario")) {
                System.out.println("Value is " + mine + ". Removing from collection.");
                removeAmiibo.main(args);
            }
            return String.join(mine);
        });

        // Favorites
        post("/favorites", (request, response) -> {
            String love;
            love = request.queryParams("love");

            if (love.equals("loveMario")) {
                System.out.println("Value is " + love + ". Adding to Favorites.");
                addFavorite.main(args);
            } else if (love.equals("unloveMario")) {
                System.out.println("Value is " + love + ". Removing from Favorites.");
                removeFavorite.main(args);
            }
            return String.join(love);
        });

        // WishList
        post("/wishlist", (request, response) -> {
            String want;
            want = request.queryParams("want");

            if (want .equals("wantMario")) {
                System.out.println("Value is " + want + ". Adding to WishList.");
                addWishList.main(args);
            }
            else if (want .equals("unwantMario")) {
                System.out.println("Value is " + want + ". Removing from WishList.");
                removeWishList.main(args);
            }
            return String.join(want);
        });
    }

    public static String render(Map<String, Object> model, String templatePath) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}


