$(document).ready(function(){
  
    $(".amiibo-image").mouseover(function() {
    console.log("amiibo-image has been moused over");
    $(".modal").addClass("is-active");  
  });

  $(".collected").mouseover(function() {
    console.log("collected switch has been moused over");
    $(".modal").addClass("is-active");  
  });
  
  $(".modal-background").mouseleave(function() {
    console.log("mouse moved off amiibo-image");
     $(".modal").removeClass("is-active");
  });

});