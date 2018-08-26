$(document).ready(function(){

    $(".amiibo-image").mouseover(function() {
    //console.log("amiibo-image has been moused over");
    $(".overlay").removeClass("is-invisible");
  });

    $(".overlay").mouseleave(function() {
    //console.log("mouse moved off amiibo-image");
     $(".overlay").addClass("is-invisible");
  });

    $(".star").mouseover(function() {
        console.log("'Mine' moused over (Star)");
        $(".star").addClass("has-text-warning");
    });

    $(".heart").mouseover(function() {
        console.log("'Love' moused over (Heart)");
        $(".heart").addClass("has-text-danger");
    });

    $(".gem").mouseover(function() {
        console.log("'Want' moused over (Gem)");
        $(".gem").addClass("has-text-info");
    });

    $(".star").mouseleave(function() {
        console.log("'Mine' mouse left (Star)");
        $(".star").removeClass("has-text-warning");
    });

    $(".heart").mouseleave(function() {
        console.log("'Love' mouse left (Heart)");
        $(".heart").removeClass("has-text-danger");
    });

    $(".gem").mouseleave(function() {
        console.log("'Want' mouse left (Gem)");
        $(".gem").removeClass("has-text-info");
    });

    $(document).ready(function(){
        $( "#mine" ).change(function() {
            var newValue = $(this).is(':checked') ? "add" : "remove";
            console.log( "Handler for .change() called with value: " + newValue );
            console.log("Sending request to backend");
            $.ajax({
                url:'/collection',
                type:'post',
                data: { mine: newValue + "Mario"},
                success: function(){
                    console.log("Request completed successfully");
                }
            });
        });
    });

    $(document).ready(function(){
        $( "#love" ).change(function() {
            var newValue = $(this).is(':checked') ? "love" : "unlove";
            console.log( "Handler for .change() called with value: " + newValue );
            console.log("Sending request to backend");
            $.ajax({
                url:'/favorites',
                type:'post',
                data: { love: newValue + "Mario"},
                success: function(){
                    console.log("Request completed successfully");
                }
            });
        });
    });

    $(document).ready(function(){
        $( "#want" ).change(function() {
            var newValue = $(this).is(':checked') ? "want" : "unwant";
            console.log( "Handler for .change() called with value: " + newValue );
            console.log("Sending request to backend");
            $.ajax({
                url:'/wishlist',
                type:'post',
                data: { want: newValue + "Mario"},
                success: function(){
                    console.log("Request completed successfully");
                }
            });
        });
    });
});


