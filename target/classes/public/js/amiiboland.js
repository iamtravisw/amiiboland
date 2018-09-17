$(document).ready(function(){

    $('.amiibo-image').mouseover(function() {
        var amiibo = $(this).attr('id');
        var activeAmiiboID = amiibo.split('-')[1];
        console.log("mouse moved on amiibo-image");
        $("#active-"+ activeAmiiboID).removeClass("is-invisible");
    });

    $(".overlay").mouseleave(function() {
        console.log("mouse moved off amiibo-image");
        $(".overlay").addClass("is-invisible");
    });

    // Begin Mouse Over - Enlarge
    $(".star").mouseover(function() {
        console.log("'Mine' moused over (Star)");
        $(".star").addClass("is-size-3");
    });

    $(".star").mouseleave(function() {
        console.log("'Mine' mouse left (Star)");
        $(".star").removeClass("is-size-3");
    });

    $(".heart").mouseover(function() {
        console.log("'Love' moused over (Heart)");
        $(".heart").addClass("is-size-3");
    });

    $(".heart").mouseleave(function() {
        console.log("'Love' mouse left (Heart)");
        $(".heart").removeClass("is-size-3");
    });

    $(".gem").mouseover(function() {
        console.log("'Want' moused over (Gem)");
        $(".gem").addClass("is-size-3");
    });

    $(".gem").mouseleave(function() {
        console.log("'Want' mouse left (Gem)");
        $(".gem").removeClass("is-size-3");
    });
    // End Mouse Over - Enlarge

    $(document).ready(function(){
        $( ".mine" ).change(function() {

         var newValue = $(this).is(':checked') ? "add" : "remove";

            var amiibo = $(this).attr('id');
            var activeAmiiboID = amiibo.split('#')[1];
            var activeAmiiboName = amiibo.split('#')[2];
            $("#star"+activeAmiiboID).toggleClass("has-text-warning");
            console.log( "Handler for .change() called with value: " + newValue );
            console.log("activeAmiiboID is: "+ activeAmiiboID);
            console.log("Sending request to backend");
            if (newValue == 'add') {
                VanillaToasts.create({
                    title: 'Added to Favorites',
                    text: activeAmiiboName + ' has been added to your Collection.',
                    timeout: 4000,
                    type: 'success',
                });
            }
            else if (newValue == 'remove') {
                VanillaToasts.create({
                    title: 'Removed from Favorites',
                    text: activeAmiiboName + ' has been removed from your Collection.',
                    timeout: 4000,
                    type: 'error',
                });
            }
            $.ajax({
                url: '/collection',
                type: 'post',
                data: {
                    mine: newValue + "Amiibo",
                    amiiboID: activeAmiiboID
                },
                success: function () {
                    console.log("Request completed successfully");
                }
            });
        });

        $(document).ready(function(){
            $( ".love" ).change(function() {
                var newValue = $(this).is(':checked') ? "love" : "unlove";
                var amiibo = $(this).attr('id');
                var activeAmiiboID = amiibo.split('#')[1];
                var activeAmiiboName = amiibo.split('#')[2];
                $("#heart"+activeAmiiboID).toggleClass("has-text-danger");
                console.log( "Handler for .change() called with value: " + newValue );
                console.log("activeAmiiboID is: "+ activeAmiiboID);
                console.log("Sending request to backend");
                if (newValue == 'love') {
                    VanillaToasts.create({
                        title: 'Added to Favorites',
                        text: activeAmiiboName + ' has been added to your Favorites.',
                        timeout: 4000,
                        type: 'success',
                    });
                }
                else if (newValue == 'unlove') {
                    VanillaToasts.create({
                        title: 'Removed from Favorites',
                        text: activeAmiiboName + ' has been removed from your Favorites.',
                        timeout: 4000,
                        type: 'error',
                    });
                }
                $.ajax({
                    url: '/favorites',
                    type: 'post',
                    data: {
                        love: newValue + "Amiibo",
                        amiiboID: activeAmiiboID
                    },
                    success: function () {
                        console.log("Request completed successfully");
                    }
                });
            });
        });

        $(document).ready(function(){
            $( ".want" ).change(function() {
                var newValue = $(this).is(':checked') ? "want" : "unwant";
                var amiibo = $(this).attr('id');
                var activeAmiiboID = amiibo.split('#')[1];
                var activeAmiiboName = amiibo.split('#')[2];
                $("#gem"+activeAmiiboID).toggleClass("has-text-info");
                console.log( "Handler for .change() called with value: " + newValue );
                console.log("activeAmiiboID is: "+ activeAmiiboID);
                console.log("Sending request to backend");
                if (newValue == 'want') {
                    VanillaToasts.create({
                        title: 'Added to Favorites',
                        text: activeAmiiboName + ' has been added to your Wish List.',
                        timeout: 4000,
                        type: 'success',
                    });
                }
                else if (newValue == 'unwant') {
                    VanillaToasts.create({
                        title: 'Removed from Favorites',
                        text: activeAmiiboName + ' has been removed from your Wish List.',
                        timeout: 4000,
                        type: 'error',
                    });
                }
                $.ajax({
                    url: '/wishlist',
                    type: 'post',
                    data: {
                        want: newValue + "Amiibo",
                        amiiboID: activeAmiiboID
                    },
                    success: function () {
                        console.log("Request completed successfully");
                    }
                });
            });
        });
    });
});