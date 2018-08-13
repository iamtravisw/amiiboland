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
        console.log("'Mine' clicked (Star)");
        $(".star").toggleClass("has-text-warning");
    });


    $(".heart").click(function() {
        console.log("'Love' clicked (Heart)");
        $(".heart").toggleClass("has-text-danger");
    });

    $(".gem").click(function() {
        console.log("'Want' clicked (Gem)");
        $(".gem").toggleClass("has-text-info");
    });

});


if($('#termsCheck').prop('checked')){}