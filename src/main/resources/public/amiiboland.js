$(document).ready(function(){
  
    $(".amiibo-image").mouseover(function() {
    console.log("amiibo-image has been moused over");
    $(".overlay").removeClass("is-invisible");
  });

  
  $(".overlay").mouseleave(function() {
    console.log("mouse moved off amiibo-image");
     $(".overlay").addClass("is-invisible");
  });

    $(".star").click(function() {
        console.log("Wish List clicked (star)");
        $(".star").toggleClass("has-text-warning");
    });

    $(".heart").click(function() {
        console.log("Favorite clicked (heart)");
        $(".heart").toggleClass("has-text-danger");
    });

});