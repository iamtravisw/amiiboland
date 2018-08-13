$(document).ready(function(){

    $(".amiibo-image").mouseover(function() {
    console.log("amiibo-image has been moused over");
    $(".overlay").removeClass("is-invisible");
  });

    $(".overlay").mouseleave(function() {
    console.log("mouse moved off amiibo-image");
     $(".overlay").addClass("is-invisible");
  });

    $(".star").mouseover(function() {
        console.log("'Mine' clicked (Star)");
        $(".star").addClass("has-text-warning");
    });

    $(".heart").mouseover(function() {
        console.log("'Love' clicked (Heart)");
        $(".heart").addClass("has-text-danger");
    });

    $(".gem").mouseover(function() {
        console.log("'Want' clicked (Gem)");
        $(".gem").addClass("has-text-info");
    });


    $(".star").mouseleave(function() {
        console.log("'Mine' clicked (Star)");
        $(".star").removeClass("has-text-warning");
    });

    $(".heart").mouseleave(function() {
        console.log("'Love' clicked (Heart)");
        $(".heart").removeClass("has-text-danger");
    });

    $(".gem").mouseleave(function() {
        console.log("'Want' clicked (Gem)");
        $(".gem").removeClass("has-text-info");
    });

    $(document).ready(function(){
        $( "#mine" ).change(function() {
            var newValue = $(this).is(':checked') ? "1" : "0";
            console.log( "Handler for .change() called with value: " + newValue );
            console.log("Sending request to backend");
            $.ajax({
                url:'/input',
                type:'post',
                data: { mine: newValue},
                success: function(){
                    console.log("Request completed successfully");
                }
            });
        });
    });

});


