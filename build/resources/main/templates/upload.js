$(document).ready(function(){
    $("h3").click(function(){
        $(this).hide();
    });


    var upload;
    $("upload").click(function(){
    jQuery.post(http://localhost:8080/upload)
    });

});